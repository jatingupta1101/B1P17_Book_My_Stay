
import java.util.*;

/**
 * Use Case 11: Concurrent Booking Simulation
 * Demonstrates thread-safe booking using synchronized methods
 */

public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("        BOOK MY STAY APP         ");
        System.out.println(" Concurrent Booking Simulation   ");
        System.out.println("=================================");

        RoomInventory inventory = new RoomInventory();
        BookingRequestQueue queue = new BookingRequestQueue();

        // Multiple guests submit requests
        queue.addRequest(new Reservation("Rahul", "Single"));
        queue.addRequest(new Reservation("Anita", "Single"));
        queue.addRequest(new Reservation("Vikram", "Single"));
        queue.addRequest(new Reservation("Riya", "Double"));
        queue.addRequest(new Reservation("Arjun", "Suite"));

        // Two worker threads processing bookings
        Thread worker1 = new Thread(new BookingProcessor(queue, inventory));
        Thread worker2 = new Thread(new BookingProcessor(queue, inventory));

        worker1.start();
        worker2.start();
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

    public synchronized void addRequest(Reservation r) {

        queue.add(r);

        System.out.println("Booking request received from "
                + r.getGuestName()
                + " for "
                + r.getRoomType());
    }

    public synchronized Reservation getNextRequest() {

        return queue.poll();
    }
}


/* ---------- Inventory ---------- */

class RoomInventory {

    private HashMap<String, Integer> availability;

    public RoomInventory() {

        availability = new HashMap<>();

        availability.put("Single", 2);
        availability.put("Double", 1);
        availability.put("Suite", 1);
    }

    public synchronized boolean allocateRoom(String roomType) {

        int available = availability.getOrDefault(roomType, 0);

        if (available > 0) {

            availability.put(roomType, available - 1);

            return true;
        }

        return false;
    }
}


/* ---------- Booking Processor (Thread) ---------- */

class BookingProcessor implements Runnable {

    private BookingRequestQueue queue;
    private RoomInventory inventory;

    public BookingProcessor(BookingRequestQueue queue,
                            RoomInventory inventory) {

        this.queue = queue;
        this.inventory = inventory;
    }

    public void run() {

        while (true) {

            Reservation request = queue.getNextRequest();

            if (request == null)
                break;

            processBooking(request);
        }
    }

    private void processBooking(Reservation r) {

        synchronized (inventory) {

            boolean success = inventory.allocateRoom(r.getRoomType());

            if (success) {

                System.out.println(Thread.currentThread().getName()
                        + " confirmed booking for "
                        + r.getGuestName()
                        + " (" + r.getRoomType() + ")");

            } else {

                System.out.println(Thread.currentThread().getName()
                        + " failed booking for "
                        + r.getGuestName()
                        + " (No rooms available)");
            }
        }
    }
}
