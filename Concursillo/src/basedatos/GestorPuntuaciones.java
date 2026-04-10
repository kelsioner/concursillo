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

        MongoDatabase baseDatos = ConexionMongo.conectar();
        MongoCollection<Document> coleccion = baseDatos.getCollection("puntuaciones");

        Document doc = new Document("nombre", puntuacion.getNombre())
                .append("puntos", puntuacion.getPuntos());

        coleccion.insertOne(doc);
    }

    public ArrayList<Puntuacion> obtenerRanking() {

        ArrayList<Puntuacion> ranking = new ArrayList<Puntuacion>();

        MongoDatabase baseDatos = ConexionMongo.conectar();
        MongoCollection<Document> coleccion = baseDatos.getCollection("puntuaciones");

        FindIterable<Document> documentos = coleccion.find().sort(Sorts.descending("puntos"));

        for (Document doc : documentos) {
            Puntuacion p = new Puntuacion();

            p.setNombre(doc.getString("nombre"));
            p.setPuntos(doc.getInteger("puntos"));

            ranking.add(p);
        }

        return ranking;
    }
}