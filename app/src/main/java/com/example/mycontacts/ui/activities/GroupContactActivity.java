package com.example.mycontacts.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycontacts.R;
import com.example.mycontacts.adapters.GroupContactAdapter;
import com.example.mycontacts.dataBase.ContactCurd;
import com.example.mycontacts.dataModel.GroupDataModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupContactActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    GroupContactAdapter adapter;
    @BindView(R.id.recyclerViewGroupContact)
    RecyclerView recyclerViewGroupContact;
    ContactCurd contactCurd;
    List<GroupDataModel> groupDataModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_contact);
        ButterKnife.bind(this);
        contactCurd = new ContactCurd(this);
        sharedPreferences = getSharedPreferences("abcd", Context.MODE_PRIVATE);
        recyclerViewGroupContact.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewGroupContact.setHasFixedSize(true);



        customActionBar();

    }

    public void customActionBar() {
        sharedPreferences = getSharedPreferences("abcd", Context.MODE_PRIVATE);
        String groupName = sharedPreferences.getString("ide", "");

        ActionBar mActionBar = ((AppCompatActivity) GroupContactActivity.this).getSupportActionBar();

        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(false);
        mActionBar.setTitle(groupName);
        mActionBar.setElevation(0);

        mActionBar.show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_contact_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.add_new_member:

                Intent intent = new Intent(GroupContactActivity.this, CustomNewMemberActivity.class);
                startActivity(intent);
                break;

        }

        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        String groupName = sharedPreferences.getString("ide", "");
        groupDataModels = contactCurd.getGroupContact(groupName, this);
        Collections.reverse(groupDataModels);
        adapter = new GroupContactAdapter(this, groupDataModels);

        recyclerViewGroupContact.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        Log.d("contactSize", String.valueOf(groupDataModels));
    }
}
