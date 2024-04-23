# Xesar-Connect Kotlin Library: MQTT Communication Wrapper for EVVA Xesar Integration

The **Xesar-Connect** library is an open-source Kotlin wrapper designed to simplify MQTT communication for seamless integration of the EVVA Xesar access control system with other software applications. This library streamlines the process of exchanging data and commands between your software and the EVVA Xesar system using the MQTT protocol.

## Features

- **Simplified Integration:** Xesar-Connect offers an intuitive interface that abstracts the complexities of MQTT communication, enabling developers to focus on seamlessly integrating Xesar functionality into their Kotlin applications.

- **Bidirectional Communication:** Xesar-Connect supports both dispatching commands to the Xesar system (e.g., granting access, revoking access) and receiving real-time events from the Xesar system (e.g., changes in door status, access attempts).

- **Event Subscription:** Subscribe to specific events, such as door status changes, access attempts, and system notifications. This empowers your application to promptly respond to events generated by the Xesar system.

- **Customizable:** Easily configure MQTT settings, including broker address, port, and security credentials, to align with your specific deployment environment.

## Installation

You can add Xesar-Connect to your project by including it as a dependency in your Kotlin project. Simply add the following to your `build.gradle.kts` file:


## Compatibility

Xesar-Connect is tested with EVVA's Xesar version 3.1 with Mqtt API version 1.2.1 see [EVVA Xesar Mqtt API](https://integrations.api.xesar.evva.com/)


```kotlin
dependencies {
    implementation("com.open200:xesar-connect:1.0.0")
}
```

## Usage

Integrating Xesar-Connect into your Kotlin project is straightforward. Here's a basic example to help you get started:

```kotlin
fun main() {

    // add a security provider
    Security.addProvider(BouncyCastleProvider())

    runBlocking {

        // path to the zip file containing the certificates
        val pathToZip = Path("mqtt-cert.zip")

        // connect to the MQTT broker and login to xesar
        val xesar = XesarConnect.connectAndLoginAsync(Config.configureFromZip(pathToZip)).await()

        // Subscribe to topics ALL_TOPICS (or the topics you are interested in)
        xesar.subscribeAsync(Topics(Topics.ALL_TOPICS)).await()

        // send command to create a person
        val personId = UUID.randomUUID()
        xesar.createPersonAsync(
            firstName = "Ford",
            lastName = "Prefect",
            identifier = "fprefect",
            externalId = "fprefect",
            personId = personId
        ).await()

        // query all persons
        val persons = xesar.queryPersons()
        log.info { "Received person list: ${persons.data}" }

        // query one person by id
        val person = xesar.queryPersonById(personId)
        person?.let {
            // send command to delete this person
            xesar.deletePersonAsync(person.externalId).await()
        }

        // fetching persons incrementally in smaller,more manageable chunks
        val chunkSize = 15
        xesar.queryStreamPerson(Query.Params(pageLimit =chunkSize)).collect{
            log.info { "person: ${it.firstName}, ${it.lastName}" }
        }
        
        // subscribe to the access event and listen to battery warnings. You can use the provided enums for the events from the library
        val batteryEmptyTopic = Topics.Event.accessProtocolEventTopic(GroupOfEvent.EvvaComponent, EventType.BATTERY_EMPTY)
        xesar.on(TopicFilter(batteryEmptyTopic)) {
            val event = AccessProtocolEvent.decode(it.message)
            log.info { "Received battery warning from installation point identifier: ${event.installationPointIdentifier}" }
        }

        // suspend the coroutine to keep the connection to xesar open
        xesar.delay()
    }
}
```

For more comprehensive information and advanced usage, please refer to the [documentation](https://github.com/open200/xesar-connect/blob/main/docs/usage.md).
Also see our [Xesar-Connect-Kotlin-Demo](https://github.com/open200/xesar-connect-kotlin-demo) app 

## Building From Source

If you're a developer interested in building Xesar-Connect from source, follow these steps:

1. Clone the repository: `git clone https://github.com/open200/xesar-connect.git`
2. Navigate to the project directory: `cd xesar-connect`
3. Build the library: `./gradlew build`
4. The built library JAR can be found in the `build/libs` directory.

For more developer related information, please refer to the [documentation](https://github.com/open200/xesar-connect/blob/main/docs/development.md).

## Contributing

Contributions to enhance and extend Xesar-Connect are highly appreciated! If you're interested in contributing, please review our [contribution guidelines](https://github.com/open200/xesar-connect/blob/main/CONTRIBUTING.md).

## Code of Conduct

Please review our [Code of Conduct](https://github.com/open200/xesar-connect/blob/main/CODE_OF_CONDUCT.md) before participating in the project.

## License

Xesar-Connect is released under the [Apache 2.0 License](https://github.com/open200/xesar-connect/blob/main/LICENSE).

---

We trust that Xesar-Connect will simplify your EVVA Xesar integration through MQTT communication. Should you encounter any challenges or have suggestions for improvement, kindly [open an issue](https://github.com/open200/xesar-connect/issues). Thank you for embracing Xesar-Connect!

**Disclaimer:** This project is not endorsed by or affiliated with EVVA. "EVVA" and "Xesar" are trademarks of EVVA Sicherheitstechnologie GmbH.
