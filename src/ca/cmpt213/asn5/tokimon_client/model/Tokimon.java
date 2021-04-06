package ca.cmpt213.asn5.tokimon_client.model;

public class Tokimon {

    private Long id;
    private String name;
    private double weight;
    private double height;
    private String ability;
    private int strength;
    private String color;

    public Tokimon() {
        this.name = "Default Tokimon Name";
        this.ability = "no type";
        this.height = 0.0D;
        this.weight = 0.0D;
        this.strength = 0;
    }

    public Tokimon(Long id, String name, double weight, double height, String ability, int strength, String color) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.height = height;
        this.ability = ability;
        this.strength = strength;
        this.color = color;
    }

    public Tokimon(Tokimon tokimon) {
        this.id = tokimon.id;
        this.name = tokimon.name;
        this.ability = tokimon.ability;
        this.height = tokimon.height;
        this.weight = tokimon.weight;
        this.strength = tokimon.strength;
        this.color = tokimon.color;
    }

    public String getName() {
        return this.name;
    }

    public String getAbility() {
        return this.ability;
    }

    public double getHeight() {
        return this.height;
    }

    public double getWeight() {
        return this.weight;
    }

    public int getStrength() {
        return this.strength;
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }

    public void setHeight(float height) {
        this.height = (double)height;
    }

    public void setWeight(float weight) {
        this.weight = (double)weight;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void alterToki(int strength) {
        this.setStrength(strength);
    }

    public String toString() {
        return this.name + "(id: " + id + ")";
    }
}
