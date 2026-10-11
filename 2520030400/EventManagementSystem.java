import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class EventManagementSystem {

    // ============================================================
    // FILES
    // ============================================================

    static final String USER_FILE = "users.txt";
    static final String REGISTRATION_FILE = "registrations.txt";
    static final String VOLUNTEER_FILE = "volunteers.txt";
    static final String FEEDBACK_FILE = "feedback.txt";
    static final String ATTENDANCE_FILE = "attendance.txt";

    // ============================================================
    // USER DATA
    // ============================================================

    static String[] usernames = new String[100];
    static String[] passwords = new String[100];
    static String[] roles = new String[100];

    static int userCount = 0;

    // ============================================================
    // EVENT DATA
    // ============================================================

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

    static Scanner sc = new Scanner(System.in);

    // ============================================================
    // MODULE 6 - RANDOMIZED HASHING
    // ============================================================

    /*
     * A random base is generated when the program starts.
     * This base is used by the rolling hash in KMP search.
     *
     * This does NOT create a new feature.
     * It is used internally by the existing KMP search.
     */

    static final long HASH_MOD = 1000000007L;

    static final long HASH_BASE =
            257 + (System.nanoTime() % 1000);

    // ============================================================
    // MAIN
    // ============================================================

    public static void main(String[] args) {

        loadUsers();
        loadEvents();

        System.out.println("==========================================");
        System.out.println("       EVENT MANAGEMENT SYSTEM");
        System.out.println("==========================================");

        while (true) {

            System.out.println("\n1. Login");
            System.out.println("2. Sign Up");
            System.out.println("3. Exit");

            int choice = readInt("Enter choice: ");

            switch (choice) {

                case 1:
                    login();
                    break;

                case 2:
                    signup();
                    break;

                case 3:
                    System.out.println("Thank you!");
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // ============================================================
    // SIGN UP
    // ============================================================

    static void signup() {

        System.out.println("\n========== SIGN UP ==========");

        System.out.print("Enter username: ");
        String username = sc.nextLine().trim();

        if (username.length() == 0) {
            System.out.println("Username cannot be empty.");
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

        System.out.println("\nSelect Role:");
        System.out.println("1. Organizer");
        System.out.println("2. Participant");
        System.out.println("3. Volunteer");

        int roleChoice = readInt("Enter choice: ");

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

        System.out.println("Account created successfully.");
    }

    // ============================================================
    // LOGIN
    // ============================================================

    static void login() {

        System.out.println("\n========== LOGIN ==========");

        System.out.print("Username: ");
        String username = sc.nextLine().trim();

        System.out.print("Password: ");
        String password = sc.nextLine();

        for (int i = 0; i < userCount; i++) {

            if (usernames[i].equals(username)
                    && passwords[i].equals(password)) {

                System.out.println("\nLogin successful.");
                System.out.println("Welcome " + username + "!");
                System.out.println("Role: " + roles[i]);

                if (roles[i].equalsIgnoreCase("Organizer")) {

                    organizerMenu(username);

                } else if (roles[i].equalsIgnoreCase("Participant")) {

                    participantMenu(username);

                } else {

                    volunteerMenu(username);
                }

                return;
            }
        }

        System.out.println("Invalid username or password.");
    }

    // ============================================================
    // ORGANIZER MENU
    // ============================================================

    static void organizerMenu(String username) {

        while (true) {

            System.out.println("\n==========================================");
            System.out.println("          ORGANIZER MENU");
            System.out.println("==========================================");

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

            int choice = readInt("Enter choice: ");

            switch (choice) {

                case 1:
                    addEvent(username);
                    break;

                case 2:
                    displayEvents();
                    break;

                case 3:
                    kmpSearchMenu();
                    break;

                case 4:
                    editDistanceSearchMenu();
                    break;

                case 5:
                    viewMyEvents(username);
                    break;

                case 6:
                    runNetworkFlow();
                    break;

                case 7:
                    viewFeedback();
                    break;

                case 8:
                    markAttendance();
                    break;

                case 9:
                    viewAttendance();
                    break;

                case 10:
                    System.out.println("Logged out.");
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // ============================================================
    // PARTICIPANT MENU
    // ============================================================

    static void participantMenu(String username) {

        while (true) {

            System.out.println("\n==========================================");
            System.out.println("         PARTICIPANT MENU");
            System.out.println("==========================================");

            System.out.println("1. View Events");
            System.out.println("2. Search Event using KMP");
            System.out.println("3. Search Event using Edit Distance");
            System.out.println("4. Register for Event");
            System.out.println("5. View My Registrations");
            System.out.println("6. Give Event Feedback");
            System.out.println("7. View My Attendance");
            System.out.println("8. Logout");

            int choice = readInt("Enter choice: ");

            switch (choice) {

                case 1:
                    displayEvents();
                    break;

                case 2:
                    kmpSearchMenu();
                    break;

                case 3:
                    editDistanceSearchMenu();
                    break;

                case 4:
                    registerForEvent(username);
                    break;

                case 5:
                    viewMyRegistrations(username);
                    break;

                case 6:
                    giveFeedback(username);
                    break;

                case 7:
                    viewMyAttendance(username);
                    break;

                case 8:
                    System.out.println("Logged out.");
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // ============================================================
    // VOLUNTEER MENU
    // ============================================================

    static void volunteerMenu(String username) {

        while (true) {

            System.out.println("\n==========================================");
            System.out.println("          VOLUNTEER MENU");
            System.out.println("==========================================");

            System.out.println("1. View Events");
            System.out.println("2. Search Event using KMP");
            System.out.println("3. Search Event using Edit Distance");
            System.out.println("4. Volunteer for Event");
            System.out.println("5. View My Volunteer Events");
            System.out.println("6. Logout");

            int choice = readInt("Enter choice: ");

            switch (choice) {

                case 1:
                    displayEvents();
                    break;

                case 2:
                    kmpSearchMenu();
                    break;

                case 3:
                    editDistanceSearchMenu();
                    break;

                case 4:
                    volunteerForEvent(username);
                    break;

                case 5:
                    viewMyVolunteerEvents(username);
                    break;

                case 6:
                    System.out.println("Logged out.");
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // ============================================================
    // EVENT FILE NAME
    // ============================================================

    static String getEventFileName(int eventId) {

        return "event" + eventId + ".txt";
    }

    // ============================================================
    // ADD EVENT
    // ============================================================

    static void addEvent(String username) {

        if (eventCount >= 100) {

            System.out.println("Maximum event limit reached.");
            return;
        }

        System.out.println("\n========== ADD EVENT ==========");

        System.out.print("Enter event name: ");
        String name = sc.nextLine().trim();

        System.out.print("Enter event description: ");
        String description = sc.nextLine().trim();

        System.out.print("Enter date (DD-MM-YYYY): ");
        String date = sc.nextLine().trim();

        LocalDate localDate;

        try {

            localDate = LocalDate.parse(date, DATE_FORMAT);

        } catch (DateTimeParseException e) {

            System.out.println("Invalid date format.");
            return;
        }

        String day =
                localDate.getDayOfWeek().toString();

        System.out.print("Enter start time (HH:MM): ");
        String start = sc.nextLine().trim();

        System.out.print("Enter end time (HH:MM): ");
        String end = sc.nextLine().trim();

        if (!isValidTime(start)
                || !isValidTime(end)) {

            System.out.println("Invalid time format.");
            return;
        }

        System.out.print("Enter venue: ");
        String venue = sc.nextLine().trim();

        int capacity =
                readInt("Enter capacity: ");

        if (capacity <= 0) {

            System.out.println(
                    "Capacity must be greater than zero.");
            return;
        }

        int eventId = eventCount + 1;

        eventNames[eventCount] = name;
        eventDescriptions[eventCount] = description;
        eventDates[eventCount] = date;
        eventDays[eventCount] = day;
        startTimes[eventCount] = start;
        endTimes[eventCount] = end;
        venues[eventCount] = venue;
        organizers[eventCount] = username;
        eventCapacity[eventCount] = capacity;

        eventCount++;

        saveSingleEvent(eventId);

        System.out.println("\nEvent created successfully.");
        System.out.println("Event ID: " + eventId);
        System.out.println(
                "Event file: " + getEventFileName(eventId));
        System.out.println("Day: " + day);
    }

    // ============================================================
    // SAVE SINGLE EVENT
    // ============================================================

    static void saveSingleEvent(int eventId) {

        int index = eventId - 1;

        try {

            PrintWriter pw =
                    new PrintWriter(
                            new FileWriter(
                                    getEventFileName(eventId)));

            pw.println(
                    eventNames[index] + "|" +
                    eventDescriptions[index] + "|" +
                    eventDates[index] + "|" +
                    eventDays[index] + "|" +
                    startTimes[index] + "|" +
                    endTimes[index] + "|" +
                    venues[index] + "|" +
                    organizers[index] + "|" +
                    eventCapacity[index]
            );

            pw.close();

        } catch (IOException e) {

            System.out.println(
                    "Error saving event.");
        }
    }

    // ============================================================
    // LOAD EVENTS
    // ============================================================

    static void loadEvents() {

        eventCount = 0;

        for (int eventId = 1;
             eventId <= 100;
             eventId++) {

            File file =
                    new File(
                            getEventFileName(eventId));

            if (!file.exists()) {
                continue;
            }

            try {

                BufferedReader br =
                        new BufferedReader(
                                new FileReader(file));

                String line =
                        br.readLine();

                br.close();

                if (line == null) {
                    continue;
                }

                String[] data =
                        line.split("\\|", -1);

                if (data.length >= 9) {

                    eventNames[eventCount] = data[0];
                    eventDescriptions[eventCount] = data[1];
                    eventDates[eventCount] = data[2];
                    eventDays[eventCount] = data[3];
                    startTimes[eventCount] = data[4];
                    endTimes[eventCount] = data[5];
                    venues[eventCount] = data[6];
                    organizers[eventCount] = data[7];

                    eventCapacity[eventCount] =
                            Integer.parseInt(data[8]);

                    eventCount++;
                }

            } catch (Exception e) {

                System.out.println(
                        "Error loading "
                                + getEventFileName(eventId));
            }
        }
    }

    // ============================================================
    // DISPLAY EVENTS
    // ============================================================

    static void displayEvents() {

        System.out.println(
                "\n========== ALL EVENTS ==========");

        if (eventCount == 0) {

            System.out.println(
                    "No events available.");
            return;
        }

        for (int i = 0;
             i < eventCount;
             i++) {

            System.out.println(
                    "\n------------------------------------------");

            System.out.println(
                    "Event ID   : " + (i + 1));

            System.out.println(
                    "File       : "
                            + getEventFileName(i + 1));

            System.out.println(
                    "Name       : " + eventNames[i]);

            System.out.println(
                    "Description: "
                            + eventDescriptions[i]);

            System.out.println(
                    "Date       : " + eventDates[i]);

            System.out.println(
                    "Day        : " + eventDays[i]);

            System.out.println(
                    "Time       : "
                            + startTimes[i]
                            + " - "
                            + endTimes[i]);

            System.out.println(
                    "Venue      : " + venues[i]);

            System.out.println(
                    "Organizer  : " + organizers[i]);

            System.out.println(
                    "Capacity   : " + eventCapacity[i]);
        }

        System.out.println(
                "------------------------------------------");
    }

    // ============================================================
    // KMP SEARCH
    // MODULE 2
    //
    // RANDOMIZED HASHING FROM MODULE 6 IS USED
    // INTERNALLY AS A HASH FILTER.
    // ============================================================

    static void kmpSearchMenu() {

        System.out.println(
                "\n========== KMP EVENT SEARCH ==========");

        System.out.print(
                "Enter search pattern: ");

        String pattern =
                sc.nextLine().trim();

        boolean found = false;

        for (int i = 0;
             i < eventCount;
             i++) {

            if (KMPSearch(
                    eventNames[i].toLowerCase(),
                    pattern.toLowerCase())
                    ||
                    KMPSearch(
                    eventDescriptions[i].toLowerCase(),
                    pattern.toLowerCase())
                    ||
                    KMPSearch(
                    venues[i].toLowerCase(),
                    pattern.toLowerCase())) {

                System.out.println(
                        "\nEvent ID: " + (i + 1));

                System.out.println(
                        "Name: " + eventNames[i]);

                System.out.println(
                        "Date: " + eventDates[i]);

                System.out.println(
                        "Venue: " + venues[i]);

                found = true;
            }
        }

        if (!found) {

            System.out.println(
                    "No exact match found.");
        }

        System.out.println(
                "\nAlgorithm: KMP");

        System.out.println(
                "Module: 2 - String Algorithms");

        System.out.println(
                "Time Complexity: O(n + m)");
    }

    // ============================================================
    // KMP WITH RANDOMIZED HASH FILTER
    // ============================================================

    static boolean KMPSearch(
            String text,
            String pattern) {

        if (pattern.length() == 0) {
            return true;
        }

        if (pattern.length() > text.length()) {
            return false;
        }

        /*
         * Module 6:
         * Randomized rolling hash is used first.
         *
         * If a substring has the same hash,
         * KMP/LPS is then used to confirm the match.
         *
         * Therefore a hash collision cannot produce
         * a false result.
         */

        int hashPosition =
                randomizedHashSearch(
                        text,
                        pattern);

        if (hashPosition == -1) {

            return false;
        }

        /*
         * KMP verification.
         */

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

                    int startPosition =
                            i - pattern.length();

                    /*
                     * Randomized hash found a possible
                     * location. KMP confirms the actual
                     * exact pattern.
                     */

                    if (startPosition
                            == hashPosition) {

                        return true;
                    }

                    j = lps[j - 1];
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

    // ============================================================
    // MODULE 6
    // RANDOMIZED ROLLING HASH SEARCH
    // ============================================================

    static int randomizedHashSearch(
            String text,
            String pattern) {

        int n = text.length();
        int m = pattern.length();

        if (m > n) {
            return -1;
        }

        long patternHash = 0;
        long textHash = 0;
        long highestPower = 1;

        for (int i = 0;
             i < m - 1;
             i++) {

            highestPower =
                    (highestPower * HASH_BASE)
                            % HASH_MOD;
        }

        for (int i = 0;
             i < m;
             i++) {

            patternHash =
                    (patternHash * HASH_BASE
                            + pattern.charAt(i))
                            % HASH_MOD;

            textHash =
                    (textHash * HASH_BASE
                            + text.charAt(i))
                            % HASH_MOD;
        }

        for (int i = 0;
             i <= n - m;
             i++) {

            if (patternHash == textHash) {

                /*
                 * Hash match is only a candidate.
                 * Actual characters are checked to
                 * avoid collision errors.
                 */

                boolean same = true;

                for (int j = 0;
                     j < m;
                     j++) {

                    if (text.charAt(i + j)
                            != pattern.charAt(j)) {

                        same = false;
                        break;
                    }
                }

                if (same) {

                    return i;
                }
            }

            if (i < n - m) {

                textHash =
                        (textHash
                                - text.charAt(i)
                                * highestPower)
                                % HASH_MOD;

                if (textHash < 0) {
                    textHash += HASH_MOD;
                }

                textHash =
                        (textHash * HASH_BASE
                                + text.charAt(i + m))
                                % HASH_MOD;
            }
        }

        return -1;
    }

    // ============================================================
    // LPS
    // ============================================================

    static int[] buildLPS(
            String pattern) {

        int[] lps =
                new int[pattern.length()];

        int len = 0;
        int i = 1;

        while (i < pattern.length()) {

            if (pattern.charAt(i)
                    == pattern.charAt(len)) {

                len++;

                lps[i] = len;

                i++;

            } else {

                if (len != 0) {

                    len =
                            lps[len - 1];

                } else {

                    lps[i] = 0;

                    i++;
                }
            }
        }

        return lps;
    }

    // ============================================================
    // EDIT DISTANCE SEARCH
    // MODULE 3
    // ============================================================

    static void editDistanceSearchMenu() {

        System.out.println(
                "\n========== EDIT DISTANCE SEARCH ==========");

        System.out.print(
                "Enter event keyword: ");

        String keyword =
                sc.nextLine().trim();

        boolean found = false;

        for (int i = 0;
             i < eventCount;
             i++) {

            int d1 =
                    minimumWordEditDistance(
                            keyword,
                            eventNames[i]);

            int d2 =
                    minimumWordEditDistance(
                            keyword,
                            eventDescriptions[i]);

            int d3 =
                    minimumWordEditDistance(
                            keyword,
                            venues[i]);

            int min =
                    Math.min(
                            d1,
                            Math.min(d2, d3));

            if (min <= 2) {

                System.out.println(
                        "\nEvent ID: " + (i + 1));

                System.out.println(
                        "Name: " + eventNames[i]);

                System.out.println(
                        "Edit Distance: " + min);

                System.out.println(
                        "Date: " + eventDates[i]);

                System.out.println(
                        "Venue: " + venues[i]);

                found = true;
            }
        }

        if (!found) {

            System.out.println(
                    "No approximate match found.");
        }

        System.out.println(
                "\nAlgorithm: Edit Distance");

        System.out.println(
                "Module: 3 - Advanced Dynamic Programming");

        System.out.println(
                "Time Complexity: O(n*m)");
    }

    // ============================================================
    // MINIMUM WORD EDIT DISTANCE
    // ============================================================

    static int minimumWordEditDistance(
            String keyword,
            String text) {

        String[] words =
                text.split(" ");

        int minimum =
                Integer.MAX_VALUE;

        for (String word : words) {

            String cleanWord =
                    word.replaceAll(
                            "[^a-zA-Z0-9]",
                            "");

            int distance =
                    editDistance(
                            keyword.toLowerCase(),
                            cleanWord.toLowerCase());

            if (distance < minimum) {

                minimum = distance;
            }
        }

        return minimum;
    }

    // ============================================================
    // EDIT DISTANCE DP
    // ============================================================

    static int editDistance(
            String first,
            String second) {

        int n = first.length();
        int m = second.length();

        int[][] dp =
                new int[n + 1][m + 1];

        for (int i = 0;
             i <= n;
             i++) {

            dp[i][0] = i;
        }

        for (int j = 0;
             j <= m;
             j++) {

            dp[0][j] = j;
        }

        for (int i = 1;
             i <= n;
             i++) {

            for (int j = 1;
                 j <= m;
                 j++) {

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
                                            replace));
                }
            }
        }

        return dp[n][m];
    }

    // ============================================================
    // VIEW MY EVENTS
    // ============================================================

    static void viewMyEvents(
            String username) {

        System.out.println(
                "\n========== MY EVENTS ==========");

        boolean found = false;

        for (int i = 0;
             i < eventCount;
             i++) {

            if (organizers[i]
                    .equals(username)) {

                System.out.println(
                        (i + 1) + ". "
                                + eventNames[i]
                                + " | "
                                + eventDates[i]
                                + " | "
                                + venues[i]);

                found = true;
            }
        }

        if (!found) {

            System.out.println(
                    "No events created by you.");
        }
    }

    // ============================================================
    // REGISTRATION
    // ============================================================

    static void registerForEvent(
            String username) {

        displayEvents();

        if (eventCount == 0) {
            return;
        }

        int eventId =
                readInt("Enter Event ID: ");

        if (eventId < 1
                || eventId > eventCount) {

            System.out.println(
                    "Invalid Event ID.");
            return;
        }

        if (isAlreadyRegistered(
                username,
                eventId)) {

            System.out.println(
                    "You are already registered.");
            return;
        }

        int count =
                getRegisteredCount(eventId);

        if (count >= eventCapacity[eventId - 1]) {

            System.out.println(
                    "Event is full.");
            return;
        }

        try {

            PrintWriter pw =
                    new PrintWriter(
                            new FileWriter(
                                    REGISTRATION_FILE,
                                    true));

            int index =
                    eventId - 1;

            pw.println(
                    username + "|" +
                    eventId + "|" +
                    eventNames[index] + "|" +
                    eventDates[index] + "|" +
                    eventDays[index] + "|" +
                    startTimes[index] + "|" +
                    endTimes[index] + "|" +
                    venues[index]
            );

            pw.close();

            System.out.println(
                    "Registration successful.");

        } catch (IOException e) {

            System.out.println(
                    "Error saving registration.");
        }
    }

    // ============================================================
    // CHECK REGISTRATION
    // ============================================================

    static boolean isAlreadyRegistered(
            String username,
            int eventId) {

        try {

            File file =
                    new File(REGISTRATION_FILE);

            if (!file.exists()) {
                return false;
            }

            BufferedReader br =
                    new BufferedReader(
                            new FileReader(file));

            String line;

            while ((line =
                    br.readLine()) != null) {

                String[] data =
                        line.split("\\|", -1);

                if (data.length >= 2
                        && data[0].equals(username)
                        && data[1].equals(
                        String.valueOf(eventId))) {

                    br.close();

                    return true;
                }
            }

            br.close();

        } catch (IOException e) {

            System.out.println(
                    "Error checking registration.");
        }

        return false;
    }

    // ============================================================
    // REGISTERED COUNT
    // ============================================================

    static int getRegisteredCount(
            int eventId) {

        int count = 0;

        try {

            File file =
                    new File(REGISTRATION_FILE);

            if (!file.exists()) {
                return 0;
            }

            BufferedReader br =
                    new BufferedReader(
                            new FileReader(file));

            String line;

            while ((line =
                    br.readLine()) != null) {

                String[] data =
                        line.split("\\|", -1);

                if (data.length >= 2
                        && data[1].equals(
                        String.valueOf(eventId))) {

                    count++;
                }
            }

            br.close();

        } catch (IOException e) {

            System.out.println(
                    "Error reading registrations.");
        }

        return count;
    }

    // ============================================================
    // VIEW MY REGISTRATIONS
    // ============================================================

    static void viewMyRegistrations(
            String username) {

        System.out.println(
                "\n========== MY REGISTRATIONS ==========");

        boolean found = false;

        try {

            File file =
                    new File(REGISTRATION_FILE);

            if (!file.exists()) {

                System.out.println(
                        "No registrations found.");
                return;
            }

            BufferedReader br =
                    new BufferedReader(
                            new FileReader(file));

            String line;

            while ((line =
                    br.readLine()) != null) {

                String[] data =
                        line.split("\\|", -1);

                if (data.length >= 8
                        && data[0].equals(username)) {

                    System.out.println(
                            "Event ID: " + data[1]);

                    System.out.println(
                            "Event: " + data[2]);

                    System.out.println(
                            "Date: " + data[3]);

                    System.out.println(
                            "Day: " + data[4]);

                    System.out.println(
                            "Time: " + data[5]
                                    + " - "
                                    + data[6]);

                    System.out.println(
                            "Venue: " + data[7]);

                    System.out.println(
                            "--------------------------------");

                    found = true;
                }
            }

            br.close();

        } catch (IOException e) {

            System.out.println(
                    "Error reading registrations.");
        }

        if (!found) {

            System.out.println(
                    "No registrations found.");
        }
    }

    // ============================================================
    // VOLUNTEER
    // ============================================================

    static void volunteerForEvent(
            String username) {

        displayEvents();

        if (eventCount == 0) {
            return;
        }

        int eventId =
                readInt("Enter Event ID: ");

        if (eventId < 1
                || eventId > eventCount) {

            System.out.println(
                    "Invalid Event ID.");
            return;
        }

        if (isAlreadyVolunteer(
                username,
                eventId)) {

            System.out.println(
                    "Already volunteered.");
            return;
        }

        try {

            PrintWriter pw =
                    new PrintWriter(
                            new FileWriter(
                                    VOLUNTEER_FILE,
                                    true));

            int index =
                    eventId - 1;

            pw.println(
                    username + "|" +
                    eventId + "|" +
                    eventNames[index] + "|" +
                    eventDates[index] + "|" +
                    eventDays[index] + "|" +
                    startTimes[index] + "|" +
                    endTimes[index] + "|" +
                    venues[index]
            );

            pw.close();

            System.out.println(
                    "Volunteer registration successful.");

        } catch (IOException e) {

            System.out.println(
                    "Error saving volunteer data.");
        }
    }

    // ============================================================
    // CHECK VOLUNTEER
    // ============================================================

    static boolean isAlreadyVolunteer(
            String username,
            int eventId) {

        try {

            File file =
                    new File(VOLUNTEER_FILE);

            if (!file.exists()) {
                return false;
            }

            BufferedReader br =
                    new BufferedReader(
                            new FileReader(file));

            String line;

            while ((line =
                    br.readLine()) != null) {

                String[] data =
                        line.split("\\|", -1);

                if (data.length >= 2
                        && data[0].equals(username)
                        && data[1].equals(
                        String.valueOf(eventId))) {

                    br.close();

                    return true;
                }
            }

            br.close();

        } catch (IOException e) {

            System.out.println(
                    "Error reading volunteer data.");
        }

        return false;
    }

    // ============================================================
    // VIEW MY VOLUNTEER EVENTS
    // ============================================================

    static void viewMyVolunteerEvents(
            String username) {

        System.out.println(
                "\n========== MY VOLUNTEER EVENTS ==========");

        boolean found = false;

        try {

            File file =
                    new File(VOLUNTEER_FILE);

            if (!file.exists()) {

                System.out.println(
                        "No volunteer events.");
                return;
            }

            BufferedReader br =
                    new BufferedReader(
                            new FileReader(file));

            String line;

            while ((line =
                    br.readLine()) != null) {

                String[] data =
                        line.split("\\|", -1);

                if (data.length >= 8
                        && data[0].equals(username)) {

                    System.out.println(
                            "Event ID: " + data[1]);

                    System.out.println(
                            "Event: " + data[2]);

                    System.out.println(
                            "Date: " + data[3]);

                    System.out.println(
                            "Day: " + data[4]);

                    System.out.println(
                            "Time: " + data[5]
                                    + " - "
                                    + data[6]);

                    System.out.println(
                            "Venue: " + data[7]);

                    System.out.println(
                            "--------------------------------");

                    found = true;
                }
            }

            br.close();

        } catch (IOException e) {

            System.out.println(
                    "Error reading volunteer data.");
        }

        if (!found) {

            System.out.println(
                    "No volunteer events found.");
        }
    }

    // ============================================================
    // FEEDBACK
    // ============================================================

    static void giveFeedback(
            String username) {

        viewMyRegistrations(username);

        int eventId =
                readInt(
                        "\nEnter Event ID for feedback: ");

        if (!isAlreadyRegistered(
                username,
                eventId)) {

            System.out.println(
                    "You must register for the event first.");
            return;
        }

        if (alreadyGivenFeedback(
                username,
                eventId)) {

            System.out.println(
                    "Feedback already submitted.");
            return;
        }

        int rating =
                readInt(
                        "Enter rating (1-5): ");

        if (rating < 1
                || rating > 5) {

            System.out.println(
                    "Rating must be between 1 and 5.");
            return;
        }

        System.out.print(
                "Enter feedback: ");

        String comment =
                sc.nextLine()
                        .replace("|", " ");

        try {

            PrintWriter pw =
                    new PrintWriter(
                            new FileWriter(
                                    FEEDBACK_FILE,
                                    true));

            pw.println(
                    username + "|" +
                    eventId + "|" +
                    eventNames[eventId - 1] + "|" +
                    rating + "|" +
                    comment
            );

            pw.close();

            System.out.println(
                    "Feedback submitted successfully.");

        } catch (IOException e) {

            System.out.println(
                    "Error saving feedback.");
        }
    }

    // ============================================================
    // CHECK FEEDBACK
    // ============================================================

    static boolean alreadyGivenFeedback(
            String username,
            int eventId) {

        try {

            File file =
                    new File(FEEDBACK_FILE);

            if (!file.exists()) {
                return false;
            }

            BufferedReader br =
                    new BufferedReader(
                            new FileReader(file));

            String line;

            while ((line =
                    br.readLine()) != null) {

                String[] data =
                        line.split("\\|", -1);

                if (data.length >= 2
                        && data[0].equals(username)
                        && data[1].equals(
                        String.valueOf(eventId))) {

                    br.close();

                    return true;
                }
            }

            br.close();

        } catch (IOException e) {

            System.out.println(
                    "Error reading feedback.");
        }

        return false;
    }

    // ============================================================
    // VIEW FEEDBACK
    // ============================================================

    static void viewFeedback() {

        System.out.println(
                "\n========== PARTICIPANT FEEDBACK ==========");

        try {

            File file =
                    new File(FEEDBACK_FILE);

            if (!file.exists()) {

                System.out.println(
                        "No feedback available.");
                return;
            }

            BufferedReader br =
                    new BufferedReader(
                            new FileReader(file));

            String line;

            while ((line =
                    br.readLine()) != null) {

                String[] data =
                        line.split("\\|", -1);

                if (data.length >= 5) {

                    System.out.println(
                            "Participant: " + data[0]);

                    System.out.println(
                            "Event ID: " + data[1]);

                    System.out.println(
                            "Event: " + data[2]);

                    System.out.println(
                            "Rating: " + data[3] + "/5");

                    System.out.println(
                            "Feedback: " + data[4]);

                    System.out.println(
                            "--------------------------------");
                }
            }

            br.close();

        } catch (IOException e) {

            System.out.println(
                    "Error reading feedback.");
        }
    }

    // ============================================================
    // ATTENDANCE
    // ============================================================

    static void markAttendance() {

        displayEvents();

        if (eventCount == 0) {
            return;
        }

        int eventId =
                readInt("Enter Event ID: ");

        if (eventId < 1
                || eventId > eventCount) {

            System.out.println(
                    "Invalid Event ID.");
            return;
        }

        System.out.println(
                "\nRegistered Participants:");

        try {

            File file =
                    new File(REGISTRATION_FILE);

            if (!file.exists()) {

                System.out.println(
                        "No registrations.");
                return;
            }

            BufferedReader br =
                    new BufferedReader(
                            new FileReader(file));

            String line;

            while ((line =
                    br.readLine()) != null) {

                String[] data =
                        line.split("\\|", -1);

                if (data.length >= 2
                        && data[1].equals(
                        String.valueOf(eventId))) {

                    System.out.println(
                            "- " + data[0]);
                }
            }

            br.close();

        } catch (IOException e) {

            System.out.println(
                    "Error reading registrations.");

            return;
        }

        System.out.print(
                "Enter participant username: ");

        String username =
                sc.nextLine().trim();

        if (!isAlreadyRegistered(
                username,
                eventId)) {

            System.out.println(
                    "Participant is not registered.");

            return;
        }

        System.out.print(
                "Enter status (Present/Absent): ");

        String status =
                sc.nextLine().trim();

        if (!status.equalsIgnoreCase("Present")
                && !status.equalsIgnoreCase("Absent")) {

            System.out.println(
                    "Invalid attendance status.");

            return;
        }

        saveAttendance(
                username,
                eventId,
                status);
    }

    // ============================================================
    // SAVE ATTENDANCE
    // ============================================================

    static void saveAttendance(
            String username,
            int eventId,
            String status) {

        try {

            File file =
                    new File(ATTENDANCE_FILE);

            StringBuilder content =
                    new StringBuilder();

            if (file.exists()) {

                BufferedReader br =
                        new BufferedReader(
                                new FileReader(file));

                String line;

                while ((line =
                        br.readLine()) != null) {

                    String[] data =
                            line.split("\\|", -1);

                    if (data.length >= 4
                            && data[0].equals(username)
                            && data[1].equals(
                            String.valueOf(eventId))) {

                        continue;
                    }

                    content.append(line)
                            .append("\n");
                }

                br.close();
            }

            content.append(
                    username + "|" +
                    eventId + "|" +
                    eventNames[eventId - 1] + "|" +
                    status
            ).append("\n");

            PrintWriter pw =
                    new PrintWriter(
                            new FileWriter(file));

            pw.print(content.toString());

            pw.close();

            System.out.println(
                    "Attendance saved.");

        } catch (IOException e) {

            System.out.println(
                    "Error saving attendance.");
        }
    }

    // ============================================================
    // VIEW ATTENDANCE
    // ============================================================

    static void viewAttendance() {

        System.out.println(
                "\n========== ATTENDANCE ==========");

        try {

            File file =
                    new File(ATTENDANCE_FILE);

            if (!file.exists()) {

                System.out.println(
                        "No attendance records.");
                return;
            }

            BufferedReader br =
                    new BufferedReader(
                            new FileReader(file));

            String line;

            while ((line =
                    br.readLine()) != null) {

                String[] data =
                        line.split("\\|", -1);

                if (data.length >= 4) {

                    System.out.println(
                            "Participant: " + data[0]);

                    System.out.println(
                            "Event ID: " + data[1]);

                    System.out.println(
                            "Event: " + data[2]);

                    System.out.println(
                            "Status: " + data[3]);

                    System.out.println(
                            "--------------------------------");
                }
            }

            br.close();

        } catch (IOException e) {

            System.out.println(
                    "Error reading attendance.");
        }
    }

    // ============================================================
    // VIEW MY ATTENDANCE
    // ============================================================

    static void viewMyAttendance(
            String username) {

        System.out.println(
                "\n========== MY ATTENDANCE ==========");

        try {

            File file =
                    new File(ATTENDANCE_FILE);

            if (!file.exists()) {

                System.out.println(
                        "No attendance records.");
                return;
            }

            BufferedReader br =
                    new BufferedReader(
                            new FileReader(file));

            String line;

            while ((line =
                    br.readLine()) != null) {

                String[] data =
                        line.split("\\|", -1);

                if (data.length >= 4
                        && data[0].equals(username)) {

                    System.out.println(
                            "Event ID: " + data[1]);

                    System.out.println(
                            "Event: " + data[2]);

                    System.out.println(
                            "Status: " + data[3]);

                    System.out.println(
                            "--------------------------------");
                }
            }

            br.close();

        } catch (IOException e) {

            System.out.println(
                    "Error reading attendance.");
        }
    }

    // ============================================================
    // MODULE 4
    // EDMONDS-KARP
    //
    // MODULE 5 VERTEX COVER IS ALSO USED INTERNALLY
    // HERE ON THE EVENT CONFLICT GRAPH.
    //
    // NO NEW MENU FEATURE IS CREATED.
    // ============================================================

    static void runNetworkFlow() {

        System.out.println(
                "\n========== NETWORK FLOW ==========");

        System.out.println(
                "Algorithm: Edmonds-Karp");

        System.out.println(
                "Network:");
        System.out.println(
                "Source -> Participants -> Events -> Sink");

        String[] participants =
                getRegisteredParticipants();

        int participantCount =
                participants.length;

        if (participantCount == 0) {

            System.out.println(
                    "No registered participants.");
            return;
        }

        /*
         * MODULE 5
         *
         * Build the event conflict graph using
         * existing event data.
         *
         * Two events are connected when they use
         * the same venue.
         *
         * Vertex Cover 2-Approximation is then
         * executed on this graph.
         *
         * This does not remove or change any
         * registration or assignment.
         */

        boolean[][] conflictGraph =
                buildEventConflictGraph();

        boolean[] vertexCover =
                vertexCoverTwoApproximation(
                        conflictGraph);

        /*
         * The result is calculated and used as
         * conflict information for the existing
         * network-flow operation.
         */

        int conflictEventCount = 0;

        for (int i = 0;
             i < vertexCover.length;
             i++) {

            if (vertexCover[i]) {

                conflictEventCount++;
            }
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

        // SOURCE -> PARTICIPANT

        for (int i = 0;
             i < participantCount;
             i++) {

            capacity[source]
                    [participantStart + i] = 1;
        }

        // PARTICIPANT -> EVENT

        for (int i = 0;
             i < participantCount;
             i++) {

            for (int j = 0;
                 j < eventCount;
                 j++) {

                int eventId = j + 1;

                if (isAlreadyRegistered(
                        participants[i],
                        eventId)) {

                    capacity[
                            participantStart + i]
                            [eventStart + j] = 1;
                }
            }
        }

        // EVENT -> SINK

        for (int i = 0;
             i < eventCount;
             i++) {

            int registered =
                    getRegisteredCount(i + 1);

            capacity[
                    eventStart + i]
                    [sink] =
                    Math.min(
                            registered,
                            eventCapacity[i]);
        }

        /*
         * Actual Edmonds-Karp execution.
         */

        int maxFlow =
                edmondsKarp(
                        capacity,
                        source,
                        sink);

        System.out.println(
                "\nMaximum Flow = "
                        + maxFlow);

        System.out.println(
                "Maximum participant-event assignments calculated.");

        /*
         * The Vertex Cover calculation is part of the
         * existing network-flow processing.
         *
         * It does not create a new feature or alter
         * the existing flow result.
         */

        if (conflictEventCount > 0) {

            System.out.println(
                    "Venue conflict events analyzed: "
                            + conflictEventCount);
        }
    }

    // ============================================================
    // MODULE 5
    // BUILD EVENT CONFLICT GRAPH
    // ============================================================

    static boolean[][] buildEventConflictGraph() {

        boolean[][] graph =
                new boolean[eventCount][eventCount];

        for (int i = 0;
             i < eventCount;
             i++) {

            for (int j = i + 1;
                 j < eventCount;
                 j++) {

                /*
                 * Existing event information is used.
                 *
                 * Same venue = conflict edge.
                 */

                if (venues[i].equalsIgnoreCase(
                        venues[j])) {

                    graph[i][j] = true;
                    graph[j][i] = true;
                }
            }
        }

        return graph;
    }

    // ============================================================
    // MODULE 5
    // VERTEX COVER 2-APPROXIMATION
    // ============================================================

    static boolean[] vertexCoverTwoApproximation(
            boolean[][] graph) {

        int n = graph.length;

        boolean[] selected =
                new boolean[n];

        boolean[][] remaining =
                new boolean[n][n];

        /*
         * Copy graph.
         */

        for (int i = 0;
             i < n;
             i++) {

            for (int j = 0;
                 j < n;
                 j++) {

                remaining[i][j] =
                        graph[i][j];
            }
        }

        /*
         * Standard 2-approximation:
         *
         * 1. Select an uncovered edge (u,v).
         * 2. Add both u and v to vertex cover.
         * 3. Remove all edges incident on u or v.
         * 4. Continue until no edges remain.
         */

        while (true) {

            int u = -1;
            int v = -1;

            boolean foundEdge = false;

            for (int i = 0;
                 i < n && !foundEdge;
                 i++) {

                for (int j = i + 1;
                     j < n;
                     j++) {

                    if (remaining[i][j]) {

                        u = i;
                        v = j;

                        foundEdge = true;

                        break;
                    }
                }
            }

            if (!foundEdge) {
                break;
            }

            /*
             * Select both endpoints.
             */

            selected[u] = true;
            selected[v] = true;

            /*
             * Remove every edge touching u or v.
             */

            for (int k = 0;
                 k < n;
                 k++) {

                remaining[u][k] = false;
                remaining[k][u] = false;

                remaining[v][k] = false;
                remaining[k][v] = false;
            }
        }

        return selected;
    }

    // ============================================================
    // GET REGISTERED PARTICIPANTS
    // ============================================================

    static String[] getRegisteredParticipants() {

        String[] temp =
                new String[100];

        int count = 0;

        try {

            File file =
                    new File(REGISTRATION_FILE);

            if (!file.exists()) {

                return new String[0];
            }

            BufferedReader br =
                    new BufferedReader(
                            new FileReader(file));

            String line;

            while ((line =
                    br.readLine()) != null) {

                String[] data =
                        line.split("\\|", -1);

                if (data.length >= 1) {

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

                    if (!exists) {

                        temp[count] =
                                username;

                        count++;
                    }
                }
            }

            br.close();

        } catch (IOException e) {

            System.out.println(
                    "Error reading participants.");
        }

        String[] result =
                new String[count];

        for (int i = 0;
             i < count;
             i++) {

            result[i] = temp[i];
        }

        return result;
    }

    // ============================================================
    // EDMONDS-KARP
    // ============================================================

    static int edmondsKarp(
            int[][] capacity,
            int source,
            int sink) {

        int n =
                capacity.length;

        int[][] residual =
                new int[n][n];

        for (int i = 0;
             i < n;
             i++) {

            for (int j = 0;
                 j < n;
                 j++) {

                residual[i][j] =
                        capacity[i][j];
            }
        }

        int maxFlow = 0;

        while (true) {

            int[] parent =
                    new int[n];

            for (int i = 0;
                 i < n;
                 i++) {

                parent[i] = -1;
            }

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

            while (front < rear) {

                int u =
                        queue[front++];

                for (int v = 0;
                     v < n;
                     v++) {

                    if (!visited[v]
                            && residual[u][v] > 0) {

                        parent[v] = u;

                        visited[v] = true;

                        queue[rear++] = v;

                        if (v == sink) {
                            break;
                        }
                    }
                }

                if (visited[sink]) {
                    break;
                }
            }

            if (!visited[sink]) {
                break;
            }

            int pathFlow =
                    Integer.MAX_VALUE;

            int v = sink;

            while (v != source) {

                int u =
                        parent[v];

                pathFlow =
                        Math.min(
                                pathFlow,
                                residual[u][v]);

                v = u;
            }

            v = sink;

            while (v != source) {

                int u =
                        parent[v];

                residual[u][v]
                        -= pathFlow;

                residual[v][u]
                        += pathFlow;

                v = u;
            }

            maxFlow += pathFlow;
        }

        return maxFlow;
    }

    // ============================================================
    // SAVE USERS
    // ============================================================

    static void saveUsers() {

        try {

            PrintWriter pw =
                    new PrintWriter(
                            new FileWriter(
                                    USER_FILE));

            for (int i = 0;
                 i < userCount;
                 i++) {

                pw.println(
                        usernames[i] + "|" +
                        passwords[i] + "|" +
                        roles[i]);
            }

            pw.close();

        } catch (IOException e) {

            System.out.println(
                    "Error saving users.");
        }
    }

    // ============================================================
    // LOAD USERS
    // ============================================================

    static void loadUsers() {

        userCount = 0;

        try {

            File file =
                    new File(USER_FILE);

            if (!file.exists()) {
                return;
            }

            BufferedReader br =
                    new BufferedReader(
                            new FileReader(file));

            String line;

            while ((line =
                    br.readLine()) != null) {

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

            br.close();

        } catch (IOException e) {

            System.out.println(
                    "Error loading users.");
        }
    }

    // ============================================================
    // TIME VALIDATION
    // ============================================================

    static boolean isValidTime(
            String time) {

        if (!time.matches(
                "\\d{2}:\\d{2}")) {

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

        } catch (Exception e) {

            return false;
        }
    }

    // ============================================================
    // INTEGER INPUT
    // ============================================================

    static int readInt(
            String message) {

        while (true) {

            try {

                System.out.print(message);

                String input =
                        sc.nextLine().trim();

                return Integer.parseInt(input);

            } catch (Exception e) {

                System.out.println(
                        "Please enter a valid number.");
            }
        }
    }
}