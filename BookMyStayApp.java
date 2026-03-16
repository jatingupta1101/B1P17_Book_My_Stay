
import java.io.*;
import java.util.*;

/**
 * Use Case 12: Data Persistence & System Recovery
 */

public class UseCase12DataPersistenceRecovery {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("        BOOK MY STAY APP         ");
        System.out.println(" Data Persistence & Recovery     ");
        System.out.println("=================================");

        PersistenceService persistence = new PersistenceService();

        SystemState state = persistence.loadState();

        if (state == null) {

            System.out.println("No previous data found. Starting fresh system.");

            state = new SystemState();
        } else {

            System.out.println("System state recovered successfully.");
        }

        RoomInventory inventory = state.getInventory();
        BookingHistory history = state.getHistory();

        Reservation r1 = new Reservation("Rahul", "Single");
        Reservation r2 = new Reservation("Anita", "Double");

        if (inventory.allocateRoom(r1.getRoomType())) {
            history.addBooking(r1);
            System.out.println("Booking confirmed for " + r1.getGuestName());
        }

        if (inventory.allocateRoom(r2.getRoomType())) {
            history.addBooking(r2);
            System.out.println("Booking confirmed for " + r2.getGuestName());
        }

        persistence.saveState(state);

        System.out.println("System state saved to file.");
    }
}


/* ---------- Reservation ---------- */

class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;

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


/* ---------- Booking History ---------- */

class BookingHistory implements Serializable {

    private static final long serialVersionUID = 1L;

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


/* ---------- Room Inventory ---------- */

class RoomInventory implements Serializable {

    private static final long serialVersionUID = 1L;

    private HashMap<String, Integer> availability;

    public RoomInventory() {

        availability = new HashMap<>();

        availability.put("Single", 5);
        availability.put("Double", 3);
        availability.put("Suite", 2);
    }

    public boolean allocateRoom(String type) {

        int available = availability.getOrDefault(type, 0);

        if (available > 0) {

            availability.put(type, available - 1);
            return true;
        }

        return false;
    }

    public HashMap<String, Integer> getAvailability() {

        return availability;
    }
}


/* ---------- System State ---------- */

class SystemState implements Serializable {

    private static final long serialVersionUID = 1L;

    private RoomInventory inventory;
    private BookingHistory history;

    public SystemState() {

        inventory = new RoomInventory();
        history = new BookingHistory();
    }

    public RoomInventory getInventory() {

        return inventory;
    }

    public BookingHistory getHistory() {

        return history;
    }
}


/* ---------- Persistence Service ---------- */

class PersistenceService {

    private static final String FILE_NAME = "system_state.dat";

    public void saveState(SystemState state) {

        try {

            ObjectOutputStream out =
                    new ObjectOutputStream(
                            new FileOutputStream(FILE_NAME));

            out.writeObject(state);
            out.close();

        } catch (Exception e) {

            System.out.println("Error saving system state.");
        }
    }

    public SystemState loadState() {

        try {

            ObjectInputStream in =
                    new ObjectInputStream(
                            new FileInputStream(FILE_NAME));

            SystemState state = (SystemState) in.readObject();

            in.close();

            return state;

        } catch (Exception e) {

            return null;
        }
    }
}
