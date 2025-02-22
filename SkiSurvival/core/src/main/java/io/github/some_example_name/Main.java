package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends Game {

    MenuScreen menuScreen;
    public SpriteBatch batch;
    private Music musicFondo;  // Variable para la música de fondo
    private Sound sonidoVictoria;  // Sonido de victoria
    private Sound sonidoDerrota;

    private final float DURACION_VICTORIA = 3.7f;  // Ajusta esta duración según el sonido
    private final float DURACION_DERROTA = 0.7f;   // Ajusta esta duración según el sonido

    @Override
    public void create() {
        batch= new SpriteBatch();
        menuScreen = new MenuScreen(this);
        setScreen(menuScreen);
        // Cargar la música de fondo
        musicFondo = Gdx.audio.newMusic(Gdx.files.internal("spritesEsqui/Black Diamond.mp3"));
        musicFondo.setLooping(true);  // Repetir la música
        musicFondo.setVolume(0.5f);   // Ajustar el volumen
        musicFondo.play();            // Reproducir la música

        sonidoVictoria = Gdx.audio.newSound(Gdx.files.internal("spritesEsqui/Victory.mp3"));
        sonidoDerrota = Gdx.audio.newSound(Gdx.files.internal("spritesEsqui/5Hit_Sounds/mp3/die1.mp3"));

    }

    @Override
    public void dispose() {
        super.dispose();
        menuScreen.dispose();
        // Liberar los recursos de la música cuando el juego termine
        if (musicFondo != null) {
            musicFondo.stop();
            musicFondo.dispose();
        }
        if (sonidoVictoria != null) {
            sonidoVictoria.dispose();
        }
        if (sonidoDerrota != null) {
            sonidoDerrota.dispose();
        }
    }
    // Método para reproducir la victoria
    public void reproducirVictoria() {
        if (musicFondo.isPlaying()) {
            musicFondo.stop();  // Detener la música de fondo
        }
        sonidoVictoria.play(1.0f);  // Reproducir el sonido de victoria
        // Reproducir la música de fondo después de que termine el sonido de victoria
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                musicFondo.play();  // Volver a iniciar la música de fondo
            }
        }, DURACION_VICTORIA);
    }

    // Método para reproducir la derrota
    public void reproducirDerrota() {
        if (musicFondo.isPlaying()) {
            musicFondo.stop();  // Detener la música de fondo
        }
        sonidoDerrota.play(1.0f);  // Reproducir el sonido de derrota
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                musicFondo.play();  // Volver a iniciar la música de fondo
            }
        }, DURACION_DERROTA);  // Usar la duración del sonido de derrota
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }
}
