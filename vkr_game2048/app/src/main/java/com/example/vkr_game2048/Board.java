package com.example.vkr_game2048;

import android.content.Context;
import android.widget.Toast;

import java.lang.Math;
import java.util.ArrayList;
import java.util.Random;

public class Board {


    public Square [] board;
    private ArrayList<Square> empties;
    private int score;

    private Context context;
    public Board(Context context){
        board = new Square [16];
        empties = new ArrayList<Square>();
        score =0;
        for (int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                board[i*4+j]=new Square(i,j,0);
                empties.add(board[i*4+j]);
            }
        }
        this.context = context;
    }
    public void generateNewRandom(){
        int random=0;
        Random randomGenerator = new Random();
        random=randomGenerator.nextInt(empties.size());
        empties.get(random).setValue((Math.random() < 0.9) ? 2 : 4);
        empties.remove(random);

    }
    public String getValue(int row, int column){
        String value ="";
        value =Integer.toString(board[row*4+column].getValue());
        if(value.compareTo("0")==0)
            return "";
        return value;

    }

    public boolean isGameOver() {
        // Проверка наличия возможных ходов
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int value = board[i * 4 + j].getValue();

                // Проверка соседних клеток на равенство или наличие свободных клеток
                if ((i > 0 && board[(i - 1) * 4 + j].getValue() == value) ||
                        (i < 3 && board[(i + 1) * 4 + j].getValue() == value) ||
                        (j > 0 && board[i * 4 + (j - 1)].getValue() == value) ||
                        (j < 3 && board[i * 4 + (j + 1)].getValue() == value) ||
                        value == 0) {
                    return false; // Есть возможный ход, игра не закончена
                }
            }
        }

        return true; // Все клетки заняты и нет возможных ходов
    }

    public void topSwipe(){
        if(moveBlockUp()>0)
            generateNewRandom();
        setSquaresToCanMove();
        if (isGameOver()) {
            handleGameOver(); // Обработка окончания игры
        }
    }
    public void bottomSwipe(){
        if(moveBlockDown()>0)
            generateNewRandom();
        setSquaresToCanMove();
        if (isGameOver()) {
            handleGameOver();
        }
    }
    public void leftSwipe(){
        if(moveBlockLeft()>0)
            generateNewRandom();
        setSquaresToCanMove();
        if (isGameOver()) {
            handleGameOver();
        }
    }
    public void rigthSwipe(){
        if(moveBlockRight()>0)
            generateNewRandom();
        setSquaresToCanMove();
        if (isGameOver()) {
            handleGameOver();
        }
    }
    public void handleGameOver() {
        System.out.println("Игра окончена!"); // Вывод в логи Игра Окончена

        handleGameOver(context);
    }

    public void handleGameOver(Context context) {
      Toast.makeText(context, "Игра окончена!", Toast.LENGTH_SHORT).show();
    }

    public boolean moveSquare(Square origin,Square destiny){
        boolean moved=false;
        if(origin.getValue()!=0 && destiny.getValue()==0){
            destiny.setValue(origin.getValue());
            origin.setValue(0);
            empties.add(origin);
            empties.remove(destiny);
            moved=true;
        }else if(origin.getValue()!=0 && origin.getValue()==destiny.getValue() && origin.isCanMove() && destiny.isCanMove()){
            score +=origin.getValue()+destiny.getValue();
            destiny.setValue(origin.getValue()+destiny.getValue());
            origin.setValue(0);
            empties.add(origin);
            destiny.setCanMove(false);
            moved=true;
        }
        return moved;
    }

    public int moveRow(int rowOrigin,boolean moveUp){
        int moved =0;
        if(moveUp){
            for(int i=0;i<4;i++)
                if(moveSquare(board[rowOrigin*4+i],board[rowOrigin*4-4+i]))
                    moved++;
        }
        else{
            for(int i=0;i<4;i++)
                if(moveSquare(board[rowOrigin*4+i],board[rowOrigin*4+4+i]))
                    moved++;
        }
        return moved;
    }
    public int moveColumn(int columnOrigin,boolean moveRight){
        int moved =0;
        if(moveRight){
            for(int i=0;i<4;i++)
                if(moveSquare(board[i*4+columnOrigin],board[i*4+columnOrigin+1]))
                    moved++;
        }
        else{
            for(int i=0;i<4;i++)
                if(moveSquare(board[i*4+columnOrigin],board[i*4+columnOrigin-1]))
                    moved++;
        }
        return moved;
    }

    public int moveBlockUp(){
        int moved=0;
        for(int j=0;j<3;j++)
            for (int i=1;i<4;i++)
                moved +=moveRow(i,true);
        return moved;
    }
    public int moveBlockDown(){
        int moved=0;
        for(int j=0;j<3;j++)
            for (int i =2;i>=0;i--)
                moved +=moveRow(i,false);
        return moved;
    }
    public int moveBlockRight(){
        int moved=0;
        for(int j=0;j<3;j++)
            for (int i =2;i>=0;i--)
                moved +=moveColumn(i,true);
        return moved;
    }
    public int moveBlockLeft(){
        int moved=0;
        for(int j=0;j<3;j++)
            for (int i =1;i<4;i++)
                moved +=moveColumn(i,false);
        return moved;
    }
    public void setSquaresToCanMove()
    {
        for (int i=0;i<16;i++)
            board[i].setCanMove(true);
    }
    public String getScore(){
        return Integer.toString(score);
    }

    public int getIntScore() { return score; }
}