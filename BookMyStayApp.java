
import java.util.*;

/**
 * Use Case 10: Booking Cancellation & Inventory Rollback
 */

public class UseCase10BookingCancellation {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("        BOOK MY STAY APP         ");
        System.out.println(" Booking Cancellation & Rollback ");
        System.out.println("=================================");

        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();

        RoomAllocationService allocation =
                new RoomAllocationService(inventory, history);

        Reservation r1 = new Reservation("Rahul", "Single");
        Reservation r2 = new Reservation("Anita", "Double");

        allocation.confirmBooking(r1);
        allocation.confirmBooking(r2);

        CancellationService cancelService =
                new CancellationService(inventory, history);

        cancelService.cancelReservation(r1.getReservationId());
        cancelService.cancelReservation("RES-999"); // invalid case
    }
}


/* ---------- Reservation ---------- */

class Reservation {

    private static int counter = 1;

    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;

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

    public void setRoomId(String id) {
        roomId = id;
    }

    public String getRoomId() {
        return roomId;
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

        int current = availability.get(type);
        availability.put(type, current - 1);
    }

    public void increaseAvailability(String type) {

        int current = availability.get(type);
        availability.put(type, current + 1);
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

    public Reservation findReservation(String id) {

        for (Reservation r : history) {
            if (r.getReservationId().equals(id)) {
                return r;
            }
        }

        return null;
    }

    public void removeReservation(Reservation r) {
        history.remove(r);
    }
}


/* ---------- Allocation Service ---------- */

class RoomAllocationService {

    private RoomInventory inventory;
    private BookingHistory history;

    private int roomCounter = 1;

    public RoomAllocationService(RoomInventory inventory,
                                 BookingHistory history) {

        this.inventory = inventory;
        this.history = history;
    }

    public void confirmBooking(Reservation r) {

        String type = r.getRoomType();

        if (inventory.getAvailability(type) > 0) {

            inventory.decreaseAvailability(type);

            String roomId = type + "-" + roomCounter++;
            r.setRoomId(roomId);

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


/* ---------- Cancellation Service ---------- */

class CancellationService {

    private RoomInventory inventory;
    private BookingHistory history;

    private Stack<String> rollbackStack;

    public CancellationService(RoomInventory inventory,
                               BookingHistory history) {

        this.inventory = inventory;
        this.history = history;

        rollbackStack = new Stack<>();
    }

    public void cancelReservation(String reservationId) {

        Reservation r = history.findReservation(reservationId);

        if (r == null) {

            System.out.println("Cancellation failed. Reservation not found: "
                    + reservationId);

            return;
        }

        rollbackStack.push(r.getRoomId());

        inventory.increaseAvailability(r.getRoomType());

        history.removeReservation(r);

        System.out.println("Reservation cancelled for "
                + r.getGuestName()
                + " | Room Released: "
                + r.getRoomId());
    }
}

