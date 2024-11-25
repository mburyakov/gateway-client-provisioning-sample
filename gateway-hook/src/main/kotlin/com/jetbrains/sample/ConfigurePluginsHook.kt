package com.jetbrains.sample

import com.intellij.ide.plugins.PluginInstaller
import com.intellij.ide.plugins.PluginManager
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.remoteDev.downloader.ConfigureClientHook
import com.intellij.remoteDev.downloader.FrontendConfigPaths
import com.intellij.remoteDev.downloader.FrontendInstallation
import com.intellij.remoteDev.util.ProductInfo
import java.nio.file.Path

class ConfigurePluginsHook : ConfigureClientHook {

    private val log = thisLogger()

    override fun beforeRun(
        extractedJetBrainsClientData: FrontendInstallation,
        productInfo: ProductInfo,
        configPath: FrontendConfigPaths
    ) {
        val thisPlugin = PluginManager.getPluginByClass(ConfigurePluginsHook::class.java)
        if (thisPlugin == null) {
            log.error("Could not find location of current plugi.n")
            return
        }
        val payloadDir = thisPlugin.pluginPath.resolve("resources/payloadPlugin").toFile()
        if (!payloadDir.exists()) {
            log.error("Could not find payload directory within current plugin installation: $payloadDir doesn't exist.")
            return
        }
        payloadDir.listFiles().forEach { file ->
            installPlugin(file.toPath(), configPath.pluginPath)
        }
    }

    fun installPlugin(pluginPath: Path, targetPath: Path) {
        try {
            PluginInstaller.unpackPlugin(pluginPath, targetPath)
        } catch (e: Exception) {
            log.error("Failed to unzip and install the file ${pluginPath.toAbsolutePath()} to ${targetPath.toAbsolutePath()}", e)
        }
    }
}