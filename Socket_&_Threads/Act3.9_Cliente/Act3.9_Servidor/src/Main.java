import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Main {
    public static void main(String[] args) {
        try {
            int puerto = 9876;
            DatagramSocket socketServidor = new DatagramSocket(puerto);

            byte[] buffer = new byte[1024];

            while (true) {
                DatagramPacket paqueteEntrada = new DatagramPacket(buffer, buffer.length);
                socketServidor.receive(paqueteEntrada);

                String numero = new String(paqueteEntrada.getData(), 0, paqueteEntrada.getLength());
                System.out.println("Número recibido: " + numero);

                try {
                    // Calcular el cuadrado
                    double num = Double.parseDouble(numero);
                    double cuadrado = num * num;
                    String resultado = String.valueOf(cuadrado);

                    // Enviar el resultado al cliente
                    DatagramPacket paqueteSalida = new DatagramPacket(resultado.getBytes(), resultado.length(), paqueteEntrada.getAddress(), paqueteEntrada.getPort());
                    socketServidor.send(paqueteSalida);

                    System.out.println("Resultado enviado: " + resultado);
                } catch (NumberFormatException e) {
                    // Si no se puede convertir a número, enviar un mensaje de error al cliente
                    String error = "Error: Por favor, introduzca un número válido.";
                    DatagramPacket paqueteError = new DatagramPacket(error.getBytes(), error.length(), paqueteEntrada.getAddress(), paqueteEntrada.getPort());
                    socketServidor.send(paqueteError);

                    System.out.println("Error enviado al cliente.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
