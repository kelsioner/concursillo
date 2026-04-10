import java.util.ArrayList;
import java.util.Scanner;

import basedatos.ConexionMongo;
import basedatos.GestorPreguntas;
import basedatos.GestorPuntuaciones;
import modelo.Pregunta;
import modelo.Puntuacion;

public class PrincipalApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        GestorPreguntas gestor = new GestorPreguntas();
        GestorPuntuaciones gestorPuntuaciones = new GestorPuntuaciones();

        int nivel = 1;
        int contador = 0;
        int puntos = 0;

        System.out.println("BIENVENIDO AL CONCURSILLO");
        System.out.print("Introduce tu nombre: ");
        String nombre = sc.nextLine();

        while (nivel <= 3) {

            Pregunta p = gestor.obtenerPreguntaAleatoria(nivel);

            if (p == null) {
                System.out.println("No quedan preguntas en el nivel " + nivel);
                break;
            }

            System.out.println("\nNivel " + nivel);
            System.out.println(p.getPregunta());
            System.out.println("A: " + p.getOpcionA());
            System.out.println("B: " + p.getOpcionB());
            System.out.println("C: " + p.getOpcionC());
            System.out.println("D: " + p.getOpcionD());

            System.out.print("Tu respuesta: ");
            String respuesta = sc.nextLine().toUpperCase();

            if (respuesta.equals(p.getCorrecta())) {
                System.out.println("Correcto");
                contador++;
                puntos++;
            } else {
                System.out.println("Incorrecto. Fin del juego.");
                break;
            }

            if (contador == 5 || contador == 10) {
                nivel++;
                System.out.println("Subes al nivel " + nivel);
            }

            if (contador == 15) {
                System.out.println("Has ganado el juego");
                break;
            }
        }

        System.out.println("\nJugador: " + nombre);
        System.out.println("Puntuacion final: " + puntos);

        Puntuacion puntuacion = new Puntuacion(nombre, puntos);
        gestorPuntuaciones.guardarPuntuacion(puntuacion);

        System.out.println("\nPuntuacion guardada en MongoDB.");

        System.out.println("\nRANKING:");
        ArrayList<Puntuacion> ranking = gestorPuntuaciones.obtenerRanking();

        for (Puntuacion p : ranking) {
            System.out.println(p);
        }

        ConexionMongo.cerrar();
        sc.close();
    }
}