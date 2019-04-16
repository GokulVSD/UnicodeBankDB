import dbdatabase.*;
import static spark.Spark.*;

public class App {

    public static void main(String[] args) {
        CustomerDB c = new DBD();
        AccountDB d = new DBD();
        get("/hello", (req, res) -> "Hello World" + c.doesCustomerExist("Gokul"));
    }
}
