package BananaFructa.tfcfarming;

import com.eerussianguy.firmalife.init.StemCrop;
import net.dries007.tfc.api.types.ICrop;
import net.dries007.tfc.util.agriculture.Crop;
import tfcflorae.util.agriculture.CropTFCF;

import javax.annotation.ParametersAreNullableByDefault;
import java.util.HashMap;

import static BananaFructa.tfcfarming.NutrientClass.*;

public enum CropNutrients {

    // Grains

    AMARANTH            (NITROGEN,       (TFCFarming.tfcfloraeLoaded ? CropTFCF.AMARANTH : null)),
    BUCKWHEAT           (NITROGEN,       (TFCFarming.tfcfloraeLoaded ? CropTFCF.BUCKWHEAT : null)),
    FONIO               (NITROGEN,       (TFCFarming.tfcfloraeLoaded ? CropTFCF.FONIO : null)),
    MILLET              (NITROGEN,       (TFCFarming.tfcfloraeLoaded ? CropTFCF.MILLET : null)),
    QUINOA              (NITROGEN,       (TFCFarming.tfcfloraeLoaded ? CropTFCF.QUINOA : null)),
    SPELT               (NITROGEN,       (TFCFarming.tfcfloraeLoaded ? CropTFCF.SPELT : null)),
    BARLEY              (NITROGEN,       Crop.BARLEY),
    MAIZE               (NITROGEN,       Crop.MAIZE),
    OAT                 (NITROGEN,       Crop.OAT),
    RICE                (NITROGEN,       Crop.RICE),
    RYE                 (NITROGEN,       Crop.RYE),
    WHEAT               (NITROGEN,       Crop.WHEAT),
    WELD                (NITROGEN,       (TFCFarming.tfcfloraeLoaded ? CropTFCF.WELD : null)),
    WOAD                (NITROGEN,       (TFCFarming.tfcfloraeLoaded ? CropTFCF.WOAD : null)),
    TOBACCO             (NITROGEN,       (TFCFarming.tfcfloraeLoaded ? CropTFCF.TOBACCO : null)),
    SUGARCANE           (NITROGEN,       Crop.SUGARCANE),
    PURPLE_GRAPE        (NITROGEN,       (TFCFarming.tfcfloraeLoaded ? CropTFCF.PURPLE_GRAPE : null)),
    GREEN_GRAPE         (NITROGEN,       (TFCFarming.tfcfloraeLoaded ? CropTFCF.GREEN_GRAPE : null)),
    INDIGO              (NITROGEN,       (TFCFarming.tfcfloraeLoaded ? CropTFCF.INDIGO : null)),
    MADDER              (NITROGEN,       (TFCFarming.tfcfloraeLoaded ? CropTFCF.MADDER : null)),
    OPIUM_POPPY         (NITROGEN,       (TFCFarming.tfcfloraeLoaded ? CropTFCF.OPIUM_POPPY : null)),

    // Legumes      N           P           K           S

    CAYENNE_PEPPER      (POTASSIUM,      (TFCFarming.tfcfloraeLoaded ? CropTFCF.CAYENNE_PEPPER : null)),
    GREEN_BEAN          (POTASSIUM,      Crop.GREEN_BEAN),
    SOYBEAN             (POTASSIUM,      Crop.SOYBEAN),
    TOMATO              (POTASSIUM,      Crop.TOMATO),
    RED_BELL_PEPPER     (POTASSIUM,      Crop.RED_BELL_PEPPER),
    YELLOW_BELL_PEPPER  (POTASSIUM,      Crop.YELLOW_BELL_PEPPER),
    PUMPKIN             (POTASSIUM,      (TFCFarming.firmalifeLoaded ? StemCrop.PUMPKIN : null)),
    MELON               (POTASSIUM,      (TFCFarming.firmalifeLoaded ? StemCrop.MELON : null)),
    BLACK_EYED_PEAS     (POTASSIUM,      (TFCFarming.tfcfloraeLoaded ? CropTFCF.BLACK_EYED_PEAS : null)),
    LIQUORICE           (POTASSIUM,      (TFCFarming.tfcfloraeLoaded ? CropTFCF.LIQUORICE_ROOT : null)),
    COFFEA              (POTASSIUM,      (TFCFarming.tfcfloraeLoaded ? CropTFCF.COFFEA : null)),
    AGAVE               (POTASSIUM,      (TFCFarming.tfcfloraeLoaded ? CropTFCF.AGAVE : null)),
    COCA                (POTASSIUM,      (TFCFarming.tfcfloraeLoaded ? CropTFCF.COCA : null)),
    COTTON              (POTASSIUM,      (TFCFarming.tfcfloraeLoaded ? CropTFCF.COTTON : null)),
    HOP                 (POTASSIUM,      (TFCFarming.tfcfloraeLoaded ? CropTFCF.HOP : null)),
    RAPE                (POTASSIUM,      (TFCFarming.tfcfloraeLoaded ? CropTFCF.RAPE : null)),


