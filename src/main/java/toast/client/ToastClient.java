package toast.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Formatting;
import toast.client.commands.CommandHandler;
import toast.client.gui.clickgui.ClickGuiScreen;
import toast.client.modules.ModuleManager;
import toast.client.utils.ASCII;
import toast.client.utils.FileManager;
import toast.client.utils.TPSCalculator;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ToastClient implements ModInitializer {

    public static String version = "b1.0";
    public static String cleanPrefix = "ToastClient";
    public static String chatPrefix = Formatting.DARK_GRAY + "[" + Formatting.LIGHT_PURPLE + "ToastClient" + Formatting.DARK_GRAY + "]";
    public static String cmdPrefix = ".";
    public static List<String> devs = Collections.singletonList("MorganAnkan, RemainingToast, Qther, Fleebs, wnuke");
    public static ModuleManager MODULE_MANAGER = new ModuleManager();
    public static CommandHandler COMMAND_HANDLER = new CommandHandler();
    public static ClickGuiScreen clickGui;
    public static Boolean clickGuiHasOpened;

    @Override
    public void onInitialize() {
        if(clickGui == null){
            clickGuiHasOpened = false;
        }
        ASCII.printFancyConsoleMSG();
        System.out.println(cleanPrefix + " Initialized");
        System.out.println("Special thanks to all contributors of this project " + devs);
        FileManager.initFileManager();
        try {
            MODULE_MANAGER.loadModules();
            COMMAND_HANDLER.initCommands();
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
        TPSCalculator.calculatorInstance = new TPSCalculator();
    }
}
