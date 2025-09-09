package com.connecthub.data.remote.dataconnect

import com.connecthub.dataconnect.ConnectHubConnector
import com.connecthub.dataconnect.ConnectHubConnectorInterface
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataConnectClient @Inject constructor() {
    
    fun getConnector(): ConnectHubConnectorInterface = ConnectHubConnector.instance
}