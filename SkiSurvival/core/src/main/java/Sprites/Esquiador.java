package Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
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
        bdef.position.set(32, 100);
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5);
        fdef.shape = shape;
        body.createFixture(fdef);
    }


}
//    private ShapeRenderer shapeRenderer;
//    private Texture texture;
//    private Sprite sprite;
//
//    public Esquiador(World world, float x, float y){
//        this.world=world;
//        shapeRenderer = new ShapeRenderer();
//        createBody(x,y);
//
//    }
//    private void createBody(float x, float y){
//        BodyDef bdef= new BodyDef();
//        bdef.type= BodyDef.BodyType.DynamicBody;
//        bdef.position.set(x,y);
//
//        b2dy=world.createBody(bdef);
//        PolygonShape shape= new PolygonShape();
//        shape.setAsBox(20,20);
//        FixtureDef fixtureDef= new FixtureDef();
//        fixtureDef.shape = shape;
//        fixtureDef.density = 1f;
//        fixtureDef.friction = 0.5f;
//        b2dy.createFixture(fixtureDef);
//        shape.dispose();
//
//    }
//
//    public void draw() {
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(0, 1, 0, 1); // Color verde
//        shapeRenderer.rect(b2dy.getPosition().x - 20, b2dy.getPosition().y - 20, 40, 40);
//        shapeRenderer.end();
//    }
//
//    public void move(float x, float y) {
//        b2dy.setLinearVelocity(new Vector2(x, y));
//    }
//
//    public float getY() {
//        return b2dy.getPosition().y;
//    }
//
//    public void dispose() {
//        shapeRenderer.dispose();
//    }


