package BananaFructa.tfcfarming.util;

import net.minecraft.item.Item;

import java.util.Objects;

public class ItemWithSubType {

    public Item i;
    public int meta;

    public ItemWithSubType(Item i,int meta) {
        this.i = i;
        this.meta = meta;
    }

    @Override
    public int hashCode() {
        return Objects.hash(i, meta);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ItemWithSubType) {
            ItemWithSubType is = (ItemWithSubType) obj;
            return i == is.i && is.meta == meta;
        } else return false;
    }
}
