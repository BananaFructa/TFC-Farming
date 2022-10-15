package BananaFructa.tfcfarming;

public enum NutrientClass {
    NITROGEN("\u00A79Nitrogen (N)\u00A7r"),
    PHOSPHORUS("\u00A75Phosphorus (P)\u00A7r"),
    POTASSIUM("\u00A76Potassium (K)\u00A7r");

    public String name;

    NutrientClass(String name) {
        this.name = name;
    }

}
