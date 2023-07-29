package BananaFructa.tfcfarming.firmalife;

import BananaFructa.tfcfarming.*;
import com.eerussianguy.firmalife.te.TEPlanter;
import net.dries007.tfc.ConfigTFC;
import net.dries007.tfc.Constants;
import net.dries007.tfc.api.types.ICrop;
import net.dries007.tfc.objects.items.ItemSeedsTFC;
import net.dries007.tfc.util.agriculture.Crop;
import net.dries007.tfc.util.calendar.CalendarTFC;
import net.dries007.tfc.util.climate.ClimateTFC;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class TEPlanterN extends TEPlanter {

    NutrientValues nutrientValues = new NutrientValues(0,0,0);


    private static final Method canGrow = Utils.getDeclaredMethod(TEPlanter.class,"canGrow",int.class);

    public TEPlanterN() {
        if (getTicksSinceUpdate() == CalendarTFC.PLAYER_TIME.getTicks()) resetCounter();
    }

    @Override
    public void onCalendarUpdate(long l) {
        if (!Config.allowedDimensions.contains(this.world.provider.getDimension())) return;

        int tier = Utils.readDeclaredField(TEPlanter.class,this,"tier");
        int waterUses = Utils.readDeclaredField(TEPlanter.class,this,"waterUses");
        double tierModifier = tier >= 2 ? 0.95 : 1.05;
        long growthTicks = (long)(24000.0 * tierModifier * ConfigTFC.General.FOOD.cropGrowthTimeModifier);

        while(this.getTicksSinceUpdate() > growthTicks) {
            this.reduceCounter(growthTicks);
            int slot = Constants.RNG.nextInt(4);
            if (waterUses < 0) {
                this.resetCounter();
                return;
            }
            // Thank god this logic is in the tile entity
            try {
                if ((boolean)canGrow.invoke(this,slot)) {
                    Item cropItem = this.inventory.getStackInSlot(slot).getItem();
                    if (cropItem instanceof ItemSeedsTFC) {
                        CropNutrients cropNutrients = CropNutrients.getCropNValues(Utils.readDeclaredField(ItemSeedsTFC.class,cropItem,"crop"));
                        if (cropNutrients != null) {
                            if (
                                    nutrientValues.getNutrient(cropNutrients.favouriteNutrient) >= cropNutrients.stepCost * Config.nutrientConsumptionInGreenhouse &&
                                    isBelowMaxTemp(cropNutrients.maximumTemperature)
                            ) {
                                nutrientValues.addNutrient(cropNutrients.favouriteNutrient, (int)(-cropNutrients.stepCost * Config.nutrientConsumptionInGreenhouse));
                                markDirty();
                            } else {
                                this.resetCounter();
                                return;
                            }
                        }
                    }
                    this.grow(slot);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean isBelowMaxTemp(float maxTemp) {
        return !Config.enforceTemperature || maxTemp > ClimateTFC.getActualTemp(world,pos,0);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        int[] NPK = compound.getIntArray("nutrients");
        nutrientValues = new NutrientValues(NPK);
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setIntArray("nutrients", nutrientValues.getNPKSet());
        return super.writeToNBT(compound);
    }

    public boolean fertilize(NutrientClass nutrientClass,int value) {
        boolean result =  nutrientValues.addNutrient(nutrientClass,value);
        if (result) markDirty();
        return  result;
    }

    public boolean anyLowNutrients() {
        for (int i = 0;i < 4;i++) {
            ItemStack stack = this.inventory.getStackInSlot(i);
            if (stack.getItem() instanceof ItemSeedsTFC) {
                ItemSeedsTFC itemSeedsTFC = (ItemSeedsTFC) stack.getItem();

                CropNutrients cropNutrients = null;
                for (ICrop c : CropNutrients.MAP.keySet()){
                    if (ItemSeedsTFC.get(c) == itemSeedsTFC) {
                        cropNutrients = CropNutrients.MAP.get(c);
                        break;
                    }
                }

                if (cropNutrients != null) {
                    if (nutrientValues.getNutrient(cropNutrients.favouriteNutrient) < cropNutrients.stepCost * Config.nutrientConsumptionInGreenhouse) return true;
                }
            }
        }
        return false;
    }

    public NutrientValues getNutrientValues() {
        return nutrientValues;
    }
}
