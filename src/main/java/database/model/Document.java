package database.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import database.model.attribute.Attribute;
import database.model.attribute.AttributeCreator;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Document implements Comparable<Document>, Serializable {
    private int id;
    private List<Attribute> attributes;

    public Document(int id) {
        this.id = id;
        this.attributes = new ArrayList();
    }

    public Document(int id, List<Attribute> attributes) {
        this.id = id;
        this.attributes = attributes;
    }

    public Document(String json) throws IOException {
        this.id = 0;
        this.attributes = new ArrayList();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = mapper.readValue(json, Map.class);
        for(Map.Entry<String, String> entry : map.entrySet()){
            Attribute attribute = AttributeCreator.createAttribute(entry.getKey(), entry.getValue());
            attributes.add(attribute);
        }
    }

    public int getId() {
        return id;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void addAttribute(String name, String value) {
        attributes.add(AttributeCreator.createAttribute(name, value));
    }

    @Override
    public int compareTo(Document o) {
        if(this.id < o.id) {
            return -1;
        }
        if(this.id > o.id) {
            return 1;
        }
        return 0;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Document " + id + ": \n");
        for(Attribute a : attributes){
            sb.append(a.getName() + " = " + a.getValue() + " type: " + a.getType() + ", \n");
        }
        return sb.toString();
    }
}
