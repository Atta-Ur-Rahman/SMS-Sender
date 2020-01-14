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

import com.example.mycontacts.ui.activities.MessagesActivity;
import com.example.mycontacts.R;

import java.util.ArrayList;
import java.util.List;

import com.example.mycontacts.dataModel.ContactDataModel;

import butterknife.ButterKnife;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> {
    private FragmentActivity contact;
    private List<ContactDataModel> modelList;
    private List<ContactDataModel> modelListss;


    public static ArrayList<Integer> integersArrayListSelectedContactPosition = new ArrayList<>();
    public static ArrayList<String> stringArrayListContactName = new ArrayList<>();
    public static ArrayList<String> stringArrayListContactNumber = new ArrayList<>();


    public ContactAdapter(FragmentActivity contact, List<ContactDataModel> modelList) {
        this.contact = contact;
        this.modelListss = modelList;
        this.modelList = modelList;
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
        ContactDataModel contactDataModel = modelList.get(position);

        holder.nameContact.setText(contactDataModel.getNameContact());
        holder.numberContact.setText(contactDataModel.getNumContact());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stringArrayListContactNumber.contains(contactDataModel.getNumContact())) {

                    holder.imageView.setBackgroundResource(R.drawable.round_ract_border);
                    stringArrayListContactName.remove(contactDataModel.getNameContact());
                    stringArrayListContactNumber.remove(contactDataModel.getNumContact());

                    Log.d("contact", "un checked    " + stringArrayListContactNumber);

                } else {
                    holder.imageView.setBackgroundResource(R.drawable.tickkkk);
                    stringArrayListContactName.add(contactDataModel.getNameContact());
                    stringArrayListContactNumber.add(contactDataModel.getNumContact());
                    Log.d("contact", "checked   " + stringArrayListContactNumber);
                }
            }
        });
        if (stringArrayListContactNumber.contains(contactDataModel.getNumContact())) {
            holder.imageView.setBackgroundResource(R.drawable.tickkkk);
        } else {
            holder.imageView.setBackgroundResource(R.drawable.round_ract_border);


        }


    }


    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    modelList = modelListss;
                } else {
                    List<ContactDataModel> filteredList = new ArrayList<>();
                    for (ContactDataModel row : modelList) {

//                        Toast.makeText(context, "charString else", Toast.LENGTH_SHORT).show();

                        if (row.getNameContact().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    modelList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = modelList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                modelList = (ArrayList<ContactDataModel>) filterResults.values;
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
