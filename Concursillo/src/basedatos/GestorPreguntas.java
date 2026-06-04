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

        try {
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
        } catch (Exception e) {
            System.err.println("Error al obtener preguntas del nivel " + nivelBuscado + ": " + e.getMessage());
        }

        return listaPreguntas;
    }

    public Pregunta obtenerPreguntaAleatoria(int nivelBuscado) {
        try {
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

        } catch (Exception e) {
            System.err.println("Error al obtener pregunta aleatoria del nivel " + nivelBuscado + ": " + e.getMessage());
            return null;
        }
    }

    public Pregunta obtenerPreguntaAleatoriaPorNivelYCategoria(int nivelBuscado, String categoriaBuscada) {

        if (categoriaBuscada == null || categoriaBuscada.equals("")) {
            return null;
        }

        try {
            MongoDatabase baseDatos = ConexionMongo.conectar();
            MongoCollection<Document> coleccion = baseDatos.getCollection("preguntas");

            Document filtro = new Document("nivel", nivelBuscado)
                    .append("categoria", categoriaBuscada);

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

        } catch (Exception e) {
            System.err.println("Error al obtener pregunta por nivel " + nivelBuscado
                    + " y categor\u00eda '" + categoriaBuscada + "': " + e.getMessage());
            return null;
        }
    }
}