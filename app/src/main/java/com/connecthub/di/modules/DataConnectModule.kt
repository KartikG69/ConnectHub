package com.connecthub.di.modules

import com.connecthub.data.remote.dataconnect.ContactDataSource
import com.connecthub.data.remote.dataconnect.ConversationDataSource
import com.connecthub.data.remote.dataconnect.DataConnectClient
import com.connecthub.data.remote.dataconnect.MessageDataSource
import com.connecthub.data.remote.dataconnect.UserDataSource
import com.connecthub.data.repository.ContactRepositoryImpl
import com.connecthub.data.repository.ConversationRepositoryImpl
import com.connecthub.domain.repository.ContactRepository
import com.connecthub.domain.repository.ConversationRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataConnectModule {

    @Binds
    abstract fun bindConversationRepository(
        conversationRepositoryImpl: ConversationRepositoryImpl
    ): ConversationRepository

    @Binds
    abstract fun bindContactRepository(
        contactRepositoryImpl: ContactRepositoryImpl
    ): ContactRepository

    companion object {
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

        @Provides
        @Singleton
        fun provideContactDataSource(): ContactDataSource {
            return ContactDataSource()
        }
    }
}