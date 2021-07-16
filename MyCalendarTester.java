import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

/**
 * Class that holds main method to run MyCalendar class
 */
public class MyCalendarTester
{
    private MyCalendar myCalendar;
    private Scanner scanner;			// Takes user inputs
    private String input;					// Holds user input values

    MyCalendarTester()
    {
        scanner  = new Scanner(System.in);
        myCalendar = new MyCalendar();
        getEvents();								// Loads all events into MyCalendar
        myCalendar.display(); 					// Displays calendar for all months
        myCalendar.printEvents(); 				// Prints all events
        boolean run = true;
        
        do {
            System.out.println("MAIN MENU" +
                    "\nSelect one of the following main menu options" +
                    "\n[V]iew by [C]reate, [G]o to [E]vent list [D]elete [Q]uit");	// Main Menu
            System.out.print("->");
            input = scanner.nextLine().trim();
            switch (Character.toLowerCase(input.charAt(0)))
            {
                case 'v':
                    viewBy();
                    break;
                case 'c':
                    create();
                    break;
                case 'g':
                    goTo();
                    break;
                case 'e':
                    eventList();
                    break;
                case 'd':
                    delete();
                    break;
                case 'q':
                    System.out.println("Good Bye");
                    store();
                    run = false;
                    break;
            }
        } while(run);
    }

    /**
     * Method that displays user whether to view events by day or by month
     */
    private void viewBy()
    {
        System.out.print("View by? [D]ay or [M]onth?" +
                "\n->");
        input = scanner.nextLine().trim();
        switch (Character.toLowerCase(input.charAt(0)))
        {
            case 'd':
                dayView(null);
                break;
            case 'm':
                monthView();
                break;
            default:
                break;
        }
    }

    /**
     * Method that allows user to view events by a specific date and allowing user to navigate
     * back and forth
     * @param 	date 	specific date to look for event
     */
    private void dayView(LocalDate date)
    {
        boolean run = true;
        myCalendar.navigate(date);
        do {
            myCalendar.printDay();
            System.out.print("Select" +
                    "\n[P]revious [N]ext [G]o back" +
                    "\n->");
            input = scanner.nextLine().trim();
            switch (Character.toLowerCase(input.charAt(0)))
            {
                case 'p':
                    myCalendar.aDayBack();
                    break;
                case 'n':
                    myCalendar.aDayAhead();
                    break;
                case 'g':
                    run = false;
                    break;
            }
        } while(run);
    }
    
    /**
     * Method that allows user to view events by month while allowing user to navigate back and forth
     */
    private void monthView()
    {
        boolean run = true;
        myCalendar.navigate(null);
        do {
            myCalendar.printMonth();
            System.out.print("Select" +
                    "\n[P]revious [N]ext [G]o back" +
                    "\n->");
            input = scanner.nextLine().trim();
            switch (Character.toLowerCase(input.charAt(0)))
            {
                case 'p':
                    myCalendar.aMonthBack();
                    break;
                case 'n':
                    myCalendar.aMonthAhead();
                    break;
                case 'g':
                    run = false;
                    break;
            }
        } while(run);
    }

    /**
     * Method that creates a new event and adds to system
     */
    private void create()
    {
        Event event = new Event();
        TimeInterval interval = new TimeInterval();
        boolean run;
        
        do {
            run = false;
            System.out.println("----Create Event--------");
            System.out.print("Event Name: ");
            input = scanner.nextLine().trim();
            event.setName(input);
            System.out.print("Date(format: MM/DD/YYYY): ");
            input = scanner.nextLine().trim();
            
            if (input.length() == 10 && isDate(input))
            {
                interval.setStartDate(extractDate(input));
                System.out.print("Start time(format: HH:MM): ");
                input = scanner.nextLine().trim();
                if (input.length() == 5 && isTime(input))
                {
                    interval.setStartTime(extractTime(input));
                    System.out.print("Ending time(format: HH:MM): ");
                    input = scanner.nextLine().trim();
                    if (input.length() == 5 && isTime(input))
                    {
                        interval.setEndTime(extractTime(input));
                        event.setTimeInterval(interval);

                        if(!myCalendar.addEvent(event))
                        {
                            System.out.println("Event overlaps with existing event. Unable to add event" +
                                    "\nPlease add another event");
                            run = true;
                        } else System.out.println("Event was added successfully");
                    } else run = true;
                } else run = true;
            } else run = true;
            
            if (run)
                System.out.println("Wrong input. \nPlease check date and time.\nInput new date and time.");
        } while(run);
    }

    /**
     * Method that checks the validity of users input as a time unit format
     * @param 	s		User input string
     * @return 	true	returns true if string is input as a time unit format
     */
    private boolean isTime(String s) 
    {
        String[] splits = s.split(":");
        if (splits.length != 2)
            return false;
        for (String k: splits)
        {
            for (char c: k.toCharArray())
                if (!Character.isDigit(c))
                    return false;
        }
        if (s.compareTo("23:59") > 0)
            return false;
        return true;
    }

    /**
     * Method that allows user to view an event at a specified date
     */
    private void goTo()
    {
        boolean run;
        do {
            run = false;
            System.out.println("----Go To-------- ");
            System.out.print("Date(format: MM/DD/YYYY): ");
            input = scanner.nextLine().trim();
            if (input.length() == 10 && isDate(input))
            {
                dayView(extractDate(input));
            } else {
                System.out.println("Wrong date entry. Input new date entry.");
                run = true;
            }
        } while(run);
    }

