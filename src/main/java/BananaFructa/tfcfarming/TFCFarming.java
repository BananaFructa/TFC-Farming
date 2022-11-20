package BananaFructa.tfcfarming;

import BananaFructa.tfcfarming.firmalife.TEHangingPlanterN;
import BananaFructa.tfcfarming.firmalife.TEPlanterN;
import BananaFructa.tfcfarming.firmalife.TEStemCropN;
import BananaFructa.tfcfarming.network.PacketHandler;
import net.dries007.tfc.api.types.ICrop;
import net.dries007.tfc.objects.blocks.agriculture.BlockCropTFC;
import net.dries007.tfc.objects.te.TECropBase;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.agriculture.Crop;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.lwjgl.Sys;

import java.util.Map;

@Mod(modid = TFCFarming.modId,name = TFCFarming.name,version = TFCFarming.version,dependencies = "required-after:tfc;after:tfcflorae;after:firmalife")
public class TFCFarming {

    public static final String modId = "tfcfarming";
    public static final String name = "TFC Farming";
    public static final String version = "1.1.3";

    public static TFCFarming INSTANCE;

    public FarmingWorldStorage worldStorage;

    public static boolean tfcfloraeLoaded = false;
    public static boolean firmalifeLoaded = false;

    @SidedProxy(modId = TFCFarming.modId,clientSide = "BananaFructa.tfcfarming.ClientProxy",serverSide = "BananaFructa.tfcfarming.CommonProxy")
    public static CommonProxy proxy;

    public TFCFarming() {
        tfcfloraeLoaded = Loader.isModLoaded("tfcflorae");
        firmalifeLoaded = Loader.isModLoaded("firmalife");
        INSTANCE = this;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Config.load(event.getModConfigurationDirectory());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(proxy);
        PacketHandler.registerPackets();
        GameRegistry.registerTileEntity(TECropBaseN.class,new ResourceLocation(modId,TECropBaseN.class.getSimpleName()));
        if (firmalifeLoaded) {
            GameRegistry.registerTileEntity(TEPlanterN.class, new ResourceLocation(modId, TEPlanterN.class.getSimpleName()));
            GameRegistry.registerTileEntity(TEHangingPlanterN.class, new ResourceLocation(modId, TEHangingPlanterN.class.getSimpleName()));
            GameRegistry.registerTileEntity(TEStemCropN.class, new ResourceLocation(modId, TEStemCropN.class.getSimpleName()));
        }
        proxy.init();
    }

    @Mod.EventHandler
    public void serverStaring(FMLServerStartingEvent event) {
        worldStorage = FarmingWorldStorage.get(event.getServer().getWorld(0));
    }

}
