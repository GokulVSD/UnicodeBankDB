package dbdatabase.account;

import dbdatabase.customer.Customer;
import dbdatabase.index.Index;

import java.util.LinkedList;
import java.util.StringTokenizer;

public class Account extends Customer {

    protected String accountNo;
    protected String account;

    public Account(String accountNo) throws Exception {
        super(new Index().getCustomerID(accountNo));
        this.accountNo = accountNo;
        account = super.getAccount(accountNo);
        if(account == null){
            super.close();
            throw new Exception("DBDatabase: Account does not exist");
        }
    }

    public String getAccountDetail(String key){
        String accDetails = new StringTokenizer(account,"!").nextToken();
        StringTokenizer st = new StringTokenizer(accDetails,",");
        while (st.hasMoreTokens()){
            StringTokenizer pair = new StringTokenizer(st.nextToken(),"=");
            if(pair.nextToken().equals(key)) return pair.nextToken();
        }
        return null;
    }

    public void appendAccountLog(String log){
        account = account.substring(0,account.lastIndexOf("!"));
        if(account.charAt(account.length() - 2) == '!')
            account = account.substring(0,account.lastIndexOf(","));
        account += log + "," + "!";
    }

    public String[] getLogs(){
        LinkedList<String> logs = new LinkedList<>();
        StringTokenizer st = new StringTokenizer(account,"!");
        st.nextToken();
        StringTokenizer logTokens = new StringTokenizer(st.nextToken(),",");
        while (logTokens.hasMoreTokens())
            logs.add(logTokens.nextToken());
        if(logs.isEmpty())
            return null;
        return logs.toArray(new String[logs.size()]);
    }

    public void save(){
        super.writeAccount(accountNo,account);
        super.close();
    }
}

