package com.example.bealert.ui.tasks;

import androidx.lifecycle.ViewModel;

import com.example.bealert.Main2Activity;
import com.example.bealert.adapters.TaskAdapter;

public class TaskViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    TaskAdapter taskAdapter;
    Main2Activity activity;

    public TaskViewModel() {
    }

    /*public void addTask(){
        createTaskFragment createTaskBottomSheetFragment = new createTaskFragment();
        createTaskBottomSheetFragment.setTaskId(0, false, activity,activity );
        createTaskBottomSheetFragment.show(activity.getSupportFragmentManager(), createTaskBottomSheetFragment.getTag());
    }



        public void calendarClick(){
        CalendarViewFragment calendarViewBottomSheet = new CalendarViewFragment();
        calendarViewBottomSheet.show(calendarViewBottomSheet.getActivity().getSupportFragmentManager(), calendarViewBottomSheet.getTag());
    }*/


}