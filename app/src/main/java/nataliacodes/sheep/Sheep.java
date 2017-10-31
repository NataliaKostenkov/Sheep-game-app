package nataliacodes.sheep;

import android.graphics.Rect;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.graphics.RectF;

/**
 * Created by Waveoss on 9/30/2017.
 */

public class Sheep {

    private float xPosition;
    private float yPosition;
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
        whereToDrawSheep.set(xPosition, yPosition, xPosition+sheepSize, yPosition+sheepSize);
        return whereToDrawSheep;
    }


    // Sheep methods

    public float getXPosition() {
        return xPosition;
    }

    public float getYPosition() {
        return yPosition;
    }

    public boolean getPassedOver() {
        return passedOver;
    } //

    public int getCurrentFrame() {
        return currentFrame;
    }

    public Rect getFrameToDrawSheep() {
        return frameToDrawSheep;
    }

    public void setXPosition(float xPosition) {
        this.xPosition = xPosition;
    }

    public void setYPosition(float sheepYPosition) {
        this.yPosition = sheepYPosition;
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

    public Sheep(float xPosition, float yPosition, Bitmap bitmapSheep,float screenWidth) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.bitmapSheep = Bitmap.createScaledBitmap(bitmapSheep, sheepSize, sheepSize, false);
        this.whereToDrawSheep = new RectF(xPosition, yPosition, xPosition+sheepSize, yPosition+sheepSize);
        this.width = screenWidth;
        this.passedOver = false;
    }

    public Sheep updateSheep()  {

        xPosition = xPosition - 2;//(walkSpeedPerSecond / fps);

        // if finished screen
        if (xPosition < -10) {
            xPosition = width + 10;

        }
        return this; // TODO what is this?
    }

    public Bitmap getBitmap() {
        //updateSheep(); //  TODO fix
        return bitmapSheep;
    }

}
