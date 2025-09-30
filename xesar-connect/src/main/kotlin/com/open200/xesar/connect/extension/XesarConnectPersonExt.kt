package com.open200.xesar.connect.extension

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.messages.DisengagePeriod
import com.open200.xesar.connect.messages.EntityMetadata
import com.open200.xesar.connect.messages.PersonalLog
import com.open200.xesar.connect.messages.SingleEventResult
import com.open200.xesar.connect.messages.command.*
import com.open200.xesar.connect.messages.event.PersonChanged
import com.open200.xesar.connect.messages.event.PersonCreated
import com.open200.xesar.connect.messages.event.PersonDeleted
import com.open200.xesar.connect.messages.query.Person
import com.open200.xesar.connect.messages.query.QueryList
import java.util.*
import kotlinx.coroutines.flow.Flow

/**
 * Queries the list of persons asynchronously.
 *
 * @param params The query parameters (optional).
 * @param requestConfig The request configuration (optional).
 * @return A response object containing a list of persons.
 */
suspend fun XesarConnect.queryPersons(
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): QueryList.Response<Person> {
    return handleQueryListFunction { queryListAsync(Person.QUERY_RESOURCE, params, requestConfig) }
}

/**
 * Queries a person by ID asynchronously.
 *
 * @param id The ID of the person to query.
 * @param requestConfig The request configuration (optional).
 * @return A person.
 */
suspend fun XesarConnect.queryPersonById(
    id: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): Person? {
    return handleQueryElementFunction {
        queryElementAsync(Person.QUERY_RESOURCE, id, requestConfig)
    }
}

suspend fun XesarConnect.queryPersonByExternalId(externalId: String): Person? {
    return queryPersons(
            Query.Params(
                filters =
                    listOf(
                        Query.Params.Filter(
                            field = "externalId",
                            value = externalId,
                            type = FilterType.EQ,
                        )
                    )
            )
        )
        .data
        .firstOrNull()
}

