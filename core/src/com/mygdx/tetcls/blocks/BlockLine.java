package com.mygdx.tetcls.blocks;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.mygdx.tetcls.GameConstants;
import com.mygdx.tetcls.Grid;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by HP-430 on 2/25/2016.
 */

/***
 * DONE: Next is to write a comparator/sorter so that the blocks are returned in order of x & y.
 */
public class BlockLine {
    private Rectangle[] rects = new Rectangle[4];
    private boolean isResting = false;
    private boolean isVertical = true;
    private boolean isFast = false;
    private static enum RowOrColumn {
        ROW, COLUMN
    }
    private static Comparator<Rectangle> BlockLineComparator = new Comparator<Rectangle>() {
        @Override
        public int compare(Rectangle o1, Rectangle o2) {
            float compareValue = o1.getY() - o2.getY();

            if (compareValue == 0)
                compareValue = o1.getX() - o2.getX();

            if (compareValue > 0) compareValue = +1;
            else if (compareValue < 0) compareValue = -1;
            else compareValue = 0;

            return (int) compareValue;
        }
    };

    public BlockLine(float x, float y) {
        for(int i = 0; i < GameConstants.BLOCK_NUM; i++) {
            rects[i] =new Rectangle(0, 0, GameConstants.BLOCK_SIDE_SIZE, GameConstants.BLOCK_SIDE_SIZE);
        }
        setX(x);
        setY(y - GameConstants.BLOCK_SIDE_SIZE*3, GameConstants.BLOCK_SIDE_SIZE);
    }
    public void setX(float x) {
        if(isVertical)
            for(Rectangle rect : getBlocks()) rect.setX(x);
        else {
            int step = 0;
            for(Rectangle rect : getBlocks()) {
                rect.setX(x + step);
                step += GameConstants.BLOCK_SIDE_SIZE;
            }

        }
    }

    /***
     *
     * @param y - Rendering of the blocks start from BLOCK_SIDE_SIZE pixels below the given value of y.
     * @param step
     */
    public void setY(float y, float step){
        if(isVertical) {
            for (Rectangle rect : rects) {
                rect.setY(y - step);
                step -= GameConstants.BLOCK_SIDE_SIZE;
            }
        } else {
            for(Rectangle rect : rects)
                rect.setY(y - step);
        }
    }

    public float getX(){
        return getLowestBlock().getX();
    }
    public void correctYAfterFast(){
        float y = getLowestBlock().getY();
//        System.out.println(y);
        float position = y / GameConstants.BLOCK_SIDE_SIZE;
        int next = (int) position;
        int  min= next +1;
        float limbo = position - next;
        float newY = (limbo >= .5f? next : min) * GameConstants.BLOCK_SIDE_SIZE;
//        System.out.println("min: "+ min + " next: " + next + " newY: " + newY+ " position: "+ position + " limbo: " + limbo);
//        System.out.println(y - newY  );
//        System.out.println("\tmin: "+ min * GameConstants.BLOCK_SIDE_SIZE+ " next: " + next * GameConstants.BLOCK_SIDE_SIZE+ " newY: " + newY* GameConstants.BLOCK_SIDE_SIZE);
        setY(newY, GameConstants.BLOCK_SIDE_SIZE);
    }

    public void updateOnSecond() {
        if(isFast){
            isFast = false;
            correctYAfterFast();
        } else {
            setY(getLowestBlock().getY(), GameConstants.BLOCK_SIDE_SIZE);
        }
        setX(getLeftmostBlock().getX());
    }

    public void updateOnDelta(float delta, boolean toTurn, boolean toLeft,
                              boolean toRight, boolean isDownPressed, Grid gameGrid) {
        if (toTurn) isVertical = !isVertical;
        if (toLeft) setX(getLeftmostBlock().getX() - GameConstants.BLOCK_SIDE_SIZE);
        else if (toRight) setX(getLeftmostBlock().getX() + GameConstants.BLOCK_SIDE_SIZE);
        if(isDownPressed) isFast = true;
        if (isFast)
            setY(
                getLowestBlock().getY(),
                GameConstants.BLOCK_SIDE_SIZE  * delta * GameConstants.BLOCK_SPEED_SCROLLING_FACTOR
            );
        correctCollision(gameGrid);
    }

    public void render(ShapeRenderer shapeRenderer) {
        for(Rectangle rect : rects){
            shapeRenderer.rect(rect.getX(), rect.getY(), rect.width, rect.height);
        }
    }

