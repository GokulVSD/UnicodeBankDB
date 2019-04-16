package dbdatabase.index;

import dbdatabase.FileCreator;

public class IndexCreator extends FileCreator {

    public IndexCreator(String customerID){
        super("index.txt");
        initIndexStructure(customerID);
        super.createFile();
    }

    private void initIndexStructure(String customerID){
        super.content = customerID + ":" + "activated" + ":" + ",";
    }
}
