import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class Room {
    public enum RoomClass {
        SUPERIOR,
        DELUXE,
        STANDARD
    }

    public enum BedType {
        TWIN,
        DOUBLE,
        QUEEN_SIZE,
        KING_SIZE
    }
    
    private int roomNumber;
    private RoomClass roomClass; 
    private BedType bedType; 
    private int rate;
    private boolean isAvailable;
    
    public Room(int roomNumber, RoomClass roomClass, int rate) {
        this.roomNumber = roomNumber;
        this.roomClass = roomClass;
        this.bedType = null;
        this.rate = rate;
        this.isAvailable = true;
    }
    
    public int getRoomNumber() {
        return roomNumber;
    }

    public RoomClass getRoomClass() {
        return roomClass;
    }

    public BedType getBedType() {
        return bedType;
    }

    public int getRate() {
        return rate;
    }

    public boolean isAvailable() {
        return isAvailable;
    }
    
    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public void setBedType(BedType bedType) {
        this.bedType = bedType;
    }

}

class Guest {
    private String firstName;
    private String lastName;
    private int lengthOfStay;
    private Room reservedRoom;
    
    public Guest(String firstName, String lastName, int lengthOfStay, Room reservedRoom) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.lengthOfStay = lengthOfStay;
        this.reservedRoom = reservedRoom;

    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getLengthOfStay() {
        return lengthOfStay;
    }

    public Room getReservedRoom() {
        return reservedRoom;
    }

}

class Reservation {
    private Guest guest;
    private Room room;

    public Reservation(Guest guest, Room room) {
        this.guest = guest;
        this.room = room;
        room.setAvailable(false); // Mark the room as not available
    }

    public Guest getGuest() {
        return guest;
    }

    public Room getRoom() {
        return room;
    }
}

public class HotelManagementSystem {
    private ArrayList<Room> rooms = new ArrayList<>();
    private ArrayList<Guest> guests = new ArrayList<>();
    private ArrayList<Reservation> reservations = new ArrayList<>();

    // Method to initiliase the hotel room allocations
    public void initializeRooms() {
        // Standard Rooms
        for (int i = 1; i <= 250; i++) {
            rooms.add(new Room(i, Room.RoomClass.STANDARD, 1000));
        }
        // Deluxe Rooms
        for (int i = 251; i <= 500; i++) {
            rooms.add(new Room(i, Room.RoomClass.DELUXE, 1200));
        }
        // Superior Rooms
        for (int i = 501; i <= 530; i++) {
            rooms.add(new Room(i, Room.RoomClass.SUPERIOR, 1800));
        }
    }

    // Menu case 1: Method to display all available rooms
    public void displayAvailableRooms() {
        for (Room.RoomClass roomClass : Room.RoomClass.values()) {
            System.out.println("Available " + roomClass + " rooms:");
            rooms.stream()
                .filter(r -> r.getRoomClass() == roomClass && r.isAvailable())
                .forEach(r -> System.out.println("Room Number: " + r.getRoomNumber()));
        }
    }

    // Menu case 2: method to display room details for a class
    public void displayRoomDetails(Room.RoomClass roomClass) {
        int reservedCount = 0;
        int availableCount = 0;
        int totalIncome = 0;
        
        for (Room room : rooms) {
            if (room.getRoomClass() == roomClass) {
                if (room.isAvailable()) {
                    availableCount++;
                } else {
                    reservedCount++;
                    totalIncome += room.getRate();
                }
            }
        }
        
        System.out.println("\nDetailed Information for " + roomClass + " Rooms:");
        System.out.printf("%-15s %-25s %-30s %-15s%n", "Room Class", "Rooms Reserved", "Rooms Available", "Total Income");
        System.out.printf("%-15s %-25d %-30d %-15d%n", roomClass, reservedCount, availableCount, totalIncome);
    }

    // Menu case 3: Method to display reservations (by order of entry)
    public void displayAllReservations() {
        System.out.println("\nCurrent Reservations:");
        System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15s%n", "First Name", "Last Name", "Length of Stay", "Room Reserved", "Room Class", "Bed Type");
        for (Reservation reservation : reservations) {
            Guest guest = reservation.getGuest();
            Room room = reservation.getRoom();
            System.out.printf("%-15s %-15s %-15d %-15d %-15s %-15s%n", 
                guest.getFirstName(), 
                guest.getLastName(), 
                guest.getLengthOfStay(), 
                room.getRoomNumber(), 
                room.getRoomClass(), 
                room.getBedType());
        }
    }

