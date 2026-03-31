package org.example;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import com.google.api.services.calendar.model.FreeBusyRequest;
import com.google.api.services.calendar.model.FreeBusyRequestItem;
import com.google.api.services.calendar.model.FreeBusyResponse;

/* class to demonstrate use of Calendar events list API */
public class CalendarQuickstart {
    /**
     * Application name.
     */

    static Calendar service = null;
    private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";
    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    /**
     * Directory to store authorization tokens for this application.
     */
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES =
            Collections.singletonList(CalendarScopes.CALENDAR);
    private static final String CREDENTIALS_FILE_PATH = "/clientSecret.json";

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        // Load client secrets.
        InputStream in = CalendarQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        //returns an authorized Credential object.
        return credential;
    }

    public static void prepService() throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
          service =
                new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                        .setApplicationName(APPLICATION_NAME)
                        .build();
    }

    // List the next 10 events from the primary calendar.
    public static void listUpcomingEvents() throws IOException {
        DateTime now = new DateTime(System.currentTimeMillis());
        Events events = service.events().list("primary")
                .setMaxResults(10)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        List<Event> items = events.getItems();
        if (items.isEmpty()) {
            System.out.println("No upcoming events found.");
        } else {
            System.out.println("Upcoming events");
            for (Event event : items) {
                DateTime start = event.getStart().getDateTime();
                if (start == null) {
                    start = event.getStart().getDate();
                }
                System.out.printf("%s (%s)\n", event.getSummary(), start);
            }
        }
    }

    public static void searchByDateRange(String startDateStr, String endDateStr) throws IOException {

// -- Pattern 3: From a user-typed date string (e.g. "2026-03-05") -----------
        LocalDate userDate = LocalDate.parse(startDateStr);
        DateTime startOfDay = new DateTime(
                userDate.atStartOfDay(ZoneId.of("America/New_York"))
                        .toInstant().toEpochMilli());


        LocalDate endDateLD = LocalDate.parse(endDateStr);

        DateTime end = new DateTime(endDateLD.toString()+"T00:00:00-04:00");

        Events events = service.events().list("primary")
                .setMaxResults(10)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                /// need to learn to translate
                .setTimeMin(startOfDay)
                .setTimeMax(end)

                .execute();
        List<Event> items = events.getItems();
        if (items.isEmpty()) {
            System.out.println("No upcoming events found.");
        } else {
            System.out.println("Upcoming events");
            for (Event event : items) {
                DateTime start = event.getStart().getDateTime();
                if (start == null) {
                    start = event.getStart().getDate();
                }
                System.out.printf("%s (%s)\n", event.getSummary(), start);

            }

        }
    }

    public static void createEvent() throws IOException {
        Scanner scanner = new Scanner(System.in);
        Scanner scanner2 = new Scanner(System.in);

        System.out.println("Give summary");
        String summary = scanner.next();
        scanner.reset();
        System.out.println("Give Year");
        String year = scanner2.next();
        System.out.println("Give Month");
        String month = scanner.next();
        System.out.println("Give Day");
        String day = scanner.next();
        System.out.println("Give Hour");
        String hour = scanner.next();
        System.out.println("Give Min");
        String min = scanner.next();


        String startTime = year + "-" + month + "-" + day;
        // + "T" + hour + ":" + min + ":" + "00-4:003
        System.out.println("Give duration in Minutes");
        String duration = scanner.next();



        // 1. Build the Event object
        Event event = new Event()
                .setSummary(summary)
                .setDescription(summary)
                .setLocation("");

// 2. Set start time
        LocalDate userDate = LocalDate.parse(startTime);
        DateTime startOfDay = new DateTime(
                userDate.atStartOfDay(ZoneId.of("America/New_York"))
                        .toInstant().toEpochMilli());
        EventDateTime eventDt = new EventDateTime()
                .setDateTime(startOfDay)
                .setTimeZone("America/New_York");  // always include the timezone!




        //Year-Month-Day-Hour-Min
        event.setStart(eventDt);

        ZonedDateTime startZdt = LocalDateTime.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day), Integer.parseInt(hour), Integer.parseInt(min))
                .atZone(ZoneId.of("America/New_York"));





