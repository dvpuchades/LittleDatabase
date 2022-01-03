package database.model.attribute;

import java.io.Serializable;

public class StringAttribute implements Attribute, Serializable {
    private String name;
    private String value;

    public StringAttribute(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getType() {
        return "String";
    }
}
