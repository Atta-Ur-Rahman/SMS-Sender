package com.example.mycontacts.ui.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycontacts.R;
import com.example.mycontacts.adapters.ContactAdapter;
import com.example.mycontacts.dataBase.ContactCurd;
import com.example.mycontacts.dataModel.AllContactDataModel;
import com.example.mycontacts.dataModel.ContactDataModel;
import com.example.mycontacts.ui.fragments.AddContactFragment;
import com.example.mycontacts.utils.PermissionUtills;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddNewMemberFromContactActivity extends AppCompatActivity implements View.OnClickListener {


    public static Boolean aBoolean = true;
    private List<ContactDataModel> modelList;
    private ArrayAdapter<String> arrayAdapter;
    Cursor cursor;
    private String name, phoneNumber;
    public static final int RequestPermissionCode = 1;
    public static ContactAdapter adapter;
    @BindView(R.id.recyclerViewContact)
    RecyclerView recyclerViewContact;
    @BindView(R.id.fbtnAddNewMember)
    FloatingActionButton fbtnAddNewMember;
    List<AllContactDataModel> allContactDataModels = new ArrayList<>();
    ContactCurd contactCurd;
    public static boolean aBooleanResfreshAdapter = false;
    public static int REQUEST_CODE = 1;
    String strGroupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_member_from_contact);

        SharedPreferences sharedPreferences = getSharedPreferences("abcd", Context.MODE_PRIVATE);
        strGroupName = sharedPreferences.getString("ide", "");


        contactCurd = new ContactCurd(this);
        initListeners();
        initUI();
        OnResumFunction();

        if (PermissionUtills.isContactPermissionGranted(this)) {
            GetContactsIntoArrayList();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    private void initListeners() {
        ButterKnife.bind(this);
        fbtnAddNewMember.setOnClickListener(this);

    }

    private void initUI() {
        modelList = new ArrayList<>();
        recyclerViewContact.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewContact.setHasFixedSize(true);

        EnableRuntimePermission();
        customActionBar();

    }

    private void OnResumFunction() {
        allContactDataModels = contactCurd.GetAllContact();
        ContactAdapter.stringArrayListContactName.clear();
        ContactAdapter.stringArrayListContactNumber.clear();
        ContactAdapter.integersArrayListSelectedContactPosition.clear();
        adapter = new ContactAdapter(this, modelList);
        recyclerViewContact.setAdapter(adapter);

    }


    public void customActionBar() {

        ActionBar mActionBar = ((AppCompatActivity) this).getSupportActionBar();

        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(false);
        mActionBar.setElevation(0);
        mActionBar.hide();

    }


    private void GetContactsIntoArrayList() {

        cursor = this.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        while (cursor.moveToNext()) {

            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            ContactDataModel number = new ContactDataModel();
            number.setNameContact(name);
            number.setNumContact(phoneNumber);
            modelList.add(number);
            adapter.notifyDataSetChanged();
        }

        cursor.close();

    }

    private void EnableRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_CONTACTS)) {

//            Toast.makeText(getActivity(), "CONTACTS permission allows us to Access CONTACTS app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_CONTACTS}, RequestPermissionCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

//                    Toast.makeText(getActivity(), "Permission Granted, Now your application can access CONTACTS.", Toast.LENGTH_LONG).show();

                } else {

//                    Toast.makeText(getActivity(), "Permission Canceled, Now your application cannot access CONTACTS.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fbtnAddNewMember:


                AddContactFragment.aBooleanResfreshAdapter = true;
                contactCurd.insertGroupName(strGroupName, this);

                for (int i = 0; i < ContactAdapter.stringArrayListContactNumber.size(); i++) {

                    String strContactName = ContactAdapter.stringArrayListContactName.get(i);
                    String strContactNumber = ContactAdapter.stringArrayListContactNumber.get(i);
                    contactCurd.insertContact(this, strGroupName, strContactName, strContactNumber);
                    Log.d("insertContact", strContactName);
                    Log.d("insertContact", strGroupName);

                    if (i == ContactAdapter.integersArrayListSelectedContactPosition.size()) {
                        finish();
                    }

                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (aBooleanResfreshAdapter) {
            aBooleanResfreshAdapter = false;
            OnResumFunction();
            GetContactsIntoArrayList();

        }
    }
}
