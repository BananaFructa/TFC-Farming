package BananaFructa.tfcfarming.network;

import BananaFructa.tfcfarming.NutrientValues;
import BananaFructa.tfcfarming.TFCFarming;
import net.dries007.tfc.api.capability.player.CapabilityPlayerData;
import net.dries007.tfc.api.capability.player.IPlayerData;
import net.dries007.tfc.util.skills.Skill;
import net.dries007.tfc.util.skills.SkillTier;
import net.dries007.tfc.util.skills.SkillType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class CPacketRequestNutrientDataHandler implements IMessageHandler<CPacketRequestNutrientData, IMessage> {
    @Override
    public IMessage onMessage(CPacketRequestNutrientData message, MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().player;
        // Request protection
        IPlayerData playerData = player.getCapability(CapabilityPlayerData.CAPABILITY,null);
        Skill skill = playerData.getSkill(SkillType.AGRICULTURE);
        // TODO:                                                                                                     V config
        if (Math.abs(player.posX - message.x) + Math.abs(player.posZ - message.z) <= 10 && skill.getTier().isAtLeast(SkillTier.ADEPT)) {
            NutrientValues values = TFCFarming.INSTANCE.worldStorage.getNutrientValues(message.x,message.z);
            int[] NPK = values.getNPKSet();
            return new SPacketNutrientDataResponse(true,NPK[0],NPK[1],NPK[2], message.x,message.z);
        } else {
            return new SPacketNutrientDataResponse(false,0,0,0, message.x, message.z);
        }
    }
}
