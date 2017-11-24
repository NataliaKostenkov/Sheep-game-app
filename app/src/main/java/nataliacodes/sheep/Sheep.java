package nataliacodes.sheep;

import android.graphics.Rect;
import android.graphics.Bitmap;
import android.graphics.RectF;

/**
 * Created by Waveoss on 9/30/2017.
 */

public class Sheep extends Entity {

    //private float xPosition;
    // private float yPosition;
    private int currentFrame;
    private Rect frameToDrawSheep;
    private static final int sheepSize = 100; //TODO it supposed to use a specific ratio that depends on the screen size. Think how to do it!
    private Bitmap bitmapSheep;
//    private RectF whereToDrawSheep;
    //    Sheep sheep ;//= new Sheep(xPosition,yPosition,currentFrame,frameToDrawSheep);
    private float width;
    private boolean passedOver;
    private int speed;


    public RectF getWhereToDrawSheep() {
        //updateSheep();
//        whereToDrawSheep.set(this.getXPosition(), this.getYPosition(), this.getXPosition() + sheepSize, this.getYPosition() + sheepSize);
        return getWhereToDraw(sheepSize, sheepSize);
//        RectF rect = new RectF(xPosition - sheepSize, yPosition - sheepSize, xPosition , yPosition);
//        return rect;
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

    public Sheep(float xPosition, float yPosition, Bitmap bitmapSheep, float screenWidth, int speed) {
//        this.xPosition = xPosition;
//        this.yPosition = yPosition;
        super(xPosition, yPosition);
        this.bitmapSheep = Bitmap.createScaledBitmap(bitmapSheep, sheepSize, sheepSize, false);
//        this.whereToDrawSheep = new RectF(xPosition - sheepSize, yPosition - sheepSize, xPosition , yPosition );
        this.width = screenWidth;
        this.passedOver = false;
        this.speed = speed;
    }

    public Sheep updateSheep() {

        // if finished screen
//        if (this.getXPosition() < -10) { //only for first sheep
////            Random rand = new Random();
////            int randomValue = rand.nextInt(100) + 200; // max 300 min 200
//            //this.setNewEntityArrayParam(int size, ArrayList list,float screenSize)
//            this.setXPosition(width + sheepSize );
//        }
        //xPosition = xPosition - 3;
        this.setXPosition(getXPosition() - speed);
        return this;
    }

    public Bitmap getBitmap() {
        return bitmapSheep;
    }

}
