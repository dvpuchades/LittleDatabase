package database.model.attribute;

import java.io.Serializable;

public class IntegerAttribute implements Attribute, Serializable {
    private String name;
    private int value;

    public IntegerAttribute(String name, int value) {
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
        this.value = Integer.parseInt(value);
    }

    @Override
    public String getType() {
        return "Integer";
    }
}
