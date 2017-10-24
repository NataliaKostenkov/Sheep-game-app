package nataliacodes.sheep;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by Waveoss on 10/6/2017.
 */

public class Bob {

    private float xPosition;
    private float yPosition;
    float playerJumpHeight = 150;
    float playerGravity = (float) 0.75;   //Whenever the character falls, he will descend at this rate.
    float playerGround = 680;
    double velocity = 2 * playerJumpHeight * playerGravity;
    float playerYVelocity = (float) (Math.sqrt(velocity));
    //    float walkSpeedPerSecond = 300;
    private int bobFrameWidth = 230;
    private int bobFrameHeight = 274;
    private static final int frameCount = 5;//= 5;
    private int spriteFrameIndex;//= 0;
    private long lastFrameChangeTime;//= 0;
    private int frameLengthInMilliseconds = 50;
    private Rect spriteFrameToDrawBob = new Rect(0, 0, 100, 100);
    boolean isJump;//= false;
    boolean isFall;

    // private RectF whereToDrawBob;
    private Bitmap bitmapBob;
    // Bob methods

    // TODO how to use
    RectF whereToDrawBob;// = new RectF((int) xPosition, (int) yPosition, (int) (xPosition + bobFrameWidth / 2), (int) (yPosition + bobFrameHeight));

    public void setWhereToDrawBob(RectF whereToDrawBob) {
      whereToDrawBob.set((int) xPosition, (int) yPosition, (int) (xPosition + bobFrameWidth / 2), (int) (yPosition + bobFrameHeight));
    }

    public RectF getWhereToDrawBob() {
        setWhereToDrawBob(whereToDrawBob);
        return whereToDrawBob;
    }


    // constructor
    public Bob(float xPosition, float yPosition, Bitmap bitmapBob) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.spriteFrameIndex = 0;
        this.spriteFrameToDrawBob = new Rect(0, 0, bobFrameWidth, bobFrameHeight);
        this.isJump = false;
        this.isFall = false;
        this.whereToDrawBob = new RectF((int) xPosition, (int) yPosition, (int) (xPosition + bobFrameWidth / 2), (int) (yPosition + bobFrameHeight));

        this.bitmapBob = Bitmap.createScaledBitmap(bitmapBob, bobFrameWidth, bobFrameHeight, false);
        spriteFrameIndex = 0;
        lastFrameChangeTime = 0;
//        this.frameCount = 5;
    }


    private void incrementBitmapBob() {

        long time = System.currentTimeMillis();

        if (time > lastFrameChangeTime + frameLengthInMilliseconds) {
            lastFrameChangeTime = time;  // TODO
            spriteFrameIndex++;
            if (spriteFrameIndex >= frameCount) {
                spriteFrameIndex = 0;
            }
        }

        //updateFrame the left and right values of the source of
        //the next frame on the spritesheet
        spriteFrameToDrawBob.left = spriteFrameIndex * (bobFrameWidth / 5) - 4;
        spriteFrameToDrawBob.right = spriteFrameToDrawBob.left + (bobFrameWidth / 4) - 12;

        this.spriteFrameIndex = spriteFrameIndex;
        // this.spriteFrameToDrawBob = spriteFrameToDrawBob; //TODO

    }

    public void handleJump() {
        if (isJump) {
            if (!isFall) {
                this.yPosition = yPosition - playerYVelocity;
            }

            if ((isFall) && (!(yPosition == playerJumpHeight))) { // player is in the second stage of jump
                this.yPosition = yPosition + playerYVelocity;
            }

            // not equal because the velocity might not divide jump height
            if (yPosition < playerJumpHeight) {
                this.isFall = true;
            }
        }

        // we "finished" jumping
        if (yPosition >= playerGround) {
            this.yPosition = playerGround;
            this.isJump = false;
            this.isFall = false;
            //25.10.17
        }

    }

    // TODO check if thread safe (can be multi threaded)
    public void setIsJump(Boolean isJump) {
        this.isJump = isJump;
    }

    /**
     * Classic example of "loose coupling" and encapsulation, where the "Game Manager" is not aware to this update in the inner
     * state of bob, because the game manager only cares about the next bitmap of bob, and the internal logic of bob is "behind the scene",
     * i.e. inside Bob's implementation.
     *
     * @return
     */
    public Bitmap getBitmap() {
        incrementBitmapBob(); // WRONG USEGE
        return bitmapBob;
    }

    public Rect getFrameToDraw(){
        // incrementBitmapBob(); // WRONG USEGE
        return spriteFrameToDrawBob;
    }


    public float getXPosition() {
        return xPosition;
    }

    public float getYPosition() {
        return yPosition;
    }

    public void setXPosition(float xPosition) {
        this.xPosition = xPosition;
    }

    public void setYPosition(float sheepYPosition) {
        this.yPosition = sheepYPosition;
    }

}

