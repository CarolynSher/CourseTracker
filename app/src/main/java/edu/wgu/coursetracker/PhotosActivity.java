package edu.wgu.coursetracker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class PhotosActivity extends AppCompatActivity
{
    protected Button imgButton;
    protected ImageView image;
    protected TextView field;

    private boolean imgCapFlag = false;
    protected boolean taken;
    protected static final String PHOTO_TAKEN = "photo_taken";
    protected String path;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        image = (ImageView) findViewById(R.id.image);
        field = (TextView) findViewById(R.id.textView);
        imgButton = (Button) findViewById(R.id.photoButton);
        imgButton.setOnClickListener(new ButtonClickHandler());
        path = Environment.getExternalStorageDirectory()
                + "/images/make_machine_example.jpg";
    }

    public class ButtonClickHandler implements View.OnClickListener {
        public void onClick(View view) {
            startCameraActivity();
        }
    }

    protected void startCameraActivity() {
        File file = new File(path);
        // Creates a Uri from a file
        Uri outputFileUri = Uri.fromFile(file);
        // Standard Intent action that can be sent to have the
        // camera application capture an image and return it.
        // You will be redirected to camera at this line
        Intent intent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        // Add the captured image in the path
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        // Start result method - Method handles the output
        // of the camera activity
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            // When user doesn't capture image, resultcode returns 0
            case 0:
                Log.i("AndroidProgrammerGuru", "User cancelled");
                break;
            // When user captures image, onPhotoTaken an user-defined method
            // to assign the capture image to ImageView
            case -1:
                onPhotoTaken();
                break;
        }
    }

    protected void onPhotoTaken() {
        // Log message
        Log.i("AndroidProgrammerGuru", "onPhotoTaken");
        // Flag used by Activity life cycle methods
        taken = true;
        // Flag used to check whether image captured or not
        imgCapFlag = true;
        // BitmapFactory- Create an object
        BitmapFactory.Options options = new BitmapFactory.Options();
        // Set image size
        options.inSampleSize = 4;
        // Read bitmap from the path where captured image is stored
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        // Set ImageView with the bitmap read in the prev line
        image.setImageBitmap(bitmap);
        // Make the TextView invisible
        field.setVisibility(View.GONE);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.i("AndroidProgrammerGuru", "onRestoreInstanceState()");
        if (savedInstanceState.getBoolean(PhotosActivity.PHOTO_TAKEN)) {
            onPhotoTaken();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(PhotosActivity.PHOTO_TAKEN, taken);
    }
}