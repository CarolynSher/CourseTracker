package edu.wgu.coursetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rickey on 5/5/2016.
 */
public class DBManager extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "coursetrack.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_TERMS = "terms";
    public static final String TERM_ID = "_id";
    public static final String TERM_TITLE = "term_title";
    public static final String TERM_START_DATE = "start_date";
    public static final String TERM_END_DATE = "end_date";

    public static final String TABLE_COURSES = "courses";
    public static final String COURSE_ID = "_id";
    public static final String COURSE_TITLE = "course_title";
    public static final String COURSE_START_DATE = "course_start_date";
    public static final String COURSE_END_DATE = "course_end_date";
    public static final String COURSE_STATUS = "course_status";
    public static final String COURSE_MENTOR_NAME = "course_mentor_name";
    public static final String COURSE_MENTOR_PHONE = "course_mentor_phone";
    public static final String COURSE_MENTOR_EMAIL = "course_mentor_email";
    public static final String COURSE_ASSIGNED_TERM = "course_assigned_term";

    public static final String TABLE_ASSESSMENTS = "assessments";
    public static final String ASSESSMENT_ID = "_id";
    public static final String ASSESSMENT_TITLE = "assessment_title";
    public static final String ASSESSMENT_TYPE = "assessment_type";
    public static final String ASSESSMENT_DUE_DATE = "assessment_due_date";
    public static final String ASSESSMENT_ASSIGNED_COURSE = "assessment_assigned_course";

    public static final String TABLE_NOTES = "notes";
    public static final String NOTE_ID = "_id";
    public static final String NOTE_TEXT = "noteText";
    public static final String NOTE_CREATED = "noteCreated";
    public static final String NOTE_ASSIGNED_COURSE = "note_assigned_course";

    public static final String[] ALL_TERM_COLUMNS =
            {TERM_ID, TERM_TITLE, TERM_START_DATE, TERM_END_DATE};

    public static final String[] ALL_COURSE_COLUMNS =
            {COURSE_ID, COURSE_TITLE, COURSE_START_DATE,
            COURSE_END_DATE, COURSE_STATUS, COURSE_MENTOR_NAME,
                    COURSE_MENTOR_PHONE, COURSE_MENTOR_EMAIL, COURSE_ASSIGNED_TERM};

    public static final String[] ALL_ASSESSMENT_COLUMNS =
            {ASSESSMENT_ID, ASSESSMENT_TITLE, ASSESSMENT_TYPE,
                    ASSESSMENT_DUE_DATE, ASSESSMENT_ASSIGNED_COURSE};

    public static final String[] ALL_NOTE_COLUMNS =
            {NOTE_ID, NOTE_TEXT, NOTE_CREATED, NOTE_ASSIGNED_COURSE};

    private static final String TERM_TABLE_CREATE =
            "CREATE TABLE " + TABLE_TERMS + " (" +
                    TERM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TERM_TITLE + " TEXT, " +
                    TERM_START_DATE + " TEXT, " +
                    TERM_END_DATE + " TEXT" +
                    ")";

    private static final String COURSE_TABLE_CREATE =
            "CREATE TABLE " + TABLE_COURSES + " (" +
                    COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COURSE_TITLE + " TEXT, " +
                    COURSE_START_DATE + " TEXT, " +
                    COURSE_END_DATE + " TEXT, " +
                    COURSE_STATUS + " TEXT, " +
                    COURSE_MENTOR_NAME + " TEXT, " +
                    COURSE_MENTOR_PHONE + " TEXT, " +
                    COURSE_MENTOR_EMAIL + " TEXT, " +
                    COURSE_ASSIGNED_TERM + " INTEGER, " +
                    "FOREIGN KEY(" + COURSE_ASSIGNED_TERM + ") " +
                    "REFERENCES " + TABLE_TERMS + "(" + TERM_ID + ")" +
                    ")";

    public static final String ASSESSMENT_TABLE_CREATE =
            "CREATE TABLE " + TABLE_ASSESSMENTS + " (" +
                    ASSESSMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ASSESSMENT_TITLE + " TEXT, " +
                    ASSESSMENT_TYPE + " TEXT, " +
                    ASSESSMENT_DUE_DATE + " TEXT, " +
                    ASSESSMENT_ASSIGNED_COURSE + " INTEGER, " +
                    "FOREIGN KEY(" + ASSESSMENT_ASSIGNED_COURSE + ") " +
                    "REFERENCES " + TABLE_COURSES + "(" + COURSE_ID + ")" +
                    ")";

    public static final String NOTE_TABLE_CREATE =
            "CREATE TABLE " + TABLE_NOTES + " (" +
                    NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NOTE_TEXT + " TEXT, " +
                    NOTE_CREATED + " TEXT default CURRENT_TIMESTAMP, " +
                    NOTE_ASSIGNED_COURSE + " INTEGER, " +
                    "FOREIGN KEY(" + NOTE_ASSIGNED_COURSE + ") " +
                    "REFERENCES " + TABLE_COURSES + "(" + COURSE_ID + ")" +
                    ")";

    public DBManager(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(TERM_TABLE_CREATE);
        db.execSQL(COURSE_TABLE_CREATE);
        db.execSQL(ASSESSMENT_TABLE_CREATE);
        db.execSQL(NOTE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TERMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSESSMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }
}
