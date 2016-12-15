package edu.wgu.coursetracker;

/**
 * Created by Rickey on 5/24/2016.
 */
public class Assessment
{
    private long id;
    private String title;
    private String assessmentType;
    private String dueDate;

    public Assessment()
    {
    }

    public Assessment(String title,
                      String assessmentType, String dueDate)
    {
        this.title = title;
        this.assessmentType = assessmentType;
        this.dueDate = dueDate;
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

    public String getAssessmentType()
    {
        return assessmentType;
    }

    public void setAssessmentType(String assessmentType)
    {
        this.assessmentType = assessmentType;
    }

    public String getDueDate()
    {
        return dueDate;
    }

    public void setDueDate(String dueDate)
    {
        this.dueDate = dueDate;
    }
}
