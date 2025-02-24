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
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import Sprites.Esquiador;

public class FinalMapScreen extends InputAdapter implements Screen {
    private final Main game;
    private Camera camera;
    private Viewport viewport;
    private TmxMapLoader mapLoader;
    private Array<TiledMap> mapChunks;
    private OrthogonalTiledMapRenderer renderer;
    private BitmapFont font;
    private World world;
    private Box2DDebugRenderer b2dr;
    private Esquiador esquiador;
    private float startX = 150;
    private float startY = 15950;
    private final float WORLD_WIDTH = 500;
    private final float WORLD_HEIGHT = 800;
    private float lastChunkY;
    private final float CHUNK_HEIGHT = 400;

    FinalMapScreen(Main game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        game.batch = new SpriteBatch();
        font = new BitmapFont();

        world = new World(new Vector2(0, -9.8f), true); // Gravedad hacia abajo
        b2dr = new Box2DDebugRenderer();

        mapLoader = new TmxMapLoader();
        mapChunks = new Array<>();
        esquiador = new Esquiador(world, startX, startY);

        cargarPrimerChunk();
    }

    private void cargarPrimerChunk() {
        TiledMap map = mapLoader.load("mapas/EsquiInfinito.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        mapChunks.add(map);
        lastChunkY = esquiador.body.getPosition().y;
        createObstacles(map);
    }

    private void crearNuevoChunk() {
        float nuevaPosY = esquiador.body.getPosition().y + CHUNK_HEIGHT;
        TiledMap nuevoChunk = mapLoader.load("mapas/EsquiInfinito.tmx");
        mapChunks.add(nuevoChunk);
        lastChunkY = nuevaPosY;
        createObstacles(nuevoChunk);

        // Limitar la cantidad de chunks cargados para no usar demasiada memoria
        if (mapChunks.size > 3) {
            TiledMap chunkViejo = mapChunks.removeIndex(0);
            chunkViejo.dispose();
        }
    }

    private void createObstacles(TiledMap map) {
        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;

        // Crear los obstáculos
        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2);
            body = world.createBody(bodyDef);
            shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef).setUserData("obstaculo");
        }

        
    }

    private void handleInput(float dt) {
        if (Gdx.input.isTouched()) {
            int screenX = Gdx.input.getX(); // Obtener la posición X del toque

            // Si el toque está en la mitad derecha de la pantalla
            if (screenX > Gdx.graphics.getWidth() / 2) {
                esquiador.move(50, esquiador.body.getLinearVelocity().y);  // Movimiento hacia la derecha
            } else {
                esquiador.move(-50, esquiador.body.getLinearVelocity().y);  // Movimiento hacia la izquierda
            }
        }
    }

    public void update(float dt) {
        handleInput(dt);
        world.step(1 / 60f, 6, 2);
        esquiador.update(dt);

        // Detectar si el esquiador está cerca del final del mapa para cargar un nuevo "chunk"
        if (esquiador.body.getPosition().y > lastChunkY - CHUNK_HEIGHT) {
            crearNuevoChunk();
        }

        // Actualiza la posición de la cámara para que siga al esquiador
        camera.position.set(esquiador.body.getPosition().x, esquiador.body.getPosition().y, 0);
        camera.update();
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.setView((OrthographicCamera) camera);
        renderer.render();
        b2dr.render(world, camera.combined);

        game.batch.begin();
        game.batch.setProjectionMatrix(camera.combined);
        esquiador.render(game.batch); // Dibujar esquiador animado
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        for (TiledMap map : mapChunks) {
            map.dispose();
        }
        game.batch.dispose();
        font.dispose();
        esquiador.dispose();
    }

    @Override
    public void show() {}

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}
}
