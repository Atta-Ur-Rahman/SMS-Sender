package com.example.mycontacts.ui.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.Toast;

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
import de.hdodenhof.circleimageview.CircleImageView;

public class NewMemberActivity extends AppCompatActivity implements View.OnClickListener {

    public static Boolean aBoolean = true;
    private List<ContactDataModel> modelList;
    private ArrayAdapter<String> arrayAdapter;
    Cursor cursor;
    private String name, phoneNumber;
    public static final int RequestPermissionCode = 1;
    private View view;
    public static ContactAdapter adapter;
    @BindView(R.id.recyclerViewnewMember)
    RecyclerView recyclerViewContact;
    @BindView(R.id.addNewMemberCiv)
    CircleImageView addNewMemberCiv;
    List<AllContactDataModel> allContactDataModels = new ArrayList<>();
    ContactCurd contactCurd;
    public static boolean aBooleanResfreshAdapter = false;
    public static int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_member);

        contactCurd = new ContactCurd(this);
        initListeners();
        Toast.makeText(this, String.valueOf(allContactDataModels.size()), Toast.LENGTH_SHORT).show();
        initUI();
        OnResumFunction();

            GetContactsIntoArrayList();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    private void initListeners() {
        ButterKnife.bind(this);

    }

    private void initUI() {
        addNewMemberCiv.setOnClickListener(this);
        modelList = new ArrayList<>();
        recyclerViewContact.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewContact.setHasFixedSize(true);

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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addNewMemberCiv:

                 SharedPreferences sharedPreferences = getSharedPreferences("abcd", Context.MODE_PRIVATE);
                String strGroupName = sharedPreferences.getString("ide", "");



                if (strGroupName.equals("")) {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                } else {

                    AddContactFragment.aBooleanResfreshAdapter = true;

                    for (int i = 0; i < ContactAdapter.stringArrayListContactNumber.size(); i++) {

                        String strContactName = ContactAdapter.stringArrayListContactName.get(i);
                        String strContactNumber = ContactAdapter.stringArrayListContactNumber.get(i);
                        contactCurd.insertContact(this, strGroupName, strContactName, strContactNumber);
                        Log.d("insertContact", strContactName);
                        Log.d("insertContact", strGroupName);
                        if (i == ContactAdapter.integersArrayListSelectedContactPosition.size()) {
                            finish();
                            Intent intent = new Intent(NewMemberActivity.this,GroupContactActivity.class);
                            startActivity(intent);
                            Toast.makeText(this, strContactName, Toast.LENGTH_SHORT).show();
                        }
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
