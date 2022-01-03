package database.model.attribute;

public class AttributeCreator {
    public static Attribute createAttribute(String name, String value) {
        if (value.equals("true") || value.equals("false")) {
            return new BooleanAttribute(name, Boolean.parseBoolean(value));
        }
        if(value.matches("^[0-9]+$")) {
            return new IntegerAttribute(name, Integer.parseInt(value));
        }
        if(value.matches("^[0-9]+\\.[0-9]+$")) {
            return new DoubleAttribute(name, Double.parseDouble(value));
        }
        return new StringAttribute(name, value);
    }
}
