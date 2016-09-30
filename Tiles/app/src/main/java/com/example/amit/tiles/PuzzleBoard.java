package com.example.amit.tiles;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.YuvImage;

import java.util.ArrayList;


public class PuzzleBoard {

    private static final int NUM_TILES = 3;
    private static final int[][] NEIGHBOUR_COORDS = {
            { -1, 0 },
            { 1, 0 },
            { 0, -1 },
            { 0, 1 }
    };
    private ArrayList<PuzzleTile> tiles;

    PuzzleBoard(Bitmap bitmap, int parentWidth) {
        tiles = new ArrayList<>();
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,parentWidth,parentWidth,true);
        for(int i=0;i<NUM_TILES;i++)
        {
            for(int j=0;j<NUM_TILES;j++) {
                int num = i * NUM_TILES + j;
                if (num != NUM_TILES * NUM_TILES - 1)
                {
                    Bitmap tileBitMap = Bitmap.createBitmap(scaledBitmap,i*scaledBitmap.getWidth()/NUM_TILES , j*scaledBitmap.getHeight()/NUM_TILES , parentWidth/NUM_TILES,parentWidth/NUM_TILES);
                    PuzzleTile tile = new PuzzleTile(tileBitMap,num);
                    tiles.add(tile);
                }
                else
                {
                    tiles.add(null);
                }
            }
        }
    }

    PuzzleBoard(PuzzleBoard otherBoard) {
        tiles = (ArrayList<PuzzleTile>) otherBoard.tiles.clone();
    }

    public void reset() {
        // Nothing for now but you may have things to reset once you implement the solver.
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        return tiles.equals(((PuzzleBoard) o).tiles);
    }

    public void draw(Canvas canvas) {
        if (tiles == null) {
            return;
        }
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile != null) {
                tile.draw(canvas, i % NUM_TILES, i / NUM_TILES);
            }
        }
    }

    public boolean click(float x, float y) {
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile != null) {
                if (tile.isClicked(x, y, i % NUM_TILES, i / NUM_TILES)) {
                    return tryMoving(i % NUM_TILES, i / NUM_TILES);
                }
            }
        }
        return false;
    }

    private boolean tryMoving(int tileX, int tileY) {
        for (int[] delta : NEIGHBOUR_COORDS) {
            int nullX = tileX + delta[0];
            int nullY = tileY + delta[1];
            if (nullX >= 0 && nullX < NUM_TILES && nullY >= 0 && nullY < NUM_TILES &&
                    tiles.get(XYtoIndex(nullX, nullY)) == null) {
                swapTiles(XYtoIndex(nullX, nullY), XYtoIndex(tileX, tileY));
                return true;
            }

        }
        return false;
    }

    public boolean resolved() {
        for (int i = 0; i < NUM_TILES * NUM_TILES - 1; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile == null || tile.getNumber() != i)
                return false;
        }
        return true;
    }

    private int XYtoIndex(int x, int y) {
        return x + y * NUM_TILES;
    }

    protected void swapTiles(int i, int j) {
        PuzzleTile temp = tiles.get(i);
        tiles.set(i, tiles.get(j));
        tiles.set(j, temp);
    }

    public ArrayList<PuzzleBoard> neighbours() {
        ArrayList<PuzzleBoard> neighbours = new ArrayList<>();
        int x_corr=0;
        int y_corr=0;
        for(int i=0;i<NUM_TILES*NUM_TILES;i++)
        {
            if(tiles.get(i)==null)
            {
                x_corr = i%NUM_TILES;
                y_corr = i/NUM_TILES;
                break;
            }
        }
        for(int delta[]:NEIGHBOUR_COORDS)
        {
            int neighbourX = x_corr + delta[0];
            int neighbourY = y_corr + delta[1];
            if((neighbourX >=0 && neighbourX <NUM_TILES  )&& (neighbourY>=0 && neighbourY<NUM_TILES))
            {
                PuzzleBoard puzzleBoard = new PuzzleBoard(this);
                puzzleBoard.swapTiles(XYtoIndex(neighbourX,neighbourY), XYtoIndex(x_corr,y_corr));
                neighbours.add(puzzleBoard);
            }
        }
        return neighbours;
    }

    public int priority() {
        return 0;
    }

}
