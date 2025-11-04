package tomas.persecucion;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.audio.Music;

public class PantallaMenu implements Screen {

    private final PersecucionGame game;
    private SpriteBatch batch;
    private Texture fondo;
    private Rectangle botonJugar;
    private Music musicaFondo;

    public PantallaMenu(PersecucionGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        fondo = new Texture("fondo_menu.png");
        botonJugar = new Rectangle(217, 186, 845, 162);
        musicaFondo = Gdx.audio.newMusic(Gdx.files.internal("musica_menu.mp3"));
        musicaFondo.setLooping(true);
        musicaFondo.setVolume(0.12f);
        musicaFondo.play();
    }

    @Override
    public void render(float delta) {
        // Limpiar pantalla
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Fondo
        batch.begin();
        batch.draw(fondo, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        // Detectar mouse
        if (Gdx.input.justTouched()) {
            float x = Gdx.input.getX();
            float y = Gdx.graphics.getHeight() - Gdx.input.getY();
            System.out.println("Clic en: X=" + x + " Y=" + y);

            if (botonJugar.contains(x, y)) {
                System.out.println("¡Jugar clickeado!");

                if (musicaFondo != null && musicaFondo.isPlaying()) {
                    musicaFondo.stop();
                }

                Gdx.graphics.setWindowedMode(1920, 1080);
                game.setScreen(new PantallaJuego(game));
                dispose();
            }


        }

        // Tecla enter
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            Gdx.graphics.setWindowedMode(1920, 1080);
            game.setScreen(new PantallaJuego(game));
            dispose();
        }
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    
    @Override
    public void dispose() {
        fondo.dispose();
        batch.dispose();
        if (musicaFondo != null) musicaFondo.dispose();
    }

}