/**
 * Changes the information of a person asynchronously.
 *
 * @param firstName The first name of the person.
 * @param lastName The last name of the person.
 * @param identifier The identifier of the person.
 * @param externalId The external ID of the person. Either externalId and/or id needs to be filled
 *   (optional).
 * @param id The ID of the person. Either externalId and/or id needs to be filled (optional).
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.changePersonInformationAsync(
    firstName: String,
    lastName: String,
    identifier: String,
    externalId: String? = null,
    id: UUID? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): SingleEventResult<PersonChanged> {
    return sendCommandAsync<ChangePersonInformationMapi, PersonChanged>(
        Topics.Command.CHANGE_PERSON_INFORMATION,
        Topics.Event.PERSON_CHANGED,
        true,
        ChangePersonInformationMapi(
            config.uuidGenerator.generateId(),
            firstName,
            lastName,
            identifier,
            externalId,
            id,
            token,
        ),
        requestConfig,
    )
}

/**
 * Creates a person asynchronously.
 *
 * @param firstName The first name of the person.
 * @param lastName The last name of the person.
 * @param identifier The identifier of the person.
 * @param externalId The external ID of the person (optional).
 * @param personId The ID of the person.
 * @param entityMetadata Contains the information for all custom data values for the person.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.createPersonAsync(
    firstName: String,
    lastName: String,
    identifier: String? = null,
    externalId: String? = null,
    personId: UUID,
    entityMetadata: List<EntityMetadata>? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): SingleEventResult<PersonCreated> {
    return sendCommandAsync<CreatePersonMapi, PersonCreated>(
        Topics.Command.CREATE_PERSON,
        Topics.Event.PERSON_CREATED,
        true,
        CreatePersonMapi(
            config.uuidGenerator.generateId(),
            firstName,
            lastName,
            identifier,
            externalId,
            personId,
            entityMetadata,
            token,
        ),
        requestConfig,
    )
}

/**
 * Deletes a person asynchronously.
 *
 * @param externalId The external ID of the person. Either externalId and/or id needs to be filled
 *   (optional).
 * @param id The ID of the person. Either externalId and/or id needs to be filled (optional).
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.deletePersonAsync(
    externalId: String? = null,
    id: UUID? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): SingleEventResult<PersonDeleted> {
    return sendCommandAsync<DeletePersonMapi, PersonDeleted>(
        Topics.Command.DELETE_PERSON,
        Topics.Event.PERSON_DELETED,
        true,
        DeletePersonMapi(config.uuidGenerator.generateId(), externalId, id, token),
        requestConfig,
    )
}

/**
 * Sets the default authorization profile for a person asynchronously.
 *
 * @param externalId The external ID of the person. Either externalId and/or id needs to be filled
 *   (optional).
 * @param id The ID of the person. Either externalId and/or id needs to be filled (optional).
 * @param defaultAuthorizationProfileName The name of the default authorization profile. Set null to
 *   remove authorization profile from person. Empty string gets ignored.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.setDefaultAuthorizationProfileForPersonAsync(
    externalId: String? = null,
    id: UUID? = null,
    defaultAuthorizationProfileName: String? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): SingleEventResult<PersonChanged> {
    return sendCommandAsync<SetDefaultAuthorizationProfileForPersonMapi, PersonChanged>(
        Topics.Command.SET_DEFAULT_AUTHORIZATION_PROFILE_FOR_PERSON,
        Topics.Event.PERSON_CHANGED,
        true,
        SetDefaultAuthorizationProfileForPersonMapi(
            config.uuidGenerator.generateId(),
            externalId,
            id,
            defaultAuthorizationProfileName,
            token,
        ),
        requestConfig,
    )
}

/**
 * Sets the default disengage period for a person asynchronously.
 *
 * @param externalId The external ID of the person. Either externalId and/or id needs to be filled
 *   (optional).
 * @param id The ID of the person. Either externalId and/or id needs to be filled (optional).
 * @param disengagePeriod The disengage period.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.setDefaultDisengagePeriodForPersonAsync(
    externalId: String? = null,
    id: UUID? = null,
    disengagePeriod: DisengagePeriod,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): SingleEventResult<PersonChanged> {
    return sendCommandAsync<SetDefaultDisengagePeriodForPersonMapi, PersonChanged>(
        Topics.Command.SET_DEFAULT_DISENGAGE_PERIOD_FOR_PERSON,
        Topics.Event.PERSON_CHANGED,
        true,
        SetDefaultDisengagePeriodForPersonMapi(
            config.uuidGenerator.generateId(),
            disengagePeriod,
            externalId,
            id,
            token,
        ),
        requestConfig,
    )
}

/**
 * Sets the default logging of personal data for a person asynchronously.
 *
 * @param externalId The external ID of the person. Either externalId and/or id needs to be filled
 *   (optional).
 * @param id The ID of the person. Either externalId and/or id needs to be filled (optional).
 * @param personalReferenceDuration The default personal reference duration
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.setPersonalReferenceDurationInPersonAsync(
    externalId: String? = null,
    id: UUID? = null,
    personalReferenceDuration: PersonalLog,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): SingleEventResult<PersonChanged> {
    return sendCommandAsync<SetPersonalReferenceDurationInPersonMapi, PersonChanged>(
        Topics.Command.SET_PERSONAL_REFERENCE_DURATION_IN_PERSON,
        Topics.Event.PERSON_CHANGED,
        true,
        SetPersonalReferenceDurationInPersonMapi(
            config.uuidGenerator.generateId(),
            personalReferenceDuration,
            externalId,
            id,
            token,
        ),
        requestConfig,
    )
}

/**
 * Changes the value of custom data field of a person asynchronously.
 *
 * @param externalId The external ID of the person. Either externalId and/or id needs to be filled
 *   (optional).
 * @param id The ID of the person. Either externalId and/or id needs to be filled (optional).
 * @param metadataId The metadataID of the data field.
 * @param value The new value of the field.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.changePersonMetadataValueAsync(
    externalId: String? = null,
    id: UUID? = null,
    metadataId: UUID,
    value: String,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): SingleEventResult<PersonChanged> {
    return sendCommandAsync<ChangePersonMetadataValueMapi, PersonChanged>(
        Topics.Command.CHANGE_PERSON_METADATA_VALUE,
        Topics.Event.PERSON_CHANGED,
        true,
        ChangePersonMetadataValueMapi(
            config.uuidGenerator.generateId(),
            externalId,
            id,
            metadataId,
            value,
            token,
        ),
        requestConfig,
    )
}

/**
 * Retrieves a cold stream of [Person] objects, fetching them incrementally in smaller,more
 * manageable chunks rather than retrieving the entire dataset at once. Use [Query.Params.pageLimit]
 * to choose the size of one chunk. Use [Query.Params.pageOffset] to choose at which offset to
 * start.
 *
 * @param params The query parameters (optional).
 * @param requestConfig The request configuration (optional).
 * @return A cold flow of [Person] objects
 */
fun XesarConnect.queryStreamPerson(
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): Flow<Person> {
    return queryStream(Person.QUERY_RESOURCE, params, requestConfig)
}
