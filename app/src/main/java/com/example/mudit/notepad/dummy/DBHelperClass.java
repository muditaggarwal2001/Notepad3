package com.example.mudit.notepad.dummy;

import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mudit on 28-01-2018.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Mudit on 26-01-2018.
 */

public class DBHelperClass extends SQLiteOpenHelper {

    public static final String name = "note.db";
    public static final String id = "_ID";
    public static final String title = "Title";
    public static final String note = "Note";
    public static final String pics = "Pics";
    public static final String Table_Name = "notetable";
    public static final int version = 1;
    SQLiteDatabase db;
    public DBHelperClass(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+Table_Name+" ("+id+" INTEGER Primary Key AUTOINCREMENT, "+title+" TEXT, "+note+" TEXT, "+pics+" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+Table_Name);
        onCreate(db);
    }

    public void open()
    {
        db=this.getWritableDatabase();
    }

    public void close()
    {
        db.close();
    }

    public long insertData(String ititle, String inote, String picpath)
    {
        ContentValues values = new ContentValues();
        values.put(title,ititle);
        values.put(note,inote);
        values.put(pics,picpath+",");
        long result=db.insert(Table_Name,null,values);
        return result;
    }

    public Cursor getData()
    {
        DummyContent.Note temp;
        DummyContent.ITEMS = new ArrayList<DummyContent.Note>();
        DummyContent.ITEM_MAP = new HashMap<String, DummyContent.Note>();
        Cursor result = db.rawQuery("Select * from "+Table_Name, null);
        while(result.moveToNext())
        {
            temp = new DummyContent.Note(result.getString(0),result.getString(1),result.getString(2));
            DummyContent.ITEMS.add(temp);
            DummyContent.ITEM_MAP.put(temp.id, temp);
        }
        return result;
    }
}