    public int getRowOrColumn(Rectangle rect, RowOrColumn selector) {
        int index = -1;
        switch (selector){
            case ROW:
                index = (int) ((rect.getY() - GameConstants.GAME_SCREEN_START_Y)/GameConstants.BLOCK_SIDE_SIZE);
//                index = (int) ((rect.getY() + GameConstants.BLOCK_SIDE_SIZE - rect.getY())/GameConstants.BLOCK_SIDE_SIZE);
                break;
            case COLUMN:
                index = (int) ((rect.getX() - GameConstants.GAME_SCREEN_START_X)/ GameConstants.BLOCK_SIDE_SIZE);
//                index = (int) ((rect.getX() - rect.getX())/GameConstants.BLOCK_SIDE_SIZE);
        }
        return index;
    }
//
//    public boolean isBottomColliding(Rectangle lowestBlock, Grid gameGrid) {
//        return lowestBlock.getY() < surfaceRect.getY();
//    }

    public Rectangle getLowestSurfaceRect(Rectangle lowestBlock, Grid gameGrid) {
        int rectColumn = getRowOrColumn(lowestBlock, RowOrColumn.COLUMN);
        Rectangle surfaceRect = gameGrid.getTopmostRectAtColumn(rectColumn);
        if (surfaceRect == null)
            surfaceRect = gameGrid.getArena();
        return surfaceRect;
    }

    /***
     * Catch all method to deal with
     * * Stop once the block hits the bottom.
     * * Nudge the block to left or right depending on which wall it hits.
     * * ***More to Come***
     *
     * TODO: Update the method so that it will look for collision wrto passive blocks.
     */
    public void correctCollision(Grid gameGrid) {
        Rectangle lowestBlock = getLowestBlock();
        Rectangle rightmostBlock = getRightmostBlock();
        Rectangle leftmostBlock = getLeftmostBlock();

        Rectangle relevantLowerSurfaceRect = getLowestSurfaceRect(lowestBlock, gameGrid);

//        if (lowestBlock.getY() < arena.getY()) {
//        System.out.println("lowest block: " + lowestBlock +" relevant lowersurface: " + relevantLowerSurfaceRect);
        if(lowestBlock.getY() <= relevantLowerSurfaceRect.getY()){
            float height = GameConstants.BLOCK_SIDE_SIZE;
            if(relevantLowerSurfaceRect.getY() > GameConstants.GAME_SCREEN_START_Y)
                height += GameConstants.BLOCK_SIDE_SIZE;
            setY(relevantLowerSurfaceRect.getY() + height, GameConstants.BLOCK_SIDE_SIZE);
            isResting = true;
        }
//        if (leftmostBlock.getX() <= arena.getX()) {
//            setY(lowestBlock.getY()  + GameConstants.BLOCK_SIDE_SIZE, GameConstants.BLOCK_SIDE_SIZE);
//            setX(arena.getX());
//        }
//        float rightBlockX = rightmostBlock.getX() + rightmostBlock.getWidth();
////        System.out.println("rb.x: " + rightmostBlock.getX() +" ar.x: " + (arena.getX() + arena.getX() ) + " arena.getWidth() + arena.getX()? " + ( rightBlockX  >= arena.getWidth() + arena.getX()));
//        if( rightBlockX  > arena.getWidth() + arena.getX()){
//            float arenaXEnd = arena.getX() + arena.getWidth();
//            float blocksToNudge = GameConstants.BLOCK_SIDE_SIZE *(isVertical? 1: 4);
//
//            setX(arenaXEnd - blocksToNudge);
//        }
    }

    public Rectangle getLowestBlock(){
        Rectangle lowestRect = rects[0];
        for(Rectangle rect : rects)
            if(rect.getY() < lowestRect.getY())
                lowestRect = rect;
        return lowestRect;
    }

    public Rectangle getLeftmostBlock() {
        Rectangle leftmostRect = rects[0];

        for(Rectangle rect: rects)
            if (rect.getX() < leftmostRect.getX() ||
                    (rect.getX() == leftmostRect.getX() && rect.getY() < leftmostRect.getY()))
                leftmostRect = rect;
        return leftmostRect;
    }

    public Rectangle getRightmostBlock() {
        Rectangle rightmostRect = rects[0];
        for(Rectangle rect : rects)
            if(rect.getX() > rightmostRect.getX() ||
                    rect.getX() == rightmostRect.getX() && rect.getY() < rightmostRect.getY())
                rightmostRect = rect;
        return rightmostRect;
    }

    public Rectangle[] getBlocks() {
        Arrays.sort(rects, BlockLineComparator);
        return rects;
    }
    public boolean isBlockResting(){
        return isResting;
    }

}
