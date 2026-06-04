package basedatos;

import java.util.ArrayList;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;

import modelo.Puntuacion;

public class GestorPuntuaciones {

    public void guardarPuntuacion(Puntuacion puntuacion) {

        try {
            MongoDatabase baseDatos = ConexionMongo.conectar();
            MongoCollection<Document> coleccion = baseDatos.getCollection("puntuaciones");

            Document doc = new Document("nombre", puntuacion.getNombre())
                    .append("puntos", puntuacion.getPuntos());

            coleccion.insertOne(doc);
        } catch (Exception e) {
            System.err.println("Error al guardar la puntuaci\u00f3n de '" + puntuacion.getNombre() + "': " + e.getMessage());
            throw new RuntimeException("No se pudo guardar la puntuaci\u00f3n.", e);
        }
    }

    public ArrayList<Puntuacion> obtenerRanking() {

        ArrayList<Puntuacion> ranking = new ArrayList<Puntuacion>();

        try {
            MongoDatabase baseDatos = ConexionMongo.conectar();
            MongoCollection<Document> coleccion = baseDatos.getCollection("puntuaciones");

            FindIterable<Document> documentos = coleccion.find().sort(Sorts.descending("puntos"));

            for (Document doc : documentos) {
                Puntuacion p = new Puntuacion();

                p.setNombre(doc.getString("nombre"));
                p.setPuntos(doc.getInteger("puntos"));

                ranking.add(p);
            }
        } catch (Exception e) {
            System.err.println("Error al obtener el ranking: " + e.getMessage());
            throw new RuntimeException("No se pudo obtener el ranking.", e);
        }

        return ranking;
    }
}