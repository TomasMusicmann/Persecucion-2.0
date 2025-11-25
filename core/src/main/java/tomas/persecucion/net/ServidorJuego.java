import com.esotericsoftware.kryonet.*;

public class ServidorJuego {
    public static void main(String[] args) throws Exception {
        Server server = new Server();
        server.getKryo().register(MensajePosicion.class);
        server.bind(54555, 54777);
        server.start();

        server.addListener(new Listener() {
            public void received(Connection c, Object o) {
                if (o instanceof MensajePosicion msg) {
                    // retransmitir a todos menos al emisor
                    server.sendToAllExceptTCP(c.getID(), msg);
                }
            }
        });

        System.out.println("Servidor iniciado en puerto 54555");
    }

    public static class MensajePosicion {
        public float x, y, rot;
    }
}
