package com.mancj.bsprofile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.Html;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by 334301 on 2/23/2016.
 */
public class DBM {

    public String TAG = this.getClass().getName();
    private DatabaseManager dbMgr = null;
    private Context cntx = null;

    public DBM(Context cntx)
    {
        this.cntx = cntx;
        dbMgr = DatabaseManager.getInstance(cntx);
       // cleanContacts();
    }
    // insert values into Waypoint

    public void insertMessages(JSONObject jsonObject,int mode)
    {
        //cleanTables("projects","","");
        Log.d(TAG," Insert Messages ");
        try {
            String sql = "INSERT INTO  messages (logid,from1,to1,message)"
                    +" values('"+jsonObject.getString("logid")+"','"+ jsonObject.getString("from")
                    +"','"+jsonObject.getString("to")+"','"+jsonObject.getString("message")+"')";

            dbMgr.insert(sql);
        }catch (Exception e)
        {
            Log.e(TAG, "insert MessageList : " + e.toString());
        }
    }
    public void cleanTables(String tableName,String fieldName,String id)
    {
        String sql = fieldName+" = '"+ id +"'";
        try {
            dbMgr.delete(tableName,sql);
        }catch (Exception e)
        {
            Log.e(TAG, "deleteWaypoint : " + e.toString());
        }
    }
    public void cleanContacts(String tableName,String id)
    {
        String sql = "logid = '"+ id +"'";
        try {
            dbMgr.delete(tableName,sql);
        }catch (Exception e)
        {
            Log.e(TAG, "deleteWaypoint : " + e.toString());
        }
    }
    public void updateMessages(String from)
    {Log.d(TAG, "updateMessages : " + from);
        try {
            ContentValues values = new ContentValues();
            values.put("status","r");
            dbMgr.update("MessagesList", values, "fromnum =?", new String[]{from});
        }catch (Exception e)
        {
            Log.e(TAG, "updateMessages : " + e.toString());
        }
    }


