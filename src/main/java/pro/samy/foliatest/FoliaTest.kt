package pro.samy.foliatest

import org.bukkit.plugin.java.JavaPlugin
import pro.samy.foliatest.command.TestCommand
import java.util.*

class FoliaTest : JavaPlugin() {

    override fun onEnable() {
        logger.info("FoliaTest has been enabled!")
        server.getPluginCommand("foliatest")?.setExecutor(TestCommand())
    }

    override fun onDisable() {
        logger.info("FoliaTest has been disabled!")
    }





}