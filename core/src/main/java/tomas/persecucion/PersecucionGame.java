package tomas.persecucion;

import com.badlogic.gdx.Game;

public class PersecucionGame extends Game {

    @Override
    public void create() {
        setScreen(new PantallaMenu(this)); // Empieza desde el menú
    }
}