// 3. Set end time (1 hour later)
        event.setEnd(new EventDateTime()
                .setDateTime(new DateTime(startZdt.plusMinutes(Long.parseLong(duration)).toInstant().toEpochMilli()))
                .setTimeZone("America/New_York"));

        ArrayList<EventAttendee> peopleInvited = new ArrayList<>();

// 4. Add attendees (optional)
        System.out.println("If attendees, press 1. If not press 0");
        int num  = scanner.nextInt();
        if (num ==1) {
            EventAttendee eventAttendee = new EventAttendee();
            System.out.println("Give email");
            String email = scanner.next();
            eventAttendee.setEmail(email);
            peopleInvited.add(eventAttendee);


        }


// 6. Insert the event
        Event created = service.events()
                .insert("primary", event)
                .setSendUpdates("all")   // sends invite emails to attendees
                .execute();
        System.out.println("Created: " + created.getHtmlLink());
        System.out.println("Event ID: " + created.getId());  // save this to update/delete later
    }

    public static void checkAvailability() throws IOException {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Give startTime date + time");
        String startTime = scanner.next();
        System.out.println("Give endTime date + time");
        String endTime = scanner.next();


        // Check the next hour
        LocalDate userDate = LocalDate.parse(startTime);
        DateTime startOfDay = new DateTime(
                userDate.atStartOfDay(ZoneId.of("America/New_York"))
                        .toInstant().toEpochMilli());

        LocalDate userDate1 = LocalDate.parse(endTime);
        DateTime end = new DateTime(endTime);

        FreeBusyRequest request = new FreeBusyRequest()
                .setTimeMin(startOfDay)
                .setTimeMax(end)
                .setItems(List.of(new FreeBusyRequestItem().setId("primary")));

        FreeBusyResponse response = service.freebusy().query(request).execute();

// Parse the response: it's a map from calendar ID ? list of busy windows
        var busySlots = response.getCalendars().get("primary").getBusy();
        if (busySlots == null || busySlots.isEmpty()) {
            System.out.println("? Calendar is FREE during this window.");
        } else {
            System.out.println("? Busy during:");
            for (var slot : busySlots) {
                // Each slot has a start and end — these are RFC3339 timestamp strings
                // Example: "2026-03-05T10:00:00-05:00" to "2026-03-05T11:00:00-05:00"
                System.out.println("  " + slot.getStart() + "  ?  " + slot.getEnd());
            }
        }
    }

    public static void deleteEvent() throws IOException {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Give eventID");
        String eventId = scanner.next();
        System.out.println("Do you give permission");
        String  response = scanner.next();
        if (response.equalsIgnoreCase("no")) {
            System.out.println("Too Bad! I'm doing it sucker :)");
        }

        // PATCH: update only specific fields — everything else is unchanged
        Event patch = new Event().setSummary("Renamed Meeting");
        service.events().patch("primary", eventId, patch)
                .setSendUpdates("all").execute();

// UPDATE: replaces the entire event object — unset fields get cleared!
// Use this when you want to change many fields at once.
        Event fullUpdate = service.events().get("primary", eventId).execute();
        fullUpdate.setSummary("New Title");
        fullUpdate.setDescription("Updated description");
        service.events().update("primary", eventId, fullUpdate).execute();

// DELETE
        service.events().delete("primary", eventId).execute();
        System.out.println("Event deleted");


    }

    public static void listCalendars() throws IOException {
        // List every calendar visible to the authenticated user
        var calendarList = service.calendarList().list().execute();
        for (CalendarListEntry entry : calendarList.getItems()) {
            System.out.println(entry.getSummary() + "  (ID: " + entry.getId() + ")");
            // Use entry.getId() wherever the API asks for a calendarId
            // "primary" is just a shortcut alias for the user's main calendar
        }
    }
    public static void main(String[] args) {
        try {
            prepService();
            listUpcomingEvents(); // or whatever method you want to test
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}