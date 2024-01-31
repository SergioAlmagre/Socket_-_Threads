import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    private static final int PORT = 8080;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor esperando conexiones en el puerto " + PORT);

            // Utilizar JFileChooser para seleccionar el archivo
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Seleccione el archivo para enviar a los clientes");
            int userSelection = fileChooser.showOpenDialog(null);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Cliente conectado desde " + clientSocket.getInetAddress());

                    // Crear un nuevo hilo para manejar la conexión con el cliente
                    Thread clientThread = new Thread(() -> sendFile(clientSocket, selectedFile));
                    clientThread.start();
                }
            } else {
                System.out.println("Operación cancelada por el usuario.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendFile(Socket clientSocket, File selectedFile) {
        try (DataOutputStream dataOut = new DataOutputStream(clientSocket.getOutputStream());
             FileInputStream fileInputStream = new FileInputStream(selectedFile)) {

            // Enviar el nombre del archivo junto con su extensión al cliente
            String fileNameWithExtension = selectedFile.getName();
            dataOut.writeUTF(fileNameWithExtension);

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                dataOut.write(buffer, 0, bytesRead);
            }

            System.out.println("Archivo enviado a " + clientSocket.getInetAddress() + " con éxito.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
