package pro.samy.foliatest.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

class TestListener : Listener {
    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        event.player.sendMessage("You broke a block!")

        

    }
}