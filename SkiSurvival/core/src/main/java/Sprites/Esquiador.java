package Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Esquiador extends Sprite {
    public World world;
    public Body body;

    public Esquiador(World world) {
        this.world = world;
        defineEsquiador();
    }

    public void defineEsquiador() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(150, 1500);  // Empieza en una posición en la que puedas ver al esquiador
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5); // Tamaño del esquiador
        fdef.shape = shape;
        body.createFixture(fdef);
    }

    public void move(float x, float y) {
        body.setLinearVelocity(x, y); // Esto mueve al esquiador
    }
}
