package edu.wgu.coursetracker;

import android.app.AlarmManager;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class ShowCourseActivity extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<Cursor>
{
    private static final int EDIT_REQUEST_CODE = 1001;
    private Course course;
    private TextView title;
    private TextView startDate;
    private TextView endDate;
    private TextView status;
    private TextView courseMentorName;
    private TextView courseMentorPhone;
    private TextView courseMentorEmail;
    private String courseFilter;
    private String titleString;
    private String startDateString;
    private String endDateString;
    private String statusString;
    private String courseMentorNameString;
    private String courseMentorPhoneString;
    private String courseMentorEmailString;

    private CursorAdapter assessmentAdapter;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_course);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = (TextView) findViewById(R.id.Course);
        startDate = (TextView) findViewById(R.id.startDateValue);
        endDate = (TextView) findViewById(R.id.endDateValue);
        status = (TextView) findViewById(R.id.statusValue);
        courseMentorName = (TextView) findViewById(R.id.courseMentorNameValue);
        courseMentorEmail = (TextView) findViewById(R.id.courseMentorEmailValue);
        courseMentorPhone = (TextView) findViewById(R.id.courseMentorPhoneValue);

        Intent intent = getIntent();
        uri = intent.getParcelableExtra(CourseProvider.COURSE_CONTENT_ITEM_TYPE);

        courseFilter = DBManager.COURSE_ID + "=" + uri.getLastPathSegment();

        setTextViews(uri);

        String[] from = { DBManager.ASSESSMENT_TITLE, DBManager.ASSESSMENT_TYPE };
        int[] to = { android.R.id.text1, android.R.id.text2 };

        assessmentAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1, null, from, to, 0);
        ListView assessmentList = (ListView) findViewById(android.R.id.list);
        assessmentList.setAdapter(assessmentAdapter);

        assessmentList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(ShowCourseActivity.this, ShowAssessmentActivity.class);
                Uri assessmentUri = Uri.parse(CourseProvider.ASSESSMENT_CONTENT_URI + "/" + id);
                intent.putExtra(CourseProvider.ASSESSMENT_CONTENT_ITEM_TYPE, assessmentUri);
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(2, null, ShowCourseActivity.this);
    }

    private void setTextViews(Uri uri)
    {
        Cursor cursor = getContentResolver().query(uri,
                DBManager.ALL_COURSE_COLUMNS, courseFilter, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            titleString = cursor.getString(cursor.getColumnIndex(DBManager.COURSE_TITLE));
            startDateString = cursor.getString(cursor.getColumnIndex(DBManager.COURSE_START_DATE));
            endDateString = cursor.getString(cursor.getColumnIndex(DBManager.COURSE_END_DATE));
            statusString = cursor.getString(cursor.getColumnIndex(DBManager.COURSE_STATUS));
            courseMentorNameString = cursor.getString(
                    cursor.getColumnIndex(DBManager.COURSE_MENTOR_NAME));
            courseMentorPhoneString = cursor.getString(
                    cursor.getColumnIndex(DBManager.COURSE_MENTOR_PHONE));
            courseMentorEmailString = cursor.getString(
                    cursor.getColumnIndex(DBManager.COURSE_MENTOR_EMAIL));
            course = new Course(titleString, startDateString, endDateString, statusString,
                    courseMentorNameString, courseMentorPhoneString, courseMentorEmailString);
            course.setId(cursor.getInt(cursor.getColumnIndex(DBManager.COURSE_ID)));
        } else {
            titleString = "No data";
            startDateString = "No data";
            endDateString = "No data";
            statusString = "No data";
            courseMentorNameString = "No data";
            courseMentorPhoneString = "No data";
            courseMentorEmailString = "No data";
        }

        setTitle(titleString);
        title.setText(titleString);
        startDate.setText(startDateString);
        endDate.setText(endDateString);
        status.setText(statusString);
        courseMentorName.setText(courseMentorNameString);
        courseMentorPhone.setText(courseMentorPhoneString);
        courseMentorEmail.setText(courseMentorEmailString);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        restartLoader();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_show_course, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_edit_course:
                Intent editCourseIntent = new Intent(this, EditCourseActivity.class);
                Uri uri = Uri.parse(CourseProvider.COURSE_CONTENT_URI + "/" + course.getId());
                editCourseIntent.putExtra(CourseProvider.COURSE_CONTENT_ITEM_TYPE, uri);
                startActivityForResult(editCourseIntent, EDIT_REQUEST_CODE);
                break;
            case R.id.action_delete_course:
                deleteCourse();
                break;
            case R.id.action_add_assessment:
                Intent assessmentIntent = new Intent(this, AddAssessmentActivity.class);
                assessmentIntent.putExtra(CourseProvider.COURSE_CONTENT_ITEM_TYPE, course.getId());
                startActivity(assessmentIntent);
                break;
            case R.id.action_add_notes:
                Intent noteIntent = new Intent(this, NotesActivity.class);
                noteIntent.putExtra(CourseProvider.COURSE_CONTENT_ITEM_TYPE, course.getId());
                startActivity(noteIntent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void setStartAlarm(View view)
    {
        int startYear = Integer.parseInt(startDateString.substring(6, 10));
        int startMonth = Integer.parseInt(startDateString.substring(0, 2))- 1;
        int startDay = Integer.parseInt(startDateString.substring(3, 5));

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.YEAR, startYear);
        startCalendar.set(Calendar.MONTH, startMonth);
        startCalendar.set(Calendar.DAY_OF_MONTH, startDay);
        startCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);
        Date startAlarmDate = startCalendar.getTime();
        Toast.makeText(this, "Alarm set for " +
                startAlarmDate, Toast.LENGTH_LONG).show();
        Intent startIntent = new Intent(this, CourseAlarmActivity.class);
        startIntent.putExtra("courseTitle", titleString);
        startIntent.putExtra("type", "start");
        PendingIntent startSender = PendingIntent.getActivity(
                this, 0, startIntent, 0);
        AlarmManager startAlarmMgr = (AlarmManager) getSystemService(
                Context.ALARM_SERVICE);
        startAlarmMgr.set(AlarmManager.RTC_WAKEUP,
                startAlarmDate.getTime(), startSender);
    }

    public void setEndAlarm(View view)
    {
        int endYear = Integer.parseInt(endDateString.substring(6, 10));
        int endMonth = Integer.parseInt(endDateString.substring(0, 2))- 1;
        int endDay = Integer.parseInt(endDateString.substring(3, 5));

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.YEAR, endYear);
        endCalendar.set(Calendar.MONTH, endMonth);
        endCalendar.set(Calendar.DAY_OF_MONTH, endDay);
        endCalendar.set(Calendar.HOUR_OF_DAY, 0);
        endCalendar.set(Calendar.MINUTE, 0);
        endCalendar.set(Calendar.SECOND, 0);
        Date endAlarmDate = endCalendar.getTime();
        Toast.makeText(this, "Alarm set for " +
                endAlarmDate, Toast.LENGTH_LONG).show();
        Intent endIntent = new Intent(this, CourseAlarmActivity.class);
        endIntent.putExtra("courseTitle", titleString);
        endIntent.putExtra("type", "end");
        PendingIntent endSender = PendingIntent.getActivity(
                this, 0, endIntent, 0);
        AlarmManager endAlarmMgr = (AlarmManager) getSystemService(
                Context.ALARM_SERVICE);
        endAlarmMgr.set(AlarmManager.RTC_WAKEUP,
                endAlarmDate.getTime(), endSender);
    }

    public void deleteCourse()
    {
        getContentResolver().delete(CourseProvider.COURSE_CONTENT_URI,
                courseFilter, null);
        Toast.makeText(this, R.string.course_deleted, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    public void restartLoader()
    {
        getLoaderManager().restartLoader(2, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        String selection;
        String[] selectionArgs;

        if (id == 2) {
            selection = DBManager.ASSESSMENT_ASSIGNED_COURSE + "=?";
            selectionArgs = new String[] { String.valueOf(course.getId()) };
        } else {
            selection = null;
            selectionArgs = null;
        }

        return new CursorLoader(this, CourseProvider.ASSESSMENT_CONTENT_URI,
                null, selection, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        assessmentAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        assessmentAdapter.swapCursor(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode) {
            case EDIT_REQUEST_CODE:
                Intent intent = getIntent();
                Uri uri = intent.getParcelableExtra(CourseProvider.COURSE_CONTENT_ITEM_TYPE);
                setTextViews(uri);
        }
    }
}