    // Menu case 4: Method to display reservations (by last name A-to-Z)
    public void alphabetisedDisplayAllReservations() {
        System.out.println("\nAlphabetised Current Reservations:");
        System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15s%n", "First Name", "Last Name", "Length of Stay", "Room Reserved", "Room Class", "Bed Type");
        
        // Create a copy of the reservations list to sort, to avoid modifying the original order
        ArrayList<Reservation> sortedReservations = new ArrayList<>(reservations);
        
        // Sort the copied list based on guests' last names
        Collections.sort(sortedReservations, new Comparator<Reservation>() {
            @Override
            public int compare(Reservation r1, Reservation r2) {
                return r1.getGuest().getLastName().compareToIgnoreCase(r2.getGuest().getLastName());
            }
        });
        
        // Iterate over the sorted list and print each reservation
        for (Reservation reservation : sortedReservations) {
            Guest guest = reservation.getGuest();
            Room room = reservation.getRoom();
            System.out.printf("%-15s %-15s %-15d %-15d %-15s %-15s%n", 
                guest.getFirstName(), 
                guest.getLastName(), 
                guest.getLengthOfStay(), 
                room.getRoomNumber(), 
                room.getRoomClass(), 
                room.getBedType());
        }
    }


    //Menu case 5: Method to calculate total income
    public void calculateTotalIncome() {
        // Map to keep track of rooms reserved and total income per room class
        Map<Room.RoomClass, Integer[]> incomeData = new HashMap<>();
    
        // Initialize the map with all room classes
        for (Room.RoomClass roomClass : Room.RoomClass.values()) {
            incomeData.put(roomClass, new Integer[]{0, 0}); // {Rooms Reserved, Total Income}
        }
    
        // Iterate through all reservations to populate the map
        for (Reservation reservation : reservations) {
            Room room = reservation.getRoom();
            Integer[] data = incomeData.get(room.getRoomClass());
            data[0] += 1; // Increment the count of rooms reserved
            data[1] += room.getRate(); // Add to the total income
        }
    
        // Display the results in a table format
        System.out.println("\nTotal Income by Room Class:");
        System.out.printf("%-15s %-25s %-25s%n", "Room Class", "Rooms Reserved", "Total Income Generated");
        for (Map.Entry<Room.RoomClass, Integer[]> entry : incomeData.entrySet()) {
            System.out.printf("%-15s %-25d %-25d%n", entry.getKey(), entry.getValue()[0], entry.getValue()[1]);
        }
    }

    // Menu case 6: Method to search for guests by last name
    public void searchGuestByLastName(String lastName) {
        System.out.println("\nSearch Results for Last Name: " + lastName);
        System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15s%n", "First Name", "Last Name", "Length of Stay", "Room Reserved", "Room Class", "Bed Type");
        boolean found = false;
        for (Reservation reservation : reservations) {
            Guest guest = reservation.getGuest();
            if (guest.getLastName().equalsIgnoreCase(lastName)) {
                found = true;
                Room room = reservation.getRoom();
                System.out.printf("%-15s %-15s %-15d %-15d %-15s %-15s%n", 
                    guest.getFirstName(), 
                    guest.getLastName(), 
                    guest.getLengthOfStay(), 
                    room.getRoomNumber(), 
                    room.getRoomClass().toString(), 
                    room.getBedType().toString());
            }
        }
        if (!found) {
            System.out.println("No bookings found under the last name: " + lastName);
        }
    }

    // Menu case 7: Method to reserve a room
    public void reserveRoom(String firstName, String lastName, int lengthOfStay, Room.RoomClass roomClass, Room.BedType bedType) {
        for (Room room : rooms) {
            if (room.getRoomClass() == roomClass && room.isAvailable()) {
                room.setBedType(bedType);
                Guest guest = new Guest(firstName, lastName, lengthOfStay, room);
                Reservation reservation = new Reservation(guest, room);
                reservations.add(reservation);
                guests.add(guest); 
                System.out.println("Room " + room.getRoomNumber() + " reserved for " + firstName + " " + lastName);
                return;
            }
        }
        System.out.println("No available rooms of the requested type.");
    }

    // Menu case 8: method for checking if sufficient rooms for a group
    public boolean checkGroupRoomAvailability(int numberOfRooms, Room.RoomClass roomClass) {
        long availableRooms = rooms.stream()
                                    .filter(room -> room.getRoomClass() == roomClass && room.isAvailable())
                                    .count();
        return availableRooms >= numberOfRooms;
    }

    // Menu case 9: Method to delete a reservation based on room number
    public void deleteReservation(int roomNumber) {
        Reservation toRemove = null;
        for (Reservation reservation : reservations) {
            if (reservation.getRoom().getRoomNumber() == roomNumber) {
                toRemove = reservation;
                break;
            }
        }
        if (toRemove != null) {
            reservations.remove(toRemove);
            toRemove.getRoom().setAvailable(true); // Mark the room as available again
            System.out.println("Reservation for room number " + roomNumber + " has been deleted.");
        } else {
            System.out.println("No reservation found for room number " + roomNumber);
        }
    }

