package com.open200.xesar.connect

import java.security.KeyStore
import java.util.*
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory

class SslContextCertificate(private val mqttCertificates: Config.MqttCertificates) {
    lateinit var ssLContext: SSLContext

    init {
        createSSLContext()
    }

    private fun createSSLContext() {

        val password = UUID.randomUUID().toString().toCharArray()

        val mqttPemCert = mqttCertificates.clientCertificate
        logger.debug { "Client - Certificate loaded. Valid until ${mqttPemCert.notAfter} " }

        val caCert = mqttCertificates.caCertificate
        logger.debug { "CA - Certificate loaded. Valid until ${caCert.notAfter} " }

        val mqttKey = mqttCertificates.clientKey

        // CA certificate is used to authenticate server
        val caKs = KeyStore.getInstance(KeyStore.getDefaultType())
        caKs.load(null, null)
        caKs.setCertificateEntry("ca-certificate", caCert)
        val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        tmf.init(caKs)

        // client key and certificates are sent to server, so it can authenticate us
        val ks = KeyStore.getInstance(KeyStore.getDefaultType())
        ks.load(null, null)
        ks.setCertificateEntry("certificate", mqttPemCert)

        ks.setKeyEntry("private-key", mqttKey.private, password, arrayOf(mqttPemCert))

        // initialize KeyManager
        val kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
        kmf.init(ks, password)

        // initialize sslContext
        ssLContext = SSLContext.getInstance("TLS")
        return ssLContext.init(kmf.keyManagers, tmf.trustManagers, null)
    }
}
