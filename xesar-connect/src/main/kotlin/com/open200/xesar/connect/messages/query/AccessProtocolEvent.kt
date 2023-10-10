package com.open200.xesar.connect.messages.query

import QueryListResource
import com.open200.xesar.connect.exception.ParsingException
import com.open200.xesar.connect.utils.LocalDateTimeSerializer
import com.open200.xesar.connect.utils.UUIDSerializer
import java.time.LocalDateTime
import java.util.UUID
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

/**
 * Represents an access protocol in the system.
 *
 * @param id The unique identifier of the access protocol event.
 * @param entryIdentificator The entry identifier of the access protocol event.
 * @param timestampComponentLocal The local timestamp component of the access protocol event.
 * @param timestampUtc The UTC timestamp of the access protocol event.
 * @param receivedAt The timestamp when the access protocol event was received.
 * @param eventType The type of the access protocol event.
 * @param eventValue The value of the access protocol event.
 * @param rawValue The raw value of the access protocol event (optional).
 * @param parameterMap The parameter map of the access protocol event (optional).
 * @param installationPointId The unique identifier of the installation point associated with the
 *   access protocol event.
 * @param installationPointName The name of the installation point associated with the access
 *   protocol event.
 * @param installationPointIdentifier The identifier of the installation point associated with the
 *   access protocol event.
 * @param zoneIds The list of zone identifiers associated with the access protocol event.
 * @param accessId The access ID of the access protocol event.
 * @param groupOfEvent The group of the access protocol event.
 * @param eventNumber The number of the access protocol event.
 * @param identificationMediumId The unique identifier of the identification medium associated with
 *   the access protocol event (optional).
 * @param identificationMediumLabel The label of the identification medium associated with the
 *   access protocol event (optional).
 * @param ttl The time-to-live value of the access protocol event (optional).
 * @param personId The unique identifier of the person associated with the access protocol event
 *   (optional).
 * @param person The name of the person associated with the access protocol event (optional).
 */
