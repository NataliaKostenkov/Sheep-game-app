package nataliacodes.sheep;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by Waveoss on 10/31/2017.
 */

public class Coin {

    private float xPosition;
    private float yPosition;
    private Rect frameToDrawCoin;
    private Bitmap bitmapCoin;
    private RectF whereToDrawCoin;
    private int coinSize = 100;
    private boolean passedOver;


    public Coin(float xPosition, float yPosition, Bitmap bitmapCoin) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.bitmapCoin = Bitmap.createScaledBitmap(bitmapCoin, coinSize, coinSize, false);
        this.whereToDrawCoin = new RectF(xPosition, yPosition, xPosition + coinSize, yPosition + coinSize);
        //this.width = screenWidth;
        this.passedOver = false;
    }

    public Bitmap getBitmap() {
        //updateCoin();
        return bitmapCoin;
    }

    public RectF getWhereToDrawCoin() {
        whereToDrawCoin.set(xPosition, yPosition, xPosition + coinSize, yPosition + coinSize);
        return whereToDrawCoin;
    }

    public void updateCoin() {
        xPosition = xPosition - 5;
        if (xPosition < -500) { // if finished the screen
            xPosition = 2000;
            passedOver = false;
        }
    }

    public void setPassedOver(boolean passedOver) {
        this.passedOver = passedOver;
    }

    public boolean getPassedOver() {
        return passedOver;
    }


}
