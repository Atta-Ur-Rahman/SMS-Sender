package com.example.mycontacts.ui.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycontacts.R;
import com.example.mycontacts.adapters.ContactAdapter;
import com.example.mycontacts.dataBase.ContactCurd;
import com.example.mycontacts.ui.fragments.AddContactFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class GroupNameSetActivity extends AppCompatActivity implements View.OnClickListener {
    private Boolean aBoolean = true;
    @BindView(R.id.civCreateGroup)
    TextView civCreateGroup;
    @BindView(R.id.etGroupName)
    EditText etGroupName;
    String strGroupName;
    ContactCurd contactCurd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_name_set);
        ButterKnife.bind(this);
        contactCurd = new ContactCurd(this);
        civCreateGroup.setOnClickListener(this);
        customActionBar();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.civCreateGroup:

                strGroupName = etGroupName.getText().toString();

                if (strGroupName.equals("")) {
                    Toast.makeText(this, "Enter Group Name", Toast.LENGTH_SHORT).show();
                } else {

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
                }
                break;
        }
    }

    public void customActionBar() {

        ActionBar mActionBar = ((AppCompatActivity) GroupNameSetActivity.this).getSupportActionBar();

        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(false);
        mActionBar.setElevation(0);

        mActionBar.hide();

    }
}
