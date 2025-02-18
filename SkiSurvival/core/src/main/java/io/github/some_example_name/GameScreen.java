package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactListener;

import Sprites.Esquiador;
public class GameScreen extends InputAdapter implements Screen {

    private final Main game;
    private Camera camera;
    private Viewport viewport;
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private BitmapFont font;
    private final float WORLD_WIDTH = 350;
    private final float WORLD_HEIGHT = 400;

    // Box2D Variables
    private World world;
    private Box2DDebugRenderer b2dr;

    // Esquiador y su body
    private Esquiador esquiador;

    GameScreen(Main game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        game.batch = new SpriteBatch();
        font = new BitmapFont();

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("mapas/EsquiVerde.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);

        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        world = new World(new Vector2(0, -9.8f), true); // Gravedad hacia abajo
        b2dr = new Box2DDebugRenderer();

        // Crear obstáculos del mapa (árboles, etc)
        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;
        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2);
            body = world.createBody(bodyDef);
            shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef);
        }

        // Crear el esquiador
        esquiador = new Esquiador(world);

        // Configurar el listener de contacto para detectar colisiones
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                System.out.println("¡Colisión detectada!");
            }

            @Override
            public void endContact(Contact contact) {
                // Aquí se puede manejar el final de la colisión si es necesario
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
                // Lógica previa a la colisión
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
                // Lógica posterior a la colisión
            }
        });
    }



    public void handleInput(float dt) {
        float screenWidth = Gdx.graphics.getWidth(); // Ancho de la pantalla
        float screenHeight = Gdx.graphics.getHeight(); // Altura de la pantalla
        if (Gdx.input.isTouched()) {

            camera.position.y -= 100 * dt;
            int screenX= Gdx.input.getX();
            int screenY=Gdx.input.getY();

            if (screenX > screenWidth / 2) {
                // Movimiento a la derecha (agregar velocidad positiva en el eje X)
                esquiador.move(20, esquiador.body.getLinearVelocity().y);  // Movimiento constante hacia la derecha
            } else {
                // Movimiento a la izquierda (agregar velocidad negativa en el eje X)
                esquiador.move(-20, esquiador.body.getLinearVelocity().y);  // Movimiento constante hacia la izquierda
            }
        }
    }

    public void update(float dt) {
        handleInput(dt);
        world.step(1 / 60f, 6, 2);

        System.out.println("Esquiador X: " + esquiador.body.getPosition().x + ", Esquiador Y: " + esquiador.body.getPosition().y);
        // Actualizar la posición de la cámara para que siga al esquiador
        camera.position.set(esquiador.body.getPosition().x, esquiador.body.getPosition().y, 0);
        camera.update();

        renderer.setView((OrthographicCamera) camera);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();
        b2dr.render(world, camera.combined);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        game.batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        game.batch.dispose();
        font.dispose();
    }

    @Override
    public void show() {}
}
