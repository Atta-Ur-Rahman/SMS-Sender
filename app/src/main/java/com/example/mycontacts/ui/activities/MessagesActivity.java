package com.example.mycontacts.ui.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycontacts.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessagesActivity extends AppCompatActivity {

    private boolean valid = false;
    SharedPreferences sharedPreferences;
    private String msg;
    @BindView(R.id.sentMessage)
    TextView sendMessage;
    @BindView(R.id.edit_Text)
    EditText msgText;

    WindowManager.LayoutParams layoutparams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        ButterKnife.bind(this);



//        int titleId = getResources().getIdentifier("action_bar_title", "id", getPackageName());
//        TextView abTitle = (TextView) findViewById(titleId);
//        abTitle.setTextColor(getResources().getColor(R.color.blackLight));



        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = getSharedPreferences("abcdef", Context.MODE_PRIVATE);
                String number = sharedPreferences.getString("iddd", "");
                sentdSMS(number, msg);
                isValid();
            }
        });

        customActionBar();
    }

    private boolean isValid() {
        valid = true;
        msg = msgText.getText().toString();

        if (msg.isEmpty()) {

            msgText.setError("ERROR");
        } else {

            msgText.setError(null);
        }
        return valid;
    }

    private void sentdSMS(String phone, String msg) {

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            try {
                SmsManager smsMgrVar = SmsManager.getDefault();
                smsMgrVar.sendTextMessage(phone, null, msg, null, null);
                Toast.makeText(getApplicationContext(), "Message Sent",
                        Toast.LENGTH_LONG).show();
            } catch (Exception ErrVar) {
                Toast.makeText(getApplicationContext(), ErrVar.getMessage().toString(),
                        Toast.LENGTH_LONG).show();
                ErrVar.printStackTrace();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 10);
            }
        }
    }

    public void customActionBar() {
        sharedPreferences = getSharedPreferences("contactName", Context.MODE_PRIVATE);
        String contactName = sharedPreferences.getString("ida", "");

        ActionBar mActionBar = ((AppCompatActivity) MessagesActivity.this).getSupportActionBar();




//        TextView textview = new TextView(MessagesActivity.this);
//
//        layoutparams = new RelativeLayout.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
//
//        textview.setLayoutParams(layoutparams);
//
//        textview.setText("Action Bar Title Text");
//
//        textview.setGravity(Gravity.CENTER);
//
//        textview.setTextColor(Color.parseColor("#fc6902"));
//
//        textview.setTextSize(25);
//
//
//        actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//
//        actionbar.setCustomView(textview);

        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(false);
        mActionBar.setTitle(contactName);
        mActionBar.setElevation(0);

        mActionBar.show();

    }
}

