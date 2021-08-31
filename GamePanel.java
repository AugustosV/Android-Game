package com.example.gametest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;
    private Rect r = new Rect();
    private RectPlayer player;
    private Point playerPoint;
    private ObstacleManager obstacleManager;

    private boolean movingPlayer = false;
    private boolean gameOver = false;
    private long gameOverTime;

    public GamePanel(Context context) {
        super(context);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(),this);

        player= new RectPlayer(new Rect(100, 100, 200, 200), Color.rgb(255,255,0));
        playerPoint = new Point(Contants.Tela_Width/2, 3*Contants.Tela_Height/4);
        player.update(playerPoint);

        obstacleManager = new ObstacleManager(200, 350, 75, Color.DKGRAY);

        setFocusable(true);
    }

    public void reset(){
        playerPoint = new Point(Contants.Tela_Width/2, 3*Contants.Tela_Height/4);
        player.update(playerPoint);
        obstacleManager = new ObstacleManager(200, 350, 75, Color.LTGRAY);
        movingPlayer = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        thread = new MainThread(getHolder(),this);

        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        while(true){
            try {
                thread.setRunning(false);
            }catch (Exception e) {e.printStackTrace();}
            retry = false;
        }
    }

    //Evento de toque em tela
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(!gameOver && player.getRectagle().contains((int)event.getX(), (int)event.getY()))
                    movingPlayer = true;
                if(gameOver && System.currentTimeMillis() - gameOverTime >= 200){
                    reset();
                    gameOver = false;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if(!gameOver && movingPlayer)
                    playerPoint.set((int) event.getY(), (int) event.getY());
                break;
            case MotionEvent.ACTION_UP:
                movingPlayer = false;
                break;
                //playerPoint.set((int)event.getX(), (int)event.getY());
        }
        return true;
        //return  super.onTouchEvent(event);
    }

    public void update(){
        if(!gameOver){
            player.update(playerPoint);
            obstacleManager.update();

            if(obstacleManager.playerCollider(player)){
                gameOver = true;
                gameOverTime =System.currentTimeMillis();
            }
        }
    }

    //Desenha o canvas para o jogo
    @Override
    public void draw(Canvas canvas){

        super.draw(canvas);
        //cor dor canvas
        canvas.drawColor((Color.WHITE));

        player.draw(canvas);
        obstacleManager.draw(canvas);

        if(gameOver){
            Paint paint = new Paint();
            paint.setTextSize(100);
            paint.setColor(Color.BLACK);
            drawCenterText(canvas, paint, "GAME OVER CARAIO");
        }
    }

    //Texto no canvas
    private void drawCenterText(Canvas canvas, Paint paint, String text){
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.rgb(255,0,0));
        canvas.getClipBounds(r);
        int cH = r.height();
        int cW = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cW / 2f - r.width() / 2f - r.left;
        float y = cH / 2f - r.height() / 2f - r.bottom;
        canvas.drawText(text, x, y, paint);

    }
}
