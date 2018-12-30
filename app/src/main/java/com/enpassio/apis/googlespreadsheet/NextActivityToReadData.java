package com.enpassio.apis.googlespreadsheet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.enpassio.apis.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class NextActivityToReadData extends AppCompatActivity {
    private TextView showText;
    private Button readPublicData;
    private Button readPrivateData;
    private Button goBackToWriteActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_to_read_data);
        showText = findViewById(R.id.show_data_text_view);
        readPublicData = findViewById(R.id.show_public_data_button);
        readPrivateData = findViewById(R.id.show_private_data_button);
        goBackToWriteActivity = findViewById(R.id.go_back_to_write_activity_button);

        readPublicData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPublic();
            }
        });
        readPrivateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPrivate();
            }
        });
        goBackToWriteActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });

    }

    public void back() {
        Intent intent = new Intent(this, WriteToExternalStorage.class);
        startActivity(intent);
    }

    public void getPublic() {
        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS); // Folder Name
        File myFile = new File(folder, "myData1.txt"); // Filename
        String text = getdata(myFile);
        if (text != null) {
            showText.setText(text);
        } else {
            showText.setText("No Data");
        }
    }

    public void getPrivate() {
        File folder = getExternalFilesDir("AbhiAndroid"); // Folder Name
        File myFile = new File(folder, "myData2.txt"); // Filename
        String text = getdata(myFile);
        if (text != null) {
            showText.setText(text);
        } else {
            showText.setText("No Data");
        }
    }

    private String getdata(File myfile) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(myfile);
            int i = -1;
            StringBuffer buffer = new StringBuffer();
            while ((i = fileInputStream.read()) != -1) {
                buffer.append((char) i);
            }
            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}