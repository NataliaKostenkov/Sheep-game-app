///////////////////////////////
/*

  GameView is the drawing screen manager

 */

package nataliacodes.sheep;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import static android.widget.Toast.LENGTH_LONG;

public class GameManager extends SurfaceView implements Runnable {

    public static final int milisecondsIntervalPassed = 2000;
    private Thread gameThread = null;
    private SurfaceHolder surfaceHolder;
    private volatile boolean playing;
    private Canvas canvas;
    private long timeThisFrame;// Used to help calculate the fps
    private float backgroundWidth;
    private float screenHeight;
    private float screenWidth;
    RectF cameraWhereToDraw;
    private Rect frameToDrawSheep;
    private Rect frameToDrawCoin;
    private Player player;
    private ArrayList<Sheep> sheepArray;
    private ArrayList<Coin> coinArray;
    private float sheepSafeLength;// = 500;
    private float sheepDiffLength;
    private float coinDiffLength;
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

    private int skySpeed = 0;
    private int cloudSpeed = 4;
    private int hillsSpeed = 3;
    private int groundSpeed = 5;
    private int max1 = 70;
    private int max2 = 100;
    private int min1 = 30;
    private int min2 = 50;
    private int coinSize;// = 50;
    private int sheepSize;// = 100;
    private float firstPlace;
    private Rect cameraFrame;

    private String name;

    long startedTime;
    long elapsedTime;

    DatabaseHandler db;

    private int groundSpeedIncrement = 1;

    private Handler mHandler;

    CharSequence text = "Got it!";
    private Toast toast;

    private AtomicBoolean finished;

    private final Object lock = new Object();

    public GameManager(final Context context) {

        super(context);
        name = null;
        db = new DatabaseHandler(context);
        initDisplayConfiguration();
        surfaceHolder = getHolder();
        initializeGame();

        playing = true;

        sound = new SoundPlayer(context);

//        // Defines a Handler object that's attached to the UI thread
//        mHandler = new Handler(Looper.getMainLooper()) {
//            @Override
//            public void handleMessage(Message msg) {
////                super.handleMessage(msg);
//                // Use the Builder class for convenient dialog construction
//                AddNewScoreToDB.Builder builder = new AddNewScoreToDB.Builder(context);
////                LayoutInflater inflater = getActivity().getLayoutInflater();
//                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                builder.setView(inflater.inflate(R.layout.dialog_name, null));
////                builder.setMessage(R.string.dialog_ask_name)
//
//                final EditText input = new EditText(context);
//                builder.setView(input);
//
//                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        String value = input.getText().toString();
//                        name = value;
//
////                        setName(value);
//
////                        finished = true;
//                        Toast toast = Toast.makeText(context, text, LENGTH_LONG);
//                        toast.show();
//
//                    }
//                });
////                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
////                                public void onClick(DialogInterface dialog, int id) {
////                                    // User cancelled the dialog
////                                }
////                            });
//                // Create the AddNewScoreToDB object
////                builder.create();
//
//                AddNewScoreToDB dialog = builder.create();
//
//                dialog.show();
//            }
//        };
    }

    //
    private synchronized void setName(String newName) {
        this.name = newName;
    }
//
//    private synchronized String getNameFromUser(Context context) {
//        AddNewScoreToDB.Builder builder = new AddNewScoreToDB.Builder(context);
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        builder.setView(inflater.inflate(R.layout.dialog_name, null));
//        final EditText input = new EditText(context);
//        builder.setView(input);
//        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                String value = input.getText().toString();
//                name = value;

//                setName(value);

//                Toast toast = Toast.makeText(context, text, LENGTH_LONG);
//                toast.show();
//            }
//        });

//        AddNewScoreToDB dialog = builder.create();
//        dialog.show();
//
//        return name;
//
//    }

    @Override
    public void run() {

        initializeGame();

//        while(name == null) {
//            getNameFromUser(getContext());
//        }

        while (playing) {

            startedTime = System.currentTimeMillis();

            update();
            draw();

            elapsedTime = System.currentTimeMillis() - startedTime;
        }

    }

