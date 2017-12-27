///////////////////////////////
/*

  GameView is the drawing screen manager

 */

package nataliacodes.sheep;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import static android.R.attr.data;
import static android.content.ContentValues.TAG;

public class GameManager extends SurfaceView implements Runnable {

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
    //private Rect frameToDrawBob;// = new Rect(0, 0, 100, 100); // 15.10.17  TODO rename
    private Rect frameToDrawSheep;// = new Rect (0, 0, 100, 100);
    private Rect frameToDrawCoin;
    private Player player;
    private ArrayList<Sheep> sheepArray; //20.10.17
    private ArrayList<Coin> coinArray;
    //private ArrayList<ScoreData> scoreArray;
    private float sheepSafeLength = 500; //23.10.17
    private float sheepDiffLength;
    private float coinDiffLength;
    //private float sheepFirstPlaceX = 2000;
    private int score;
    private int sheepArraySize = 3;
    private int coinArraySize = 5;
    private int scoreArraySize = 5;
    private float coinFirstPlaceX = 1200;
    private float coinSafeLength = 200;
    private float coinY = 500;
    private int coins;
    private SoundPlayer sound;
    private DrawableLayout sky;
    private DrawableLayout cloud;
    private DrawableLayout hills;
    private DrawableLayout ground;

    //TODO fix

    private int skySpeed = 0;
    private int cloudSpeed = 4;
    private int hillsSpeed = 3;
    private int groundSpeed = 5;
    private int max1 = 70;
    private int max2 = 100;
    private int min1 = 30;
    private int min2 = 50;
    private int coinsSize = 50;
    private int sheepSize = 100;
    private float firstPlace;
    private Rect cameraFrame;

    long startedTime;
    long elapsedTime;

    //GameView gameManager;

    public GameManager(Context context) {

        super(context);
        //GameView gameManager = new GameView(context);
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

            long startedTime = System.currentTimeMillis();

            update();
            draw();

            elapsedTime = System.currentTimeMillis() - startedTime;

//            TODO why do you need this code? sheep bug <---------------
            // Calculate the fps this frame
//            timeThisFrame = System.currentTimeMillis() - startFrameTime;
//            if (timeThisFrame >= 1) {
//                fps = 1000 / timeThisFrame;
//            }

        }
    }