    // error handling for if non-integer entered into scanner
    private static int safeReadInt(Scanner scanner) {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.print("Invalid input. Please enter a valid integer: ");
                scanner.next(); // Consume the incorrect input
            }
        }
    }

    // Main method to run the application
    public static void main(String[] args) {
        HotelManagementSystem system = new HotelManagementSystem();
        system.initializeRooms();
        Scanner scanner = new Scanner(System.in);

        // Declare reused variables before the switch statement
        Room.RoomClass roomClass = null;
        String firstName = null;
        String lastName = null;
        int lengthOfStay = 0;
        List<Room.BedType> bedTypes = null;
        Room.BedType bedType = null;
    
        // Map to associate each RoomClass with its allowed BedTypes
        Map<Room.RoomClass, List<Room.BedType>> allowedBedTypes = new HashMap<>();
        allowedBedTypes.put(Room.RoomClass.STANDARD, List.of(Room.BedType.TWIN, Room.BedType.DOUBLE));
        allowedBedTypes.put(Room.RoomClass.DELUXE, List.of(Room.BedType.QUEEN_SIZE));
        allowedBedTypes.put(Room.RoomClass.SUPERIOR, List.of(Room.BedType.QUEEN_SIZE, Room.BedType.KING_SIZE));
    
        while (true) {
            System.out.println("\nHotel Management System Menu:");
            System.out.println("1. Display all available rooms as a list");
            System.out.println("2. Display detailed room information by class");
            System.out.println("3. Display all reservations");
            System.out.println("4. Display all reservations alphabetically by last name");
            System.out.println("5. Display total income from bookings");            
            System.out.println("6. Search for guest by last name");
            System.out.println("7. Create a new single reservation");
            System.out.println("8. Create a new group reservation");
            System.out.println("9. Delete a reservation");
            System.out.println("10. Exit");
    
            System.out.print("Enter your choice: ");
            int choice = safeReadInt(scanner); // Use the safeReadInt method
            scanner.nextLine(); // Consume newline
    
            switch (choice) {

                case 1:
                    system.displayAvailableRooms();
                    break;

                case 2:
                    System.out.println("Select Room Class for detailed information:");
                    System.out.println("1. STANDARD");
                    System.out.println("2. DELUXE");
                    System.out.println("3. SUPERIOR");
                    System.out.print("Enter choice: ");
                    int detailedRoomChoice = safeReadInt(scanner);

                    Room.RoomClass selectedClass = null;
                    switch (detailedRoomChoice) {
                        case 1:
                            selectedClass = Room.RoomClass.STANDARD;
                            break;
                        case 2:
                            selectedClass = Room.RoomClass.DELUXE;
                            break;
                        case 3:
                            selectedClass = Room.RoomClass.SUPERIOR;
                            break;
                        default:
                            System.out.println("Invalid choice, please select a valid Room Class.");
                            continue;
                    }

                    system.displayRoomDetails(selectedClass);
                    break;

                case 3:
                    system.displayAllReservations();
                    break;

                case 4:
                    system.alphabetisedDisplayAllReservations();
                    break;

                case 5:
                    system.calculateTotalIncome();
                    break;

                case 6:
                    System.out.print("Enter last name to search: ");
                    String searchLastName = scanner.nextLine();
                    system.searchGuestByLastName(searchLastName);
                    break;

                case 7:                 
                    System.out.print("Enter first name: ");
                    firstName = scanner.nextLine();
    
                    System.out.print("Enter last name: ");
                    lastName = scanner.nextLine();
    
                    do {
                        System.out.print("Enter length of stay (minimum 1 day): ");
                        lengthOfStay = safeReadInt(scanner);
                        scanner.nextLine(); // Consume the leftover newline
                        if (lengthOfStay < 1) {
                            System.out.println("Invalid length of stay. The minimum length of stay is 1 day.");
                        }
                    } while (lengthOfStay < 1);
    
                    roomClass = null;
                    while (roomClass == null) {
                        System.out.println("Choose room class:");
                        System.out.println("1. STANDARD");
                        System.out.println("2. DELUXE");
                        System.out.println("3. SUPERIOR");
                        System.out.print("Enter choice: ");
                        int roomClassChoice = safeReadInt(scanner);
                        switch (roomClassChoice) {
                            case 1:
                                roomClass = Room.RoomClass.STANDARD;
                                break;
                            case 2:
                                roomClass = Room.RoomClass.DELUXE;
                                break;
                            case 3:
                                roomClass = Room.RoomClass.SUPERIOR;
                                break;
                            default:
                                System.out.println("Invalid choice, please enter a valid option.");
                                continue;
                        }
                    }
    
                    // Get the allowed bed types for the selected room class
                    bedTypes = allowedBedTypes.get(roomClass);
                    bedType = null;
                    while (bedType == null) {
                        System.out.println("Choose bed type:");
                        for (int i = 0; i < bedTypes.size(); i++) {
                            System.out.println((i + 1) + ". " + bedTypes.get(i));
                        }
                        System.out.print("Enter choice: ");
                        int bedTypeChoice = safeReadInt(scanner);
                        if (bedTypeChoice < 1 || bedTypeChoice > bedTypes.size()) {
                            System.out.println("Invalid choice, please enter a valid option.");
                        } else {
                            bedType = bedTypes.get(bedTypeChoice - 1);
                        }
                    }
    
                    system.reserveRoom(firstName, lastName, lengthOfStay, roomClass, bedType);
                    break;

                case 8:
                    int numberOfRooms = 0;
                    do {
                        System.out.print("How many rooms would you like to book? ");
                        numberOfRooms = safeReadInt(scanner);
                        scanner.nextLine(); // Consume the leftover newline
                
                        if (numberOfRooms > 3) {
                            System.out.println("You cannot book more than 3 rooms in a group booking. Please try again.");
                        } else if (numberOfRooms <= 0) {
                            System.out.println("Please enter a valid number of rooms to book.");
                        }
                    } while (numberOfRooms > 3 || numberOfRooms <= 0);
                
                    // Select room class for all rooms in the booking
                    roomClass = null;
                    while (roomClass == null) {
                        System.out.println("Choose room class for all rooms:");
                        System.out.println("1. STANDARD");
                        System.out.println("2. DELUXE");
                        System.out.println("3. SUPERIOR");
                        System.out.print("Enter choice: ");
                        int roomClassChoice = safeReadInt(scanner);
                        scanner.nextLine(); // Consume the leftover newline
                        switch (roomClassChoice) {
                            case 1:
                                roomClass = Room.RoomClass.STANDARD;
                                break;
                            case 2:
                                roomClass = Room.RoomClass.DELUXE;
                                break;
                            case 3:
                                roomClass = Room.RoomClass.SUPERIOR;
                                break;
                            default:
                                System.out.println("Invalid choice, please enter a valid option.");
                                continue;
                        }
                    }

                    if (!system.checkGroupRoomAvailability(numberOfRooms, roomClass)) {
                        System.out.println("Unfortunately, we do not have enough available rooms of the requested type for your group.");
                        break; // Exit the current iteration or switch case
                    }
                
                    for (int roomNumber = 1; roomNumber <= numberOfRooms; roomNumber++) {
                        System.out.println("Booking details for room " + roomNumber + ":");
                
                        System.out.print("Enter first name: ");
                        firstName = scanner.nextLine();
                
                        System.out.print("Enter last name: ");
                        lastName = scanner.nextLine();
                
                        do {
                            System.out.print("Enter length of stay (minimum 1 day): ");
                            lengthOfStay = safeReadInt(scanner);
                            scanner.nextLine(); // Consume the leftover newline
                            if (lengthOfStay < 1) {
                                System.out.println("Invalid length of stay. The minimum length of stay is 1 day.");
                            }
                        } while (lengthOfStay < 1);
                
                        // Get the allowed bed types for the selected room class
                        bedTypes = allowedBedTypes.get(roomClass);
                        bedType = null;
                        while (bedType == null) {
                            System.out.println("Choose bed type:");
                            for (int i = 0; i < bedTypes.size(); i++) {
                                System.out.println((i + 1) + ". " + bedTypes.get(i));
                            }
                            System.out.print("Enter choice: ");
                            int bedTypeChoice = safeReadInt(scanner);
                            scanner.nextLine(); // Consume the leftover newline
                            if (bedTypeChoice < 1 || bedTypeChoice > bedTypes.size()) {
                                System.out.println("Invalid choice, please enter a valid option.");
                            } else {
                                bedType = bedTypes.get(bedTypeChoice - 1);
                            }
                        }
                
                        system.reserveRoom(firstName, lastName, lengthOfStay, roomClass, bedType);
                    }
                    break;  
        
                        
                case 9:
                    System.out.print("Enter the room number of the reservation to delete: ");
                    int roomNumberToDelete = safeReadInt(scanner);
                    scanner.nextLine(); // Consume the newline
                    system.deleteReservation(roomNumberToDelete);
                    break;

                case 10:
                    System.out.println("Exiting system...");
                    scanner.close();
                    return;
    
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

}