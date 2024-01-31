import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {
        int numeroPuerto = 6000;
        ServerSocket servidor = new ServerSocket(numeroPuerto);
        String numero;
        System.out.println("Esperando Conexión...");
        Socket clienteConectado = servidor.accept();
        System.out.println("Cliente conectado ...");

        // CREO FLUJO DE SALIDA AL CLIENTE
        PrintWriter fsalida = new PrintWriter(clienteConectado.getOutputStream(), true);
        // CREO FLUJO DE ENTRADA DEL CLIENTE
        BufferedReader fentrada = new BufferedReader(new InputStreamReader(clienteConectado.getInputStream()));

        while ((numero = fentrada.readLine()) != null) // Recibo número del cliente
        {
            try {
                // Calcular el cuadrado
                double num = Double.parseDouble(numero);
                double cuadrado = num * num;

                // Enviar el cuadrado al cliente
                fsalida.println("Cuadrado de " + numero + ": " + cuadrado);
                System.out.println("Recibiendo: " + numero + ", Enviando cuadrado: " + cuadrado);
            } catch (NumberFormatException e) {
                // Si no se puede convertir a número, enviar un mensaje de error
                fsalida.println("Error: Por favor, introduzca un número válido.");
            }

            if (numero.equals("*")) break;
        }

        // CERRAR STREAMS Y SOCKETS
        System.out.println("Cerrando conexión ...");
        fentrada.close();
        fsalida.close();
        clienteConectado.close();
        servidor.close();
    }
}
