package com.open200.xesar.connect.filters

/**
 * Implementation of the MessageFilter interface that filters messages based on topic(s).
 *
 * @param topics The list of topics to filter by.
 */
class TopicFilter(private val topics: List<String>) : MessageFilter {

    /**
     * Secondary constructor that creates a TopicFilter with a single topic.
     *
     * @param topic The topic to filter by.
     */
    constructor(topic: String) : this(listOf(topic))

    /**
     * Filters the given topic and message based on the topics in the filter.
     *
     * @param topic The topic of the message.
     * @param message The message payload.
     * @return `true` if the message's topic matches any of the topics in the filter, `false`
     *   otherwise.
     */
    @Override
    override fun filter(topic: String, message: String): Boolean {
        return this.topics.contains(topic)
    }
}
