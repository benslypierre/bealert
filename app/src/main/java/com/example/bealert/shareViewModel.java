package com.example.bealert;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bealert.models.Favourites;
import com.example.bealert.models.Note;
import com.example.bealert.models.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class shareViewModel extends ViewModel {
    public MutableLiveData<String> pending = new MutableLiveData<>();
    public MutableLiveData<String> overdue = new MutableLiveData<>();
    public MutableLiveData<String> done = new MutableLiveData<>();
    public MutableLiveData<ArrayList>upcomingArray = new MutableLiveData<>();
    public ArrayList<LocalDate> completed = new ArrayList<>();
    public ArrayList<Favourites> favourites = new ArrayList<>();
    public ArrayList favouritesList = new ArrayList<>();
    public Note editNote =new Note("test", "test");
    public String noteId;
    public int code;




    public shareViewModel(MutableLiveData<String> pending, MutableLiveData<String> overdue, MutableLiveData<String> done) {
        this.pending = pending;
        this.overdue = overdue;
        this.done = done;
    }

    public shareViewModel() {
        setPending("0");
        setOverdue("0");
        setDone("0");

    }

    public void setPending(String pendingNew) {
        pending.setValue(pendingNew);
    }

    public void setOverdue(String overdueNew) {
        overdue.setValue(overdueNew);
    }

    public void setDone(String doneNew) {
        done.setValue(doneNew);
    }

    public void setUpcomingArray(ArrayList upcomingArrayNew) {
        upcomingArray.setValue(upcomingArrayNew);
    }

    public void addCompleted(LocalDate completedNew) {

        completed.add(completedNew);
    }

    //controls data  displayed on dashboard
    public void updateDashboard(List<Task> task)
    {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int overdueCount = 0, pendingCount =0, doneCount = 0;
            ArrayList temp = new ArrayList<>();
            LocalDate today = LocalDate.now();

            for(int i = 0; i<task.size();i++){
                String[] items1 = task.get(i).getDate().split("-");
                String dd = items1[0];
                String month = items1[1];
                String year = items1[2];


                if(today.isAfter(LocalDate.of(Integer.parseInt(year), Integer.parseInt(month),Integer.parseInt(dd)))) {
                    overdueCount++;
                    setOverdue(String.valueOf(overdueCount));
                } else if (today.isEqual((LocalDate.of(Integer.parseInt(year), Integer.parseInt(month),Integer.parseInt(dd)))))
                {
                    pendingCount++;
                    setPending(String.valueOf(pendingCount));
                    System.out.println(pendingCount + "-------------------------------------------------------------------------------------");

                }
                if (i < 4)
                {
                    temp.add(task.get(i ).getTaskTitle());
                }

                setUpcomingArray(temp);



            }

            if (completed != null) {
                for (int i = 0; i < completed.size(); i++) {

                    /*String[] items1 = task.get(i).getDate().split("-");
                    String dd = items1[0];
                    String month = items1[1];
                    String year = items1[2];*/

                    //debugging
                    System.out.println(today + "////////////////////////////////////////////////!!!!!!!!!!!!@");
                    if (today.isAfter(completed.get(i))) {
                        completed.remove(i);
                    } else if ((today.isEqual(completed.get(i)))) {
                        doneCount++;

                    }

                }
                //debugging
                System.out.println(doneCount + "!!!!!!!!!!!!@@@@@@@@@@@^^^^^^^doneeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
                setDone(String.valueOf(doneCount));
            }






        }
    }


    public void addFavourites(Favourites newFavourite, Main2Activity context){
        favourites.add(newFavourite);
        saveData(context);



    }
//Save location Data
    private void saveData(Main2Activity  context) {
        // method for saving the data in array list.
        // creating a variable for storing data in
        // shared preferences.
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);

        // creating a variable for editor to
        // store data in shared preferences.
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();

        // creating a new variable for gson.
        Gson gson = new Gson();

        // getting data from gson and storing it in a string.
        String json = gson.toJson(favourites);

        // below line is to save data in shared
        // prefs in the form of string.
        editor.putString("favourites", json);

        // below line is to apply changes
        // and save data in shared prefs.
        editor.apply();
        loadData(context);

        // after saving data we are displaying a toast message.
        Toast.makeText(context.getApplicationContext(), "Saved Array List to Shared preferences. ", Toast.LENGTH_SHORT).show();
    }




    //Load Favourite  locations from Shared Preferences
    public void loadData(Main2Activity context) {

        ArrayList<Favourites> temp = new ArrayList<>();
        // method to load arraylist from shared prefs
        // initializing our shared prefs with name as
        // shared preferences.
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);

        // creating a variable for gson.
        Gson gson = new Gson();

        // below line is to get to string present from our
        // shared prefs if not present setting it as null.
        String json = sharedPreferences.getString("favourites", null);

        // below line is to get the type of our array list.
        Type type = new TypeToken<ArrayList<Favourites>>() {
        }.getType();

        // in below line we are getting data from gson
        // and saving it to our array list
        temp = gson.fromJson(json, type);
        if (temp != null) {
            favouritesList.clear();
            for (int i = 0; i < temp.size(); i++) {
                favouritesList.add(temp.get(i).getName());
            }
        }
        Toast.makeText(context, "" +  favouritesList.size(), Toast.LENGTH_SHORT).show();



            // checking below if the array list is empty or not
            //if (courseModalArrayList == null) {
            // if the array list is empty
            // creating a new array list.
            //    courseModalArrayList = new ArrayList<>();
            //}
        }
    //manage note data
        public void editnote(String id, String title, String content){
        editNote.setTitle(title);
        editNote.setContent(content);
        noteId = id;

        }
    public void editnote(String id, String title, String content, int colorcode){
        editNote.setTitle(title);
        editNote.setContent(content);
        noteId = id;
        code = colorcode;

    }



    }