//    public void setHighScoreFile() {
//        try {
//            File scores = new File("scores.dat");
//            scores.createNewFile();
//            BufferedReader reader = new BufferedReader(new FileReader(scores));
//            String line = reader.readLine();
//            while (line != null)                 // read the score file line by line
//            {
//                try {
//                    int score = Integer.parseInt(line.trim());   // parse each line as an int
//                    if (score > highScore)                       // and keep track of the largest
//                    {
//                        highScore = score;
//                    }
//                } catch (NumberFormatException e1) {
//                    // ignore invalid scores
//                    //System.err.println("ignoring invalid score: " + line);
//                }
//                line = reader.readLine();
//            }
//            reader.close();
//
//        } catch (IOException ex) {
//            System.err.println("ERROR reading scores from file");
//        }
//
//    }

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

    public void updateSpeed() {
        if (elapsedTime % 5000 == 0) {
            for (int i = 0; i < sheepArray.size(); ++i) { // TODO fix speed the same for all
                Sheep currentSheep = sheepArray.get(i);
                currentSheep.setSpeed(currentSheep.getSpeed());
            }
            groundSpeed = groundSpeed + 1; //TODO fix
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

    public void updateCameraBackground(float cameraX, float screenWidth, float backgroundWidth) {

        // if we finished walking the background image loop it again
        if (cameraX + screenWidth >= backgroundWidth) {
            cameraX = 0;
        } else {
            cameraX = cameraX + 5;
        }
        cameraFrame = new Rect((int) cameraX, 0, (int) (screenWidth + cameraX), (int) screenHeight);
    }

    private void init() {

        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics); //TODO do we need this line of code?
        screenHeight = (float) metrics.heightPixels;
        screenWidth = (float) metrics.widthPixels;
        backgroundWidth = 2 * screenWidth; //TODO createScoreboardTable in function,Do I need it here
        cameraWhereToDraw = new RectF(0, 0, screenWidth, screenHeight);

        sky = new DrawableLayout(screenHeight, screenWidth, 0, R.drawable.sky, this.getResources(), (int) backgroundWidth, skySpeed);
        cloud = new DrawableLayout(screenHeight, screenWidth, 0, R.drawable.cloud, this.getResources(), (int) backgroundWidth, cloudSpeed);
        hills = new DrawableLayout(screenHeight, screenWidth, 0, R.drawable.hills, this.getResources(), (int) backgroundWidth, hillsSpeed);
        ground = new DrawableLayout(screenHeight, screenWidth, 0, R.drawable.ground, this.getResources(), (int) backgroundWidth, groundSpeed);

        // createScoreboardTable player
        float playerX = screenWidth / 2;
        float playerY = screenHeight * 5 / 6;
        Bitmap playerBitmapImageFactory = BitmapFactory.decodeResource(this.getResources(), R.drawable.player);
        this.player = new Player(playerX, playerY, playerBitmapImageFactory);

        // createScoreboardTable sheep
        float sheepX = screenWidth + 15;
        float sheepY = screenHeight * 5 / 6;
        Bitmap bitmapSheep = BitmapFactory.decodeResource(this.getResources(), R.drawable.sheep);
        this.sheepArray = new ArrayList(sheepArraySize);

        for (int i = 0; i < sheepArraySize; ++i) {
            Random rand = new Random();
            sheepDiffLength = rand.nextInt(200) + 50;
            sheepArray.add(i, new Sheep(sheepX + i * sheepSafeLength + sheepDiffLength, sheepY, bitmapSheep/*getBitmap()?*/, screenWidth, groundSpeed));
        }

        Bitmap bitmapCoin = BitmapFactory.decodeResource(this.getResources(), R.drawable.coin);

        this.coinArray = new ArrayList(coinArraySize);
        for (int i = 0; i < coinArraySize; ++i) {
            Random rand = new Random();
            coinDiffLength = rand.nextInt(60) + 50; // max 200 min 50
            if (i % 2 == 0) {
                coinArray.add(i, new Coin(coinFirstPlaceX + i * coinSafeLength + coinDiffLength, coinY, bitmapCoin));
            } else {
                coinArray.add(i, new Coin(coinFirstPlaceX + i * coinSafeLength + coinDiffLength, coinY + 70, bitmapCoin));
            }
        }

        frameLengthInMilliseconds = 50;

        ourHolder = getHolder();
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

        UpdateScore();
        updateCoins();
        //ifCaughtCoin();

        updateEntityArray(coinsSize, coinArray, max1, min1);
        updateEntityArray(sheepSize, sheepArray, max2, min2);
        updateSpeed();
        player.handleJump();

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

    private void updateCoins() {

        for (int i = 0; i < coinArray.size(); ++i) {
            Coin currentCoin = coinArray.get(i);
            if ((currentCoin.getWhereToDrawCoin().intersect(player.getWhereToDrawPlayer())) && (!currentCoin.getPassedOver())) {
                currentCoin.setPassedOver(true);
                coins++;
                sound.playCoin();
            }
            if (currentCoin.getXPosition() > screenWidth) {
                currentCoin.setPassedOver(false);
            }
            currentCoin.updateCoin();
        }
    }

    public void updateEntityArray(int entitySize, ArrayList<? extends Entity> array, int max, int min) {
        if ((array.get(array.size() - 1).getXPosition()) < -10) {
            setNewEntityArrayParam(entitySize, array, screenWidth, max, min);
        }
    }

    // for coins and sheep
    private void setNewEntityArrayParam(int entitySize, ArrayList<? extends Entity> array, float screenWidth, int max, int min) {
        for (int i = 0; i < array.size(); ++i) {
            Random rand = new Random();
            int randomValue = rand.nextInt(max) + min;
            Entity currentEntity = array.get(i);
            if (i == 0) {
                firstPlace = screenWidth + randomValue;
                currentEntity.setXPosition(screenWidth + randomValue);
            } else {
                currentEntity.setXPosition(entitySize * 4 + array.get(i - 1).getXPosition() + randomValue);
            }
        }
    }


    public void draw() {


        //gameManager.draw(ourHolder);

        if (ourHolder.getSurface().isValid()) {

            //lock canvas
            canvas = ourHolder.lockCanvas(); // TODO all right to be in Game Manager
            //draw background on canvas not on screen

            canvas.drawBitmap(sky.getBitmap(), sky.getCameraFrame(), cameraWhereToDraw, null);
            canvas.drawBitmap(cloud.getBitmap(), cloud.getCameraFrame(), cameraWhereToDraw, null);
            canvas.drawBitmap(hills.getBitmap(), hills.getCameraFrame(), cameraWhereToDraw, null);
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
//                createScoreboardTable();
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



