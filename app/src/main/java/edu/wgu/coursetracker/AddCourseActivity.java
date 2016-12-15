package edu.wgu.coursetracker;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddCourseActivity extends AppCompatActivity
    implements View.OnFocusChangeListener
{
    private EditText courseTitle;
    private EditText startDate;
    private EditText endDate;
    private Spinner statusSpinner;
    private EditText courseMentorName;
    private EditText courseMentorPhone;
    private EditText courseMentorEmail;
    SimpleDateFormat dateFormat;

    DatePickerDialog startDatePickerDialog;
    DatePickerDialog endDatePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        courseTitle = (EditText) findViewById(R.id.courseTitleInput);
        startDate = (EditText) findViewById(R.id.courseStartDateInput);
        endDate = (EditText) findViewById(R.id.courseEndDateInput);
        statusSpinner = (Spinner) findViewById(R.id.statusSpinner);
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(this,
                R.array.status_array, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusAdapter);

        courseMentorName = (EditText) findViewById(R.id.courseMentorNameInput);
        courseMentorPhone = (EditText) findViewById(R.id.courseMentorPhoneInput);
        courseMentorEmail = (EditText) findViewById(R.id.courseMentorEmailInput);

        dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

        setDateFields();
    }

    public void addCourse(View view)
    {
        String courseTitleString = courseTitle.getText().toString();
        String startDateString = startDate.getText().toString();
        String endDateString = endDate.getText().toString();
        String statusString = statusSpinner.getSelectedItem().toString();
        String courseMentorNameString = courseMentorName.getText().toString();
        String courseMentorPhoneString = courseMentorPhone.getText().toString();
        String courseMentorEmailString = courseMentorEmail.getText().toString();

        Course course = new Course(courseTitleString, startDateString, endDateString,
                statusString, courseMentorNameString, courseMentorPhoneString,
                courseMentorEmailString);

        Intent intent = getIntent();
        long id = intent.getLongExtra(CourseProvider.TERM_CONTENT_ITEM_TYPE, 0);

        ContentValues values = new ContentValues();
        values.put(DBManager.COURSE_TITLE, course.getTitle());
        values.put(DBManager.COURSE_START_DATE, course.getStartDate());
        values.put(DBManager.COURSE_END_DATE, course.getEndDate());
        values.put(DBManager.COURSE_STATUS, course.getStatus());
        values.put(DBManager.COURSE_MENTOR_NAME, course.getCourseMentorName());
        values.put(DBManager.COURSE_MENTOR_PHONE, course.getCourseMentorPhone());
        values.put(DBManager.COURSE_MENTOR_EMAIL, course.getCourseMentorEmail());
        values.put(DBManager.COURSE_ASSIGNED_TERM, id);

        getContentResolver().insert(CourseProvider.COURSE_CONTENT_URI, values);

        finish();

    }

    public void finishActivity(View view)
    {
        finish();
    }

    public void setDateFields()
    {
        startDate.setOnFocusChangeListener(this);
        endDate.setOnFocusChangeListener(this);

        Calendar calendar = Calendar.getInstance();
        startDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                startDate.setText(dateFormat.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        endDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                endDate.setText(dateFormat.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus)
    {
        if (hasFocus) {
            if (v == startDate)
                startDatePickerDialog.show();
            else if (v == endDate)
                endDatePickerDialog.show();
        }
        v.clearFocus();
    }
}
