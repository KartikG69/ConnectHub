package com.connecthub.di.modules

import com.connecthub.data.remote.dataconnect.ConversationDataSource
import com.connecthub.data.remote.dataconnect.DataConnectClient
import com.connecthub.data.remote.dataconnect.MessageDataSource
import com.connecthub.data.remote.dataconnect.UserDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataConnectModule {
    
    @Provides
    @Singleton
    fun provideUserDataSource(): UserDataSource {
        return UserDataSource()
    }
    
    @Provides
    @Singleton
    fun provideMessageDataSource(): MessageDataSource {
        return MessageDataSource()
    }
    
    @Provides
    @Singleton
    fun provideConversationDataSource(): ConversationDataSource {
        return ConversationDataSource()
    }
}