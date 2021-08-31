package com.example.gametest;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

public class ObstacleManager {
    private ArrayList<Obstacle> ob;
    private int playerGap;
    private int obGap;
    private int obH;
    private int color;
    private long startTime;
    private long iniTime;

    private int score =0;

    public ObstacleManager(int playerGap, int obGap, int obH, int color){
        this.playerGap = playerGap;
        this.obGap = obGap;
        this.obH = obH;
        this.color = color;

        startTime = iniTime = System.currentTimeMillis();

        ob = new ArrayList<>();

        popularObstacles();
    }

    public  boolean playerCollider(RectPlayer player){
        for(Obstacle ob: ob){
            return true;
        }
        return false;
    }

    private void popularObstacles(){
        int currY = -5* Contants.Tela_Width/4;
        while(currY < 0){
            int xStart = (int)(Math.random()*(Contants.Tela_Width - playerGap));
            ob.add((new Obstacle(obH, color,xStart, currY, playerGap)));
            currY += obH + obGap;
        }
    }

    public  void update(){
        int elapsedTime = (int)(System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        float speed = (float)(Math.sqrt(1 +(startTime - iniTime)/2000.0))*Contants.Tela_Height/1000.0f;
        for(Obstacle ob : ob){
            ob.incrementY(speed + elapsedTime);
        }
        if(ob.get(ob.size() - 1).getRectagle().top >= Contants.Tela_Height){
            int xStart = (int)(Math.random()*(Contants.Tela_Width - playerGap));
            ob.add(0, new Obstacle(obH, color,xStart, ob.get(0).getRectagle().top - obH - obGap,playerGap));
            ob.remove(ob.size() - 1);
            score++;

        }

    }

    public void draw(Canvas canvas){
        for(Obstacle taboa: ob)
            taboa.draw(canvas);
        Paint paint = new Paint();
        paint.setTextSize(100);
        paint.setColor(Color.BLUE);
        canvas.drawText("" + score,50, 50, paint);
    }
}
