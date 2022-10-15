package BananaFructa.tfcfarming.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SPacketNutrientDataResponse implements IMessage {

    public boolean accepted;
    public int x;
    public int z;
    public int n;
    public int p;
    public int k;

    public SPacketNutrientDataResponse() {

    }

    public SPacketNutrientDataResponse(boolean accepted, int n, int p, int k,int x,int z) {
        this.accepted = accepted;
        this.n = n;
        this.p = p;
        this.k = k;
        this.x = x;
        this.z = z;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        accepted = buf.readBoolean();
        n = buf.readInt();
        p = buf.readInt();
        k = buf.readInt();
        x = buf.readInt();
        z = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(accepted);
        buf.writeInt(n);
        buf.writeInt(p);
        buf.writeInt(k);
        buf.writeInt(x);
        buf.writeInt(z);
    }
}
