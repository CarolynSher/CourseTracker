package edu.wgu.coursetracker;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class AddTermActivity extends AppCompatActivity implements View.OnFocusChangeListener
{
    private EditText title_input;
    private EditText start_date_input;
    private EditText end_date_input;
    SimpleDateFormat dateFormatter;

    DatePickerDialog startDatePickerDialog;
    DatePickerDialog endDatePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title_input = (EditText) findViewById(R.id.title_input);
        start_date_input = (EditText) findViewById(R.id.start_date_input);
        end_date_input = (EditText) findViewById(R.id.end_date_input);

        dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        setDateFields();
    }

    public void addTerm(View view)
    {
        String termTitle = title_input.getText().toString();
        String startDate = start_date_input.getText().toString();
        String endDate = end_date_input.getText().toString();

        Term term = new Term(termTitle, startDate, endDate);

        ContentValues values = new ContentValues();
        values.put(DBManager.TERM_TITLE, term.getTitle());
        values.put(DBManager.TERM_START_DATE, term.getStartDate());
        values.put(DBManager.TERM_END_DATE, term.getEndDate());

        getContentResolver().insert(CourseProvider.TERM_CONTENT_URI,
                values);

        Toast.makeText(AddTermActivity.this, term.getTitle() + " added successfully.", Toast.LENGTH_LONG).show();

        finish();
    }

    public void finishActivity(View view)
    {
        finish();
    }

    private void setDateFields()
    {
        start_date_input.setOnFocusChangeListener(this);
        end_date_input.setOnFocusChangeListener(this);

        Calendar calendar = Calendar.getInstance();
        startDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                start_date_input.setText(dateFormatter.format(newDate.getTime()));
            }
    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.DAY_OF_MONTH);
        endDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                end_date_input.setText(dateFormatter.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus)
    {
        if(hasFocus) {
            if (v == start_date_input)
                startDatePickerDialog.show();
            else if (v == end_date_input)
                endDatePickerDialog.show();
        }
        v.clearFocus();
    }
}
