import java.util.HashMap;

/**
 * Book My Stay Application
 *
 * Demonstrates room search and availability check
 * using centralized inventory.
 *
 * Version 4.1 – Added Room Search functionality
 *
 * @author Jatin
 * @version 4.1
 */

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("        BOOK MY STAY APP         ");
        System.out.println("      Hotel Booking System       ");
        System.out.println("           Version 4.1           ");
        System.out.println("=================================");

        // Create room objects
        Room singleRoom = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suiteRoom = new SuiteRoom();

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Create search service
        RoomSearchService searchService = new RoomSearchService(inventory);

        // Guest searches for available rooms
        searchService.searchAvailableRooms(singleRoom, "Single");
        searchService.searchAvailableRooms(doubleRoom, "Double");
        searchService.searchAvailableRooms(suiteRoom, "Suite");
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


/* ---------- Inventory Management ---------- */

class RoomInventory {

    private HashMap<String, Integer> availability;

    public RoomInventory() {

        availability = new HashMap<>();

        availability.put("Single", 5);
        availability.put("Double", 3);
        availability.put("Suite", 2);
    }

    public int getAvailability(String roomType) {

        return availability.getOrDefault(roomType, 0);
    }
}


/* ---------- Room Search Service ---------- */

class RoomSearchService {

    private RoomInventory inventory;

    public RoomSearchService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void searchAvailableRooms(Room room, String roomType) {

        int available = inventory.getAvailability(roomType);

        // Show only rooms with availability
        if (available > 0) {
            room.displayRoomDetails(available);
        }
    }
}