package test;

import basedatos.ConexionMongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class TestPreguntaAtlas {

    public static void main(String[] args) {

        try {
            MongoDatabase baseDatos = ConexionMongo.conectar();

            MongoCollection<Document> coleccion = baseDatos.getCollection("preguntas");

            Document pregunta = coleccion.find().first();

            if (pregunta != null) {
                System.out.println("Pregunta encontrada:");
                System.out.println("Pregunta: " + pregunta.getString("pregunta"));
                System.out.println("A: " + pregunta.getString("opcionA"));
                System.out.println("B: " + pregunta.getString("opcionB"));
                System.out.println("C: " + pregunta.getString("opcionC"));
                System.out.println("D: " + pregunta.getString("opcionD"));
                System.out.println("Correcta: " + pregunta.getString("correcta"));
                System.out.println("Nivel: " + pregunta.getInteger("nivel"));
                System.out.println("Pista: " + pregunta.getString("pista"));
                System.out.println("Categoría: " + pregunta.getString("categoria"));
            } else {
                System.out.println("No se ha encontrado ninguna pregunta.");
            }

            ConexionMongo.cerrar();

        } catch (Exception e) {
            System.err.println("Error al leer pregunta:");
            e.printStackTrace();
        }
    }
}