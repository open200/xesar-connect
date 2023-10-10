package com.open200.xesar.connect.filters

/** Implementation of the MessageFilter interface that accepts all topics and messages. */
class AllTopicsFilter : MessageFilter {

    /**
     * Filters the given topic and message.
     *
     * @param topic The topic of the message.
     * @param message The message payload.
     * @return always `true` whatever the message.
     */
    @Override
    override fun filter(topic: String, message: String): Boolean {
        return true
    }
}
