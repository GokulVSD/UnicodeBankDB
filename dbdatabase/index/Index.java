package dbdatabase.index;

import dbdatabase.DB;
import dbdatabase.FileCreator;

import java.io.File;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Index extends FileCreator{

    private LinkedList<String> entries;

    public Index()throws Exception{
        super("index.txt");
        if(DB.isIndexBeingUsed())
            throw new Exception("DBDatabase: Index file already open, please call object.close() or object.save() after using the object");
        DB.useIndex();
        entries = new LinkedList<>();
        readEntries();
    }

    private void readEntries()throws Exception{
        String homeDir = System.getProperty("user.home");
        String dir = homeDir + File.separator + "Documents" + File.separator + "DBDatabase";
        File file = new File(dir, "index.txt");
        Scanner sc;
        try {
            sc = new Scanner(file);
        } catch (Exception e){
            DB.doneUsingIndex();
            throw new Exception("DBDatabase: Index file does not exist");
        }
        sc.useDelimiter("\\Z");
        String content = sc.next().trim();
        StringTokenizer st = new StringTokenizer(content,",");
        while(st.hasMoreTokens())
            entries.add(st.nextToken().trim());
    }

    public boolean isAccountOpen(String accountNo){
        for(String s:entries){
            StringTokenizer st = new StringTokenizer(s,":");
            st.nextToken(); st.nextToken();
            while(st.hasMoreTokens()){
                if(st.nextToken().equals(accountNo))
                    return st.nextToken().equals("open");
                st.nextToken();
            }
        }
        return false;
    }

    public boolean doesAccountExist(String accountNo){
        for(String s:entries)
            if(s.contains(accountNo)) return true;
        return false;
    }

    public boolean isCustomerDeactivated(String customerID){
        for(String s:entries){
            StringTokenizer st = new StringTokenizer(s,":");
            if(st.nextToken().equals(customerID))
                return st.nextToken().equals("deactivated");
        }
        return true;
    }

    public boolean doesCustomerExist(String customerID){
        for(String s:entries)
            if(s.contains(customerID)) return true;
        return false;
    }

    public String[] getListOfAccounts(String customerID){
        for(String s:entries){
            StringTokenizer st = new StringTokenizer(s,":");
            if(st.nextToken().equals(customerID)){
                st.nextToken();
                LinkedList<String> listOfAccounts = new LinkedList<>();
                while (st.hasMoreTokens()){
                    listOfAccounts.add(st.nextToken());
                    st.nextToken();
                }
                if(listOfAccounts.isEmpty())
                    return null;
                return listOfAccounts.toArray(new String[listOfAccounts.size()]);
            }
        }
        return null;
    }

    public String getCustomerID(String accountNo) throws Exception{
        for(String s:entries){
            if(s.contains(accountNo)){
                close();
                return new StringTokenizer(s,":").nextToken();
            }
        }
        close();
        throw new Exception("DBDatabase: Account does not exist");
    }

    protected String getCustomerEntry(String customerID) throws Exception{
        for(String s:entries){
            StringTokenizer st = new StringTokenizer(s,":");
            if(st.nextToken().equals(customerID)){
                return s;
            }
        }
        return null;
    }

    protected void writeCustomerEntry(String customerID,String entry){
        for(String s:entries){
            StringTokenizer st = new StringTokenizer(s,":");
            if(st.nextToken().equals(customerID)){
                entries.removeFirstOccurrence(s);
                entries.add(entry);
                return;
            }
        }
        entries.add(entry);
    }

    public void close(){
        writeEntries();
    }

    private void writeEntries(){
        StringBuilder sb = new StringBuilder("");
        for(String s:entries) {
            sb.append(s);
            sb.append(",");
        }
        super.content = sb.toString();
        super.createFile();
        DB.doneUsingIndex();
    }
}
