import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Main {

    public static void main(String[] args) {
        String host = "localhost"; // Puedes cambiar esto según donde se ejecute tu servidor

        try {
            // Establecer la conexión con el servidor en el puerto 7
            Socket socket = new Socket(host, 7);

            // Obtener flujos de entrada y salida del socket
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            // Enviar datos al servidor
            String mensaje = "Hola, esto es un mensaje de prueba.";
            outputStream.write(mensaje.getBytes());

            // Recibir la respuesta del servidor
            byte[] buffer = new byte[1024];
            int bytesRead = inputStream.read(buffer);

            // Mostrar la respuesta del servidor
            String respuesta = new String(buffer, 0, bytesRead);
            System.out.println("Respuesta del servidor: " + respuesta);

            // Cerrar la conexión
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
