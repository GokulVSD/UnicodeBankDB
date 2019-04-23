package dbdatabase.customer;

import java.util.StringTokenizer;

public class CustomerEditor extends Customer {

    public CustomerEditor(String customerID)throws Exception {
        super(customerID);
    }

    public void editCustomerDetail(String key,String value){
        boolean doesNotExist = true;
        StringBuilder sb = new StringBuilder("");
        StringTokenizer st = new StringTokenizer(super.details,";");
        while(st.hasMoreTokens()){
            String token = st.nextToken();
            if(new StringTokenizer(token,":").nextToken().equals(key)){
                sb.append(key);
                sb.append(":");
                sb.append(value);
                sb.append(";");
                doesNotExist = false;
            }
            else {
                sb.append(token);
                sb.append(";");
            }
        }
        super.details = sb.toString();
        if(doesNotExist)
            super.details += key + ":" + value + ";";
        if(super.details.equals("")) super.details = ";";
    }

    public void save(){
        super.close();
    }
}
