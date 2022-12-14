package BananaFructa.tfcfarming.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class CPacketRequestNutrientData implements IMessage {

    int x;
    int z;
    int y = -1;

    public CPacketRequestNutrientData() {

    }

    public CPacketRequestNutrientData(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public CPacketRequestNutrientData(int x,int y,int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }


    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        z = buf.readInt();
        y = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(z);
        buf.writeInt(y);
    }
}
