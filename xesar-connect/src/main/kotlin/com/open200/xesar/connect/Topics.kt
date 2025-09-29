package com.open200.xesar.connect

import com.open200.xesar.connect.messages.query.EventType
import com.open200.xesar.connect.messages.query.GroupOfEvent
import java.util.*

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

            /** MQTT topic string from the "AuthorizationProfileChanged" event. */
            val AUTHORIZATION_PROFILE_CHANGED = "xs3/1/ces/AuthorizationProfileChanged"

            /** MQTT topic string from the "AuthorizationProfileAccessChanged" event. */
            val AUTHORIZATION_PROFILE_ACCESS_CHANGED = "xs3/1/ces/AuthorizationProfileAccessChanged"

            /** MQTT topic string from the "AuthorizationProfileInfoChanged" event. */
            val AUTHORIZATION_PROFILE_INFO_CHANGED = "xs3/1/ces/AuthorizationProfileInfoChanged"

            /** MQTT topic string from the "MediumAddedToInstallation" event. */
            val MEDIUM_ADDED_TO_INSTALLATION = "xs3/1/ces/MediumAddedToInstallation"

            /** MQTT topic string from the "AddMediumToInstallationRequested" event. */
            val ADD_MEDIUM_TO_INSTALLATION_REQUESTED = "xs3/1/ces/AddMediumToInstallationRequested"

            /** MQTT topic string from the "InstallationPointCreated" event. */
            val INSTALLATION_POINT_CREATED = "xs3/1/ces/InstallationPointCreated"

            /** MQTT topic string from the "MediumAuthorizationProfileChanged" event. */
            val MEDIUM_AUTHORIZATION_PROFILE_CHANGED = "xs3/1/ces/MediumAuthorizationProfileChanged"

            /** MQTT topic string from the "EvvaComponentAdded" event. */
            val EVVA_COMPONENT_ADDED = "xs3/1/ces/EvvaComponentAdded"

            /** MQTT topic string from the "AuthorizationProfileWithdrawnFromMedium" event. */
            val AUTHORIZATION_PROFILE_WITHDRAWN_FROM_MEDIUM =
                "xs3/1/ces/AuthorizationProfileWithdrawnFromMedium"

            /** MQTT topic string for the "PartitionChanged" event. */
            val PARTITION_CHANGED = "xs3/1/ces/PartitionChanged"

            /** MQTT topic string for the "MediumChanged" event. */
            val MEDIUM_CHANGED = "xs3/1/ces/MediumChanged"

            /** MQTT topic string for the "PrepareEvvaComponentRemovalReverted" event. */
            val PREPARE_EVVA_COMPONENT_REMOVAL_REVERTED =
                "xs3/1/ces/PrepareEvvaComponentRemovalReverted"

            /** MQTT topic string for the "IndividualAuthorizationsDeleted" event. */
            val INDIVIDUAL_AUTHORIZATIONS_DELETED = "xs3/1/ces/IndividualAuthorizationsDeleted"

            /** MQTT topic string for the "EvvaComponentRemovalPrepared" event. */
            val EVVA_COMPONENT_REMOVAL_PREPARED = "xs3/1/ces/EvvaComponentRemovalPrepared"

            /** MQTT topic string for the "MediumLocked" event. */
            val MEDIUM_LOCKED = "xs3/1/ces/MediumLocked"

            /** MQTT topic string for the "EvvaComponentRemoved" event. */
            val EVVA_COMPONENT_REMOVED = "xs3/1/ces/EvvaComponentRemoved"

            /** MQTT topic string for the "ZoneDeleted" event. */
            val ZONE_DELETED = "xs3/1/ces/ZoneDeleted"

            /** MQTT topic string for the "PersonDeleted" event. */
            val PERSON_DELETED = "xs3/1/ces/PersonDeleted"

            /** MQTT topic string for the "OfficeModeTimeProfileDeleted" event. */
            val OFFICE_MODE_TIME_PROFILE_DELETED = "xs3/1/ces/OfficeModeTimeProfileDeleted"

            /** MQTT topic string for the "InstallationPointDeleted" event. */
            val INSTALLATION_POINT_DELETED = "xs3/1/ces/InstallationPointDeleted"

            /** MQTT topic string for the "CodingStationDeleted" event. */
            val CODING_STATION_DELETED = "xs3/1/ces/CodingStationDeleted"

            /** MQTT topic string for the "CalendarDeleted" event. */
            val CALENDAR_DELETED = "xs3/1/ces/CalendarDeleted"

            /** MQTT topic string for the "AuthorizationTImeProfileDeleted" event. */
            val AUTHORIZATION_TIME_PROFILE_DELETED = "xs3/1/ces/AuthorizationTimeProfileDeleted"

            /** MQTT topic string for the "AuthorizationProfileDeleted" event. */
            val AUTHORIZATION_PROFILE_DELETED = "xs3/1/ces/AuthorizationProfileDeleted"

            /** MQTT topic string for the "ZoneCreated" event. */
            val ZONE_CREATED = "xs3/1/ces/ZoneCreated"

            /** MQTT topic string for the "PersonCreated" event. */
            val PERSON_CREATED = "xs3/1/ces/PersonCreated"

            /** MQTT topic string for the "OfficeModeTimeProfileCreated" event. */
            val OFFICE_MODE_TIME_PROFILE_CREATED = "xs3/1/ces/OfficeModeTimeProfileCreated"

            /** MQTT topic string for the "CodingStationCreated" event. */
            val CODING_STATION_CREATED = "xs3/1/ces/CodingStationCreated"

            /** MQTT topic string for the "CalendarCreated" event. */
            val CALENDAR_CREATED = "xs3/1/ces/CalendarCreated"

            /** MQTT topic string for the "AuthorizationTimeProfileCreated" event. */
            val AUTHORIZATION_TIME_PROFILE_CREATED = "xs3/1/ces/AuthorizationTimeProfileCreated"

            /** MQTT topic string for the "UserGroupChanged" event. */
            val USER_GROUP_CHANGED = "xs3/1/ces/UserGroupChanged"

            /** MQTT topic string for the "ZoneChanged" event. */
            val ZONE_CHANGED = "xs3/1/ces/ZoneChanged"

            /** MQTT topic string for the "PersonChanged" event. */
            val PERSON_CHANGED = "xs3/1/ces/PersonChanged"

            /** MQTT topic string for the "OfficeModeTimeProfileChanged" event. */
            val OFFICE_MODE_TIME_PROFILE_CHANGED = "xs3/1/ces/OfficeModeTimeProfileChanged"

            /** MQTT topic string for the "InstallationPointChanged" event. */
            val INSTALLATION_POINT_CHANGED = "xs3/1/ces/InstallationPointChanged"

            /** MQTT topic string for the "CodingStationChanged" event. */
            val CODING_STATION_CHANGED = "xs3/1/ces/CodingStationChanged"

            /** MQTT topic string for the "CalendarChanged" event. */
            val CALENDAR_CHANGED = "xs3/1/ces/CalendarChanged"

            /** MQTT topic string for the "MediumPersonChanged" event. */
            val MEDIUM_PERSON_CHANGED = "xs3/1/ces/MediumPersonChanged"

            /** MQTT topic string for the "InstallationPointsInZoneChanged" event. */
            val INSTALLATION_POINTS_IN_ZONE_CHANGED = "xs3/1/ces/InstallationPointsInZoneChanged"

            /** MQTT topic string for the "IndividualAuthorizationsAddedToMedium" event. */
            val INDIVIDUAL_AUTHORIZATIONS_ADDED_TO_MEDIUM =
                "xs3/1/ces/IndividualAuthorizationsAddedToMedium"

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

            /** MQTT topic string for the "AuthorizationProfileCreated" event */
            val AUTHORIZATION_PROFILE_CREATED = "xs3/1/ces/AuthorizationProfileCreated"

            /** MQTT topic string for the "AuthorizationTimeProfileChanged" event */
            val AUTHORIZATION_TIME_PROFILE_CHANGED = "xs3/1/ces/AuthorizationTimeProfileChanged"

            /** MQTT topic string for the "PhoneNumberChanged" event */
            val PHONE_NUMBER_CHANGED = "xs3/1/ces/PhoneNumberChanged"

            /** MQTT topic string for the "SmartphoneAddedToInstallation" event */
            val SMARTPHONE_ADDED_TO_INSTALLATION = "xs3/1/ces/SmartphoneAddedToInstallation"

            /** MQTT topic string for the "NewRegistrationCodeRequested" event */
            val NEW_REGISTRATION_CODE_REQUESTED = "xs3/1/ces/NewRegistrationCodeRequested"

            /** MQTT topic string for the "SmartphoneUnregistered" event */
            val SMARTPHONE_UNREGISTERED = "xs3/1/ces/SmartphoneUnregistered"

            /** MQTT topic string for the "SmartphoneAuthorizationsResent" event */
            val SMARTPHONE_AUTHORIZATIONS_RESENT = "xs3/1/ces/SmartphoneAuthorizationsResent"

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

            /**
             * MQTT topic string for the "AccessProtocolEvent" event for a specific [GroupOfEvent]
             * and [EventType]
             */
            fun accessProtocolEventTopic(eventType: EventType): String {
                return "xs3/1/ase/%s/%04X"
                    .format(eventType.groupOfEvent.name, eventType.eventNumber)
            }

            /**
             * MQTT topic string for the "AccessProtocolEvent" event for a specific [GroupOfEvent]
             */
            fun accessProtocolEventTopic(eventGroup: GroupOfEvent): String {
                return "xs3/1/ase/%s/+".format(eventGroup.name)
            }
        }
    }

    class Command {

        companion object {

            /** MQTT topic string for the "ChangeAuthorizationProfileMapi" command. */
            val CHANGE_AUTHORIZATION_PROFILE = "xs3/1/cmd/ChangeAuthorizationProfileMapi"

            /** MQTT topic string for the "RequestAddMediumToInstallationMapi" command. */
            val REQUEST_ADD_MEDIUM_TO_INSTALLATION = "xs3/1/cmd/RequestAddMediumToInstallationMapi"

            /** MQTT topic string for the "CreateInstallationPointMapi" command. */
            val CREATE_INSTALLATION_POINT = "xs3/1/cmd/CreateInstallationPointMapi"

            /** MQTT topic string for the "AssignAuthorizationProfileToMediumMapi" command. */
            val ASSIGN_AUTHORIZATION_PROFILE_TO_MEDIUM =
                "xs3/1/cmd/AssignAuthorizationProfileToMediumMapi"

            /** MQTT topic string for the "AddEvvaComponentMapi" command. */
            val ADD_EVVA_COMPONENT = "xs3/1/cmd/AddEvvaComponentMapi"

            /** MQTT topic string for the "WithdrawAuthorizationProfileFromMediumMapi" command. */
            val WITHDRAW_AUTHORIZATION_PROFILE_FROM_MEDIUM =
                "xs3/1/cmd/WithdrawAuthorizationProfileFromMediumMapi"

            /** MQTT topic string for the "SetValidityThresholdMapi" command. */
            val SET_VALIDITY_THRESHOLD = "xs3/1/cmd/SetValidityThresholdMapi"

            /** MQTT topic string for the "SetValidityDurationMapi" command. */
            val SET_VALIDITY_DURATION = "xs3/1/cmd/SetValidityDurationMapi"

            /** MQTT topic string for the "SetReplacementMediumDurationMapi" command. */
            val SET_REPLACEMENT_MEDIUM_DURATION = "xs3/1/cmd/SetReplacementMediumDurationMapi"

            /** MQTT topic string for the "SetPersonalReferenceDurationInPersonMapi" command. */
            val SET_PERSONAL_REFERENCE_DURATION_IN_PERSON =
                "xs3/1/cmd/SetPersonalReferenceDurationInPersonMapi"

            /**
             * MQTT topic string for the "SetPersonalReferenceDurationForInstallationPointMapi"
             * command.
             */
            val SET_PERSONAL_REFERENCE_DURATION_FOR_INSTALLATION_POINT =
                "xs3/1/cmd/SetPersonalReferenceDurationForInstallationPointMapi"

            /** MQTT topic string for the "SetPersonPersonalReferenceDurationMapi" command. */
            val SET_PERSON_PERSONAL_REFERENCE_DURATION =
                "xs3/1/cmd/SetPersonPersonalReferenceDurationMapi"

            /** MQTT topic string for the "SetLabelOnMediumMapi" command. */
            val SET_LABEL_ON_MEDIUM = "xs3/1/cmd/SetLabelOnMediumMapi"

            /**
             * MQTT topic string for the "SetInstallationPointPersonalReferenceDurationMapi"
             * command.
             */
            val SET_INSTALLATION_POINT_PERSONAL_REFERENCE_DURATION =
                "xs3/1/cmd/SetInstallationPointPersonalReferenceDurationMapi"

            /** MQTT topic string for the "SetDisengagePeriodOnMediumMapi" command. */
            val SET_DISENGAGE_PERIOD_ON_MEDIUM = "xs3/1/cmd/SetDisengagePeriodOnMediumMapi"

            /** MQTT topic string for the "SetDefaultValidityDurationMapi" command. */
            val SET_DEFAULT_VALIDITY_DURATION = "xs3/1/cmd/SetDefaultValidityDurationMapi"

            /** MQTT topic string for the "SetDefaultDisengagePeriodForPersonMapi" command. */
            val SET_DEFAULT_DISENGAGE_PERIOD_FOR_PERSON =
                "xs3/1/cmd/SetDefaultDisengagePeriodForPersonMapi"

            /** MQTT topic string for the "SetDefaultAuthorizationProfileForPersonMapi" command. */
            val SET_DEFAULT_AUTHORIZATION_PROFILE_FOR_PERSON =
                "xs3/1/cmd/SetDefaultAuthorizationProfileForPersonMapi"

            /** MQTT topic string for the "SetDailySchedulerExecutionTimeMapi" command. */
            val SET_DAILY_SCHEDULER_EXECUTION_TIME = "xs3/1/cmd/SetDailySchedulerExecutionTimeMapi"

            /** MQTT topic string for the "SetAccessEndAtMapi" command. */
            val SET_ACCESS_END_AT = "xs3/1/cmd/SetAccessEndAtMapi"

            /** MQTT topic string for the "SetAccessBeginAtMapi" command. */
            val SET_ACCESS_BEGIN_AT = "xs3/1/cmd/SetAccessBeginAtMapi"

            /** MQTT topic string for the "RevertPrepareRemovalOfEvvaComponentMapi" command. */
            val REVERT_PREPARE_REMOVAL_OF_EVVA_COMPONENT =
                "xs3/1/cmd/RevertPrepareRemovalOfEvvaComponentMapi"

            /** MQTT topic string for the "RemoveZoneAuthorizationFromMediumMapi" command. */
            val REMOVE_ZONE_AUTHORIZATION_FROM_MEDIUM =
                "xs3/1/cmd/RemoveZoneAuthorizationFromMediumMapi"

            /** MQTT topic string for the "RemoveInstallationPointFromZoneMapi" command. */
            val REMOVE_INSTALLATION_POINT_FROM_ZONE =
                "xs3/1/cmd/RemoveInstallationPointFromZoneMapi"

            /**
             * MQTT topic string for the "RemoveInstallationPointAuthorizationFromMediumMapi"
             * command.
             */
            val REMOVE_INSTALLATION_POINT_AUTHORIZATION_FROM_MEDIUM =
                "xs3/1/cmd/RemoveInstallationPointAuthorizationFromMediumMapi"

            /** MQTT topic string for the "PrepareRemovalOfEvvaComponentMapi" command. */
            val PREPARE_REMOVAL_OF_EVVA_COMPONENT = "xs3/1/cmd/PrepareRemovalOfEvvaComponentMapi"

            /** MQTT topic string for the "LockMediumMapi" command. */
            val LOCK_MEDIUM = "xs3/1/cmd/LockMediumMapi"

            /** MQTT topic string for the "ForceRemoveEvvaComponentMapi" command. */
            val FORCE_REMOVE_EVVA_COMPONENT = "xs3/1/cmd/ForceRemoveEvvaComponentMapi"

            /** MQTT topic string for the "DeleteZoneMapi" command. */
            val DELETE_ZONE = "xs3/1/cmd/DeleteZoneMapi"

            /** MQTT topic string for the "DeletePersonMapi" command. */
            val DELETE_PERSON = "xs3/1/cmd/DeletePersonMapi"

            /** MQTT topic string for the "DeleteOfficeModeTimeProfileMapi" command. */
            val DELETE_OFFICE_MODE_TIME_PROFILE = "xs3/1/cmd/DeleteOfficeModeTimeProfileMapi"

            /** MQTT topic string for the "DeleteInstallationPointMapi" command. */
            val DELETE_INSTALLATION_POINT = "xs3/1/cmd/DeleteInstallationPointMapi"

            /** MQTT topic string for the "DeleteCodingStationMapi" command. */
            val DELETE_CODING_STATION = "xs3/1/cmd/DeleteCodingStationMapi"

            /** MQTT topic string for the "DeleteCalendarMapi" command. */
            val DELETE_CALENDAR = "xs3/1/cmd/DeleteCalendarMapi"

            /** MQTT topic string for the "DeleteAuthorizationTimeProfileMapi" command. */
            val DELETE_AUTHORIZATION_TIME_PROFILE = "xs3/1/cmd/DeleteAuthorizationTimeProfileMapi"

            /** MQTT topic string for the "DeleteAuthorizationProfileMapi" command. */
            val DELETE_AUTHORIZATION_PROFILE = "xs3/1/cmd/DeleteAuthorizationProfileMapi"

            /** MQTT topic string for the "CreateZoneMapi" command. */
            val CREATE_ZONE = "xs3/1/cmd/CreateZoneMapi"

            /** MQTT topic string for the "CreatePersonMapi" command. */
            val CREATE_PERSON = "xs3/1/cmd/CreatePersonMapi"

            /** MQTT topic string for the "CreateOfficeModeTimeProfileMapi" command. */
            val CREATE_OFFICE_MODE_TIME_PROFILE = "xs3/1/cmd/CreateOfficeModeTimeProfileMapi"

            /** MQTT topic string for the "CreateCodingStationMapi" command. */
            val CREATE_CODING_STATION = "xs3/1/cmd/CreateCodingStationMapi"

            /** MQTT topic string for the "CreateCalendarMapi" command. */
            val CREATE_CALENDAR = "xs3/1/cmd/CreateCalendarMapi"

            /** MQTT topic string for the "CreateAuthorizationTimeProfileMapi" command. */
            val CREATE_AUTHORIZATION_TIME_PROFILE = "xs3/1/cmd/CreateAuthorizationTimeProfileMapi"

            /** MQTT topic string for the "CreateAuthorizationProfileMapi" command. */
            val CREATE_AUTHORIZATION_PROFILE = "xs3/1/cmd/CreateAuthorizationProfileMapi"

            /** MQTT topic string for the "ConfigureReleaseDurationMapi" command. */
            val CONFIGURE_RELEASE_DURATION = "xs3/1/cmd/ConfigureReleaseDurationMapi"

            /** MQTT topic string for the "ConfigureOfficeModeTimeProfileMapi" command. */
            val CONFIGURE_OFFICE_MODE_TIME_PROFILE = "xs3/1/cmd/ConfigureOfficeModeTimeProfileMapi"

            /** MQTT topic string for the "ConfigureMediaUpgradeMapi" command. */
            val CONFIGURE_MEDIA_UPGRADE = "xs3/1/cmd/ConfigureMediaUpgradeMapi"

            /** MQTT topic string for the "ConfigureManualOfficeModeAndShopModeMapi" command. */
            val CONFIGURE_MANUAL_OFFICE_MODE_AND_SHOP_MODE =
                "xs3/1/cmd/ConfigureManualOfficeModeAndShopModeMapi"

            /** MQTT topic string for the "ConfigureAssignableAuthorizationProfilesMapi" command. */
            val CONFIGURE_ASSIGNABLE_AUTHORIZATION_PROFILES =
                "xs3/1/cmd/ConfigureAssignableAuthorizationProfilesMapi"

            /** MQTT topic string for the "ChangeZoneDataMapi" command. */
            val CHANGE_ZONE_DATA = "xs3/1/cmd/ChangeZoneDataMapi"

            /** MQTT topic string for the "ChangePersonInformationMapi" command. */
            val CHANGE_PERSON_INFORMATION = "xs3/1/cmd/ChangePersonInformationMapi"

            /** MQTT topic string for the "ChangeOfficeModeTimeProfileMapi" command. */
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

            /** MQTT topic string for the "SetDefaultSmartphoneValidityDurationMapi" command. */
            val SET_DEFAULT_SMARTPHONE_VALIDITIY_DURATION =
                "xs3/1/cmd/SetDefaultSmartphoneValidityDurationMapi"

            /** MQTT topic string for the "SetPhoneNumberOnSmartphoneMapi" command. */
            val SET_PHONE_NUMBER_ON_SMARTPHONE = "xs3/1/cmd/SetPhoneNumberOnSmartphoneMapi"

            /** MQTT topic string for the "AddSmartphoneToInstallationMapi" command. */
            val ADD_SMARTPHONE_TO_INSTALLATION = "xs3/1/cmd/AddSmartphoneToInstallationMapi"

            /** MQTT topic string for the "RequestNewRegistrationCodeMapi" command. */
            val REQUEST_NEW_REGISTRATION_CODE = "xs3/1/cmd/RequestNewRegistrationCodeMapi"

            /** MQTT topic string for the "SetMessageLanguageOnSmartphoneMapi" command. */
            val SET_MESSAGE_LANGUAGE_ON_SMARTPHONE = "xs3/1/cmd/SetMessageLanguageOnSmartphoneMapi"

            /** MQTT topic string for the "ConfigureBluetoothStateMapi" command. */
            val CONFIGURE_BLUETOOTH_STATE = "xs3/1/cmd/ConfigureBluetoothStateMapi"

            /** MQTT topic string for the "UnregisterSmartphoneMapi" command. */
            val UNREGISTER_SMARTPHONE = "xs3/1/cmd/UnregisterSmartphoneMapi"

            /** MQTT topic string for the "ResendSmartphoneAuthorizationsMapi" command. */
            val RESEND_SMARTPHONE_AUTHORIZATIONS = "xs3/1/cmd/ResendSmartphoneAuthorizationsMapi"

            /** MQTT topic string for the "SetMobileServiceModeMapi" command. */
            val SET_MOBILE_SERVICE_MODE = "xs3/1/cmd/SetMobileServiceModeMapi"

            /** MQTT topic string for the "AddEntityMetadataDefinition" command. */
            val ADD_ENTITY_METADATA_DEFINITION = "xs3/1/cmd/AddEntityMetadataDefinitionMapi"

            /** MQTT topic string for the "DeleteEntityMetadataDefinition" command. */
            val DELETE_ENTITY_METADATA_DEFINITION = "xs3/1/cmd/DeleteEntityMetadataDefinitionMapi"

            /** MQTT topic string for the "RenameEntityMetadataDefinitionMapi" command. */
            val RENAME_ENTITY_METADATA_DEFINITION = "xs3/1/cmd/RenameEntityMetadataDefinitionMapi"
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
