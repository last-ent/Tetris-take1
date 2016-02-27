package com.mygdx.tetcls;

/**
 * Created by HP-430 on 2/25/2016.
 */
public class GameConstants {

    public static final float SECOND = 1f;
    public static final float BLOCK_SIDE_SIZE = 24f;
    public static final int BLOCK_NUM = 4;

    public static final int GRID_ROWS = 22;
    public static final int GRID_COLUMNS = 10;
    public static final float GAME_WIDTH = GRID_COLUMNS * BLOCK_SIDE_SIZE;
    public static final float GAME_HEIGHT = GRID_ROWS * BLOCK_SIDE_SIZE;
    public static final float GAME_SCREEN_START_X = BLOCK_SIDE_SIZE * 2;
    public static final float GAME_SCREEN_START_Y = BLOCK_SIDE_SIZE * 2;
    public static final float WORLD_WIDTH = 15 * BLOCK_SIDE_SIZE;
    public static final float WORLD_HEIGHT = 25 * BLOCK_SIDE_SIZE;
    public static final int BLOCK_SPEED_SCROLLING_FACTOR = 5;


    public static final float BLOCK_START_X = (GameConstants.GAME_SCREEN_START_X + GameConstants.GAME_WIDTH)/2;
    public static final float BLOCK_START_Y = (GameConstants.GAME_SCREEN_START_Y + GameConstants.GAME_HEIGHT);



}
