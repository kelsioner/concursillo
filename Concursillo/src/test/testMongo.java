package test;

import basedatos.ConexionMongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class testMongo {

    public static void main(String[] args) {

        try {
            MongoDatabase baseDatos = ConexionMongo.conectar();

            System.out.println("Conexión correcta a MongoDB Atlas");
            System.out.println("Base de datos: " + baseDatos.getName());

            System.out.println("\nColecciones encontradas:");

            for (String nombreColeccion : baseDatos.listCollectionNames()) {
                System.out.println("- " + nombreColeccion);
            }

            MongoCollection<Document> coleccionPreguntas = baseDatos.getCollection("preguntas");
            long totalPreguntas = coleccionPreguntas.countDocuments();

            System.out.println("\nTotal de preguntas: " + totalPreguntas);

            MongoCollection<Document> coleccionPuntuaciones = baseDatos.getCollection("puntuaciones");
            long totalPuntuaciones = coleccionPuntuaciones.countDocuments();

            System.out.println("Total de puntuaciones: " + totalPuntuaciones);

            ConexionMongo.cerrar();

        } catch (Exception e) {
            System.err.println("Error al conectar con MongoDB Atlas");
            e.printStackTrace();
        }
    }
}