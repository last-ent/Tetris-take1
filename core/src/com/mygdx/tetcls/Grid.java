package com.mygdx.tetcls;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.mygdx.tetcls.blocks.BlockLine;

import java.util.Arrays;

/**
 * Created by HP-430 on 2/26/2016.
 */
public class Grid {
    private Rectangle[][] grid = new Rectangle[GameConstants.GRID_COLUMNS][GameConstants.GRID_ROWS];
    private int[] surface = new int[GameConstants.GRID_COLUMNS];
    private Rectangle arena;

    public Grid(Rectangle arena) {
        this.arena = arena;
//        Arrays.fill(surface, -1);
    }

    public void register(BlockLine piece) {
        int column = -1, row = -1;
        Rectangle[] rects = piece.getBlocks();
        for(Rectangle rect : rects) {
            column = (int) ((rect.getX() - arena.getX())/GameConstants.BLOCK_SIDE_SIZE);
            row = (int) ((rect.getY() + GameConstants.BLOCK_SIDE_SIZE - arena.getY())/GameConstants.BLOCK_SIDE_SIZE);
            grid[column][row] = rect;
        }
        updateSurface(column, row);
    }
    public void updateSurface(int column, int row){
        if (column < 0) return;
        if(grid[column][row] != null) surface[column] = row;
    }

    public Rectangle[][] getGrid() {
        return grid;
    }

    public int[] getSurface() {
        return surface;
    }

    public Rectangle getTopmostRectAtColumn(int column){
        return grid[column][surface[column]];
    }

    public Rectangle getArena() {
        return arena;
    }

    //
//    public void calibrateGridSurface() {
//        for(int column = 0; column< GameConstants.GRID_COLUMNS; column++ ){
////            if (surface[column] < 0) surface[column] = 0;
//            for (int row = surface[column]; row < GameConstants.GRID_ROWS; row++ ){
//                if (grid[column][row] != null) surface[column] = row;
//            }
//        }
////        return surface;
//    }
}
