package tomas.persecucion;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;

public class PantallaJuego implements Screen {

    private final PersecucionGame game;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Texture fondo, autoNegro, autoPolicia;
    private BitmapFont font;

    private static final float MAP_WIDTH = 1920f;
    private static final float MAP_HEIGHT = 1080f;

    private Auto jugador;
    private Auto policia;
    private boolean colision;

    public PantallaJuego(PersecucionGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        fondo = new Texture("fondo_juego.png");
        autoNegro = new Texture("auto_negro.png");
        autoPolicia = new Texture("auto_policia.png");

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(2);

        camera = new OrthographicCamera(MAP_WIDTH, MAP_HEIGHT);
        camera.position.set(0, 0, 0);
        camera.update();

        jugador = new Auto(autoNegro, -200f, 0f);
        policia = new Auto(autoPolicia, 200f, 0f);
    }

    private void update(float delta) {
        if (colision) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                game.setScreen(new PantallaJuego(game));
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                Gdx.graphics.setWindowedMode(1240, 1080);
                game.setScreen(new PantallaMenu(game));
            }
            return;
        }

        jugador.mover(Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D, delta);
        policia.mover(Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT, delta);

        limitarMovimiento(jugador);
        limitarMovimiento(policia);

        jugador.actualizarHitbox();
        policia.actualizarHitbox();

        if (Intersector.overlapConvexPolygons(jugador.hitbox, policia.hitbox)) {
            colision = true;
        }
    }

    private void limitarMovimiento(Auto auto) {
        float hw = MAP_WIDTH / 2f;
        float hh = MAP_HEIGHT / 2f;
        float halfW = auto.drawWidth / 2f;
        float halfH = auto.drawHeight / 2f;

        if (auto.x < -hw + halfW) auto.x = -hw + halfW;
        if (auto.x > hw - halfW)  auto.x = hw - halfW;
        if (auto.y < -hh + halfH) auto.y = -hh + halfH;
        if (auto.y > hh - halfH)  auto.y = hh - halfH;
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        batch.draw(fondo, -MAP_WIDTH / 2f, -MAP_HEIGHT / 2f, MAP_WIDTH, MAP_HEIGHT);
        jugador.dibujar(batch);
        policia.dibujar(batch);

        if (colision) {
            font.draw(batch, "¡El policia atrapó al fugitivo! Presiona R para reiniciar",
                    -300, 0);
        }

        batch.end();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        fondo.dispose();
        autoNegro.dispose();
        autoPolicia.dispose();
        font.dispose();
    }

    private static class Auto {
        final Texture textura;
        final Polygon hitbox;
        float x, y, rot;
        final float escala;
        final float drawWidth;
        final float drawHeight;
        final float velocidad = 350f;

        private static final float HITBOX_PADDING = 0.75f;

        Auto(Texture textura, float startX, float startY) {
            this.textura = textura;
            this.x = startX;
            this.y = startY;

            this.escala = 0.22f;
            this.drawWidth  = textura.getWidth()  * escala;
            this.drawHeight = textura.getHeight() * escala;

            float hitboxWidth  = drawWidth  * HITBOX_PADDING;
            float hitboxHeight = drawHeight * HITBOX_PADDING;

            float[] vertices = new float[] {
                    -hitboxWidth / 2f, -hitboxHeight / 2f,
                     hitboxWidth / 2f, -hitboxHeight / 2f,
                     hitboxWidth / 2f,  hitboxHeight / 2f,
                    -hitboxWidth / 2f,  hitboxHeight / 2f
            };

            hitbox = new Polygon(vertices);
            hitbox.setOrigin(0, 0);
            actualizarHitbox();
        }

        void mover(int up, int down, int left, int right, float delta) {
            float dx = 0f, dy = 0f;
            if (Gdx.input.isKeyPressed(up))    dy += velocidad * delta;
            if (Gdx.input.isKeyPressed(down))  dy -= velocidad * delta;
            if (Gdx.input.isKeyPressed(left))  dx -= velocidad * delta;
            if (Gdx.input.isKeyPressed(right)) dx += velocidad * delta;

            x += dx;
            y += dy;

            if (dx != 0f || dy != 0f)
                rot = (float) Math.toDegrees(Math.atan2(dy, dx)) + 90f;
        }

        void actualizarHitbox() {
            hitbox.setPosition(x, y);
            hitbox.setRotation(rot);
        }

        void dibujar(SpriteBatch batch) {
            batch.draw(textura,
                    x - drawWidth / 2f, y - drawHeight / 2f,
                    drawWidth / 2f, drawHeight / 2f,
                    drawWidth, drawHeight,
                    1f, 1f,
                    rot,
                    0, 0, textura.getWidth(), textura.getHeight(),
                    false, false);
        }
    }
}
