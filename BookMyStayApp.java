/**
 * Book My Stay Application
 *
 * Demonstrates basic room modeling using abstraction and inheritance.
 * Version 2.1 – Refactored version introducing Room types.
 *
 * @author Jatin
 * @version 2.1
 */

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("        BOOK MY STAY APP         ");
        System.out.println("      Hotel Booking System       ");
        System.out.println("           Version 2.1           ");
        System.out.println("=================================");

        // Create room objects
        Room singleRoom = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suiteRoom = new SuiteRoom();

        // Static availability
        int singleAvailable = 5;
        int doubleAvailable = 3;
        int suiteAvailable = 2;

        // Display room information
        singleRoom.displayRoomDetails(singleAvailable);
        doubleRoom.displayRoomDetails(doubleAvailable);
        suiteRoom.displayRoomDetails(suiteAvailable);
    }
}


/* ---------- Abstract Room Class ---------- */

abstract class Room {

    protected String roomType;
    protected int beds;
    protected int size;
    protected double price;

    public void displayRoomDetails(int availability) {

        System.out.println("\nRoom Type: " + roomType);
        System.out.println("Beds: " + beds);
        System.out.println("Room Size: " + size + " sq.ft");
        System.out.println("Price per Night: ₹" + price);
        System.out.println("Available Rooms: " + availability);
    }
}


/* ---------- Single Room ---------- */

class SingleRoom extends Room {

    public SingleRoom() {
        roomType = "Single Room";
        beds = 1;
        size = 200;
        price = 3000;
    }
}


/* ---------- Double Room ---------- */

class DoubleRoom extends Room {

    public DoubleRoom() {
        roomType = "Double Room";
        beds = 2;
        size = 350;
        price = 5000;
    }
}


/* ---------- Suite Room ---------- */

class SuiteRoom extends Room {

    public SuiteRoom() {
        roomType = "Suite Room";
        beds = 3;
        size = 600;
        price = 9000;
    }
}