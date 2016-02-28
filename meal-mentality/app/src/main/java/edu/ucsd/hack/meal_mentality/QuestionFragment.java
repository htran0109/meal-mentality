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
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.view.View rootView = inflater.inflate(R.layout.listview, container, false);


        listView = (ListView) rootView.findViewById(R.id.card_listView);
        questArrayAdapter = new QuestionArrayAdapter(getActivity(), R.layout.listviewitem);


        Question card = new Question("YN", "Do you lose concentration? Do you become tired easily?");
        questArrayAdapter.add(card);
        card = new Question("YN", "Do unwelcome thoughts or images repeatedly enter your mind that you can’t control?");
        questArrayAdapter.add(card);
        card = new Question("YN", "Have you ever had periodic states of extreme sadness?");
        questArrayAdapter.add(card);
        card = new Question("NUM", "How long, on average, do you sleep in a night?");
        questArrayAdapter.add(card);
        card = new Question("NUM", "Do you think that a majority of people in the world disagree with you in an argument?");
        questArrayAdapter.add(card);
        card = new Question("YN", "Are you sleeping less or eating more than you normally would?");
        questArrayAdapter.add(card);



        listView.setAdapter(questArrayAdapter);
        return rootView;
    }
}
