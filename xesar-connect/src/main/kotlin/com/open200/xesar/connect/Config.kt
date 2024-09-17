package com.open200.xesar.connect

import com.open200.xesar.connect.utils.DefaultRequestIdGenerator
import com.open200.xesar.connect.utils.IRequestIdGenerator
import com.open200.xesar.connect.utils.UUIDSerializer
import java.io.*
import java.nio.file.Path
import java.security.KeyFactory
import java.security.KeyPair
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*
import java.util.zip.ZipFile
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable

/**
 * Configuration data class for Xesar API.
 *
 * @property mqttClientId The client ID to use for the connection. If not provided, a random
 *   generated id is used.
 * @property apiProperties The API properties containing hostname, port, userId, and token.
 * @property mqttCertificates The MQTT certificates used for secure communication (optional).
 * @property uuidGenerator The generator for request IDs.
 * @property mqttConnectOptions The MQTT connect options.
 * @property logoutOnClose Whether the logout is set up on close of the connection (optional).
 */
data class Config(
    val mqttClientId: String? = null,
    val apiProperties: ApiProperties,
    val mqttCertificates: MqttCertificates? = null,
    val uuidGenerator: IRequestIdGenerator = DefaultRequestIdGenerator(),
    val mqttConnectOptions: MqttConnectOptions = MqttConnectOptions(),
    val logoutOnClose: Boolean = true,
    val dispatcherForCommandsAndCleanUp: CoroutineDispatcher = Dispatchers.IO
) {
    /**
     * @property hostname The hostname of the API.
     * @property port The port number of the API.
     * @property userId The UUID of the user.
     * @property token The token used for authentication (optional).
     */
    data class ApiProperties(
        val hostname: String,
        val port: String,
        @Serializable(with = UUIDSerializer::class) val userId: UUID,
        val token: Token? = null,
    )

    /**
     * Data class for MQTT certificates.
     *
     * @property caCertificate The path to the CA certificates.
     * @property clientCertificate The path to the client certificates.
     * @property clientKey The path to the client key.
     */
    data class MqttCertificates(
        val caCertificate: X509Certificate,
        val clientCertificate: X509Certificate,
        val clientKey: KeyPair,
    )

    /**
     * Data class for MQTT connect options.
     *
     * @property isCleanSession Whether to start with a clean session (default: false).
     * @property connectionTimeout The connection timeout in seconds (default: 30).
     * @property isAutomaticReconnect Whether to enable automatic reconnection (default: true).
     * @property maxInflight The maximum number of in-flight messages (default: 500).
     * @property keepAliveInterval The keep-alive interval in seconds (default: 600).
     * @property securityProtocol The protocol used for a secure connection between client and
     *   broker when [MqttCertificates] are provided. (default: "TLSv1.3").
     */
    data class MqttConnectOptions(
        val isCleanSession: Boolean = false,
        val connectionTimeout: Int = 30,
        val isAutomaticReconnect: Boolean = true,
        val maxInflight: Int = 500,
        val keepAliveInterval: Int = 600,
        val securityProtocol: String = "TLSv1.3"
    )

    companion object {

        private const val DEFAULT_PORT = "1883"

        /**
         * Creates a [Config] instance by configuring it from a ZIP file containing the necessary
         * configuration data.
         *
         * @param configurationZipFile The path to the ZIP file containing the configuration data.
         * @param archiveOptions The options to configure the name of the files inside your ZIP
         *   archive (optional, default: ArchiveOptions()).
         * @param port The port number for the API (default: "1883").
         * @param mqttConnectOptions The MQTT connect options (optional, default:
         *   MqttConnectOptions()).
         * @param requestIdGenerator The generator for request IDs (optional, default:
         *   DefaultRequestIdGenerator()).
         * @param logoutOnClose Whether to log out on close (optional).
         * @return A [Config] instance configured from the provided ZIP file that you can use to
         *   connect to the XesarMqttInstance.
         */
        fun configureFromZip(
            configurationZipFile: Path,
            archiveOptions: ArchiveOptions = ArchiveOptions(),
            port: String? = null,
            mqttConnectOptions: MqttConnectOptions = MqttConnectOptions(),
            requestIdGenerator: IRequestIdGenerator = DefaultRequestIdGenerator(),
            logoutOnClose: Boolean = true,
        ): Config {

            val zipFile = extractZipFile(configurationZipFile)

            val apiProperties =
                readTokenProperties(
                    archiveOptions.apiPropertiesFileName, zipFile, port ?: DEFAULT_PORT)

            logger.debug(
                "ApiProperties for host: ${apiProperties.hostname} on port: ${apiProperties.port} loaded.")

            return Config(
                apiProperties =
                    ApiProperties(
                        hostname = apiProperties.hostname,
                        port = apiProperties.port,
                        userId = apiProperties.userId,
                        token = apiProperties.token),
                mqttCertificates =
                    MqttCertificates(
                        caCertificate =
                            readX509CertificateFromZip(archiveOptions.caCertificateName, zipFile),
                        clientCertificate =
                            readX509CertificateFromZip(
                                archiveOptions.clientCertificateName, zipFile),
                        clientKey = readKeyPairWithZip(archiveOptions.clientKeyName, zipFile)),
                uuidGenerator = requestIdGenerator,
                mqttConnectOptions =
                    MqttConnectOptions(
                        isCleanSession = mqttConnectOptions.isCleanSession,
                        connectionTimeout = mqttConnectOptions.connectionTimeout,
                        isAutomaticReconnect = mqttConnectOptions.isAutomaticReconnect,
                        maxInflight = mqttConnectOptions.maxInflight,
                        keepAliveInterval = mqttConnectOptions.keepAliveInterval),
                logoutOnClose = logoutOnClose)
        }

        private fun readTokenProperties(
            fileName: String,
            zipFile: ZipFile,
            port: String
        ): ApiProperties {
            val fileHeader = zipFile.getEntry(fileName)

            val bufferedReader =
                BufferedReader(InputStreamReader(zipFile.getInputStream(fileHeader)))

            val properties = Properties()

            properties.load(bufferedReader)
            val hostname = properties.getProperty("broker.address")
            val userId = properties.getProperty("userid")
            val token = properties.getProperty("token")

            return ApiProperties(hostname, port, UUID.fromString(userId), token)
        }

        private fun extractZipFile(configurationZipFile: Path): ZipFile {
            return ZipFile(configurationZipFile.toFile())
        }

        /**
         * Creates an instance of [MqttCertificates] by configuring it from individual file paths of
         * certificates.
         *
         * @param caCertificatePath The path to the CA certificate file.
         * @param clientCertificatePath The path to the client certificate file.
         * @param clientKeyPath The path to the client key file.
         * @return An instance of [MqttCertificates] configured from the provided certificate file
         *   paths.
         */
        fun configureFromPaths(
            caCertificatePath: Path,
            clientCertificatePath: Path,
            clientKeyPath: Path
        ): MqttCertificates {

            return MqttCertificates(
                caCertificate = readX509CertificateFromPath("$caCertificatePath"),
                clientCertificate = readX509CertificateFromPath("$clientCertificatePath"),
                clientKey = readKeyPairWithPath("$clientKeyPath"))
        }

        private fun readX509CertificateFromZip(
            fileName: String,
            zipFile: ZipFile
        ): X509Certificate {

            val fileHeader = zipFile.getEntry(fileName)

            zipFile.getInputStream(fileHeader).use {
                return decodeX509Certificate(it)
            }
        }

        private fun readX509CertificateFromPath(filePath: String): X509Certificate {

            val certificateInputStream = FileInputStream(filePath)

            return decodeX509Certificate(certificateInputStream)
        }

        private fun decodeX509Certificate(inputStreamReader: InputStream): X509Certificate {
            val certificateFactory = CertificateFactory.getInstance("X.509")
            return certificateFactory.generateCertificate(inputStreamReader) as X509Certificate
        }

        private fun readKeyPairWithZip(fileName: String, zipFile: ZipFile): KeyPair {

            val fileHeader = zipFile.getEntry(fileName)

            val bufferedReader =
                BufferedReader(InputStreamReader(zipFile.getInputStream(fileHeader)))

            return decodeKeyPair(bufferedReader)
        }

        private fun readKeyPairWithPath(filePath: String): KeyPair {

            val file = File(filePath)

            val bufferedReader = BufferedReader(FileReader(file))

            return decodeKeyPair(bufferedReader)
        }

        private fun decodeKeyPair(bufferedReader: BufferedReader): KeyPair {
            val sb = StringBuilder()

            var line: String? = bufferedReader.readLine()
            while (line != null) {
                if (line.startsWith("-----BEGIN") || line.startsWith("-----END")) {
                    line = bufferedReader.readLine()
                    continue
                }
                sb.append(line)
                line = bufferedReader.readLine()
            }
            bufferedReader.close()

            val base64Key = sb.toString()
            val privateKeyBytes = Base64.getDecoder().decode(base64Key)

            val keyFactory = KeyFactory.getInstance("RSA")
            val privateKeySpec = PKCS8EncodedKeySpec(privateKeyBytes)
            val privateKey = keyFactory.generatePrivate(privateKeySpec)

            return KeyPair(null, privateKey)
        }
    }
}
