package nataliacodes.sheep;

/**
 * Created by Waveoss on 10/6/2017.
 * Maintains game state : number of successful jumps over sheep
 */

public class GameState {





  public int score;


    private void updateScore(){
     score++;
    }

    private void ifScored(){

    }

    public boolean ifCollision() {
        if ((whereToDrawSheepA.intersect(whereToDrawBob)) || ((whereToDrawSheepB.intersect(whereToDrawBob)))) {
            return true;
        }
        else
            return false;
    }


}
