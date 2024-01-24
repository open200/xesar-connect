package com.open200.xesar.connect.extension

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.messages.DisengagePeriod
import com.open200.xesar.connect.messages.PersonalLog
import com.open200.xesar.connect.messages.command.*
import com.open200.xesar.connect.messages.event.PersonChanged
import com.open200.xesar.connect.messages.event.PersonCreated
import com.open200.xesar.connect.messages.event.PersonDeleted
import com.open200.xesar.connect.messages.query.Person
import com.open200.xesar.connect.messages.query.QueryList
import java.util.*
import kotlinx.coroutines.Deferred

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
suspend fun XesarConnect.changePersonInformation(
    firstName: String? = null,
    lastName: String? = null,
    identifier: String? = null,
    externalId: String,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<PersonChanged> {
    return sendCommand<ChangePersonInformationMapi, PersonChanged>(
        Topics.Command.CHANGE_PERSON_INFORMATION,
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
suspend fun XesarConnect.createPerson(
    firstName: String,
    lastName: String,
    identifier: String? = null,
    externalId: String? = null,
    personId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<PersonCreated> {
    return sendCommand<CreatePersonMapi, PersonCreated>(
        Topics.Command.CREATE_PERSON,
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
suspend fun XesarConnect.deletePerson(
    externalId: String? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<PersonDeleted> {
    return sendCommand<DeletePersonMapi, PersonDeleted>(
        Topics.Command.DELETE_PERSON,
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
suspend fun XesarConnect.setDefaultAuthorizationProfileForPerson(
    externalId: String,
    defaultAuthorizationProfileName: String? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<PersonChanged> {
    return sendCommand<SetDefaultAuthorizationProfileForPersonMapi, PersonChanged>(
        Topics.Command.SET_DEFAULT_AUTHORIZATION_PROFILE_FOR_PERSON,
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
suspend fun XesarConnect.setDefaultDisengagePeriodForPerson(
    externalId: String,
    disengagePeriod: DisengagePeriod,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<PersonChanged> {
    return sendCommand<SetDefaultDisengagePeriodForPersonMapi, PersonChanged>(
        Topics.Command.SET_DEFAULT_DISENGAGE_PERIOD_FOR_PERSON,
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
suspend fun XesarConnect.setPersonalReferenceDurationInPerson(
    externalId: String,
    personalReferenceDuration: PersonalLog,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<PersonChanged> {
    return sendCommand<SetPersonalReferenceDurationInPersonMapi, PersonChanged>(
        Topics.Command.SET_PERSONAL_REFERENCE_DURATION_IN_PERSON,
        SetPersonalReferenceDurationInPersonMapi(
            config.uuidGenerator.generateId(), personalReferenceDuration, externalId, token),
        requestConfig)
}