    GINGER              (PHOSPHORUS,     (TFCFarming.tfcfloraeLoaded ? CropTFCF.GINGER : null)),
    GINSENG             (PHOSPHORUS,     (TFCFarming.tfcfloraeLoaded ? CropTFCF.GINSENG : null)),
    RUTABAGA            (PHOSPHORUS,     (TFCFarming.tfcfloraeLoaded ? CropTFCF.RUTABAGA : null)),
    TURNIP              (PHOSPHORUS,     (TFCFarming.tfcfloraeLoaded ? CropTFCF.TURNIP : null)),
    SUGAR_BEET          (PHOSPHORUS,     (TFCFarming.tfcfloraeLoaded ? CropTFCF.SUGAR_BEET : null)),
    BEET                (PHOSPHORUS,     Crop.BEET),
    CABBAGE             (PHOSPHORUS,     Crop.CABBAGE),
    CARROT              (PHOSPHORUS,     Crop.CARROT),
    GARLIC              (PHOSPHORUS,     Crop.GARLIC),
    ONION               (PHOSPHORUS,     Crop.ONION),
    POTATO              (PHOSPHORUS,     Crop.POTATO),
    SQUASH              (PHOSPHORUS,     Crop.SQUASH),
    FLAX                (PHOSPHORUS,     (TFCFarming.tfcfloraeLoaded ? CropTFCF.FLAX : null)),
    HEMP                (PHOSPHORUS,     (TFCFarming.tfcfloraeLoaded ? CropTFCF.HEMP : null)),
    JUTE                (PHOSPHORUS,     Crop.JUTE);

    public final int stepCost;
    public final ICrop crop;
    public final NutrientClass favouriteNutrient;
    public float maximumTemperature;

    // reverse map
    public static HashMap<ICrop, CropNutrients> MAP = new HashMap<ICrop, CropNutrients>();


    CropNutrients(NutrientClass favouriteNutrient, ICrop crop) {
        if (crop != null) {
            stepCost = 255 / (crop.getMaxStage() + 1);
            this.favouriteNutrient = favouriteNutrient;
            this.crop = crop;

            if (crop instanceof Crop) {
                maximumTemperature = Utils.readDeclaredField(Crop.class,crop,"tempMaxGrow");
            } else if (crop instanceof StemCrop) {
                maximumTemperature = Utils.readDeclaredField(StemCrop.class,crop,"tempMaxGrow");
            } else if (crop instanceof CropTFCF) {
                maximumTemperature = Utils.readDeclaredField(CropTFCF.class,crop,"tempMaxGrow");
            }

        } else {
            this.stepCost = 0;
            this.favouriteNutrient = PHOSPHORUS; //safer default! Avoids NPE issue with TECropBaseN.processFactor
            this.crop = null;
        }

    }
    public static CropNutrients getCropNValues(ICrop crop) {
        if (MAP.isEmpty()) {
            for (CropNutrients value : values()) {
                MAP.put(value.crop,value);
            }
        }
        return MAP.get(crop);
    }

}
