package nataliacodes.sheep;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Waveoss on 10/1/2017.
 * <p>
 * The GameManager manages the classes and the data
 * Game manager activates/updates Bob class and sheep class (gives them "orders")
 */

public class GameManager {

    Thread gameThread = null;
    SurfaceHolder ourHolder;
    volatile boolean playing;
    Canvas canvas;
    long fps;
    private long timeThisFrame;// Used to help calculate the fps
    float backgroundWidth; // equals to 2*screenWidth
    float screenHeight;
    float screenWidth;
    float cameraX;//= 0; //24.09.17
    Bitmap bitmapBob;
    Bitmap transheep;
    Bitmap bitmapBackground;
    boolean isJump;//= false;
    boolean isFall;//= false;
    float bobXPosition;
    float bobYPosition;
    float sheepXPosition;
    float sheepYPosition;
    float playerJumpHeight = 150;
    float playerGravity = (float) 0.75;   //Whenever the character falls, he will descend at this rate.
    float playerGround = 680;
    double velocity = 2 * playerJumpHeight * playerGravity;
    float playerYVelocity = (float) (Math.sqrt(velocity));
    float walkSpeedPerSecond = 300;
    private int bobFrameWidth = 230;
    private int bobFrameHeight = 274;
    private int frameCount;//= 5;
    private int currentFrame;//= 0;
    private long lastFrameChangeTime;//= 0;
    private int frameLengthInMilliseconds = 50;
    private Rect frameToDrawBob = new Rect(0, 0, 100, 100);
    private Rect frameToDrawSheep = new Rect(0, 0, 100, 100);
    Rect cameraFrame;
    RectF whereToDrawBob;
    RectF whereToDrawSheep;
    RectF cameraWhereToDraw;
    int sheepSize = 100;
    ArrayList sheep; // TODO is this right?
    private int arraySize = 3;
    private float sheepDiffLength;
    private float sheepFirstPlaceX = 2000;
    private float sheepSafeLength = 500;
    float sheepY = screenHeight / 2 + screenHeight / 4; //TODO fix

    // GameManager constructor receives instances of bob and sheep
    public GameManager(Bob bob, ArrayList<Sheep> sheep, GameView view) {
        GameManager game = new GameManager(bob, sheep, view);
    }

//    public void createNewSheepArray() { // init game manager? constructor?
//        Bitmap bitmapSheep = BitmapFactory.decodeResource(this.getResources(), R.drawable.transheep);
//        sheep = new ArrayList(arraySize); //TODO fix - create 1 time per game only
//        for (int i = 0; i < arraySize; ++i) { //TODO is the loop correct using arraySize
//            Random rand = new Random();
//            sheepDiffLength = rand.nextInt(200) + 10; // max 200 min 10
//            sheep.add(i, new Sheep(sheepFirstPlaceX + i * sheepSafeLength + sheepDiffLength, sheepY, bitmapSheep, screenWidth));
//        }
//
//    }

    public void updateCameraBackground(float cameraX, float screenWidth, float backgroundWidth) {

        // if we finished walking the background image loop it again
        if (cameraX + screenWidth >= backgroundWidth) {
            cameraX = 0;
        } else {
            cameraX = cameraX + 5;
        }
        cameraFrame = new Rect((int) cameraX, 0, (int) (screenWidth + cameraX), (int) screenHeight);
    }

//    public void incrementBitmapBob(long lastFrameChangeTime,long frameLengthInMilliseconds,int currentFrame,int frameCount) {
//
//        long time = System.currentTimeMillis();
//
//        if (time > lastFrameChangeTime + frameLengthInMilliseconds) {
//            lastFrameChangeTime = time;
//            currentFrame++;
//            if (currentFrame >= frameCount) {
//                currentFrame = 0;
//            }
//        }
//
//        //updateFrame the left and right values of the source of
//        //the next frame on the spritesheet
//        frameToDrawBob.left = currentFrame * (bobFrameWidth / 5) - 4;
//        frameToDrawBob.right = frameToDrawBob.left + (bobFrameWidth / 4) - 12;
//
//    }

//    public void updateSheep(float sheepXPosition,float screenWidth) {
//        sheepXPosition = sheepXPosition - 5;//(walkSpeedPerSecond / fps);
//
//        if (sheepXPosition < 10) {
//            sheepXPosition = screenWidth + 10;
//        }
//
//    }


//    public void updateJump(boolean isJump,boolean isFall,float bobYPosition,float playerYVelocity,float playerJumpHeight) {
//        if (isJump) {
//            if (!isFall) {
//                bobYPosition = bobYPosition - playerYVelocity;
//            }
//
//            if ((isFall) && (!(bobYPosition == playerJumpHeight))) { // player is in the second stage of jump
//                bobYPosition = bobYPosition + playerYVelocity;
//            }
//
//            if (bobYPosition == playerJumpHeight) { // TO DO check if needed
//                // no change to bobYPosition
//            }
//
//            // not equal because the velocity might not divide jump height
//            if (bobYPosition < playerJumpHeight) {
//                isFall = true;
//            }
//        }
//
//        // we "finished" jumping
//        if (bobYPosition >= playerGround) {
//            bobYPosition = playerGround;
//            isJump = false;
//            isFall = false;
//        }
//
//    }

    //
    public void updateFrame(GameManager game) {

        updateCameraBackground(cameraX, screenWidth, backgroundWidth);
        //incrementBitmapBob(lastFrameChangeTime,frameLengthInMilliseconds,currentFrame,frameCount);
//        updateSheep(sheepXPosition,screenWidth);
//        updateJump(isJump,isFall,bobYPosition,playerYVelocity,playerJumpHeight);
        // whereToDrawBob;
        // whereToDrawSheep;
        // cameraWhereToDraw;
    }
}