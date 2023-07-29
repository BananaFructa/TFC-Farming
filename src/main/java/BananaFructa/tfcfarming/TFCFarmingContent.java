package BananaFructa.tfcfarming;

import BananaFructa.tfcfarming.util.ItemWithSubType;
import net.dries007.tfc.objects.Powder;
import net.dries007.tfc.objects.items.ItemPowder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Mod.EventBusSubscriber
public class TFCFarmingContent {

    public static HashMap<ItemWithSubType,NutrientClass> fertilizerClass = new HashMap<>();
    public static HashMap<ItemWithSubType,Integer> fertilizerValues = new HashMap<>();
    public static BasicItem fertilizerP = new BasicItem("fertilizer_p");

    public static void registerFertilizer(Item item,int type,NutrientClass n,int value) {
        fertilizerClass.put(new ItemWithSubType(item,type),n);
        fertilizerValues.put(new ItemWithSubType(item,type),value);
    }

    static Item cachedItem = null;
    static int cachedType = 0;
    static boolean cachedResponse;
    public static boolean isFertilizer(ItemStack is) {
        Item i = is.getItem();
        boolean meta = i.getHasSubtypes() ;
        if (i != cachedItem || (meta && is.getMetadata() != cachedType)) {
            cachedItem = i;
            cachedType = meta ? is.getMetadata() : 0;
            cachedResponse = false;
            cachedResponse = fertilizerClass.containsKey(new ItemWithSubType(i,cachedType));
        }
        return  cachedResponse;
    }

    public static NutrientClass getFertilizerClass(ItemStack i) {
        return  fertilizerClass.get(new ItemWithSubType(i.getItem(),i.getHasSubtypes() ? i.getMetadata() : 0));
    }

    public static int getFertilizerValue(ItemStack i) {
        return fertilizerValues.get(new ItemWithSubType(i.getItem(),i.getHasSubtypes() ? i.getMetadata() : 0));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(fertilizerP);
        for (int i = 0;i < Config.fertilizerNames.length;i++) {
            String[] s = Config.fertilizerNames[i].split(":");
            Item item;
            int type = 0;
            if (s[s.length - 1].matches("[0-9]+")) {
                item = Item.REGISTRY.getObject(new ResourceLocation(String.join(":", Arrays.copyOfRange(s,0,s.length-1))));
                type = Integer.parseInt(s[s.length - 1]);
            } else {
                item = Item.REGISTRY.getObject(new ResourceLocation(Config.fertilizerNames[i]));
            }
            registerFertilizer(item,type,Config.fertilizerClasses[i],Config.fertilizerValues[i]);
        }
    }

    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(fertilizerP,0,new ModelResourceLocation(fertilizerP.getRegistryName(),"inventory"));
    }

}
