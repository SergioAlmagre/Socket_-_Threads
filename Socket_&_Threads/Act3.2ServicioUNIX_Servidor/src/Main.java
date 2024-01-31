import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.time.Instant;
import java.util.Date;

public class Main {
    //Variable de clase con la fecha en formato cadena:
    static String fechaStr = "";
    public static void main(String[] args) throws IOException {

        int puerto = 12345;//puerto multicast
        InetAddress grupo = InetAddress.getByName("225.0.0.1");//Grupo

        //Se crea el socket multicast.
        MulticastSocket msSend = new MulticastSocket(puerto);
        MulticastSocket msReceive = new MulticastSocket(puerto);

        msReceive.joinGroup(grupo);

        byte[] buf = new byte[1000];
        String msg = "";


                while(true){
//                    System.out.printf(msg);
//                    System.out.printf("Hola");
                    // Limpiar el búfer antes de recibir un nuevo mensaje
                    buf = new byte[1000];
                    // Recibe el paquete del servidor multicast
                    DatagramPacket paquete = new DatagramPacket(buf, buf.length);
                    msReceive.receive(paquete);
                    msg = new String(paquete.getData(), 0, paquete.getLength());


                    if (msg.equals("1")) {
                        actualizar_fecha();
//                        System.out.println(fechaStr);
                        System.out.println("Enviado datos...");
                        DatagramPacket paquete2 = new DatagramPacket(fechaStr.getBytes(), fechaStr.length(), grupo, puerto);
                        msReceive.send(paquete2);
                        msg = "";
                    }else if(msg.equals("3")){
                        msReceive .close ();//cierro socket
                        System.out.println ("Socket cerrado...");
                        break;
                    }
                }

    }

    private static void actualizar_fecha(){
        // Obtener el tiempo actual en segundos desde la época de UNIX
        long tiempoUnix = Instant.now().getEpochSecond();

//           System.out.println("Tiempo de UNIX en segundos: " + tiempoUnix);

        // Convertir el tiempo de UNIX a un objeto Date
        Date fecha = new Date(tiempoUnix * 1000L); // Multiplicar por 1000 para convertir segundos a milisegundos
        fechaStr = "Fecha y hora en formato de Java: " + fecha;
    }


}
