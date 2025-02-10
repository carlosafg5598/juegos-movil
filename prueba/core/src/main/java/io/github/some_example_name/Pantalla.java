package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public abstract class Pantalla extends InputAdapter implements Screen {
    protected Main juego;

    //tamaños de pantalla
    public static final float SCREEN_WIDTH = 800;
    public static final float SCREEN_HEIGHT = 400;
    public static final float WORLD_HEIGHT = 4.8f;
    public static final float WORLD_WIDHT = 8f;


    public OrthographicCamera oCamUI;
    public OrthographicCamera oCamBox2D;

    public SpriteBatch spriteBach;

    public Stage stage;

    public Pantalla(Main juego) {
        this.juego = juego;
        stage = new Stage(new StretchViewport(SCREEN_WIDTH, SCREEN_HEIGHT));

        oCamUI = new OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT);
        oCamUI.position.set(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f, 0);

        oCamBox2D = new OrthographicCamera(WORLD_WIDHT, WORLD_HEIGHT);
        oCamBox2D.position.set(WORLD_WIDHT / 2f, WORLD_HEIGHT / 2f, 0);

        InputMultiplexer input = new InputMultiplexer(this, stage);
        Gdx.input.setInputProcessor(input);
        spriteBach = new SpriteBatch();

    }

    @Override
    public void render(float delta) {
        update(delta);
        stage.act(delta);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        draw(delta);
        stage.draw();
    }

    public abstract void draw(float delta);

    public abstract void update(float delta);

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width,height,true);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode== Input.Keys.ESCAPE||keycode==Input.Keys.BACK){
            if (this instanceof MenuPantalla){
                Gdx.app.exit();
            }else{
                juego.setScreen(new MenuPantalla(juego));
            }
            //si estoy en el menu principal salgo de la app
            //si estoy en un tutorial salgo al menu principal
            //TODO: implementar más adelante
        }
        return super.keyDown(keycode);
    }
}
