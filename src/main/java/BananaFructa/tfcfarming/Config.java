package BananaFructa.tfcfarming;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class Config {

    public static Configuration config;
    public static int nPassive;
    public static int pPassive;
    public static int kPassive;
    public static double growthDead;

    public static void load(File configDirectory) {
        config = new Configuration(new File(configDirectory,"tfcfarming.cfg"));

        nPassive = config.getInt("N passive growth","general",8,0,255,"The amount the N nutrient passively grows each month.");
        pPassive = config.getInt("P passive growth","general",8,0,255,"The amount the P nutrient passively grows each month.");
        kPassive = config.getInt("K passive growth","general",8,0,255,"The amount the K nutrient passively grows each month.");
        growthDead = config.getFloat("Nutrient passive growth with dead crop","general",0.8f,0,1,"The growth factor when a dead crop is let to exist on the soil.");
        if (config.hasChanged()) config.save();
    }

}
