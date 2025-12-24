package com.oyenscilik.di

import android.content.Context
import com.oyenscilik.utils.TextToSpeechHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilsModule {

    @Provides
    @Singleton
    fun provideTextToSpeechHelper(
        @ApplicationContext context: Context
    ): TextToSpeechHelper {
        return TextToSpeechHelper(context)
    }
}
