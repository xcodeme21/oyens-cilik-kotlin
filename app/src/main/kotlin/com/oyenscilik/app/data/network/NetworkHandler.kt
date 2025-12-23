package com.oyenscilik.app.data.network

import com.google.gson.Gson
import com.oyenscilik.app.data.model.ApiErrorResponse
import com.oyenscilik.app.data.model.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Sealed class untuk hasil API call
 */
sealed class NetworkResult<out T> {
    data class Success<T>(val data: T, val message: String = "Success") : NetworkResult<T>()
    data class Error(
        val message: String,
        val code: Int = 0,
        val errorType: ErrorType = ErrorType.UNKNOWN
    ) : NetworkResult<Nothing>()
}

/**
 * Tipe-tipe error yang mungkin terjadi
 */
enum class ErrorType {
    NETWORK,           // Tidak ada koneksi internet
    TIMEOUT,           // Request timeout
    SERVER,            // Error dari server (5xx)
    CLIENT,            // Error dari client (4xx)
    UNAUTHORIZED,      // Token expired / tidak valid
    NOT_FOUND,         // Resource tidak ditemukan
    VALIDATION,        // Validasi gagal
    UNKNOWN            // Error tidak diketahui
}

/**
 * Handler untuk network calls dengan centralized error handling
 */
@Singleton
class NetworkHandler @Inject constructor() {
    
    private val gson = Gson()
    
    /**
     * Execute API call dengan otomatis handle semua error
     * @param maxRetries Jumlah retry jika gagal (default: 0)
     * @param retryDelayMs Delay antar retry dalam ms (default: 1000)
     * @param apiCall Suspend function yang memanggil API
     */
    suspend fun <T> execute(
        maxRetries: Int = 0,
        retryDelayMs: Long = 1000,
        apiCall: suspend () -> Response<ApiResponse<T>>
    ): NetworkResult<T> = withContext(Dispatchers.IO) {
        var lastError: NetworkResult.Error? = null
        
        repeat(maxRetries + 1) { attempt ->
            try {
                val response = apiCall()
                
                if (response.isSuccessful) {
                    val body = response.body()
                    return@withContext if (body != null && body.success) {
                        NetworkResult.Success(body.data, body.message)
                    } else {
                        NetworkResult.Error(
                            message = body?.message ?: "Terjadi kesalahan",
                            code = response.code(),
                            errorType = ErrorType.SERVER
                        )
                    }
                } else {
                    lastError = handleHttpError(response)
                    
                    // Don't retry on client errors (4xx) except timeout
                    if (response.code() in 400..499) {
                        return@withContext lastError!!
                    }
                }
            } catch (e: Exception) {
                lastError = handleException(e)
                
                // Don't retry on non-recoverable errors
                if (lastError?.errorType == ErrorType.UNAUTHORIZED) {
                    return@withContext lastError!!
                }
            }
            
            // Delay before retry
            if (attempt < maxRetries) {
                delay(retryDelayMs * (attempt + 1)) // Exponential backoff
            }
        }
        
        lastError ?: NetworkResult.Error("Terjadi kesalahan tidak diketahui")
    }
    
    /**
     * Parse error dari HTTP response
     */
    private fun <T> handleHttpError(response: Response<ApiResponse<T>>): NetworkResult.Error {
        val errorBody = response.errorBody()?.string()
        val errorResponse = try {
            gson.fromJson(errorBody, ApiErrorResponse::class.java)
        } catch (e: Exception) {
            null
        }
        
        val message = errorResponse?.message ?: getDefaultErrorMessage(response.code())
        val errorType = when (response.code()) {
            401 -> ErrorType.UNAUTHORIZED
            403 -> ErrorType.UNAUTHORIZED
            404 -> ErrorType.NOT_FOUND
            422 -> ErrorType.VALIDATION
            in 400..499 -> ErrorType.CLIENT
            in 500..599 -> ErrorType.SERVER
            else -> ErrorType.UNKNOWN
        }
        
        return NetworkResult.Error(
            message = message,
            code = response.code(),
            errorType = errorType
        )
    }
    
    /**
     * Handle exception dari network call
     */
    private fun handleException(e: Exception): NetworkResult.Error {
        return when (e) {
            is UnknownHostException -> NetworkResult.Error(
                message = "Tidak ada koneksi internet",
                errorType = ErrorType.NETWORK
            )
            is SocketTimeoutException -> NetworkResult.Error(
                message = "Koneksi timeout, coba lagi",
                errorType = ErrorType.TIMEOUT
            )
            is IOException -> NetworkResult.Error(
                message = "Gagal terhubung ke server",
                errorType = ErrorType.NETWORK
            )
            is HttpException -> NetworkResult.Error(
                message = "Error server: ${e.code()}",
                code = e.code(),
                errorType = ErrorType.SERVER
            )
            else -> NetworkResult.Error(
                message = e.message ?: "Terjadi kesalahan",
                errorType = ErrorType.UNKNOWN
            )
        }
    }
    
    /**
     * Default error message berdasarkan HTTP status code
     */
    private fun getDefaultErrorMessage(code: Int): String = when (code) {
        400 -> "Permintaan tidak valid"
        401 -> "Sesi telah berakhir, silakan login kembali"
        403 -> "Akses ditolak"
        404 -> "Data tidak ditemukan"
        422 -> "Data tidak valid"
        429 -> "Terlalu banyak permintaan, coba lagi nanti"
        500 -> "Terjadi kesalahan pada server"
        502 -> "Server sedang tidak tersedia"
        503 -> "Layanan sedang maintenance"
        else -> "Terjadi kesalahan (Kode: $code)"
    }
}

/**
 * Extension untuk kemudahan penggunaan
 */
inline fun <T> NetworkResult<T>.onSuccess(action: (T) -> Unit): NetworkResult<T> {
    if (this is NetworkResult.Success) action(data)
    return this
}

inline fun <T> NetworkResult<T>.onError(action: (NetworkResult.Error) -> Unit): NetworkResult<T> {
    if (this is NetworkResult.Error) action(this)
    return this
}

fun <T> NetworkResult<T>.getOrNull(): T? = when (this) {
    is NetworkResult.Success -> data
    is NetworkResult.Error -> null
}

fun <T> NetworkResult<T>.getOrDefault(default: T): T = when (this) {
    is NetworkResult.Success -> data
    is NetworkResult.Error -> default
}
