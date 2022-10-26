package BananaFructa.tfcfarming;

import net.dries007.tfc.objects.Powder;
import net.dries007.tfc.objects.items.ItemPowder;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;

@Mod.EventBusSubscriber
public class TFCFarmingContent {

    public static HashMap<Item,NutrientClass> fertilizerClass = new HashMap<>();
    public static HashMap<Item,Integer> fertilizerValues = new HashMap<>();
    public static BasicItem fertilizerP = new BasicItem("fertilizer_p");

    public static void registerFertilizer(Item item,NutrientClass n,int value) {
        fertilizerClass.put(item,n);
        fertilizerValues.put(item,value);
    }

    static Item cachedItem = null;
    static boolean cachedResponse;
    public static boolean isFertilizer(Item i) {
        if (i != cachedItem) {
            cachedItem = i;
            cachedResponse = fertilizerClass.containsKey(i);
        }
        return  cachedResponse;
    }

    public static NutrientClass getFertilizerClass(Item i) {
        return  fertilizerClass.get(i);
    }

    public static int getFertilizerValue(Item i) {
        return fertilizerValues.get(i);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
        /*registerFertilizer(fertilizerP,NutrientClass.PHOSPHORUS,128);
        registerFertilizer(ItemPowder.get(Powder.FERTILIZER),NutrientClass.POTASSIUM,128);
        registerFertilizer(ItemPowder.get(Powder.SALTPETER),NutrientClass.NITROGEN,128);*/
        event.getRegistry().register(fertilizerP);
        for (int i = 0;i < Config.fertilizerNames.length;i++) {
            registerFertilizer(Item.REGISTRY.getObject(new ResourceLocation(Config.fertilizerNames[i])),Config.fertilizerClasses[i],Config.fertilizerValues[i]);
        }
    }

    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(fertilizerP,0,new ModelResourceLocation(fertilizerP.getRegistryName(),"inventory"));
    }

}
