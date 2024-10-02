package com.example.bealert.ui.tasks;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bealert.Main2Activity;
import com.example.bealert.R;
import com.example.bealert.adapters.TaskAdapter;
import com.example.bealert.broadcastReceiver.AlarmBroadcastReceiver;
import com.example.bealert.database.DatabaseClient;
import com.example.bealert.databinding.FragmentTaskBinding;
import com.example.bealert.models.Task;
import com.example.bealert.shareViewModel;
import com.example.bealert.ui.bottomSheetFragment.CalendarViewFragment;
import com.example.bealert.ui.bottomSheetFragment.createTaskFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class TaskFragment extends Fragment implements createTaskFragment.setRefreshListener {


    private FragmentTaskBinding binding;
    RecyclerView taskRecycler;
    TaskAdapter taskAdapter;
    List<Task> tasks = new ArrayList<>();
    ImageView noDataImage, calendar;
    FloatingActionButton addTask;
    shareViewModel shareviewmodel;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        shareviewmodel = new ViewModelProvider(requireActivity()).get(shareViewModel.class);


        binding = FragmentTaskBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        taskRecycler = binding.taskRecycler;
        noDataImage = binding.noDataImage;
        addTask =  binding.addTask;
        calendar = binding.calendar;

        setUpAdapter();

        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ComponentName receiver = new ComponentName(getContext(),AlarmBroadcastReceiver.class);
        PackageManager pm = getActivity().getPackageManager();
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        Glide.with(getActivity().getApplicationContext()).load(R.drawable.empty).into(binding.noDataImage);

        addTask.setOnClickListener(view -> {
            createTaskFragment createTaskBottomSheetFragment = new createTaskFragment();
            createTaskBottomSheetFragment.setTaskId(0, false, this, (Main2Activity) getActivity().getParent());
            createTaskBottomSheetFragment.show(getActivity().getSupportFragmentManager(), createTaskBottomSheetFragment.getTag());
        });

        getSavedTasks();

        calendar.setOnClickListener(view -> {
            CalendarViewFragment showCalendarViewBottomSheet = new CalendarViewFragment();
            showCalendarViewBottomSheet.show(getActivity().getSupportFragmentManager(), showCalendarViewBottomSheet.getTag());
        });




        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



    @Override
    public void refresh() {
        getSavedTasks();
    }

    public void setUpAdapter() {
        taskAdapter = new TaskAdapter((Main2Activity) getActivity(), tasks, this);
        taskRecycler.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        taskRecycler.setAdapter(taskAdapter);
    }

    //get tasks from database
    public void getSavedTasks() {

        class GetSavedTasks extends AsyncTask<Void, Void, List<Task>> {
            @Override
            protected List<Task> doInBackground(Void... voids) {
                tasks = DatabaseClient
                        .getInstance(requireContext())
                        .getAppDatabase()
                        .dataBaseAction()
                        .getAllTasksList();
                return tasks;
            }

            @Override
            protected void onPostExecute(List<Task> tasks) {
                super.onPostExecute(tasks);
                noDataImage.setVisibility(tasks.isEmpty() ? View.VISIBLE : View.GONE);
                setUpAdapter();
                shareviewmodel.updateDashboard(tasks);

            }
        }

        GetSavedTasks savedTasks = new GetSavedTasks();
        savedTasks.execute();
}
}