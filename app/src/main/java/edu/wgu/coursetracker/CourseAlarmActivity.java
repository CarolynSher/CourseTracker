package edu.wgu.coursetracker;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class CourseAlarmActivity extends AppCompatActivity
{
    private TextView textView;
    private final int START_NOTIFICATION_ID = 1001;
    private final int END_NOTIFICATION_ID = 1002;
    String courseTitle;
    String dateType;
    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wake_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textView = (TextView) findViewById(R.id.textView);

        Intent intent = getIntent();
        courseTitle = intent.getExtras().getString("courseTitle");
        dateType = intent.getExtras().getString("type");

        if (dateType.equals("start")) {
            text = courseTitle + " has started!";
        } else if (dateType.equals("end")) {
            text = courseTitle + " has ended!";
        }

        textView.setText(text);

        final Window window = getWindow();
        window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        addNotification();
    }

    private void addNotification()
    {
        if (dateType.equals("start")) {
            NotificationManager startNotificationMgr = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
            Notification startNotification = new Notification.Builder(this)
                    .setContentTitle("Course: " + courseTitle + " has started.")
                    .setSmallIcon(android.R.drawable.star_on)
                    .setAutoCancel(false)
                    .build();
            startNotification.defaults|= startNotification.DEFAULT_SOUND;
            startNotification.defaults|= startNotification.DEFAULT_LIGHTS;
            startNotification.defaults|= startNotification.DEFAULT_VIBRATE;
            startNotification.flags |= startNotification.FLAG_INSISTENT;
            startNotification.flags |= startNotification.FLAG_AUTO_CANCEL;
            startNotificationMgr.notify(START_NOTIFICATION_ID, startNotification);
        } else if (dateType.equals("end")){
            NotificationManager endNotificationMgr = (NotificationManager)
                    getSystemService(NOTIFICATION_SERVICE);
            Notification endNotification = new Notification.Builder(this)
                    .setContentTitle("Course: " + courseTitle + " has ended.")
                    .setSmallIcon(android.R.drawable.star_on)
                    .setAutoCancel(false)
                    .build();
            endNotification.defaults|= Notification.DEFAULT_SOUND;
            endNotification.defaults|= Notification.DEFAULT_LIGHTS;
            endNotification.defaults|= Notification.DEFAULT_VIBRATE;
            endNotification.flags |= Notification.FLAG_INSISTENT;
            endNotification.flags |= Notification.FLAG_AUTO_CANCEL;
            endNotificationMgr.notify(END_NOTIFICATION_ID, endNotification);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();

        }
        return true;
    }
}
