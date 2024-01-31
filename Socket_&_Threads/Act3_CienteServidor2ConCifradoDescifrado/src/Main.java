import com.sun.jdi.CharType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.UUID;

public class Main {
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
                while (true) {
//                    System.out.print("Datos a enviar al grupo: ");
                    String userInput = in.readLine();

                    if (userInput.equals("*")) {
                        break;
                    }

                    // Adjuntar el identificador único al mensaje

                    String userInputCifrado = cifrarDesdePunto(uniqueIdentifier + ": " + userInput,5);
                    String messageWithID = userInputCifrado;

                    // ENVIANDO AL GRUPO
                    DatagramPacket paquete2 = new DatagramPacket(messageWithID.getBytes(), messageWithID.getBytes().length, grupo, Puerto);
                    msSend.send(paquete2);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        userInputThread.start();

        while (!msg.trim().equals("*")) {
            // Limpiar el búfer antes de recibir un nuevo mensaje
            buf = new byte[1000];

            // Recibe el paquete del servidor multicast
            DatagramPacket paquete = new DatagramPacket(buf, buf.length);
            msReceive.receive(paquete);
            msg = new String(paquete.getData());
            String msgDes = descifrarDesdePunto(msg,5);

            // Verificar si el mensaje proviene de esta instancia
            if (!msg.startsWith(uniqueIdentifier)) {
//                // Recortar el mensaje para eliminar el identificador
//                String trimmedMessage = msg.substring(msg.indexOf(":") + 2).trim();
//                System.out.println("Mensaje entrante: " + trimmedMessage);
                System.out.println(msgDes.trim());
            }
        }

        // Detener el hilo de entrada del usuario
        userInputThread.interrupt();

        // Salir del grupo y cerrar sockets
        msReceive.leaveGroup(grupo);
        msReceive.close();
        msSend.close();
    }



    public static String cifrarDesdePunto(String texto, int desplazamiento) {
        StringBuilder resultado = new StringBuilder();

        boolean inicioEncontrado = false;

        for (int i = 0; i < texto.length(); i++) {
            char caracter = texto.charAt(i);

            if (inicioEncontrado) {
                if (Character.isLetter(caracter)) {
                    char inicio = Character.isLowerCase(caracter) ? 'a' : 'A';
                    caracter = (char) (inicio + (caracter - inicio + desplazamiento) % 26);
                }
            } else if (caracter == ':') {
                inicioEncontrado = true;
            }

            resultado.append(caracter);
        }

        return resultado.toString();
    }

    public static String descifrarDesdePunto(String textoCifrado, int desplazamiento) {
        return cifrarDesdePunto(textoCifrado, -desplazamiento);
    }



}
