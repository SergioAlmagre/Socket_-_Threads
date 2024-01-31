import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Introduce un número para calcular su cuadrado (* para salir): ");
            String numero = scanner.nextLine();

            while (!numero.equals("*")) {
                enviarYRecibirResultado(numero);

                System.out.print("Introduce otro número para calcular su cuadrado (* para salir): ");
                numero = scanner.nextLine();
            }

            System.out.println("Cliente cerrado.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void enviarYRecibirResultado(String numero) throws Exception {
        String host = "localhost";
        int puerto = 9876;

        DatagramSocket socketCliente = new DatagramSocket();
        InetAddress direccionServidor = InetAddress.getByName(host);

        // Enviar número al servidor
        byte[] buffer = numero.getBytes();
        DatagramPacket paqueteSalida = new DatagramPacket(buffer, buffer.length, direccionServidor, puerto);
        socketCliente.send(paqueteSalida);

        // Recibir resultado del servidor
        byte[] bufferResultado = new byte[1024];
        DatagramPacket paqueteEntrada = new DatagramPacket(bufferResultado, bufferResultado.length);
        socketCliente.receive(paqueteEntrada);

        String resultado = new String(paqueteEntrada.getData(), 0, paqueteEntrada.getLength());
        System.out.println("Resultado recibido: " + resultado);

        socketCliente.close();
    }
}
