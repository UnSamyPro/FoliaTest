package pro.samy.foliatest.command

import io.papermc.paper.threadedregions.ThreadedRegionizer.ThreadedRegion
import io.papermc.paper.threadedregions.TickRegions.*
import io.papermc.paper.threadedregions.commands.CommandUtil
import net.kyori.adventure.extra.kotlin.text
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld
import java.text.DecimalFormat

class TestCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {

        val regions: MutableList<ThreadedRegion<TickRegionData, TickRegionSectionData>> = mutableListOf()
        Bukkit.getWorlds().forEach { world ->
            val craftWorld = world as CraftWorld
            val nmsWorld = craftWorld.handle
            nmsWorld.regioniser.computeForAllRegions(regions::add)
        }

        val textComponent = text {
            append(Component.text("Region stats:", HEADER))
            append(Component.newline())
            regions.forEach { region ->
                val data = region.data
                append(formatRegionStats(data.regionStats, true))
            }
            append(Component.newline())
        }
        sender.sendMessage(textComponent)



//        Bukkit.getWorlds().forEach { world ->
//            val craftWorld = world as CraftWorld
//            val nmsWorld = craftWorld.handle
//
//            var sections: SWMRLong2ObjectHashTable<ThreadedRegionSection<TickRegions.TickRegionData, TickRegions.TickRegionSectionData>> = SWMRLong2ObjectHashTable()
//            nmsWorld.regioniser.javaClass.superclass.declaredFields.find { it.name == "sections" }?.apply {
//                isAccessible = true
//                sections = get(sections) as SWMRLong2ObjectHashTable<ThreadedRegionSection<TickRegions.TickRegionData, TickRegions.TickRegionSectionData>>
//            }
//
//            sections.forEach { section ->
//                var data = section.value.region.data
//
//                sender.sendMessage(formatRegionStats(data.regionStats, true))
//            }
//        }


        return true
    }


    private fun formatRegionInfo(
        prefix: String, util: Double, mspt: Double, tps: Double,
        newline: Boolean
    ): Component {
        return Component.text()
            .append(Component.text(prefix, PRIMARY, TextDecoration.BOLD))
            .append(
                Component.text(
                    ONE_DECIMAL_PLACES.get().format(util * 100.0),
                    CommandUtil.getUtilisationColourRegion(util)
                )
            )
            .append(Component.text("% util at ", PRIMARY))
            .append(
                Component.text(
                    TWO_DECIMAL_PLACES.get().format(mspt),
                    CommandUtil.getColourForMSPT(mspt)
                )
            )
            .append(Component.text(" MSPT at ", PRIMARY))
            .append(
                Component.text(
                    TWO_DECIMAL_PLACES.get().format(tps),
                    CommandUtil.getColourForTPS(tps)
                )
            )
            .append(Component.text(" TPS" + if (newline) "\n" else "", PRIMARY))
            .build()
    }

    private fun formatRegionStats(stats: RegionStats, newline: Boolean): Component {
        return Component.text()
            .append(Component.text("Chunks: ", PRIMARY))
            .append(
                Component.text(
                    NO_DECIMAL_PLACES.get().format(stats.chunkCount.toLong()),
                    INFORMATION
                )
            )
            .append(Component.text(" Players: ", PRIMARY))
            .append(
                Component.text(
                    NO_DECIMAL_PLACES.get().format(stats.playerCount.toLong()),
                    INFORMATION
                )
            )
            .append(Component.text(" Entities: ", PRIMARY))
            .append(
                Component.text(
                    NO_DECIMAL_PLACES.get()
                        .format(stats.entityCount.toLong()) + if (newline) "\n" else "", INFORMATION
                )
            )
            .build()
    }

    private val TWO_DECIMAL_PLACES = ThreadLocal.withInitial { DecimalFormat("#,##0.00") }
    private val ONE_DECIMAL_PLACES = ThreadLocal.withInitial { DecimalFormat("#,##0.0") }
    private val NO_DECIMAL_PLACES = ThreadLocal.withInitial { DecimalFormat("#,##0") }

    private val HEADER = TextColor.color(79, 164, 240)
    private val PRIMARY = TextColor.color(48, 145, 237)
    private val SECONDARY = TextColor.color(104, 177, 240)
    private val INFORMATION = TextColor.color(145, 198, 243)
    private val LIST = TextColor.color(33, 97, 188)

}