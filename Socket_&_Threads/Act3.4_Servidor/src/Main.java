import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * El término "servidor concurrente" se refiere a un servidor capaz de manejar múltiples conexiones
 * de clientes de manera simultánea, es decir, atender a varios clientes al mismo tiempo sin esperar
 * a que se complete una conexión antes de aceptar otra. Esto mejora la eficiencia y la capacidad de
 * respuesta del servidor, especialmente en entornos donde hay un alto número de solicitudes concurrentes.
 */

public class Main {
    static String userInput = "";
    static int puerto = 7;
    public static void main(String[] args) throws IOException {

        pedirNumeroPuerto();

        /**
         * ServerSocket: Se crea un objeto ServerSocket en el puerto especificado (puerto). Este ServerSocket espera conexiones entrantes de clientes.
         * Bucle Infinito (while (true)): El servidor entra en un bucle infinito que espera continuamente nuevas conexiones de clientes.
         * serverSocket.accept(): La llamada a accept() bloquea la ejecución del servidor hasta que un cliente se conecta. Cuando un cliente se conecta, accept() devuelve un nuevo objeto Socket que representa la conexión con ese cliente.
         * Creación de un Hilo: Se crea un nuevo hilo utilizando una expresión lambda para manejar la conexión con el cliente. Esto permite que el servidor continúe aceptando conexiones mientras el hilo maneja la comunicación con el cliente
         */

        try {
            // Crear un servidor en el puerto especificado
            ServerSocket serverSocket = new ServerSocket(puerto);
            System.out.println("Servidor Eco Concurrente iniciado en el puerto " + puerto);

            while (true) {
                // Esperar a que un cliente se conecte
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde " + clientSocket.getInetAddress());

                // Iniciar un hilo para manejar la conexión con el cliente!! aquí esta la chicha como dice Joaquín
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }// Final del main


    /**
     * @param clientSocket
     * Creación de Flujos de Entrada y Salida: Se crean objetos BufferedReader y PrintWriter para manejar la entrada y salida desde y hacia el cliente respectivamente. Estos objetos se crean utilizando los flujos de entrada y salida del Socket del cliente.
     * Lectura y Escritura: El servidor lee líneas del cliente mediante reader.readLine() y las envía de vuelta al cliente mediante writer.println(). El bucle continúa hasta que se recibe una línea que contiene solo un punto ".".
     * Cierre de la Conexión: Una vez que el cliente envía un punto ".", el bucle termina y se cierra la conexión con ese cliente utilizando clientSocket.close().
     */
    private static void handleClient(Socket clientSocket) {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true)
        ) {
            String linea;

            // Leer líneas del cliente y enviarlas de vuelta hasta recibir un punto "."
            while ((linea = reader.readLine()) != null && !linea.equals(".")) {
                writer.println("Servidor Eco: " + linea);
            }

            // Cerrar la conexión con el cliente
            clientSocket.close();
            System.out.println("Conexión cerrada con " + clientSocket.getInetAddress());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Obtener el número de puerto desde la línea de comandos o usar el puerto predeterminado (7)
    private static void pedirNumeroPuerto(){

        boolean correct = false;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

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


}
