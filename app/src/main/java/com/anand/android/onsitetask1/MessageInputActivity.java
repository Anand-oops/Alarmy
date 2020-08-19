package com.anand.android.onsitetask1;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.job.JobInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MessageInputActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener, View.OnClickListener {

    private static final String TAG = "MessageInputActivity";
    Calendar calendar_main = Calendar.getInstance();
    int yr,mon,day;
    MessageHelper messageHelper= new MessageHelper(this);
    int hr,min;
    TextView dateTextView,timeTextView;
    EditText editTextNum,editTextMsg;
    String mNumber;
    String mMessage;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.entry_input_menu, menu);
        return true;
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesage_input);

        editTextNum= findViewById(R.id.id_num);
        dateTextView= findViewById(R.id.id_date);
        timeTextView= findViewById(R.id.id_time);
        editTextMsg= findViewById(R.id.id_msg);

        dateTextView.setOnClickListener(this);
        timeTextView.setOnClickListener(this);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        calendar_main.set(Calendar.YEAR,year);
        calendar_main.set(Calendar.MONTH,month);
        calendar_main.set(Calendar.DAY_OF_MONTH,dayOfMonth);

        Date temp_date=calendar_main.getTime();
        Date currentDate= Calendar.getInstance().getTime();
        if (temp_date.before(currentDate)){
            AlertDialog.Builder dialog = new AlertDialog.Builder(MessageInputActivity.this);
            dialog.setMessage("The time has passed.. ");
            dialog.setTitle("Error !");
            dialog.setIcon(R.drawable.ic_block);
            dialog.setPositiveButton("Set Again",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {
                        }
                    });
            AlertDialog alertDialog = dialog.create();
            alertDialog.setCancelable(false);
            alertDialog.show();
        }else {
            SimpleDateFormat temp_format = new SimpleDateFormat("dd/MM/yyyy");
            dateTextView.setText(temp_format.format(temp_date));
        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        calendar_main.set(Calendar.HOUR_OF_DAY,hourOfDay);
        calendar_main.set(Calendar.MINUTE,minute);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatConvert = new SimpleDateFormat("hh:mm a");
        Date dateConvert=calendar_main.getTime();

        timeTextView.setText(formatConvert.format(dateConvert));
    }

    @Override
    public void onClick(View view) {
        if(view==dateTextView){
            Calendar calendar_set=Calendar.getInstance();
            yr=calendar_set.get(Calendar.YEAR);
            mon=calendar_set.get(Calendar.MONTH);
            day=calendar_set.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePicker=new DatePickerDialog(MessageInputActivity.this,this,yr,mon,day);

            datePicker.show();
        }
        else if(view==timeTextView){

            Calendar calendar_set=Calendar.getInstance();
            hr=calendar_set.get(Calendar.HOUR_OF_DAY);
            min=calendar_set.get(Calendar.MINUTE);
            TimePickerDialog timePicker=new TimePickerDialog(MessageInputActivity.this,this,hr,min,
                    DateFormat.is24HourFormat(MessageInputActivity.this));
            timePicker.show();

        }
    }

    public void pickNumber(View view){
        Intent pick_intent = new Intent(Intent.ACTION_PICK);
        pick_intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(pick_intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Uri uri = data.getData();

            if (uri != null) {
                try (Cursor cursor = getContentResolver().query(uri, new String[]{
                                ContactsContract.CommonDataKinds.Phone.NUMBER,
                                ContactsContract.CommonDataKinds.Phone.TYPE},
                        null, null, null)) {

                    if (cursor != null && cursor.moveToFirst()) {
                        String number = cursor.getString(0);
                        editTextNum.setText(number.replaceAll("\\s",""));
                    }
                }
            }
        }
    }

    public void saveData(MenuItem item) {
        mMessage=editTextMsg.getText().toString().trim();
        mNumber=editTextNum.getText().toString().trim();
        String date=dateTextView.getText().toString().trim();
        String time=timeTextView.getText().toString().trim();

        if (mNumber.isEmpty() || date.isEmpty() || time.isEmpty()) {

            AlertDialog.Builder dialog = new AlertDialog.Builder(MessageInputActivity.this);
            dialog.setMessage("Invalid Entry.... ");
            dialog.setTitle("Error !");
            dialog.setIcon(R.drawable.ic_block);
            dialog.setPositiveButton("Enter Again",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {
                        }
                    });
            dialog.setNegativeButton("Cancel Feed",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
            AlertDialog alertDialog = dialog.create();
            alertDialog.setCancelable(false);
            alertDialog.show();
        } else {
            if (messageHelper.insertData(mMessage,mNumber,date,time)) {
                Toast.makeText(this,"Added",Toast.LENGTH_SHORT).show();
                Log.i(TAG, "saveData: "+mNumber+date+time);
                sendMessage();
                finish();
            }
        }
    }

    public void sendMessage(){
        Log.i(TAG,"send Message at"+calendar_main.getTimeInMillis());
        Intent intentAlarm= new Intent(this,AlarmReceiver.class);
        intentAlarm.putExtra("number",mNumber);
        intentAlarm.putExtra("message",mMessage);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(this.getApplicationContext(),619,intentAlarm,PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager= (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar_main.getTimeInMillis(),pendingIntent);
        Toast.makeText(this,"Message Scheduled",Toast.LENGTH_SHORT).show();
    }
}
