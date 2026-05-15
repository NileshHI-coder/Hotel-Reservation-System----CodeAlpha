import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

class Room {

    int roomNumber;
    String category;
    boolean booked;

    public Room(int roomNumber, String category) {

        this.roomNumber = roomNumber;
        this.category = category;
        this.booked = false;
    }
}

class Booking {

    String customerName;
    int roomNumber;
    String category;
    double payment;

    public Booking(String customerName,
                   int roomNumber,
                   String category,
                   double payment) {

        this.customerName = customerName;
        this.roomNumber = roomNumber;
        this.category = category;
        this.payment = payment;
    }
}

public class HotelReservationSystem extends JFrame {

    ArrayList<Room> rooms = new ArrayList<>();
    ArrayList<Booking> bookings = new ArrayList<>();

    JTextField nameField;
    JComboBox<String> categoryBox;

    JTable table;
    DefaultTableModel model;

    JLabel statusLabel;

    public HotelReservationSystem() {

        setTitle("Hotel Reservation System");
        setSize(900, 600);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));

        // Create Rooms
        createRooms();

        // Load Old Bookings
        loadBookingsFromFile();

        // ---------------- TOP PANEL ----------------

        JPanel topPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        topPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Hotel Reservation"));

        topPanel.add(new JLabel("Customer Name:"));

        nameField = new JTextField();

        topPanel.add(nameField);

        topPanel.add(new JLabel("Room Category:"));

        categoryBox = new JComboBox<>();

        categoryBox.addItem("Standard");
        categoryBox.addItem("Deluxe");
        categoryBox.addItem("Suite");

        topPanel.add(categoryBox);

        JButton bookButton =
                new JButton("Book Room");

        JButton cancelButton =
                new JButton("Cancel Booking");

        JButton searchButton =
                new JButton("Search Rooms");

        JButton clearButton =
                new JButton("Clear Table");

        topPanel.add(bookButton);
        topPanel.add(cancelButton);
        topPanel.add(searchButton);
        topPanel.add(clearButton);

        add(topPanel, BorderLayout.NORTH);

        // ---------------- TABLE ----------------

        model = new DefaultTableModel();

        model.addColumn("Customer Name");
        model.addColumn("Room Number");
        model.addColumn("Category");
        model.addColumn("Payment");

        table = new JTable(model);

        JScrollPane scrollPane =
                new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);

        // ---------------- BOTTOM PANEL ----------------

        JPanel bottomPanel =
                new JPanel(new BorderLayout());

        statusLabel =
                new JLabel("Welcome to Hotel Reservation System");

        JButton availableButton =
                new JButton("View Available Rooms");

        bottomPanel.add(statusLabel,
                BorderLayout.CENTER);

        bottomPanel.add(availableButton,
                BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        // ---------------- BUTTON ACTIONS ----------------

        // Book Room
        bookButton.addActionListener(
                new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                bookRoom();
            }
        });

        // Cancel Booking
        cancelButton.addActionListener(
                new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                cancelBooking();
            }
        });

        // Search Rooms
        searchButton.addActionListener(
                new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                searchRooms();
            }
        });

        // View Available Rooms
        availableButton.addActionListener(
                new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                viewAvailableRooms();
            }
        });

        // Clear Table
        clearButton.addActionListener(
                new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                clearTable();
            }
        });

        setVisible(true);
    }

    // ---------------- CREATE ROOMS ----------------

    public void createRooms() {

        rooms.add(new Room(101, "Standard"));
        rooms.add(new Room(102, "Standard"));

        rooms.add(new Room(201, "Deluxe"));
        rooms.add(new Room(202, "Deluxe"));

        rooms.add(new Room(301, "Suite"));
        rooms.add(new Room(302, "Suite"));
    }

    // ---------------- BOOK ROOM ----------------

    public void bookRoom() {

        String customerName =
                nameField.getText().trim();

        String category =
                categoryBox.getSelectedItem().toString();

        if(customerName.isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Please enter customer name.");

            return;
        }

        for(Room room : rooms) {

            if(room.category.equals(category)
                    && !room.booked) {

                room.booked = true;

                double payment =
                        calculatePayment(category);

                Booking booking =
                        new Booking(customerName,
                                room.roomNumber,
                                category,
                                payment);

                bookings.add(booking);

                model.addRow(new Object[] {

                        booking.customerName,
                        booking.roomNumber,
                        booking.category,
                        booking.payment
                });

                saveBookingToFile(booking);

                statusLabel.setText(
                        "Room Booked Successfully!");

                JOptionPane.showMessageDialog(this,

                        "Booking Successful!\n"
                                + "Room Number: "
                                + room.roomNumber
                                + "\nPayment: ₹"
                                + payment);

                nameField.setText("");

                return;
            }
        }

        JOptionPane.showMessageDialog(this,
                "No rooms available.");
    }

    // ---------------- CANCEL BOOKING ----------------

    public void cancelBooking() {

        int selectedRow = table.getSelectedRow();

        if(selectedRow == -1) {

            JOptionPane.showMessageDialog(this,
                    "Select booking to cancel.");

            return;
        }

        int roomNumber =
                (int) model.getValueAt(selectedRow, 1);

        for(Room room : rooms) {

            if(room.roomNumber == roomNumber) {

                room.booked = false;
            }
        }

        model.removeRow(selectedRow);

        statusLabel.setText(
                "Booking Cancelled Successfully!");
    }

    // ---------------- SEARCH ROOMS ----------------

    public void searchRooms() {

        String category =
                categoryBox.getSelectedItem().toString();

        StringBuilder result =
                new StringBuilder();

        for(Room room : rooms) {

            if(room.category.equals(category)
                    && !room.booked) {

                result.append(
                        "Room ")
                        .append(room.roomNumber)
                        .append(" - Available\n");
            }
        }

        if(result.length() == 0) {

            JOptionPane.showMessageDialog(this,
                    "No available rooms found.");
        }

        else {

            JOptionPane.showMessageDialog(this,
                    result.toString());
        }
    }

    // ---------------- VIEW AVAILABLE ROOMS ----------------

    public void viewAvailableRooms() {

        StringBuilder availableRooms =
                new StringBuilder();

        for(Room room : rooms) {

            if(!room.booked) {

                availableRooms.append(
                        "Room ")
                        .append(room.roomNumber)
                        .append(" - ")
                        .append(room.category)
                        .append("\n");
            }
        }

        JOptionPane.showMessageDialog(this,

                availableRooms.length() == 0
                        ? "No rooms available."
                        : availableRooms.toString());
    }

    // ---------------- PAYMENT ----------------

    public double calculatePayment(String category) {

        switch(category) {

            case "Standard":
                return 2000;

            case "Deluxe":
                return 4000;

            case "Suite":
                return 7000;

            default:
                return 0;
        }
    }

    // ---------------- FILE HANDLING ----------------

    public void saveBookingToFile(Booking booking) {

        try {

            FileWriter writer =
                    new FileWriter(
                            "bookings.txt",
                            true);

            writer.write(

                    booking.customerName + ","
                            + booking.roomNumber + ","
                            + booking.category + ","
                            + booking.payment + "\n"
            );

            writer.close();
        }

        catch(IOException e) {

            JOptionPane.showMessageDialog(this,

                    "Error saving booking.");
        }
    }

    // ---------------- LOAD BOOKINGS ----------------

    public void loadBookingsFromFile() {

        try {

            File file =
                    new File("bookings.txt");

            if(!file.exists()) {

                return;
            }

            BufferedReader reader =
                    new BufferedReader(
                            new FileReader(file));

            String line;

            while((line = reader.readLine()) != null) {

                String[] data =
                        line.split(",");

                String customerName =
                        data[0];

                int roomNumber =
                        Integer.parseInt(data[1]);

                String category =
                        data[2];

                double payment =
                        Double.parseDouble(data[3]);

                Booking booking =
                        new Booking(customerName,
                                roomNumber,
                                category,
                                payment);

                bookings.add(booking);

                model.addRow(new Object[] {

                        booking.customerName,
                        booking.roomNumber,
                        booking.category,
                        booking.payment
                });

                for(Room room : rooms) {

                    if(room.roomNumber == roomNumber) {

                        room.booked = true;
                    }
                }
            }

            reader.close();
        }

        catch(Exception e) {

            JOptionPane.showMessageDialog(this,

                    "Error loading bookings.");
        }
    }

    // ---------------- CLEAR TABLE ----------------

    public void clearTable() {

        model.setRowCount(0);

        bookings.clear();

        statusLabel.setText(
                "Table Cleared.");
    }

    // ---------------- MAIN METHOD ----------------

    public static void main(String[] args) {

        new HotelReservationSystem();
    }
}