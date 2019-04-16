import dbdatabase.*;
import static spark.Spark.*;

public class App {

    public static void main(String[] args) {
        staticFileLocation("/public");
        CustomerDB c = new DBD();
        AccountDB d = new DBD();
        get("/", (req, res) -> {
            res.redirect("/login.html");
            return " ";
        });

    }
}
