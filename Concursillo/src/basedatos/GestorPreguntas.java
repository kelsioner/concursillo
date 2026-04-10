package basedatos;

import java.util.ArrayList;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import modelo.Pregunta;

public class GestorPreguntas {

    private ArrayList<Pregunta> usadas = new ArrayList<Pregunta>();

    public ArrayList<Pregunta> obtenerPreguntasPorNivel(int nivelBuscado) {
        ArrayList<Pregunta> listaPreguntas = new ArrayList<Pregunta>();

        MongoDatabase baseDatos = ConexionMongo.conectar();
        MongoCollection<Document> coleccion = baseDatos.getCollection("preguntas");

        Document filtro = new Document("nivel", nivelBuscado);

        for (Document doc : coleccion.find(filtro)) {
            Pregunta pregunta = new Pregunta();

            pregunta.setPregunta(doc.getString("pregunta"));
            pregunta.setOpcionA(doc.getString("opcionA"));
            pregunta.setOpcionB(doc.getString("opcionB"));
            pregunta.setOpcionC(doc.getString("opcionC"));
            pregunta.setOpcionD(doc.getString("opcionD"));
            pregunta.setCorrecta(doc.getString("correcta"));
            pregunta.setNivel(doc.getInteger("nivel"));
            pregunta.setPista(doc.getString("pista"));
            pregunta.setCategoria(doc.getString("categoria"));

            listaPreguntas.add(pregunta);
        }

        return listaPreguntas;
    }

    public Pregunta obtenerPreguntaAleatoria(int nivelBuscado) {
        MongoDatabase baseDatos = ConexionMongo.conectar();
        MongoCollection<Document> coleccion = baseDatos.getCollection("preguntas");

        Document filtro = new Document("nivel", nivelBuscado);

        ArrayList<Pregunta> disponibles = new ArrayList<Pregunta>();

        for (Document doc : coleccion.find(filtro)) {
            Pregunta p = new Pregunta();

            p.setPregunta(doc.getString("pregunta"));
            p.setOpcionA(doc.getString("opcionA"));
            p.setOpcionB(doc.getString("opcionB"));
            p.setOpcionC(doc.getString("opcionC"));
            p.setOpcionD(doc.getString("opcionD"));
            p.setCorrecta(doc.getString("correcta"));
            p.setNivel(doc.getInteger("nivel"));
            p.setPista(doc.getString("pista"));
            p.setCategoria(doc.getString("categoria"));

            if (!usadas.contains(p)) {
                disponibles.add(p);
            }
        }

        if (disponibles.isEmpty()) {
            return null;
        }

        int indice = (int) (Math.random() * disponibles.size());
        Pregunta elegida = disponibles.get(indice);
        usadas.add(elegida);

        return elegida;
    }
}