package com.example.ramzan.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ramzan.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddEditNoteActivity extends AppCompatActivity {
    public static final String EXTRA_TITLE = "com.example.bogdan.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.example.bogdan.EXTRA_DESCRIPTION";
    public static final String EXTRA_QUANTITY = "com.example.bogdan.EXTRA_QUANTITY";
    public static final String EXTRA_PRICE = "com.example.bogdan.EXTRA_PRICE";
    public static final String EXTRA_IMAGE = "com.example.bogdan.EXTRA_IMAGE";
    public static final String EXTRA_ID = "com.example.bogdan.EXTRA_ID";

    public static int CAMERA_INTENT = 51;
    final int TYPE_PHOTO = 1;
    final int TYPE_VIDEO = 2;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;

    File storageDir;
    File image;
    final String TAG = "myLog";
    String currentPhotoPath;
    Uri photoURI;


    private EditText editText_title;
    private EditText editText_description;
    private NumberPicker numberPicker_quantity;
    private Bitmap bitmapImage;
    private EditText editText_price;
    private Button button_takePhoto;
    private ImageView image_view_takePhoto;
    private String pictureImagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        //createDirectory();



        editText_title = findViewById(R.id.edit_text_title);
        editText_description = findViewById(R.id.edit_text_description);
        editText_price = findViewById(R.id.edit_text_price);
        button_takePhoto = findViewById(R.id.button_takePhoto);
        bitmapImage = null;
        numberPicker_quantity = findViewById(R.id.number_picker_quantity);
        image_view_takePhoto = findViewById(R.id.image_view_takePhoto);


        numberPicker_quantity.setMinValue(1);
        numberPicker_quantity.setMaxValue(10);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Note");
            editText_title.setText(intent.getStringExtra(EXTRA_TITLE));
            editText_description.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            editText_price.setText(String.valueOf(intent.getDoubleExtra(EXTRA_PRICE, 1)));
            numberPicker_quantity.setValue(intent.getIntExtra(EXTRA_QUANTITY, 1));
        } else {
            setTitle("Add Note");

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        menuInflater.inflate(R.menu.delete_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                saveNote();
                return true;
            case R.id.delete_note:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void saveNote() {
        String title = "";
        String description = "";
        double price = 0;

        if (editText_title.getEditableText().toString().length() == 0 || editText_description.getEditableText()
                .toString()
                .length() == 0) {
            Toast.makeText(this, "Please input title and description", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (editText_price.getEditableText().toString().length() == 0) {
            Toast.makeText(this, "Please input title and description", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        title = editText_title.getText().toString();
        description = editText_description.getText().toString();
        price = Double.parseDouble(editText_price.getText().toString());
        int quantity = numberPicker_quantity.getValue();
        String picture = photoURI.toString();



        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_QUANTITY, quantity);
        data.putExtra(EXTRA_PRICE, price);
        data.putExtra(EXTRA_IMAGE, picture);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();


    }







    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Glide.with(this).load(image.getAbsolutePath()).into(image_view_takePhoto);
        }
    }






    public void dispatchTakePictureIntent(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.example.ramzan.fileProvider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

            }
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }







    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }





}