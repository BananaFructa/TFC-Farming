package BananaFructa.tfcfarming;

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
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.lwjgl.Sys;

import java.util.Map;

@Mod(modid = TFCFarming.modId,name = TFCFarming.name,version = TFCFarming.version,dependencies = "required-after:tfc;after:tfcflorae")
public class TFCFarming {

    public static final String modId = "tfcfarming";
    public static final String name = "TFC Farming";
    public static final String version = "1.0";

    public static TFCFarming INSTANCE;

    public FarmingWorldStorage worldStorage;

    public static boolean tfcfloraeLoaded = false;

    @SidedProxy(modId = TFCFarming.modId,clientSide = "BananaFructa.tfcfarming.ClientProxy",serverSide = "BananaFructa.tfcfarming.CommonProxy")
    public static CommonProxy proxy;

    public TFCFarming() {
        tfcfloraeLoaded = Loader.isModLoaded("tfcflorae");
        INSTANCE = this;
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(proxy);
        PacketHandler.registerPackets();
        GameRegistry.registerTileEntity(TECropBaseN.class,new ResourceLocation(modId,TECropBaseN.class.getSimpleName()));
        proxy.init();
    }

    //@Mod.EventHandler
    //public void postInit(FMLPostInitializationEvent event) {
    //    tfcfloraeLoaded = Loader.isModLoaded("tfcflorae");
    //}

    @Mod.EventHandler
    public void serverStaring(FMLServerStartingEvent event) {
        worldStorage = FarmingWorldStorage.get(event.getServer().getWorld(0));
    }

}
