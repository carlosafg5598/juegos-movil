package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen extends InputAdapter implements Screen {

    private final Main game;
    GameScreen gameScreen;
    MenuDeJuego menuDeJuego;
    private Camera camera;
    private Viewport viewport;
    private SpriteBatch batch;
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private BitmapFont font;
    private final int WORLD_WIDTH = 72;
    private final int WORLD_HEIGHT = 128;

    GameScreen(Main game) {
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        batch = new SpriteBatch();
        font = new BitmapFont();

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("mapas/mapaVerdeEsqui.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        camera.position.set(viewport.getScreenWidth() / 2, viewport.getScreenHeight() / 2, 0);

    }

    public void handleInput(float dt) {
        if (Gdx.input.isTouched()) {
            camera.position.y += 100 * dt;
        }
    }

    public void update(float dt) {
        handleInput(dt);
        camera.update();
        renderer.setView((OrthographicCamera) camera);
    }


    @Override
    public void render(float delta) {
        update(delta);
        renderer.render();
        batch.begin();


        batch.end();


    }


    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        //background.dispose();
        font.dispose();
    }

    @Override
    public void show() {

    }
}
