package com.open200.xesar.connect

import com.open200.xesar.connect.filters.MessageFilter

/**
 * Represents a listener for receiving messages from the Xesar API.
 *
 * @property xesarConnect: An instance of XesarApi that the listener is associated with.
 * @property messageHandler: An implementation of the MessageHandler interface that handles the
 *   received messages.
 * @property filter: An implementation of the MessageFilter interface that filters the messages
 *   based on certain criteria.
 */
class Listener(
    val xesarConnect: XesarConnect,
    val messageHandler: MessageHandler,
    val filter: MessageFilter,
) : AutoCloseable {

    fun unregister() {
        xesarConnect.removeListener(this)
    }

    override fun close() {
        unregister()
    }
}
