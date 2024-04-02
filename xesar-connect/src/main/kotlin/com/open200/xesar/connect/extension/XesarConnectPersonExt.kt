package com.open200.xesar.connect.extension

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.messages.DisengagePeriod
import com.open200.xesar.connect.messages.PersonalLog
import com.open200.xesar.connect.messages.SingleEventResult
import com.open200.xesar.connect.messages.command.*
import com.open200.xesar.connect.messages.event.PersonChanged
import com.open200.xesar.connect.messages.event.PersonCreated
import com.open200.xesar.connect.messages.event.PersonDeleted
import com.open200.xesar.connect.messages.query.Person
import com.open200.xesar.connect.messages.query.QueryList
import java.util.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow

/**
 * Queries the list of persons asynchronously.
 *
 * @param params The query parameters (optional).
 * @param requestConfig The request configuration (optional).
 * @return A deferred object that resolves to a response containing a list of persons.
 */
suspend fun XesarConnect.queryPersonListAsync(
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<QueryList.Response<Person>> {
    return queryListAsync(Person.QUERY_RESOURCE, params, requestConfig)
}

/**
 * Queries a person by ID asynchronously.
 *
 * @param id The ID of the person to query.
 * @param requestConfig The request configuration (optional).
 * @return A deferred object that resolves to the queried person.
 */
suspend fun XesarConnect.queryPersonByIdAsync(
    id: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<Person> {
    return queryElementAsync(Person.QUERY_RESOURCE, id, requestConfig)
}

/**
 * Changes the information of a person asynchronously.
 *
 * @param firstName The first name of the person.
 * @param lastName The last name of the person.
 * @param identifier The identifier of the person.
 * @param externalId The external ID of the person.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.changePersonInformationAsync(
    firstName: String? = null,
    lastName: String? = null,
    identifier: String? = null,
    externalId: String,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): SingleEventResult<PersonChanged> {
    return sendCommandAsync<ChangePersonInformationMapi, PersonChanged>(
        Topics.Command.CHANGE_PERSON_INFORMATION,
        Topics.Event.PERSON_CHANGED,
        true,
        ChangePersonInformationMapi(
            config.uuidGenerator.generateId(), firstName, lastName, identifier, externalId, token),
        requestConfig)
}
/**
 * Creates a person asynchronously.
 *
 * @param firstName The first name of the person.
 * @param lastName The last name of the person.
 * @param identifier The identifier of the person.
 * @param externalId The external ID of the person.
 * @param personId The ID of the person.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.createPersonAsync(
    firstName: String,
    lastName: String,
    identifier: String? = null,
    externalId: String? = null,
    personId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
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
            token),
        requestConfig)
}
/**
 * Deletes a person asynchronously.
 *
 * @param externalId The external ID of the person.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.deletePersonAsync(
    externalId: String? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): SingleEventResult<PersonDeleted> {
    return sendCommandAsync<DeletePersonMapi, PersonDeleted>(
        Topics.Command.DELETE_PERSON,
        Topics.Event.PERSON_DELETED,
        true,
        DeletePersonMapi(config.uuidGenerator.generateId(), externalId, token),
        requestConfig)
}

/**
 * Sets the default authorization profile for a person asynchronously.
 *
 * @param externalId The external ID of the person.
 * @param defaultAuthorizationProfileName The name of the default authorization profile. Set null to
 *   remove authorization profile from person. Empty string gets ignored.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.setDefaultAuthorizationProfileForPersonAsync(
    externalId: String,
    defaultAuthorizationProfileName: String? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): SingleEventResult<PersonChanged> {
    return sendCommandAsync<SetDefaultAuthorizationProfileForPersonMapi, PersonChanged>(
        Topics.Command.SET_DEFAULT_AUTHORIZATION_PROFILE_FOR_PERSON,
        Topics.Event.PERSON_CHANGED,
        true,
        SetDefaultAuthorizationProfileForPersonMapi(
            config.uuidGenerator.generateId(), externalId, defaultAuthorizationProfileName, token),
        requestConfig)
}
/**
 * Sets the default disengage period for a person asynchronously.
 *
 * @param externalId The external ID of the person.
 * @param disengagePeriod The disengage period.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.setDefaultDisengagePeriodForPersonAsync(
    externalId: String,
    disengagePeriod: DisengagePeriod,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): SingleEventResult<PersonChanged> {
    return sendCommandAsync<SetDefaultDisengagePeriodForPersonMapi, PersonChanged>(
        Topics.Command.SET_DEFAULT_DISENGAGE_PERIOD_FOR_PERSON,
        Topics.Event.PERSON_CHANGED,
        true,
        SetDefaultDisengagePeriodForPersonMapi(
            config.uuidGenerator.generateId(), disengagePeriod, externalId, token),
        requestConfig)
}

/**
 * Sets the default logging of personal data for a person asynchronously.
 *
 * @param personalReferenceDuration The default personal reference duration
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.setPersonalReferenceDurationInPersonAsync(
    externalId: String,
    personalReferenceDuration: PersonalLog,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): SingleEventResult<PersonChanged> {
    return sendCommandAsync<SetPersonalReferenceDurationInPersonMapi, PersonChanged>(
        Topics.Command.SET_PERSONAL_REFERENCE_DURATION_IN_PERSON,
        Topics.Event.PERSON_CHANGED,
        true,
        SetPersonalReferenceDurationInPersonMapi(
            config.uuidGenerator.generateId(), personalReferenceDuration, externalId, token),
        requestConfig)
}

fun XesarConnect.queryStreamPerson(
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Flow<Person> {
    return queryStream(Person.QUERY_RESOURCE, params, requestConfig)
}
