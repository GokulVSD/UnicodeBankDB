import dbdatabase.*;
import jdk.vm.ci.meta.Local;

import java.io.File;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Random;
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
            String time = "" + LocalDateTime.now();
            c.editCustomerDetail("admin","createdon",time.substring(0,time.indexOf("T")) + " " + time.substring(time.indexOf("T") + 1,time.lastIndexOf(".")).replace(':','@'));
            c.editCustomerDetail("admin","accessLevel","1");
            c.editCustomerDetail("admin","name","Default_Admin");
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
                if(c.isCustomerDeactivated(username)) {
                    d.appendDBDLog("UnicodeBank: Deactivated customer attempted login: " + username);
                    return Templates.logindeactivated;
                }
                if(c.getCustomerDetail(username,"password").equals(password)){
                    d.appendDBDLog("UnicodeBank: Customer login successful: " + username);
                    String time = "" + LocalDateTime.now();
                    c.editCustomerDetail(username,"lastlogin",time.substring(0,time.indexOf("T")) + " " + time.substring(time.indexOf("T") + 1,time.lastIndexOf(".")).replace(':','@'));
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
            String custnm = req.queryParams("custnm");
            String crcuspass = req.queryParams("crcuspass");
            String accesslevel = req.queryParams("accesslevel");

            if(c.doesCustomerExist(custname)){
                return "<h4></h4><h6>Customer ID already exists</h6>";
            }
            c.createNewCustomer(custname);

            String time = "" + LocalDateTime.now();
            c.editCustomerDetail(custname,"createdon",time.substring(0,time.indexOf("T")) + " " + time.substring(time.indexOf("T") + 1,time.lastIndexOf(".")).replace(':','@'));
            c.editCustomerDetail(custname,"lastlogin","Never");
            c.editCustomerDetail(custname,"password",crcuspass);
            c.editCustomerDetail(custname,"name",custnm);
            c.editCustomerDetail(custname,"accessLevel",accesslevel);
            d.appendDBDLog("UnicodeBank: Created new Customer: " + custname);
            return "<h4></h4><h6>Successfully created Customer with ID: " + custname + "</h6>";
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
            html = "<h4>System Logs</h4><h4></h4> <div style=\"text-align: left; margin-left: 12%;\"" + html;
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
                return "<h4>Customer ID does not exist</h4>";
            String html = "<h4>Customer ID Match</h4>";
            html += getCustomerManagementButton(searchcust,c);
            return html;
        });

        post("/loadcustomerdetails", (req, res) -> {
            String custname = req.queryParams("custname");
            String admin = req.queryParams("privileged");
            String html = "<h4>ID: " + custname + "</h4>";
            html += ("<h5>Name: " + c.getCustomerDetail(custname,"name") + "</h5>");
            html += ("<h6>Created: " + c.getCustomerDetail(custname,"createdon").replace('@',':') + "</h6>");
            html += ("<h6>Last Login: " + c.getCustomerDetail(custname,"lastlogin").replace('@',':') + "</h6>");
            html += ("<h6>Access Level: " + (c.getCustomerDetail(custname,"accessLevel").equals("1")?"Administrator":"Regular") + "</h6>");
            html += ("<h6>Status: " + (c.isCustomerDeactivated(custname)?"Deactive":"Active") + "</h6>");
            html += ("<h4>Options</h4>");
            if(admin.equals("1")){
                html += ("<button class=\"custmanbtns\" onclick=\'changeName(\"" + custname + "\")\'>Change Name</button>");
                html += ("<button class=\"custmanbtns\" onclick=\'changeAccessLevel(\"" + custname + "\")\'>" + (c.getCustomerDetail(custname,"accessLevel").equals("1")?"Demote Access Level":"Promote to Administrator") + "</button>");
            }
            html += ("<button class=\"custmanbtns\" onclick=\'changeCustStatus(\"" + custname + "\")\'>" + (c.isCustomerDeactivated(custname)?"Activate":"Deactivate") + "</button>");
            html += ("<button class=\"custmanbtns\" onclick=\'changeCustPassword(\"" + custname + "\")\'>Change Password</button>");
            html += ("<button class=\"custmanbtns\" onclick=\'getListOfAllAccounts(\"" + custname + "\")\'>View / Modify Accounts</button>");
            html += ("<button class=\"custmanbtns\" onclick=\'getTransferPage(\"" + custname + "\")\'>Transfer Funds</button>");
            return html;
        });

        post("/changename", (req, res) -> {
            String custname = req.queryParams("custname");
            String name = req.queryParams("namechange");
            c.editCustomerDetail(custname,"name",name);
            d.appendDBDLog("UnicodeBank: Password was changed for Customer ID: " + custname);
            return " ";
        });

        post("/changecuststatus", (req, res) -> {
            String custname = req.queryParams("custname");
            if(c.isCustomerDeactivated(custname)) {
                d.appendDBDLog("UnicodeBank: " + custname + "was activated");
                c.activateCustomer(custname);
            }
            else {
                d.appendDBDLog("UnicodeBank: " + custname + "was deactivated");
                c.deactivateCustomer(custname);
            }
            return "<h4>Successfully Changed Status</h4>";
        });

        post("/changepass", (req, res) -> {
            String custname = req.queryParams("custname");
            String password = req.queryParams("passchange");
            c.editCustomerDetail(custname,"password",password);
            d.appendDBDLog("UnicodeBank: Password was changed for Customer ID: " + custname);
            return " ";
        });

        post("/getlistofallaccounts", (req, res) -> {
            String custname = req.queryParams("custname");
            StringBuilder sb = new StringBuilder("<h4>New Account</h4>");
            sb.append("<button class=\"accbtns\" onclick=\'createNewAccount(\"" + custname + "\")\'>Create</button>");
            String[] accounts = a.getListofAccounts(custname);
            sb.append("<h4>Accounts</h4>");
            if(accounts != null)
                for(String s:accounts)
                    sb.append(getAccountButton(s,a));
            else
                sb.append("<h6>No Accounts Exist</h6>");
            return sb.toString();
        });

        post("/getacccreationpage", (req, res) -> {
            String custname = req.queryParams("custname");
            return Templates.createaccount(custname);
        });

        post("/newaccount", (req, res) -> {
            String custname = req.queryParams("custname");
            String accname = req.queryParams("accname");
            String acctype = req.queryParams("acctype");
            String accno;
            Random rnd = new Random();
            do {
                StringBuilder sb = new StringBuilder(10);
                for (int i = 0; i < 10; i++)
                    sb.append((char) ('0' + rnd.nextInt(10)));
                accno = sb.toString();
            } while (a.doesAccountExist(accno));
            a.createNewAccount(custname,accno);
            a.appendAccountLog(accno,"DBDatabase: New account for Customer ID: "+custname+" created with Number: "+accno);
            a.editAccountDetail(accno,"name",accname);
            String time = "" + LocalDateTime.now();
            a.editAccountDetail(accno,"createdon",time.substring(0,time.indexOf("T")) + " " + time.substring(time.indexOf("T") + 1,time.lastIndexOf(".")).replace(':','@'));
            a.editAccountDetail(accno,"balance","0.0");
            a.editAccountDetail(accno,"type",acctype);
            return "<h4></h4><h6>Successfully Created Account with Account Number: " + accno;
        });

        post("/getaccountpage", (req, res) -> {
            String accno = req.queryParams("accno");
            boolean admin = req.queryParams("privileged").equals("1");
            return Templates.accountpage(accno,admin,a);
        });

        post("/alterbalance", (req, res) -> {
            String accno = req.queryParams("accno");
            String balance = req.queryParams("balance");
            a.editAccountDetail(accno,"balance",balance);
            a.appendAccountLog(accno,"UnicodeBank: Admin altered balance for Account Number :"+accno+" to: ₹ "+balance);
            return " ";
        });

        post("/reopenaccount", (req, res) -> {
            String accno = req.queryParams("accno");
            String custname = req.queryParams("custname");
            a.reopenAccount(custname,accno);
            a.appendAccountLog(accno,"UnicodeBank: Admin reopened Account Number: "+accno);
            return " ";
        });

        post("/changeaccname", (req, res) -> {
            String accno = req.queryParams("accno");
            String name = req.queryParams("name");
            a.editAccountDetail(accno,"name",name);
            a.appendAccountLog(accno,"UnicodeBank: Name Changed for Account Number: "+accno);
            return " ";
        });

        post("/closeaccount", (req, res) -> {
            String accno = req.queryParams("accno");
            String custname = req.queryParams("custname");
            a.closeAccount(custname,accno);
            a.appendAccountLog(accno,"UnicodeBank: Closed Account with Number: "+accno);
            return " ";
        });

        post("/getacclogs", (req, res) -> {
            String accno = req.queryParams("accno");
            a.appendAccountLog(accno,"UnicodeBank: Accesed Logs of Account Number: "+accno);
            String[] content = a.getLogs(accno);
            String html = "</div>";
            for(int i=content.length-1;i!=0;i--)
                html = "<h6>" + content[i] + "</h6>" + html;
            html = "<h4>System Logs</h4><h4></h4> <div style=\"text-align: left; margin-left: 12%;\"" + html;
            return html;
        });
    }

    static String getCustomerManagementButton(String custname, CustomerDB c){
        return "<button onclick=\'loadCustomerDetails(\"" + custname + "\")\'><div><h5>Customer ID: " + custname + "</h5></div><div>Access Level: " +
                (c.getCustomerDetail(custname,"accessLevel").equals("1")?"Administrator":"Regular") +
                "</div><div>Status: " + (c.isCustomerDeactivated(custname)?"Deactive":"Active") +
                "</div></button>";
    }

    static String getAccountButton(String accno,AccountDB a){
        return "<button class=\"accbtns\" onclick=\'loadAccountDetails(\"" + accno + "\")\'><div>Name: " + a.getAccountDetail(accno,"name") + "</div>" +
                "<div>Account No: " + accno + "</div><div>Type: " +
                a.getAccountDetail(accno,"type") + "</div><div>Status: " + (a.isAccountOpen(accno)?"Open":"Closed") +
                "</div></button>";
    }
}
