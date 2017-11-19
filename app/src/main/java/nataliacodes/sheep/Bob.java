package nataliacodes.sheep;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by Waveoss on 10/6/2017.
 */

public class Bob extends Character{

    //private float xPosition;
    //private float yPosition;
    float playerJumpHeight = 150;
    float playerGravity = (float) 0.75;   //Whenever the character falls, he will descend at this rate.
    float playerGround = 680;
    double velocity = 2 * playerJumpHeight * playerGravity;
    float playerYVelocity = (float) (Math.sqrt(velocity));
    float jumpSpeed = 25;
    private int bobFrameWidth = 230;
    private int bobFrameHeight = 274;
    private static final int frameCount = 5;//= 5;
    private int spriteFrameIndex;//= 0;
    private long lastFrameChangeTime;//= 0;
    private int frameLengthInMilliseconds = 50;
    private Rect spriteFrameToDrawBob = new Rect(0, 0, 100, 100);
    volatile boolean isJump;//= false;
    boolean isFall;

    float previousJumpPos;
    float posJump = 0;
    int numOfFrames = 0;

    // private RectF whereToDrawBob;
    private Bitmap bitmapBob;
    // Bob methods

    // TODO how to use
    RectF whereToDrawBob;// = new RectF((int) xPosition, (int) yPosition, (int) (xPosition + bobFrameWidth / 2), (int) (yPosition + bobFrameHeight));

    public void setWhereToDrawBob(RectF whereToDrawBob) {
        whereToDrawBob.set((int) getXPosition(), (int) getYPosition(), (int) (getXPosition() + bobFrameWidth / 2), (int) (getYPosition() + bobFrameHeight));
    }

    public RectF getWhereToDrawBob() {
        setWhereToDrawBob(whereToDrawBob);
        return whereToDrawBob;
    }


    // constructor
    public Bob(float xPosition, float yPosition, Bitmap bitmapBob) {
       // this.xPosition = xPosition;
        //this.yPosition = yPosition;
        super(xPosition,yPosition);
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
        //the next frame on the sprite sheet
        spriteFrameToDrawBob.left = spriteFrameIndex * (bobFrameWidth / 5) - 4;
        spriteFrameToDrawBob.right = spriteFrameToDrawBob.left + (bobFrameWidth / 4) - 12;

        this.spriteFrameIndex = spriteFrameIndex;
        // this.spriteFrameToDrawBob = spriteFrameToDrawBob; //TODO

    }

    public void handleJump() {
        if (isJump) {
            numOfFrames += 2; // acceleration of speed?
            //yPosition -=  jumpSpeed * Math.sin(Math.PI*(numOfFrames + 90) / 180.0);
            setYPosition((float)(getYPosition() - jumpSpeed * Math.sin(Math.PI*(numOfFrames + 90) / 180.0)));
            if (getYPosition() >= playerGround - 1) {//playerJumpHeight
                //playeraction = PLAYER_STAND; -switch
                setIsJump(false);
                setYPosition(playerGround);
                numOfFrames = 0;
            }
        }

    }

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
        incrementBitmapBob(); // WRONG USAGE
        return bitmapBob;
    }

    public Rect getFrameToDraw() {
        return spriteFrameToDrawBob;
    }

}

