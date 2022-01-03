package database.model.attribute;

import java.io.Serializable;

public class BooleanAttribute implements Attribute, Serializable {
    private String name;
    private boolean value;

    public BooleanAttribute(String name, boolean value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return String.valueOf(value);
    }

    @Override
    public void setValue(String value) {
        this.value = Boolean.parseBoolean(value);
    }

    @Override
    public String getType() {
        return "Boolean";
    }
}
