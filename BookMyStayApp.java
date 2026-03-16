
import java.util.*;

/**
 * Use Case 9: Error Handling & Validation
 * Demonstrates validation and custom exception handling
 */

public class UseCase9ErrorHandlingValidation {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("        BOOK MY STAY APP         ");
        System.out.println("    Error Handling & Validation  ");
        System.out.println("=================================");

        RoomInventory inventory = new RoomInventory();

        BookingRequestQueue queue = new BookingRequestQueue();

        // Valid booking
        queue.addRequest(new Reservation("Rahul", "Single"));

        // Invalid room type
        queue.addRequest(new Reservation("Anita", "Triple"));

        // Valid booking
        queue.addRequest(new Reservation("Vikram", "Suite"));

        BookingHistory history = new BookingHistory();

        RoomAllocationService allocation =
                new RoomAllocationService(inventory, queue, history);

        allocation.processBookings();
    }
}


/* ---------- Custom Exception ---------- */

class InvalidBookingException extends Exception {

    public InvalidBookingException(String message) {
        super(message);
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

        return availability.getOrDefault(type, -1);
    }

    public void decreaseAvailability(String type)
            throws InvalidBookingException {

        int current = availability.get(type);

        if (current <= 0) {
            throw new InvalidBookingException(
                    "No rooms available for type: " + type);
        }

        availability.put(type, current - 1);
    }

    public boolean isValidRoomType(String type) {

        return availability.containsKey(type);
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


/* ---------- Allocation Service ---------- */

class RoomAllocationService {

    private RoomInventory inventory;
    private BookingRequestQueue queue;
    private BookingHistory history;

    private int roomCounter = 1;

    public RoomAllocationService(RoomInventory inventory,
                                 BookingRequestQueue queue,
                                 BookingHistory history) {

        this.inventory = inventory;
        this.queue = queue;
        this.history = history;
    }

    public void processBookings() {

        while (queue.hasRequests()) {

            Reservation r = queue.getNextRequest();

            try {

                validateReservation(r);

                String type = r.getRoomType();

                inventory.decreaseAvailability(type);

                String roomId = type + "-" + roomCounter++;

                history.addBooking(r);

                System.out.println("Reservation confirmed for "
                        + r.getGuestName()
                        + " | Room ID: "
                        + roomId);

            } catch (InvalidBookingException e) {

                System.out.println("Booking failed for "
                        + r.getGuestName()
                        + " : "
                        + e.getMessage());
            }
        }
    }

    private void validateReservation(Reservation r)
            throws InvalidBookingException {

        if (!inventory.isValidRoomType(r.getRoomType())) {

            throw new InvalidBookingException(
                    "Invalid room type: " + r.getRoomType());
        }
    }
}

