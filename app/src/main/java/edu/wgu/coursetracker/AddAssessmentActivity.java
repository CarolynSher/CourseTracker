package edu.wgu.coursetracker;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

public class AddAssessmentActivity extends AppCompatActivity
    implements View.OnFocusChangeListener
{
    private EditText assessmentTitle;
    private Spinner assessmentType;
    private EditText assessmentDueDate;

    SimpleDateFormat simpleDateFormat;
    DatePickerDialog dueDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assessment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        assessmentTitle = (EditText) findViewById(R.id.assessmentTitleInput);
        assessmentType = (Spinner) findViewById(R.id.assessmentTypeSpinner);
        assessmentDueDate = (EditText) findViewById(R.id.assessmentDueDateInput);

        ArrayAdapter<CharSequence> assessmentTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.assessment_type_array, android.R.layout.simple_spinner_item);
        assessmentTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        assessmentType.setAdapter(assessmentTypeAdapter);

        simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

        setDueDateField();
    }

    public void addAssessment(View view)
    {
        String assessmentTitleString = assessmentTitle.getText().toString();
        String assessmentTypeString = assessmentType.getSelectedItem().toString();
        String assessmentDueDateString = assessmentDueDate.getText().toString();

        Assessment assessment = new Assessment(assessmentTitleString, assessmentTypeString,
                assessmentDueDateString);

        Intent intent = getIntent();
        long id = intent.getLongExtra(CourseProvider.COURSE_CONTENT_ITEM_TYPE, 0);

        ContentValues values = new ContentValues();
        values.put(DBManager.ASSESSMENT_TITLE, assessment.getTitle());
        values.put(DBManager.ASSESSMENT_TYPE, assessment.getAssessmentType());
        values.put(DBManager.ASSESSMENT_DUE_DATE, assessment.getDueDate());
        values.put(DBManager.ASSESSMENT_ASSIGNED_COURSE, id);

        getContentResolver().insert(CourseProvider.ASSESSMENT_CONTENT_URI, values);

        finish();
    }

    public void setDueDateField()
    {
        assessmentDueDate.setOnFocusChangeListener(this);

        Calendar calendar = Calendar.getInstance();
        dueDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                assessmentDueDate.setText(simpleDateFormat.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus)
    {
        if (hasFocus)
        {
            if (v == assessmentDueDate)
            {
                dueDatePicker.show();
            }
        }
        v.clearFocus();
    }
}
