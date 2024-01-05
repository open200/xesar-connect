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

            val WITHDRAW_AUTHORIZATION_PROFILE_FROM_MEDIUM =
                "xs3/1/cmd/WithdrawAuthorizationProfileFromMediumMapi"
            val SET_VALIDITY_THRESHOLD_MAPI = "xs3/1/cmd/SetValidityThresholdMapi"
            val SET_VALIDITY_DURATION = "xs3/1/cmd/SetValidityDurationMapi"
            val SET_REPLACEMENT_MEDIUM_DURATION_MAPI = "xs3/1/cmd/SetReplacementMediumDurationMapi"
            val SET_PERSONAL_REFERENCE_DURATION_IN_PERSON =
                "xs3/1/cmd/SetPersonalReferenceDurationInPersonMapi"
            val SET_PERSONAL_REFERENCE_DURATION_FOR_INSTALLATION_POINT =
                "xs3/1/cmd/SetPersonalReferenceDurationForInstallationPointMapi"
            val SET_PERSON_PERSONAL_REFERENCE_DURATION =
                "xs3/1/cmd/SetPersonPersonalReferenceDurationMapi"
            val SET_LABEL_ON_MEDIUM = "xs3/1/cmd/SetLabelOnMediumMapi"
            val SET_INSTALLATION_POINT_PERSONAL_REFERENCE_DURATION =
                "xs3/1/cmd/SetInstallationPointPersonalReferenceDurationMapi"
            val SET_DISENGAGE_PERIOD_ON_MEDIUM = "xs3/1/cmd/SetDisengagePeriodOnMediumMapi"
            val SET_DEFAULT_VALIDITY_DURATION_MAPI = "xs3/1/cmd/SetDefaultValidityDurationMapi"
            val SET_DEFAULT_DISENGAGE_PERIOD_FOR_PERSON =
                "xs3/1/cmd/SetDefaultDisengagePeriodForPersonMapi"
            val SET_DEFAULT_AUTHORIZATION_PROFILE_FOR_PERSON =
                "xs3/1/cmd/SetDefaultAuthorizationProfileForPersonMapi"
            val SET_DAILY_SCHEDULER_EXECUTION_TIME = "xs3/1/cmd/SetDailySchedulerExecutionTimeMapi"
            val SET_ACCESS_END_AT = "xs3/1/cmd/SetAccessEndAtMapi"
            val SET_ACCESS_BEGIN_AT = "xs3/1/cmd/SetAccessBeginAtMapi"
            val REVERT_PREPARE_REMOVAL_OF_EVVA_COMPONENT =
                "xs3/1/cmd/RevertPrepareRemovalOfEvvaComponentMapi"
            val REMOVE_ZONE_AUTHORIZATION_FROM_MEDIUM =
                "xs3/1/cmd/RemoveZoneAuthorizationFromMediumMapi"
            val REMOVE_INSTALLATION_POINT_FROM_ZONE =
                "xs3/1/cmd/RemoveInstallationPointFromZoneMapi"
            val REMOVE_INSTALLATION_POINT_AUTHORIZATION_FROM_MEDIUM =
                "xs3/1/cmd/RemoveInstallationPointAuthorizationFromMediumMapi"
            val PREPARE_REMOVAL_OF_EVVA_COMPONENT = "xs3/1/cmd/PrepareRemovalOfEvvaComponentMapi"
            val LOCK_MEDIUM = "xs3/1/cmd/LockMediumMapi"
            val FORCE_REMOVE_EVVA_COMPONENT = "xs3/1/cmd/ForceRemoveEvvaComponentMapi"
            val DELETE_ZONE = "xs3/1/cmd/DeleteZoneMapi"
            val DELETE_PERSON = "xs3/1/cmd/DeletePersonMapi"
            val DELETE_OFFICE_MODE_TIME_PROFILE = "xs3/1/cmd/DeleteOfficeModeTimeProfileMapi"
            val DELETE_INSTALLATION_POINT = "xs3/1/cmd/DeleteInstallationPointMapi"
            val DELETE_CODING_STATION = "xs3/1/cmd/DeleteCodingStationMapi"
            val DELETE_CALENDAR = "xs3/1/cmd/DeleteCalendarMapi"
            val DELETE_AUTHORIZATION_TIME_PROFILE = "xs3/1/cmd/DeleteAuthorizationTimeProfileMapi"
            val DELETE_AUTHORIZATION_PROFILE = "xs3/1/cmd/DeleteAuthorizationProfileMapi"
            val CREATE_ZONE = "xs3/1/cmd/CreateZoneMapi"
            val CREATE_PERSON = "xs3/1/cmd/CreatePersonMapi"
            val CREATE_OFFICE_MODE_TIME_PROFILE = "xs3/1/cmd/CreateOfficeModeTimeProfileMapi"
            val CREATE_CODING_STATION = "xs3/1/cmd/CreateCodingStationMapi"
            val CREATE_CALENDAR = "xs3/1/cmd/CreateCalendarMapi"
            val CREATE_AUTHORIZATION_TIME_PROFILE = "xs3/1/cmd/CreateAuthorizationTimeProfileMapi"
            val CREATE_AUTHORIZATION_PROFILE = "xs3/1/cmd/CreateAuthorizationProfileMapi"
            val CONFIGURE_RELEASE_DURATION = "xs3/1/cmd/ConfigureReleaseDurationMapi"
            val CONFIGURE_OFFICE_MODE_TIME_PROFILE = "xs3/1/cmd/ConfigureOfficeModeTimeProfileMapi"
            val CONFIGURE_MEDIA_UPGRADE = "xs3/1/cmd/ConfigureMediaUpgradeMapi"
            val CONFIGURE_MANUAL_OFFICE_MODE_AND_SHOP_MODE =
                "xs3/1/cmd/ConfigureManualOfficeModeAndShopModeMapi"
            val CONFIGURE_ASSIGNABLE_AUTHORIZATION_PROFILES =
                "xs3/1/cmd/ConfigureAssignableAuthorizationProfilesMapi"
            val CHANGE_ZONE_DATA = "xs3/1/cmd/ChangeZoneDataMapi"
            val CHANGE_PERSON_INFORMATION = "xs3/1/cmd/ChangePersonInformationMapi"
            val CHANGE_OFFICE_MODE_TIME_PROFILE = "xs3/1/cmd/ChangeOfficeModeTimeProfileMapi"

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
            /**
             * MQTT topic string for the "AddInstallationPointAuthorizationToMediumMapi" command.
             */
            val ADD_INSTALLATION_POINT_AUTHORIZATION_TO_MEDIUM =
                "xs3/1/cmd/AddInstallationPointAuthorizationToMediumMapi"
            /** MQTT topic string for the "AddInstallationPointToZoneMapi" command. */
            val ADD_INSTALLATION_POINT_TO_ZONE = "xs3/1/cmd/AddInstallationPointToZoneMapi"
            /** MQTT topic string for the "AddZoneAuthorizationToMediumMapi" command. */
            val ADD_ZONE_AUTHORIZATION_TO_MEDIUM = "xs3/1/cmd/AddZoneAuthorizationToMediumMapi"
            /** MQTT topic string for the "AssignPersonToMediumMapi" command. */
            val ASSIGN_PERSON_TO_MEDIUM = "xs3/1/cmd/AssignPersonToMediumMapi"
            /** MQTT topic string for the "ChangeAuthorizationTimeProfileMapi" command. */
            val CHANGE_AUTHORIZATION_TIME_PROFILE = "xs3/1/cmd/ChangeAuthorizationTimeProfileMapi"
            /** MQTT topic string for the "ChangeCalendarMapi" command. */
            val CHANGE_CALENDAR = "xs3/1/cmd/ChangeCalendarMapi"
            /** MQTT topic string for the "ChangeCodingStationMapi" command */
            val CHANGE_CODING_STATION = "xs3/1/cmd/ChangeCodingStationMapi"
            /** MQTT topic string for the "ChangeInstallationPointMapi" command */
            val CHANGE_INSTALLATION_POINT = "xs3/1/cmd/ChangeInstallationPointMapi"
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
