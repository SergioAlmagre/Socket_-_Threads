import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {
        String Host = "localhost";
        int Puerto = 6000; // Puerto remoto
        Socket Cliente = new Socket(Host, Puerto);

        // CREO FLUJO DE SALIDA AL SERVIDOR
        PrintWriter fsalida = new PrintWriter(Cliente.getOutputStream(), true);
        // CREO FLUJO DE ENTRADA AL SERVIDOR
        BufferedReader fentrada = new BufferedReader(new InputStreamReader(Cliente.getInputStream()));
        // FLUJO PARA ENTRADA ESTANDAR
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String numero, resultado;
        System.out.print("Introduce número para calcular su cuadrado (* para salir): ");
        numero = in.readLine(); // Lectura por teclado

        while (numero != null) {
            fsalida.println(numero); // Envío número al servidor
            resultado = fentrada.readLine(); // Recibo resultado del servidor
            System.out.println(" =>Resultado: " + resultado);

            System.out.print("Introduce número para calcular su cuadrado (* para salir): ");
            numero = in.readLine(); // Lectura por teclado
            if (numero.equals("*")) break;
        }

        // CERRAR STREAMS Y SOCKET
        fsalida.close();
        fentrada.close();
        System.out.println("Fin del envío... ");
        in.close();
        Cliente.close();
    }
}
