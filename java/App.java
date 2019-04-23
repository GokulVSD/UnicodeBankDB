import dbdatabase.*;
import static spark.Spark.*;

public class App {

    public static void main(String[] args) {
        staticFileLocation("/public");
        CustomerDB c = new DBD();
        AccountDB d = new DBD();
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
            return Templates.loginfail;
        });
    }
}
