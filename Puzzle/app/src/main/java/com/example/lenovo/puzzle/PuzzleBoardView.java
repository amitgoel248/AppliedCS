package com.example.lenovo.puzzle;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

public class PuzzleBoardView extends View {
    public static final int NUM_SHUFFLE_STEPS = 40;
    private Activity activity;
    private PuzzleBoard puzzleBoard;
    private ArrayList<PuzzleBoard> animation;
    private Random random = new Random();

    private Comparator<PuzzleBoard> comparator= new Comparator<PuzzleBoard>(

    ) {
        @Override
        public int compare(PuzzleBoard lhs, PuzzleBoard rhs) {
            return lhs.priority()-rhs.priority();
        }
    };
    Context c;
    public PuzzleBoardView(Context context) {
        super(context);
        activity = (Activity) context;
        animation = null;
        c=context;
    }

    public void initialize(Bitmap imageBitmap) {
        int width = getWidth();
        Log.v("cc",width+"");
        puzzleBoard = new PuzzleBoard(imageBitmap, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (puzzleBoard != null) {
            if (animation != null && animation.size() > 0) {
                puzzleBoard = animation.remove(0);
                puzzleBoard.draw(canvas);
                if (animation.size() == 0) {
                    animation = null;
                    puzzleBoard.reset();
                    Toast toast = Toast.makeText(activity, "Solved! ", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    this.postInvalidateDelayed(500);
                }
            } else {
                puzzleBoard.draw(canvas);
            }
        }
    }

    public void shuffle() {
        Log.v("cc","shuffle called");
        if (animation == null && puzzleBoard != null) {
            Log.v("cc","shuffle if");
                for(int i = 0;i<NUM_SHUFFLE_STEPS;i++) {
                    Log.v("cc","shuffle for");
                    ArrayList<PuzzleBoard> neighbours = puzzleBoard.neighbours();
                    int randomInt = random.nextInt(neighbours.size());
                    puzzleBoard = neighbours.get(randomInt);
                }
          //  puzzleBoard.reset();
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (animation == null && puzzleBoard != null) {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (puzzleBoard.click(event.getX(), event.getY())) {
                        invalidate();
                        if (puzzleBoard.resolved()) {
                            Toast toast = Toast.makeText(activity, "Congratulations!", Toast.LENGTH_LONG);
                            toast.show();
                        }
                        return true;
                    }
            }
        }
        return super.onTouchEvent(event);
    }

    public void solve() {
        PriorityQueue<PuzzleBoard> board=new PriorityQueue<>(1,comparator);
        PuzzleBoard pboard = new PuzzleBoard(puzzleBoard,-1);
        pboard.setPrevious(null);
        board.add(pboard);
        //ArrayList<PuzzleBoard> neboard=pboa// rd.neighbours();
        int x=0;
        while(board.isEmpty()==false )
        {
            PuzzleBoard top=board.poll();
           if(top.resolved())
           {
               ArrayList<PuzzleBoard> steps = new ArrayList<>();
               while (top.getPrevious() != null) {
                   steps.add(top);
                   top = top.getPrevious();
               }
               Collections.reverse(steps);
               animation=steps;
               invalidate();
             //  Toast.makeText(c.getApplicationContext(),"Resolved",Toast.LENGTH_SHORT).show();
               break;
           }
            else
           {
               board.addAll(top.neighbours());
           }
        }


    }
}