    /**
     * Method that checks the validity of users input as a date unit format
     * @param 	s		User input string
     * @return	true	returns true if string is input as a date unit format
     */
    private boolean isDate(String s)
    {
        String[] splits = s.split("/");
        if (splits.length != 3) 
        {
            System.out.println("Not three splits");
            return false;
        }
        for (String k: splits)
        {
            for (char c: k.toCharArray())
                if (!Character.isDigit(c)) 
                {
                    System.out.println("Not digit");
                    return false;
                }
        }
        return true;
    }

    /**
     * Method that prints all events in the calendar
     */
    private void eventList() { myCalendar.printEventList(); }

    /**
     * Method that allows user to select type of event deletion
     */
    private void delete()
    {
        boolean run;
        do {
            run = false;
            System.out.print("-----Delete----" +
                    "\nSelect deletion type" +
                    "\n[S]elected [A]ll [DR]");
            input = scanner.nextLine().trim();
            switch (Character.toLowerCase(input.charAt(0)))
            {
                case 's':
                    deleteSelected();
                    break;
                case 'a':
                    deleteAll();
                    break;
                case 'd':
                    if (input.length() > 1 && Character.toLowerCase(input.charAt(0)) == 'r')
                    {
                        deleteRecurring();
                        break;
                    }
                default:
                    System.out.println("Wrong selection. Please make new selection.");
                    run = true;
            }
        } while(run);
    }

    /**
     * Method that allows user to delete a recurring event
     */
    private void deleteRecurring()
    {
        System.out.println("....delete recurring....");
        System.out.print("Event Name: ");
        String name = scanner.nextLine().trim();
        int deleted = myCalendar.deleteRecurring(name);
        System.out.println(deleted+ " Events have been deleted");
    }
    
    /**
     * Method that allows user to delete all events in a specified day
     */
    private void deleteAll() 
    {
        System.out.print("Date(format: MM/DD/YYYY): ");
        input = scanner.nextLine().trim();
        if (input.length() == 10 && isDate(input))
        {
            int deleted = myCalendar.deleteAllSinglesInDate(extractDate(input));
            System.out.println(deleted+ " Events have been deleted");
        } else System.out.println("Wrong date format");
    }

    /**
     * Method that allows user to delete specific event
     */
    private void deleteSelected()
    {
        System.out.print("Event Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Date(format: MM/DD/YYYY): ");
        input = scanner.nextLine().trim();
        if (input.length() == 10 && isDate(input))
        {
            int deleted = myCalendar.deleteSingle(name, extractDate(input));
            System.out.println(deleted+ " Events have been deleted");
        } else System.out.println("Wrong date format");
    }

    /**
     * Method that stores all events into system after quitting the application. 
     * An output.txt file is created with all updated events
     */
    private void store()
    {
        try {
            File file = new File("output.txt");
            FileWriter writer = new FileWriter(file, false);
            writer.write(myCalendar.storageString());
            writer.flush();
            writer.close();
        } catch ( IOException e) { }
    }

    /**
     * Method that extracts dates from events.txt file
     */
    private void getEvents()
    {
        try {
            File file = new File("events.txt");
            Scanner scanner = new Scanner(file);
            Event event;
            TimeInterval interval;
            String s;
            String[] splits, inner;
            int l;
            while (scanner.hasNext()) 
            {
                event = new Event();
                event.setName(scanner.nextLine().trim());
                interval = new TimeInterval();
                s = scanner.nextLine().trim();
                splits = s.split(" ");
                l = splits.length;

                if (l == 5)
                {
                    for (int j = 0; j < splits[0].length(); j++)
                        interval.addDay(splits[0].charAt(j));
                    s = splits[1];
                    interval.setStartTime(extractTime(s));
                    s = splits[2];
                    interval.setEndTime(extractTime(s));
                    s = splits[3];
                    interval.setStartDate(extractDate(s));
                    s = splits[4];
                    interval.setEndDate(extractDate(s));
                    interval.setRecurring(true);
                } else{
                    s = splits[0];
                    interval.setStartDate(extractDate(s));
                    s = splits[1];
                    interval.setStartTime(extractTime(s));
                    s = splits[2];
                    interval.setEndTime(extractTime(s));
                    interval.setRecurring(false);
                }

                event.setTimeInterval(interval);
                myCalendar.addEvent(event);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method that extracts date from string
     * @param	s				extracted string
     * @return	LocalDate		extracted string is parsed to a LocalDate obj
     */
    private LocalDate extractDate(String s)
    {
        if (s.length() == 10)
            return LocalDate.of(Integer.parseInt(s.split("/")[2]),  Integer.parseInt(s.split("/")[0]),
                    Integer.parseInt(s.split("/")[1]));
        return LocalDate.of(2000+Integer.parseInt(s.split("/")[2]),  Integer.parseInt(s.split("/")[0]),
                Integer.parseInt(s.split("/")[1]));
    }

    /**
     * Method that extracts time from string
     * @param 	s				extracted string
     * @return	LocalTime		extracted string is parsed to a LocalTime obj
     */
    private LocalTime extractTime(String s)
    {
        return LocalTime.of(Integer.parseInt(s.split(":")[0]),
                Integer.parseInt(s.split(":")[1]));
    }

    
    /**
     * Main Method. Runs MyCalendar
     * @param args
     */
    public static void main(String[] args)
    {
        new MyCalendarTester();
    }
    
}
