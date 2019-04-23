import dbdatabase.*;
import static spark.Spark.*;

public class App {

    public static void main(String[] args) {
        staticFileLocation("/public");
        CustomerDB c = new DBD();
        AccountDB d = new DBD();
        c.createNewCustomer("administrator");
        get("/", (req, res) -> {
            res.redirect("/login.html");
            return " ";
        });
        post("/login", (req, res) -> {
            String username = req.queryParams("username");
            String password = req.queryParams("password");

            if(username.equals("administrator") && password.equals("123")){
                return " ";
            }
        });
    }
}
