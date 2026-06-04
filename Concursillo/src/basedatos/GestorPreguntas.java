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
            listaPreguntas.add(DocumentMapper.documentToPregunta(doc));
        }

        return listaPreguntas;
    }

    public Pregunta obtenerPreguntaAleatoria(int nivelBuscado) {
        Document filtro = new Document("nivel", nivelBuscado);
        return seleccionarPreguntaAleatoria(filtro);
    }

    public Pregunta obtenerPreguntaAleatoriaPorNivelYCategoria(int nivelBuscado, String categoriaBuscada) {
        if (categoriaBuscada == null || categoriaBuscada.equals("")) {
            return null;
        }

        Document filtro = new Document("nivel", nivelBuscado)
                .append("categoria", categoriaBuscada);

        return seleccionarPreguntaAleatoria(filtro);
    }

    private Pregunta seleccionarPreguntaAleatoria(Document filtro) {
        MongoDatabase baseDatos = ConexionMongo.conectar();
        MongoCollection<Document> coleccion = baseDatos.getCollection("preguntas");

        ArrayList<Pregunta> disponibles = new ArrayList<Pregunta>();

        for (Document doc : coleccion.find(filtro)) {
            Pregunta p = DocumentMapper.documentToPregunta(doc);
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
