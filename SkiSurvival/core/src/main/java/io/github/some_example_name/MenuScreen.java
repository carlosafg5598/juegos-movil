package io.github.some_example_name;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.w3c.dom.Text;

import java.nio.channels.spi.SelectorProvider;

public class MenuScreen implements Screen {

    private final Main game;
    GameScreen gameScreen;
    MenuDeJuego menuDeJuego;
    private Camera camera;
    private Viewport viewport;
    private SpriteBatch batch;
    private Texture background;
    private int backgroundOffset;
    private BitmapFont font;
    private int selectedOption = 0;
    private final String[] menuOptions = {"Jugar", "Opciones", "Salir"};
    private final int WORLD_WIDTH = 72;
    private final int WORLD_HEIGHT = 128;
    private Vector3 touchCoords = new Vector3();
    MenuScreen(Main game) {
        this.game=game;
        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        background = new Texture("static_snow.png");
        backgroundOffset = 0;
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(0.4f); // Reduce el tamaño a la mitad

    }


    @Override
    public void render(float delta) {
        batch.begin();
        backgroundOffset++;
        if (backgroundOffset % WORLD_HEIGHT == 0) {
            backgroundOffset = 0;
        }
        batch.draw(background, 0, -backgroundOffset, WORLD_WIDTH, WORLD_HEIGHT);
        batch.draw(background, 0, -backgroundOffset + WORLD_HEIGHT, WORLD_WIDTH, WORLD_HEIGHT);
        for (int i = 0; i < menuOptions.length; i++) {
            if (i == selectedOption) {
                font.setColor(1, 1, 0, 1); // Amarillo si está seleccionado
            } else {
                font.setColor(1, 1, 1, 1); // Blanco normal
            }
            font.draw(batch, menuOptions[i], WORLD_WIDTH / 15, WORLD_HEIGHT / 4 - i * 10);
        }
        batch.end();
//        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
//            selectedOption = (selectedOption - 1 + menuOptions.length) % menuOptions.length;
//        }
//        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
//            selectedOption = (selectedOption + 1) % menuOptions.length;
//        }
//        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
//            handleSelection();
//        }
        handleTouchInput();

    }

    private void handleTouchInput() {
        // Detectar si el usuario está tocando la pantalla o haciendo clic con el mouse
        if (Gdx.input.isTouched()) {
            // Obtener las coordenadas del toque (o clic en el escritorio)
            float touchX = Gdx.input.getX();
            float touchY = Gdx.input.getY();

            // Convertir las coordenadas del toque a las coordenadas del mundo
            camera.unproject(touchCoords.set(touchX, touchY, 0));

            // Verificar si el toque o clic está dentro de alguna de las opciones del menú
            for (int i = 0; i < menuOptions.length; i++) {
                float optionTop = WORLD_HEIGHT / 4 - i * 10;
                float optionBottom = optionTop - 10; // Espacio entre opciones

                // Comprobar si el toque está dentro de los límites de la opción
                if (touchCoords.y > optionBottom && touchCoords.y < optionTop) {
                    selectedOption = i;
                    if (Gdx.input.isTouched()) {
                        handleSelection(); // Ejecutar la selección
                    }
                }
            }
        }
    }

    private void handleSelection() {
        switch (selectedOption) {
            case 0: // Jugar
                //gameScreen = new GameScreen(game);
                //game.setScreen(new GameScreen(game));
                game.setScreen(new MenuDeJuego(game));

                break;
            case 1: // Opciones
                System.out.println("Opciones seleccionadas");
                break;
            case 2: // Salir
                Gdx.app.exit();
                break;
        }
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
        background.dispose();
        font.dispose();
    }

    @Override
    public void show() {

    }
}
