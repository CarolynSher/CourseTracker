package edu.wgu.coursetracker;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
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
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class NotesActivity extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<Cursor>
{
    private static final int EDITOR_REQUEST_CODE = 101;
    long courseId;
    private CursorAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent courseIntent = getIntent();
        courseId = courseIntent.getLongExtra(CourseProvider.COURSE_CONTENT_ITEM_TYPE, 0);

        String[] from = { DBManager.NOTE_TEXT };
        int[] to = { android.R.id.text1 };

        noteAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1, null, from, to, 0);
        ListView noteList = (ListView) findViewById(android.R.id.list);
        noteList.setAdapter(noteAdapter);
        noteList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent noteIntent = new Intent(NotesActivity.this, NoteEditorActivity.class);
                Uri noteUri = Uri.parse(CourseProvider.NOTE_CONTENT_URI + "/" + id);
                noteIntent.putExtra(CourseProvider.NOTE_CONTENT_ITEM_TYPE, noteUri);
                startActivityForResult(noteIntent, EDITOR_REQUEST_CODE);
            }
        });

        getLoaderManager().initLoader(3, null, NotesActivity.this);
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
        getMenuInflater().inflate(R.menu.menu_notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_new_note:
                Intent newNoteIntent = new Intent(this, NoteEditorActivity.class);
                newNoteIntent.putExtra(CourseProvider.COURSE_CONTENT_ITEM_TYPE, courseId);
                startActivityForResult(newNoteIntent, EDITOR_REQUEST_CODE);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void restartLoader() {getLoaderManager().restartLoader(3, null, this);}

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        String selection;
        String[] selectionArgs;

        if (id == 3)
        {
            selection = DBManager.NOTE_ASSIGNED_COURSE + "=?";
            selectionArgs = new String[] { String.valueOf(courseId) };
        } else {
            selection = null;
            selectionArgs = null;
        }

        return new CursorLoader(this, CourseProvider.NOTE_CONTENT_URI,
                null, selection, selectionArgs, DBManager.NOTE_CREATED + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        noteAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        noteAdapter.swapCursor(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == EDITOR_REQUEST_CODE && resultCode == RESULT_OK) {
            restartLoader();
        }
    }
}
