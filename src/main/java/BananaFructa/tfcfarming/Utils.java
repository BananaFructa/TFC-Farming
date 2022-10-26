package BananaFructa.tfcfarming;

import net.dries007.tfc.api.types.ICrop;
import net.dries007.tfc.objects.blocks.agriculture.BlockCropTFC;
import net.dries007.tfc.util.agriculture.Crop;
import net.dries007.tfc.world.classic.ChunkGenTFC;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.client.config.GuiUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class Utils {
    public static <T> T readDeclaredField(Class<?> targetType, Object target, String name) {
        try {
            Field f = targetType.getDeclaredField(name);
            f.setAccessible(true);
            return (T) f.get(target);
        } catch (Exception err) {
            err.printStackTrace();
            return null;
        }
    }

    public static void writeDeclaredField(Class<?> targetType, Object target, String name, Object value,boolean final_) {
        try {
            Field f = targetType.getDeclaredField(name);
            f.setAccessible(true);
            if (final_) {
                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);
            }
            f.set(target,value);

        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public static void writeDeclaredDouble(Class<?> targetType, Object target, String name, double value,boolean final_) {
        try {
            Field f = targetType.getDeclaredField(name);
            f.setAccessible(true);
            if (final_) {
                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);
            }
            f.setDouble(target,value);

        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public static Method getDeclaredMethod(Class<?> targetClass, String name, Class<?>... parameters) {
        try {
            Method m = targetClass.getDeclaredMethod(name,parameters);
            m.setAccessible(true);
            return m;
        } catch (Exception err) {
            err.printStackTrace();
            return null;
        }
    }

    public static void drawTooltipBox(int tooltipX,int tooltipY,int width,int height,int backgroundColor,int borderColorStart,int borderColorEnd) {
        GuiUtils.drawGradientRect(0, tooltipX - 3, tooltipY - 4, tooltipX + width + 3, tooltipY - 3, backgroundColor, backgroundColor);
        GuiUtils.drawGradientRect(0, tooltipX - 3, tooltipY + height + 3, tooltipX + width + 3, tooltipY + height + 4, backgroundColor, backgroundColor);
        GuiUtils.drawGradientRect(0, tooltipX - 3, tooltipY - 3, tooltipX + width + 3, tooltipY + height + 3, backgroundColor, backgroundColor);
        GuiUtils.drawGradientRect(0, tooltipX - 4, tooltipY - 3, tooltipX - 3, tooltipY + height + 3, backgroundColor, backgroundColor);
        GuiUtils.drawGradientRect(0, tooltipX + width + 3, tooltipY - 3, tooltipX + width + 4, tooltipY + height + 3, backgroundColor, backgroundColor);
        GuiUtils.drawGradientRect(0, tooltipX - 3, tooltipY - 3 + 1, tooltipX - 3 + 1, tooltipY + height + 3 - 1, borderColorStart, borderColorEnd);
        GuiUtils.drawGradientRect(0, tooltipX + width + 2, tooltipY - 3 + 1, tooltipX + width + 3, tooltipY + height + 3 - 1, borderColorStart, borderColorEnd);
        GuiUtils.drawGradientRect(0, tooltipX - 3, tooltipY - 3, tooltipX + width + 3, tooltipY - 3 + 1, borderColorStart, borderColorStart);
        GuiUtils.drawGradientRect(0, tooltipX - 3, tooltipY + height + 2, tooltipX + width + 3, tooltipY + height + 3, borderColorEnd, borderColorEnd);
    }

    public static RayTraceResult rayTrace(World worldIn, EntityPlayer playerIn, boolean useLiquids)
    {
        float f = playerIn.rotationPitch;
        float f1 = playerIn.rotationYaw;
        double d0 = playerIn.posX;
        double d1 = playerIn.posY + (double)playerIn.getEyeHeight();
        double d2 = playerIn.posZ;
        Vec3d vec3d = new Vec3d(d0, d1, d2);
        float f2 = MathHelper.cos(-f1 * 0.017453292F - (float)Math.PI);
        float f3 = MathHelper.sin(-f1 * 0.017453292F - (float)Math.PI);
        float f4 = -MathHelper.cos(-f * 0.017453292F);
        float f5 = MathHelper.sin(-f * 0.017453292F);
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d3 = playerIn.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
        Vec3d vec3d1 = vec3d.addVector((double)f6 * d3, (double)f5 * d3, (double)f7 * d3);
        return worldIn.rayTraceBlocks(vec3d, vec3d1, useLiquids, !useLiquids, false);
    }
    public static ActionResult<ItemStack> ricePlaceFixed(ICrop crop, Item item, World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        RayTraceResult raytraceresult = rayTrace(worldIn, playerIn, false);
        if (crop == Crop.RICE) {
            if (raytraceresult == null) {
                return new ActionResult(EnumActionResult.PASS, itemstack);
            } else {
                if (raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK) {
                    BlockPos blockpos = raytraceresult.getBlockPos().up();
                    Material material = worldIn.getBlockState(blockpos.down()).getMaterial();
                    if ((!worldIn.isBlockModifiable(playerIn, blockpos) || !playerIn.canPlayerEdit(blockpos.offset(raytraceresult.sideHit), raytraceresult.sideHit, itemstack)) && material == Material.WATER) {
                        return new ActionResult(EnumActionResult.FAIL, itemstack);
                    }

                    BlockPos blockpos1 = blockpos.up();
                    IBlockState iblockstate = worldIn.getBlockState(blockpos);
                    if (iblockstate.getMaterial() == Material.WATER && (Integer)iblockstate.getValue(BlockLiquid.LEVEL) == 0 && worldIn.isAirBlock(blockpos1) && iblockstate == ChunkGenTFC.FRESH_WATER && material != Material.WATER) {
                        BlockSnapshot blocksnapshot = BlockSnapshot.getBlockSnapshot(worldIn, blockpos1);
                        worldIn.setBlockState(blockpos1, BlockCropTFC.get(crop).getDefaultState());
                        if (ForgeEventFactory.onPlayerBlockPlace(playerIn, blocksnapshot, EnumFacing.UP, handIn).isCanceled()) {
                            blocksnapshot.restore(true, false);
                            return new ActionResult(EnumActionResult.FAIL, itemstack);
                        }

                        worldIn.setBlockState(blockpos1, BlockCropTFC.get(crop).getDefaultState(), 11);
                        if (playerIn instanceof EntityPlayerMP) {
                            CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)playerIn, blockpos1, itemstack);
                        }

                        if (!playerIn.capabilities.isCreativeMode) {
                            itemstack.shrink(1);
                        }

                        playerIn.addStat(StatList.getObjectUseStats(item));
                        worldIn.playSound(playerIn, blockpos, SoundEvents.BLOCK_WATERLILY_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        return new ActionResult(EnumActionResult.SUCCESS, itemstack);
                    }
                }

                return new ActionResult(EnumActionResult.FAIL, itemstack);
            }
        } else {
            return new ActionResult(EnumActionResult.FAIL, itemstack);
        }
    }
}
