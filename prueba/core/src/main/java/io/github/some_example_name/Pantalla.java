package io.github.some_example_name;

import com.badlogic.gdx.Screen;

public abstract class Pantalla implements Screen {
    protected Main juego;

    public Pantalla(Main juego) {
        this.juego = juego;
    }

}
