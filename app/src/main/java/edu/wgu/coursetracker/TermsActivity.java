package edu.wgu.coursetracker;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class TermsActivity extends AppCompatActivity
    implements android.app.LoaderManager.LoaderCallbacks<Cursor>
{
    private CursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String[] from = {
                DBManager.TERM_TITLE };

        int[] to = {
                android.R.id.text1 };

        adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1, null, from, to, 0);

        ListView list = (ListView) findViewById(android.R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(TermsActivity.this, ShowTermActivity.class);
                Uri uri = Uri.parse(CourseProvider.TERM_CONTENT_URI + "/" + id);
                intent.putExtra(CourseProvider.TERM_CONTENT_ITEM_TYPE, uri);
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(0, null, TermsActivity.this);
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
        getMenuInflater().inflate(R.menu.menu_terms, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            // add Action Bar button
            case R.id.action_add_term:
                Intent intent = new Intent(this, AddTermActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void restartLoader()
    {
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        return new android.content.CursorLoader(this, CourseProvider.TERM_CONTENT_URI,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data)
    {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader)
    {
        adapter.swapCursor(null);
    }
}