    //fetch all records from Professions
    public ArrayList<String> getAllProfession()
    {
        ArrayList<String> professionsList = new ArrayList<String>();
        professionsList.add("Your Professions");
        String sql = "SELECT * from professions";
        Cursor resultSet = null;
        try {
            resultSet = dbMgr.getRecords(sql);

            if(resultSet != null)
            {
                resultSet.moveToFirst();
                while (!resultSet.isAfterLast()){
                    String profession= resultSet.getString(1);
                    professionsList.add(profession);
                    resultSet.moveToNext();
                }
            }

        }catch (Exception e)
        {
            Log.e(TAG, "getAllContacts : " + e.toString());
        }

        return  professionsList;
    }
    public ArrayList<String> getAllData(String tableName,String id)
    {
        ArrayList<String> professionsList = new ArrayList<String>();
        professionsList.add("Your Previous Data");
        String sql = "SELECT * from "+tableName+" where logid='"+id+"'";
        Cursor resultSet = null;
        try {
            resultSet = dbMgr.getRecords(sql);

            if(resultSet != null)
            {
                resultSet.moveToFirst();
                while (!resultSet.isAfterLast()){
                    String profession= resultSet.getString(1);
                    professionsList.add(profession);
                    resultSet.moveToNext();
                }
            }

        }catch (Exception e)
        {
            Log.e(TAG, "getAllContacts : " + e.toString());
        }

        return  professionsList;
    }
    public ArrayList<String> getAllCompanies(String id)
    {
        ArrayList<String> professionsList = new ArrayList<String>();
        String sql = "SELECT * from work where logid='"+id+"'";
        Cursor resultSet = null;
        try {
            resultSet = dbMgr.getRecords(sql);

            if(resultSet != null)
            {
                resultSet.moveToFirst();
                while (!resultSet.isAfterLast()){
                    String profession= resultSet.getString(1);
                    professionsList.add(profession);
                    resultSet.moveToNext();
                }
            }

        }catch (Exception e)
        {
            Log.e(TAG, "getAllContacts : " + e.toString());
        }

        return  professionsList;
    }
    public JSONArray getProjectsData(String id)
    {
        JSONArray jsonObject=new JSONArray();
        String sql = "SELECT * from projects where companyname='"+id+"'";
        Cursor resultSet = null;
        try {
            resultSet = dbMgr.getRecords(sql);

            if(resultSet != null)
            {
                resultSet.moveToFirst();
                while (!resultSet.isAfterLast()){
                    JSONObject service=new JSONObject();
                    String companyname=resultSet.getString(1);
                    service.put("companyname",companyname);
                    service.put("description",resultSet.getString(2));
                    service.put("fromdate",resultSet.getString(3));
                    service.put("todate",resultSet.getString(4));
                    service.put("technologies",resultSet.getString(5));
                    service.put("projectname",resultSet.getString(6));
                    jsonObject.put(service);
                    resultSet.moveToNext();                }
            }

        }catch (Exception e)
        {
            Log.e(TAG, "getAllContacts : " + e.toString());
        }

        return  jsonObject;
    }
    public Cursor getAllStoredData(String tableName,String fieldName,String fieldValue,String id)
    {
        JSONArray jsonObject=new JSONArray();
        String sql = "SELECT * from "+tableName+" where logid='"+id+"' and "+fieldName+"='"+fieldValue+"'";
        Cursor resultSet = null;
        try {
            resultSet = dbMgr.getRecords(sql);

        }catch (Exception e)
        {
            Log.e(TAG, "getAllContacts : " + e.toString());
        }

        return  resultSet;
    }
    public JSONArray getWorkData(String id)
    {
        JSONArray jsonObject=new JSONArray();
        String sql = "SELECT * from work where logid='"+id+"'";
        Cursor resultSet = null;
        try {
            resultSet = dbMgr.getRecords(sql);

            if(resultSet != null)
            {
                resultSet.moveToFirst();

                while (!resultSet.isAfterLast()){
                    JSONObject service=new JSONObject();
                    String companyname=resultSet.getString(1);
                    service.put("companyname",companyname);
                    service.put("description",resultSet.getString(2));
                    service.put("fromdate",resultSet.getString(3));
                    service.put("todate",resultSet.getString(4));
                    service.put("profession",resultSet.getString(5));
                    service.put("projects",getProjectsData(companyname));
                    jsonObject.put(service);
                    resultSet.moveToNext();                }
            }

        }catch (Exception e)
        {
            Log.e(TAG, "getAllContacts : " + e.toString());
        }

        return  jsonObject;
    }
    public JSONArray getEducationData(String id)
    {
        JSONArray jsonObject=new JSONArray();
        String sql = "SELECT * from education where logid='"+id+"'";
        Cursor resultSet = null;
        try {
            resultSet = dbMgr.getRecords(sql);

            if(resultSet != null)
            {
                resultSet.moveToFirst();
                while (!resultSet.isAfterLast()){
                    JSONObject service=new JSONObject();
                    service.put("educationtype",resultSet.getString(1));
                    service.put("organisation",resultSet.getString(2));
                    service.put("description",resultSet.getString(3));
                    service.put("fromdate",resultSet.getString(4));
                    service.put("todate",resultSet.getString(5));
                    jsonObject.put(service);
                    resultSet.moveToNext();
                }
            }

        }catch (Exception e)
        {
            Log.e(TAG, "getAllContacts : " + e.toString());
        }

        return  jsonObject;
    }
    public JSONArray getSkillsetData(String id)
    {
        JSONArray jsonObject=new JSONArray();
        String sql = "SELECT * from skillset where logid='"+id+"'";
        Cursor resultSet = null;
        try {
            resultSet = dbMgr.getRecords(sql);

            if(resultSet != null)
            {
                resultSet.moveToFirst();
                while (!resultSet.isAfterLast()){
                    JSONObject service=new JSONObject();
                    service.put("skillname",resultSet.getString(1));
                    service.put("skilllevel",resultSet.getString(2));
                    service.put("description",resultSet.getString(3));
                    jsonObject.put(service);
                    resultSet.moveToNext();                }
            }

        }catch (Exception e)
        {
            Log.e(TAG, "getAllContacts : " + e.toString());
        }

        return  jsonObject;
    }
    public JSONArray getLocationData(String id)
    {
        JSONArray jsonObject=new JSONArray();
        String sql = "SELECT * from location where logid='"+id+"'";
        Cursor resultSet = null;
        try {
            resultSet = dbMgr.getRecords(sql);

            if(resultSet != null)
            {
                resultSet.moveToFirst();
                while (!resultSet.isAfterLast()){
                    JSONObject service=new JSONObject();
                    service.put("locationname",resultSet.getString(1));
                    jsonObject.put(service);
                    resultSet.moveToNext();
                }
            }

        }catch (Exception e)
        {
            Log.e(TAG, "getAllContacts : " + e.toString());
        }

        return  jsonObject;
    }
    public ArrayList<ChatMessage> getMessagesData(String id)
    {
        ArrayList<ChatMessage> listchat=new ArrayList<>();
        String sql = "SELECT * from messages where logid='"+id+"' ORDER By datatime ASC";
        Cursor resultSet = null;
        try {
            resultSet = dbMgr.getRecords(sql);

            if(resultSet != null)
            {
                resultSet.moveToFirst();
                while (!resultSet.isAfterLast()){
                    ChatMessage msg1=new ChatMessage();
                    if(resultSet.getString(2).equals("me")) {
                        //ChatMessage msg1 = new ChatMessage();
                        msg1.setId(1);
                        msg1.setMe(true);
                        msg1.setMessage(resultSet.getString(3));
                        msg1.setDate(resultSet.getString(4));
                    }else{
                        //ChatMessage msg1 = new ChatMessage();
                        msg1.setId(2);
                        msg1.setMe(false);
                        msg1.setMessage(resultSet.getString(3));
                        msg1.setDate(resultSet.getString(4));
                    }
                    listchat.add(msg1);
                    //JSONObject service=new JSONObject();
                    //service.put("from",resultSet.getString(1));
                    //service.put("to",resultSet.getString(2));
                    //service.put("message",resultSet.getString(3));
                    //jsonObject.put(service);
                    resultSet.moveToNext();
                }
            }

        }catch (Exception e)
        {
            Log.e(TAG, "getAllContacts : " + e.toString());
        }

        return  listchat;
    }
    public JSONArray getYourServiceData(String id)
    {
        JSONArray jsonObject=new JSONArray();
        String sql = "SELECT * from yourservice where logid='"+id+"'";
        Cursor resultSet = null;
        try {
            resultSet = dbMgr.getRecords(sql);

            if(resultSet != null)
            {
                resultSet.moveToFirst();
                while (!resultSet.isAfterLast()){
                    JSONObject service=new JSONObject();
                    service.put("servicetype",resultSet.getString(1));
                    service.put("servicename",resultSet.getString(2));
                    service.put("description",resultSet.getString(3));
                    service.put("noofcourse",resultSet.getString(4));
                    service.put("noofprojects",resultSet.getString(5));
                    service.put("noofclients",resultSet.getString(6));
                    jsonObject.put(service);
                    resultSet.moveToNext();
                }
            }

        }catch (Exception e)
        {
            Log.e(TAG, "getAllContacts : " + e.toString());
        }

        return  jsonObject;
    }
    public JSONObject getPersonalData(String id)
    {
        JSONObject jsonObject=new JSONObject();
        String sql = "SELECT * from ProfessionDetails where logid='"+id+"'";
        Cursor resultSet = null;
        try {
            resultSet = dbMgr.getRecords(sql);

            if(resultSet != null)
            {
                resultSet.moveToFirst();
                while (!resultSet.isAfterLast()){
                    jsonObject.put("fullname",resultSet.getString(1));
                    jsonObject.put("description",resultSet.getString(2));
                    jsonObject.put("profession",resultSet.getString(3));
                    jsonObject.put("highlights",resultSet.getString(4));
                    jsonObject.put("dob",resultSet.getString(5));
                    jsonObject.put("gender",resultSet.getString(6));
                    jsonObject.put("levelprofile",resultSet.getString(7));
                    //waypointList.add(wp);
                    resultSet.moveToNext();
                }
            }

        }catch (Exception e)
        {
            Log.e(TAG, "getAllContacts : " + e.toString());
        }

        return  jsonObject;
    }
    public ArrayList<String> getskillSet()
    {
        ArrayList<String> skills=new ArrayList<>();
        skills.add("Your Skills");
        String sql = "SELECT * from skills";
        Cursor resultSet = null;
        try {
            resultSet = dbMgr.getRecords(sql);
            if(resultSet != null)
            {
                resultSet.moveToFirst();
                while (!resultSet.isAfterLast()){
                    skills.add(resultSet.getString(1));
                    resultSet.moveToNext();
                }
            }
        }catch (Exception e)
        {
            Log.e(TAG, "getAllContacts : " + e.toString());
        }

        return  skills;
    }



}
