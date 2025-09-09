package com.connecthub.di.modules

import android.content.Context
import com.connecthub.data.local.media.LocalMediaManager
import com.connecthub.data.remote.dataconnect.DataConnectClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideDataConnectClient(): DataConnectClient {
        return DataConnectClient()
    }
    
    @Provides
    @Singleton
    fun provideLocalMediaManager(
        @ApplicationContext context: Context
    ): LocalMediaManager {
        return LocalMediaManager(context)
    }
}