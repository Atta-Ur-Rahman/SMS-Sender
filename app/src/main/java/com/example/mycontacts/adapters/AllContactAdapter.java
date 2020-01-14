package com.example.mycontacts.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycontacts.R;
import com.example.mycontacts.dataModel.AllContactDataModel;
import com.example.mycontacts.ui.activities.MessagesActivity;

import java.util.ArrayList;
import java.util.List;

public class AllContactAdapter extends RecyclerView.Adapter<AllContactAdapter.MyViewHolder> {
    private boolean isColor = true;
    private FragmentActivity contact;
    private List<AllContactDataModel> allContactDataModels;
    private List<AllContactDataModel> contactDataModels;


    public static ArrayList<Integer> integersArrayListSelectedContactPosition = new ArrayList<>();
    public static ArrayList<String> stringArrayListContactName = new ArrayList<>();
    public static ArrayList<String> stringArrayListContactNumber = new ArrayList<>();


    public AllContactAdapter(FragmentActivity contact, List<AllContactDataModel> modelList) {
        this.contact = contact;
        this.contactDataModels = modelList;
        this.allContactDataModels = modelList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(contact);
        View view = layoutInflater.inflate(R.layout.card_view_contact, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AllContactDataModel contactDataModel = allContactDataModels.get(position);

        holder.nameContact.setText(contactDataModel.getStrName());
        holder.numberContact.setText(contactDataModel.getStrNumber());
//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                contact.startActivity(new Intent(contact, MessagesActivity.class));
//
//                SharedPreferences sharedPreferences = contact.getSharedPreferences("abc", Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString("id", contactDataModel.getStrNumber());
//                editor.apply();
//
//                SharedPreferences sharedPreferences1 = contact.getSharedPreferences("contactName", Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor1 = sharedPreferences1.edit();
//                editor1.putString("ida", contactDataModel.getStrName());
//                editor1.apply();
//            }
//
//        });
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stringArrayListContactNumber.contains(contactDataModel.getStrNumber())) {

                    holder.imageView.setBackgroundResource(R.drawable.round_ract_border);
                    stringArrayListContactName.remove(contactDataModel.getStrName());
                    stringArrayListContactNumber.remove(contactDataModel.getStrNumber());

                    Log.d("contact", "un checked    " + stringArrayListContactNumber);

                } else {
                    holder.imageView.setBackgroundResource(R.color.colorAccent);
                    stringArrayListContactName.add(contactDataModel.getStrName());
                    stringArrayListContactNumber.add(contactDataModel.getStrNumber());

                    Log.d("contact", "checked   " + stringArrayListContactNumber);

                }


            }
        });


        if (stringArrayListContactNumber.contains(contactDataModel.getStrNumber())) {
            holder.imageView.setBackgroundResource(R.color.colorAccent);
        } else {
            holder.imageView.setBackgroundResource(R.drawable.round_ract_border);


        }


    }


    @Override
    public int getItemCount() {
        return allContactDataModels.size();
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    allContactDataModels = contactDataModels;
                } else {
                    List<AllContactDataModel> filteredList = new ArrayList<>();
                    for (AllContactDataModel row : allContactDataModels) {

//                        Toast.makeText(context, "charString else", Toast.LENGTH_SHORT).show();

                        if (row.getStrName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    allContactDataModels = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = allContactDataModels;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                allContactDataModels = (ArrayList<AllContactDataModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameContact;
        TextView numberContact;
        ImageView imageView;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            numberContact = itemView.findViewById(R.id.contactNum);
            nameContact = itemView.findViewById(R.id.contactName);
            imageView = itemView.findViewById(R.id.clickImage);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
