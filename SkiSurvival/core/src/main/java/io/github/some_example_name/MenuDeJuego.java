package io.github.some_example_name;

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

public class MenuDeJuego implements Screen {

    private final Main game;

    private Camera camera;
    private Viewport viewport;
    private SpriteBatch batch;
    private Texture background;
    private int backgroundOffset;
    private BitmapFont font;
    private int selectedOption = 0;
    private final String[] menuOptions = {"Verde (Fácil)", "Azul (Normal)", "Rojo (Difícil)", "Negro (Imposible)", "Salir"};
    private final int WORLD_WIDTH = 72;
    private final int WORLD_HEIGHT = 128;

    private Vector3 touchCoords = new Vector3();

    MenuDeJuego(Main game) {
        this.game = game;
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

            font.setColor(1, 1, 1, 1); // Blanco normal

            font.draw(batch, menuOptions[i], WORLD_WIDTH / 20, WORLD_HEIGHT / 3 - i * 8);
        }
        batch.end();


        handleTouchInput();

    }

    private boolean touchHandled = false;  // Bandera para evitar toques múltiples

    private void handleTouchInput() {
        if (Gdx.input.isTouched() && !touchHandled) {
            float touchX = Gdx.input.getX(); // Coordenada X del toque
            float touchY = Gdx.input.getY(); // Coordenada Y del toque

            // Convertir las coordenadas del toque a coordenadas del mundo
            camera.unproject(touchCoords.set(touchX, touchY, 0));

            // Comprobar si se tocó alguna opción del menú
            for (int i = 0; i < menuOptions.length; i++) {
                float optionTop = WORLD_HEIGHT / 3 - i * 8;
                float optionBottom = optionTop - 10; // El espacio entre cada opción

                // Verifica si el toque está dentro de los límites de la opción
                if (touchCoords.y > optionBottom && touchCoords.y < optionTop) {
                    selectedOption = i;
                    handleSelection(); // Ejecuta la acción de la opción seleccionada
                    break;  // Salir del bucle una vez que se haya procesado el toque
                }
            }

            touchHandled = true;  // Marcar el toque como procesado
        } else if (!Gdx.input.isTouched()) {
            touchHandled = false;  // Restablecer la bandera cuando el toque se haya soltado
        }
    }


    private void handleSelection() {
        switch (selectedOption) {
            case 0: // Verde
                game.setScreen(new GameScreen(game));
                System.out.println("Opcion Verde");
                //game.setScreen(new GameScreen(game));//hay que cambiar cuando tenga el juego
                break;
            case 1: // Azul
                System.out.println("Opcion Azul");
                //game.setScreen(new GameScreen(game));//hay que cambiar cuando tenga el juego
                break;
            case 2: // Rojo
                System.out.println("Opcion Rojo");
                //game.setScreen(new GameScreen(game));//hay que cambiar cuando tenga el juego
                break;
            case 3: // Negro
                System.out.println("Opcion Negro");
                //game.setScreen(new GameScreen(game));//hay que cambiar cuando tenga el juego
                break;

            case 4: // Salir
                game.setScreen(new MenuScreen(game));
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
