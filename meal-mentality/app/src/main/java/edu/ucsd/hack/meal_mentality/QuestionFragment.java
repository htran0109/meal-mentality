package edu.ucsd.hack.meal_mentality;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.Calendar;

/**
 * Created by chirag on 2/27/16.
 */
public class QuestionFragment extends Fragment {

    private QuestionArrayAdapter questArrayAdapter;
    private ListView listView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState ){
        super.onCreate(savedInstanceState);
        android.view.View rootView = inflater.inflate(R.layout.listview, container, false);


        listView = (ListView) rootView.findViewById(R.id.card_listView);
        questArrayAdapter = new QuestionArrayAdapter(getActivity(), R.layout.listviewitem);

        for (int i = 0; i < 10; i++) {
            if(i%2==0) {
                Question card = new Question("NUM", "Hello World?");
                questArrayAdapter.add(card);
            }
            else {
                Question card = new Question("YN", "Hello World?");
                questArrayAdapter.add(card);
            }
        }
        listView.setAdapter(questArrayAdapter);
        return rootView;
    }
}
