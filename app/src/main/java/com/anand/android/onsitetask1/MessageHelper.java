package com.anand.android.onsitetask1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.util.Log;

public class MessageHelper extends SQLiteOpenHelper {

    public static final String DataBaseName="SMS.DB";
    public static final int DataBase_version=1;

    public static final String TABLE_NAME="Messages";
    public static final String MESSAGE_ID="id";
    public static final String MESSAGE_BODY="message";
    public static final String MESSAGE_NUMBER="number";
    public static final String MESSAGE_DATE="date";
    public static final String MESSAGE_TIME="time";
    private static final String TAG = "MessageHelper" ;

    public MessageHelper(Context context) {
        super(context, DataBaseName, null, DataBase_version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CreateQuery= "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME+"("+ MESSAGE_ID+
                " INTEGER PRIMARY KEY AUTOINCREMENT, "+MESSAGE_BODY+" TEXT, "+ MESSAGE_NUMBER+
                " TEXT, "+ MESSAGE_DATE+" TEXT, "+MESSAGE_TIME+" TEXT);";
        sqLiteDatabase.execSQL(CreateQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists TABLE_ENTRIES");
        onCreate(sqLiteDatabase);
    }

    public boolean insertData(String msg, String num, String date, String time) {
        SQLiteDatabase db =this. getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(MESSAGE_BODY,msg);
        contentValues.put(MESSAGE_NUMBER,num);
        contentValues.put(MESSAGE_DATE,date);
        contentValues.put(MESSAGE_TIME,time);
        long ins=db.insert(TABLE_NAME,null,contentValues);
        db.close();
        Log.i(TAG, "insertData: DATA Inserted");
        return ins != -1;
    }

    public Cursor viewData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * from " + TABLE_NAME;
        return db.rawQuery(query, null);
    }

    public void deleteData(int id){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_NAME,MESSAGE_ID+"="+ id,null);
        db.close();
    }
}
