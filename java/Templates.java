public class Templates {
    static String administrator = "<button onclick=\"login()\" class=\"btn btn-warning\">Reset</button>\n" +
            "<h5 style=\"display: inline-block; vertical-align: middle; margin-left: 25px; margin-right: 25px; margin-top: 28px; font-size: 32px; border: 3px solid #FFFFFF; padding-bottom: 6px; padding-top: 3px; padding-left: 8px; padding-right: 8px; border-radius: 20px\">Admin</h5>\n" +
            "<button onclick=\"location.href='./'\" class=\"btn btn-warning\">Logout</button>\n" +
            "<div id=\"options\">\n" +
            "\t<button onclick=\"createCustomer()\">Create Customer</button>\n" +
            "\t<button onclick=\"getSystemLogs()\">System Logs</button>\n" +
            "\t<button onclick=\"manageCustomer()\">Manage Customers</button>\n" +
            "\t<button onclick=\"stateTransfer()\">Backup/Restore System</button>\n" +
            "</div>\n" +
            "<div id=\"admin-dynamic-1\"></div>\n" +
            "<div id=\"admin-dynamic-2\"></div>\n" +
            "<div id=\"admin-dynamic-3\"></div>\n" +
            "<div id=\"admin-dynamic-4\"></div>\n" +
            "<div id=\"admin-dynamic-5\"></div>";

    static String loginfail = "\n" +
            "<h4 style=\"display: block; margin-bottom: 50px\">Incorrect Credentials</h4>\n" +
            "<button onclick=\"location.href='./'\" class=\"btn btn-warning\">Try Again</button>\n";

    static String logindeactivated = "\n" +
            "<h4 style=\"display: block; margin-bottom: 50px\">Customer is Deactivated, Contact Administrator</h4>\n" +
            "<button onclick=\"location.href='./'\" class=\"btn btn-warning\">Try Again</button>\n";

    static String createcustomer = "<h4>Create New Customer</h4>\n" +
            "<input type=\"text\" name=\"custname\" placeholder=\"Customer ID\">\n" +
            "<input type=\"text\" name=\"custnm\" placeholder=\"Name\">\n" +
            "<h5>Access Level</h5>\n" +
            "<button class='alevelbtn' onclick=\'custAccessLevel(\"0\")\'>Regular</button>\n" +
            "<button class='alevelbtn' onclick=\'custAccessLevel(\"1\")\'>Administrator</button>\n" +
            "<input type=\"password\" name=\"crcuspass\" placeholder=\"Password\">\n" +
            "<button onclick=\"submitCustCreate()\">Create</button>";

    static String createaccount(String custname){
        return "<h4>Create New Account</h4>\n" +
                "<input type=\"text\" name=\"accname\" placeholder=\"Account Name\">\n" +
                "<h5>Account Type</h5>\n" +
                "<button class='acctypebtn' onclick=\'setAccType(\"transaction\")\'>Transaction</button>\n" +
                "<button class='acctypebtn' onclick=\'setAccType(\"savings\")\'>Savings</button>\n" +
                "<div></div>" +
                "<button onclick=\"submitCreateAccount(\'" + custname + "\')\">Create</button>";
    }
}
