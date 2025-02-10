package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class Assets {

    public static BitmapFont font;

    public static TextButton.TextButtonStyle txtButtonStyle;
    public static ScrollPane.ScrollPaneStyle scrollPaneStyle;

    public static void load(){
        font= new BitmapFont();
       // TextureAtlas atlas= new TextureAtlas(Gdx.files.internal())
    }
}
