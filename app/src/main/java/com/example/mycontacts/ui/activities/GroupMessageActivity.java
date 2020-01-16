package com.example.mycontacts.ui.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.mycontacts.R;
import com.example.mycontacts.dataBase.ContactCurd;
import com.example.mycontacts.dataModel.GroupDataModel;
import com.example.mycontacts.utils.AlertUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupMessageActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    private boolean validGroup = false;
    private String msgGroup;
    @BindView(R.id.sentMessageGroup)
    TextView sendMessageGroup;
    @BindView(R.id.edit_TextGroup)
    EditText msgTextGroup;
    ContactCurd contactCurd;
    List<GroupDataModel> groupDataModels = new ArrayList<>();

    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_message);
        ButterKnife.bind(this);
        contactCurd = new ContactCurd(this);
        dialog = AlertUtils.createProgressDialog(this);

        sharedPreferences = getSharedPreferences("abcd", Context.MODE_PRIVATE);
        String groupName = sharedPreferences.getString("ide", "");

        groupDataModels = contactCurd.getGroupContact(groupName, this);


        sendMessageGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isValidGroup()) {
                    sentdSMSGroup(msgGroup);
                }
            }
        });
        customActionBar();
    }

    private boolean isValidGroup() {
        validGroup = true;
        msgGroup = msgTextGroup.getText().toString();
        if (msgGroup.isEmpty()) {
            msgTextGroup.setError("ERROR");
        } else {
            msgTextGroup.setError(null);
        }
        return validGroup;
    }

    private void sentdSMSGroup(String msg) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            try {
                new SendMsg(this).execute(msg);
            } catch (Exception ErrVar) {
                Toast.makeText(getApplicationContext(), "ERROR",
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
        sharedPreferences = getSharedPreferences("abcd", Context.MODE_PRIVATE);
        String groupName = sharedPreferences.getString("ide", "");

        ActionBar mActionBar = GroupMessageActivity.this.getSupportActionBar();
        assert mActionBar != null;
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle(groupName);

        mActionBar.show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.deleteGroupMenu:


                AlertDialog alertDialog = new AlertDialog.Builder(GroupMessageActivity.this).create();
                alertDialog.setTitle("Delete!");
                alertDialog.setMessage("Are you sure, You wanted to delete this group");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Delete",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                sharedPreferences = getSharedPreferences("abcd", Context.MODE_PRIVATE);
                                String groupName = sharedPreferences.getString("ide", "");
                                contactCurd.deleteGroup(groupName);
                                finish();
                            }
                        });
                alertDialog.show();


                break;
            case R.id.contactsMenu:
                Intent i = new Intent(GroupMessageActivity.this, GroupContactActivity.class);
                startActivity(i);
                break;

            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }


    @SuppressLint("StaticFieldLeak")
    private class SendMsg extends AsyncTask<String, String, String> {
        private Context context;

        public SendMsg(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub

            SmsManager smsManager = SmsManager.getDefault();

            String SENT = "SMS_SENT";
            String DELIVERED = "SMS_DELIVERED";

            SmsManager sms = SmsManager.getDefault();
            ArrayList<String> parts = sms.divideMessage(msgGroup);
            int messageCount = parts.size();


            Log.i("Message Count", "Message Count: " + messageCount);

            ArrayList<PendingIntent> deliveryIntents = new ArrayList<PendingIntent>();
            ArrayList<PendingIntent> sentIntents = new ArrayList<PendingIntent>();


            PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, new Intent(SENT), 0);
            PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0, new Intent(DELIVERED), 0);

            for (int j = 0; j < messageCount; j++) {
                sentIntents.add(sentPI);
                deliveryIntents.add(deliveredPI);
            }

            for (int i = 0; i < groupDataModels.size(); i++) {
                String phoneNumber = groupDataModels.get(i).getGroupContactNum();
                Log.d("contact", phoneNumber);


                registerReceiver(new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context arg0, Intent arg1) {

                        switch (getResultCode()) {
                            case Activity.RESULT_OK:
                                Toast.makeText(getBaseContext(), "SMS sent",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                                Toast.makeText(getBaseContext(), "Generic failure",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case SmsManager.RESULT_ERROR_NO_SERVICE:
                                Toast.makeText(getBaseContext(), "No service",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case SmsManager.RESULT_ERROR_NULL_PDU:
                                Toast.makeText(getBaseContext(), "Null PDU",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case SmsManager.RESULT_ERROR_RADIO_OFF:
                                Toast.makeText(getBaseContext(), "Radio off",
                                        Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }, new IntentFilter(SENT));

                registerReceiver(new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context arg0, Intent arg1) {
                        switch (getResultCode()) {

                            case Activity.RESULT_OK:
                                Toast.makeText(getBaseContext(), "SMS delivered",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case Activity.RESULT_CANCELED:
                                Toast.makeText(getBaseContext(), "SMS not delivered",
                                        Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }, new IntentFilter(DELIVERED));

//        smsManager.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
                sms.sendMultipartTextMessage(phoneNumber, null, parts, sentIntents, deliveryIntents);


            }


            return null;
        }

        @Override
        protected void onPostExecute(String args) {


        }
    }


}


