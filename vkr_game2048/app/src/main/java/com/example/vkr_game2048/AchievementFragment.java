package com.example.vkr_game2048;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AchievementFragment extends Fragment {

    private View view;

    public AchievementFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_achievement, container, false);

        boolean updates = getArguments().getBoolean("update");

        TextView title;
        TextView content;
        View bg;

        if (updates){
            if (Achievements.first){

                bg = view.findViewById(R.id.constraintLayout);

                title = view.findViewById(R.id.firstTitle);
                content = view.findViewById(R.id.firstContent);

                bg.setBackgroundColor(Color.parseColor("#FF0A67AE"));
                title.setText("Первые шаги");
                title.setTextColor(Color.parseColor("#FF4900"));
                content.setText("Объединить первые плитки");
                content.setTextColor(Color.parseColor("#FF4900"));

            }
            if (Achievements.second){

                bg = view.findViewById(R.id.constraintLayout2);

                title = view.findViewById(R.id.secondTitle);
                content = view.findViewById(R.id.secondContent);

                bg.setBackgroundColor(Color.parseColor("#FF0A67AE"));
                title.setText("Уже неплохо");
                title.setTextColor(Color.parseColor("#FF4900"));
                content.setText("Набрать больше 100 очков");
                content.setTextColor(Color.parseColor("#FF4900"));
            }
            if (Achievements.third){

                bg = view.findViewById(R.id.constraintLayout3);

                title = view.findViewById(R.id.thirdTitle);
                content = view.findViewById(R.id.thirdContent);

                bg.setBackgroundColor(Color.parseColor("#FF0A67AE"));
                title.setText("Разминка для мозгов");
                title.setTextColor(Color.parseColor("#FF4900"));
                content.setText("Набрать больше 500 очков");
                content.setTextColor(Color.parseColor("#FF4900"));
            }
            if (Achievements.fourth){

                bg = view.findViewById(R.id.constraintLayout4);

                title = view.findViewById(R.id.fourthTitle);
                content = view.findViewById(R.id.fourthContent);

                bg.setBackgroundColor(Color.parseColor("#FF0A67AE"));
                title.setText("Мудрец");
                title.setTextColor(Color.parseColor("#FF4900"));
                content.setText("Набрать больше 1000 очков");
                content.setTextColor(Color.parseColor("#FF4900"));
            }
            if (Achievements.fifth){

                bg = view.findViewById(R.id.constraintLayout5);

                title = view.findViewById(R.id.fifthTitle);
                content = view.findViewById(R.id.fifthContent);

                bg.setBackgroundColor(Color.parseColor("#FF0A67AE"));
                title.setText("Мегамозг");
                title.setTextColor(Color.parseColor("#FF4900"));
                content.setText("Набрать больше 2000 очков");
                content.setTextColor(Color.parseColor("#FF4900"));
            }
        }


        return view;
    }
}
