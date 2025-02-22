package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MenuDeJuego implements Screen {

    private final Main game;
    private Camera camera;
    private Viewport viewport;
    private Texture background;
    private int backgroundOffset;
    private BitmapFont font;
    private int selectedOption = -1;
    private final String[] menuOptions = {"V e r d e ", "A z u l ", "R o j o ", "N e g r o ", "A t r a s"};
    private final int WORLD_WIDTH = 72;
    private final int WORLD_HEIGHT = 128;
    private Vector3 touchCoords = new Vector3();
    private Rectangle[] menuBounds;
    private boolean touchHandled = false;
    private boolean transitioning = false;
    private float touchCooldown = 0.3f;

    MenuDeJuego(Main game) {
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        background = new Texture("static_snow.png");
        backgroundOffset = 0;
        game.batch = new SpriteBatch();
        font = new BitmapFont();

        // 📌 **Reducimos el tamaño del texto**
        font.getData().setScale(0.2f); // Antes era 0.3f, ahora más pequeño

        // 📌 **Creamos los botones más pequeños**
        menuBounds = new Rectangle[menuOptions.length];
        for (int i = 0; i < menuOptions.length; i++) {
            float optionX = WORLD_WIDTH / 4; // Centramos más los botones
            float optionY = WORLD_HEIGHT / 2 + 15 - i * 10; // Ajustamos el espaciado
            menuBounds[i] = new Rectangle(optionX, optionY - 3, 40, 8); // Botones más pequeños
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (touchCooldown > 0) {
            touchCooldown -= delta;
        }

        game.batch.begin();

        // 📌 **Dibujamos el fondo correctamente**
        backgroundOffset++;
        if (backgroundOffset % WORLD_HEIGHT == 0) {
            backgroundOffset = 0;
        }
        game.batch.draw(background, 0, -backgroundOffset, WORLD_WIDTH, WORLD_HEIGHT);
        game.batch.draw(background, 0, -backgroundOffset + WORLD_HEIGHT, WORLD_WIDTH, WORLD_HEIGHT);

        // 📌 **Dibujamos el texto dentro del botón de forma más centrada**
        for (int i = 0; i < menuOptions.length; i++) {
            font.setColor(0, 0, 0, 1);

            // 📌 **Ajustamos el texto para que esté dentro del botón**
            float textX = menuBounds[i].x + menuBounds[i].width / 6; // Centrar horizontalmente
            float textY = menuBounds[i].y + menuBounds[i].height / 2 + 2; // Centrar verticalmente
            font.draw(game.batch, menuOptions[i], textX, textY);
        }

        game.batch.end();
        handleTouchInput();
    }

    private void handleTouchInput() {
        if (transitioning || touchCooldown > 0) return;

        if (Gdx.input.isTouched() && !touchHandled) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.input.getY();
            camera.unproject(touchCoords.set(touchX, touchY, 0));

            for (int i = 0; i < menuBounds.length; i++) {
                if (menuBounds[i].contains(touchCoords.x, touchCoords.y)) {
                    if (selectedOption != i) {
                        selectedOption = i;
                        handleSelection();
                    }
                    break;
                }
            }
            touchHandled = true;
        } else if (!Gdx.input.isTouched()) {
            touchHandled = false;
            selectedOption = -1;
        }
    }

    private void handleSelection() {
        transitioning = true;
        Gdx.app.postRunnable(() -> {
            switch (selectedOption) {
                case 0:
                    game.setScreen(new GameScreen(game));
                    break;
                case 1:
                    game.setScreen(new BlueScreen(game));
                    break;
                case 2:
                    game.setScreen(new RedScreen(game));
                    break;
                case 3:
                    game.setScreen(new BlackScreen(game));
                    break;
                case 4:
                    game.setScreen(new MenuScreen(game));
                    break;
            }
        });
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        game.batch.setProjectionMatrix(camera.combined);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        game.batch.dispose();
        background.dispose();
        font.dispose();
    }
    @Override public void show() {
        touchCooldown = 0.3f;
    }
}
