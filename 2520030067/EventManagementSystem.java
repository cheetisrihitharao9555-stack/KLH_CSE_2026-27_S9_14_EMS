import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
    static final String EVENT_FILE = "events.txt";
    static final String REGISTRATION_FILE = "registrations.txt";
    static final String VOLUNTEER_FILE = "volunteers.txt";

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

    static int eventCount = 0;

    // Date format
    static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    // =========================================================
    // MAIN METHOD
    // =========================================================

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // Load saved data from text files
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
                    System.out.println("Thank you for using Event Management System!");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
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

        // Prevent | because it is used as a separator
        if (username.contains("|")) {
            System.out.println("Username cannot contain '|'.");
            return;
        }

        // Check duplicate username
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

        // Store user in memory
        usernames[userCount] = username;
        passwords[userCount] = password;
        roles[userCount] = role;

        userCount++;

        // Save user in text file
        saveUsers();

        System.out.println();
        System.out.println("Account created successfully!");
        System.out.println("Username : " + username);
        System.out.println("Role     : " + role);
        System.out.println("Saved in  : users.txt");
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

    static void organizerMenu(Scanner sc, String organizerName) {

        while (true) {

            System.out.println();
            System.out.println("========== ORGANIZER MENU ==========");
            System.out.println("1. Add Event");
            System.out.println("2. View All Events");
            System.out.println("3. Search Event");
            System.out.println("4. View My Events");
            System.out.println("5. Logout");

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
                    displayOrganizerEvents(organizerName);
                    break;

                case 5:
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

    static void addEvent(Scanner sc, String organizerName) {

        if (eventCount >= 100) {
            System.out.println("Event storage is full.");
            return;
        }

        System.out.println();
        System.out.println("========== ADD EVENT ==========");

        System.out.print("Enter Event Name: ");
        String name = sc.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Event name cannot be empty.");
            return;
        }

        System.out.print("Enter Event Description: ");
        String description = sc.nextLine().trim();

        // Check separator
        if (name.contains("|") || description.contains("|")) {
            System.out.println("Event details cannot contain '|'.");
            return;
        }

        // -----------------------------------------------------
        // DATE
        // -----------------------------------------------------

        LocalDate selectedDate = null;
        String date = "";

        while (selectedDate == null) {

            System.out.print("Enter Date (DD-MM-YYYY): ");
            date = sc.nextLine().trim();

            try {

                selectedDate = LocalDate.parse(
                        date,
                        DATE_FORMAT
                );

            } catch (DateTimeParseException e) {

                System.out.println(
                        "Invalid date. Please use DD-MM-YYYY."
                );
            }
        }

        // Automatically calculate day
        DayOfWeek dayOfWeek = selectedDate.getDayOfWeek();

        String day = getDayName(dayOfWeek);

        // -----------------------------------------------------
        // TIME
        // -----------------------------------------------------

        System.out.print("Enter Start Time (HH:MM): ");
        String startTime = sc.nextLine().trim();

        System.out.print("Enter End Time (HH:MM): ");
        String endTime = sc.nextLine().trim();

        if (!isValidTime(startTime) || !isValidTime(endTime)) {
            System.out.println("Invalid time format. Use HH:MM.");
            return;
        }

        // -----------------------------------------------------
        // VENUE
        // -----------------------------------------------------

        System.out.print("Enter Venue: ");
        String venue = sc.nextLine().trim();

        if (venue.isEmpty()) {
            System.out.println("Venue cannot be empty.");
            return;
        }

        if (venue.contains("|")) {
            System.out.println("Venue cannot contain '|'.");
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

        eventCount++;

        // Save to events.txt
        saveEvents();

        System.out.println();
        System.out.println("Event added successfully!");
        System.out.println("------------------------------------------");
        System.out.println("Event       : " + name);
        System.out.println("Description : " + description);
        System.out.println("Date        : " + date);
        System.out.println("Day         : " + day);
        System.out.println("Time        : " + startTime + " - " + endTime);
        System.out.println("Venue       : " + venue);
        System.out.println("Organizer   : " + organizerName);
        System.out.println("------------------------------------------");
        System.out.println("Event saved in events.txt");
    }

    // =========================================================
    // VIEW ALL EVENTS
    // =========================================================

    static void displayEvents() {

        System.out.println();
        System.out.println("========== ALL EVENTS ==========");

        if (eventCount == 0) {
            System.out.println("No events available.");
            return;
        }

        for (int i = 0; i < eventCount; i++) {
            printEvent(i);
        }
    }

    // =========================================================
    // PRINT ONE EVENT
    // =========================================================

    static void printEvent(int index) {

        System.out.println();
        System.out.println("------------------------------------------");
        System.out.println("Event ID     : " + (index + 1));
        System.out.println("Event Name   : " + eventNames[index]);
        System.out.println("Description  : " + eventDescriptions[index]);
        System.out.println("Date         : " + eventDates[index]);
        System.out.println("Day          : " + eventDays[index]);
        System.out.println("Time         : "
                + startTimes[index]
                + " - "
                + endTimes[index]);
        System.out.println("Venue        : " + venues[index]);
        System.out.println("Organizer    : " + organizers[index]);
        System.out.println("------------------------------------------");
    }

    // =========================================================
    // VIEW ORGANIZER EVENTS
    // =========================================================

    static void displayOrganizerEvents(String organizerName) {

        System.out.println();
        System.out.println("========== MY EVENTS ==========");

        boolean found = false;

        for (int i = 0; i < eventCount; i++) {

            if (organizers[i].equalsIgnoreCase(organizerName)) {

                printEvent(i);
                found = true;
            }
        }

        if (!found) {
            System.out.println("You have not created any events.");
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
            System.out.println("2. Search Event");
            System.out.println("3. Register for Event");
            System.out.println("4. View My Registrations");
            System.out.println("5. Logout");

            int choice = readInt(sc, "Enter choice: ");

            switch (choice) {

                case 1:
                    displayEvents();
                    break;

                case 2:
                    searchEvent(sc);
                    break;

                case 3:
                    registerForEvent(sc, participantName);
                    break;

                case 4:
                    viewMyRegistrations(participantName);
                    break;

                case 5:
                    System.out.println("Logged out successfully.");
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // =========================================================
    // REGISTER FOR EVENT
    // =========================================================

    static void registerForEvent(
            Scanner sc,
            String participantName) {

        if (eventCount == 0) {
            System.out.println("No events available.");
            return;
        }

        displayEvents();

        int eventId = readInt(sc, "Enter Event ID: ");

        if (eventId < 1 || eventId > eventCount) {
            System.out.println("Invalid Event ID.");
            return;
        }

        int index = eventId - 1;

        if (alreadyRegistered(
                participantName,
                eventNames[index])) {

            System.out.println(
                    "You are already registered for this event."
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
                            + "\n"
            );

            writer.close();

            System.out.println();
            System.out.println("Registration successful!");
            System.out.println("Participant : " + participantName);
            System.out.println("Event       : " + eventNames[index]);
            System.out.println("Date        : " + eventDates[index]);
            System.out.println("Day         : " + eventDays[index]);
            System.out.println("Time        : "
                    + startTimes[index]
                    + " - "
                    + endTimes[index]);
            System.out.println("Venue       : " + venues[index]);

        } catch (IOException e) {

            System.out.println(
                    "Error saving registration: "
                            + e.getMessage()
            );
        }
    }

    // =========================================================
    // VIEW MY REGISTRATIONS
    // =========================================================

    static void viewMyRegistrations(String participantName) {

        File file = new File(REGISTRATION_FILE);

        System.out.println();
        System.out.println("========== MY REGISTRATIONS ==========");

        if (!file.exists()) {
            System.out.println("No registrations found.");
            return;
        }

        boolean found = false;

        try {

            BufferedReader reader =
                    new BufferedReader(
                            new FileReader(file)
                    );

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data = line.split("\\|", -1);

                if (data.length >= 7
                        && data[0].equals(participantName)) {

                    System.out.println();
                    System.out.println("Event : " + data[1]);
                    System.out.println("Date  : " + data[2]);
                    System.out.println("Day   : " + data[3]);
                    System.out.println("Time  : "
                            + data[4]
                            + " - "
                            + data[5]);
                    System.out.println("Venue : " + data[6]);

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
    // VOLUNTEER MENU
    // =========================================================

    static void volunteerMenu(
            Scanner sc,
            String volunteerName) {

        while (true) {

            System.out.println();
            System.out.println("========== VOLUNTEER MENU ==========");
            System.out.println("1. View Events");
            System.out.println("2. Search Event");
            System.out.println("3. Volunteer for Event");
            System.out.println("4. View My Volunteer Events");
            System.out.println("5. Logout");

            int choice = readInt(sc, "Enter choice: ");

            switch (choice) {

                case 1:
                    displayEvents();
                    break;

                case 2:
                    searchEvent(sc);
                    break;

                case 3:
                    volunteerForEvent(sc, volunteerName);
                    break;

                case 4:
                    viewMyVolunteerEvents(volunteerName);
                    break;

                case 5:
                    System.out.println("Logged out successfully.");
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // =========================================================
    // VOLUNTEER FOR EVENT
    // =========================================================

    static void volunteerForEvent(
            Scanner sc,
            String volunteerName) {

        if (eventCount == 0) {
            System.out.println("No events available.");
            return;
        }

        displayEvents();

        int eventId = readInt(sc, "Enter Event ID: ");

        if (eventId < 1 || eventId > eventCount) {
            System.out.println("Invalid Event ID.");
            return;
        }

        int index = eventId - 1;

        if (alreadyVolunteer(
                volunteerName,
                eventNames[index])) {

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
                            + "\n"
            );

            writer.close();

            System.out.println();
            System.out.println(
                    "Volunteer registration successful!"
            );

            System.out.println(
                    "Volunteer : " + volunteerName
            );

            System.out.println(
                    "Event     : " + eventNames[index]
            );

            System.out.println(
                    "Date      : " + eventDates[index]
            );

            System.out.println(
                    "Day       : " + eventDays[index]
            );

            System.out.println(
                    "Time      : "
                            + startTimes[index]
                            + " - "
                            + endTimes[index]
            );

            System.out.println(
                    "Venue     : " + venues[index]
            );

        } catch (IOException e) {

            System.out.println(
                    "Error saving volunteer data: "
                            + e.getMessage()
            );
        }
    }

    // =========================================================
    // VIEW MY VOLUNTEER EVENTS
    // =========================================================

    static void viewMyVolunteerEvents(
            String volunteerName) {

        File file = new File(VOLUNTEER_FILE);

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

            while ((line = reader.readLine()) != null) {

                String[] data = line.split("\\|", -1);

                if (data.length >= 7
                        && data[0].equals(volunteerName)) {

                    System.out.println();
                    System.out.println(
                            "Event : " + data[1]
                    );

                    System.out.println(
                            "Date  : " + data[2]
                    );

                    System.out.println(
                            "Day   : " + data[3]
                    );

                    System.out.println(
                            "Time  : "
                                    + data[4]
                                    + " - "
                                    + data[5]
                    );

                    System.out.println(
                            "Venue : " + data[6]
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
    // SEARCH EVENT USING KMP
    // =========================================================

    static void searchEvent(Scanner sc) {

        if (eventCount == 0) {
            System.out.println("No events available.");
            return;
        }

        System.out.println();
        System.out.println("========== SEARCH EVENT ==========");

        System.out.print(
                "Enter event name or keyword: "
        );

        String pattern =
                sc.nextLine().trim().toLowerCase();

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
             * CO2 - KMP String Matching
             *
             * Search the pattern in:
             * 1. Event name
             * 2. Event description
             * 3. Venue
             */

            if (KMPSearch(name, pattern)
                    || KMPSearch(description, pattern)
                    || KMPSearch(venue, pattern)) {

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

        int[] lps = buildLPS(pattern);

        int i = 0;
        int j = 0;

        while (i < text.length()) {

            if (text.charAt(i) == pattern.charAt(j)) {

                i++;
                j++;

                if (j == pattern.length()) {
                    return true;
                }

            } else {

                if (j != 0) {

                    j = lps[j - 1];

                } else {

                    i++;
                }
            }
        }

        return false;
    }

    // =========================================================
    // BUILD LPS ARRAY FOR KMP
    // =========================================================

    static int[] buildLPS(String pattern) {

        int[] lps =
                new int[pattern.length()];

        int length = 0;
        int i = 1;

        while (i < pattern.length()) {

            if (pattern.charAt(i)
                    == pattern.charAt(length)) {

                length++;

                lps[i] = length;

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
    // SAVE USERS TO users.txt
    // =========================================================

    static void saveUsers() {

        try {

            FileWriter writer =
                    new FileWriter(USER_FILE);

            for (int i = 0; i < userCount; i++) {

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
    // LOAD USERS FROM users.txt
    // =========================================================

    static void loadUsers() {

        File file = new File(USER_FILE);

        if (!file.exists()) {
            return;
        }

        try {

            BufferedReader reader =
                    new BufferedReader(
                            new FileReader(file)
                    );

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] data =
                        line.split("\\|", -1);

                if (data.length == 3
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
    // SAVE EVENTS TO events.txt
    // =========================================================

    static void saveEvents() {

        try {

            FileWriter writer =
                    new FileWriter(EVENT_FILE);

            for (int i = 0; i < eventCount; i++) {

                writer.write(
                        eventNames[i]
                                + "|"
                                + eventDescriptions[i]
                                + "|"
                                + eventDates[i]
                                + "|"
                                + eventDays[i]
                                + "|"
                                + startTimes[i]
                                + "|"
                                + endTimes[i]
                                + "|"
                                + venues[i]
                                + "|"
                                + organizers[i]
                                + System.lineSeparator()
                );
            }

            writer.close();

        } catch (IOException e) {

            System.out.println(
                    "Error saving events: "
                            + e.getMessage()
            );
        }
    }

    // =========================================================
    // LOAD EVENTS FROM events.txt
    // =========================================================

    static void loadEvents() {

        File file = new File(EVENT_FILE);

        if (!file.exists()) {
            return;
        }

        try {

            BufferedReader reader =
                    new BufferedReader(
                            new FileReader(file)
                    );

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] data =
                        line.split("\\|", -1);

                if (data.length == 8
                        && eventCount < 100) {

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

                    eventCount++;
                }
            }

            reader.close();

        } catch (IOException e) {

            System.out.println(
                    "Error loading events: "
                            + e.getMessage()
            );
        }
    }

    // =========================================================
    // CHECK EXISTING PARTICIPANT REGISTRATION
    // =========================================================

    static boolean alreadyRegistered(
            String username,
            String eventName) {

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

            while ((line = reader.readLine()) != null) {

                String[] data =
                        line.split("\\|", -1);

                if (data.length >= 2
                        && data[0].equals(username)
                        && data[1].equals(eventName)) {

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
    // CHECK EXISTING VOLUNTEER ASSIGNMENT
    // =========================================================

    static boolean alreadyVolunteer(
            String username,
            String eventName) {

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

            while ((line = reader.readLine()) != null) {

                String[] data =
                        line.split("\\|", -1);

                if (data.length >= 2
                        && data[0].equals(username)
                        && data[1].equals(eventName)) {

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
    // GET DAY NAME
    // =========================================================

    static String getDayName(DayOfWeek day) {

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

    static boolean isValidTime(String time) {

        if (time == null || time.length() != 5) {
            return false;
        }

        if (time.charAt(2) != ':') {
            return false;
        }

        try {

            int hour =
                    Integer.parseInt(
                            time.substring(0, 2));

            int minute =
                    Integer.parseInt(
                            time.substring(3, 5));

            return hour >= 0
                    && hour <= 23
                    && minute >= 0
                    && minute <= 59;

        } catch (NumberFormatException e) {

            return false;
        }
    }

    // =========================================================
    // READ INTEGER SAFELY
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