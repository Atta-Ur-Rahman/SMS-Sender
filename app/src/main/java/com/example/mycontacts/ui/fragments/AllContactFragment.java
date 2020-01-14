package com.example.mycontacts.ui.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mycontacts.R;
import com.example.mycontacts.dataBase.ContactCurd;
import com.example.mycontacts.dataModel.AllContactDataModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class AllContactFragment extends Fragment {
    View parentView;
    List<AllContactDataModel> allContactDataModels = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_all_contact, container, false);
        ButterKnife.bind(this.parentView);
        ContactCurd contactCurd = new ContactCurd(getActivity());

        allContactDataModels = contactCurd.GetAllContact();


        return parentView;
    }

}
