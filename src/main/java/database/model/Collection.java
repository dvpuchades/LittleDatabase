package database.model;

import java.io.Serializable;
import java.util.*;

public class Collection implements Serializable {
    private SortedMap<Integer, Document> documents;
    private int counter;

    public Collection() {
        this.documents = new TreeMap();
        this.counter = 0;
    }

    public void add(Document document) {
        this.documents.put(counter, new Document(counter, document.getAttributes()));
        this.counter++;
    }

    public Document get(int id) {
        return this.documents.get(id);
    }

    public SortedMap<Integer, Document> getDocuments() {
        return this.documents;
    }

    public boolean update(int id, Document document) {
        if (this.documents.containsKey(id)) {
            this.documents.put(id, document);
            return true;
        }
        return false;
    }

    public boolean delete(int id) {
        if (this.documents.containsKey(id)) {
            this.documents.remove(id);
            return true;
        }
        return false;
    }
}
