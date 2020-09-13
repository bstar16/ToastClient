package dev.toastmc.client.module.movement

import baritone.api.pathing.goals.GoalBlock
import dev.toastmc.client.ToastClient.Companion.EVENT_BUS
import dev.toastmc.client.event.PacketEvent
import dev.toastmc.client.module.Category
import dev.toastmc.client.module.Module
import dev.toastmc.client.module.ModuleManifest
import dev.toastmc.client.util.Baritone
import dev.toastmc.client.util.mc
import me.zero.alpine.listener.EventHandler
import me.zero.alpine.listener.EventHook
import me.zero.alpine.listener.Listener
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket

@ModuleManifest(
        label = "AutoWalk",
        description = "Runs Forward",
        category = Category.MOVEMENT
)
class AutoWalk : Module() {

    override fun onEnable() {
        super.onEnable()
        EVENT_BUS.subscribe(onEvent)
    }

    override fun onDisable() {
        super.onDisable()
        EVENT_BUS.unsubscribe(onEvent)
        Baritone.invoke {
            if(Baritone.baritonePresent()){
                if (Baritone.BARITONE.customGoalProcess.isActive){
                    Baritone.BARITONE.customGoalProcess.setGoalAndPath(null)
                }
            }
        }
    }

    @EventHandler
    private val onEvent = Listener(EventHook<PacketEvent.Send> {
        if (mc.player == null) return@EventHook
        var baritone = false
        Baritone.invoke {
            if(Baritone.baritonePresent()){
                if (!Baritone.BARITONE.customGoalProcess.isActive){
                    Baritone.BARITONE.customGoalProcess.setGoalAndPath(GoalBlock(mc.player!!.blockPos.method_30513(mc.player!!.movementDirection.axis, 1000)))
                }
            }
            baritone = true
            return@invoke
        }
        if (!baritone) {
            if (it.packet is PlayerMoveC2SPacket || it.packet is InventoryS2CPacket) {
                mc.options.sprintToggled = true
                mc.options.keyForward.isPressed = true
            }
        }
    })
}