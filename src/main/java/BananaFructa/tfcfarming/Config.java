package BananaFructa.tfcfarming;

import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.lang.reflect.AccessibleObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Config {

    public static Configuration config;
    public static int nPassive;
    public static int pPassive;
    public static int kPassive;
    public static double growthDead;
    public static double nutrientConsumptionInGreenhouse = 1;
    public static double nutrientConsumptionHangingPlanter = 1;
    public static boolean hangingPlanters;
    public static String[] fertilizerNames;
    public static NutrientClass[] fertilizerClasses;
    public static Integer[] fertilizerValues;
    public static boolean enforceTemperature;

    public static void load(File configDirectory) {
        config = new Configuration(new File(configDirectory,"tfcfarming.cfg"));

        nPassive = config.getInt("n_passive","general",8,0,255,"The amount the N nutrient passively grows each month.");
        pPassive = config.getInt("p_passive","general",8,0,255,"The amount the P nutrient passively grows each month.");
        kPassive = config.getInt("k_passive","general",8,0,255,"The amount the K nutrient passively grows each month.");
        growthDead = config.getFloat("penalty_dead_crop","general",0.8f,0,1,"The growth factor when a dead crop is let to exist on the soil.");
        hangingPlanters = config.getBoolean("hanging_planter","general",false,"True if the hanging planters should also have nutrient values");
        nutrientConsumptionInGreenhouse = config.getFloat("nutrient_in_greenhouse","general",1,0,10,"The rate at which crops consume nutrients while in Firmalife's greenhouse");
        nutrientConsumptionHangingPlanter = config.getFloat("nutrient_int_hanging","general",1,0,10,"The rate at which hanging planters consume nutrients");
        enforceTemperature = config.getBoolean("enforce_temperature","general",false,"Plants in the greenhouse cannot grow in too of a hot climate");
        String[] fertilizerData = config.getStringList("fertilizers","general",new String[]{"tfc:powder/saltpeter~N~128","tfcfarming:fertilizer_p~P~128","tfc:powder/fertilizer~K~128"},"Fertilizer list: <item name>~<N/P/K>~<fertilizer value between 0~255>");
        List<String> names = new ArrayList<>();
        List<NutrientClass> classes = new ArrayList<>();
        List<Integer> values = new ArrayList<>();
        int count = 0;
        for (String s : fertilizerData) {
            try {
                String[] split = s.split("~");
                if (split.length == 3) {
                    String item = split[0];
                    NutrientClass nutrientClass;
                    int value = Math.max(0,Math.min(255,Integer.parseInt(split[2])));
                    switch (split[1]) {
                        case "N":
                            nutrientClass = NutrientClass.NITROGEN;
                            break;
                        case "P":
                            nutrientClass = NutrientClass.PHOSPHORUS;
                            break;
                        case "K":
                            nutrientClass = NutrientClass.POTASSIUM;
                            break;
                        default:
                            continue;
                    }
                    names.add(item);
                    classes.add(nutrientClass);
                    values.add(value);
                    count++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        fertilizerNames = new String[count];
        fertilizerClasses = new NutrientClass[count];
        fertilizerValues = new Integer[count];
        names.toArray(fertilizerNames);
        classes.toArray(fertilizerClasses);
        values.toArray(fertilizerValues);
        if (config.hasChanged()) config.save();
    }

}
