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
import com.badlogic.gdx.math.Rectangle;

public class PantallaJuego implements Screen {

    private final PersecucionGame game;
    private SpriteBatch batch;
    private Texture fondo;
    private Texture autoNegro, autoPolicia;
    private OrthographicCamera camera;
    private BitmapFont font;
    private Rectangle rectNegro, rectPolicia;

    private float xNegro = 100, yNegro = 100;
    private float xPolicia = 400, yPolicia = 100;
    private float anguloNegro = 0, anguloPolicia = 0;

    public PantallaJuego(PersecucionGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        fondo = new Texture("fondo_juego.png");
        autoNegro = new Texture("auto_negro.png");
        autoPolicia = new Texture("auto_policia.png");
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(false);

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(2);

        rectNegro = new Rectangle(xNegro, yNegro, autoNegro.getWidth() * 0.2f, autoNegro.getHeight() * 0.4f);
        rectPolicia = new Rectangle(xPolicia, yPolicia, autoPolicia.getWidth() * 0.2f, autoPolicia.getHeight() * 0.4f);
    }

    private boolean colision = false; // agregar arriba en la clase

    private void update() {

        if (colision) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                PantallaJuego nueva = new PantallaJuego(game);
                game.setScreen(nueva);
                return;
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                Gdx.graphics.setWindowedMode(1280, 1080);
                PantallaMenu menu = new PantallaMenu(game);
                game.setScreen(menu);
                return;
            }
            return;
        }

        float speed = 350 * Gdx.graphics.getDeltaTime();

        //Auto Negro (WASD)
        float dxN = 0, dyN = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) dyN += speed;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) dyN -= speed;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) dxN -= speed;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) dxN += speed;

        xNegro += dxN;
        yNegro += dyN;
        if (dxN != 0 || dyN != 0)
            anguloNegro = (float) Math.toDegrees(Math.atan2(dyN, dxN)) + 90;

        float dxP = 0, dyP = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) dyP += speed;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) dyP -= speed;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) dxP -= speed;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) dxP += speed;

        xPolicia += dxP;
        yPolicia += dyP;
        if (dxP != 0 || dyP != 0)
            anguloPolicia = (float) Math.toDegrees(Math.atan2(dyP, dxP)) + 90;

        rectNegro.setPosition(xNegro, yNegro);
        rectPolicia.setPosition(xPolicia, yPolicia);

        if (rectNegro.overlaps(rectPolicia)) {
            System.out.println("¡Colisión detectada!");
            colision = true;
        }

        //Volver al menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
        	Gdx.graphics.setWindowedMode(1240,1080);
            PantallaMenu menu = new PantallaMenu(game);
            game.setScreen(menu);
        }
    }




    @Override
    public void render(float delta) {
        update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Fondo
        batch.draw(fondo, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Auto Negro
        batch.draw(autoNegro,
                xNegro, yNegro,
                autoNegro.getWidth() / 2f, autoNegro.getHeight() / 2f,
                autoNegro.getWidth(), autoNegro.getHeight(),
                0.2f, 0.2f,
                anguloNegro,
                0, 0, autoNegro.getWidth(), autoNegro.getHeight(),
                false, false);

        // Auto Policía
        batch.draw(autoPolicia,
                xPolicia, yPolicia,
                autoPolicia.getWidth() / 2f, autoPolicia.getHeight() / 2f,
                autoPolicia.getWidth(), autoPolicia.getHeight(),
                0.2f, 0.2f,
                anguloPolicia,
                0, 0, autoPolicia.getWidth(), autoPolicia.getHeight(),
                false, false);

        batch.end();
        
        if (colision) {
            batch.begin();
            font.draw(batch, "¡Colisión! Presiona R para reiniciar", 
                           Gdx.graphics.getWidth() / 2f - 150, 
                           Gdx.graphics.getHeight() / 2f + 50);
            batch.end();
        }

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
}
