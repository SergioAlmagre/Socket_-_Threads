import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.UUID;

public class Main {
    static String userInput = "";
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

        System.out.println("Puedes comenzar a escribir");

        // Hilo para la entrada del usuario
        Thread userInputThread = new Thread(() -> {
            try {
                while (true) {
                    System.out.println("\nMenú:");
                    System.out.println("1. Solicitar fecha y hora");
                    System.out.println("2. Otra opción");
                    System.out.println("3. Salir");
                    System.out.println("Seleccione una opción: ");

                    // Obtener la opción del usuario
                    userInput = in.readLine();

                    switch (userInput){
                        case "1":
                            // ENVIANDO AL GRUPO
                            DatagramPacket paquete2 = new DatagramPacket(userInput.getBytes(), userInput.getBytes().length, grupo, Puerto);
                            msSend.send(paquete2);
                            Thread.sleep(100);
                            break;
                        case "3":
                            break;

                    }
                    if (userInput.equals("3")) {
                        break;
                    }


                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        userInputThread.start();

        while (!msg.trim().equals("3")) {
            // Limpiar el búfer antes de recibir un nuevo mensaje
            buf = new byte[1000];

            // Recibe el paquete del servidor multicast
            DatagramPacket paquete = new DatagramPacket(buf, buf.length);
            msReceive.receive(paquete);
            msg = new String(paquete.getData());

            if(!msg.trim().equals(userInput)){
                System.out.println(msg.trim());
            }


        }

        // Detener el hilo de entrada del usuario
        userInputThread.interrupt();

        // Salir del grupo y cerrar sockets
        msReceive.leaveGroup(grupo);
        msReceive.close();
        msSend.close();
    }
}
