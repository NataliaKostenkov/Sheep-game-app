//package nataliacodes.sheep;
//
//        import java.util.ArrayList;
//
//        import static nataliacodes.sheep.R.drawable.bob;
//
///**
// * Created by Waveoss on 10/6/2017.
// * Maintains game state : number of successful jumps over sheep
// */
//
//public class GameState {
//
//    private Bob bob;
//    private Sheep sheepA;
//    private Sheep sheepB;
//
//    public int score;
//
//    public GameState(Bob bob, ArrayList sheepArray) {
//        this.bob = bob;
//        this.sheepArray = sheepArray;
//    }
//
//    private void ifScored() {
//        if (((bob.getXPosition()) > sheepA.getXPosition()) || (bob.getXPosition()) > sheepB.getXPosition())){
//            score++;
//        }
//    }
//
//    public boolean ifCollision() {
//        if ((whereToDrawSheepA.intersect(whereToDrawBob)) || ((whereToDrawSheepB.intersect(whereToDrawBob)))) {
//            return true;
//        } else
//            return false;
//    }
//
//
//}
