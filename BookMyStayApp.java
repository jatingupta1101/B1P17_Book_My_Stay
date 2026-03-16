
import java.util.*;

/**
 * Use Case 8: Booking History & Reporting
 * Demonstrates storing confirmed bookings and generating reports
 */

public class UseCase8BookingHistoryReport {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("        BOOK MY STAY APP         ");
        System.out.println("    Booking History & Reporting  ");
        System.out.println("=================================");

        RoomInventory inventory = new RoomInventory();

        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        RoomSearchService search = new RoomSearchService(inventory);

        search.searchAvailableRooms(single, "Single");
        search.searchAvailableRooms(doubleRoom, "Double");
        search.searchAvailableRooms(suite, "Suite");

        BookingRequestQueue queue = new BookingRequestQueue();

        Reservation r1 = new Reservation("Rahul", "Single");
        Reservation r2 = new Reservation("Anita", "Double");
        Reservation r3 = new Reservation("Vikram", "Suite");

        queue.addRequest(r1);
        queue.addRequest(r2);
        queue.addRequest(r3);

        BookingHistory history = new BookingHistory();

        RoomAllocationService allocation =
                new RoomAllocationService(inventory, queue, history);

        allocation.processBookings();

        BookingReportService report = new BookingReportService(history);

        report.showBookingHistory();
        report.generateSummaryReport();
    }
}


/* ---------- Room ---------- */

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

    public int getAvailability(String type) {

        return availability.getOrDefault(type, 0);
    }

    public void decreaseAvailability(String type) {

        availability.put(type, availability.get(type) - 1);
    }
}


/* ---------- Search ---------- */

class RoomSearchService {

    private RoomInventory inventory;

    public RoomSearchService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void searchAvailableRooms(Room room, String type) {

        int available = inventory.getAvailability(type);

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

    public Reservation(String guest, String type) {

        guestName = guest;
        roomType = type;
        reservationId = "RES-" + counter++;
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


/* ---------- Booking Queue ---------- */

class BookingRequestQueue {

    private Queue<Reservation> queue;

    public BookingRequestQueue() {

        queue = new LinkedList<>();
    }

    public void addRequest(Reservation r) {

        queue.add(r);

        System.out.println("Booking request received from "
                + r.getGuestName()
                + " for "
                + r.getRoomType());
    }

    public Reservation getNextRequest() {

        return queue.poll();
    }

    public boolean hasRequests() {

        return !queue.isEmpty();
    }
}


/* ---------- Booking History ---------- */

class BookingHistory {

    private List<Reservation> history;

    public BookingHistory() {

        history = new ArrayList<>();
    }

    public void addBooking(Reservation r) {

        history.add(r);
    }

    public List<Reservation> getHistory() {

        return history;
    }
}


/* ---------- Room Allocation ---------- */

class RoomAllocationService {

    private RoomInventory inventory;
    private BookingRequestQueue queue;
    private BookingHistory history;

    private int roomCounter = 1;

    public RoomAllocationService(RoomInventory inv,
                                 BookingRequestQueue q,
                                 BookingHistory h) {

        inventory = inv;
        queue = q;
        history = h;
    }

    public void processBookings() {

        while (queue.hasRequests()) {

            Reservation r = queue.getNextRequest();

            String type = r.getRoomType();

            if (inventory.getAvailability(type) > 0) {

                String roomId = type + "-" + roomCounter++;

                inventory.decreaseAvailability(type);

                history.addBooking(r);

                System.out.println("Reservation confirmed for "
                        + r.getGuestName()
                        + " | Room ID: "
                        + roomId);

            } else {

                System.out.println("No rooms available for "
                        + r.getGuestName());
            }
        }
    }
}


/* ---------- Reporting Service ---------- */

class BookingReportService {

    private BookingHistory history;

    public BookingReportService(BookingHistory history) {

        this.history = history;
    }

    public void showBookingHistory() {

        System.out.println("\n===== Booking History =====");

        for (Reservation r : history.getHistory()) {

            System.out.println(
                    r.getReservationId() + " | "
                            + r.getGuestName() + " | "
                            + r.getRoomType()
            );
        }
    }

    public void generateSummaryReport() {

        System.out.println("\n===== Booking Summary Report =====");

        HashMap<String, Integer> summary = new HashMap<>();

        for (Reservation r : history.getHistory()) {

            String type = r.getRoomType();

            summary.put(type,
                    summary.getOrDefault(type, 0) + 1);
        }

        for (String type : summary.keySet()) {

            System.out.println(type + " Rooms Booked: "
                    + summary.get(type));
        }
    }
}

