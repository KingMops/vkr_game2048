package com.example.vkr_game2048;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LinearLayout soundButton;
    LinearLayout vibrationButton;

    String color0 = "#ffffff";
    String color2 = "#B2D0F3";
    String color4 = "#9AC1EF";
    String color8 = "#84B4EB";
    String color16 = "#6FA7E8";
    String color32 = "#5C9BE5";
    String color64 = "#4A90E2";
    String color128 = "#3885DF";
    String color256 = "#277BDC";
    String color512 = "#2272CF";
    String color1024 = "#9D84EB";
    String color2048 = "#8C6FE8";
    String color4096 = "#7D5CE5";
    String color8192 = "#6F4AE2";
    String color16384 = "#6F4AE2";
    String color32768 = "#6F4AE2";
    String color65536 = "#6F4AE2";
    String color131072 = "#6F4AE2";

    int bonusCount = 0;
    private Board board;

    private TextView firstSelectedCell;
    private int firstSelectedCellId;

    private int firstSelectedCellColor;

    private TextView secondSelectedCell;
    private int secondSelectedCellId;

    private int secondSelectedCellColor;
    private TextView [][]tvBoard;

    GameRelativeLayout rel;

    OnSwipeTouchListener swipeTouchListener;

    Fragment achievementFragment = new AchievementFragment();

    private final int[][] tvIds={{R.id.tv00,R.id.tv01,R.id.tv02,R.id.tv03},
            {R.id.tv10,R.id.tv11,R.id.tv12,R.id.tv13},
            {R.id.tv20,R.id.tv21,R.id.tv22,R.id.tv23},
            {R.id.tv30,R.id.tv31,R.id.tv32,R.id.tv33}};

    private final int[] allCells = {R.id.tv00, R.id.tv01,R.id.tv02,R.id.tv03,
            R.id.tv10,R.id.tv11,R.id.tv12,R.id.tv13,
            R.id.tv20,R.id.tv21,R.id.tv22,R.id.tv23,
            R.id.tv30,R.id.tv31,R.id.tv32,R.id.tv33};

    private TextView score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rel = (GameRelativeLayout) findViewById(R.id.completeLayout);

        Bundle bundle = new Bundle();
        bundle.putBoolean("update", false);
        achievementFragment.setArguments(bundle);

        startFragment();

        initSwipeTouchListener();

        findViewById(R.id.tv00).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleVibration();
            }
        });

        board = new Board(getApplicationContext());
        score = (TextView) findViewById(R.id.textResult);
        tvBoard = new TextView[4][4];
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                tvBoard[i][j] = (TextView) findViewById(tvIds[i][j]);
        board.generateNewRandom();
        board.generateNewRandom();
        showBoard();

        soundButton = findViewById(R.id.soundButton);
        soundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSound();
            }
        });

        vibrationButton = findViewById(R.id.vibrationButton);
        vibrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleVibration();
            }
        });

        rel.setOnTouchListener(swipeTouchListener);

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Fragment ach = getSupportFragmentManager().findFragmentByTag("achievement");

                if (ach.isVisible()){
                    getSupportFragmentManager().beginTransaction().hide(ach).commit();
                }
                else{
                    finish();
                }
            }
        };

        TextView textBonus = findViewById(R.id.bonuses);

        this.getOnBackPressedDispatcher().addCallback(this, callback);

        findViewById(R.id.destroy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rel.setOnTouchListener(null);
                for (int i = 0; i < allCells.length; i++){
                    if (bonusCount == 0) {
                        rel.setOnTouchListener(swipeTouchListener);
                        break;
                    }
                    int currentIteration = i;

                    findViewById(allCells[i]).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TextView currentTv = (TextView) v;

                            if (currentTv.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(),"Выберите ненулевую ячейку для обнуления", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            currentTv.setText("");
                            currentTv.setBackgroundColor(Color.parseColor("#ffffff"));

                            board.board[currentIteration].value = 0;

                            for (int i = 0; i < allCells.length; i++){
                                findViewById(allCells[i]).setOnClickListener(null);
                            }
                            rel.setOnTouchListener(swipeTouchListener);
                            textBonus.setText(String.format(Locale.forLanguageTag("ru-RU"),"БОНУСЫ (текущее количество: %d)", --bonusCount));
                        }
                    });
                }
            }
        });



        findViewById(R.id.swap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rel.setOnTouchListener(null);
                for (int i = 0; i < allCells.length; i++){
                    if (bonusCount == 0) {
                        rel.setOnTouchListener(swipeTouchListener);
                        break;
                    }
                    int currentIteration = i;

                    findViewById(allCells[i]).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TextView currentTv = (TextView) v;

                            if (firstSelectedCell == null){
                                if (currentTv.getText().equals("")) return;
                                firstSelectedCell = currentTv;
                                firstSelectedCellId = currentIteration;
                                ColorDrawable cd = (ColorDrawable) currentTv.getBackground();
                                firstSelectedCellColor = cd.getColor();
                            }
                            else{
                                if (firstSelectedCellId != currentIteration){
                                    secondSelectedCell = currentTv;
                                    secondSelectedCellId = currentIteration;
                                    try {
                                        ColorDrawable cd = (ColorDrawable) currentTv.getBackground();
                                        secondSelectedCellColor = cd.getColor();
                                    }
                                    catch (Throwable t){
                                        secondSelectedCellColor = Color.parseColor(color0);
                                    }
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"Выберите другую ячейку для перемещения", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                int cellValue;

                                if (currentTv.getText().toString().equals("")){
                                    cellValue = 0;
                                }
                                else{
                                    cellValue = Integer.parseInt(currentTv.getText().toString());
                                }

                                board.board[secondSelectedCellId].value =  board.board[firstSelectedCellId].value;
                                secondSelectedCell.setText(board.board[secondSelectedCellId].value + "");
                                secondSelectedCell.setBackgroundColor(firstSelectedCellColor);

                                board.board[firstSelectedCellId].value = cellValue;
                                if (cellValue == 0){
                                    firstSelectedCell.setText("");
                                }
                                else{
                                    firstSelectedCell.setText(board.board[firstSelectedCellId].value+ "");
                                }
                                firstSelectedCell.setBackgroundColor(secondSelectedCellColor);

                                for (int i = 0; i < allCells.length; i++){
                                    findViewById(allCells[i]).setOnClickListener(null);
                                }
                                rel.setOnTouchListener(swipeTouchListener);
                                textBonus.setText(String.format(Locale.forLanguageTag("ru-RU"),"БОНУСЫ (текущее количество: %d)", --bonusCount));
                            }
                            }
                    });
                }
            }
        });
    }

    View.OnClickListener destroyCell = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    public void initSwipeTouchListener(){
        swipeTouchListener = new OnSwipeTouchListener(this) {

            public void onSwipeTop() {
                super.onSwipeTop();
                board.topSwipe();
                showBoard();
            }

            public void onSwipeRight() {
                super.onSwipeRight();
                board.rigthSwipe();
                showBoard();
            }

            public void onSwipeLeft() {
                super.onSwipeLeft();
                board.leftSwipe();
                showBoard();

            }

            public void onSwipeBottom() {
                super.onSwipeBottom();
                board.bottomSwipe();
                showBoard();
            }


        };
    }



        private void toggleSound() {
            AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
            if (Settings.soundsIsOn) {
                //audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                Settings.soundsIsOn = false;
                findViewById(R.id.soundsImage).setBackground(getResources().getDrawable(R.drawable.offsoundicon));
            } else {
                //audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 0);
                Settings.soundsIsOn = true;
                findViewById(R.id.soundsImage).setBackground(getResources().getDrawable(R.drawable.onsoundicon));
            }
        }

        private void toggleVibration() {
        if (Settings.vibrationIsOn) {
            Settings.vibrationIsOn = false;
            findViewById(R.id.vibrationImage).setBackground(getResources().getDrawable(R.drawable.offvibrationicon));
        } else {
            Settings.vibrationIsOn = true;
            findViewById(R.id.vibrationImage).setBackground(getResources().getDrawable(R.drawable.onvibrationicon));
        }
    }

    public void openAchievementsActivity (View v) {
        getSupportFragmentManager().beginTransaction()
                .show(achievementFragment)
                .commit();
    }

    public void newgameButton (View v) { {
                Achievements.clearAllAchievements();
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
    }



    public void showBoard() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {

                if (board.getValue(i, j).compareTo(tvBoard[i][j].getText().toString()) != 0) {
                    tvBoard[i][j].setText(board.getValue(i, j));
                    tvBoard[i][j].startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
                    if (board.getValue(i, j).compareTo("") == 0)
                        tvBoard[i][j].setBackgroundColor(Color.parseColor(color0));
                    if (board.getValue(i, j).compareTo("2") == 0)
                        tvBoard[i][j].setBackgroundColor(Color.parseColor(color2));
                    if (board.getValue(i, j).compareTo("4") == 0)
                        tvBoard[i][j].setBackgroundColor(Color.parseColor(color4));
                    if (board.getValue(i, j).compareTo("8") == 0)
                        tvBoard[i][j].setBackgroundColor(Color.parseColor(color8));
                    if (board.getValue(i, j).compareTo("16") == 0)
                        tvBoard[i][j].setBackgroundColor(Color.parseColor(color16));
                    if (board.getValue(i, j).compareTo("32") == 0)
                        tvBoard[i][j].setBackgroundColor(Color.parseColor(color32));
                    if (board.getValue(i, j).compareTo("64") == 0)
                        tvBoard[i][j].setBackgroundColor(Color.parseColor(color64));
                    if (board.getValue(i, j).compareTo("128") == 0)
                        tvBoard[i][j].setBackgroundColor(Color.parseColor(color128));
                    if (board.getValue(i, j).compareTo("256") == 0)
                        tvBoard[i][j].setBackgroundColor(Color.parseColor(color256));
                    if (board.getValue(i, j).compareTo("512") == 0)
                        tvBoard[i][j].setBackgroundColor(Color.parseColor(color512));
                    if (board.getValue(i, j).compareTo("1024") == 0)
                        tvBoard[i][j].setBackgroundColor(Color.parseColor(color1024));
                    if (board.getValue(i, j).compareTo("2048") == 0)
                        tvBoard[i][j].setBackgroundColor(Color.parseColor(color2048));
                    if (board.getValue(i, j).compareTo("4096") == 0)
                        tvBoard[i][j].setBackgroundColor(Color.parseColor(color4096));
                    if (board.getValue(i, j).compareTo("8192") == 0)
                        tvBoard[i][j].setBackgroundColor(Color.parseColor(color8192));
                    if (board.getValue(i, j).compareTo("16384") == 0)
                        tvBoard[i][j].setBackgroundColor(Color.parseColor(color16384));
                    if (board.getValue(i, j).compareTo("32768") == 0)
                        tvBoard[i][j].setBackgroundColor(Color.parseColor(color32768));
                    if (board.getValue(i, j).compareTo("65536") == 0)
                        tvBoard[i][j].setBackgroundColor(Color.parseColor(color65536));
                    if (board.getValue(i, j).compareTo("131072") == 0)
                        tvBoard[i][j].setBackgroundColor(Color.parseColor(color131072));
                }
            }
        }
        score.setText("Счет: " + board.getScore());
        TextView textBonus = findViewById(R.id.bonuses);

        if (board.getIntScore() > 3 && !Achievements.first){
            Achievements.first = true;
            showToast("Первые шаги!");
            sendMessageToFragment();
            textBonus.setText(String.format(Locale.forLanguageTag("ru-RU"),"БОНУСЫ (текущее количество: %d)", ++bonusCount));
        }
        else if (board.getIntScore() > 99 && !Achievements.second){
            Achievements.second = true;
            showToast("Уже неплохо!");
            sendMessageToFragment();
            textBonus.setText(String.format(Locale.forLanguageTag("ru-RU"),"БОНУСЫ (текущее количество: %d)", ++bonusCount));
        }
        else if (board.getIntScore() > 499 && !Achievements.third){
            Achievements.third = true;
            showToast("Разминка для мозгов!");
            sendMessageToFragment();
            textBonus.setText(String.format(Locale.forLanguageTag("ru-RU"),"БОНУСЫ (текущее количество: %d)", ++bonusCount));
        }
        else if (board.getIntScore() > 999 && !Achievements.fourth){
            Achievements.fourth = true;
            showToast("Мудрец!");
            sendMessageToFragment();
            textBonus.setText(String.format(Locale.forLanguageTag("ru-RU"),"БОНУСЫ (текущее количество: %d)", ++bonusCount));
        }
        else if (board.getIntScore() > 1999 && !Achievements.fifth){
            Achievements.fifth = true;
            showToast("Мегамозг!");
            sendMessageToFragment();
            textBonus.setText(String.format(Locale.forLanguageTag("ru-RU"),"БОНУСЫ (текущее количество: %d)", ++bonusCount));
        }


    }


    public void showToast(String message) {
        //создаём и отображаем текстовое уведомление
        Toast toast = Toast.makeText(getApplicationContext(),
                message,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    public void sendMessageToFragment(){
        Bundle bundle = new Bundle();
        bundle.putBoolean("update", true);
        achievementFragment = new AchievementFragment();
        achievementFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .remove(achievementFragment).commit();
        startFragment();
    }

    public void startFragment(){
        getSupportFragmentManager().beginTransaction()
                .add(R.id.achievement_frame, achievementFragment, "achievement")
                .hide(achievementFragment)
                .commit();
    }

}
