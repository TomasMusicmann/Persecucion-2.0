import com.esotericsoftware.kryonet.*;

public class ClienteJuego {
    private Client client;
    private float x, y, rot;

    public void conectar(String ip) throws Exception {
        client = new Client();
        client.getKryo().register(ServidorJuego.MensajePosicion.class);
        client.start();
        client.connect(5000, ip, 54555, 54777);

        client.addListener(new Listener() {
            public void received(Connection c, Object o) {
                if (o instanceof ServidorJuego.MensajePosicion msg) {
                    // actualiza posición del otro jugador
                    x = msg.x;
                    y = msg.y;
                    rot = msg.rot;
                }
            }
        });
    }

    public void enviarPosicion(float x, float y, float rot) {
        ServidorJuego.MensajePosicion msg = new ServidorJuego.MensajePosicion();
        msg.x = x;
        msg.y = y;
        msg.rot = rot;
        client.sendTCP(msg);
    }
}
