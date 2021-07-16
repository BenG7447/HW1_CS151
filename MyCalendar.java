import java.util.ArrayList;
import java.time.LocalDate;

/**
 * Class that provides functions for displaying calendar and handling events
 */
public class MyCalendar
{
    private ArrayList<Event> events = new ArrayList<>();	 	// Holds scheduled events
    private LocalDate navigateDate; 							// Use for navigating day and months

    /**
     * Method that displays all 12 months of calendar
     */
    public void display() 
    {
        LocalDate date = LocalDate.now();
        System.out.println("day:" + date.getDayOfMonth());

        for (int i = 1; i <= 12; i++)
            draw(date.getYear(), i);
    }
    
    /**
     * Method that draws the calendar of the month provided. Events are attached to their specific dates
     * @param year		int of year to view
     * @param month		int of month to view
     */
    public void draw(int year, int month)
    {
        LocalDate startOfMonth = LocalDate.of(year, month,1);
        int startDayOfMonth = startOfMonth.getDayOfWeek().getValue();

        String[] months ={
                "",										// First index not used
                "January", "February", "March",
                "April", "May", "June",
                "July", "August", "September",
                "October", "November", "December"
        };

        int[] days = {									// First index not used
                0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31
        };

        	// Sets February to 29 days if year is a leap year
            if  ((((year % 4 == 0) && (year % 100 != 0)) ||  (year % 400 == 0)) && month == 2)
                days[month] = 29;

            System.out.println("          "+ months[month] + " " + year);

            System.out.println("...........................................");
            System.out.println("   Sun   Mon   Tue   Wed   Thu   Fri   Sat");

            for (int i = 0; i < startDayOfMonth; i++)
                System.out.print("      ");
            String d;
            
            // Loop the number of days in the given month
            for (int i = 1; i <= days[month]; i++)
            {
                d = ""+i;
                if (isToday(year, month, i))			// If today, surround number with []
                    d= "["+d+"]";
                if ( hasEvents(year, month, i))
                    d= "{"+d+"}";
                System.out.print(format(d));
                if (((i + startDayOfMonth) % 7 == 0) || (i == days[month])) System.out.println();
            }
            System.out.println();
    }
    
    /**
     * Method Event setter
     * @param events 
     */
    public void setEvents(ArrayList<Event> events) { this.events = events; }

    /**
     * Method that prints all events in order of startdate
     */
    public void printEvents() { for(Event e: events) { System.out.println(e); } }

    /**
     * Method that checks if a day has events registered to it
     * @param year		int for year of event
     * @param month		int for month of event
     * @param day 		int for day of event
     * @return false		if event is not found
     */
    private boolean hasEvents(int year, int month, int day)
    {
        for (Event event: events)
        {
            if(event.getTimeInterval().dateApplies(LocalDate.of(year, month, day)))
                return true;
        }
        return false;
    }

    /**
     * Method that checks if the date is today
     * @param year 		int for year of event
     * @param month 	int for month of event
     * @param day 		int for day of event
     * @return boolean	true is given date equals today
     */
    public boolean isToday(int year, int month, int day)
    {
        LocalDate date = LocalDate.now();
        return date.equals(LocalDate.of(year, month, day));
    }

    /**
     * Method that formats string to a char of length 6 by appending spaces on both sides
     * @param 	s			String to append
     * @return	String		Formated string
     */
    private String format(String s)
    {
        int l = s.length(), aim = 6, i;
        i = l;
        for ( ; i < (aim-l)/2; i++)
            s += " ";
        for ( ; i < aim; i++)
            s = " "+s;
        return s;
    }

    /**
     * Method that draws a month calendar with its events.
     */
    public void printMonth()
    {
    	// navigateDate holds month value, if null today date month is printed
        if(navigateDate == null)
            navigateDate = LocalDate.now();
        draw(navigateDate.getYear(), navigateDate.getMonthValue());
    }

    /**
     * Method that sets the navigation date to specified one
     * @param date	LocalDate specified to set
     */
    public void navigate(LocalDate date)
    {
    	// If date is null, navigateDate is set to todays date
        if(date == null)
            navigateDate = LocalDate.now();
        else navigateDate = date;
    }

