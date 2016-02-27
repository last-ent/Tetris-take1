package com.mygdx.tetcls;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.mygdx.tetcls.blocks.BlockLine;

/**
 * Created by HP-430 on 2/26/2016.
 */
public class Grid {
    private Rectangle[][] grid = new Rectangle[GameConstants.GRID_COLUMNS][GameConstants.GRID_ROWS];
    private Rectangle arena;

    public Grid(Rectangle arena) {
        this.arena = arena;
    }

    public void register(BlockLine piece) {
        Rectangle[] rects = piece.getBlocks();
        for(Rectangle rect : rects) {
            int x = (int) ((rect.getX() - arena.getX())/GameConstants.BLOCK_SIDE_SIZE);
            int y = (int) ((rect.getY() + GameConstants.BLOCK_SIDE_SIZE - arena.getY())/GameConstants.BLOCK_SIDE_SIZE);
            grid[x][y] = rect;
        }
    }

    public Rectangle[][] getGrid() {
        return grid;
    }
}
