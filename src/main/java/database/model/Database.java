package database.model;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;

public class Database implements Serializable {
    private String name;
    private HashMap<String, Collection> collections;

    public Database(String name) {
        this.name = name;
        this.collections = new HashMap();
    }

    public String getName() {
        return this.name;
    }
    public boolean createCollection(String name) {
        if(this.collections.containsKey(name)) {
            return false;
        }
        this.collections.put(name, new Collection());
        return true;
    }

    public boolean updateCollection(String name, Collection collection) {
        if(!this.collections.containsKey(name)) {
            return false;
        }
        this.collections.put(name, collection);
        return true;
    }

    public boolean dropCollection(String name) {
        if(!this.collections.containsKey(name)) {
            return false;
        }
        this.collections.remove(name);
        return true;
    }

    public Collection getCollection(String name) {
        return this.collections.get(name);
    }

    public HashMap<String, Collection> getCollections(){
        return this.collections;
    }

    public String toString() {
        return this.name + " with " + this.collections.size() + " collections";
    }
}
