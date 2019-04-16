package dbdatabase;

public interface AccountDB {

    public boolean getStatus();

    public void createNewAccount(String customerID,String accountNo);

    public String getAccountDetail(String accountNo,String key);

    public void appendAccountLog(String accountNo,String log);

    public String[] getLogs(String accountNo);

    public void editAccountDetail(String accountNo,String key,String value);

    public void deleteAccount(String accountNo);

    public boolean isAccountOpen(String accountNo);

    public boolean doesAccountExist(String accountNo);

    public String[] getListofAccounts(String customerID);

    public void closeAccount(String customerID,String accountNo);

    public void reopenAccount(String customerID,String accountNo);
}
