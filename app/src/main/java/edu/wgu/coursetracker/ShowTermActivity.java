package edu.wgu.coursetracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ShowTermActivity extends AppCompatActivity
    implements android.app.LoaderManager.LoaderCallbacks<Cursor>
{
    private Term term;
    private TextView title;
    private TextView startDate;
    private TextView endDate;
    private String termFilter;
    private String assignedCourseFilter;
    private String titleString;
    private String startDateString;
    private String endDateString;

    Uri uri;
    private CursorAdapter courseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_term);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = (TextView) findViewById(R.id.Term);
        startDate = (TextView) findViewById(R.id.Start);
        endDate = (TextView) findViewById(R.id.End);

        Intent intent = getIntent();

        uri = intent.getParcelableExtra(CourseProvider.TERM_CONTENT_ITEM_TYPE);

        termFilter = DBManager.TERM_ID + "=" + uri.getLastPathSegment();

        Cursor cursor = getContentResolver().query(uri,
                DBManager.ALL_TERM_COLUMNS, termFilter, null, null);

        if (cursor != null) {
            cursor.moveToFirst();

            titleString = cursor.getString(cursor.getColumnIndex(DBManager.TERM_TITLE));
            startDateString = cursor.getString(cursor.getColumnIndex(DBManager.TERM_START_DATE));
            endDateString = cursor.getString(cursor.getColumnIndex(DBManager.TERM_END_DATE));
            term = new Term(titleString, startDateString, endDateString);
            term.setId(cursor.getInt(cursor.getColumnIndex(DBManager.TERM_ID)));
        } else {
            titleString = "No data";
            startDateString = "No data";
            endDateString = "No data";

        }

        setTitle(titleString);
        title.setText(titleString);
        startDate.setText("Start date: " + startDateString);
        endDate.setText("End date: " + endDateString);

        String[] from = {
                DBManager.COURSE_TITLE
        };

        int[] to = {
                android.R.id.text1
        };

        courseAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1, null, from, to, 0);

        ListView courseList = (ListView) findViewById(android.R.id.list);
        courseList.setAdapter(courseAdapter);

        courseList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(ShowTermActivity.this, ShowCourseActivity.class);
                Uri courseURI = Uri.parse(CourseProvider.COURSE_CONTENT_URI + "/" + id);
                intent.putExtra(CourseProvider.COURSE_CONTENT_ITEM_TYPE, courseURI);
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(1, null, ShowTermActivity.this);
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
        getMenuInflater().inflate(R.menu.menu_show_term, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_delete_term:
                deleteTerm();
                break;
            case R.id.action_add_Course:
                Intent intent = new Intent(this, AddCourseActivity.class);
                intent.putExtra(CourseProvider.TERM_CONTENT_ITEM_TYPE, term.getId());
                startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void deleteTerm()
    {
        if (hasCourses(term.getId())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Term cannot be deleted if courses are assigned to it.")
                    .setTitle("Term has assigned courses")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            getContentResolver().delete(CourseProvider.TERM_CONTENT_URI,
                    termFilter, null);
            Toast.makeText(this, R.string.term_deleted, Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        }
    }

    private boolean hasCourses(long id)
    {
        String[] projection = {DBManager.COURSE_ID};
        assignedCourseFilter = DBManager.COURSE_ASSIGNED_TERM + "=" + id;

        Uri courseUri = CourseProvider.COURSE_CONTENT_URI;
        Cursor courseCursor = getContentResolver().query(courseUri,
                projection, assignedCourseFilter, null, null);
        int courses = courseCursor.getCount();
        if (courses == 0)
            return false;
        else
            return true;
    }

    private void restartLoader()
    {
        getLoaderManager().restartLoader(1, null, this);
    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        String selection;
        String[] selectionArgs;

        if (id == 1) {
            selection = DBManager.COURSE_ASSIGNED_TERM + "=?";
            selectionArgs = new String[]{String.valueOf(term.getId())};
        } else {
            selection = null;
            selectionArgs = null;
        }
        return new android.content.CursorLoader(this, CourseProvider.COURSE_CONTENT_URI,
                null, selection, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data)
    {
        courseAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader)
    {
        courseAdapter.swapCursor(null);
    }
}
