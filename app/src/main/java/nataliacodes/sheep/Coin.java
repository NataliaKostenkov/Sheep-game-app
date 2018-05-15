package nataliacodes.sheep;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.Random;

import static android.R.attr.width;

/**
 * Created on 10/31/2017.
 */

public class Coin extends Entity{

    private Rect frameToDrawCoin;
    private Bitmap bitmapCoin;
    private RectF whereToDrawCoin;
    private int coinSize;// = 100;
    private boolean passedOver;


    public Coin(float xPosition, float yPosition, Bitmap bitmapCoin,float screenHeight,float screenWidth) {
        super(xPosition, yPosition,screenHeight,screenWidth);
        coinSize = (int)(2*screenHeight/21);
        this.bitmapCoin = Bitmap.createScaledBitmap(bitmapCoin, coinSize, coinSize, false);
        this.whereToDrawCoin = new RectF(xPosition, yPosition, xPosition + coinSize, yPosition + coinSize);
        //this.width = screenWidth;
        this.passedOver = false;
    }

    public Bitmap getBitmap() {
        return bitmapCoin;
    }

    public RectF getWhereToDrawCoin() {
        whereToDrawCoin.set(xPosition, yPosition, xPosition + coinSize, yPosition + coinSize);
        return whereToDrawCoin;
    }

//    public void updateCoin() {
//
//        this.setXPosition(getXPosition() - 5);
//
//       // xPosition = xPosition - 5;
//        if (xPosition < 0) { // if finished the screen
//            //xPosition = 2000;
////            Random rand = new Random();
////            int randomValue = rand.nextInt(300) + coinSize*3; // max 300 min 200
//            //setNewEntityArrayParam(int size, ArrayList list,float screenSize)
//           // xPosition = (2000 + coinSize);
//            passedOver = false;
//        }
//    }

    public Coin updateCoin() {

        this.setXPosition(getXPosition() - 5);
        return this;
    }

    public void setPassedOver(boolean passedOver) {
        this.passedOver = passedOver;
    }

    public boolean getPassedOver() {
        return passedOver;
    }



}
