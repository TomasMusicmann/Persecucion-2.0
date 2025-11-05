package tomas.persecucion.net;

import java.io.*;
import java.net.*;

public class ClienteJuego {
    private Socket socket;
    private BufferedReader entrada;
    private PrintWriter salida;
    private boolean conectado = false;

    public void conectar(String ip) throws IOException {
        socket = new Socket(ip, 5000);
        entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        salida = new PrintWriter(socket.getOutputStream(), true);
        conectado = true;
        System.out.println("Conectado");
    }

    public void enviar(float x, float y) {
        if (conectado) salida.println(x + "," + y);
    }

    public float[] recibir() {
        try {
            if (entrada.ready()) {
                String linea = entrada.readLine();
                if (linea != null && linea.contains(",")) {
                    String[] partes = linea.split(",");
                    return new float[]{Float.parseFloat(partes[0]), Float.parseFloat(partes[1])};
                }
            }
        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
        }
        return null;
    }

    public void cerrar() {
        try {
            conectado = false;
            if (entrada != null) entrada.close();
            if (salida != null) salida.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
