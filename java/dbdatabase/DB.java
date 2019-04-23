package dbdatabase;

import java.util.LinkedList;

public class DB {

    private static LinkedList<String> customersCurrentlyBeingUsed = new LinkedList<>();
    private static boolean indexFileBeingUsed = false;

    public static void useIndex() {
        indexFileBeingUsed = true;
    }

    public static void doneUsingIndex() {
        indexFileBeingUsed = false;
    }

    public static void useCustomer(String CustomerID) {
        customersCurrentlyBeingUsed.add(CustomerID);
    }

    public static void doneUsingCustomer(String CustomerID) {
        customersCurrentlyBeingUsed.removeFirstOccurrence(CustomerID);
    }

    public static boolean isIndexBeingUsed() {
        return indexFileBeingUsed;
    }

    public static boolean isCustomerBeingUsed(String CustomerID) {
        if (customersCurrentlyBeingUsed.contains(CustomerID)) return true;
        else return false;
    }
}
