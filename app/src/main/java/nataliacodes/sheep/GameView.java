///////////////////////////////
/*

  GameView is the drawing screen manager

 */

package nataliacodes.sheep;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

    Thread gameThread = null;
    SurfaceHolder ourHolder;
    volatile boolean playing;
    Canvas canvas;
    long fps;
    private long timeThisFrame;// Used to help calculate the fps
    float backgroundWidth; // equals to 2*screenWidth
    float screenHeight;
    float screenWidth;
    float cameraX = 0; //24.09.17
    Bitmap bitmapBackground;

    float playerJumpHeight = 150;
    float playerGravity = (float) 0.75;   //Whenever the character falls, he will descend at this rate.

    double velocity = 2 * playerJumpHeight * playerGravity;

    private int frameLengthInMilliseconds = 50;

    Rect cameraFrame;
    RectF whereToDrawBob;
    RectF whereToDrawSheepA;
    RectF whereToDrawSheepB;
    RectF cameraWhereToDraw;
    int sheepSize = 100;

    private Rect frameToDrawBob;// = new Rect(0, 0, 100, 100); // 15.10.17  TODO rename
    private Rect frameToDrawSheep;// = new Rect (0, 0, 100, 100);

    private Bob bob;
    private Sheep sheepA;
    private Sheep sheepB;

    private ArrayList sheepArray; //20.10.17

    private float sheepSafeLength = 600; //23.10.17
    private float sheepDiffLength;
    private float sheepFirstPlaceX;

    private boolean firstScreen = true;

    private int score;

    private void setSheepArray() { // TODO where to put this?

        sheepArray.add(0, sheepA);
        sheepArray.add(1, sheepB);
    }

    public GameView(Context context) {

        super(context);
        init();

        playing = true;
        firstScreen = false;
    }

    @Override
    public void run() {

        init(); //  TODO does it fix the beginning bug?

        while (playing) {
            //pipeline of input -> update -> draw

            // Capture the current time in milliseconds in startFrameTime
//            long startFrameTime = System.currentTimeMillis();
            update();
            draw();

//            TODO why do you need this code? sheep bug <---------------
            // Calculate the fps this frame
//            timeThisFrame = System.currentTimeMillis() - startFrameTime;
//            if (timeThisFrame >= 1) {
//                fps = 1000 / timeThisFrame;
//            }

        }
    }


    private void init() {

        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenHeight = (float) metrics.heightPixels;
        screenWidth = (float) metrics.widthPixels;
        backgroundWidth = 2 * screenWidth;

        cameraX = 0;
        cameraFrame = new Rect((int) cameraX, 0, (int) (screenWidth + cameraX), (int) screenHeight);
        cameraWhereToDraw = new RectF(0, 0, screenWidth, screenHeight);

        bitmapBackground = BitmapFactory.decodeResource(this.getResources(), R.drawable.background);
        bitmapBackground = Bitmap.createScaledBitmap(bitmapBackground, (int) backgroundWidth, (int) screenHeight, false);

        // init bob
        float bobX = screenWidth / 2;
        float bobY = screenHeight / 2 + screenHeight / 4;
        Bitmap bobBitmapImageFactory = BitmapFactory.decodeResource(this.getResources(), R.drawable.bob);
        this.bob = new Bob(bobX, bobY, bobBitmapImageFactory);

        // init sheep
        float sheepX = screenWidth + 15;
        float sheepY = screenHeight / 2 + screenHeight / 4;
        Bitmap bitmapSheep = BitmapFactory.decodeResource(this.getResources(), R.drawable.transheep);

        Random randA = new Random();
        sheepDiffLength = randA.nextInt(200) + 10; // randA.nextInt(200) + 50

        Random randB = new Random();
        sheepFirstPlaceX = randB.nextInt(200) + 1700;

        if (firstScreen) {
            this.sheepA = new Sheep(sheepFirstPlaceX, sheepY, bitmapSheep, screenWidth);
            this.sheepB = new Sheep(sheepFirstPlaceX + sheepSafeLength + sheepDiffLength, sheepY, bitmapSheep, screenWidth);
        }
        else{
            this.sheepA = new Sheep(sheepX, sheepY, bitmapSheep, screenWidth);
            this.sheepB = new Sheep(sheepX + sheepSafeLength + sheepDiffLength, sheepY, bitmapSheep, screenWidth);
        }

        frameLengthInMilliseconds = 50;

        ourHolder = getHolder(); //TODO understand what is holder!! it's the surface holder, but what is it, and why does it needed?
        gameThread = null;

    }

    public void updateCameraBackground() {

        // if we finished walking the background image loop it again
        if (cameraX + screenWidth >= backgroundWidth) {
            cameraX = 0;
        } else {
            cameraX = cameraX + 5;
        }
        cameraFrame = new Rect((int) cameraX, 0, (int) (screenWidth + cameraX), (int) screenHeight);
    }

    public void update() {

        updateCameraBackground();
        bob.handleJump();

        // 15.10.17
//            Bob.whereToDrawBob.set((int) bobXPosition, (int) bobYPosition, (int) (bobXPosition + bobFrameWidth / 2), (int) (bobYPosition + bobFrameHeight));
//            Sheep.whereToDrawSheep.set((int) sheepXPosition, (int) sheepYPosition, (int) (sheepXPosition + sheepSize), (int) (sheepYPosition + sheepSize));
        whereToDrawBob = bob.getWhereToDrawBob();
        whereToDrawSheepA = sheepA.getWhereToDrawSheep();
        whereToDrawSheepB = sheepB.getWhereToDrawSheep();

        if (cameraX + screenWidth >= backgroundWidth / 2) {
            sheepArray = new ArrayList(2);
            setSheepArray();
        }

    }

    public void draw() {

        if (ourHolder.getSurface().isValid()) {

            //lock canvas
            canvas = ourHolder.lockCanvas();
            //draw background on canvas not on screen!!!
            canvas.drawBitmap(bitmapBackground, cameraFrame, cameraWhereToDraw, null);
            //draw bob
            // canvas.drawBitmap(bob.getBitmap(),frameToDrawBob, whereToDrawBob, null);
            canvas.drawBitmap(bob.getBitmap(), bob.getFrameToDraw(), whereToDrawBob, null);

            //draw sheep array
            canvas.drawBitmap((((Sheep)sheepArray.get(0))).getBitmap(), frameToDrawSheep, whereToDrawSheepA, null);
            canvas.drawBitmap(((Sheep)(sheepArray.get(1))).getBitmap(), frameToDrawSheep, whereToDrawSheepB, null);


            if ((whereToDrawSheepA.intersect(whereToDrawBob))||((whereToDrawSheepB.intersect(whereToDrawBob)))) {
                firstScreen = true; // TODO create a method that checks intersect
                // TODO, this is not the correct place for this code line
                // put this code at Utils
                Paint paint = new Paint();
                paint.setColor(Color.RED);
                paint.setTextSize(300);
                canvas.drawText("Game Over", 175, 400, paint);
                ourHolder.unlockCanvasAndPost(canvas);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                init();
                run();
            }

            // Draw everything to the screen
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    // The SurfaceView class implements onTouchListener
    // So we can override this method and detect screen touches.
//    TODO understand if this code runs in a separate thread - in other words, whether it works in synchronous way or in asynchronous way
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            //  has touched the screen
            case MotionEvent.ACTION_DOWN:
                bob.setIsJump(true);//old: isJump = true;
                break;
            //  has removed finger from screen
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }
}



