package com.example.mycontacts.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycontacts.R;
import com.example.mycontacts.dataBase.ContactCurd;
import com.example.mycontacts.dataModel.GroupDataModel;
import com.example.mycontacts.ui.activities.GroupContactActivity;
import com.example.mycontacts.ui.activities.GroupMessageActivity;
import com.example.mycontacts.ui.activities.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.myViewHolder> {
    public List<GroupDataModel> contactListFilter;
    public List<GroupDataModel> contactList;

    Context context;
    ContactCurd contactCurd;


    public GroupAdapter(Context context, List<GroupDataModel> myValues) {
        this.contactListFilter = myValues;
        this.contactList = myValues;
        this.context = context;
    }

    @Override
    public GroupAdapter.myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listitem = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_group, parent, false);
        return new myViewHolder(listitem);
    }

    @Override
    public void onBindViewHolder(GroupAdapter.myViewHolder holder, int position) {
        contactCurd = new ContactCurd(context);
        GroupDataModel groupDataModel = contactListFilter.get(position);

        String strgroup = contactListFilter.get(position).getGroupName();
        Log.d("itemnumber", "   " + contactCurd.getItemCount(strgroup));


        holder.tvGroupName.setText(contactListFilter.get(position).getGroupName());
        holder.tvGroupNum.setText(String.valueOf(contactCurd.getItemCount(strgroup)));

        holder.cardViewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("abcd", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("ide", groupDataModel.getGroupName());
                editor.apply();
                context.startActivity(new Intent(context, GroupMessageActivity.class));

            }
        });
        holder.cardViewGroup.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                String[] str = {"Delete Group", "Group Contact"};
                final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("Group");
                dialog.setItems(str, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {

                            if (contactCurd.deleteGroup(strgroup)) {
                                ((Activity) context).finish();
                                ((Activity) context).startActivity(new Intent(context, MainActivity.class));
                            }

                        } else if (which == 1) {

                            SharedPreferences sharedPreferences = context.getSharedPreferences("abcd", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("ide", groupDataModel.getGroupName());
                            editor.apply();
                            context.startActivity(new Intent(context, GroupContactActivity.class));
                        } else {
                            Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();


                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return contactListFilter.size();
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFilter = contactList;
                } else {
                    List<GroupDataModel> filteredList = new ArrayList<>();
                    for (GroupDataModel row : contactListFilter) {

//                        Toast.makeText(context, "charString else", Toast.LENGTH_SHORT).show();

                        if (row.getGroupName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    contactListFilter = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFilter = (ArrayList<GroupDataModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };

    }


    public static class myViewHolder extends RecyclerView.ViewHolder {
        private TextView tvGroupName, tvGroupNum;
        private CardView cardViewGroup;

        public myViewHolder(View itemView) {
            super(itemView);
            cardViewGroup = itemView.findViewById(R.id.cardViewGroup);
            tvGroupName = itemView.findViewById(R.id.tvGroupName);
            tvGroupNum = itemView.findViewById(R.id.groupNum);
        }
    }
}
