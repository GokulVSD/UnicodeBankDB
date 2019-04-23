package dbdatabase.customer;

import dbdatabase.FileCreator;
import dbdatabase.index.IndexCreator;
import dbdatabase.index.IndexEntry;

public class CustomerCreator extends FileCreator {

    public CustomerCreator(String customerID) throws Exception {
        super(customerID + ".txt");
        initCustomerStructure();
        IndexEntry iE;
        boolean entryExists = false;
        try{
            iE = new IndexEntry(customerID);
            entryExists = iE.doesEntryExist();
            iE.save();
        } catch (Exception e){
            if(e.getMessage().equals("DBDatabase: Index file already open, please call object.close() or object.save() after using the object"))
                throw new Exception("DBDatabase: Can't create new customer since Index file is currently being used");
            if(e.getMessage().equals("DBDatabase: Index file does not exist"))
                new IndexCreator(customerID);
        }
        if(entryExists)
            throw new Exception("DBDatabase: Customer already exists");
        super.createFile();
    }

    private void initCustomerStructure(){
        super.content = ";" + "|" + ";" + "|";
    }
}
