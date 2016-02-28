package com.mygdx.tetcls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import com.mygdx.tetcls.blocks.BlockLine;

public class GameScreen extends ScreenAdapter {

    private static float timer = GameConstants.SECOND;
    private boolean isSpacePressed = false;
    private boolean isLeftPressed = false;
    private boolean isRightPressed = false;
    private boolean isDownPressed = false;
    private Rectangle tetrisArena = new Rectangle(
            GameConstants.GAME_SCREEN_START_X, GameConstants.GAME_SCREEN_START_Y,
            GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT
    );
    private Grid arenaGrid = new Grid(tetrisArena);;

    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private Camera camera;
    private SpriteBatch batch;

    private BlockLine blockLine;


    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.position.set(GameConstants.WORLD_WIDTH / 2, GameConstants.WORLD_HEIGHT / 2, 0);
        camera.update();

        viewport = new FitViewport(GameConstants.WORLD_WIDTH, GameConstants.WORLD_HEIGHT, camera);
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();


//        createNewBlock();
        blockLine = new BlockLine(GameConstants.BLOCK_START_X, GameConstants.BLOCK_START_Y/3);
    }

    @Override
    public void render(float delta) {
        clearScreen();
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        batch.begin();
        batch.end();

        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        updateOnOneSecond(delta);
        updateOnDelta(delta);
        shapeRenderer.end();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public void updateOnOneSecond(float delta) {
        timer -= delta;
        if (timer > 0) return;

        timer = GameConstants.SECOND;

        blockLine.updateOnSecond();
    }
    public void createNewBlock(){
        blockLine = new BlockLine(GameConstants.BLOCK_START_X, GameConstants.BLOCK_START_Y);
    }

    public void drawPassiveBlocks(){
        Rectangle[][] grid = arenaGrid.getGrid();
        Rectangle rect;
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.LIGHT_GRAY.r, Color.LIGHT_GRAY.g, Color.LIGHT_GRAY.b, Color.LIGHT_GRAY.a);
        for(int x=0; x< GameConstants.GRID_COLUMNS; x++)
            for(int y = 0; y < GameConstants.GRID_ROWS; y++){
                rect = grid[x][y];
                if(rect != null) {
                    shapeRenderer.rect(rect.getX(), rect.getY(), GameConstants.BLOCK_SIDE_SIZE, GameConstants.BLOCK_SIDE_SIZE);
                }
            }
        shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, Color.WHITE.a);
    }
    public void renderGrid() {
        shapeRenderer.setColor(Color.YELLOW.r, Color.YELLOW.g, Color.YELLOW.b, 0.5f);
        // Create Rows :: x1 & x2 <= CONST & y1 == y2 <= [0,  GAME_HEIGHT, block_side]
        for(int row= 0; row < GameConstants.GAME_HEIGHT; row += GameConstants.BLOCK_SIDE_SIZE)
            shapeRenderer.line(GameConstants.GAME_SCREEN_START_X,
                    GameConstants.BLOCK_START_Y - row,
                    GameConstants.GAME_SCREEN_START_X + GameConstants.GAME_WIDTH,
                    GameConstants.BLOCK_START_Y - row);

        // Create Columns :: y1 & y2 <= CONST & x1 == x2 <= [0, Game_Width, block_side]
        for(int column = 0; column < GameConstants.GAME_WIDTH; column += GameConstants.BLOCK_SIDE_SIZE)
            shapeRenderer.line(
                    GameConstants.GAME_SCREEN_START_X + column,
                    GameConstants.BLOCK_START_Y,
                    GameConstants.GAME_SCREEN_START_X + column,
                    GameConstants.BLOCK_START_Y - GameConstants.GAME_HEIGHT
            );
        shapeRenderer.setColor(Color.WHITE);
    }

    public void updateOnDelta(float delta) {
        renderGrid();
        checkForEvents(delta);
        blockLine.updateOnDelta(delta, isSpacePressed, isLeftPressed, isRightPressed, isDownPressed, arenaGrid);
        shapeRenderer.rect(tetrisArena.getX(), tetrisArena.getY(), tetrisArena.width, tetrisArena.height);

        if(blockLine.isBlockResting()) {
            arenaGrid.register(blockLine);
            createNewBlock();
        }
        blockLine.render(shapeRenderer);
        drawPassiveBlocks();
    }

    public void checkForEvents(float delta){
        isSpacePressed = Gdx.input.isKeyJustPressed(Input.Keys.SPACE);
        isLeftPressed = Gdx.input.isKeyJustPressed(Input.Keys.LEFT);
        isRightPressed = Gdx.input.isKeyJustPressed(Input.Keys.RIGHT);
        isDownPressed = Gdx.input.isKeyJustPressed(Input.Keys.DOWN);
//        if(isDownPressed) System.out.println(isDownPressed);
    }
}
