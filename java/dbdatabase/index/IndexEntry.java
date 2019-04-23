package dbdatabase.index;

import java.util.StringTokenizer;

public class IndexEntry extends Index {

    private String entry;
    private String customerID;

    public IndexEntry(String customerID)throws Exception{
        super();
        this.customerID = customerID;
        entry = super.getCustomerEntry(customerID);
    }

    public boolean doesEntryExist(){
        boolean var = entry == null;
        if(var)
            entry = customerID + ":" + "activated" + ":";
        return !var;
    }

    public void addAccount(String accountNo)throws Exception{
        if(entry.contains(accountNo)) {
            save();
            throw new Exception("DBDatabase: Account already exists");
        }
        entry += accountNo + ":" + "open" + ":";
    }

    public void closeAccount(String accountNo){
        if(!entry.contains(accountNo))
            return;
        StringTokenizer st = new StringTokenizer(entry,":");
        st.nextToken();
        while(st.hasMoreTokens()){
            st.nextToken();
            if(st.nextToken().equals(accountNo))
                if(st.nextToken().equals("closed"))
                    return;
        }
        int accountIndex = entry.indexOf(accountNo);
        entry = entry.substring(0,accountIndex) + entry.substring(accountIndex).replaceFirst("open","closed");
    }

    public void reopenAccount(String accountNo){
        if(!entry.contains(accountNo))
            return;
        StringTokenizer st = new StringTokenizer(entry,":");
        st.nextToken();
        while(st.hasMoreTokens()){
            st.nextToken();
            if(st.nextToken().equals(accountNo))
                if(st.nextToken().equals("open"))
                    return;
        }
        int accountIndex = entry.indexOf(accountNo);
        entry = entry.substring(0,accountIndex) + entry.substring(accountIndex).replaceFirst("closed","open");
    }

    public void activateCustomer(){
        entry = entry.replaceFirst("deactivated","activated");
    }

    public void deactivateCustomer(){
        entry = entry.replaceFirst("activated","deactivated");
    }

    public void save(){
        super.writeCustomerEntry(customerID,entry);
        super.close();
    }
}
