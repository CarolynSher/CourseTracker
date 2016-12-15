package edu.wgu.coursetracker;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditCourseActivity extends AppCompatActivity
    implements View.OnFocusChangeListener
{
    private EditText courseTitle;
    private EditText courseStartDate;
    private EditText courseEndDate;
    private Spinner statusSpinner;
    private EditText courseMentorName;
    private EditText courseMentorPhone;
    private EditText courseMentorEmail;
    private String courseFilter;
    private String courseTitleString;
    private String courseStartDateString;
    private String courseEndDateString;
    private String statusString;
    private String courseMentorNameString;
    private String courseMentorPhoneString;
    private String courseMentorEmailString;
    SimpleDateFormat dateFormat;
    DatePickerDialog startDatePickerDialog;
    DatePickerDialog endDatePickerDialog;
    Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        courseTitle = (EditText) findViewById(R.id.courseTitleInput);
        courseStartDate = (EditText) findViewById(R.id.courseStartDateInput);
        courseEndDate = (EditText) findViewById(R.id.courseEndDateInput);
        statusSpinner = (Spinner) findViewById(R.id.statusSpinner);
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(this,
                R.array.status_array, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusAdapter);
        courseMentorName = (EditText) findViewById(R.id.courseMentorNameInput);
        courseMentorPhone = (EditText) findViewById(R.id.courseMentorPhoneInput);
        courseMentorEmail = (EditText) findViewById(R.id.courseMentorEmailInput);
        cancelButton = (Button) findViewById(R.id.CancelButton);

        cancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        setDateFields();

        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra(CourseProvider.COURSE_CONTENT_ITEM_TYPE);
        courseFilter = DBManager.COURSE_ID + "=" + uri.getLastPathSegment();
        Cursor cursor = getContentResolver().query(uri,
                DBManager.ALL_COURSE_COLUMNS, courseFilter, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            courseTitleString = cursor.getString(cursor.getColumnIndex(DBManager.COURSE_TITLE));
            courseStartDateString = cursor.getString(cursor.getColumnIndex(DBManager.COURSE_START_DATE));
            courseEndDateString = cursor.getString(cursor.getColumnIndex(DBManager.COURSE_END_DATE));
            courseMentorNameString = cursor.getString(cursor.getColumnIndex(DBManager.COURSE_MENTOR_NAME));
            courseMentorPhoneString = cursor.getString(cursor.getColumnIndex(DBManager.COURSE_MENTOR_PHONE));
            courseMentorEmailString = cursor.getString(cursor.getColumnIndex(DBManager.COURSE_MENTOR_EMAIL));
        }

        courseTitle.setText(courseTitleString);
        courseStartDate.setText(courseStartDateString);
        courseEndDate.setText(courseEndDateString);
        courseMentorName.setText(courseMentorNameString);
        courseMentorPhone.setText(courseMentorPhoneString);
        courseMentorEmail.setText(courseMentorEmailString);

    }

    private void setDateFields()
    {
        courseStartDate.setOnFocusChangeListener(this);
        courseEndDate.setOnFocusChangeListener(this);

        Calendar calendar = Calendar.getInstance();
        startDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                courseStartDate.setText(dateFormat.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        endDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                courseEndDate.setText(dateFormat.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus)
    {
        if (hasFocus) {
            if (v == courseStartDate)
                startDatePickerDialog.show();
            else if (v == courseEndDate)
                endDatePickerDialog.show();
        }
        v.clearFocus();
    }

    public void updateCourse(View view)
    {
        courseTitleString = courseTitle.getText().toString();
        courseStartDateString = courseStartDate.getText().toString();
        courseEndDateString = courseEndDate.getText().toString();
        statusString = statusSpinner.getSelectedItem().toString();
        courseMentorNameString = courseMentorName.getText().toString();
        courseMentorPhoneString = courseMentorPhone.getText().toString();
        courseMentorEmailString = courseMentorEmail.getText().toString();

        ContentValues values = new ContentValues();
        values.put(DBManager.COURSE_TITLE, courseTitleString);
        values.put(DBManager.COURSE_START_DATE, courseStartDateString);
        values.put(DBManager.COURSE_END_DATE, courseEndDateString);
        values.put(DBManager.COURSE_STATUS, statusString);
        values.put(DBManager.COURSE_MENTOR_NAME, courseMentorNameString);
        values.put(DBManager.COURSE_MENTOR_PHONE, courseMentorPhoneString);
        values.put(DBManager.COURSE_MENTOR_EMAIL, courseMentorEmailString);

        getContentResolver().update(CourseProvider.COURSE_CONTENT_URI, values, courseFilter, null);
        setResult(RESULT_OK);
        finish();
    }
}
