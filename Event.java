import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Class that holds event.
 * Each event has a name and a TimeInterval
 */
public class Event 
{
    private String name;
    private TimeInterval timeInterval; 	 // Holds time and date of events plus boolean of recurring events

    /**
     * Method Name Setter
     * @param name		name of event
     */
    public void setName(String name) { this.name = name; }
    
    /**
     * Method TimeInterval Setter
     * @param timeInterval 	TimeInterval of event
     */
    public void setTimeInterval(TimeInterval timeInterval) { this.timeInterval = timeInterval; }
    
    /**
     * Method Name Getter
     * @return String 	name of event
     */
    public String getName() { return name; }
    
    /**
     * Method TimeInterval Getter
     * @return TimeInterval 	TimeInterval of event
     */
    public TimeInterval getTimeInterval() { return timeInterval; }
    
    /**
     * @Override toString
     * @return String	Event toString
     */
    public String toString() 
    {
        return "Event{" +
                "name='" + name + '\'' +
                ", timeInterval=" + timeInterval +
                '}';
    }

    /**
     * Method that generates a string of event for storing in text file
     * @return String	string to be stored in text file
     */
    public String storeString() 
    {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        String string = name+"\n";
        TimeInterval interval = timeInterval;
        
        if (interval.isRecurring())
        {
            ArrayList<Integer> days = interval.getDays();
            for(Integer day: days)
            {
                char c = 'c';
                switch (day)
                {
                    case 1:
                        c = 'M';
                        break;
                    case 2:
                        c = 'T';
                        break;
                    case 3:
                        c = 'W';
                        break;
                    case 4:
                        c = 'R';
                        break;
                    case 5:
                        c = 'F';
                        break;
                    case 6:
                        c = 'A';
                        break;
                    case 7:
                        c = 'S';
                        break;
                }
                string += ""+c;
            }
            string += " ";
            string += interval.getStartTime().getHour()+":"+interval.getStartTime().getMinute()+" ";
            string += interval.getEndTime().getHour()+":"+interval.getEndTime().getMinute()+" ";
            string += dateFormatter.format(interval.getStartDate())+" ";
            string += dateFormatter.format(interval.getEndDate())+" ";
        } else {
            string += dateFormatter.format(interval.getStartDate())+" ";
            string += interval.getStartTime().getHour()+":"+interval.getStartTime().getMinute()+" ";
            string += interval.getEndTime().getHour()+":"+interval.getEndTime().getMinute()+" ";
        }
        System.out.println(string);
        return string;
    }
    
}