    public void updateSpeed() {
        if (elapsedTime % milisecondsIntervalPassed == 0) {
            for (int i = 0; i < sheepArray.size(); ++i) {
                Sheep currentSheep = sheepArray.get(i);
                currentSheep.setSpeed(currentSheep.getSpeed());
            }
            groundSpeed = groundSpeed + groundSpeedIncrement;
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

    private void initializeGame() {

        createBackground();
        createPlayer();
        createSheep();
        createCoins();
        score = 0;
        coins = 0;
    }

    private void createCoins() {
        Bitmap bitmapCoin = BitmapFactory.decodeResource(this.getResources(), R.drawable.coin);

        this.coinArray = new ArrayList(coinArraySize);
        for (int i = 0; i < coinArraySize; ++i) {
            Random rand = new Random();
            coinDiffLength = rand.nextInt(60) + 50; //TODO
            if (i % 2 == 0) {
                coinArray.add(i, new Coin(coinFirstPlaceX + i * coinSafeLength + coinDiffLength, coinY, bitmapCoin, screenHeight, screenWidth));
            } else {
                coinArray.add(i, new Coin(coinFirstPlaceX + i * coinSafeLength + coinDiffLength, coinY + 70, bitmapCoin, screenHeight, screenWidth));
            }
        }
    }

    private void initDisplayConfiguration() {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenHeight = (float) metrics.heightPixels;
        screenWidth = (float) metrics.widthPixels;
        backgroundWidth = 2 * screenWidth;
        cameraWhereToDraw = new RectF(0, 0, screenWidth, screenHeight);

        coinSize = (int) (2 * screenHeight / 21);
        sheepSize = (int) (screenHeight / 7);

    }

    private void createBackground() {
        sky = new DrawableLayout(screenHeight, screenWidth, 0, R.drawable.sky, this.getResources(), (int) backgroundWidth, skySpeed);
        cloud = new DrawableLayout(screenHeight, screenWidth, 0, R.drawable.cloud, this.getResources(), (int) backgroundWidth, cloudSpeed);
        hills = new DrawableLayout(screenHeight, screenWidth, 0, R.drawable.hills, this.getResources(), (int) backgroundWidth, hillsSpeed);
        ground = new DrawableLayout(screenHeight, screenWidth, 0, R.drawable.ground, this.getResources(), (int) backgroundWidth, groundSpeed);
    }

    private void createPlayer() {
        float playerX = screenWidth / 2;
        float playerY = screenHeight * 5 / 6;
        Bitmap playerBitmapImageFactory = BitmapFactory.decodeResource(this.getResources(), R.drawable.player);
        this.player = new Player(playerX, playerY, playerBitmapImageFactory, screenHeight, screenWidth);
    }

    private void createSheep() {
        float sheepX = screenWidth + 15;
        float sheepY = screenHeight * 5 / 6;

        Bitmap bitmapSheep = BitmapFactory.decodeResource(this.getResources(), R.drawable.sheep);
        this.sheepArray = new ArrayList(sheepArraySize);
        sheepSafeLength = screenWidth / 4;

        for (int i = 0; i < sheepArraySize; ++i) {
            Random rand = new Random();
            sheepDiffLength = rand.nextInt(200) + 50;
            sheepArray.add(i, new Sheep(sheepX + i * sheepSafeLength + sheepDiffLength, sheepY, bitmapSheep/*getBitmap()?*/, screenWidth, groundSpeed, screenHeight));
        }
    }

    public void updateBackground(DrawableLayout layout) {
        layout.updateLayoutX();
        layout.setCameraFrame();
    }

    public void update() {

        updateBackground(cloud);
        updateBackground(hills);
        updateBackground(ground);
        updateCurrentScore();
        updateCoins();
        updateEntityArray(coinSize, coinArray, max1, min1);
        updateEntityArray(sheepSize, sheepArray, max2, min2);
        updateSpeed();
        player.handleJump();

    }

    private void updateCurrentScore() {

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

        if (surfaceHolder.getSurface().isValid()) {

            //lock canvas
            canvas = surfaceHolder.lockCanvas();

            drawBackground();
            drawPlayer();
            drawSheeps();
            drawCoins();

            Paint paint = buildPaint();

            drawScore(paint);

            if (ifCollision()) {
                handleGameOver(paint, getContext());
//                run(); #TODO noet that it was enabled - 26.04
            } else {
                // Draw everything to the screen
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    @NonNull
    private Paint buildPaint() {
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setTextSize(70);
        return paint;
    }

    private void drawPlayer() {
        canvas.drawBitmap(player.getBitmap(), player.getFrameToDraw(), player.getWhereToDrawPlayer(), null);
    }

    private void handleGameOver(Paint paint, Context context) {

//        mHandler.post(null);
//        db.addScore(new Score(name, score), null);
        //1) get the name of the user
        String username = GetUserNameActivity.getUserName();
        //2) save the username and the score to the db (by utilizing the same db handler)
        AddNewScoreToDB newScoreHandler = new AddNewScoreToDB(score, username);

        paint.setColor(Color.RED);
        paint.setTextSize(300);
        canvas.drawText("Game Over", 175, 400, paint);
        surfaceHolder.unlockCanvasAndPost(canvas);

        playing = false;

//        }

//        try {
//            gameThread.sleep(3000);
//        } catch (InterruptedException ex) {
//            gameThread.currentThread().interrupt();
//        }

//        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

    }


    private void drawScore(Paint paint) {
        //canvas.drawBitmap(sheepArray.get(0).getBitmap(), frameToDrawSheep, new RectF(60, 70, 60 + 50, 70 + 50), null);
        //canvas.drawText("" + score, 60, 80, paint);
        canvas.drawText("Score: " + score, 60, 80, paint);
        //canvas.drawBitmap((coinArray.get(0)).getBitmap(), frameToDrawCoin,new RectF(60, 90, 60 + 50, 90 + 50), null);
        //canvas.drawText("" + coins, 60, 140, paint); // TODO add a coin picture instead text
    }

    private void drawCoins() {
        for (int i = 0; i < coinArray.size(); ++i) {
            Coin currentCoin = coinArray.get(i);
            if (!currentCoin.getPassedOver()) {
                canvas.drawBitmap(((currentCoin)).getBitmap(), frameToDrawCoin, ((currentCoin)).getWhereToDrawCoin(), null);
            }
        }
    }

    private void drawSheeps() {
        for (int i = 0; i < sheepArray.size(); ++i) {
            Sheep currentSheep = sheepArray.get(i); //TODO is this a style to use currentSheep?
            canvas.drawBitmap(currentSheep.getBitmap(), frameToDrawSheep, currentSheep.getWhereToDrawSheep(), null);
        }
    }

    private void drawBackground() {
        //draw background on canvas not on screen
        canvas.drawBitmap(sky.getBitmap(), sky.getCameraFrame(), cameraWhereToDraw, null);
        canvas.drawBitmap(cloud.getBitmap(), cloud.getCameraFrame(), cameraWhereToDraw, null);
        canvas.drawBitmap(hills.getBitmap(), hills.getCameraFrame(), cameraWhereToDraw, null);
        canvas.drawBitmap(ground.getBitmap(), ground.getCameraFrame(), cameraWhereToDraw, null);
    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }
//        gameThread.interrupt();
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







