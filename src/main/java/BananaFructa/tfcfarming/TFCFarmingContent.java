package BananaFructa.tfcfarming;

import net.dries007.tfc.objects.Powder;
import net.dries007.tfc.objects.items.ItemPowder;
import net.dries007.tfc.objects.items.ItemsTFC;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;

@Mod.EventBusSubscriber
public class TFCFarmingContent {

    public static HashMap<Item,NutrientClass> fertilizerValue = new HashMap<>();
    public static BasicItem fertilizerP = new BasicItem("fertilizer_p");

    public static void registerFertilizer(Item item,NutrientClass n) {
        fertilizerValue.put(item,n);
    }

    static Item cachedItem = null;
    static boolean cachedResponse;
    public static boolean isFertilizer(Item i) {
        if (i != cachedItem) {
            cachedItem = i;
            cachedResponse = fertilizerValue.containsKey(i);
        }
        return  cachedResponse;
    }

    public static NutrientClass getFertilizerValues(Item i) {
        return  fertilizerValue.get(i);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
        registerFertilizer(fertilizerP,NutrientClass.PHOSPHORUS);
        registerFertilizer(ItemPowder.get(Powder.FERTILIZER),NutrientClass.POTASSIUM);
        registerFertilizer(ItemPowder.get(Powder.SALTPETER),NutrientClass.NITROGEN);
        event.getRegistry().register(fertilizerP);
    }

    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(fertilizerP,0,new ModelResourceLocation(fertilizerP.getRegistryName(),"inventory"));
    }

}
