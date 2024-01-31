import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        try {
            // Crear un servidor en el puerto 7
            ServerSocket serverSocket = new ServerSocket(7);
            System.out.println("Servidor Eco iniciado en el puerto 7");

            while (true) {
                // Esperar a que un cliente se conecte
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde " + clientSocket.getInetAddress());

                // Obtener flujos de entrada y salida del cliente
                InputStream inputStream = clientSocket.getInputStream();
                OutputStream outputStream = clientSocket.getOutputStream();

                // Leer datos del cliente
                byte[] buffer = new byte[1024];
                int bytesRead = inputStream.read(buffer);

                // Simular el servicio Eco: enviar de vuelta los mismos datos
                outputStream.write(buffer, 0, bytesRead);

                // Cerrar la conexión con el cliente
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
