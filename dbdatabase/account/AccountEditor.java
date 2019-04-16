package dbdatabase.account;

import java.util.StringTokenizer;

public class AccountEditor extends Account {

    public AccountEditor(String accountNo)throws Exception {
        super(accountNo);
    }

    public void editAccountDetail(String key,String value){
        boolean doesNotExist=true;
        StringTokenizer st = new StringTokenizer(super.account,"!");
        StringBuilder sb = new StringBuilder("");
        StringTokenizer acc = new StringTokenizer(st.nextToken(),",");
        while (acc.hasMoreTokens()){
            String token = acc.nextToken();
            if(new StringTokenizer(token,"=").nextToken().equals(key)){
                doesNotExist=false;
                sb.append(key);
                sb.append("=");
                sb.append(value);
                sb.append(",");
            }
            else{
                sb.append(token);
                sb.append(",");
            }
        }
        if(doesNotExist){
            sb.append(key);
            sb.append("=");
            sb.append(value);
            sb.append(",");
        }
        sb.append("!");
        sb.append(st.nextToken());
        sb.append("!");
        super.account = sb.toString();
    }
}
