package edu.ucsd.hack.meal_mentality;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
        android.view.View rootView = inflater.inflate(R.layout.listview_questions, container, false);


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
        card = new Question("YN", "Do you think that a majority of people in the world disagree with you in an argument?");
        questArrayAdapter.add(card);
        card = new Question("YN", "Are you sleeping less or eating more than you normally would?");
        questArrayAdapter.add(card);

        card = new Question("NUM", "What is your weight?");
        questArrayAdapter.add(card);
        card = new Question("NUM", "What is your height?");
        questArrayAdapter.add(card);
        card = new Question("NUM", "What is your age?");
        questArrayAdapter.add(card);
        card = new Question("NUM", "How many calories do you eat per day?");
        questArrayAdapter.add(card);

        LayoutInflater inf = LayoutInflater.from(getActivity());
        View v = inf.inflate(R.layout.footer, null);
        Button button = (Button) v.findViewById(R.id.footer);
        listView.addFooterView(v);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CardListActivity car = new CardListActivity();
                android.app.Fragment fragment = car;
                Bundle bundle = new Bundle();
                bundle.putBoolean("restart", false);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            }
        });

        listView.setAdapter(questArrayAdapter);
        return rootView;
    }
}