    /**
     * Method that substracts a day from navigation date
     */
    public void aDayBack() { navigateDate = navigateDate.minusDays(1); }

    /**
     * Method that adds a day to navigation date
     */
    public void aDayAhead() { navigateDate = navigateDate.plusDays(1); }

    /**
     * Method that substracts a month from navigation date
     */
    public void aMonthBack() { navigateDate = navigateDate.minusMonths(1); }

    /**
     * Method that adds a month to navigation date
     */
    public void aMonthAhead() { navigateDate = navigateDate.plusMonths(1); }

    /**
     * Method that prints date held in navigation date and the events on that date
     */
    public void printDay()
    {
        if (navigateDate == null)
            navigateDate = LocalDate.now();
        System.out.println("--------"+navigateDate+"---------");
        int l =  0;
        System.out.println("....Events...");
        
        for (Event event:events) 
        {
            if(event.getTimeInterval().dateApplies(navigateDate))
            {
                System.out.println((++l)+"."+event);
            }
        }
        
        if(l == 0)
            System.out.println("There are no events today");

    }

    /**
     * Method that adds events to array of events
     * @param event 	Event to be added to array
     * @return boolean 	true if event was successfully added to array
     */
    public boolean addEvent(Event event)
    {
        for(Event e: events)
            if(e.getTimeInterval().overlaps(event.getTimeInterval()))
                return false;
        int s = events.size(), i ;
        i = s-1;
        for ( ; i >= 0; i--)
        {
            if (events.get(i).getTimeInterval().getStartDate().compareTo(event.getTimeInterval().getStartDate()) < 0)
            {
                events.add(i + 1, event);
                return true;
            }
        }
        
        if(s > 0)
        {
            events.add(0, event);
            return true;
        }
        events.add(event);
        return true;
    }

    /**
     * Method that deletes all one day occurring events 
     * @param date		Specified date to delete
     * @return int 		number of events deleted
     */
    public int deleteAllSinglesInDate(LocalDate date)
    {
        int deleted = 0;
        for(int i = 0; i < events.size(); i++) 
        {
            if(!events.get(i).getTimeInterval().isRecurring() 
            		&& events.get(i).getTimeInterval().getStartDate().equals(date)) 
            {
                events.remove(i);
                i--;
                deleted++;
            }
        }
        return deleted;
    }

    /**
     * Method that deletes a one day event 
     * deletes a one day event of the specified name and date
     * @param name		name of event
     * @param date		date of event
     * @return int		1 if event was deleted, 0 if it was not
     */
    public int deleteSingle(String name, LocalDate date)
    {
        for(int i = 0; i < events.size(); i++)
        {
            if(!events.get(i).getTimeInterval().isRecurring()
                    && events.get(i).getTimeInterval().getStartDate().equals(date)
                    && events.get(i).getName().equalsIgnoreCase(name))
            {
                events.remove(i);
                i--;
                return 1;
            }
        }
        return 0;
    }

    /**
     * Method that deletes recurring event
     * @param name	 	name of event to delete
     * @return int 		1 if event was deleted, 0 if it was not
     */
    public int deleteRecurring(String name)
    {
        for(int i = 0; i < events.size(); i++) 
        {
            if(!events.get(i).getTimeInterval().isRecurring()
                    && events.get(i).getName().equalsIgnoreCase(name))
            {
                events.remove(i);
                return 1;
            }
        }
        return 0;
    }

    /**
     * Method that prints all events in system separated as One Time Events and Recurring Events
     */
    public void printEventList()
    {
        System.out.println("ONE TIME EVENTS");
        int i = 0;
        for (Event event: events)
        {
            if(!event.getTimeInterval().isRecurring())
                System.out.println((++i)+". "+event);
        }
        i = 0;
        System.out.println("\nRECURRING EVENTS");
        for (Event event: events)
        {
            if(event.getTimeInterval().isRecurring())
                System.out.println((++i)+". "+event);
        }
    }

    /**
     * Method that formats string to be stored in text file
     * @return String	string that would be stored in a text file for all events
     */
    public String storageString() 
    {
        String string = "";
        int i = 0;
        for(Event event: events) 
        {
            if(i++ > 0)
                string +="\n";
            string += event.storeString();
        }
        return string;
    }
    
}
