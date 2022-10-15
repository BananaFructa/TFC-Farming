package BananaFructa.tfcfarming.network;

import BananaFructa.tfcfarming.ClientProxy;
import BananaFructa.tfcfarming.TFCFarming;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SPacketNutrientDataResponseHandler implements IMessageHandler<SPacketNutrientDataResponse, IMessage> {
    @Override
    public IMessage onMessage(SPacketNutrientDataResponse message, MessageContext ctx) {
        if (message.accepted) {
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    ((ClientProxy)TFCFarming.proxy).setLastResponse(message);
                }
            });
        }
        return null;
    }
}
