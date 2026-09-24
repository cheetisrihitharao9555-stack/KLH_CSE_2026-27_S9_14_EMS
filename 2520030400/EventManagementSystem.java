import java.io.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class EventManagementSystem {

    // =========================================================
    // TEXT FILES
    // =========================================================

    static final String USER_FILE = "users.txt";
    static final String REGISTRATION_FILE = "registrations.txt";
    static final String VOLUNTEER_FILE = "volunteers.txt";
    static final String FEEDBACK_FILE = "feedback.txt";
    static final String ATTENDANCE_FILE = "attendance.txt";

    // =========================================================
    // USER DATA
    // =========================================================

    static String[] usernames = new String[100];
    static String[] passwords = new String[100];
    static String[] roles = new String[100];

    static int userCount = 0;

    // =========================================================
    // EVENT DATA
    // =========================================================

    static String[] eventNames = new String[100];
    static String[] eventDescriptions = new String[100];
    static String[] eventDates = new String[100];
    static String[] eventDays = new String[100];
    static String[] startTimes = new String[100];
    static String[] endTimes = new String[100];
    static String[] venues = new String[100];
    static String[] organizers = new String[100];
    static int[] eventCapacity = new int[100];

    static int eventCount = 0;

    static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    // =========================================================
    // MAIN
    // =========================================================

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        loadUsers();
        loadEvents();

        while (true) {

            System.out.println();
            System.out.println("==========================================");
            System.out.println("       EVENT MANAGEMENT SYSTEM");
            System.out.println("==========================================");
            System.out.println("1. Sign Up");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.println("==========================================");

            int choice = readInt(sc, "Enter choice: ");

            switch (choice) {

                case 1:
                    signUp(sc);
                    break;

                case 2:
                    login(sc);
                    break;

                case 3:
                    System.out.println();
                    System.out.println(
                            "Thank you for using Event Management System!"
                    );
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // =========================================================
    // SIGN UP
    // =========================================================

    static void signUp(Scanner sc) {

        System.out.println();
        System.out.println("========== SIGN UP ==========");

        if (userCount >= 100) {
            System.out.println("User storage is full.");
            return;
        }

        System.out.print("Enter username: ");
        String username = sc.nextLine().trim();

        if (username.isEmpty()) {
            System.out.println("Username cannot be empty.");
            return;
        }

        if (username.contains("|")) {
            System.out.println("Username cannot contain '|'.");
            return;
        }

        for (int i = 0; i < userCount; i++) {

            if (usernames[i].equalsIgnoreCase(username)) {

                System.out.println("Username already exists.");
                return;
            }
        }

        System.out.print("Enter password: ");
        String password = sc.nextLine();

        if (password.isEmpty()) {
            System.out.println("Password cannot be empty.");
            return;
        }

        if (password.contains("|")) {
            System.out.println("Password cannot contain '|'.");
            return;
        }

        System.out.println();
        System.out.println("Select Role:");
        System.out.println("1. Organizer");
        System.out.println("2. Participant");
        System.out.println("3. Volunteer");

        int roleChoice = readInt(sc, "Enter choice: ");

        String role;

        if (roleChoice == 1) {
            role = "Organizer";
        } else if (roleChoice == 2) {
            role = "Participant";
        } else if (roleChoice == 3) {
            role = "Volunteer";
        } else {
            System.out.println("Invalid role.");
            return;
        }

        usernames[userCount] = username;
        passwords[userCount] = password;
        roles[userCount] = role;

        userCount++;

        saveUsers();

        System.out.println();
        System.out.println("Account created successfully!");
        System.out.println("Username : " + username);
        System.out.println("Role     : " + role);
        System.out.println("Saved in : users.txt");
    }

    // =========================================================
    // LOGIN
    // =========================================================

    static void login(Scanner sc) {

        System.out.println();
        System.out.println("========== LOGIN ==========");

        if (userCount == 0) {

            System.out.println("No users registered.");
            System.out.println("Please sign up first.");
            return;
        }

        System.out.print("Enter username: ");
        String username = sc.nextLine().trim();

        System.out.print("Enter password: ");
        String password = sc.nextLine();

        for (int i = 0; i < userCount; i++) {

            if (usernames[i].equals(username)
                    && passwords[i].equals(password)) {

                System.out.println();
                System.out.println("Login successful!");
                System.out.println("Welcome, " + username);
                System.out.println("Role: " + roles[i]);

                if (roles[i].equals("Organizer")) {

                    organizerMenu(sc, username);

                } else if (roles[i].equals("Participant")) {

                    participantMenu(sc, username);

                } else if (roles[i].equals("Volunteer")) {

                    volunteerMenu(sc, username);
                }

                return;
            }
        }

        System.out.println("Invalid username or password.");
    }

    // =========================================================
    // ORGANIZER MENU
    // =========================================================

    static void organizerMenu(
            Scanner sc,
            String organizerName) {

        while (true) {

            System.out.println();
            System.out.println("========== ORGANIZER MENU ==========");
            System.out.println("1. Add Event");
            System.out.println("2. View All Events");
            System.out.println("3. Search Event using KMP");
            System.out.println("4. Search Event using Edit Distance");
            System.out.println("5. View My Events");
            System.out.println("6. Network Flow - Event Assignment");
            System.out.println("7. View Participant Feedback");
            System.out.println("8. Mark Attendance");
            System.out.println("9. View Attendance");
            System.out.println("10. Logout");

            int choice = readInt(sc, "Enter choice: ");

            switch (choice) {

                case 1:
                    addEvent(sc, organizerName);
                    break;

                case 2:
                    displayEvents();
                    break;

                case 3:
                    searchEvent(sc);
                    break;

                case 4:
                    editDistanceSearch(sc);
                    break;

                case 5:
                    displayOrganizerEvents(organizerName);
                    break;

                case 6:
                    runNetworkFlow();
                    break;

                case 7:
                    viewAllFeedback();
                    break;

                case 8:
                    markAttendance(sc);
                    break;

                case 9:
                    viewAllAttendance();
                    break;

                case 10:
                    System.out.println("Logged out successfully.");
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // =========================================================
    // PARTICIPANT MENU
    // =========================================================

    static void participantMenu(
            Scanner sc,
            String participantName) {

        while (true) {

            System.out.println();
            System.out.println("========== PARTICIPANT MENU ==========");
            System.out.println("1. View Events");
            System.out.println("2. Search Event using KMP");
            System.out.println("3. Search Event using Edit Distance");
            System.out.println("4. Register for Event");
            System.out.println("5. View My Registrations");
            System.out.println("6. Give Event Feedback");
            System.out.println("7. View My Attendance");
            System.out.println("8. Logout");

            int choice = readInt(sc, "Enter choice: ");

            switch (choice) {

                case 1:
                    displayEvents();
                    break;

                case 2:
                    searchEvent(sc);
                    break;

                case 3:
                    editDistanceSearch(sc);
                    break;

                case 4:
                    registerForEvent(sc, participantName);
                    break;

                case 5:
                    viewMyRegistrations(participantName);
                    break;

                case 6:
                    giveFeedback(sc, participantName);
                    break;

                case 7:
                    viewMyAttendance(participantName);
                    break;

                case 8:
                    System.out.println("Logged out successfully.");
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // =========================================================
    // VOLUNTEER MENU
    // =========================================================

    static void volunteerMenu(
            Scanner sc,
            String volunteerName) {

        while (true) {

            System.out.println();
            System.out.println("========== VOLUNTEER MENU ==========");
            System.out.println("1. View Events");
            System.out.println("2. Search Event using KMP");
            System.out.println("3. Search Event using Edit Distance");
            System.out.println("4. Volunteer for Event");
            System.out.println("5. View My Volunteer Events");
            System.out.println("6. Logout");

            int choice = readInt(sc, "Enter choice: ");

            switch (choice) {

                case 1:
                    displayEvents();
                    break;

                case 2:
                    searchEvent(sc);
                    break;

                case 3:
                    editDistanceSearch(sc);
                    break;

                case 4:
                    volunteerForEvent(sc, volunteerName);
                    break;

                case 5:
                    viewMyVolunteerEvents(volunteerName);
                    break;

                case 6:
                    System.out.println("Logged out successfully.");
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // =========================================================
    // ADD EVENT
    // =========================================================

    static void addEvent(
            Scanner sc,
            String organizerName) {

        if (eventCount >= 100) {

            System.out.println("Event storage is full.");
            return;
        }

        System.out.println();
        System.out.println("========== ADD EVENT ==========");

        System.out.print("Enter Event Name: ");
        String name = sc.nextLine().trim();

        if (name.isEmpty()) {

            System.out.println(
                    "Event name cannot be empty."
            );
            return;
        }

        System.out.print("Enter Event Description: ");
        String description = sc.nextLine().trim();

        if (name.contains("|")
                || description.contains("|")) {

            System.out.println(
                    "Event details cannot contain '|'."
            );
            return;
        }

        // -----------------------------------------------------
        // DATE
        // -----------------------------------------------------

        LocalDate selectedDate = null;
        String date = "";

        while (selectedDate == null) {

            System.out.print(
                    "Enter Date (DD-MM-YYYY): "
            );

            date = sc.nextLine().trim();

            try {

                selectedDate =
                        LocalDate.parse(
                                date,
                                DATE_FORMAT
                        );

            } catch (DateTimeParseException e) {

                System.out.println(
                        "Invalid date. Please use DD-MM-YYYY."
                );
            }
        }

        DayOfWeek dayOfWeek =
                selectedDate.getDayOfWeek();

        String day =
                getDayName(dayOfWeek);

        // -----------------------------------------------------
        // TIME
        // -----------------------------------------------------

        System.out.print(
                "Enter Start Time (HH:MM): "
        );

        String startTime =
                sc.nextLine().trim();

        System.out.print(
                "Enter End Time (HH:MM): "
        );

        String endTime =
                sc.nextLine().trim();

        if (!isValidTime(startTime)
                || !isValidTime(endTime)) {

            System.out.println(
                    "Invalid time format. Use HH:MM."
            );
            return;
        }

        // -----------------------------------------------------
        // VENUE
        // -----------------------------------------------------

        System.out.print("Enter Venue: ");
        String venue = sc.nextLine().trim();

        if (venue.isEmpty()) {

            System.out.println(
                    "Venue cannot be empty."
            );
            return;
        }

        if (venue.contains("|")) {

            System.out.println(
                    "Venue cannot contain '|'."
            );
            return;
        }

        // -----------------------------------------------------
        // CAPACITY
        // -----------------------------------------------------

        int capacity =
                readInt(
                        sc,
                        "Enter Event Capacity: "
                );

        if (capacity <= 0) {

            System.out.println(
                    "Capacity must be greater than 0."
            );
            return;
        }

        // -----------------------------------------------------
        // STORE EVENT IN MEMORY
        // -----------------------------------------------------

        eventNames[eventCount] = name;
        eventDescriptions[eventCount] = description;
        eventDates[eventCount] = date;
        eventDays[eventCount] = day;
        startTimes[eventCount] = startTime;
        endTimes[eventCount] = endTime;
        venues[eventCount] = venue;
        organizers[eventCount] = organizerName;
        eventCapacity[eventCount] = capacity;

        int eventId = eventCount + 1;

        eventCount++;

        // -----------------------------------------------------
        // SAVE ONLY THIS EVENT
        // -----------------------------------------------------

        saveSingleEvent(eventId);

        System.out.println();
        System.out.println("Event added successfully!");
        System.out.println("------------------------------------------");
        System.out.println("Event ID     : " + eventId);
        System.out.println("Event        : " + name);
        System.out.println("Description  : " + description);
        System.out.println("Date         : " + date);
        System.out.println("Day          : " + day);
        System.out.println(
                "Time         : "
                        + startTime
                        + " - "
                        + endTime
        );
        System.out.println("Venue        : " + venue);
        System.out.println("Organizer    : " + organizerName);
        System.out.println("Capacity     : " + capacity);
        System.out.println("------------------------------------------");
        System.out.println(
                "Saved in     : "
                        + getEventFileName(eventId)
        );
    }

    // =========================================================
    // EVENT FILE NAME
    // =========================================================

    static String getEventFileName(int eventId) {

        return "event" + eventId + ".txt";
    }

    // =========================================================
    // SAVE SINGLE EVENT
    // =========================================================

    static void saveSingleEvent(int eventId) {

        int index = eventId - 1;

        String fileName =
                getEventFileName(eventId);

        try {

            FileWriter writer =
                    new FileWriter(fileName);

            writer.write(
                    eventNames[index]
                            + "|"
                            + eventDescriptions[index]
                            + "|"
                            + eventDates[index]
                            + "|"
                            + eventDays[index]
                            + "|"
                            + startTimes[index]
                            + "|"
                            + endTimes[index]
                            + "|"
                            + venues[index]
                            + "|"
                            + organizers[index]
                            + "|"
                            + eventCapacity[index]
                            + System.lineSeparator()
            );

            writer.close();

        } catch (IOException e) {

            System.out.println(
                    "Error saving "
                            + fileName
                            + ": "
                            + e.getMessage()
            );
        }
    }

    // =========================================================
    // LOAD ALL EVENT FILES
    // =========================================================

    static void loadEvents() {

        eventCount = 0;

        int eventId = 1;

        while (eventId <= 100) {

            String fileName =
                    getEventFileName(eventId);

            File file =
                    new File(fileName);

            if (!file.exists()) {

                /*
                 * Stop when the next sequential event
                 * file does not exist.
                 */
                break;
            }

            if (eventCount >= 100) {
                break;
            }

            try {

                BufferedReader reader =
                        new BufferedReader(
                                new FileReader(file)
                        );

                String line =
                        reader.readLine();

                reader.close();

                if (line != null
                        && !line.trim().isEmpty()) {

                    String[] data =
                            line.split("\\|", -1);

                    /*
                     * New format:
                     * name
                     * description
                     * date
                     * day
                     * start time
                     * end time
                     * venue
                     * organizer
                     * capacity
                     */

                    if (data.length >= 8) {

                        eventNames[eventCount] =
                                data[0];

                        eventDescriptions[eventCount] =
                                data[1];

                        eventDates[eventCount] =
                                data[2];

                        eventDays[eventCount] =
                                data[3];

                        startTimes[eventCount] =
                                data[4];

                        endTimes[eventCount] =
                                data[5];

                        venues[eventCount] =
                                data[6];

                        organizers[eventCount] =
                                data[7];

                        if (data.length >= 9) {

                            try {

                                eventCapacity[eventCount] =
                                        Integer.parseInt(data[8]);

                            } catch (NumberFormatException e) {

                                eventCapacity[eventCount] = 0;
                            }

                        } else {

                            eventCapacity[eventCount] = 0;
                        }

                        eventCount++;
                    }
                }

            } catch (IOException e) {

                System.out.println(
                        "Error loading "
                                + fileName
                                + ": "
                                + e.getMessage()
                );
            }

            eventId++;
        }

        if (eventCount > 0) {

            System.out.println(
                    eventCount
                            + " event file(s) loaded successfully."
            );
        }
    }

    // =========================================================
    // DISPLAY ALL EVENTS
    // =========================================================

    static void displayEvents() {

        System.out.println();
        System.out.println("========== ALL EVENTS ==========");

        if (eventCount == 0) {

            System.out.println(
                    "No events available."
            );
            return;
        }

        for (int i = 0; i < eventCount; i++) {

            printEvent(i);
        }
    }

    // =========================================================
    // PRINT EVENT
    // =========================================================

    static void printEvent(int index) {

        System.out.println();
        System.out.println("------------------------------------------");
        System.out.println(
                "Event ID     : "
                        + (index + 1)
        );
        System.out.println(
                "Event Name   : "
                        + eventNames[index]
        );
        System.out.println(
                "Description  : "
                        + eventDescriptions[index]
        );
        System.out.println(
                "Date         : "
                        + eventDates[index]
        );
        System.out.println(
                "Day          : "
                        + eventDays[index]
        );
        System.out.println(
                "Time         : "
                        + startTimes[index]
                        + " - "
                        + endTimes[index]
        );
        System.out.println(
                "Venue        : "
                        + venues[index]
        );
        System.out.println(
                "Organizer    : "
                        + organizers[index]
        );
        System.out.println(
                "Capacity     : "
                        + eventCapacity[index]
        );
        System.out.println(
                "File         : "
                        + getEventFileName(index + 1)
        );
        System.out.println("------------------------------------------");
    }

    // =========================================================
    // VIEW ORGANIZER EVENTS
    // =========================================================

    static void displayOrganizerEvents(
            String organizerName) {

        System.out.println();
        System.out.println("========== MY EVENTS ==========");

        boolean found = false;

        for (int i = 0; i < eventCount; i++) {

            if (organizers[i]
                    .equalsIgnoreCase(organizerName)) {

                printEvent(i);
                found = true;
            }
        }

        if (!found) {

            System.out.println(
                    "You have not created any events."
            );
        }
    }

    // =========================================================
    // REGISTER FOR EVENT
    // =========================================================

    static void registerForEvent(
            Scanner sc,
            String participantName) {

        if (eventCount == 0) {

            System.out.println(
                    "No events available."
            );
            return;
        }

        displayEvents();

        int eventId =
                readInt(
                        sc,
                        "Enter Event ID: "
                );

        if (eventId < 1
                || eventId > eventCount) {

            System.out.println(
                    "Invalid Event ID."
            );
            return;
        }

        int index = eventId - 1;

        if (alreadyRegistered(
                participantName,
                eventId)) {

            System.out.println(
                    "You are already registered for this event."
            );
            return;
        }

        int registeredCount =
                countRegistrations(eventId);

        if (registeredCount
                >= eventCapacity[index]) {

            System.out.println(
                    "Event capacity is full."
            );
            return;
        }

        try {

            FileWriter writer =
                    new FileWriter(
                            REGISTRATION_FILE,
                            true
                    );

            writer.write(
                    participantName
                            + "|"
                            + eventId
                            + "|"
                            + eventNames[index]
                            + "|"
                            + eventDates[index]
                            + "|"
                            + eventDays[index]
                            + "|"
                            + startTimes[index]
                            + "|"
                            + endTimes[index]
                            + "|"
                            + venues[index]
                            + System.lineSeparator()
            );

            writer.close();

            System.out.println();
            System.out.println(
                    "Registration successful!"
            );
            System.out.println(
                    "Participant : "
                            + participantName
            );
            System.out.println(
                    "Event       : "
                            + eventNames[index]
            );
            System.out.println(
                    "Date        : "
                            + eventDates[index]
            );
            System.out.println(
                    "Venue       : "
                            + venues[index]
            );

        } catch (IOException e) {

            System.out.println(
                    "Error saving registration: "
                            + e.getMessage()
            );
        }
    }

    // =========================================================
    // CHECK REGISTRATION
    // =========================================================

    static boolean alreadyRegistered(
            String username,
            int eventId) {

        File file =
                new File(REGISTRATION_FILE);

        if (!file.exists()) {
            return false;
        }

        try {

            BufferedReader reader =
                    new BufferedReader(
                            new FileReader(file)
                    );

            String line;

            while ((line =
                    reader.readLine()) != null) {

                String[] data =
                        line.split("\\|", -1);

                if (data.length >= 3
                        && data[0].equals(username)
                        && data[1].equals(
                                String.valueOf(eventId))) {

                    reader.close();
                    return true;
                }
            }

            reader.close();

        } catch (IOException e) {

            System.out.println(
                    "Error checking registration."
            );
        }

        return false;
    }

    // =========================================================
    // COUNT REGISTRATIONS
    // =========================================================

    static int countRegistrations(int eventId) {

        File file =
                new File(REGISTRATION_FILE);

        if (!file.exists()) {
            return 0;
        }

        int count = 0;

        try {

            BufferedReader reader =
                    new BufferedReader(
                            new FileReader(file)
                    );

            String line;

            while ((line =
                    reader.readLine()) != null) {

                String[] data =
                        line.split("\\|", -1);

                if (data.length >= 2
                        && data[1].equals(
                                String.valueOf(eventId))) {

                    count++;
                }
            }

            reader.close();

        } catch (IOException e) {

            System.out.println(
                    "Error counting registrations."
            );
        }

        return count;
    }

    // =========================================================
    // VIEW MY REGISTRATIONS
    // =========================================================

    static void viewMyRegistrations(
            String participantName) {

        File file =
                new File(REGISTRATION_FILE);

        System.out.println();
        System.out.println(
                "========== MY REGISTRATIONS =========="
        );

        if (!file.exists()) {

            System.out.println(
                    "No registrations found."
            );
            return;
        }

        boolean found = false;

        try {

            BufferedReader reader =
                    new BufferedReader(
                            new FileReader(file)
                    );

            String line;

            while ((line =
                    reader.readLine()) != null) {

                String[] data =
                        line.split("\\|", -1);

                if (data.length >= 8
                        && data[0].equals(
                                participantName)) {

                    System.out.println();
                    System.out.println(
                            "Event ID : " + data[1]
                    );
                    System.out.println(
                            "Event    : " + data[2]
                    );
                    System.out.println(
                            "Date     : " + data[3]
                    );
                    System.out.println(
                            "Day      : " + data[4]
                    );
                    System.out.println(
                            "Time     : "
                                    + data[5]
                                    + " - "
                                    + data[6]
                    );
                    System.out.println(
                            "Venue    : " + data[7]
                    );

                    found = true;
                }
            }

            reader.close();

        } catch (IOException e) {

            System.out.println(
                    "Error reading registrations: "
                            + e.getMessage()
            );
        }

        if (!found) {

            System.out.println(
                    "You have no event registrations."
            );
        }
    }

    // =========================================================
    // VOLUNTEER FOR EVENT
    // =========================================================

    static void volunteerForEvent(
            Scanner sc,
            String volunteerName) {

        if (eventCount == 0) {

            System.out.println(
                    "No events available."
            );
            return;
        }

        displayEvents();

        int eventId =
                readInt(
                        sc,
                        "Enter Event ID: "
                );

        if (eventId < 1
                || eventId > eventCount) {

            System.out.println(
                    "Invalid Event ID."
            );
            return;
        }

        int index = eventId - 1;

        if (alreadyVolunteer(
                volunteerName,
                eventId)) {

            System.out.println(
                    "You are already a volunteer for this event."
            );
            return;
        }

        try {

            FileWriter writer =
                    new FileWriter(
                            VOLUNTEER_FILE,
                            true
                    );

            writer.write(
                    volunteerName
                            + "|"
                            + eventId
                            + "|"
                            + eventNames[index]
                            + "|"
                            + eventDates[index]
                            + "|"
                            + eventDays[index]
                            + "|"
                            + startTimes[index]
                            + "|"
                            + endTimes[index]
                            + "|"
                            + venues[index]
                            + System.lineSeparator()
            );

            writer.close();

            System.out.println();
            System.out.println(
                    "Volunteer registration successful!"
            );
            System.out.println(
                    "Volunteer : "
                            + volunteerName
            );
            System.out.println(
                    "Event     : "
                            + eventNames[index]
            );

        } catch (IOException e) {

            System.out.println(
                    "Error saving volunteer data: "
                            + e.getMessage()
            );
        }
    }

    // =========================================================
    // CHECK VOLUNTEER
    // =========================================================

    static boolean alreadyVolunteer(
            String username,
            int eventId) {

        File file =
                new File(VOLUNTEER_FILE);

        if (!file.exists()) {
            return false;
        }

        try {

            BufferedReader reader =
                    new BufferedReader(
                            new FileReader(file)
                    );

            String line;

            while ((line =
                    reader.readLine()) != null) {

                String[] data =
                        line.split("\\|", -1);

                if (data.length >= 3
                        && data[0].equals(username)
                        && data[1].equals(
                                String.valueOf(eventId))) {

                    reader.close();
                    return true;
                }
            }

            reader.close();

        } catch (IOException e) {

            System.out.println(
                    "Error checking volunteer assignment."
            );
        }

        return false;
    }

    // =========================================================
    // VIEW VOLUNTEER EVENTS
    // =========================================================

    static void viewMyVolunteerEvents(
            String volunteerName) {

        File file =
                new File(VOLUNTEER_FILE);

        System.out.println();
        System.out.println(
                "========== MY VOLUNTEER EVENTS =========="
        );

        if (!file.exists()) {

            System.out.println(
                    "No volunteer assignments found."
            );
            return;
        }

        boolean found = false;

        try {

            BufferedReader reader =
                    new BufferedReader(
                            new FileReader(file)
                    );

            String line;

            while ((line =
                    reader.readLine()) != null) {

                String[] data =
                        line.split("\\|", -1);

                if (data.length >= 8
                        && data[0].equals(
                                volunteerName)) {

                    System.out.println();
                    System.out.println(
                            "Event ID : " + data[1]
                    );
                    System.out.println(
                            "Event    : " + data[2]
                    );
                    System.out.println(
                            "Date     : " + data[3]
                    );
                    System.out.println(
                            "Day      : " + data[4]
                    );
                    System.out.println(
                            "Time     : "
                                    + data[5]
                                    + " - "
                                    + data[6]
                    );
                    System.out.println(
                            "Venue    : " + data[7]
                    );

                    found = true;
                }
            }

            reader.close();

        } catch (IOException e) {

            System.out.println(
                    "Error reading volunteer data: "
                            + e.getMessage()
            );
        }

        if (!found) {

            System.out.println(
                    "You have no volunteer assignments."
            );
        }
    }

    // =========================================================
    // KMP SEARCH
    // =========================================================

    static void searchEvent(Scanner sc) {

        if (eventCount == 0) {

            System.out.println(
                    "No events available."
            );
            return;
        }

        System.out.println();
        System.out.println(
                "========== KMP EVENT SEARCH =========="
        );

        System.out.print(
                "Enter event name or keyword: "
        );

        String pattern =
                sc.nextLine()
                        .trim()
                        .toLowerCase();

        if (pattern.isEmpty()) {

            System.out.println(
                    "Search keyword cannot be empty."
            );
            return;
        }

        boolean found = false;

        for (int i = 0; i < eventCount; i++) {

            String name =
                    eventNames[i].toLowerCase();

            String description =
                    eventDescriptions[i].toLowerCase();

            String venue =
                    venues[i].toLowerCase();

            /*
             * CO2
             * KMP STRING MATCHING
             */

            if (KMPSearch(name, pattern)
                    || KMPSearch(
                            description,
                            pattern)
                    || KMPSearch(
                            venue,
                            pattern)) {

                printEvent(i);

                found = true;
            }
        }

        if (!found) {

            System.out.println();
            System.out.println(
                    "No matching events found."
            );
        }
    }

    // =========================================================
    // KMP ALGORITHM
    // =========================================================

    static boolean KMPSearch(
            String text,
            String pattern) {

        if (pattern.length() == 0) {
            return true;
        }

        if (text.length() == 0) {
            return false;
        }

        int[] lps =
                buildLPS(pattern);

        int i = 0;
        int j = 0;

        while (i < text.length()) {

            if (text.charAt(i)
                    == pattern.charAt(j)) {

                i++;
                j++;

                if (j == pattern.length()) {

                    return true;
                }

            } else {

                if (j != 0) {

                    j =
                            lps[j - 1];

                } else {

                    i++;
                }
            }
        }

        return false;
    }

    // =========================================================
    // LPS ARRAY
    // =========================================================

    static int[] buildLPS(
            String pattern) {

        int[] lps =
                new int[pattern.length()];

        int length = 0;
        int i = 1;

        while (i < pattern.length()) {

            if (pattern.charAt(i)
                    == pattern.charAt(length)) {

                length++;

                lps[i] =
                        length;

                i++;

            } else {

                if (length != 0) {

                    length =
                            lps[length - 1];

                } else {

                    lps[i] = 0;
                    i++;
                }
            }
        }

        return lps;
    }

    // =========================================================
    // EDIT DISTANCE SEARCH
    // =========================================================

    static void editDistanceSearch(
            Scanner sc) {

        if (eventCount == 0) {

            System.out.println(
                    "No events available."
            );
            return;
        }

        System.out.println();
        System.out.println(
                "========== EDIT DISTANCE SEARCH =========="
        );

        System.out.println(
                "This search allows small spelling mistakes."
        );

        System.out.print(
                "Enter event name or keyword: "
        );

        String pattern =
                sc.nextLine()
                        .trim()
                        .toLowerCase();

        if (pattern.isEmpty()) {

            System.out.println(
                    "Search keyword cannot be empty."
            );
            return;
        }

        int maxDistance = 2;

        boolean found = false;

        for (int i = 0; i < eventCount; i++) {

            int nameDistance =
                    minimumWordEditDistance(
                            pattern,
                            eventNames[i]
                    );

            int descriptionDistance =
                    minimumWordEditDistance(
                            pattern,
                            eventDescriptions[i]
                    );

            int venueDistance =
                    minimumWordEditDistance(
                            pattern,
                            venues[i]
                    );

            int distance =
                    Math.min(
                            nameDistance,
                            Math.min(
                                    descriptionDistance,
                                    venueDistance
                            )
                    );

            if (distance <= maxDistance) {

                printEvent(i);

                System.out.println(
                        "Edit Distance : "
                                + distance
                );

                if (distance == 0) {

                    System.out.println(
                            "Match Type    : Exact match"
                    );

                } else {

                    System.out.println(
                            "Match Type    : Approximate match"
                    );
                }

                found = true;
            }
        }

        if (!found) {

            System.out.println();
            System.out.println(
                    "No close matching events found."
            );
        }
    }

    // =========================================================
    // MINIMUM WORD EDIT DISTANCE
    // =========================================================

    static int minimumWordEditDistance(
            String pattern,
            String text) {

        if (text == null
                || text.trim().isEmpty()) {

            return Integer.MAX_VALUE;
        }

        String[] words =
                text.toLowerCase()
                        .split("\\s+");

        int minimum =
                Integer.MAX_VALUE;

        for (String word : words) {

            String cleanWord =
                    word.replaceAll(
                            "[^a-z0-9]",
                            ""
                    );

            if (!cleanWord.isEmpty()) {

                int distance =
                        editDistance(
                                pattern,
                                cleanWord
                        );

                if (distance < minimum) {

                    minimum = distance;
                }
            }
        }

        return minimum;
    }

    // =========================================================
    // EDIT DISTANCE
    // =========================================================

    static int editDistance(
            String first,
            String second) {

        int m = first.length();
        int n = second.length();

        int[][] dp =
                new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) {

            dp[i][0] = i;
        }

        for (int j = 0; j <= n; j++) {

            dp[0][j] = j;
        }

        for (int i = 1; i <= m; i++) {

            for (int j = 1; j <= n; j++) {

                if (first.charAt(i - 1)
                        == second.charAt(j - 1)) {

                    dp[i][j] =
                            dp[i - 1][j - 1];

                } else {

                    int insert =
                            dp[i][j - 1];

                    int delete =
                            dp[i - 1][j];

                    int replace =
                            dp[i - 1][j - 1];

                    dp[i][j] =
                            1 + Math.min(
                                    insert,
                                    Math.min(
                                            delete,
                                            replace
                                    )
                            );
                }
            }
        }

        return dp[m][n];
    }

    // =========================================================
    // FEEDBACK
    // =========================================================

    static void giveFeedback(
            Scanner sc,
            String participantName) {

        if (eventCount == 0) {

            System.out.println(
                    "No events available."
            );
            return;
        }

        displayEvents();

        int eventId =
                readInt(
                        sc,
                        "Enter Event ID: "
                );

        if (eventId < 1
                || eventId > eventCount) {

            System.out.println(
                    "Invalid Event ID."
            );
            return;
        }

        if (!alreadyRegistered(
                participantName,
                eventId)) {

            System.out.println(
                    "You must register for the event before giving feedback."
            );
            return;
        }

        if (alreadyGivenFeedback(
                participantName,
                eventId)) {

            System.out.println(
                    "You have already given feedback for this event."
            );
            return;
        }

        int rating;

        while (true) {

            rating =
                    readInt(
                            sc,
                            "Enter rating (1-5): "
                    );

            if (rating >= 1
                    && rating <= 5) {

                break;
            }

            System.out.println(
                    "Rating must be between 1 and 5."
            );
        }

        System.out.print(
                "Enter your feedback: "
        );

        String comment =
                sc.nextLine().trim();

        if (comment.contains("|")) {

            System.out.println(
                    "Feedback cannot contain '|'."
            );
            return;
        }

        int index = eventId - 1;

        try {

            FileWriter writer =
                    new FileWriter(
                            FEEDBACK_FILE,
                            true
                    );

            writer.write(
                    participantName
                            + "|"
                            + eventId
                            + "|"
                            + eventNames[index]
                            + "|"
                            + rating
                            + "|"
                            + comment
                            + System.lineSeparator()
            );

            writer.close();

            System.out.println();
            System.out.println(
                    "Feedback submitted successfully!"
            );

        } catch (IOException e) {

            System.out.println(
                    "Error saving feedback: "
                            + e.getMessage()
            );
        }
    }

    // =========================================================
    // CHECK FEEDBACK
    // =========================================================

    static boolean alreadyGivenFeedback(
            String username,
            int eventId) {

        File file =
                new File(FEEDBACK_FILE);

        if (!file.exists()) {
            return false;
        }

        try {

            BufferedReader reader =
                    new BufferedReader(
                            new FileReader(file)
                    );

            String line;

            while ((line =
                    reader.readLine()) != null) {

                String[] data =
                        line.split("\\|", -1);

                if (data.length >= 4
                        && data[0].equals(username)
                        && data[1].equals(
                                String.valueOf(eventId))) {

                    reader.close();
                    return true;
                }
            }

            reader.close();

        } catch (IOException e) {

            System.out.println(
                    "Error checking feedback."
            );
        }

        return false;
    }

    // =========================================================
    // VIEW ALL FEEDBACK
    // =========================================================

    static void viewAllFeedback() {

        File file =
                new File(FEEDBACK_FILE);

        System.out.println();
        System.out.println(
                "========== PARTICIPANT FEEDBACK =========="
        );

        if (!file.exists()) {

            System.out.println(
                    "No feedback available."
            );
            return;
        }

        boolean found = false;

        try {

            BufferedReader reader =
                    new BufferedReader(
                            new FileReader(file)
                    );

            String line;

            while ((line =
                    reader.readLine()) != null) {

                String[] data =
                        line.split("\\|", -1);

                if (data.length >= 5) {

                    System.out.println();
                    System.out.println(
                            "Participant : " + data[0]
                    );
                    System.out.println(
                            "Event ID    : " + data[1]
                    );
                    System.out.println(
                            "Event       : " + data[2]
                    );
                    System.out.println(
                            "Rating      : " + data[3] + "/5"
                    );
                    System.out.println(
                            "Feedback    : " + data[4]
                    );
                    System.out.println(
                            "------------------------------------------"
                    );

                    found = true;
                }
            }

            reader.close();

        } catch (IOException e) {

            System.out.println(
                    "Error reading feedback: "
                            + e.getMessage()
            );
        }

        if (!found) {

            System.out.println(
                    "No feedback available."
            );
        }
    }

    // =========================================================
    // MARK ATTENDANCE
    // =========================================================

    static void markAttendance(Scanner sc) {

        if (eventCount == 0) {

            System.out.println(
                    "No events available."
            );
            return;
        }

        displayEvents();

        int eventId =
                readInt(
                        sc,
                        "Enter Event ID: "
                );

        if (eventId < 1
                || eventId > eventCount) {

            System.out.println(
                    "Invalid Event ID."
            );
            return;
        }

        boolean foundParticipant = false;

        File registrationFile =
                new File(REGISTRATION_FILE);

        if (!registrationFile.exists()) {

            System.out.println(
                    "No participants registered for this event."
            );
            return;
        }

        System.out.println();
        System.out.println(
                "Registered Participants:"
        );

        try {

            BufferedReader reader =
                    new BufferedReader(
                            new FileReader(
                                    registrationFile
                            )
                    );

            String line;

            while ((line =
                    reader.readLine()) != null) {

                String[] data =
                        line.split("\\|", -1);

                if (data.length >= 3
                        && data[1].equals(
                                String.valueOf(eventId))) {

                    System.out.println(
                            "- " + data[0]
                    );

                    foundParticipant = true;
                }
            }

            reader.close();

        } catch (IOException e) {

            System.out.println(
                    "Error reading registrations."
            );
            return;
        }

        if (!foundParticipant) {

            System.out.println(
                    "No participants registered for this event."
            );
            return;
        }

        System.out.print(
                "Enter participant username: "
        );

        String participant =
                sc.nextLine().trim();

        if (!alreadyRegistered(
                participant,
                eventId)) {

            System.out.println(
                    "This participant is not registered for the event."
            );
            return;
        }

        System.out.println();
        System.out.println("1. Present");
        System.out.println("2. Absent");

        int statusChoice =
                readInt(
                        sc,
                        "Enter attendance status: "
                );

        String status;

        if (statusChoice == 1) {

            status = "Present";

        } else if (statusChoice == 2) {

            status = "Absent";

        } else {

            System.out.println(
                    "Invalid attendance choice."
            );
            return;
        }

        saveAttendance(
                participant,
                eventId,
                status
        );
    }

    // =========================================================
    // SAVE / UPDATE ATTENDANCE
    // =========================================================

    static void saveAttendance(
            String participant,
            int eventId,
            String status) {

        File file =
                new File(ATTENDANCE_FILE);

        StringBuilder content =
                new StringBuilder();

        boolean updated = false;

        if (file.exists()) {

            try {

                BufferedReader reader =
                        new BufferedReader(
                                new FileReader(file)
                        );

                String line;

                while ((line =
                        reader.readLine()) != null) {

                    String[] data =
                            line.split("\\|", -1);

                    if (data.length >= 4
                            && data[0].equals(participant)
                            && data[1].equals(
                                    String.valueOf(eventId))) {

                        int index = eventId - 1;

                        content.append(
                                participant
                                        + "|"
                                        + eventId
                                        + "|"
                                        + eventNames[index]
                                        + "|"
                                        + status
                                        + System.lineSeparator()
                        );

                        updated = true;

                    } else {

                        content.append(
                                line
                                        + System.lineSeparator()
                        );
                    }
                }

                reader.close();

            } catch (IOException e) {

                System.out.println(
                        "Error reading attendance."
                );
                return;
            }
        }

        if (!updated) {

            int index = eventId - 1;

            content.append(
                    participant
                            + "|"
                            + eventId
                            + "|"
                            + eventNames[index]
                            + "|"
                            + status
                            + System.lineSeparator()
            );
        }

        try {

            FileWriter writer =
                    new FileWriter(file);

            writer.write(
                    content.toString()
            );

            writer.close();

            System.out.println();
            System.out.println(
                    "Attendance saved successfully!"
            );
            System.out.println(
                    "Participant : "
                            + participant
            );
            System.out.println(
                    "Event       : "
                            + eventNames[eventId - 1]
            );
            System.out.println(
                    "Status      : "
                            + status
            );

        } catch (IOException e) {

            System.out.println(
                    "Error saving attendance: "
                            + e.getMessage()
            );
        }
    }

    // =========================================================
    // VIEW MY ATTENDANCE
    // =========================================================

    static void viewMyAttendance(
            String participantName) {

        File file =
                new File(ATTENDANCE_FILE);

        System.out.println();
        System.out.println(
                "========== MY ATTENDANCE =========="
        );

        if (!file.exists()) {

            System.out.println(
                    "No attendance records found."
            );
            return;
        }

        boolean found = false;

        try {

            BufferedReader reader =
                    new BufferedReader(
                            new FileReader(file)
                    );

            String line;

            while ((line =
                    reader.readLine()) != null) {

                String[] data =
                        line.split("\\|", -1);

                if (data.length >= 4
                        && data[0].equals(
                                participantName)) {

                    System.out.println();
                    System.out.println(
                            "Event ID : " + data[1]
                    );
                    System.out.println(
                            "Event    : " + data[2]
                    );
                    System.out.println(
                            "Status   : " + data[3]
                    );

                    found = true;
                }
            }

            reader.close();

        } catch (IOException e) {

            System.out.println(
                    "Error reading attendance: "
                            + e.getMessage()
            );
        }

        if (!found) {

            System.out.println(
                    "No attendance records found."
            );
        }
    }

    // =========================================================
    // VIEW ALL ATTENDANCE
    // =========================================================

    static void viewAllAttendance() {

        File file =
                new File(ATTENDANCE_FILE);

        System.out.println();
        System.out.println(
                "========== ALL ATTENDANCE =========="
        );

        if (!file.exists()) {

            System.out.println(
                    "No attendance records found."
            );
            return;
        }

        boolean found = false;

        try {

            BufferedReader reader =
                    new BufferedReader(
                            new FileReader(file)
                    );

            String line;

            while ((line =
                    reader.readLine()) != null) {

                String[] data =
                        line.split("\\|", -1);

                if (data.length >= 4) {

                    System.out.println();
                    System.out.println(
                            "Participant : " + data[0]
                    );
                    System.out.println(
                            "Event ID    : " + data[1]
                    );
                    System.out.println(
                            "Event       : " + data[2]
                    );
                    System.out.println(
                            "Status      : " + data[3]
                    );
                    System.out.println(
                            "------------------------------------------"
                    );

                    found = true;
                }
            }

            reader.close();

        } catch (IOException e) {

            System.out.println(
                    "Error reading attendance: "
                            + e.getMessage()
            );
        }

        if (!found) {

            System.out.println(
                    "No attendance records found."
            );
        }
    }

    // =========================================================
    // NETWORK FLOW
    // =========================================================

    static void runNetworkFlow() {

        System.out.println();
        System.out.println(
                "========== NETWORK FLOW =========="
        );

        if (eventCount == 0) {

            System.out.println(
                    "No events available."
            );
            return;
        }

        /*
         * CO4 - NETWORK FLOW
         *
         * Source
         *   |
         * Participants
         *   |
         * Events
         *   |
         * Sink
         */

        String[] participants =
                getRegisteredParticipants();

        int participantCount =
                participants.length;

        if (participantCount == 0) {

            System.out.println(
                    "No participant registrations found."
            );
            return;
        }

        int source = 0;

        int participantStart = 1;

        int eventStart =
                participantStart
                        + participantCount;

        int sink =
                eventStart
                        + eventCount;

        int totalNodes =
                sink + 1;

        int[][] capacity =
                new int[totalNodes][totalNodes];

        // Source -> Participants
        for (int i = 0;
                i < participantCount;
                i++) {

            capacity[source]
                    [participantStart + i] = 1;
        }

        // Participants -> Events
        for (int i = 0;
                i < participantCount;
                i++) {

            String participant =
                    participants[i];

            for (int j = 0;
                    j < eventCount;
                    j++) {

                int eventId = j + 1;

                if (alreadyRegistered(
                        participant,
                        eventId)) {

                    capacity[
                            participantStart + i
                    ][
                            eventStart + j
                    ] = 1;
                }
            }
        }

        // Events -> Sink
        for (int j = 0;
                j < eventCount;
                j++) {

            int registered =
                    countRegistrations(j + 1);

            int cap =
                    Math.min(
                            registered,
                            eventCapacity[j]
                    );

            capacity[
                    eventStart + j
            ][sink] = cap;
        }

        int maxFlow =
                edmondsKarp(
                        capacity,
                        source,
                        sink
                );

        System.out.println();
        System.out.println(
                "Algorithm: Edmonds-Karp"
        );

        System.out.println();
        System.out.println(
                "Network:"
        );

        System.out.println(
                "Source -> Participants -> Events -> Sink"
        );

        System.out.println();
        System.out.println(
                "Maximum Flow = "
                        + maxFlow
        );

        System.out.println();
        System.out.println(
                "Maximum participant-event assignments calculated."
        );
    }

    // =========================================================
    // GET REGISTERED PARTICIPANTS
    // =========================================================

    static String[] getRegisteredParticipants() {

        String[] temp =
                new String[100];

        int count = 0;

        File file =
                new File(REGISTRATION_FILE);

        if (!file.exists()) {

            return new String[0];
        }

        try {

            BufferedReader reader =
                    new BufferedReader(
                            new FileReader(file)
                    );

            String line;

            while ((line =
                    reader.readLine()) != null) {

                String[] data =
                        line.split("\\|", -1);

                if (data.length >= 2) {

                    String username =
                            data[0];

                    boolean exists = false;

                    for (int i = 0;
                            i < count;
                            i++) {

                        if (temp[i]
                                .equals(username)) {

                            exists = true;
                            break;
                        }
                    }

                    if (!exists
                            && count < 100) {

                        temp[count] =
                                username;

                        count++;
                    }
                }
            }

            reader.close();

        } catch (IOException e) {

            System.out.println(
                    "Error reading participants."
            );
        }

        String[] result =
                new String[count];

        for (int i = 0; i < count; i++) {

            result[i] = temp[i];
        }

        return result;
    }

    // =========================================================
    // EDMONDS-KARP
    // =========================================================

    static int edmondsKarp(
            int[][] capacity,
            int source,
            int sink) {

        int n =
                capacity.length;

        int[][] residual =
                new int[n][n];

        for (int i = 0; i < n; i++) {

            for (int j = 0; j < n; j++) {

                residual[i][j] =
                        capacity[i][j];
            }
        }

        int maxFlow = 0;

        int[] parent =
                new int[n];

        while (bfs(
                residual,
                source,
                sink,
                parent)) {

            int pathFlow =
                    Integer.MAX_VALUE;

            int current = sink;

            while (current != source) {

                int previous =
                        parent[current];

                pathFlow =
                        Math.min(
                                pathFlow,
                                residual[
                                        previous
                                ][
                                        current
                                ]
                        );

                current =
                        previous;
            }

            current = sink;

            while (current != source) {

                int previous =
                        parent[current];

                residual[
                        previous
                ][
                        current
                ] -= pathFlow;

                residual[
                        current
                ][
                        previous
                ] += pathFlow;

                current =
                        previous;
            }

            maxFlow += pathFlow;
        }

        return maxFlow;
    }

    // =========================================================
    // BFS FOR EDMONDS-KARP
    // =========================================================

    static boolean bfs(
            int[][] residual,
            int source,
            int sink,
            int[] parent) {

        int n =
                residual.length;

        boolean[] visited =
                new boolean[n];

        int[] queue =
                new int[n];

        int front = 0;
        int rear = 0;

        queue[rear++] =
                source;

        visited[source] =
                true;

        parent[source] =
                -1;

        while (front < rear) {

            int current =
                    queue[front++];

            for (int next = 0;
                    next < n;
                    next++) {

                if (!visited[next]
                        && residual[
                                current
                        ][
                                next
                        ] > 0) {

                    queue[rear++] =
                            next;

                    parent[next] =
                            current;

                    visited[next] =
                            true;

                    if (next == sink) {

                        return true;
                    }
                }
            }
        }

        return false;
    }

    // =========================================================
    // SAVE USERS
    // =========================================================

    static void saveUsers() {

        try {

            FileWriter writer =
                    new FileWriter(
                            USER_FILE
                    );

            for (int i = 0;
                    i < userCount;
                    i++) {

                writer.write(
                        usernames[i]
                                + "|"
                                + passwords[i]
                                + "|"
                                + roles[i]
                                + System.lineSeparator()
                );
            }

            writer.close();

        } catch (IOException e) {

            System.out.println(
                    "Error saving users: "
                            + e.getMessage()
            );
        }
    }

    // =========================================================
    // LOAD USERS
    // =========================================================

    static void loadUsers() {

        File file =
                new File(USER_FILE);

        if (!file.exists()) {

            return;
        }

        try {

            BufferedReader reader =
                    new BufferedReader(
                            new FileReader(file)
                    );

            String line;

            while ((line =
                    reader.readLine()) != null) {

                String[] data =
                        line.split("\\|", -1);

                if (data.length >= 3
                        && userCount < 100) {

                    usernames[userCount] =
                            data[0];

                    passwords[userCount] =
                            data[1];

                    roles[userCount] =
                            data[2];

                    userCount++;
                }
            }

            reader.close();

        } catch (IOException e) {

            System.out.println(
                    "Error loading users: "
                            + e.getMessage()
            );
        }
    }

    // =========================================================
    // GET DAY NAME
    // =========================================================

    static String getDayName(
            DayOfWeek day) {

        switch (day) {

            case MONDAY:
                return "Monday";

            case TUESDAY:
                return "Tuesday";

            case WEDNESDAY:
                return "Wednesday";

            case THURSDAY:
                return "Thursday";

            case FRIDAY:
                return "Friday";

            case SATURDAY:
                return "Saturday";

            case SUNDAY:
                return "Sunday";

            default:
                return "";
        }
    }

    // =========================================================
    // VALIDATE TIME
    // =========================================================

    static boolean isValidTime(
            String time) {

        if (time == null
                || time.length() != 5) {

            return false;
        }

        if (time.charAt(2) != ':') {

            return false;
        }

        try {

            int hour =
                    Integer.parseInt(
                            time.substring(0, 2)
                    );

            int minute =
                    Integer.parseInt(
                            time.substring(3, 5)
                    );

            return hour >= 0
                    && hour <= 23
                    && minute >= 0
                    && minute <= 59;

        } catch (NumberFormatException e) {

            return false;
        }
    }

    // =========================================================
    // READ INTEGER
    // =========================================================

    static int readInt(
            Scanner sc,
            String message) {

        while (true) {

            System.out.print(message);

            String input =
                    sc.nextLine().trim();

            try {

                return Integer.parseInt(input);

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a valid number."
                );
            }
        }
    }
}