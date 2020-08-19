package com.anand.android.onsitetask1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG ="MainActivity" ;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1 ;
    ArrayList<MessageItemClass> messageArrayList;
    MessageHelper messageHelper;
    MessageAdapter adapter;
    RecyclerView messageEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermission();
        messageEntries=findViewById(R.id.recyclerView);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MessageInputActivity.class);
                startActivity(intent);
            }
        });
    }

    public void requestPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
    }

    @Override
    protected void onResume() {
        getMessages();
        super.onResume();
    }

    private void getMessages() {

        messageArrayList=new ArrayList<>();
        messageHelper=new MessageHelper(this);
        Cursor cursor=messageHelper.viewData();
        TextView textView=findViewById(R.id.noData);
        if (cursor.getCount() == 0) {
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
            if (textView.getVisibility() == View.VISIBLE)
                textView.setVisibility(View.GONE);
            messageArrayList.clear();
            while (cursor.moveToNext()) {
                String msg;
                int id;
                String num;
                String date, time;

                id = cursor.getInt(0);
                msg = cursor.getString(1);
                num = cursor.getString(2);
                date = cursor.getString(3);
                time = cursor.getString(4);
                Log.i(TAG, "getMessages: "+msg+num+date);

                messageArrayList.add(new MessageItemClass(id, msg, num, date, time));
            }
            adapter = new MessageAdapter(messageArrayList, MainActivity.this);
            messageEntries.setHasFixedSize(true);
            messageEntries.setAdapter(adapter);
            messageEntries.setLayoutManager(new LinearLayoutManager(this));
        }
    }
}