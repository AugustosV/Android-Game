package com.example.gametest;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Obstacle {
    private Rect rectangle;
    private Rect rectangle2;
    private int color;
    private int playerGap;

    public Rect getRectagle(){
        return rectangle;
    }

    public void incrementY(float Y){
        rectangle.top += Y;
        rectangle.bottom += Y;
        rectangle2.top += Y;
        rectangle2.bottom += Y;
    }

    public Obstacle (int recH, int color, int startX,int startY, int playerGap){
        this.color = color;
        //l,t,r,b
        rectangle = new Rect(0, startY, startX, startY + recH);
        rectangle2 = new Rect(startX + playerGap, startY, Contants.Tela_Width, startY + recH);
    }

    public boolean playerCollider(RectPlayer player){
       return Rect.intersects(rectangle, player.getRectagle())
               || Rect.intersects(rectangle2, player.getRectagle()) ;

    }

    //@Override
    public void draw(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rectangle, paint);
        canvas.drawRect(rectangle2, paint);

    }
    //@Override
    public void uptade(){

    }
}
