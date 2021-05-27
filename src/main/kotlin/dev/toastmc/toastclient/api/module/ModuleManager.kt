package dev.toastmc.toastclient.api.module

import dev.toastmc.toastclient.IToastClient
import dev.toastmc.toastclient.ToastClient
import dev.toastmc.toastclient.api.events.OverlayEvent
import dev.toastmc.toastclient.api.events.TickEvent
import dev.toastmc.toastclient.impl.module.client.CustomChat
import dev.toastmc.toastclient.impl.module.client.Font
import dev.toastmc.toastclient.impl.module.client.FriendModule
import dev.toastmc.toastclient.impl.module.combat.AutoArmour
import dev.toastmc.toastclient.impl.module.combat.AutoRespawn
import dev.toastmc.toastclient.impl.module.combat.KillAura
import dev.toastmc.toastclient.impl.module.combat.Offhand
import dev.toastmc.toastclient.impl.module.misc.ExtraSign
import dev.toastmc.toastclient.impl.module.misc.ExtraTab
import dev.toastmc.toastclient.impl.module.misc.ExtraTooltip
import dev.toastmc.toastclient.impl.module.misc.PortalChat
import dev.toastmc.toastclient.impl.module.movement.*
import dev.toastmc.toastclient.impl.module.player.*
import dev.toastmc.toastclient.impl.module.render.*
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW
import org.quantumclient.energy.Subscribe
import java.util.*
import java.util.stream.Collectors

object ModuleManager : IToastClient {

    var modules: ArrayList<Module> = ArrayList()

    init {
        register(
            AutoArmour,
            AutoRespawn,
            AutoWalk,
            ExtraSign,
            ExtraTab,
            ExtraTooltip,
            Font,
            FastUtil,
            FriendModule,
            NoRender,
            MCF,
            Offhand,
            Capes,
            NameTags,
            FullBright,
            SafeWalk,
            NoEntityTrace,
            NoFog,
            CustomChat,
            KillAura,
            AntiVelocity,
            NoFall,
            AntiHunger,
            Sprint,
            Jesus,
            PortalChat
        )

        ToastClient.eventBus.register(this)

        logger.info("Finished loading ${modules.size} modules")
    }

    private fun register(vararg mods: Module) {
        modules = ArrayList()

        for (mod in mods) {
            modules.add(mod)
        }

        Collections.sort(modules, Comparator.comparing(Module::getName))
    }

    fun getModulesByCategory(category: Module.Category): ArrayList<Module>? {
        return modules.stream()
            .filter { module: Module -> module.getCategory() == category }
            .collect(Collectors.toCollection { ArrayList() })
    }

    fun onKey(window: Long, keyCode: Int, scancode: Int) {
        for (mod in modules) {
            if (mod.getKey() === InputUtil.fromKeyCode(keyCode, scancode)) {
                if (GLFW.glfwGetKey(window, keyCode) == 0) {
                    mod.toggle()
                }
            }
        }
    }

    @Subscribe
    fun on(event: TickEvent.Client.InGame) {
        modules.stream().filter { mod: Module -> mod.isEnabled() }.forEach { mod: Module -> mod.onUpdate() }
    }

    @Subscribe
    fun on(event: OverlayEvent) {
        modules.stream().filter { mod: Module -> mod.isEnabled() }.forEach { mod: Module -> mod.onHUDRender() }
    }

}