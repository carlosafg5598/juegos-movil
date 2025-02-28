package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import Sprites.Esquiador;

public class FinalMapScreen extends InputAdapter implements Screen {
    private final Main game;
    private Camera camera;
    private Viewport viewport;
    private TmxMapLoader mapLoader;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Array<TiledMap> mapSections;
    private int currentSectionIndex = 0;
    private float playerY;

    // Box2D Variables
    private World world;
    private Box2DDebugRenderer b2dr;
    private Esquiador esquiador;

    private static final int SECTION_HEIGHT = 100; // Cada sección tiene 100 tiles de alto
    private static final int TOTAL_SECTIONS = 10;

    public FinalMapScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(320, 480, camera); // Ajuste para portrait mode
        mapLoader = new TmxMapLoader();
        mapRenderer = new OrthogonalTiledMapRenderer(null);
        mapSections = new Array<>();

        world = new World(new Vector2(0, -9.8f), true);
        b2dr = new Box2DDebugRenderer();
        esquiador = new Esquiador(world, 160, 1600); // Ajuste inicial más arriba

        splitMapIntoSections();
        createObstacles();

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Object dataA = contact.getFixtureA().getUserData();
                Object dataB = contact.getFixtureB().getUserData();

                if ("obstaculo".equals(dataA) || "obstaculo".equals(dataB)) {
                    game.setScreen(new GameOverScreen(game, "FinalMapScreen", "DERROTA"));
                } else if ("meta".equals(dataA) || "meta".equals(dataB)) {
                    game.setScreen(new GameOverScreen(game, "FinalMapScreen", "VICTORIA"));
                }
            }

            @Override
            public void endContact(Contact contact) {}
            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {}
            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {}
        });
    }

    private void splitMapIntoSections() {
        for (int i = 0; i < TOTAL_SECTIONS; i++) {
            int startY = i * SECTION_HEIGHT;
            TiledMap section = extractMapSection(startY, SECTION_HEIGHT);
            mapSections.add(section);
        }
        mapRenderer.setMap(mapSections.first());
    }

    private TiledMap extractMapSection(int startY, int height) {
        return mapLoader.load("mapas/EsquiInfinito.tmx");
    }

    private void createObstacles() {
        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;

        for (TiledMap section : mapSections) {
            for (MapObject object : section.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                bodyDef.type = BodyDef.BodyType.StaticBody;
                bodyDef.position.set(rect.getX(), rect.getY());
                body = world.createBody(bodyDef);
                shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
                fixtureDef.shape = shape;
                body.createFixture(fixtureDef).setUserData("obstaculo");
            }
        }
    }

    public void update(float delta) {
        world.step(1 / 60f, 6, 2);
        esquiador.update(delta);
        playerY = esquiador.body.getPosition().y;
        checkForNewSection();

        camera.position.set(160, playerY, 0); // Ajuste para modo vertical
        camera.update();
        mapRenderer.setView((OrthographicCamera) camera);
    }

    private void checkForNewSection() {
        if (playerY < currentSectionIndex * SECTION_HEIGHT * 16) { // Se detecta cuando baja
            currentSectionIndex++;
            if (currentSectionIndex < TOTAL_SECTIONS) {
                mapRenderer.setMap(mapSections.get(currentSectionIndex));
            }
        }
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mapRenderer.render();
        b2dr.render(world, camera.combined);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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
        for (TiledMap map : mapSections) {
            map.dispose();
        }
        mapRenderer.dispose();
        esquiador.dispose();
    }
}
