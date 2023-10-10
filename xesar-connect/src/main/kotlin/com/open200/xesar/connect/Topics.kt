package com.open200.xesar.connect

import java.util.UUID

class Topics(vararg val topics: String) {

    companion object {
        /** MQTT topic string representing the wildcard topic for subscribing to all topics. */
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
        }
    }

    class Request {

        companion object {
            /** MQTT topic string for the "Login" request. */
            val LOGIN = "xs3/1/cmd/Login"
            /** MQTT topic string for the "Logout" request. */
            val LOGOUT = "xs3/1/cmd/Logout"
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
