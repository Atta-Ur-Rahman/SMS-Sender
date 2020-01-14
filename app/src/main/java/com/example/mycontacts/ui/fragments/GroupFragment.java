package com.example.mycontacts.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycontacts.R;
import com.example.mycontacts.adapters.GroupAdapter;
import com.example.mycontacts.dataBase.ContactCurd;
import com.example.mycontacts.dataModel.GroupDataModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupFragment extends Fragment {


    public static List<GroupDataModel> groupDataModelArrayList = new ArrayList<>();
    public static GroupAdapter adapterGroup;
    @BindView(R.id.recyclerViewGroup)
    RecyclerView recyclerViewGroup;
    private View view;
    ContactCurd contactCurd;

    @BindView(R.id.tvNoGroupFound)
    TextView tvNoGroupFound;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_group, container, false);
        contactCurd = new ContactCurd(getActivity());
        ButterKnife.bind(this, view);
        recyclerViewGroup.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewGroup.setHasFixedSize(true);

        customActionBar();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        intiUi();

    }

    private void intiUi() {

        groupDataModelArrayList = contactCurd.GetAllGroupName();
        adapterGroup = new GroupAdapter(getActivity(), groupDataModelArrayList);
        recyclerViewGroup.setAdapter(adapterGroup);
        adapterGroup.notifyDataSetChanged();


        if (groupDataModelArrayList.size() <= 0) {
            tvNoGroupFound.setVisibility(View.VISIBLE);
        } else {
            tvNoGroupFound.setVisibility(View.GONE);

        }

    }


    public void customActionBar() {

        ActionBar mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(false);
        mActionBar.setElevation(0);

        mActionBar.hide();

    }
}
