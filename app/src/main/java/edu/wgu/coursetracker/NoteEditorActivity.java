package edu.wgu.coursetracker;

import android.content.ContentValues;
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
import android.widget.EditText;
import android.widget.Toast;

public class NoteEditorActivity extends AppCompatActivity
{

    private static final int MENU_ITEM_SAVE = 1001;
    private static final int MENU_ITEM_DELETE = 1002;
    private String action;
    private EditText editor;
    long id;
    private String noteFilter;
    private String oldText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editor = (EditText) findViewById(R.id.editText);

        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra(CourseProvider.NOTE_CONTENT_ITEM_TYPE);
        id = intent.getLongExtra(CourseProvider.COURSE_CONTENT_ITEM_TYPE, 0);

        if (uri == null) {
            action = Intent.ACTION_INSERT;
            setTitle(getString(R.string.new_note_title));
        } else {
            action = Intent.ACTION_EDIT;
            noteFilter = DBManager.NOTE_ID + "=" + uri.getLastPathSegment();
            Cursor cursor = getContentResolver().query(uri,
                DBManager.ALL_NOTE_COLUMNS, noteFilter, null, null);
            cursor.moveToFirst();
            oldText = cursor.getString(cursor.getColumnIndex(DBManager.NOTE_TEXT));
            editor.setText(oldText);
            editor.requestFocus();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        if (action.equals(Intent.ACTION_EDIT)) {
            getMenuInflater().inflate(R.menu.menu_note_editor, menu);
        } else if (action.equals(Intent.ACTION_INSERT)) {
            menu.add(0, MENU_ITEM_SAVE, 0, R.string.save_note)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case MENU_ITEM_SAVE: case R.id.action_save:
                finishEdit();
                break;
            case R.id.action_delete_note:
                deleteNote();
                break;
        }
        return true;
    }

    private void deleteNote()
    {
        getContentResolver().delete(CourseProvider.NOTE_CONTENT_URI,
                noteFilter, null);
        Toast.makeText(NoteEditorActivity.this, R.string.note_deleted,
                Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private void finishEdit()
    {
        String newText = editor.getText().toString().trim();
        
        switch (action) {
            case Intent.ACTION_INSERT:
                if (newText.length() == 0) {
                    setResult(RESULT_CANCELED);
                } else {
                    addNote(newText, id);
                }
                break;
            case Intent.ACTION_EDIT:
                if (newText.length() == 0) {
                    deleteNote();
                } else if (oldText.equals(newText)) {
                    setResult(RESULT_CANCELED);
                } else {
                    updateNote(newText);
                }
        }
        finish();
    }

    private void updateNote(String noteText)
    {
        ContentValues values = new ContentValues();
        values.put(DBManager.NOTE_TEXT, noteText);
        getContentResolver().update(CourseProvider.NOTE_CONTENT_URI, values, noteFilter, null);
        Toast.makeText(NoteEditorActivity.this, R.string.note_updated, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    private void addNote(String noteText, long courseId)
    {
        ContentValues values = new ContentValues();
        values.put(DBManager.NOTE_TEXT, noteText);
        values.put(DBManager.NOTE_ASSIGNED_COURSE, courseId);
        getContentResolver().insert(CourseProvider.NOTE_CONTENT_URI, values);
        setResult(RESULT_OK);
    }


}
