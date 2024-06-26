package net.mymai1208.villageroverlay

import net.fabricmc.api.ClientModInitializer
import org.apache.logging.log4j.LogManager
import java.util.*

object VillagerOverlay : ClientModInitializer {
    @JvmStatic
    val LOGGER = LogManager.getLogger(VillagerOverlay::class.java)

    @JvmStatic
    val openedVillagers = mutableListOf<UUID>()
    @JvmStatic
    var currentOpenVillager: UUID? = null

    override fun onInitializeClient() {
        openedVillagers.clear()
        currentOpenVillager = null
    }
}