package com.open200.xesar.connect.testutils

import com.open200.xesar.connect.Config
import io.mockk.mockk
import java.util.*
import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.MountableFile

object MosquittoContainer {

    fun container() =
        GenericContainer<Nothing>("eclipse-mosquitto:2.0").apply {
            withExposedPorts(1883)
            withCopyToContainer(
                MountableFile.forClasspathResource("mosquitto.conf"),
                "mosquitto/config/mosquitto.conf")
            start()
        }

    fun config(container: GenericContainer<Nothing>) =
        Config(
            Config.ApiProperties(
                hostname = container.host,
                port = container.firstMappedPort.toString(),
                userId = UUID.fromString("faf3d0c4-1281-40ae-89d7-5c541d77a757"),
            ),
            uuidGenerator = mockk())
}
