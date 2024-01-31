import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 8080;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             DataInputStream dataIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()))) {

            saveFile(dataIn);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveFile(DataInputStream dataIn) {
        try {
            // Recibir el nombre del archivo junto con su extensión desde el servidor
            String fileNameWithExtension = dataIn.readUTF();
            System.out.println("Recibiendo archivo: " + fileNameWithExtension);

            // Utilizar JFileChooser para seleccionar la ruta y el nombre del archivo
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Seleccione la ruta y el nombre para guardar el archivo recibido");
            fileChooser.setSelectedFile(new File(fileNameWithExtension));  // Establecer el nombre predeterminado del archivo

            int userSelection = fileChooser.showSaveDialog(null);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                // Obtener el archivo seleccionado (incluyendo la ruta y el nombre)
                File selectedFile = fileChooser.getSelectedFile();

                try (FileOutputStream fileOutputStream = new FileOutputStream(selectedFile)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;

                    while ((bytesRead = dataIn.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, bytesRead);
                    }

                    System.out.println("Archivo recibido y guardado en: " + selectedFile.getAbsolutePath());

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                System.out.println("Operación cancelada por el usuario.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}