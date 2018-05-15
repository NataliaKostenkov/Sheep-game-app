package nataliacodes.sheep;

import android.graphics.Rect;
import android.graphics.Bitmap;
import android.graphics.RectF;

/**
 * Created on 9/30/2017.
 */

public class Sheep extends Entity {

    private int currentFrame;
    private Rect frameToDrawSheep;
    private static int sheepSize;// = 150;
    private Bitmap bitmapSheep;
    private float width;
    private float height;
    private boolean passedOver;
    private int speed;


    public RectF getWhereToDrawSheep() {
        return getWhereToDraw(sheepSize, sheepSize);
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public Rect getFrameToDrawSheep() {
        return frameToDrawSheep;
    }

    public boolean getPassedOver() {
        return passedOver;
    }

    public void setPassedOver(boolean passedOver) {
        this.passedOver = passedOver;
    } //

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }

    public Sheep(float xPosition, float yPosition, Bitmap bitmapSheep, float screenWidth, int speed,float screenHeight) {
        super(xPosition, yPosition,screenHeight,screenWidth);
        sheepSize = (int)(screenHeight/7);
        this.bitmapSheep = Bitmap.createScaledBitmap(bitmapSheep, sheepSize, sheepSize, false);
        this.width = screenWidth;
        //this.height = screenHeight;
        this.passedOver = false;
        this.speed = speed;
    }

    public int getSpeed() {
       return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed + 1;
    }

    public Sheep updateSheep() {
        this.setXPosition(getXPosition() - speed);
        return this;
    }

    public Bitmap getBitmap() {
        return bitmapSheep;
    }

}
