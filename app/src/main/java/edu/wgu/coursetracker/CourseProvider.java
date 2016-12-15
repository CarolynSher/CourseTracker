package edu.wgu.coursetracker;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Rickey on 5/12/2016.
 */
public class CourseProvider extends ContentProvider
{
    private static final String AUTHORITY = "edu.wgu.coursetracker.courseprovider";
    private static final String TERMS_BASE_PATH = "terms";
    private static final String COURSES_BASE_PATH = "courses";
    private static final String ASSESSMENTS_BASE_PATH = "assessments";
    private static final String NOTES_BASE_PATH = "notes";

    public static final Uri TERM_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TERMS_BASE_PATH);
    public static final Uri COURSE_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + COURSES_BASE_PATH);
    public static final Uri ASSESSMENT_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + ASSESSMENTS_BASE_PATH);
    public static final Uri NOTE_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + NOTES_BASE_PATH);

    // Constant to identify the requested operation
    private static final int TERMS = 1;
    private static final int TERMS_ID = 2;
    private static final int COURSES = 3;
    private static final int COURSES_ID = 4;
    private static final int ASSESSMENTS = 5;
    private static final int ASSESSMENTS_ID = 6;
    private static final int NOTES = 7;
    private static final int NOTES_ID = 8;


    private static final UriMatcher uriMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    public static final String TERM_CONTENT_ITEM_TYPE = "Term";
    public static final String COURSE_CONTENT_ITEM_TYPE = "Course";
    public static final String ASSESSMENT_CONTENT_ITEM_TYPE = "Assessment";
    public static final String NOTE_CONTENT_ITEM_TYPE = "Note";

    static {
        uriMatcher.addURI(AUTHORITY, TERMS_BASE_PATH, TERMS);
        uriMatcher.addURI(AUTHORITY, TERMS_BASE_PATH + "/#", TERMS_ID);

        uriMatcher.addURI(AUTHORITY, COURSES_BASE_PATH, COURSES);
        uriMatcher.addURI(AUTHORITY, COURSES_BASE_PATH + "/#", COURSES_ID);

        uriMatcher.addURI(AUTHORITY, ASSESSMENTS_BASE_PATH, ASSESSMENTS);
        uriMatcher.addURI(AUTHORITY, ASSESSMENTS_BASE_PATH + "/#", ASSESSMENTS_ID);

        uriMatcher.addURI(AUTHORITY, NOTES_BASE_PATH, NOTES);
        uriMatcher.addURI(AUTHORITY, NOTES_BASE_PATH + "/#", NOTES_ID);
    }

    private SQLiteDatabase db;

    @Override
    public boolean onCreate()
    {
        DBManager dbManager = new DBManager(getContext());
        db = dbManager.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case TERMS:
                queryBuilder.setTables(DBManager.TABLE_TERMS);
                cursor = queryBuilder.query(db, DBManager.ALL_TERM_COLUMNS, selection,
                        null, null, null, DBManager.TERM_TITLE + " DESC");
                break;
            case TERMS_ID:
                queryBuilder.setTables(DBManager.TABLE_TERMS);
                queryBuilder.appendWhere(DBManager.TERM_ID + " = " + uri.getLastPathSegment());
                cursor = queryBuilder.query(db, DBManager.ALL_TERM_COLUMNS, selection,
                        null, null, null, null);
                break;
            case COURSES:
                queryBuilder.setTables(DBManager.TABLE_COURSES);
                cursor = queryBuilder.query(db, projection, selection,
                        selectionArgs, null, null, DBManager.COURSE_TITLE + " DESC");
                break;
            case COURSES_ID:
                queryBuilder.setTables(DBManager.TABLE_COURSES);
                queryBuilder.appendWhere(DBManager.COURSE_ID + " = " + uri.getLastPathSegment());
                cursor = queryBuilder.query(db, DBManager.ALL_COURSE_COLUMNS, selection,
                        selectionArgs, null, null, null);
                break;
            case ASSESSMENTS:
                queryBuilder.setTables(DBManager.TABLE_ASSESSMENTS);
                cursor = queryBuilder.query(db, DBManager.ALL_ASSESSMENT_COLUMNS, selection,
                        selectionArgs, null, null, DBManager.ASSESSMENT_TITLE + " DESC");
                break;
            case ASSESSMENTS_ID:
                queryBuilder.setTables(DBManager.TABLE_ASSESSMENTS);
                queryBuilder.appendWhere(DBManager.ASSESSMENT_ID + " = " + uri.getLastPathSegment());
                cursor = queryBuilder.query(db, DBManager.ALL_ASSESSMENT_COLUMNS, selection,
                        selectionArgs, null, null, null);
                break;
            case NOTES:
                queryBuilder.setTables(DBManager.TABLE_NOTES);
                cursor = queryBuilder.query(db, DBManager.ALL_NOTE_COLUMNS, selection,
                        selectionArgs, null, null, DBManager.NOTE_CREATED + " DESC");
                break;
            case NOTES_ID:
                queryBuilder.setTables(DBManager.TABLE_NOTES);
                queryBuilder.appendWhere(DBManager.NOTE_ID + " = " + uri.getLastPathSegment());
                cursor = queryBuilder.query(db, DBManager.ALL_NOTE_COLUMNS, selection,
                        selectionArgs, null, null, null);
                break;
            default:
                throw new SQLException("Failed to find row " + uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri)
    {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
        switch (uriMatcher.match(uri)) {
            case TERMS:
                long termId = db.insert(DBManager.TABLE_TERMS,
                        null, values);
                return Uri.parse(TERMS_BASE_PATH + "/" + termId);
            case COURSES:
                long courseId = db.insert(DBManager.TABLE_COURSES,
                        null, values);
                return Uri.parse(COURSES_BASE_PATH + "/" + courseId);
            case ASSESSMENTS:
                long assessmentId = db.insert(DBManager.TABLE_ASSESSMENTS,
                        null, values);
                return Uri.parse(ASSESSMENTS_BASE_PATH + "/" + assessmentId);
            case NOTES:
                long noteId = db.insert(DBManager.TABLE_NOTES,
                        null, values);
                return Uri.parse(NOTES_BASE_PATH + "/" + noteId);
            default:
                throw new SQLException("Failed to insert new row into " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        switch (uriMatcher.match(uri)) {
            case TERMS:
                return db.delete(DBManager.TABLE_TERMS, selection, selectionArgs);
            case COURSES:
                return db.delete(DBManager.TABLE_COURSES, selection, selectionArgs);
            case ASSESSMENTS:
                return db.delete(DBManager.TABLE_ASSESSMENTS, selection, selectionArgs);
            case NOTES:
                return db.delete(DBManager.TABLE_NOTES, selection, selectionArgs);
            default:
                throw new SQLException("Failed to delete row " + uri);
        }
    }
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        switch (uriMatcher.match(uri)) {
            case TERMS:
                return db.update(DBManager.TABLE_TERMS,
                        values, selection, selectionArgs);
            case COURSES:
                return db.update(DBManager.TABLE_COURSES,
                        values, selection, selectionArgs);
            case ASSESSMENTS:
                return db.update(DBManager.TABLE_ASSESSMENTS,
                        values, selection, selectionArgs);
            case NOTES:
                return db.update(DBManager.TABLE_NOTES,
                        values, selection, selectionArgs);
            default:
                throw new SQLException("Failed to update row " + uri);
        }
    }
}
