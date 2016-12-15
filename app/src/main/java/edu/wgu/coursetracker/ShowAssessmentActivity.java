package edu.wgu.coursetracker;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ShowAssessmentActivity extends AppCompatActivity
{
    private static final int EDIT_REQUEST_CODE = 1001;
    private Assessment assessment;
    private TextView title;
    private TextView type;
    private TextView dueDate;
    private String assessmentFilter;
    private String titleString;
    private String typeString;
    private String dueDateString;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_assessment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = (TextView) findViewById(R.id.assessmentTitleValue);
        type = (TextView) findViewById(R.id.assessmentTypeValue);
        dueDate = (TextView) findViewById(R.id.assessmentDueDateValue);

        Intent intent = getIntent();
        uri = intent.getParcelableExtra(CourseProvider.ASSESSMENT_CONTENT_ITEM_TYPE);
        assessmentFilter = DBManager.ASSESSMENT_ID + "=" + uri.getLastPathSegment();
        setTextViews(uri);
    }

    private void setTextViews(Uri uri)
    {
        Cursor cursor = getContentResolver().query(uri,
                DBManager.ALL_ASSESSMENT_COLUMNS, assessmentFilter, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            titleString = cursor.getString(cursor.getColumnIndex(DBManager.ASSESSMENT_TITLE));
            typeString = cursor.getString(cursor.getColumnIndex(DBManager.ASSESSMENT_TYPE));
            dueDateString = cursor.getString(cursor.getColumnIndex(DBManager.ASSESSMENT_DUE_DATE));
            assessment = new Assessment(titleString, typeString, dueDateString);
            assessment.setId(cursor.getInt(cursor.getColumnIndex(DBManager.ASSESSMENT_ID)));
        }

        setTitle(titleString);
        title.setText(titleString);
        type.setText(typeString);
        dueDate.setText(dueDateString);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_show_assessment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_edit_assessment:
                Intent editAssessmentIntent = new Intent(this, EditAssessmentActivity.class);
                Uri uri = Uri.parse(CourseProvider.ASSESSMENT_CONTENT_URI + "/" + assessment.getId());
                editAssessmentIntent.putExtra(CourseProvider.ASSESSMENT_CONTENT_ITEM_TYPE, uri);
                startActivityForResult(editAssessmentIntent, EDIT_REQUEST_CODE);
                break;
            case R.id.action_delete_assessment:
                deleteAssessment();
                break;
            case R.id.action_add_photos:
                Intent photoIntent = new Intent(this, PhotosActivity.class);
                photoIntent.putExtra(CourseProvider.ASSESSMENT_CONTENT_ITEM_TYPE, assessment.getId());
                startActivity(photoIntent);
        }
        return true;
    }

    private void deleteAssessment()
    {
        getContentResolver().delete(CourseProvider.ASSESSMENT_CONTENT_URI,
                assessmentFilter, null);
        Toast.makeText(ShowAssessmentActivity.this, "Assessment deleted.", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch(requestCode) {
            case EDIT_REQUEST_CODE:
                Intent intent = getIntent();
                Uri uri = intent.getParcelableExtra(CourseProvider.ASSESSMENT_CONTENT_ITEM_TYPE);
                setTextViews(uri);
        }
    }
}
