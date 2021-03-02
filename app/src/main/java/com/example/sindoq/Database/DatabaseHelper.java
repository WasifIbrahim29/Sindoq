package com.example.sindoq.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper
{
    public static final String name = "Sindoq.db";

    public DatabaseHelper(Context context)
    {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table blocked_apps(app_id integer primary key autoincrement,  app_name string, package_name string)");
        db.execSQL("create table unblocked_apps(app_id integer primary key autoincrement,  app_name string, package_name string)");
        db.execSQL("create table sec_result(sec_id integer primary key autoincrement, seconds int)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("drop table if exists blocked_apps");
        db.execSQL("drop table if exists unblocked_apps");
        db.execSQL("drop table if exists sec_result");
        onCreate(db);
    }

    public boolean insertapp(String app_name, String packagename)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("app_name", app_name);
        cv.put("package_name", packagename);

        return db.insert("blocked_apps", null, cv) != -1;

    }
    public boolean insertUnBlockedApp(String app_name,String packagename)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("app_name", app_name);
        cv.put("package_name", packagename);
        return db.insert("unblocked_apps", null, cv) != -1;
    }

    public void deleteApp(String app_name) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "app_name=?";
        String whereArgs[] = {app_name};
        db.delete("blocked_apps", whereClause, whereArgs);
    }

    public void deleteUnBlockedApp(String app_name) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "app_name=?";
        String whereArgs[] = {app_name};
        db.delete("unblocked_apps", whereClause, whereArgs);
    }


    public boolean CHeckIfAppExists(String AppName){
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "app_name=?";
        String whereArgs[] = {AppName};
       // Log.e("adapter", "IN APP EXISTS " + AppName);
        Cursor c= db.rawQuery( "SELECT * FROM blocked_apps WHERE app_name=?", whereArgs);
        if(c.getCount()==0)
        {
            return false;
        }
        else{
            return true;
        }
    }
    public boolean CHeckIfAppExistsFromPackageName(String PackageName){
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "package_name=?";
        String whereArgs[] = {PackageName};
       // Log.e("adapter", "IN APP EXISTS " + PackageName);
        Cursor c= db.rawQuery( "SELECT * FROM blocked_apps WHERE package_name=?", whereArgs);
        if(c.getCount()==0)
        {
            return false;
        }
        else{
            return true;
        }
    }
    public boolean CHeckIfAppExistsInUnblocked(String AppName){
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "app_name=?";
        String whereArgs[] = {AppName};
       // Log.e("adapter", "IN APP EXISTS " + AppName);
        Cursor c= db.rawQuery( "SELECT * FROM unblocked_apps WHERE app_name=?", whereArgs);
        if(c.getCount()==0)
        {
            return false;
        }
        else{
            return true;
        }
    }

    public boolean insert_sec(int sec)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("seconds", sec);
        return db.insert("sec_result", null, cv) != -1;
    }
    public Cursor getSeconds()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("Select * from sec_result",null);
        return c;
    }


    public boolean CHeckIfAppExistsInUnblockedFromPackageName(String PackageName){
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "package_name=?";
        String whereArgs[] = {PackageName};
        //Log.e("adapter", "IN APP EXISTS " + PackageName);
        Cursor c= db.rawQuery( "SELECT * FROM unblocked_apps WHERE package_name=?", whereArgs);
        if(c.getCount()==0)
        {
            return false;
        }
        else{
            return true;
        }
    }

    public Cursor getBlockedApps()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("Select * from blocked_apps",null);
        return c;
    }

    public Cursor getManualSelectedQuestions()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("Select * from manual_selection",null);
        return c;
    }

    public Cursor getQuizID()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("Select quiz_id from quiz",null);
        return c;
    }
    public int delete(int position)
    {
        String x = Integer.toString(position);
        SQLiteDatabase db = this.getReadableDatabase();
        return db.delete("drafts","chat_id = ?", new String[]{x});
    }
}
