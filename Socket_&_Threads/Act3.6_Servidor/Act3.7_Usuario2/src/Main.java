import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.UUID;

public class Main {
         static boolean terminar = false;
    public static void main(String[] args) throws IOException {

        // Se crea el socket multicast
        int Puerto = 12345; // Puerto multicast
        InetAddress grupo = InetAddress.getByName("225.0.0.1"); // Grupo

        // Socket para recibir mensajes
        MulticastSocket msReceive = new MulticastSocket(Puerto);
        msReceive.joinGroup(grupo);

        // Socket para enviar mensajes
        MulticastSocket msSend = new MulticastSocket();

        // FLUJO PARA ENTRADA ESTANDAR
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        String msg = "";
        byte[] buf = new byte[1000];

        // Identificador único para esta instancia
        System.out.println("Primero, escribe tu nombre de usuario");
        String uniqueIdentifier = in.readLine();
        System.out.println("Ahora ya puedes comenzar a escribir en el chat");

        // Hilo para la entrada del usuario
        Thread userInputThread = new Thread(() -> {
            try {
                while (!terminar) {
//                    System.out.print("Datos a enviar al grupo: ");
                    String userInput = in.readLine();
                    if (userInput.equalsIgnoreCase ("terminar")) {
                        terminar = true;
                    }

                    // Adjuntar el identificador único al mensaje
                    String messageWithID = uniqueIdentifier + ": " + userInput;

                    // ENVIANDO AL GRUPO
                    DatagramPacket paquete2 = new DatagramPacket(messageWithID.getBytes(), messageWithID.getBytes().length, grupo, Puerto);
                    msSend.send(paquete2);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        userInputThread.start();

        while (!terminar) {
            // Limpiar el búfer antes de recibir un nuevo mensaje
            buf = new byte[1000];

            // Recibe el paquete del servidor multicast
            DatagramPacket paquete = new DatagramPacket(buf, buf.length);
            msReceive.receive(paquete);
            msg = new String(paquete.getData());

            // Verificar si el mensaje corresponde a uno mismo
            if (!msg.trim().startsWith(uniqueIdentifier)) {
                // Imprimir el mensaje recibido
                System.out.println(msg.trim());
            }

            // Verificar si termina el programa
            if (msg.trim().equalsIgnoreCase(uniqueIdentifier + ": " + "terminar") || msg.trim().substring(msg.indexOf(":") + 1).trim().equalsIgnoreCase(" terminar")) {
                terminar = true;
            }



        }

        // Detener el hilo de entrada del usuario
        userInputThread.interrupt();

        // Salir del grupo y cerrar sockets
        msReceive.leaveGroup(grupo);
        msReceive.close();
        msSend.close();
        System.out.println("Hasta la proxima!");
    }
}