package database.model.attribute;

import java.io.Serializable;

public class DoubleAttribute implements Attribute, Serializable {
    private String name;
    private double value;

    public DoubleAttribute(String name, double value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value + "";
    }

    @Override
    public void setValue(String value) {
        this.value = Double.parseDouble(value);
    }

    @Override
    public String getType() {
        return "Double";
    }
}
