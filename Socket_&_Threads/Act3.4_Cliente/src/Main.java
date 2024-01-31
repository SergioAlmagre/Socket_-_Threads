import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main {
    static String userInput = "";
    static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    static int puerto = 7;
    static boolean salir = false;
    public static void main(String[] args) throws IOException {
        String host = "127.0.0.1"; // Cambia esto con la dirección del servidor

        while(!salir){
            try {
                pedirNumeroPuerto();
                // Establecer la conexión con el servidor
                Socket socket = new Socket(host, puerto);

                // Obtener flujos de entrada y salida del socket
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

                // Leer desde la consola y enviar al servidor
                BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
                String userInput;
                System.out.println("Escribe líneas para enviar al servidor. Escribe '.' para salir.");

                while (true) {
                    userInput = consoleReader.readLine();
                    writer.println(userInput);

                    if (".".equals(userInput)) {
                        break;
                    }

                    // Recibir la respuesta del servidor
                    String respuesta = reader.readLine();
                    System.out.println("Respuesta del servidor: " + respuesta);
                }

                // Cerrar la conexión con el servidor
                socket.close();

            } catch (UnknownHostException e) {
                System.err.println("Error: Host desconocido. Verifica la dirección del servidor.");
            } catch (ConnectException e) {
                System.err.println("Error: No se puede conectar al servidor. Verifica el puerto y asegúrate de que el servidor esté en ejecución.");
            } catch (IOException e) {
                System.err.println("Error de entrada/salida al conectar con el servidor.");
                e.printStackTrace();
            }
            menuSalida();
        }
    }

    // Obtener el número de puerto desde la línea de comandos o usar el puerto predeterminado (7)
    private static void pedirNumeroPuerto(){
        boolean correct = false;
        in = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Elige el puerto del servidor");
        // Tratamiento de errores en la elección del puerto
        while(!correct){
            try{
                userInput = in.readLine();
                if(userInput.isBlank() || userInput.isEmpty()){
                    correct = true;
                }else{
                    puerto = Integer.parseInt(userInput);
                    if (puerto < 9999 && puerto > 0) {
                        correct = true;
                    }else{
                        System.out.println("Número de puerto no válido, vuelva a introducir otro");
                    }
                }
            }catch(NumberFormatException | IOException e){
                System.out.println("Número de puerto no válido, vuelva a introducir otro");
            }
        }
    }

    private static void menuSalida() throws IOException {
        System.out.println("¿Desea salir del servicio? Y/N");
        userInput = in.readLine();

        // equalsIgnoreCase  realiza una comparación de cadenas sin tener en cuenta las diferencias entre mayúsculas
        // y minúsculas. Esto significa que compara las cadenas sin importar si las letras son mayúsculas o minúsculas.
        if(userInput.equalsIgnoreCase("Y")){
            salir = true;
        }
    }

}