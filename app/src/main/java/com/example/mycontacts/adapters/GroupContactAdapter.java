package com.example.mycontacts.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycontacts.R;
import com.example.mycontacts.dataModel.ContactDataModel;
import com.example.mycontacts.dataModel.GroupDataModel;
import com.example.mycontacts.ui.activities.MessagesActivity;

import java.util.List;

public class GroupContactAdapter extends RecyclerView.Adapter<GroupContactAdapter.MyViewHolder> {
    public List<GroupDataModel> groupDataModelsContact;
    Context context;

    public GroupContactAdapter(Context context, List<GroupDataModel> myValues) {
        this.groupDataModelsContact = myValues;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listitem = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_contact, parent, false);
        return new GroupContactAdapter.MyViewHolder(listitem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        GroupDataModel groupDataModel = groupDataModelsContact.get(position);
        holder.tvGroupName.setText(groupDataModel.getGroupContactName());
        holder.tvGroupNum.setText(groupDataModel.getGroupContactNum());
        holder.cardViewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, MessagesActivity.class));

                SharedPreferences sharedPreferences = context.getSharedPreferences("abcdef", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("iddd", groupDataModel.getGroupContactNum());
                editor.apply();

                SharedPreferences sharedPreferences1 = context.getSharedPreferences("contactName", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                editor1.putString("ida", groupDataModel.getGroupContactName());
                editor1.apply();
            }
        });


    }

    @Override
    public int getItemCount() {
        return groupDataModelsContact.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvGroupName, tvGroupNum;
        private CardView cardViewGroup;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardViewGroup = itemView.findViewById(R.id.cardViewContactGroup);
            tvGroupName = itemView.findViewById(R.id.contactNameGroup);
            tvGroupNum = itemView.findViewById(R.id.contactNumGroup);

        }
    }
}
