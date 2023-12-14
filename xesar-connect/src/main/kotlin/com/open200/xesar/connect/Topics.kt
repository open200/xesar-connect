package com.open200.xesar.connect

import java.util.UUID

class Topics(vararg val topics: String) {

    companion object {
        /**
         * MQTT topic string representing the wildcard topic for subscribing to all topics.
         *
         * Do not subscribe to ALL_TOPICS as a client during testing if you are internally using
         * on(QueryIdFilter) or on(CommandIdFilter). This can lead to the client receiving its own
         * sent messages due to the lack of configuration (mosquitto.conf) in the Mosquitto test
         * containers.
         */
        const val ALL_TOPICS = "xs3/1/#"
    }
    class Event {

        companion object {
            /**
             * Generates the MQTT topic string for the "LoggedIn" event of a specific user ID.
             *
             * @param userId The UUID of the user.
             * @return The MQTT topic string for the "LoggedIn" event.
             */
            fun loggedIn(userId: UUID): String {
                return "xs3/1/$userId/LoggedIn"
            }
            /** MQTT topic string for the "UnauthorizedLoginAttempt" event. */
            val UNAUTHORIZED_LOGIN_ATTEMPT = "xs3/1/ces/UnauthorizedLoginAttempt"

            /** MQTT topic string for the "LoggedOut" event. */
            val LOGGED_OUT = "xs3/1/ces/LoggedOut"

            /** MQTT topic string for the "RemoteDisengagePerformed" event. */
            val REMOTE_DISENGAGE_PERFORMED = "xs3/1/ces/RemoteDisengagePerformed"

            /** MQTT topic string for the "RemoteDisengagePermanentPerformed" event. */
            val REMOTE_DISENGAGE_PERMANENT_PERFORMED = "xs3/1/ces/RemoteDisengagePermanentPerformed"

            /** MQTT topic string for the "FindComponentPerformed" event. */
            val FIND_COMPONENT_PERFORMED = "xs3/1/ces/FindComponentPerformed"

            /**
             * Generates the MQTT topic string which emits errors for previously received queries or
             * commands.
             *
             * @param userId The UUID of the user.
             * @return The MQTT topic string for errors after receiving queries or commands.
             */
            fun error(userId: UUID): String {
                return "xs3/1/$userId/err"
            }
        }
    }

    class Command {

        companion object {
            /** MQTT topic string for the "Login" command. */
            val LOGIN = "xs3/1/cmd/Login"
            /** MQTT topic string for the "Logout" command. */
            val LOGOUT = "xs3/1/cmd/Logout"

            /** MQTT topic string for the "RemoteDisengage" command. */
            val REMOTE_DISENGAGE = "xs3/1/cmd/RemoteDisengage"
            /** MQTT topic string for the "RemoteDisengagePermanent" command. */
            val REMOTE_DISENGAGE_PERMANENT = "xs3/1/cmd/RemoteDisengagePermanent"
            /** MQTT topic string for the "FindComponent" command. */
            val FIND_COMPONENT = "xs3/1/cmd/FindComponent"
        }
    }

    class Query {

        companion object {
            /** MQTT topic string for general query requests. */
            val REQUEST = "xs3/1/q"

            /**
             * Generates the MQTT topic string for query results of a specific user ID.
             *
             * @param userId The UUID of the user.
             * @return The MQTT topic string for query results.
             */
            fun result(userId: UUID): String {
                return "xs3/1/$userId/q"
            }
        }
    }
}
