package com.example.vkr_game2048;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class OnSwipeTouchListener implements OnTouchListener {

    private final GestureDetector gestureDetector;

    Vibrator v;

    MediaPlayer mPlayer;

    public OnSwipeTouchListener (Context ctx){
        this.gestureDetector = new GestureDetector(ctx, new GestureListener());
        this.v = (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);
        this.mPlayer = MediaPlayer.create(ctx, R.raw.swipesound);

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopPlay();
            }
        });
    }

    private void stopPlay(){
        mPlayer.stop();
        try {
            mPlayer.prepare();
            mPlayer.seekTo(0);
        }
        catch (Throwable t) {

        }
    }

    private final class GestureListener extends SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                    }
                } else {
                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeBottom();
                        } else {
                            onSwipeTop();
                        }
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    public void moveCells(){
        new Thread(() -> {
            try {
                stopPlay();
            }
            catch (Throwable t){

            }
            if (Settings.soundsIsOn == true) {
                mPlayer.start();
            }
            if (Settings.vibrationIsOn == true){
                v.vibrate(100);
            }
        }).start();
    }

    public void onSwipeTop() {
        moveCells();
    }
    public void onSwipeRight() {
        moveCells();
    }
    public void onSwipeLeft() {
        moveCells();
    }
    public void onSwipeBottom() {
        moveCells();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

}
