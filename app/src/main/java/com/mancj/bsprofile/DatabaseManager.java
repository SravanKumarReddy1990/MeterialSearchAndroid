package com.mancj.bsprofile;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by 334301 on 2/23/2016.
 */
public class DatabaseManager extends SQLiteOpenHelper {

    //SQLite Database object
    //private static SQLiteDatabase rapidResDB = null;
    public String TAG = this.getClass().getName();
    private static DatabaseManager singleton = null;
    public static final String DATABASE_NAME = "BSProfiles";

    private DatabaseManager(Context cntx)
    {
        super(cntx, DATABASE_NAME , null, 1);
    }

    /* Static 'instance' method */
    public static DatabaseManager getInstance(Context cntx) {
        if (singleton == null)
        {
            singleton = new DatabaseManager(cntx);
        }
        return singleton;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d(TAG, "*** onCreate ***");
        db.execSQL("CREATE TABLE IF NOT EXISTS 'ProfessionDetails' ('logid' VARCHAR,'fullname' VARCHAR,'description' VARCHAR, 'profession' VARCHAR,'highlights' VARCHAR,'dob' VARCHAR,'gender' VARCHAR,'levelprofile' VARCHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS 'professions' ('serial' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,'professionname' VARCHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS 'yourservice' ('logid' VARCHAR,'servicetype' VARCHAR,'servicename' VARCHAR,'description' VARCHAR,'noofcourse' INTEGER,'noofprojects' INTEGER,'noofclients' INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS 'skills' ('serial' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,'skillname' VARCHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS 'skillset' ('logid' VARCHAR,'skillname' VARCHAR,'skilllevel' VARCHAR,'description' VARCHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS 'education' ('logid' VARCHAR,'educationtype' VARCHAR,'organisation' VARCHAR,'description' VARCHAR,'fromdate' VARCHAR,'todate' VARCHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS 'work' ('logid' VARCHAR,'companyname' VARCHAR,'description' VARCHAR,'fromdate' VARCHAR,'todate' VARCHAR,'profession' VARCHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS 'projects' ('logid' VARCHAR,'companyname' VARCHAR,'description' VARCHAR,'fromdate' VARCHAR,'todate' VARCHAR,'technologies' VARCHAR,'projectname' VARCHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS 'location' ('logid' VARCHAR,'locationname' VARCHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS 'messages' ('logid' VARCHAR,'from1' VARCHAR,'to1' VARCHAR,'message' VARCHAR,'datatime' datetime default current_timestamp)");
        //db.execSQL("CREATE TABLE IF NOT EXISTS 'MessagesList' ('me' VARCHAR,'fromnum' VARCHAR, 'message' VARCHAR,'status' VARCHAR,'datatime' datetime default current_timestamp)");
        storeProfessionNames(db);
        storeSkill(db);
    }
    public void storeProfessionNames(SQLiteDatabase db){
        Log.d(TAG, "*** Professions ***");
        try
        {
            db.beginTransaction();
            db.execSQL("INSERT INTO professions VALUES ( 1,'Senior Software Engeneer')");
            db.execSQL("INSERT INTO professions VALUES ( 2,'Junior Software Engeneer')");
            db.execSQL("INSERT INTO professions VALUES ( 3,'Software Engeneer')");
            db.execSQL("INSERT INTO professions VALUES ( 4,'Senior Software Architect')");
            db.execSQL("INSERT INTO professions VALUES ( 5,'Junior Software Architect')");
            db.execSQL("INSERT INTO professions VALUES ( 6,'Software Architect')");
            db.execSQL("INSERT INTO professions VALUES ( 7,'Architect')");
            db.execSQL("INSERT INTO professions VALUES ( 8,'Programmer')");
            db.execSQL("INSERT INTO professions VALUES ( 9,'Junior Programmer')");
            db.execSQL("INSERT INTO professions VALUES ( 10,'Human Resourse')");
            db.execSQL("INSERT INTO professions VALUES ( 11,'Field Marcketing')");
            db.execSQL("INSERT INTO professions VALUES ( 12,'Junior Field Marcketing')");
            db.execSQL("INSERT INTO professions VALUES ( 13,'Senior Field Marcketing')");
            db.execSQL("INSERT INTO professions VALUES ( 14,'Online Marcketing')");
            db.execSQL("INSERT INTO professions VALUES ( 15,'Team Lead')");
            db.execSQL("INSERT INTO professions VALUES ( 16,'Project Manager')");
            db.execSQL("INSERT INTO professions VALUES ( 17,'General Manager')");
            db.execSQL("INSERT INTO professions VALUES ( 18,'Ceo')");
            db.execSQL("INSERT INTO professions VALUES ( 19,'Director')");
            db.execSQL("INSERT INTO professions VALUES ( 20,'Accountant')");
            db.execSQL("INSERT INTO professions VALUES ( 21,'Hotel Management')");
            db.execSQL("INSERT INTO professions VALUES ( 22,'High School Teacher')");
            db.execSQL("INSERT INTO professions VALUES ( 23,'Primary School Teacher')");
            db.execSQL("INSERT INTO professions VALUES ( 24,'Junior Lecturer')");
            db.execSQL("INSERT INTO professions VALUES ( 25,'Senior Lecturer')");

            db.setTransactionSuccessful();
        }catch (Exception e)
        {
            Log.e(TAG,"Exception in configCategories : "+e.toString());
        }finally {
            db.endTransaction();
        }
    }
    public void storeSkill(SQLiteDatabase db){
        Log.d(TAG, "*** Skills ***");
        try
        {
            db.beginTransaction();
            db.execSQL("INSERT INTO skills VALUES ( 1,'Java')");
            db.execSQL("INSERT INTO skills VALUES ( 2,'J2ee')");
            db.execSQL("INSERT INTO skills VALUES ( 3,'Jquery')");
            db.execSQL("INSERT INTO skills VALUES ( 4,'Node js')");
            db.execSQL("INSERT INTO skills VALUES ( 5,'Spring')");
            db.execSQL("INSERT INTO skills VALUES ( 6,'Spring Boot')");
            db.execSQL("INSERT INTO skills VALUES ( 7,'Hibernate')");
            db.execSQL("INSERT INTO skills VALUES ( 8,'Postgresql')");
            db.execSQL("INSERT INTO skills VALUES ( 9,'Geoserver')");
            db.execSQL("INSERT INTO skills VALUES ( 10,'Android')");
            db.execSQL("INSERT INTO skills VALUES ( 11,'IOS')");
            db.execSQL("INSERT INTO skills VALUES ( 12,'C')");
            db.execSQL("INSERT INTO skills VALUES ( 13,'Cpp')");
            db.execSQL("INSERT INTO skills VALUES ( 14,'Qtcpp')");
            db.execSQL("INSERT INTO skills VALUES ( 15,'html css javascript')");
            db.execSQL("INSERT INTO skills VALUES ( 16,'PhotoShop')");
            db.execSQL("INSERT INTO skills VALUES ( 17,'Seo')");
            db.execSQL("INSERT INTO skills VALUES ( 18,'Digital Marcketing')");

            db.setTransactionSuccessful();
        }catch (Exception e)
        {
            Log.e(TAG,"Exception in configCategories : "+e.toString());
        }finally {
            db.endTransaction();
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "*** onUpgrade ***");
    }

