package com.example.bealert.ui.commuteAlert;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bealert.Main2Activity;
import com.example.bealert.databinding.FragmentMyLocationsBinding;
import com.example.bealert.shareViewModel;

import java.util.ArrayList;


public class MyLocationsFragment extends Fragment {

    ListView favouritesList;
    private FragmentMyLocationsBinding binding;
    shareViewModel shareviewmodel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentMyLocationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        favouritesList = binding.favList;

        shareviewmodel = new ViewModelProvider(requireActivity()).get(shareViewModel.class);


            shareviewmodel.loadData((Main2Activity) requireActivity());
        ArrayList  adapter = new ArrayList();
        adapter.addAll(shareviewmodel.favouritesList);


        ArrayAdapter favouritesAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,shareviewmodel.favouritesList);
        favouritesList.setAdapter(favouritesAdapter);


        return root;
    }
}