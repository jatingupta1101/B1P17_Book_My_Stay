import java.util.*;

/**
 * Book My Stay Application
 *
 * Demonstrates reservation confirmation and
 * safe room allocation.
 *
 * Version 6.1 – Added Room Allocation Service
 *
 * @author Jatin
 * @version 6.1
 */

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("        BOOK MY STAY APP         ");
        System.out.println("      Hotel Booking System       ");
        System.out.println("           Version 6.1           ");
        System.out.println("=================================");

        RoomInventory inventory = new RoomInventory();

        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        bookingQueue.addRequest(new Reservation("Rahul", "Single"));
        bookingQueue.addRequest(new Reservation("Anita", "Double"));
        bookingQueue.addRequest(new Reservation("Vikram", "Suite"));

        RoomAllocationService allocationService =
                new RoomAllocationService(inventory, bookingQueue);

        allocationService.processBookings();
    }
}


/* ---------- Abstract Room ---------- */

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
        roomType = "Single";
        beds = 1;
        size = 200;
        price = 3000;
    }
}

class DoubleRoom extends Room {

    public DoubleRoom() {
        roomType = "Double";
        beds = 2;
        size = 350;
        price = 5000;
    }
}

class SuiteRoom extends Room {

    public SuiteRoom() {
        roomType = "Suite";
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

    public void decreaseAvailability(String roomType) {

        int current = availability.get(roomType);
        availability.put(roomType, current - 1);
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


/* ---------- Booking Queue ---------- */

class BookingRequestQueue {

    private Queue<Reservation> queue;

    public BookingRequestQueue() {

        queue = new LinkedList<>();
    }

    public void addRequest(Reservation reservation) {

        queue.add(reservation);

        System.out.println("Booking request received from "
                + reservation.getGuestName()
                + " for "
                + reservation.getRoomType());
    }

    public Reservation getNextRequest() {

        return queue.poll();
    }

    public boolean hasRequests() {

        return !queue.isEmpty();
    }
}


/* ---------- Room Allocation Service ---------- */

class RoomAllocationService {

    private RoomInventory inventory;
    private BookingRequestQueue bookingQueue;

    private Set<String> allocatedRoomIds;
    private HashMap<String, Set<String>> roomAllocations;

    private int roomCounter = 1;

    public RoomAllocationService(RoomInventory inventory,
                                 BookingRequestQueue bookingQueue) {

        this.inventory = inventory;
        this.bookingQueue = bookingQueue;

        allocatedRoomIds = new HashSet<>();
        roomAllocations = new HashMap<>();
    }

    public void processBookings() {

        while (bookingQueue.hasRequests()) {

            Reservation request = bookingQueue.getNextRequest();

            String roomType = request.getRoomType();

            if (inventory.getAvailability(roomType) > 0) {

                String roomId = generateRoomId(roomType);

                allocatedRoomIds.add(roomId);

                roomAllocations
                        .computeIfAbsent(roomType, k -> new HashSet<>())
                        .add(roomId);

                inventory.decreaseAvailability(roomType);

                System.out.println("Reservation confirmed for "
                        + request.getGuestName()
                        + " | Room ID: "
                        + roomId);

            } else {

                System.out.println("No rooms available for "
                        + request.getGuestName());
            }
        }
    }

    private String generateRoomId(String roomType) {

        String roomId;

        do {
            roomId = roomType + "-" + roomCounter++;
        } while (allocatedRoomIds.contains(roomId));

        return roomId;
    }
}