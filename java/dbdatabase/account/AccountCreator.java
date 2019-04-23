package dbdatabase.account;

import dbdatabase.customer.Customer;
import dbdatabase.index.IndexEntry;

public class AccountCreator extends Customer {

    private String account;
    private String accountNo;

    public AccountCreator(String customerID,String accountNo)throws Exception {
        super(customerID);
        try{
            IndexEntry iE = new IndexEntry(customerID);
            iE.addAccount(accountNo);
            iE.save();
        } catch (Exception e){
            super.close();
            if(e.getMessage().equals("DBDatabase: Account already exists"))
                throw e;
            throw new Exception("DBDatabase: Can't create new account since Index file is currently being used");
        }
        this.accountNo = accountNo;
        initStructure();
        save();
    }

    private void initStructure(){
        account = "," + "!" + "," + "!";
    }

    private void save(){
        super.writeAccount(accountNo,account);
        super.close();
    }
}
