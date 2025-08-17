import java.io.*;
import java.util.*;


class Room {
    int roomNumber;
    String category;
    boolean isBooked;

    public Room(int roomNumber, String category) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.isBooked = false;
    }

    @Override
    public String toString() {
        return "Room " + roomNumber + " (" + category + ") - " + (isBooked ? "Booked" : "Available");
    }
}


class Booking implements Serializable {
    String customerName;
    int roomNumber;
    String category;
    double amountPaid;

    public Booking(String customerName, int roomNumber, String category, double amountPaid) {
        this.customerName = customerName;
        this.roomNumber = roomNumber;
        this.category = category;
        this.amountPaid = amountPaid;
    }

    @Override
    public String toString() {
        return "Booking: " + customerName + " | Room " + roomNumber +
               " (" + category + ") | Paid: $" + amountPaid;
    }
}


public class HotelReservationSystem {
    private static ArrayList<Room> rooms = new ArrayList<>();
    private static ArrayList<Booking> bookings = new ArrayList<>();
    private static final String FILE_NAME = "bookings.dat";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        initializeRooms();
        loadBookings();

        int choice;
        do {
            System.out.println("\n=== Hotel Reservation System ===");
            System.out.println("1. View Available Rooms");
            System.out.println("2. Book a Room");
            System.out.println("3. View My Bookings");
            System.out.println("4. Cancel a Booking");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> viewAvailableRooms();
                case 2 -> bookRoom(sc);
                case 3 -> viewBookings();
                case 4 -> cancelBooking(sc);
                case 5 -> saveBookings();
                default -> System.out.println("Invalid choice!");
            }
        } while (choice != 5);

        sc.close();
    }


    private static void initializeRooms() {
        rooms.add(new Room(101, "Standard"));
        rooms.add(new Room(102, "Standard"));
        rooms.add(new Room(201, "Deluxe"));
        rooms.add(new Room(202, "Deluxe"));
        rooms.add(new Room(301, "Suite"));
    }


    private static void viewAvailableRooms() {
        System.out.println("\n--- Available Rooms ---");
        for (Room room : rooms) {
            if (!room.isBooked) {
                System.out.println(room);
            }
        }
    }


    private static void bookRoom(Scanner sc) {
        System.out.print("Enter your name: ");
        String name = sc.nextLine();

        System.out.print("Enter room number to book: ");
        int roomNumber = sc.nextInt();
        sc.nextLine();

        Room selectedRoom = null;
        for (Room room : rooms) {
            if (room.roomNumber == roomNumber && !room.isBooked) {
                selectedRoom = room;
                break;
            }
        }

        if (selectedRoom == null) {
            System.out.println("Room not available!");
            return;
        }


        double amount = switch (selectedRoom.category) {
            case "Standard" -> 100.0;
            case "Deluxe" -> 200.0;
            case "Suite" -> 300.0;
            default -> 0.0;
        };

        System.out.println("Room " + roomNumber + " (" + selectedRoom.category + ") costs $" + amount);
        System.out.print("Proceed with payment? (yes/no): ");
        String confirm = sc.nextLine();

        if (confirm.equalsIgnoreCase("yes")) {
            selectedRoom.isBooked = true;
            Booking booking = new Booking(name, roomNumber, selectedRoom.category, amount);
            bookings.add(booking);
            System.out.println("Booking confirmed!");
        } else {
            System.out.println("Booking cancelled.");
        }
    }


    private static void viewBookings() {
        System.out.println("\n--- Booking Details ---");
        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
        } else {
            for (Booking b : bookings) {
                System.out.println(b);
            }
        }
    }


    private static void cancelBooking(Scanner sc) {
        System.out.print("Enter room number to cancel booking: ");
        int roomNumber = sc.nextInt();
        sc.nextLine();

        Booking bookingToRemove = null;
        for (Booking b : bookings) {
            if (b.roomNumber == roomNumber) {
                bookingToRemove = b;
                break;
            }
        }

        if (bookingToRemove != null) {
            bookings.remove(bookingToRemove);


            for (Room r : rooms) {
                if (r.roomNumber == roomNumber) {
                    r.isBooked = false;
                }
            }

            System.out.println("Booking cancelled successfully.");
        } else {
            System.out.println("No booking found for that room.");
        }
    }


    private static void saveBookings() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(bookings);
            System.out.println("Bookings saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving bookings: " + e.getMessage());
        }
    }


    private static void loadBookings() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            bookings = (ArrayList<Booking>) ois.readObject();


            for (Booking b : bookings) {
                for (Room r : rooms) {
                    if (r.roomNumber == b.roomNumber) {
                        r.isBooked = true;
                    }
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            bookings = new ArrayList<>();
        }
    }
}
