package com.anand.android.onsitetask1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    String phoneNumber, messageBody;
    Context ctx;


    @Override
    public void onReceive(Context context, Intent intent) {
        phoneNumber = intent.getStringExtra("number");
        messageBody = intent.getStringExtra("message");
        ctx = context;
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, messageBody, null, null);
        Toast.makeText(ctx, "Message Sent", Toast.LENGTH_SHORT).show();
    }

}


