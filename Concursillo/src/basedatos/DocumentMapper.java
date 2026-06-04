package basedatos;

import org.bson.Document;

import modelo.Pregunta;

public class DocumentMapper {

    private DocumentMapper() {
    }

    public static Pregunta documentToPregunta(Document doc) {
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
        return p;
    }
}
