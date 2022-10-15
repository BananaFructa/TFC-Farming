package BananaFructa.tfcfarming;

import net.dries007.tfc.api.types.ICrop;
import net.dries007.tfc.objects.blocks.agriculture.BlockCropTFC;
import net.dries007.tfc.objects.te.TECropBase;
import net.dries007.tfc.util.climate.ClimateTFC;
import net.dries007.tfc.world.classic.chunkdata.ChunkDataTFC;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.Sys;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class TECropBaseN extends TECropBase {
    ICrop crop;
    CropNutrients nValues;
    long growthPhaseInterval;
    double factor = -1;
    double virtualFactor = -1;
    List<Double> factorList = new ArrayList<>();

    public TECropBaseN() {

    }

    public TECropBaseN(TECropBase lastBase) {
        lastTickCalChecked = lastBase.getLastUpdateTick();
    }

    public static double processFactor(NutrientValues nutrientValues, CropNutrients n) {
        return (nutrientValues.getNPKSet()[n.favouriteNutrient.ordinal()] >= n.stepCost ? 1 : 0.3);
    }

    void load() {
        if (crop == null) {
            if (blockType instanceof BlockCropTFC) {
                crop = ((BlockCropTFC) blockType).getCrop();
                nValues = CropNutrients.getCropNValues(crop);
                growthPhaseInterval = crop.getGrowthTicks();
                if (factor == -1) resetFactor();
            }
        }
    }

    @Override
    public long getTicksSinceUpdate() {

        load();

        NutrientValues nutrientValues = TFCFarming.INSTANCE.worldStorage.getNutrientValues(pos.getX(), pos.getZ());

        double newFactor = processFactor(nutrientValues,nValues);

        long dg = (long)(super.getTicksSinceUpdate() * factor);

        if (newFactor != virtualFactor && dg < growthPhaseInterval) {

            addFactor(virtualFactor,super.getTicksSinceUpdate());
            virtualFactor = newFactor;
            factor = getAverageFactor(virtualFactor,super.getTicksSinceUpdate());
        }
        
        return (long)(super.getTicksSinceUpdate() * factor);
    }

    private void resetFactor() {
        NutrientValues nutrientValues = TFCFarming.INSTANCE.worldStorage.getNutrientValues(pos.getX(), pos.getZ());
        factor = processFactor(nutrientValues,nValues);
        virtualFactor = factor;
        factorList.clear();
    }

    private void addFactor(double factor, double currentTicks) {
        factorList.add(factor);
        factorList.add(currentTicks);
    }

    private double getDeltaAt(int i) {
        if (i == 0) return factorList.get(1);
        else return factorList.get(i * 2 + 1) - factorList.get((i - 1) * 2 + 1);
    }

    private double getAverageFactor(double current,double currentTicks) {
        double rest = growthPhaseInterval;

        for (int i = 0;i < factorList.size()/2;i++) {
            //         V speed                 V time
            double r = factorList.get(i * 2) * getDeltaAt(i);
            rest -= r;
        }
        //     V total distance       V time         V dx   V speed
        return growthPhaseInterval / (currentTicks + rest / current);

    }

    @Override
    public void reduceCounter(long amount) {
        super.reduceCounter((long)(amount / factor));

        float temp = ClimateTFC.getActualTemp(getWorld(), pos, -getTicksSinceUpdate());
        float rainfall = ChunkDataTFC.getRainfall(getWorld(), pos);
        if (this.crop.isValidForGrowth(temp, rainfall)) {
            NutrientValues nutrientValues = TFCFarming.INSTANCE.worldStorage.getNutrientValues(pos.getX(), pos.getZ());

            if (nutrientValues.getNPKSet()[nValues.favouriteNutrient.ordinal()] >= nValues.stepCost) {
                nutrientValues.addNutrient(nValues.favouriteNutrient,-nValues.stepCost);
            }

            TFCFarming.INSTANCE.worldStorage.setNutrientValues(pos.getX(), pos.getZ(), nutrientValues);
            resetFactor();
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        factor = compound.getDouble("growthUnitFactor");
        virtualFactor = compound.getDouble("virtualGrowthFactor");

        ByteArrayInputStream arrayInputStream  = new ByteArrayInputStream(compound.getByteArray("factorList"));

        try {
            ObjectInputStream in = new ObjectInputStream(arrayInputStream);
            factorList = (List<Double>) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setDouble("growthUnitFactor",factor);
        compound.setDouble("virtualGrowthFactor",virtualFactor);

        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();

        try {

            ObjectOutputStream out = new ObjectOutputStream(arrayOutputStream);

            out.writeObject(factorList);
            compound.setByteArray("factorList", arrayOutputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.writeToNBT(compound);
    }
}
