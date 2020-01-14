package com.example.mycontacts.dataBase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.mycontacts.dataModel.AllContactDataModel;
import com.example.mycontacts.dataModel.GroupDataModel;

import java.util.ArrayList;
import java.util.List;

public class ContactCurd {

    private static SQLiteDatabase database, database1;
    private Context context;

    public ContactCurd(Context context) {
        ContactDataBase dbContactDataBase = new ContactDataBase(context);
        database = dbContactDataBase.getWritableDatabase();
        this.context = context;
    }

    public void insertGroupName(String strGroupName, Context context) {

        Cursor cursor = this.database.rawQuery("SELECT * FROM GROUP_TABLE WHERE CONTACT_GROUP_NAME = '" + strGroupName + "' ", null);

        if (cursor.moveToFirst()) {

//            Toast.makeText(context, "Already Exist", Toast.LENGTH_SHORT).show();

        } else {

            ContentValues values = new ContentValues();
            values.put("CONTACT_GROUP_NAME", strGroupName);

            database.insert("GROUP_TABLE", null, values);
            Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show();

        }


    }

    public List<GroupDataModel> GetAllGroupName() {
        List<GroupDataModel> list = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("SELECT * FROM GROUP_TABLE ", null);
        if (cursor.moveToFirst()) {

            if (cursor.moveToFirst()) {
                do {
                    GroupDataModel groupDataModel = new GroupDataModel();
                    String strGroupName = (cursor.getString(cursor.getColumnIndex("CONTACT_GROUP_NAME")));


                    groupDataModel.setGroupName(strGroupName);

                    list.add(groupDataModel);

                    Log.d("groupName", strGroupName);

                } while (cursor.moveToNext());
            }


        } else {
            Toast.makeText(context, "no exist", Toast.LENGTH_SHORT).show();
        }
        return list;
    }

    public void insertContact(Context context, String strGroupName, String strContactName, String strContactNumber) {

        Cursor cursor = this.database.rawQuery("SELECT * FROM CONTACT_TABLE WHERE CONTACT_NUMBER ='" + strContactNumber + "' AND GROUP_NAME= '" + strGroupName + "'", null);

        if (cursor.moveToFirst()) {

            Toast.makeText(context, "Already Exist", Toast.LENGTH_SHORT).show();


        } else {

            ContentValues values = new ContentValues();
            values.put("GROUP_NAME", strGroupName);
            values.put("CONTACT_NAME", strContactName);
            values.put("CONTACT_NUMBER", strContactNumber);

            database.insert("CONTACT_TABLE", null, values);
            Log.d("contactNumber", strContactName);

        }
    }
    public List<AllContactDataModel> GetAllContact() {
        List<AllContactDataModel> list = new ArrayList<>();

        String query = "SELECT * FROM CONTACT_TABLE ";
        Cursor cursor = this.database.rawQuery(query, null);
        if (cursor.moveToFirst()) {

            if (cursor.moveToFirst()) {
                do {
                    AllContactDataModel allContactDataModel = new AllContactDataModel();
                    String strContactName = (cursor.getString(cursor.getColumnIndex("CONTACT_NAME")));
                    String strContactNumber = (cursor.getString(cursor.getColumnIndex("CONTACT_NUMBER")));
                    String strGroupName = (cursor.getString(cursor.getColumnIndex("GROUP_NAME")));

                    allContactDataModel.setStrName(strContactName);
                    allContactDataModel.setStrNumber(strContactNumber);
                    list.add(allContactDataModel);
                    Log.d("contactName", strContactName);
                    Log.d("contactName", strContactNumber);


                } while (cursor.moveToNext());
            }


        } else {
            Toast.makeText(context, "no exist", Toast.LENGTH_SHORT).show();
        }
        return list;
    }
    public List<GroupDataModel> getGroupContact(String strGroupName, Context context) {
        List<GroupDataModel> list = new ArrayList<>();

        String query = "SELECT * FROM CONTACT_TABLE WHERE GROUP_NAME = '" + strGroupName + "'";
        Cursor cursor = this.database.rawQuery(query, null);

        if (cursor.moveToFirst()) {

            do {
                GroupDataModel groupDataModel = new GroupDataModel();
                String strContactName = (cursor.getString(cursor.getColumnIndex("CONTACT_NAME")));
                String strContactNumber = (cursor.getString(cursor.getColumnIndex("CONTACT_NUMBER")));


                groupDataModel.setGroupContactName(strContactName);
                groupDataModel.setGroupContactNum(strContactNumber);

                Log.d("contactcontact", strContactName);


                list.add(groupDataModel);
            } while (cursor.moveToNext());

        }
        return list;
    }

    public boolean deleteGroup(String groupDelete) {

        Cursor cursor = this.database.rawQuery("SELECT * FROM GROUP_TABLE WHERE CONTACT_GROUP_NAME ='" + groupDelete + "'", null);

        if (cursor.moveToFirst()) {
            this.database.delete("GROUP_TABLE", "CONTACT_GROUP_NAME = '" + groupDelete + "'", null);
            String strGroupName = (String.valueOf(cursor.getString(cursor.getColumnIndex("CONTACT_GROUP_NAME"))));

            Toast.makeText(context, "The " + strGroupName + " Group  Delete", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(context, "No Exist", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void deleteContact(String strContactName){
        Cursor cursor = this.database.rawQuery("SELECT * FROM GROUP_TABLE WHERE CONTACT_GROUP_NAME ='" + strContactName + "'", null);

        if (cursor.moveToFirst()) {
            this.database.delete("CONTACT_TABLE", "CONTACT_NAME = '" + strContactName + "'" , null);
            String strContactNamee = (String.valueOf(cursor.getString(cursor.getColumnIndex("CONTACT_NAME"))));

            Toast.makeText(context, "The " + strContactNamee + " Deleted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "No Exist", Toast.LENGTH_SHORT).show();
        }
    }

    public int getItemCount(String strGRoupName) {
        String countQuery = "SELECT  * FROM CONTACT_TABLE WHERE GROUP_NAME = '" + strGRoupName + "' ";
        Cursor cursor = database.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

//        Cursor cursors = database.rawQuery(
//                "SELECT SUM(ADVANCE_PRICE) FROM CONTACT_TABLE WHERE CONTACT_NUMBER  = '"+ strGRoupName + "'", null);
//
//        Log.d("itemnumber",String.valueOf(cursors.getInt(0)));



        return count;
    }



}
