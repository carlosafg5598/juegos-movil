package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PlayScreen implements Screen {
    private Main game;
    private Hud hud;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private TmxMapLoader mapLoader; //cargar el mapa
    private TiledMap map; //referencia al mapa en si
    private OrthogonalTiledMapRenderer render; // representa mapa en pantalla


    public PlayScreen(Main game) {
        this.game = game;
        hud = new Hud(game.batch);
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(Main.V_WIDTH, Main.V_HEIGHT, gameCam);
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level1.tmx");
        render = new OrthogonalTiledMapRenderer(map);
        gameCam.position.set(gamePort.getScreenWidth() / 2, gamePort.getScreenHeight() / 2, 0);

    }

    @Override
    public void show() {

    }

    public void handleInput(float dt) {
        if (Gdx.input.isTouched()) {
            gameCam.position.x += 100 * dt;

        }
    }

    public void update(float dt) {
        handleInput(dt);
        gameCam.update();
        render.setView(gameCam);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        render.render();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
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
}
