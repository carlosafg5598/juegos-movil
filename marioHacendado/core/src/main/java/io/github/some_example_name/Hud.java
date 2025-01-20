package io.github.some_example_name;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.awt.Label;

public class Hud {
    public Stage stage;
    private Viewport viewport;

    private Integer worldTimer;
    private float timeCount;
    private Integer score;


    Label countDownLabel;
    Label scoreLabel;
    Label timeLabel;
    Label leverLabel;
    Label worldLabel;
    Label marioLabel;

    public Hud(SpriteBatch sb){//TODO: MINUTO 4, PARTE 4 SUPER MARIO
        worldTimer=300;
        timeCount =0;
        score=0;
        viewport=new FillViewport(Main.V_WIDTH,Main.V_HEIGHT,new OrthographicCamera());
        stage= new Stage(viewport,sb);
    }
}
