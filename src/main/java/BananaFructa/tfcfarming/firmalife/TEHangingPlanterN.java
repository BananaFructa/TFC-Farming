package BananaFructa.tfcfarming.firmalife;

import BananaFructa.tfcfarming.Config;
import BananaFructa.tfcfarming.CropNutrients;
import BananaFructa.tfcfarming.NutrientClass;
import BananaFructa.tfcfarming.NutrientValues;
import com.eerussianguy.firmalife.te.TEHangingPlanter;
import net.dries007.tfc.api.types.ICrop;
import net.dries007.tfc.util.calendar.CalendarTFC;
import net.dries007.tfc.util.climate.ClimateTFC;
import net.minecraft.nbt.NBTTagCompound;

public class TEHangingPlanterN extends TEHangingPlanter {

    NutrientValues nutrientValues = new NutrientValues(0,0,0);
    NutrientClass nutrientClass;
    int cost;
    float maxTemp;
    public TEHangingPlanterN() {

    }

    public TEHangingPlanterN(ICrop crop) {
        CropNutrients cropNutrients = CropNutrients.getCropNValues(crop);
        nutrientClass = cropNutrients.favouriteNutrient;
        cost = (int)(cropNutrients.stepCost * Config.nutrientConsumptionHangingPlanter);
        maxTemp = cropNutrients.maximumTemperature;
    }

    @Override
    public void reduceCounter(long amount) {
        super.reduceCounter(amount);
        if (nutrientValues.getNutrient(nutrientClass) >= cost) nutrientValues.addNutrient(nutrientClass,-cost);
    }

    public boolean fertilize(NutrientClass nutrientClass, int value) {
        return nutrientValues.addNutrient(nutrientClass,value);
    }

    public NutrientValues getNutrientValues() {
        return nutrientValues;
    }

    @Override
    public boolean isClimateValid(int tierMinimum) {
        if (!Config.allowedDimensions.contains(this.world.provider.getDimension())) return false;
        return super.isClimateValid(tierMinimum) && nutrientValues.getNutrient(nutrientClass) >= cost && isTempBelowMax();
    }

    public boolean isTempBelowMax() {
        return !Config.enforceTemperature || maxTemp > ClimateTFC.getActualTemp(world,pos,0);
    }

    public boolean isLow() {
        return nutrientValues.getNutrient(nutrientClass) < cost;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        int[] NPK = compound.getIntArray("nutrients");
        nutrientValues = new NutrientValues(NPK);
        cost = compound.getInteger("cost");
        nutrientClass = NutrientClass.values()[compound.getInteger("nutrientClass")];
        maxTemp = compound.getFloat("maxTemp");
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setIntArray("nutrients", nutrientValues.getNPKSet());
        compound.setInteger("cost",cost);
        compound.setInteger("nutrientClass",nutrientClass.ordinal());
        compound.setFloat("maxTemp",maxTemp);
        return super.writeToNBT(compound);
    }
}
