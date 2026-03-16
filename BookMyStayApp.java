
import java.util.*;

/**
 * Book My Stay Application
 *
 * Demonstrates booking request handling using FIFO queue
 * and safe room allocation.
 *
 * Version 6.1 – Room Allocation
 * Version 7.0 – Add-On Service Selection
 *
 * @author Jatin
 */

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("        BOOK MY STAY APP         ");
        System.out.println("      Hotel Booking System       ");
        System.out.println("           Version 7.0           ");
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

        Reservation r1 = new Reservation("Rahul", "Single");
        Reservation r2 = new Reservation("Anita", "Double");
        Reservation r3 = new Reservation("Vikram", "Suite");

        bookingQueue.addRequest(r1);
        bookingQueue.addRequest(r2);
        bookingQueue.addRequest(r3);

        // Room allocation
        RoomAllocationService allocationService =
                new RoomAllocationService(inventory, bookingQueue);

        allocationService.processBookings();

        // ---------- USE CASE 7 : ADD-ON SERVICES ----------

        AddOnServiceManager serviceManager = new AddOnServiceManager();

        AddOnService breakfast = new AddOnService("Breakfast", 500);
        AddOnService spa = new AddOnService("Spa Access", 1500);
        AddOnService airportPickup = new AddOnService("Airport Pickup", 800);

        serviceManager.addService(r1.getReservationId(), breakfast);
        serviceManager.addService(r1.getReservationId(), spa);

        serviceManager.addService(r2.getReservationId(), airportPickup);

        serviceManager.showServices(r1.getReservationId());
        serviceManager.showServices(r2.getReservationId());
    }
}


/* ---------- Abstract Room ---------- */

abstract class Room {

    protected String roomType;
    protected int beds;
    protected int size;
    protected int price;

    public void displayRoomDetails(int availability) {

        System.out.println("\nRoom Type: " + roomType);
        System.out.println("Beds: " + beds);
        System.out.println("Size: " + size + " sq ft");
        System.out.println("Price: ₹" + price);
        System.out.println("Available: " + availability);
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


/* ---------- Room Inventory ---------- */

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

    private static int counter = 1;

    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {

        this.guestName = guestName;
        this.roomType = roomType;
        this.reservationId = "RES-" + counter++;
    }

    public String getReservationId() {
        return reservationId;
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


/* ---------- Add On Service ---------- */

class AddOnService {

    private String serviceName;
    private int cost;

    public AddOnService(String serviceName, int cost) {

        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public int getCost() {
        return cost;
    }
}


/* ---------- Add On Service Manager ---------- */

class AddOnServiceManager {

    private HashMap<String, List<AddOnService>> reservationServices;

    public AddOnServiceManager() {

        reservationServices = new HashMap<>();
    }

    public void addService(String reservationId, AddOnService service) {

        reservationServices
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);

        System.out.println(service.getServiceName()
                + " added to reservation "
                + reservationId);
    }

    public void showServices(String reservationId) {

        List<AddOnService> services = reservationServices.get(reservationId);

        if (services == null) {
            System.out.println("No services for " + reservationId);
            return;
        }

        int total = 0;

        System.out.println("\nServices for " + reservationId);

        for (AddOnService s : services) {

            System.out.println("- " + s.getServiceName()
                    + " : ₹" + s.getCost());

            total += s.getCost();
        }

        System.out.println("Total Add-on Cost: ₹" + total);
    }
}
