package com.open200.xesar.connect

import io.kotest.core.spec.style.FunSpec
import io.kotest.engine.spec.tempfile
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.string.shouldContain
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.security.Security
import java.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.testcontainers.shaded.org.bouncycastle.jce.provider.BouncyCastleProvider

class ConfigTest :
    FunSpec({
        beforeTest { Security.addProvider(BouncyCastleProvider()) }

        afterTest { Security.removeProvider("BC") }

        suspend fun copyResourceToTempFile(resourcePath: String): File =
            withContext(Dispatchers.IO) {
                val file = tempfile()

                requireNotNull(this::class.java.getResourceAsStream(resourcePath)) {
                        "Can't find $resourcePath"
                    }
                    .use { resourceStream ->
                        Files.copy(
                            resourceStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING)
                    }

                file
            }

        test(
            "should check that the config class is created with the right settings when the zip file is read") {
                val certificateZipFile = copyResourceToTempFile("/certificates/certificates.zip")

                val configZip =
                    Config.configureFromZip(certificateZipFile.toPath())
                        .copy(mqttConnectOptions = Config.MqttConnectOptions(isCleanSession = true))

                configZip.apiProperties.userId.shouldBeEqual(
                    UUID.fromString("35919920-c5aa-4371-900a-2ad6aa8206e0"))
                configZip.apiProperties.hostname.shouldBeEqual("xesar-test.com")
                configZip.apiProperties.token?.shouldBeEqual(
                    "JDJhJDEwJFdhTzREd3doRzZQTU5SZzQxeHNzRHVWVkJhLmp5ejM1Q3d0YklVdThMQlJHN2NHVFVjZVpT")

                configZip.mqttCertificates
                    ?.caCertificate
                    ?.serialNumber
                    ?.toString()
                    ?.shouldBeEqual("656231467021761541719339832774370847008982169026")

                configZip.mqttCertificates
                    ?.clientCertificate
                    ?.serialNumber
                    ?.toString()
                    ?.shouldBeEqual("604463769523327737375482424106426849567757226886")

                configZip.mqttCertificates
                    ?.clientKey
                    ?.private
                    .toString()
                    .shouldContain(
                        "c037f46b04f345eebab0cd1273d285641fd442311ae365132ef489b9fd3cacf0805d1eecbc190e4e2420f64b6c58e5d5558c08697cac23c2e281d9a0fe72708c9eb4267873989e3f9c3101a5141e5d491ee9a6765498091a5a4d042bb86c8cb")
            }

        test(
            "should check that the config class is created with the right settings when the certificates are read from path") {
                val caCertificate = copyResourceToTempFile("/certificates/paths/ca.pem")
                val clientCertificate = copyResourceToTempFile("/certificates/paths/mqtt.pem")
                val clientKey = copyResourceToTempFile("/certificates/paths/mqtt.key")

                val mqttCertificates =
                    Config.configureFromPaths(
                        caCertificate.toPath(), clientCertificate.toPath(), clientKey.toPath())

                mqttCertificates.caCertificate.serialNumber
                    ?.toString()
                    ?.shouldBeEqual("656231467021761541719339832774370847008982169026")

                mqttCertificates.clientCertificate.serialNumber
                    ?.toString()
                    ?.shouldBeEqual("604463769523327737375482424106426849567757226886")

                mqttCertificates.clientKey.private
                    .toString()
                    .shouldContain(
                        "c037f46b04f345eebab0cd1273d285641fd442311ae365132ef489b9fd3cacf0805d1eecbc190e4e2420f64b6c58e5d5558c08697cac23c2e281d9a0fe72708c9eb4267873989e3f9c3101a5141e5d491ee9a6765498091a5a4d042bb86c8cb")
            }
    })
