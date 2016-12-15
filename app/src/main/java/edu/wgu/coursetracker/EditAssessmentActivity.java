package edu.wgu.coursetracker;

import android.app.DatePickerDialog;
import android.app.PendingIntent;
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

public class EditAssessmentActivity extends AppCompatActivity
    implements View.OnFocusChangeListener
{
    private EditText title;
    private Spinner type;
    private EditText dueDate;
    private String assessmentFilter;
    private String titleString;
    private String typeString;
    private String dueDateString;
    Button cancelButton;
    SimpleDateFormat dateFormat;
    DatePickerDialog dueDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_assessment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = (EditText) findViewById(R.id.assessmentTitleInput);
        type = (Spinner) findViewById(R.id.assessmentTypeSpinner);
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,
                 R.array.assessment_type_array, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(typeAdapter);
        dueDate = (EditText) findViewById(R.id.assessmentDueDateInput);
        cancelButton = (Button) findViewById(R.id.assessmentCancelButton);
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

        dueDate.setOnFocusChangeListener(this);
        Calendar calendar = Calendar.getInstance();
        dueDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dueDate.setText(dateFormat.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra(CourseProvider.ASSESSMENT_CONTENT_ITEM_TYPE);
        assessmentFilter = DBManager.ASSESSMENT_ID + "=" + uri.getLastPathSegment();
        Cursor cursor = getContentResolver().query(uri,
                DBManager.ALL_ASSESSMENT_COLUMNS, assessmentFilter, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            titleString = cursor.getString(cursor.getColumnIndex(DBManager.ASSESSMENT_TITLE));
            typeString = cursor.getString(cursor.getColumnIndex(DBManager.ASSESSMENT_TYPE));
            dueDateString = cursor.getString(cursor.getColumnIndex(DBManager.ASSESSMENT_DUE_DATE));
        }

        title.setText(titleString);
        dueDate.setText(dueDateString);
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
        if(hasFocus) {
            if (v == dueDate)
                dueDatePicker.show();
        }
    }

    public void updateAssessment(View view)
    {
        String newTitle = title.getText().toString();
        String newType = type.getSelectedItem().toString();
        String newDueDate = dueDate.getText().toString();

        ContentValues values = new ContentValues();
        values.put(DBManager.ASSESSMENT_TITLE, newTitle);
        values.put(DBManager.ASSESSMENT_TYPE, newType);
        values.put(DBManager.ASSESSMENT_DUE_DATE, newDueDate);
        getContentResolver().update(CourseProvider.ASSESSMENT_CONTENT_URI, values,
                assessmentFilter, null);
        setResult(RESULT_OK);
        finish();
    }
}
