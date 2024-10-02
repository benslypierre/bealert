package com.example.bealert.ui.home;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.bealert.R;
import com.example.bealert.database.DatabaseClient;
import com.example.bealert.databinding.FragmentHomeBinding;
import com.example.bealert.models.Task;
import com.example.bealert.shareViewModel;
import com.marvel999.acr.ArcProgress;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    ListView upcomingList;
    List<Task>  upcomingArray ;
    List<Task> temp ;
    ArrayList  upcomingArray1 = new ArrayList<>();
    TextView pending, overdue, done;
    int overdueCount =0;
    shareViewModel shareviewmodel;
    ArcProgress arcProgress;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //HomeViewModel homeViewModel =
         //       new ViewModelProvider(this).get(HomeViewModel.class);

        shareviewmodel = new ViewModelProvider(requireActivity()).get(shareViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        arcProgress = binding.arcImg;
        pending = binding.todocount;
        overdue = binding.overdue;
        done = binding.completed;
        upcomingList = binding.upcomingList;
        getUpcomingTasks();

        //upcomingArray1.addAll(shareviewmodel.upcomingArray);

        //upcomingArray1 = shareviewmodel.upcomingArray;








        binding.commuteAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(R.id.nav_to_bus);

            }
        });
        binding.locationsAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //NavController navController = Navigation.findNavController(getActivity().getParent(), R.id.nav_host_fragment_content_main2);
                //navController.navigate(R.id.nav_commute);

                Navigation.findNavController(getView()).navigate(R.id.nav_to_locations);

            }
        });


        binding.taskAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(R.id.nav_to_tasks);


            }
        });

        binding.notesAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(R.id.nav_to_notes);


            }
        });









        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void setupAdapter(){
        //int size = upcomingArray.size();
        //ArrayList<Task> temp = new ArrayList<>();



       /* if (size > 5)
        {

            for (int i = size; i > size-5 ; i--) {
                temp.add(upcomingArray.get(i - 1));
                upcomingArray.clear();
                upcomingArray1.addAll(temp);
            }
        } else {*/
        /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            overdueCount = 0;
            LocalDate today = LocalDate.now();
            upcomingArray1.clear();
            for(int i = 0; i<upcomingArray.size();i++){
                String[] items1 = upcomingArray.get(i).getDate().split("-");
                String dd = items1[0];
                String month = items1[1];
                String year = items1[2];

                if(today.isAfter(LocalDate.of(Integer.parseInt(year), Integer.parseInt(month),Integer.parseInt(dd))))
                    overdueCount++;

            }
        }*/
        //pending.setText(upcomingArray.size() + "");
       // overdue.setText(overdueCount + "");

        if (upcomingArray1 != null){
            upcomingArray1.clear();
        }
            for (int i = 0; i < upcomingArray.size(); i++) {
                upcomingArray1.add(upcomingArray.get(i ).getTaskTitle());

                System.out.println(upcomingArray.get(i ).getDate());

                if (i == 5)
                    break;


            }


        //}

        pending.setText(String.valueOf(shareviewmodel.pending.getValue()));
        overdue.setText(String.valueOf(shareviewmodel.overdue.getValue()));
        done.setText(String.valueOf(shareviewmodel.done.getValue()));

        if(Integer.parseInt(pending.getText().toString())!= 0) {
            if(Integer.parseInt(done.getText().toString()) == 0)
                arcProgress.setProgressWithAnimation(0);
            else
                arcProgress.setProgressWithAnimation((Integer.parseInt(done.getText().toString()) / Integer.parseInt(pending.getText().toString())) * 100);

        } else  if (Integer.parseInt(done.getText().toString()) > 0)
            arcProgress.setProgressWithAnimation(100);
        else if (Integer.parseInt(done.getText().toString()) == 0 && Integer.parseInt(pending.getText().toString()) == 0)
            arcProgress.setProgressWithAnimation(0);

        ArrayAdapter<Task> upcomingAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,upcomingArray1);
        upcomingList.setAdapter(upcomingAdapter);
    }



    public void getUpcomingTasks(){

        class GetUpcomingTasks extends AsyncTask<Void, Void, List<Task>> {
            @Override
            protected List<Task> doInBackground(Void... voids) {

                if (upcomingArray != null){
                    upcomingArray.clear();
                }
                upcomingArray = DatabaseClient
                        .getInstance(getActivity().getApplicationContext())
                        .getAppDatabase()
                        .dataBaseAction()
                        .getAllTasksList();

               /* for (int i = temp.size(); i < temp.size() - 5; i--) {
                    upcomingArray.add(temp.get(i));
                }*/
                return upcomingArray;
            }

            @Override
            protected void onPostExecute(List<Task> task) {
                super.onPostExecute( task);
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                shareviewmodel.updateDashboard(task);
                setupAdapter();


            }


        }
        GetUpcomingTasks upcomingTasks = new GetUpcomingTasks();
        upcomingTasks.execute();







    }
}