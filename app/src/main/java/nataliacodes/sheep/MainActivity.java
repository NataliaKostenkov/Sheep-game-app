
package nataliacodes.sheep;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.media.MediaPlayer;

public class MainActivity extends AppCompatActivity {

    MediaPlayer music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //02.09.17
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        super.onCreate(savedInstanceState);
        // removes action bar
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        // removes notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

//        music = MediaPlayer.create(MainActivity.this,R.raw.music);
//        music.setLooping(true);
//        music.start();

    }

    //ImageButton start = (ImageButton)findViewById(R.id.btnStart);


    /*
    start.setOnClickListener(new OnClickListener(){
    @Override
    public void onClick(View v) {
    // Start Game!!!! //
    Intent game = new Intent(getApplicationContext(),PBGame.class);
    PBMainMenu.this.startActivity(game);
     */

    // Called when the user taps the start_button
    public void startGame(View view) {
        //Intent game = new Intent(this, GameActivity.class);
        Intent game = new Intent(this, GameActivity.class);
        startActivity(game);
    }

    // Called when the user taps the start_button
    public void startScoreBoard(View view) {
        //Intent game = new Intent(this, GameActivity.class);
        Intent scoreboard = new Intent(this, Scoreboard.class);
        startActivity(scoreboard);
    }

    // Called when the user taps the quit_button
    public void quitGame(View view) {

        finish();
    }

/*
@Override
protected void onResume() {
super.onResume();
gameView.onResume();
}
@Override
protected void onPause() {
super.onPause();
gameView.onPause();
}


 */

//    @Override
//    protected void onPause() {
//        super.onPause();
//        music.release();
//        finish();
//    }

}



