import dbdatabase.*;

import java.io.File;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.StringTokenizer;

import static spark.Spark.*;

public class App {

    public static void main(String[] args) {
        staticFileLocation("/public");
        CustomerDB c = new DBD();
        AccountDB a = new DBD();
        DBD d = new DBD();
        c.createNewCustomer("admin");
        // true if wasn't already present
        if(c.getStatus()){
            c.editCustomerDetail("admin","accessLevel","1");
            c.editCustomerDetail("admin","password","123");
        }

        get("/", (req, res) -> {
            res.redirect("/login.html");
            return " ";
        });

        post("/login", (req, res) -> {
            String username = req.queryParams("username");
            String password = req.queryParams("password");

            if(c.doesCustomerExist(username)){
                if(c.getCustomerDetail(username,"password").equals(password)){
                    d.appendDBDLog("UnicodeBank: Customer login successful: " + username);
                    if(c.getCustomerDetail(username,"accessLevel").equals("1")){
                        //admin
                        return Templates.administrator;
                    }
                    else{
                        //regular
                        return "successful regular customer";
                    }
                }
            }
            d.appendDBDLog("UnicodeBank: Customer login failed: " + username);
            return Templates.loginfail;
        });

        get("/createcustomer", (req, res) -> {
            return Templates.createcustomer;
        });

        post("/newcustomer", (req, res) -> {
            String custname = req.queryParams("custname");
            String crcuspass = req.queryParams("crcuspass");
            String accesslevel = req.queryParams("accesslevel");

            if(c.doesCustomerExist(custname)){
                return "<h6>Customer ID already exists</h6>";
            }
            c.createNewCustomer(custname);
            c.editCustomerDetail(custname,"password",crcuspass);
            c.editCustomerDetail(custname,"accessLevel",accesslevel);
            d.appendDBDLog("UnicodeBank: Created new Customer: " + custname);
            return "<h6>Successfully created Customer with ID: " + custname + "</h6>";
        });

        get("/getsystemlogs", (req, res) -> {
            d.appendDBDLog("UnicodeBank: DBD log accessed");
            String homeDir = System.getProperty("user.home");
            String dir = homeDir + File.separator + "Documents" + File.separator + "DBDatabase";
            File file = new File(dir, "DBLogs.txt");
            Scanner sc = new Scanner(file);
            sc.useDelimiter("\\Z");
            String content = sc.next().trim();
            StringTokenizer st = new StringTokenizer(content, "\n");
            String html = "</div>";
            while(st.hasMoreTokens())
                html = "<h6>" + st.nextToken() + "</h6>" + html;
            html = "<h4>System Logs</h4> <div style=\"text-align: left; margin-left: 12%;\"" + html;
            return html;
        });

        get("/getallcustomers", (req, res) -> {
            LinkedList<String> customers = d.getListOfAllCustomers();
            StringBuilder sb = new StringBuilder("<h4>All Customers</h4>");
            for(String s:customers){
                sb.append(getCustomerManagementButton(s,c));
            }
            return sb.toString();
        });

        post("/getonecustomer", (req, res) -> {
            String searchcust = req.queryParams("searchcust");
            if(!c.doesCustomerExist(searchcust))
                return "<h4>Customer ID does not exist<h4>";
            String html = "<h4>Customer ID Match</h4>";
            html += getCustomerManagementButton(searchcust,c);
            return html;
        });
    }

    static String getCustomerManagementButton(String custname, CustomerDB c){
        return "<button onclick=\'loadCustomerDetails(\"" + custname + "\")\'><div>Customer ID: " + custname + "</div><div>Access Level: " +
                (c.getCustomerDetail(custname,"accessLevel").equals("1")?"Administrator":"Regular") +
                "</div><div>Status: " + (c.isCustomerDeactivated(custname)?"Deactivated":"Activated") +
                "</div></button>";
    }
}
