package com.example.amit.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    TextView ghost_text,status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);


        ghost_text = (TextView) findViewById(R.id.ghostText);
        status = (TextView) findViewById(R.id.gameStatus);

        AssetManager assetManager = getAssets();
//        try {
//            InputStream inputStream = assetManager.open("words.txt");
//            dictionary = new FastDictionary(inputStream);
//        } catch (IOException e) {
//            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
//            toast.show();
//        }

        try {
            InputStream inputStream = assetManager.open("words.txt");
            dictionary = new SimpleDictionary(inputStream);
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }

        onStart(null);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        int ch = event.getUnicodeChar();
        if(ch>=97 && ch<=122)
        {
            char c = (char) ch;
            ghost_text.append(c+"");
            computerTurn();
        }
        return super.onKeyUp(keyCode, event);
    }

    private boolean check_string(String current_text) {
        return dictionary.isWord(current_text);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    private void computerTurn() {
        TextView label = (TextView) findViewById(R.id.gameStatus);
        // Do computer turn stuff then make it the user's turn again

        String current_text = ghost_text.getText().toString();
        if(current_text.length()>=4 && check_string(current_text))
        {
            status.setText("Victory");
        }
        else {
            String long_word = dictionary.getAnyWordStartingWith(current_text);
            if (long_word == null) {
                status.setText("Computer Challenged you (CPU wins)");
            } else {
                int len = current_text.length();
                String sub = long_word.substring(0, len+1);
                ghost_text.setText(sub);
                userTurn = true;
                label.setText(USER_TURN);
            }
        }
    }

    public void reset_everything(View view) {
        ghost_text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        label.setText(USER_TURN);
    }

    public void challenge(View view) {

        String current_text = ghost_text.getText().toString();
        if(current_text.length()>=4 && check_string(current_text))
        {
            status.setText("CPU wins");
        }
        else {
            String long_word = dictionary.getAnyWordStartingWith(current_text);
            if (long_word == null) {
                status.setText("You Wins");
            } else {
                ghost_text.setText(long_word);
                status.setText("CPU wins");
            }
        }
    }
}
