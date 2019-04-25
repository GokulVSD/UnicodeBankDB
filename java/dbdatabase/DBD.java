package dbdatabase;

import dbdatabase.account.Account;
import dbdatabase.account.AccountCreator;
import dbdatabase.account.AccountDeleter;
import dbdatabase.account.AccountEditor;
import dbdatabase.customer.Customer;
import dbdatabase.customer.CustomerCreator;
import dbdatabase.customer.CustomerEditor;
import dbdatabase.index.Index;
import dbdatabase.index.IndexEntry;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class DBD implements AccountDB,CustomerDB {

    private File file;
    public boolean status;

    public DBD(){
        String homeDir = System.getProperty("user.home");
        String dir = homeDir + File.separator + "Documents" + File.separator + "DBDatabase";
        file = new File(dir,"DBLogs.txt");
    }

    public boolean appendDBDLog(String s){
        try {
            FileWriter fW = new FileWriter(file,true);
            String time = "" + LocalDateTime.now();
            fW.write(time.substring(0,time.indexOf("T")) + " " + time.substring(time.indexOf("T") + 1,time.lastIndexOf(".")) + ": " + s + "\n");
            fW.close();
            return true;
        } catch (Exception e){
            return false;
        }
    }

    private boolean shouldIWait(String s){
        if(s == null) return false;
        switch (s){
            case "DBDatabase: Can't delete, Index file currently being used":
            case "DBDatabase: Can't create new account since Index file is currently being used":
            case "DBDatabase: Can't create new customer since Index file is currently being used":
            case "DBDatabase: Index file already open, please call object.close() or object.save() after using the object":
            case "DBDatabase: Customer file already open, please call object.close() or object.save() after using the object": return true;
        }
        return false;
    }

    private boolean waiting(){
        try {
            Thread.sleep(200);
        } catch (Exception e){
            return false;
        }
        return true;
    }

    public boolean getStatus(){
        return status;
    }

    public void createNewCustomer(String customerID){
        while (true) {
            try {
                new CustomerCreator(customerID);
            } catch (Exception e) {
                if(shouldIWait(e.getMessage())){
                    if(!waiting()){
                        status = false;
                        return;
                    }
                    continue;
                }
                appendDBDLog(e.getMessage()==null?e.toString():e.getMessage());
                status = false;
                return;
            }
            status = true;
            return;
        }
    }

    public String getCustomerDetail(String customerID,String key){
        while (true) {
            try {
                String var;
                Customer c = new Customer(customerID);
                var = c.getCustomerDetail(key);
                c.close();
                status = true;
                return var;

            } catch (Exception e) {
                if (shouldIWait(e.getMessage())) {
                    if (!waiting()) {
                        status = false;
                        return null;
                    }
                    continue;
                }
                appendDBDLog(e.getMessage()==null?e.toString():e.getMessage());
                status = false;
                return null;
            }
        }
    }

    public void editCustomerDetail(String customerID,String key,String value){
        while (true) {
            try {
                CustomerEditor cE = new CustomerEditor(customerID);
                cE.editCustomerDetail(key,value);
                cE.save();
            } catch (Exception e) {
                if(shouldIWait(e.getMessage())){
                    if(!waiting()){
                        status = false;
                        return;
                    }
                    continue;
                }
                appendDBDLog(e.getMessage()==null?e.toString():e.getMessage());
                status = false;
                return;
            }
            status = true;
            return;
        }
    }

    public void createNewAccount(String customerID,String accountNo) {
        while (true) {
            try {
                new AccountCreator(customerID, accountNo);
            } catch (Exception e) {
                if (shouldIWait(e.getMessage())) {
                    if (!waiting()) {
                        status = false;
                        return;
                    }
                    continue;
                }
                appendDBDLog(e.getMessage()==null?e.toString():e.getMessage());
                status = false;
                return;
            }
            status = true;
            return;
        }
    }

    public String getAccountDetail(String accountNo,String key){
        while (true) {
            try {
                String var;
                Account a = new Account(accountNo);
                var = a.getAccountDetail(key);
                a.close();
                status = true;
                return var;

            } catch (Exception e) {
                if (shouldIWait(e.getMessage())) {
                    if (!waiting()) {
                        status = false;
                        return null;
                    }
                    continue;
                }
                appendDBDLog(e.getMessage()==null?e.toString():e.getMessage());
                status = false;
                return null;
            }
        }
    }

    public void appendAccountLog(String accountNo,String log) {
        while (true) {
            try {
                Account a = new Account(accountNo);
                a.appendAccountLog(log);
                a.save();
            } catch (Exception e) {
                if (shouldIWait(e.getMessage())) {
                    if (!waiting()) {
                        status = false;
                        return;
                    }
                    continue;
                }
                appendDBDLog(e.getMessage()==null?e.toString():e.getMessage());
                status = false;
                return;
            }
            status = true;
            return;
        }
    }

    public String[] getLogs(String accountNo){
        while (true) {
            try {
                String[] var;
                Account a = new Account(accountNo);
                var = a.getLogs();
                a.close();
                status = true;
                return var;

            } catch (Exception e) {
                if (shouldIWait(e.getMessage())) {
                    if (!waiting()) {
                        status = false;
                        return null;
                    }
                    continue;
                }
                appendDBDLog(e.getMessage()==null?e.toString():e.getMessage());
                status = false;
                return null;
            }
        }
    }

    public void editAccountDetail(String accountNo,String key,String value) {
        while (true) {
            try {
                AccountEditor aE = new AccountEditor(accountNo);
                aE.editAccountDetail(key,value);
                aE.save();
            } catch (Exception e) {
                if (shouldIWait(e.getMessage())) {
                    if (!waiting()) {
                        status = false;
                        return;
                    }
                    continue;
                }
                appendDBDLog(e.getMessage()==null?e.toString():e.getMessage());
                status = false;
                return;
            }
            status = true;
            return;
        }
    }

    public void deleteAccount(String accountNo) {
        while (true) {
            try {
                new AccountDeleter(accountNo);
            } catch (Exception e) {
                if (shouldIWait(e.getMessage())) {
                    if (!waiting()) {
                        status = false;
                        return;
                    }
                    continue;
                }
                appendDBDLog(e.getMessage()==null?e.toString():e.getMessage());
                status = false;
                return;
            }
            status = true;
            return;
        }
    }

    public boolean isAccountOpen(String accountNo){
        while (true) {
            try {
                boolean flag;
                Index i = new Index();
                flag = i.isAccountOpen(accountNo);
                i.close();
                status = true;
                return flag;

            } catch (Exception e) {
                if (shouldIWait(e.getMessage())) {
                    if (!waiting()) {
                        status = false;
                        return false;
                    }
                    continue;
                }
                appendDBDLog(e.getMessage()==null?e.toString():e.getMessage());
                status = false;
                return false;
            }
        }
    }

    public boolean doesAccountExist(String accountNo){
        while (true) {
            try {
                boolean flag;
                Index i = new Index();
                flag = i.doesAccountExist(accountNo);
                i.close();
                status = true;
                return flag;

            } catch (Exception e) {
                if (shouldIWait(e.getMessage())) {
                    if (!waiting()) {
                        status = false;
                        return false;
                    }
                    continue;
                }
                appendDBDLog(e.getMessage()==null?e.toString():e.getMessage());
                status = false;
                return false;
            }
        }
    }

    public boolean isCustomerDeactivated(String customerID){
        while (true) {
            try {
                boolean flag;
                Index i = new Index();
                flag = i.isCustomerDeactivated(customerID);
                i.close();
                status = true;
                return flag;

            } catch (Exception e) {
                if (shouldIWait(e.getMessage())) {
                    if (!waiting()) {
                        status = false;
                        return false;
                    }
                    continue;
                }
                appendDBDLog(e.getMessage()==null?e.toString():e.getMessage());
                status = false;
                return false;
            }
        }
    }

    public boolean doesCustomerExist(String customerID){
        while (true) {
            try {
                boolean flag;
                Index i = new Index();
                flag = i.doesCustomerExist(customerID);
                i.close();
                status = true;
                return flag;

            } catch (Exception e) {
                if (shouldIWait(e.getMessage())) {
                    if (!waiting()) {
                        status = false;
                        return false;
                    }
                    continue;
                }
                appendDBDLog(e.getMessage()==null?e.toString():e.getMessage());
                status = false;
                return false;
            }
        }
    }

    public String[] getListofAccounts(String customerID){
        while (true) {
            try {
                String[] var;
                Index i = new Index();
                var = i.getListOfAccounts(customerID);
                i.close();
                status = true;
                return var;

            } catch (Exception e) {
                if (shouldIWait(e.getMessage())) {
                    if (!waiting()) {
                        status = false;
                        return null;
                    }
                    continue;
                }
                appendDBDLog(e.getMessage()==null?e.toString():e.getMessage());
                status = false;
                return null;
            }
        }
    }

    public void closeAccount(String customerID,String accountNo){
        while (true) {
            try {
                IndexEntry iE = new IndexEntry(customerID);
                iE.closeAccount(accountNo);
                iE.save();
            } catch (Exception e) {
                if (shouldIWait(e.getMessage())) {
                    if (!waiting()) {
                        status = false;
                        return;
                    }
                    continue;
                }
                appendDBDLog(e.getMessage()==null?e.toString():e.getMessage());
                status = false;
                return;
            }
            status = true;
            return;
        }
    }

    public void reopenAccount(String customerID,String accountNo){
        while (true) {
            try {
                IndexEntry iE = new IndexEntry(customerID);
                iE.reopenAccount(accountNo);
                iE.save();
            } catch (Exception e) {
                if (shouldIWait(e.getMessage())) {
                    if (!waiting()) {
                        status = false;
                        return;
                    }
                    continue;
                }
                appendDBDLog(e.getMessage()==null?e.toString():e.getMessage());
                status = false;
                return;
            }
            status = true;
            return;
        }
    }

    public void activateCustomer(String customerID){
        while (true) {
            try {
                IndexEntry iE = new IndexEntry(customerID);
                iE.activateCustomer();
                iE.save();
            } catch (Exception e) {
                if (shouldIWait(e.getMessage())) {
                    if (!waiting()) {
                        status = false;
                        return;
                    }
                    continue;
                }
                appendDBDLog(e.getMessage()==null?e.toString():e.getMessage());
                status = false;
                return;
            }
            status = true;
            return;
        }
    }

    public void deactivateCustomer(String customerID){
        while (true) {
            try {
                IndexEntry iE = new IndexEntry(customerID);
                iE.deactivateCustomer();
                iE.save();
            } catch (Exception e) {
                if (shouldIWait(e.getMessage())) {
                    if (!waiting()) {
                        status = false;
                        return;
                    }
                    continue;
                }
                appendDBDLog(e.getMessage()==null?e.toString():e.getMessage());
                status = false;
                return;
            }
            status = true;
            return;
        }
    }

    public LinkedList<String> getListOfAllCustomers(){
        while (true) {
            try {
                LinkedList<String> customers = new LinkedList<>();
                Index i = new Index();
                for(String s : i.entries){
                    StringTokenizer st = new StringTokenizer(s,":");
                    customers.add(st.nextToken());
                }
                i.close();
                status = true;
                return customers;

            } catch (Exception e) {
                if (shouldIWait(e.getMessage())) {
                    if (!waiting()) {
                        status = false;
                        return null;
                    }
                    continue;
                }
                appendDBDLog(e.getMessage()==null?e.toString():e.getMessage());
                status = false;
                return null;
            }
        }
    }
}
