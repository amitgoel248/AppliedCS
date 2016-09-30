package com.example.amit.dice_example;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity {

    public int user_score=0,turn_score=0,cpu_score=0;
    int turn=1;
    ImageView imageView;
    TextView tv_user,tv_cpu,tv_turn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.dice);
        tv_cpu = (TextView) findViewById(R.id.cpu_score);
        tv_turn = (TextView) findViewById(R.id.turn_score);
        tv_user  =(TextView) findViewById(R.id.user_score);
    }

    public void reset_all(View view) {
        user_score=0;
        cpu_score=0;
        turn_score=0;
        tv_cpu.setText("0");
        tv_user.setText("0");
        tv_turn.setText("0");
        imageView.setImageResource(R.drawable.dice1);
    }

    public void hold_the_score(View view) {
        if(turn==1)
            user_score+=turn_score;
        else
            cpu_score+=turn_score;

        turn=1-turn;
        turn_score=0;
        tv_user.setText(user_score+"");
        tv_cpu.setText(cpu_score+"");
        if(turn==0)
            computer_turn();
    }

    private void computer_turn() {
        while(turn_score<20 && turn==0)
        {
            roll_the_dice_no_view();
        }
        if(turn==0)
        {
            cpu_score+=turn_score;
            tv_cpu.setText(cpu_score+"");
            turn_score=0;
            turn=1-turn;
        }
        else
        {
            turn_score=0;
        }
    }

    private void roll_the_dice_no_view() {
        Random rn = new Random();
        int val = rn.nextInt(6) + 1 ;
        if(val==1)
        {
            imageView.setImageResource(R.drawable.dice1);
            turn_score=0;
            turn=1-turn;
            if(turn==0)
                computer_turn();
        }
        else if(val==2)
        {
            imageView.setImageResource(R.drawable.dice2);
            turn_score+=2;
            tv_turn.setText(turn_score+"");
        }
        else if(val==3)
        {
            imageView.setImageResource(R.drawable.dice3);
            turn_score+=3;
            tv_turn.setText(turn_score+"");
        }
        else if(val==4)
        {
            imageView.setImageResource(R.drawable.dice4);
            turn_score+=4;
            tv_turn.setText(turn_score+"");
        }
        else if(val==5)
        {
            imageView.setImageResource(R.drawable.dice5);
            turn_score+=5;
            tv_turn.setText(turn_score+"");
        }
        else
        {
            imageView.setImageResource(R.drawable.dice6);
            turn_score+=6;
            tv_turn.setText(turn_score+"");
        }
    }


    public void roll_the_dice(View view) {
      roll_the_dice_no_view();
    }
}
