package dbdatabase;

public interface CustomerDB {

    public boolean getStatus();

    public void createNewCustomer(String customerID);

    public String getCustomerDetail(String customerID,String key);

    public void editCustomerDetail(String customerID,String key,String value);

    public boolean isCustomerDeactivated(String customerID);

    public boolean doesCustomerExist(String customerID);

    public String[] getListofAccounts(String customerID);

    public void activateCustomer(String customerID);

    public void deactivateCustomer(String customerID);
}
