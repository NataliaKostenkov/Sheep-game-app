package nataliacodes.sheep;

import android.graphics.Rect;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.graphics.RectF;

/**
 * Created by Waveoss on 9/30/2017.
 */

public class Sheep extends Character {

    //private float xPosition;
    // private float yPosition;
    private int currentFrame;
    private Rect frameToDrawSheep;
    private static final int sheepSize = 100; //TODO it supposed to use a specific ratio that depends on the screen size. Think how to do it!
    private Bitmap bitmapSheep;
    private RectF whereToDrawSheep;
    //    Sheep sheep ;//= new Sheep(xPosition,yPosition,currentFrame,frameToDrawSheep);
    private float width;
    private boolean passedOver;


    public RectF getWhereToDrawSheep() {
        updateSheep();
        whereToDrawSheep.set(this.getXPosition(), this.getYPosition(), this.getXPosition() + sheepSize, this.getYPosition() + sheepSize);
        return whereToDrawSheep;
    }


    // Sheep methods

//    public float getXPosition() {
//        return xPosition;
//    }
//
//    public float getYPosition() {
//        return yPosition;
//    }


    public int getCurrentFrame() {
        return currentFrame;
    }

    public Rect getFrameToDrawSheep() {
        return frameToDrawSheep;
    }

//    public void setXPosition(float xPosition) {
//        this.xPosition = xPosition;
//    }
//
//    public void setYPosition(float sheepYPosition) {
//        this.yPosition = sheepYPosition;
//    }

    public boolean getPassedOver() {
        return passedOver;
    }

    public void setPassedOver(boolean passedOver) {
        this.passedOver = passedOver;
    } //

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }

    public void setFrameToDrawSheep(Rect frameToDrawSheep) {
        this.frameToDrawSheep = frameToDrawSheep;
    }

    public Sheep(float xPosition, float yPosition, Bitmap bitmapSheep, float screenWidth) {
//        this.xPosition = xPosition;
//        this.yPosition = yPosition;
        super(xPosition, yPosition);
        this.bitmapSheep = Bitmap.createScaledBitmap(bitmapSheep, sheepSize, sheepSize, false);
        this.whereToDrawSheep = new RectF(xPosition, yPosition, xPosition + sheepSize, yPosition + sheepSize);
        this.width = screenWidth;
        this.passedOver = false;
    }

    public Sheep updateSheep() {

        // if finished screen
        if (this.getXPosition() < -10) {
            this.setXPosition(width + 10);
        }
        //xPosition = xPosition - 3;
        this.setXPosition(getXPosition() - 3);
        return this;
    }

    public Bitmap getBitmap() {
        return bitmapSheep;
    }

}
