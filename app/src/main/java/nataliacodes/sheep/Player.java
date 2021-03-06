package nataliacodes.sheep;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created on 10/6/2017.
 */

public class Player extends Entity {

    private int playerFrameWidth;// = (int)(230*1.2);//345;//230*1.5;
    private int playerFrameHeight;// = (int)(274*1.2);//411;//274*1.5;
    //private float playerJumpHeight = playerFrameHeight/2;//150;
    //private float playerGravity = (float) 0.75;   //Whenever the character falls, he will descend at this rate.
    private float playerGround;
    //private double velocity = 2 * playerJumpHeight * playerGravity;
    //private float playerYVelocity = (float) (Math.sqrt(velocity));
    private float jumpSpeed = 20;
    private static final int frameCount = 5;
    private int spriteFrameIndex;//= 0;
    private long lastFrameChangeTime;//= 0;
    private int frameLengthInMilliseconds = 70; // was 50
    private Rect spriteFrameToDrawPlayer;//= new Rect(0, 0, 100, 100);
    volatile boolean isJump;//= false;
    private boolean isFall;
    private int numOfFrames = 0;
    private Bitmap bitmapPlayer;

    // Player methods

    public RectF getWhereToDrawPlayer() {
        return getWhereToDraw(playerFrameWidth / 2, playerFrameHeight);
    }

    // Player constructor
    public Player(float xPosition, float yPosition, Bitmap bitmapPlayer, float screenHeight, float screenWidth) {
        super(xPosition, yPosition, screenHeight, screenWidth);
        this.spriteFrameIndex = 0;
        playerFrameWidth = (int)(screenWidth/6);
        playerFrameHeight = (int)(2*screenHeight/7);
        this.spriteFrameToDrawPlayer = new Rect(0, 0, playerFrameWidth, playerFrameHeight);
        this.isJump = false;
        this.isFall = false;
        this.bitmapPlayer = Bitmap.createScaledBitmap(bitmapPlayer, playerFrameWidth, playerFrameHeight, false);
        spriteFrameIndex = 0;
        lastFrameChangeTime = 0;
        playerGround = yPosition;
    }


    private void incrementBitmapPlayer() {

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
//        spriteFrameToDrawPlayer.left = spriteFrameIndex * (playerFrameWidth / 5) - 4;
//        spriteFrameToDrawPlayer.right = spriteFrameToDrawPlayer.left + (playerFrameWidth / 4) - 12;

        spriteFrameToDrawPlayer.left = spriteFrameIndex * (playerFrameWidth / 5) - 4;
        spriteFrameToDrawPlayer.right = spriteFrameToDrawPlayer.left + (playerFrameWidth / 4) - 12;

        //this.spriteFrameIndex = spriteFrameIndex;
        //this.spriteFrameToDrawPlayer = spriteFrameToDrawPlayer;

    }

    public void handleJump() {
        if (isJump) {
            numOfFrames += 2; // acceleration of speed?
            setYPosition((float) (getYPosition() - jumpSpeed * Math.sin(Math.PI * (numOfFrames + 90) / 180.0)));
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
     * state of player, because the game manager only cares about the next bitmap of player, and the internal logic of player is "behind the scene",
     * i.e. inside player's implementation.
     *
     * @return
     */

    public Bitmap getBitmap() {
        incrementBitmapPlayer(); // WRONG USAGE
        return bitmapPlayer;
    }

    public Rect getFrameToDraw() {
        return spriteFrameToDrawPlayer;
    }

}

