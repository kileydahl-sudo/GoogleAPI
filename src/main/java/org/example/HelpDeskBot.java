package org.example;

import java.util.Scanner;

import java.io.IOException;
import java.util.Scanner;

public class HelpDeskBot {

        public static void beginBot() throws Exception {
            CalendarQuickstart.prepService();
            boolean stillPicking = true;
            while(stillPicking) {
                System.out.println(presentMenu());
                int userChoice = getUserChoice();
                switch (userChoice) {
                    case 0:
                        System.out.println("See you later");
                        stillPicking = false;
                        break;
                    case 1:
                        System.out.println("You chose 1");
                        CalendarQuickstart.listUpcomingEvents();
                        break;
                    case 2:
                        System.out.println("You chose 2");
                        Scanner scan = new Scanner(System.in);
                        System.out.println("Please enter start date in format yyyy-MM-dd");
                        String start = scan.nextLine();
                        System.out.println("Please enter end date in format yyyy-MM-dd");
                        String end = scan.nextLine();
                        CalendarQuickstart.searchByDateRange(start, end);
                        break;
                    case 3:
                        System.out.println("You chose 3");
                        CalendarQuickstart.createEvent();
                        break;
                    case 4:
                        System.out.println("You chose 4");
                        CalendarQuickstart.checkAvailability();
                        break;
                    case 5:
                        System.out.println("You chose 5");
                        CalendarQuickstart.deleteEvent();
                        break;
                    case 6:
                        System.out.println("You chose 6");
                        CalendarQuickstart.listCalendars();

                }

            }


        }

        public static String presentMenu() {
            return "+----------------------------------+\n" +
                    "¦     Smart Scheduling Assistant  \uD83D\uDCEC           ¦\n" +
                    "¦----------------------------------¦\n" +
                    "¦  1. List upcoming events           |\n" +
                    "|  2. Search by date range           |\n" +
                    "|  3. Create an event                |\n" +
                    "|  4. Check availability (FreeBusy)  |\n" +
                    "|  5. Delete an event                |\n" +
                    "|  6. List all calendars             |\n" +
                    "|  0. Exit                             ¦\n" +
                    "+----------------------------------+\n" +
                    "Choice: ";
        }

        public static int getUserChoice() {
            Scanner scanner = new Scanner(System.in);
            return scanner.nextInt();
        }



    public static void main(String[] args) {
        try {
            beginBot();
             // or whatever method you want to test
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
