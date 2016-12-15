package edu.wgu.coursetracker;


/**
 * Created by Rickey on 5/5/2016.
 */
public class Term
{
    private long id;
    private String title;
    private String startDate;
    private String endDate;

    public Term(){}

    public Term(String title, String startDate, String endDate)
    {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getStartDate()
    {
        return startDate;
    }

    public void setStartDate(String date)
    {
        startDate = date;
    }

    public String getEndDate()
    {
        return endDate;
    }

    public void setEndDate(String date)
    {
        endDate = date;
    }
}
