package com.enpassio.apis.googlespreadsheet;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.enpassio.apis.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

//reference for the example: https://abhiandroid.com/database/external-storage
public class WriteToExternalStorage extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_WRITE_STORAGE = 101;
    private boolean isPermissionGranted = false;
    private Button goToReadActivity;
    private Button saveTextPrivatelyButton;
    private Button saveTextPubliclyButton;
    private EditText getTextToSaveEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_to_external_storage);
        getTextToSaveEditText = findViewById(R.id.add_text_edit_text);
        saveTextPrivatelyButton = findViewById(R.id.save_data_privately_button);
        saveTextPubliclyButton = findViewById(R.id.save_data_publicly_button);
        goToReadActivity = findViewById(R.id.read_data_from_file_button);

        saveTextPrivatelyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPermissionGranted) {
                    savePrivate();
                }
            }
        });
        saveTextPubliclyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPermissionGranted) {
                    savePublic();
                }
            }
        });
        goToReadActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPermissionGranted) {
                    next();
                }
            }
        });
        checkReadWritePermission();
    }

    private void savePrivate() {
        String info = getTextToSaveEditText.getText().toString();
        File folder = getExternalFilesDir("AbhiAndroid");// Folder Name
        File myFile = new File(folder, "myData2.txt");// Filename
        writeData(myFile, info);
        getTextToSaveEditText.setText("");
    }

    private void savePublic() {
        String info = getTextToSaveEditText.getText().toString();
        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);// Folder Name
        File myFile = new File(folder, "myData1.txt");// Filename
        writeData(myFile, info);
        getTextToSaveEditText.setText("");
    }

    private void next() {
        Intent intent = new Intent(this, NextActivityToReadData.class);
        startActivity(intent);
    }

    private void writeData(File myFile, String data) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(myFile);
            fileOutputStream.write(data.getBytes());
            Toast.makeText(this, "Done" + myFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void checkReadWritePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, so ask for permission
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_WRITE_STORAGE);

                //MY_PERMISSIONS_REQUEST_READ_WRITE_STORAGE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

        } else {
            //permission is already granted
            isPermissionGranted = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_WRITE_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    isPermissionGranted = true;
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    isPermissionGranted = false;
                }
            }
            break;
        }
    }
}
