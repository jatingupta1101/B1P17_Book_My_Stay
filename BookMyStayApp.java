import java.util.HashMap;
import java.util.Queue;
import java.util.LinkedList;

/**
 * Book My Stay Application
 *
 * Demonstrates booking request handling using
 * a FIFO queue (First-Come-First-Served).
 *
 * Version 5.1 – Added Booking Request Queue
 *
 * @author Jatin
 * @version 5.1
 */

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("        BOOK MY STAY APP         ");
        System.out.println("      Hotel Booking System       ");
        System.out.println("           Version 5.1           ");
        System.out.println("=================================");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Create room objects
        Room singleRoom = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suiteRoom = new SuiteRoom();

        // Search service
        RoomSearchService searchService = new RoomSearchService(inventory);

        searchService.searchAvailableRooms(singleRoom, "Single");
        searchService.searchAvailableRooms(doubleRoom, "Double");
        searchService.searchAvailableRooms(suiteRoom, "Suite");

        // Booking request queue
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Guests submit booking requests
        bookingQueue.addRequest(new Reservation("Rahul", "Single"));
        bookingQueue.addRequest(new Reservation("Anita", "Double"));
        bookingQueue.addRequest(new Reservation("Vikram", "Suite"));

        // Display queued requests
        bookingQueue.showRequests();
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


/* ---------- Room Types ---------- */

class SingleRoom extends Room {

    public SingleRoom() {
        roomType = "Single Room";
        beds = 1;
        size = 200;
        price = 3000;
    }
}

class DoubleRoom extends Room {

    public DoubleRoom() {
        roomType = "Double Room";
        beds = 2;
        size = 350;
        price = 5000;
    }
}

class SuiteRoom extends Room {

    public SuiteRoom() {
        roomType = "Suite Room";
        beds = 3;
        size = 600;
        price = 9000;
    }
}


/* ---------- Inventory ---------- */

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


/* ---------- Search Service ---------- */

class RoomSearchService {

    private RoomInventory inventory;

    public RoomSearchService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void searchAvailableRooms(Room room, String roomType) {

        int available = inventory.getAvailability(roomType);

        if (available > 0) {
            room.displayRoomDetails(available);
        }
    }
}


/* ---------- Reservation ---------- */

class Reservation {

    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}


/* ---------- Booking Request Queue ---------- */

class BookingRequestQueue {

    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    public void addRequest(Reservation reservation) {

        queue.add(reservation);
        System.out.println("\nBooking request added for: "
                + reservation.getGuestName()
                + " (" + reservation.getRoomType() + ")");
    }

    public void showRequests() {

        System.out.println("\nCurrent Booking Queue:");

        for (Reservation r : queue) {
            System.out.println(r.getGuestName()
                    + " requested "
                    + r.getRoomType() + " room");
        }
    }
}