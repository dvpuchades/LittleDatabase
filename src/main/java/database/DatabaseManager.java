package database;

import database.model.Database;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager implements Serializable {
    private static DatabaseManager instance;
    private List<Database> databases;

    private DatabaseManager() {
        try {
            FileInputStream fileInputStream = new FileInputStream("data.ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            instance = (DatabaseManager) objectInputStream.readObject();
            databases = instance.getDatabases();
            instance = this;
            objectInputStream.close();
        }
        catch (Exception e) {
            databases = new ArrayList();
            instance = this;
        }
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("data.ser");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(instance);
            objectOutputStream.flush();
            objectOutputStream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return instance;
    }

    public List<Database> getDatabases() {
        return databases;
    }

    public void addDatabase(Database database) {
        databases.add(database);
    }

    public void removeDatabase(Database database) {
        databases.remove(database);
    }

    public void updateDatabase(Database database) {
        int index = databases.indexOf(database);
        databases.set(index, database);
    }
}
