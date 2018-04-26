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

    float playerJumpHeight = 150;
    float playerGravity = (float) 0.75;   //Whenever the character falls, he will descend at this rate.

    double velocity = 2 * playerJumpHeight * playerGravity;

    private int frameLengthInMilliseconds = 50;

    RectF cameraWhereToDraw;
    int sheepSize = 100;

    private Rect frameToDrawBob;// = new Rect(0, 0, 100, 100); // 15.10.17  TODO rename
    private Rect frameToDrawSheep;// = new Rect (0, 0, 100, 100);
    private Rect frameToDrawCoin;

    private Player player;

    private ArrayList<Sheep> sheepArray; //20.10.17
    private ArrayList<Coin> coinArray;

    private float sheepSafeLength = 500; //23.10.17
    private float sheepDiffLength;
    private float sheepFirstPlaceX = 2000;

    private int score;

    private int sheepArraySize = 3;

    private int coinArraySize = 5;
    private float coinFirstPlaceX = 1200;
    private float coinSafeLength = 200;
    private float coinY = 500;

    private int coins;
    private SoundPlayer sound;

    DrawableLayout sky;
    DrawableLayout cloud;
    DrawableLayout hills;
    DrawableLayout ground;

    //TODO fix
    int skySpeed = 0;
    int cloudSpeed = 4;
    int hillsSpeed = 3;
    int groundSpeed = 5;

    public GameView(Context context) {

        super(context);
        init();

        playing = true;
        sound = new SoundPlayer(context); //31.10.17
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

    private void UpdateScore() {

        for (int i = 0; i < sheepArray.size(); ++i) {
            Sheep currentSheep = sheepArray.get(i);
            if (player.getXPosition() > currentSheep.getXPosition() && !(currentSheep.getPassedOver())) {
                currentSheep.setPassedOver(true);
                score++;
                sound.playSheep();
            }
            if (currentSheep.getXPosition() > screenWidth) {
                currentSheep.setPassedOver(false);
            }

            currentSheep.updateSheep();
        }

    }

    public void ifCaughtCoin() {

        for (int i = 0; i < coinArray.size(); ++i) {
            Coin currentCoin = coinArray.get(i);
            if ((currentCoin.getWhereToDrawCoin().intersect(player.getWhereToDrawPlayer())) && (!currentCoin.getPassedOver())) {
                currentCoin.setPassedOver(true);
                coins++;
                sound.playCoin();
            }
        }
    }

    public boolean ifCollision() {

        for (int i = 0; i < sheepArray.size(); ++i) {
            Sheep currentSheep = sheepArray.get(i);
            if (currentSheep.getWhereToDrawSheep().intersect(player.getWhereToDrawPlayer())) {
                return true;
            }
        }
        return false;
    }

    private void init() {

        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics); //TODO do we need this line of code?
        screenHeight = (float) metrics.heightPixels;
        screenWidth = (float) metrics.widthPixels;
        backgroundWidth = 2 * screenWidth; //TODO displayScoreboardTable in function,Do I need it here

        //cameraX = 0;
        //cameraFrame = new Rect((int) cameraX, 0, (int) (screenWidth + cameraX), (int) screenHeight);
        cameraWhereToDraw = new RectF(0, 0, screenWidth, screenHeight);

        //TODO fix the draw() method to support the new DrawableLayout objects!
        sky = new DrawableLayout(screenHeight, screenWidth, 0, R.drawable.sky, this.getResources(), (int) backgroundWidth, skySpeed);
        cloud = new DrawableLayout(screenHeight, screenWidth, 0, R.drawable.cloud, this.getResources(), (int) backgroundWidth, cloudSpeed);
        hills = new DrawableLayout(screenHeight, screenWidth, 0, R.drawable.hills, this.getResources(), (int) backgroundWidth, hillsSpeed);
        ground = new DrawableLayout(screenHeight, screenWidth, 0, R.drawable.ground, this.getResources(), (int) backgroundWidth, groundSpeed);

        // displayScoreboardTable player
        float playerX = screenWidth / 2;
        float playerY = screenHeight * 5 / 6; //TODO fix
        Bitmap playerBitmapImageFactory = BitmapFactory.decodeResource(this.getResources(), R.drawable.player);
        this.player = new Player(playerX, playerY, playerBitmapImageFactory,screenWidth, screenHeight);

        // displayScoreboardTable sheep
        float sheepX = screenWidth + 15;
        float sheepY = screenHeight * 5 / 6;
        Bitmap bitmapSheep = BitmapFactory.decodeResource(this.getResources(), R.drawable.sheep);
        this.sheepArray = new ArrayList(sheepArraySize); //TODO fix - create 1 time per game only

        // TODO fix array is not dynamic?
        for (int i = 0; i < sheepArraySize; ++i) { //TODO is the loop correct using sheepArraySize
            Random rand = new Random();
            sheepDiffLength = rand.nextInt(200) + 50; // max 200 min 50
            sheepArray.add(i, new Sheep(sheepFirstPlaceX + i * sheepSafeLength + sheepDiffLength, sheepY, bitmapSheep/*getBitmap()?*/, screenWidth, groundSpeed,screenHeight));
        }

        Bitmap bitmapCoin = BitmapFactory.decodeResource(this.getResources(), R.drawable.coin);

        this.coinArray = new ArrayList(coinArraySize);
        for (int i = 0; i < coinArraySize; ++i) {
            coinArray.add(i, new Coin(coinFirstPlaceX + i * coinSafeLength, coinY, bitmapCoin,screenHeight,screenWidth));
        }

        frameLengthInMilliseconds = 50;

        ourHolder = getHolder(); //TODO understand what is holder!! it's the surface holder, but what is it, and why does it needed?
        gameThread = null;

        score = 0;
        coins = 0;

    }

    public void updateBackground(DrawableLayout layout) {
        layout.updateLayoutX();
        layout.setCameraFrame();
    }

    public void update() {

        updateBackground(cloud);
        updateBackground(hills);
        updateBackground(ground);

        int max1 = 50;
        int max2 = 100;
        int min1 = 0;
        int min2 = 0;

        int coinsSize = 100;
        int sheepSize = 100;

        updateEntityArray(coinsSize, coinArray, max1, min1);
        updateEntityArray(sheepSize, sheepArray, max2, min2);

        player.handleJump();
        UpdateScore();
        updateCoins();

        ifCaughtCoin();

    }

    private void updateCoins() {
        for (int i = 0; i < coinArray.size(); ++i) {
            Coin currentCoin = coinArray.get(i);
            currentCoin.updateCoin();
        }
    }

    public void updateEntityArray(int entitySize, ArrayList<? extends Entity> array, int max, int min) {
        if (checkIfArrayOutOfScreen(array.get(array.size() - 1))) {
            setNewEntityArrayParam(entitySize,array, screenWidth, max, min);
        }
    }

    private boolean checkIfArrayOutOfScreen(Entity entity) {
        if (entity.getXPosition() < -10) {
            return true;
        }
        return false;
    }

    // for coins and sheep
    private void setNewEntityArrayParam(int entitySize,ArrayList<? extends Entity> array, float screenWidth, int max, int min) {
        for (int i = 0; i < array.size(); ++i) {
            Random rand = new Random();
            int randomValue = rand.nextInt(max) + min;
            Entity currentEntity = array.get(i);
            if (i == 0) {
                currentEntity.setXPosition(screenWidth + randomValue);
            } else {
                currentEntity.setXPosition(entitySize*3 + array.get(i - 1).getXPosition() + randomValue);
            }
        }
    }


    public void draw() {

        if (ourHolder.getSurface().isValid()) {

            //lock canvas
            canvas = ourHolder.lockCanvas();
            //draw background on canvas not on screen

            canvas.drawBitmap(sky.getBitmap(), sky.getCameraFrame(), cameraWhereToDraw, null);
            canvas.drawBitmap(hills.getBitmap(), hills.getCameraFrame(), cameraWhereToDraw, null);
            canvas.drawBitmap(cloud.getBitmap(), cloud.getCameraFrame(), cameraWhereToDraw, null);
            canvas.drawBitmap(ground.getBitmap(), ground.getCameraFrame(), cameraWhereToDraw, null);

            //draw player
            canvas.drawBitmap(player.getBitmap(), player.getFrameToDraw(), player.getWhereToDrawPlayer(), null);

            //draw sheep array
            //TODO fix frame to draw sheep - not initialized - what?

            for (int i = 0; i < sheepArray.size(); ++i) {
                Sheep currentSheep = sheepArray.get(i); //TODO is this a style to use currentSheep?
                canvas.drawBitmap(currentSheep.getBitmap(), frameToDrawSheep, currentSheep.getWhereToDrawSheep(), null);
            }

            for (int i = 0; i < coinArray.size(); ++i) {
                Coin currentCoin = coinArray.get(i);
                if (!currentCoin.getPassedOver()) {
                    canvas.drawBitmap(((currentCoin)).getBitmap(), frameToDrawCoin, ((currentCoin)).getWhereToDrawCoin(), null);
                }
            }

            // draw score
            Paint paint = new Paint();
            paint.setColor(Color.BLUE);
            paint.setTextSize(70);
            canvas.drawText("Score: " + score, 60, 80, paint);
            canvas.drawText("Coins: " + coins, 60, 140, paint); // TODO add a coin picture instead text

//            if (ifCollision()) {
//                //Paint paint = new Paint();
//                paint.setColor(Color.RED);
//                paint.setTextSize(300);
//                canvas.drawText("Game Over", 175, 400, paint);
//                ourHolder.unlockCanvasAndPost(canvas);
//
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException ex) {
//                    Thread.currentThread().interrupt();
//                }
//                displayScoreboardTable();
//                run();
//            }

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
                player.setIsJump(true);
                break;
            //  has removed finger from screen
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }
}



