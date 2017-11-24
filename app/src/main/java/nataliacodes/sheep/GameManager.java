package nataliacodes.sheep;

import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Waveoss on 10/1/2017.
 * The GameManager manages the classes and the data
 * Game manager activates/updates Player class and sheep class (gives them "orders")
 */

public class GameManager {

    float backgroundWidth; // equals to 2*screenWidth
    float screenHeight;
    float screenWidth;
    float cameraX;//= 0; //24.09.17

    int max1 = 50;
    int max2 = 100;
    int min1 = 0;
    int min2 = 0;

    int coinsSize = 5;
    int sheepSize = 3;

    Rect cameraFrame;

    ArrayList sheep; // TODO is this right?

    // GameManager constructor receives instances of player and sheep
    public GameManager(Player bob, ArrayList<Sheep> sheep, GameView view) {
        GameManager game = new GameManager(bob, sheep, view);
    }

    public void updateCameraBackground(float cameraX, float screenWidth, float backgroundWidth) {

        // if we finished walking the background image loop it again
        if (cameraX + screenWidth >= backgroundWidth) {
            cameraX = 0;
        } else {
            cameraX = cameraX + 5;
        }
        cameraFrame = new Rect((int) cameraX, 0, (int) (screenWidth + cameraX), (int) screenHeight);
    }

    public void updateFrame(GameManager game) {

        updateCameraBackground(cameraX, screenWidth, backgroundWidth);

    }

//    public void updateEntityArray(int entitySize, ArrayList<Entity> array, int max, int min) {
//        if (checkIfArrayFinishedScreen(array.get(0))) {
//            setNewEntityArrayParam(entitySize, array, screenWidth, max, min);
//        }
//    }
//
//    private boolean checkIfArrayFinishedScreen(Entity entity) {
//        if (entity.getXPosition() < -10) {
//            return true;
//        }
//        return false;
//    }
//
//    // for coins and sheep
//    private void setNewEntityArrayParam(int entitySize, ArrayList<Entity> array, float screenWidth, int max, int min) {
//        Random rand = new Random();
//        int randomValue = rand.nextInt(max) + min;
//        for (int i = 0; i < array.size(); ++i) {
//            Entity currentEntity = array.get(i);
//            currentEntity.setXPosition(screenWidth + entitySize + randomValue);
//        }
//    }
//
//
//    public void update() {
//        updateEntityArray(coinsSize, coinArray, max1, min1);
//        updateEntityArray(sheepSize, sheepArray, max2, min2);
//    }


}