package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class opcionesScreen implements Screen {
    private final Main game;
    private Camera camera;
    private Viewport viewport;
    private Texture background;
    private BitmapFont font;
    private SpriteBatch batch;
    private Stage stage;
    private Slider musicSlider, sfxSlider;
    private TextButton backButton;
    private final int WORLD_WIDTH = 720;
    private final int WORLD_HEIGHT = 1280;
    private Preferences preferences;
    private Music backgroundMusic; // Objeto para controlar la música
    private Sound[] soundEffects; // Arreglo de efectos de sonido
    private int backgroundOffset;

    public opcionesScreen(Main game) {
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        background = new Texture("static_snow.png");
        batch = game.batch;
        font = new BitmapFont();
        font.getData().setScale(3f);
        font.setColor(0, 0, 0, 1);

        preferences = Gdx.app.getPreferences("GameSettings");
        float musicVolume = preferences.getFloat("musicVolume", 0.5f);
        float sfxVolume = preferences.getFloat("sfxVolume", 0.5f);

        // Cargar música y efectos de sonido
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("spritesEsqui/Black Diamond.mp3"));
        backgroundMusic.setLooping(true); // Si deseas que la música se repita
        backgroundMusic.setVolume(musicVolume); // Establecer el volumen inicial de la música

        soundEffects = new Sound[2]; // Cargar algunos efectos de sonido de ejemplo
        soundEffects[0] = Gdx.audio.newSound(Gdx.files.internal("spritesEsqui/Victory.mp3"));
        soundEffects[1] = Gdx.audio.newSound(Gdx.files.internal("spritesEsqui/5Hit_Sounds/mp3/die1.mp3"));

        // Establecer el volumen de los efectos de sonido
        setSfxVolume(sfxVolume);

        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(stage);

        // Crear un estilo para los botones
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;

        // Crear un estilo para los sliders
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.knob = null;
        sliderStyle.background = null;

        // Crear sliders sin necesidad de un archivo .json
        musicSlider = new Slider(0, 1, 0.1f, false, sliderStyle);
        musicSlider.setValue(musicVolume);
        musicSlider.setSize(400, 50);
        musicSlider.setPosition(160, 850);
        musicSlider.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float volume = musicSlider.getValue();
                preferences.putFloat("musicVolume", volume);
                preferences.flush();
                backgroundMusic.setVolume(volume); // Actualizar volumen de la música
            }
        });
        stage.addActor(musicSlider);

        sfxSlider = new Slider(0, 1, 0.1f, false, sliderStyle);
        sfxSlider.setValue(sfxVolume);
        sfxSlider.setSize(400, 50);
        sfxSlider.setPosition(160, 750);
        sfxSlider.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float volume = sfxSlider.getValue();
                preferences.putFloat("sfxVolume", volume);
                preferences.flush();
                setSfxVolume(volume); // Actualizar volumen de los efectos de sonido
            }
        });

        stage.addActor(sfxSlider);

        // Botón para volver atrás
        backButton = new TextButton("Atrás", buttonStyle);
        backButton.setSize(400, 100);
        backButton.setPosition(160, 500);
        backButton.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                preferences.putFloat("musicVolume", musicSlider.getValue());
                preferences.putFloat("sfxVolume", sfxSlider.getValue());
                preferences.flush();
                game.setScreen(new MenuScreen(game)); // Volver al menú principal
            }
        });
        stage.addActor(backButton);
    }

    private void setSfxVolume(float volume) {
        // Actualiza el volumen de todos los efectos de sonido
        for (Sound sound : soundEffects) {
            sound.setVolume(0, volume); // 0 es el índice del canal de reproducción (por defecto)
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        backgroundOffset++;
        if (backgroundOffset % WORLD_HEIGHT == 0) {
            backgroundOffset = 0;
        }
        game.batch.draw(background, 0, -backgroundOffset, WORLD_WIDTH, WORLD_HEIGHT);
        game.batch.draw(background, 0, -backgroundOffset + WORLD_HEIGHT, WORLD_WIDTH, WORLD_HEIGHT);
        font.draw(batch, "Volumen Música", 280, 920);
        font.draw(batch, "Volumen Efectos", 280, 820);
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        background.dispose();
        font.dispose();
        stage.dispose();
        backgroundMusic.dispose();
        for (Sound sound : soundEffects) {
            sound.dispose();
        }
    }

    @Override
    public void show() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}
}
