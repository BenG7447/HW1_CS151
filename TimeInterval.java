import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Class that holds date and time of an event. For recurring events, a start date and an end date are 
 * given. Days of the event in a week are held in the days Arraylist. Day 1 is Monday while Day 7 is 
 * Sunday. One time events do not use enddate and days Arraylist.
 */
public class TimeInterval
{
    private boolean recurring  = false;
    private LocalDate startDate, endDate;
    private LocalTime startTime, endTime;
    private ArrayList<Integer> days = new ArrayList();

    /**
     * Method EndDate Setter
     * @param endDate 		LocalDate end date of event
     */
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    
    /**
     * Method EndTime Setter
     * @param endTime 		LocalTime end time of event
     */
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    
    /**
     * Method Recurring Event Setter
     * @param recurring		boolean of recurring event
     */
    public void setRecurring(boolean recurring) { this.recurring = recurring; }
    
    /**
     * Method StartDate Setter
     * @param startDate 	LocalDate start date of event
     */
    public void setStartDate(LocalDate startDate){ this.startDate = startDate; }
    
    /**
     * Method StartTime Setter
     * @param startTime 	LocalTime start time of event
     */
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    
    /**
     * Method EndDate Getter
     * @return LocalDate	LocalDate end date of event
     */
    public LocalDate getEndDate() { return endDate; }
    
    /**
     * Method StartDate Getter
     * @return LocalDate 	LocalDate start date of event
     */
    public LocalDate getStartDate() { return startDate; }
    
    /**
     * Method StartTime Getter
     * @return LocalTime 	LocalTime start time of event
     */
    public LocalTime getStartTime() { return startTime; }
    
    /**
     * Method EndTime Getter
     * @return LocalTime 	LocalTime end time of event
     */
    public LocalTime getEndTime() { return endTime; }
    
    /**
     * Method Recurring Event Getter
     * @return boolean 		if event is recurring
     */
    public boolean isRecurring() { return recurring; }
    
    /**
     * Method that receives MTWRFAS chars, where M is Monday and S is Sunday. The days are
     * stored in system respectively by number where 1 is Monday and 7 is Sunday
     * @param c		char representing a day of the week
     */
    public void addDay(char c) 
    {
        switch(c)
        {
            case 'S':
                days.add(7);
                break;
            case 'M':
                days.add(1);
                break;
            case 'T':
                days.add(2);
                break;
            case 'W':
                days.add(3);
                break;
            case 'R':
                days.add(4);
                break;
            case 'F':
                days.add(5);
                break;
            case 'A':
                days.add(6);
                break;
        }
    }

    /**
     * @Override toString
     * @return String	TimeInterval toString
     */
    public String toString() 
    {
        return "TimeInterval{" +
                "recurring=" + recurring +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", days_per_week=" + days.size() +
                '}';
    }
    
    /**
     * Method Days Getter
     * @return ArrayList<Integer> 		days
     */
    public ArrayList<Integer> getDays() { return days; }
        
    /**
     * Method that checks if TimeInterval instance falls within date specified
     * @param td				LocalDate to check validity
     * @return boolean		true if valid
     */
    public boolean dateApplies(LocalDate td)
    {
        if(isRecurring())
        {
            LocalDate date;
            ArrayList<Integer> days = getDays();
            for (Integer i: days)
            {
                date = getStartDate();
                if(date.getDayOfWeek().getValue() < i)
                    date = date.plusDays(i - date.getDayOfWeek().getValue());
                else if (date.getDayOfWeek().getValue() > i)
                    date = date.plusDays((7 -date.getDayOfWeek().getValue())+i);

                if(td.getDayOfWeek().getValue() != date.getDayOfWeek().getValue()) { continue; }
                
                if (td.compareTo(getEndDate()) <= 0 && td.compareTo(getStartDate()) >= 0)
                {
                    return true;
                }
            }
        } else {
            if(getStartDate().equals(td))
                return true;
        }
        return false;
    }

    /**
     * Method that checks if TimeInterval is valid
     * @param td 		TimeInterval 
     * @return boolean 	true if valid
     */
    private boolean timeWraps(TimeInterval td)
    {
        if (getStartTime().equals(td.getStartTime()) || getEndTime().equals(td.getEndTime()))
            return true;
        if (getStartTime().compareTo(td.getStartTime())>0 && getStartTime().compareTo(td.getEndTime())<0)
            return true;
        if (getEndTime().compareTo(td.getStartTime()) > 0 && getEndTime().compareTo(td.getEndTime()) < 0)
            return true;
        return false;
    }
    
    /**
     * Method that compares two dates
     * It compares this instance with TimeInterval parameter
     * @param td 		TimeInterval
     * @return boolean 	true if equal
     */
    private boolean dateWraps(TimeInterval td)
    {
    	// If both intervals are not recurring, return true if they have same startdate
        if( (!td.isRecurring() && !isRecurring()) )
        {
            return getStartDate().equals(td.getStartDate());
        }

        // If only one interval is recurring
        if(td.isRecurring() ^ isRecurring())
        {
            if(isRecurring())
            {
                if(startDate.compareTo(td.getStartDate()) < 0 || startDate.compareTo(td.getEndDate())> 0)
                    return false;
                return true;
            }
            
            if(startDate.compareTo(td.getStartDate()) > 0 || td.getStartDate().compareTo(endDate) > 0)
                return false;
            return true;
        }

        if (getStartDate().equals(td.getStartDate()) || getEndDate().equals(td.getEndDate()))
            return true;
        
        if (getStartDate().compareTo(td.getStartDate())>0 && getStartDate().compareTo(td.getEndDate())<0)
            return true;
        
        if (getEndDate().compareTo(td.getStartDate()) > 0 && getEndDate().compareTo(td.getEndDate()) < 0)
            return true;
        
        return false;
    }
    
    /**
     * Method that compares two TimeIntervals. Compares this instance and parameter
     * @param td 		TimeIntervals
     * @return boolean 	true if equal
     */
    public boolean overlaps(TimeInterval td)
    {
        if((!td.isRecurring() && !isRecurring()))
        {
            if(!dateWraps(td))
                return false;
            return  timeWraps(td);
         }

        if(!timeWraps(td))
            return false;

        if(!dateWraps(td))
            return false;

        ArrayList<Integer> cDays = td.getDays(), o = new ArrayList<>();
        for (Integer i: cDays)
        {
            if(days.contains(i))
                return true;
        }
        return false;
    }
    
}
