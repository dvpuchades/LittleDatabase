import database.DatabaseManager;
import database.model.Collection;
import database.model.Database;
import database.model.Document;

import javax.swing.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;

public class GUI {
    private JFrame frame;
    private JPanel panel;
    private JPanel mainPanel;
    private JPanel collectionPanel;
    private JPanel documentPanel;

    private JPanel sideBar;
    private JButton addButton;

    private int width = 1400;
    private int height = 450;

    public void display(){
        frame = new JFrame();
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));

        // Side Bar
        sideBar = new JPanel();
        JLabel label = new JLabel("Database Manager");
        addButton = new JButton("Add Database");
        JButton importButton = new JButton("Import");
        JButton exportButton = new JButton("Export");

        addButton.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(frame, "Enter database name");
            DatabaseManager.getInstance().addDatabase(new Database(name));
            this.updateMainPanel();
        });

        importButton.addActionListener(e -> {
            try{
                String url = "rmi:///";
                WrapperInterface wrapper = (WrapperInterface) Naming.lookup(url+"databases");
                List<Database> databases = (List<Database>) wrapper.getObject();
                for(Database database : databases){
                    DatabaseManager.getInstance().addDatabase(database);
                }
                updateMainPanel();
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        });

        exportButton.addActionListener(e -> {
            try {
                WrapperInterface wrapper = new Wrapper(DatabaseManager.getInstance().getDatabases());
                Naming.rebind("databases", wrapper);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        sideBar.setLayout(new BoxLayout(sideBar, BoxLayout.PAGE_AXIS));

        sideBar.add(label);
        sideBar.add(addButton);
        sideBar.add(importButton);
        sideBar.add(exportButton);


        panel.add(sideBar);

        //Main Panel
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

        insertDatabasesInPanel();

        panel.add(mainPanel);

        //collection panel
        collectionPanel = new JPanel();
        collectionPanel.setLayout(new BoxLayout(collectionPanel, BoxLayout.PAGE_AXIS));
        panel.add(collectionPanel);

        //document panel
        documentPanel = new JPanel();
        documentPanel.setLayout(new BoxLayout(documentPanel, BoxLayout.PAGE_AXIS));
        panel.add(documentPanel);

        //show it
        frame.add(panel);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void updateMainPanel(){
        panel.remove(documentPanel);
        panel.remove(collectionPanel);
        frame.remove(panel);
        panel.remove(mainPanel);
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

        insertDatabasesInPanel();

        panel.add(mainPanel);
        //collection panel
        if(collectionPanel == null) {
            collectionPanel = new JPanel();
            collectionPanel.setLayout(new BoxLayout(collectionPanel, BoxLayout.PAGE_AXIS));
            panel.add(collectionPanel);
        }

        if(documentPanel == null) {
            documentPanel = new JPanel();
            documentPanel.setLayout(new BoxLayout(documentPanel, BoxLayout.PAGE_AXIS));
            panel.add(documentPanel);
        }

        frame.add(panel);
        frame.setVisible(true);
    }

    private void showDatabase(Database database){
        updateMainPanel();
        panel.remove(collectionPanel);

        collectionPanel = new JPanel();
        collectionPanel.setLayout(new BoxLayout(collectionPanel, BoxLayout.PAGE_AXIS));
        collectionPanel.add(new JLabel(database.getName()));

        HashMap<String, Collection> collections = database.getCollections();
        for (String collectionName : collections.keySet()) {
            JPanel collectionButtonPanel = new JPanel();
            JButton collectionButton = new JButton(collectionName);
            JButton createDocumentButton = new JButton("Create Document");
            JButton deleteCollectionButton = new JButton("Delete Collection");

            collectionButton.addActionListener(e -> {
                this.showCollection(database, collectionName);
            });

            createDocumentButton.addActionListener(e -> {
                String documentContent = JOptionPane.showInputDialog(frame, "Enter document content in JSON format");
                try {
                    Document document = new Document(documentContent);
                    Collection collection = collections.get(collectionName);
                    collection.add(document);
                    database.updateCollection(collectionName, collection);
                    DatabaseManager.getInstance().updateDatabase(database);
                    showCollection(database, collectionName);
                }
                catch(Exception ex){
                    JOptionPane.showMessageDialog(frame, "Please enter a valid JSON document");
                }
            });

            deleteCollectionButton.addActionListener(e -> {
                database.dropCollection(collectionName);
                DatabaseManager.getInstance().updateDatabase(database);
                showDatabase(database);
            });

            collectionButtonPanel.add(collectionButton);
            collectionButtonPanel.add(createDocumentButton);
            collectionButtonPanel.add(deleteCollectionButton);
            collectionPanel.add(collectionButtonPanel);
        }
        panel.add(collectionPanel);
        SwingUtilities.updateComponentTreeUI(frame);
    }

    private void showCollection(Database database, String collectionName){
        updateMainPanel();
        showDatabase(database);
        panel.remove(documentPanel);
        documentPanel = new JPanel();
        documentPanel.add(new JLabel(collectionName));
        documentPanel.setLayout(new BoxLayout(documentPanel, BoxLayout.PAGE_AXIS));
        Collection collection = database.getCollection(collectionName);
        SortedMap<Integer, Document> documents = collection.getDocuments();
        for (Integer index : documents.keySet()) {
            Document document = documents.get(index);
            JPanel docPane = new JPanel();
            JLabel documentLabel = new JLabel(document.toString());
            JButton updateButton = new JButton("Update");
            JButton deleteButton = new JButton("Delete");

            updateButton.addActionListener(e -> {
                String documentContent = JOptionPane.showInputDialog(frame, "Enter document content in JSON format");
                try {
                    Document newDocument = new Document(documentContent);
                    collection.update(index, newDocument);
                    database.updateCollection(collectionName, collection);
                    DatabaseManager.getInstance().updateDatabase(database);
                    showCollection(database, collectionName);
                }
                catch(Exception ex){
                    JOptionPane.showMessageDialog(frame, "Document don't update. Please enter a valid JSON document");
                }
            });

            deleteButton.addActionListener(e -> {
                collection.delete(index);
                database.updateCollection(collectionName, collection);
                DatabaseManager.getInstance().updateDatabase(database);
                showCollection(database, collectionName);
            });

            docPane.setLayout(new BoxLayout(docPane, BoxLayout.PAGE_AXIS));

            docPane.add(documentLabel);

            JPanel operationPanel = new JPanel();
            operationPanel.add(updateButton);
            operationPanel.add(deleteButton);

            docPane.add(operationPanel);
            documentPanel.add(docPane);
        }
        panel.add(documentPanel);
        SwingUtilities.updateComponentTreeUI(frame);
    }

    private void insertDatabasesInPanel(){
        mainPanel.add(new JLabel("Databases"));
        List<Database> databases = DatabaseManager.getInstance().getDatabases();
        for (Database database : databases) {
            JButton showDatabaseButton = new JButton(database.toString());
            showDatabaseButton.addActionListener(e -> this.showDatabase(database));

            JButton createCollectionButton = new JButton("Create Collection");
            createCollectionButton.addActionListener(e -> {
                String name = JOptionPane.showInputDialog(frame, "Enter collection name");
                database.createCollection(name);
                DatabaseManager.getInstance().updateDatabase(database);
                this.showDatabase(database);
            });

            JButton removeDatabaseButton = new JButton("Remove Database");
            removeDatabaseButton.addActionListener(e -> {
                DatabaseManager.getInstance().removeDatabase(database);
                this.updateMainPanel();
            });

            JPanel databaseButtonPanel = new JPanel();
            databaseButtonPanel.setLayout(new BoxLayout(databaseButtonPanel, BoxLayout.LINE_AXIS));
            databaseButtonPanel.add(showDatabaseButton);
            databaseButtonPanel.add(createCollectionButton);
            databaseButtonPanel.add(removeDatabaseButton);

            mainPanel.add(databaseButtonPanel);
        }
    }
}
