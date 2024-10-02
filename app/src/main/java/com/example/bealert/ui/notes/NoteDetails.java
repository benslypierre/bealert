package com.example.bealert.ui.notes;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.bealert.R;
import com.example.bealert.shareViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileReader;

public class NoteDetails extends Fragment {

    shareViewModel  shareviewmodel;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        shareviewmodel = new ViewModelProvider(requireActivity()).get(shareViewModel.class);

        //setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        TextView content = view.findViewById(R.id.noteDetailsContent);
        TextView title = view.findViewById(R.id.noteDetailsTitle);
        content.setMovementMethod(new ScrollingMovementMethod());
        String id = shareviewmodel.noteId;

        content.setText(shareviewmodel.editNote.getContent());
        title.setText(shareviewmodel.editNote.getTitle());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            content.setBackgroundColor(getResources().getColor(shareviewmodel.code,null));
        }


        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                shareviewmodel.editnote(id,title.getText().toString(), content.getText().toString());

                Navigation.findNavController(getView()).navigate(R.id.nav_to_editNote);


            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
