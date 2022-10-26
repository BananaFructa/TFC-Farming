package BananaFructa.tfcfarming;

import java.util.Arrays;

public class NutrientValues {

    int[] NPK = new int[] {0,0,0};

    public NutrientValues(int nitrogen,int phosphorus,int potassium) {
        NPK = new int[] {nitrogen,phosphorus,potassium};
    }

    public NutrientValues(int[] NPK) {
        this.NPK = Arrays.copyOf(NPK,NPK.length);
    }

    public NutrientValues(int packed) {
        NPK[0] = packed & 0xff;
        NPK[1] = (packed >> 8) & 0xff;
        NPK[2] = (packed >> 16) & 0xff;
    }

    public int packToInt() {
        int i = 0;
        i |= NPK[0];
        i |= NPK[1] << 8;
        i |= NPK[2] << 16;
        return i;
    }

    public boolean addNutrient(NutrientClass nutrientClass,int amount) {
        if (NPK[nutrientClass.ordinal()] == 255 && amount > 0) return false;
        if (NPK[nutrientClass.ordinal()] == 0 && amount < 0) return false;
        NPK[nutrientClass.ordinal()] = Math.max(Math.min(NPK[nutrientClass.ordinal()] + amount,255),0);
        return true;
    }

    public int getNutrient(NutrientClass nutrientClass) {
        return NPK[nutrientClass.ordinal()];
    }

    public int[] getNPKSet() {
        return NPK;
    }

}