@Serializable
data class AccessProtocolEvent(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val entryIdentificator: Int? = null,
    @Serializable(with = LocalDateTimeSerializer::class)
    val timestampComponentLocal: LocalDateTime? = null,
    @Serializable(with = LocalDateTimeSerializer::class) val timestampUtc: LocalDateTime? = null,
    @Serializable(with = LocalDateTimeSerializer::class) val receivedAt: LocalDateTime? = null,
    val eventType: EventType? = null,
    val eventValue: EventValue? = null,
    val rawValue: String? = null,
    val parameterMap: ParameterMap? = null,
    @Serializable(with = UUIDSerializer::class) val installationPointId: UUID? = null,
    val installationPointName: String? = null,
    val installationPointIdentifier: String? = null,
    val zoneIds: List<@Serializable(with = UUIDSerializer::class) UUID>? = null,
    val accessId: Int? = null,
    val groupOfEvent: String? = null,
    val eventNumber: Int? = null,
    @Serializable(with = UUIDSerializer::class) val identificationMediumId: UUID? = null,
    val mediumIdentifier: Int? = null,
    val identificationMediumLabel: String? = null,
    @Serializable(with = LocalDateTimeSerializer::class) val ttl: LocalDateTime? = null,
    @Serializable(with = UUIDSerializer::class) val personId: UUID? = null,
    val person: String? = null,
) : QueryListResource {

    /**
     * Represents the event value of the access protocol.
     *
     * @param raw The raw value of the event value (optional).
     * @param officeModeAllowed Indicates if office mode is allowed (optional).
     * @param shopModeActivated Indicates if shop mode is activated (optional).
     * @param timestampFrom The starting timestamp (optional).
     * @param fwVersionChanges The firmware version changes (optional).
     * @param mediumIdentifier The medium identifier (optional).
     * @param errorCodes The error codes (optional).
     * @param errorReaction The error reaction (optional).
     * @param initializationReason The initialization reason (optional).
     * @param mediaRestrictedReason The media restricted reason (optional).
     * @param accessId The access ID (optional).
     * @param timestampTo The ending timestamp (optional).
     * @param errorModule The error module (optional).
     * @param inputState The input state (optional).
     * @param keyType The key type (optional).
     * @param mediaChangedReason The media changed reason (optional).
     * @param componentType The component type (optional).
     * @param inputNr The input number (optional).
     * @param nonceInHardware The nonce in hardware (optional).
     * @param fwUpdatePerformed Indicates if firmware update was performed (optional).
     * @param deltablacklistUid The delta blacklist UID (optional).
     * @param fwUpdateStatus The firmware update status (optional).
     * @param nonceInSoftware The nonce in software (optional).
     * @param errorLocation The error location (optional).
     * @param startingUp The starting up status (optional).
     * @param errorNumber The error number (optional).
     * @param doorOpening Indicates if the door is opening (optional).
     * @param mediaUpgrade Indicates if media upgrade is performed (optional).
     */
    @Serializable
    data class EventValue(
        val raw: String? = null,
        val officeModeAllowed: Boolean? = null,
        val shopModeActivated: Boolean? = null,
        @Serializable(with = LocalDateTimeSerializer::class)
        val timestampFrom: LocalDateTime? = null,
        val fwVersionChanges: String? = null,
        val mediumIdentifier: Int? = null,
        val errorCodes: String? = null,
        val errorReaction: Int? = null,
        val initializationReason: Int? = null,
        val mediaRestrictedReason: Int? = null,
        val accessId: Int? = null,
        @Serializable(with = LocalDateTimeSerializer::class) val timestampTo: LocalDateTime? = null,
        val errorModule: Int? = null,
        val inputState: Int? = null,
        val keyType: Int? = null,
        val mediaChangedReason: Int? = null,
        val componentType: ComponentType? = null,
        val inputNr: Int? = null,
        val nonceInHardware: Long? = null,
        val fwUpdatePerformed: Boolean? = null,
        val deltablacklistUid: Int? = null,
        val fwUpdateStatus: String? = null,
        val nonceInSoftware: Long? = null,
        val errorLocation: Int? = null,
        val startingUp: String? = null,
        val errorNumber: String? = null,
        val doorOpening: Boolean? = null,
        val mediaUpgrade: Boolean? = null,
    )

    /**
     * Represents the parameter map of the access protocol.
     *
     * @param raw The raw value of the parameter map (optional).
     * @param keyType The key type (optional).
     * @param doorId The door ID (optional).
     * @param permanentOpeningAllowed Indicates if permanent opening is allowed (optional).
     * @param shopModeActivated Indicates if shop mode is activated (optional).
     */
    @Serializable
    data class ParameterMap(
        val raw: String? = null,
        val keyType: String? = null,
        val doorId: String? = null,
        val permanentOpeningAllowed: String? = null,
        val shopModeActivated: String? = null
    )

    companion object {
        const val QUERY_RESOURCE = "access-protocol"

        /**
         * Returns an [AccessProtocolEvent] object from a JSON string.
         *
         * @param json The JSON string to parse.
         * @return The [AccessProtocolEvent] object.
         */
        fun decode(json: String): AccessProtocolEvent {
            try {
                return jsonFormat.decodeFromString(json)
            } catch (e: Exception) {
                logger.warn("Couldn't parse $json", e)
                throw ParsingException()
            }
        }

        /**
         * Returns a JSON string from an [AccessProtocolEvent] object.
         *
         * @param accessProtocol The [AccessProtocolEvent] object to parse.
         * @return The JSON string.
         */
        fun encode(accessProtocol: AccessProtocolEvent): String {
            try {
                return jsonFormat.encodeToString(accessProtocol)
            } catch (e: Exception) {
                logger.warn("Couldn't parse $accessProtocol", e)
                throw ParsingException()
            }
        }
    }
}
