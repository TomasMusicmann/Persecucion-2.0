package tomas.persecucion.net;

import java.io.*;
import java.net.*;

public class ServidorJuego {
    private ServerSocket serverSocket;
    private Socket socketCliente;
    private BufferedReader entrada;
    private PrintWriter salida;
    private boolean conectado = false;

    public void iniciar() throws IOException {
        serverSocket = new ServerSocket(5000);
        System.out.println("Esperando");
        socketCliente = serverSocket.accept();
        System.out.println("Cliente");

        entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
        salida = new PrintWriter(socketCliente.getOutputStream(), true);
        conectado = true;
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
            if (socketCliente != null) socketCliente.close();
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
