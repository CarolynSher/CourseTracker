package edu.wgu.coursetracker;

/**
 * Created by Rickey on 5/24/2016.
 */
public class Course
{
    private long id;
    private String title;
    private String startDate;
    private String endDate;
    private String status;
    private String courseMentorName;
    private String courseMentorPhone;
    private String courseMentorEmail;

    private Term termAssigned;

    public Course()
    {
    }

    public Course(String title, String startDate, String endDate, String status,
                  String courseMentorName, String courseMentorPhone, String courseMentorEmail)
    {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.courseMentorName = courseMentorName;
        this.courseMentorEmail = courseMentorEmail;
        this.courseMentorPhone = courseMentorPhone;
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

    public void setStartDate(String startDate)
    {
        this.startDate = startDate;
    }

    public String getEndDate()
    {
        return endDate;
    }

    public void setEndDate(String endDate)
    {
        this.endDate = endDate;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getCourseMentorName()
    {
        return courseMentorName;
    }

    public void setCourseMentorName(String courseMentorName)
    {
        this.courseMentorName = courseMentorName;
    }

    public String getCourseMentorPhone()
    {
        return courseMentorPhone;
    }

    public void setCourseMentorPhone(String courseMentorPhone)
    {
        this.courseMentorPhone = courseMentorPhone;
    }

    public String getCourseMentorEmail()
    {
        return courseMentorEmail;
    }

    public void setCourseMentorEmail(String courseMentorEmail)
    {
        this.courseMentorEmail = courseMentorEmail;
    }
}
