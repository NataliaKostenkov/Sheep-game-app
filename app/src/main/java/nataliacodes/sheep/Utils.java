package nataliacodes.sheep;

import android.app.Activity;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;

import java.util.Random;

/**
 * Created on 10/1/2017.
 */

public class Utils {

    private int randomAmount = 0;

    public int getRandomNum() {

        Random rand = new Random();
        randomAmount = rand.nextInt(200) + 50;
        //200 is the maximum and the 50 is our minimum
        return randomAmount;
    }


}