    //Insert values into database
    public void insert(String sql)
    {
        Log.d(TAG,"*** insert *** sql : "+sql);
        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            db.beginTransaction();
            if(sql != null)
            {
                db.execSQL(sql);
                db.setTransactionSuccessful();
            }
        }catch (Exception e)
        {
            Log.e(TAG,"Exception insert : "+e.toString());
        }finally {
            db.endTransaction();
        }

    }

    //Delete Record from Database
    public void delete(String tableName,String whereStr)
    {
        Log.d(TAG,"*** delete *** sql : "+tableName+" where: "+whereStr);
        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            db.beginTransaction();
            if(tableName != null)
            {
                //db.execSQL(sql);
                db.delete(tableName, whereStr, null);
                db.setTransactionSuccessful();
            }
        }catch (Exception e)
        {
            Log.e(TAG,"delete : "+e.toString());
        }finally {
            db.endTransaction();
        }
    }

    //Update Databas
    public void update(String tableName, ContentValues values, String whereCond,String[] whereArgs)
    {
        Log.d(TAG,"*** update *** sql : TableName:"+tableName+"  Where"+whereCond);
        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            db.beginTransaction();
            if(tableName != null)
            {
                db.update(tableName, values, whereCond, whereArgs);
                db.setTransactionSuccessful();
            }
        }catch (Exception e)
        {
            Log.e(TAG,"update : "+e.toString());
        }finally {
            db.endTransaction();
        }
    }

    public void cleanTheDatabase()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.beginTransaction();
            //db.execSQL("DELETE FROM "+Constants.WayPointTbl);
            //db.execSQL("DELETE FROM "+Constants.CacheTbl);
            //db.execSQL("DELETE FROM "+Constants.CategoryTbl);
            //db.execSQL("DELETE FROM " + Constants.ZoneTbl);
            //db.setTransactionSuccessful();
        }catch (Exception e)
        {
            Log.e(TAG,"cleanTheDatabase : "+e.toString());
        }finally {
            db.endTransaction();
        }

    }

    public Cursor getRecords(String sql) {
        Log.d(TAG,"*** getRecords *** sql : "+sql);
        Cursor resultSet = null;
        SQLiteDatabase db = this.getReadableDatabase();
        try {
                db.beginTransaction();
                if (sql != null) {
                    resultSet = db.rawQuery(sql, null);
                    db.setTransactionSuccessful();
                }
        } catch (Exception e) {
                Log.e(TAG, "getRecords : "+e.toString());
        }finally {
            db.endTransaction();
        }
        return resultSet;
    }

    public SQLiteDatabase inserted(){
        return  this.getReadableDatabase();
    }
    private void configCategories(SQLiteDatabase db)
    {
        Log.d(TAG, "*** configCategories ***");
        try
        {
            //db.beginTransaction();
           // db.execSQL("INSERT INTO " + Constants.CategoryTbl + " VALUES (1,'Convenience',1,1)");

            //db.setTransactionSuccessful();
        }catch (Exception e)
        {
            Log.e(TAG,"Exception in configCategories : "+e.toString());
        }finally {
            db.endTransaction();
        }
    }

    private void configZones(SQLiteDatabase db)
    {
        Log.d(TAG, "*** configZones ***");
        try
        {
            //db.beginTransaction();
            //db.execSQL("INSERT INTO " + Constants.ZoneTbl + " VALUES (1,'Industrial',1,1)");

           // db.setTransactionSuccessful();
        }catch (Exception e)
        {
            Log.e(TAG,"Exception in configCategories : "+e.toString());
        }finally {
            db.endTransaction();
        }
    }

    public void fireSQL(String sql){
        Log.d(TAG,"*** fireSQL *** sql : "+sql);
        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            db.beginTransaction();
            if(sql != null)
            {
                db.execSQL(sql);
                db.setTransactionSuccessful();
            }
        }catch (Exception e)
        {
            Log.e(TAG,"Exception fireSQL : "+e.toString());
        }finally {
            db.endTransaction();
        }
    }
}
