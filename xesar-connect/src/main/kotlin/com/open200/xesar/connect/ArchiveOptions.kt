package com.open200.xesar.connect

data class ArchiveOptions(
    val caCertificateName: String = "ca.pem",
    val clientCertificateName: String = "mqtt.pem",
    val clientKeyName: String = "mqtt.key",
    val apiPropertiesFileName: String = "api.properties",
)
