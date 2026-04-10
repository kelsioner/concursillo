package test;
import basedatos.ConexionMongo;
import basedatos.GestorPreguntas;
import modelo.Pregunta;

public class testMongo {

    public static void main(String[] args) {

        GestorPreguntas gestor = new GestorPreguntas();

        // Sacar varias preguntas para comprobar que NO se repiten
        for (int i = 0; i < 20; i++) {

            Pregunta p = gestor.obtenerPreguntaAleatoria(1);

            if (p == null) {
                System.out.println("No quedan preguntas disponibles");
                break;
            }

            System.out.println("---------- PREGUNTA ----------");
            System.out.println(p.getPregunta());
            System.out.println("A: " + p.getOpcionA());
            System.out.println("B: " + p.getOpcionB());
            System.out.println("C: " + p.getOpcionC());
            System.out.println("D: " + p.getOpcionD());
            System.out.println("Correcta: " + p.getCorrecta());
            System.out.println("------------------------------\n");
        }

        ConexionMongo.cerrar();
    }
}