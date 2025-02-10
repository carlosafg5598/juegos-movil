package io.github.some_example_name;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;//esto es lo que hay que importar
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

//import java.awt.Label;

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

    public Hud(SpriteBatch sb){
        worldTimer=300;
        timeCount =0;
        score=0;
        viewport=new FitViewport(Main.V_WIDTH,Main.V_HEIGHT,new OrthographicCamera());
        stage= new Stage(viewport,sb);

        Table table= new Table();
        table.top();
        table.setFillParent(true);

        countDownLabel= new Label(String.format("%03d",worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel=new Label(String.format("%06d",score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel=new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        leverLabel=new Label("1-1", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        worldLabel=new Label("WORLD", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        marioLabel= new Label("MARIO", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(marioLabel).expandX();
        table.add(worldLabel).expandX();
        table.add(timeLabel).expandX();
        table.row();
        table.add(scoreLabel).expandX();
        table.add(leverLabel).expandX();
        table.add(countDownLabel).expandX();

        stage.addActor(table);



    }
}
