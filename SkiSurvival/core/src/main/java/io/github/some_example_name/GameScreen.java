package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen extends InputAdapter implements Screen {

    private final Main game;

    //mapa
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //screen
    private OrthographicCamera camera;
    private Viewport viewport;

    //graphics
    private SpriteBatch batch;
    private Texture background;

    //timing
    private int backgroundOffset;

    //world param
    private final int WORLD_WIDTH = 72;
    private final int WORLD_HEIGHT = 128;

    //contructor
    GameScreen(Main game) {
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
//        background = new Texture("static_snow.png");
//        backgroundOffset = 0;
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("mapaAzulEsqui.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        camera.position.set(viewport.getScreenWidth() / 2, viewport.getScreenHeight() / 2, 0);
        batch = new SpriteBatch();
    }

    public void handleInput(float dt) {
        if (Gdx.input.isTouched()) {
            camera.position.y += -100;
        }
    }

    public void update(float dt) {
        handleInput(dt);
        camera.update();
        renderer.setView(camera);
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

        viewport.update(width, height, true);//actualiza con el ancho y el alto y true para que centre la camara
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

    }

    @Override
    public void show() {

    }
}
