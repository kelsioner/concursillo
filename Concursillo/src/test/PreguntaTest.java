package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import modelo.Pregunta;

public class PreguntaTest {

    private Pregunta crearPreguntaEjemplo() {
        return new Pregunta(
            "¿Cuál es la capital de España?",
            "Barcelona", "Madrid", "Valencia", "Sevilla",
            "B", 1, "Está en el centro de la península", "Geografía"
        );
    }

    // ---------- Constructores ----------

    @Test
    void constructorVacioDevuelveNull() {
        Pregunta p = new Pregunta();
        assertNull(p.getPregunta());
        assertNull(p.getOpcionA());
        assertEquals(0, p.getNivel());
    }

    @Test
    void constructorCompletoAsignaTodosLosCampos() {
        Pregunta p = crearPreguntaEjemplo();
        assertEquals("¿Cuál es la capital de España?", p.getPregunta());
        assertEquals("Barcelona", p.getOpcionA());
        assertEquals("Madrid", p.getOpcionB());
        assertEquals("Valencia", p.getOpcionC());
        assertEquals("Sevilla", p.getOpcionD());
        assertEquals("B", p.getCorrecta());
        assertEquals(1, p.getNivel());
        assertEquals("Está en el centro de la península", p.getPista());
        assertEquals("Geografía", p.getCategoria());
    }

    // ---------- Getters y Setters ----------

    @Test
    void setPreguntaActualizaValor() {
        Pregunta p = new Pregunta();
        p.setPregunta("Nueva pregunta");
        assertEquals("Nueva pregunta", p.getPregunta());
    }

    @Test
    void setOpcionesActualizanValores() {
        Pregunta p = new Pregunta();
        p.setOpcionA("A1");
        p.setOpcionB("B1");
        p.setOpcionC("C1");
        p.setOpcionD("D1");
        assertEquals("A1", p.getOpcionA());
        assertEquals("B1", p.getOpcionB());
        assertEquals("C1", p.getOpcionC());
        assertEquals("D1", p.getOpcionD());
    }

    @Test
    void setCorrectaActualizaValor() {
        Pregunta p = new Pregunta();
        p.setCorrecta("C");
        assertEquals("C", p.getCorrecta());
    }

    @Test
    void setNivelActualizaValor() {
        Pregunta p = new Pregunta();
        p.setNivel(3);
        assertEquals(3, p.getNivel());
    }

    @Test
    void setPistaActualizaValor() {
        Pregunta p = new Pregunta();
        p.setPista("Pista nueva");
        assertEquals("Pista nueva", p.getPista());
    }

    @Test
    void setCategoriaActualizaValor() {
        Pregunta p = new Pregunta();
        p.setCategoria("Historia");
        assertEquals("Historia", p.getCategoria());
    }

    // ---------- equals ----------

    @Test
    void equalsConMismaInstanciaDevuelveTrue() {
        Pregunta p = crearPreguntaEjemplo();
        assertTrue(p.equals(p));
    }

    @Test
    void equalsConMismaPreguntaDevuelveTrue() {
        Pregunta p1 = crearPreguntaEjemplo();
        Pregunta p2 = new Pregunta(
            "¿Cuál es la capital de España?",
            "Otra A", "Otra B", "Otra C", "Otra D",
            "A", 2, "Otra pista", "Otra categoría"
        );
        assertTrue(p1.equals(p2));
    }

    @Test
    void equalsConPreguntaDiferenteDevuelveFalse() {
        Pregunta p1 = crearPreguntaEjemplo();
        Pregunta p2 = new Pregunta(
            "¿Cuál es la capital de Francia?",
            "Barcelona", "Madrid", "Valencia", "Sevilla",
            "B", 1, "Pista", "Geografía"
        );
        assertFalse(p1.equals(p2));
    }

    @Test
    void equalsConNullDevuelveFalse() {
        Pregunta p = crearPreguntaEjemplo();
        assertFalse(p.equals(null));
    }

    @Test
    void equalsConOtroTipoDevuelveFalse() {
        Pregunta p = crearPreguntaEjemplo();
        assertFalse(p.equals("Un string"));
    }

    // ---------- toString ----------

    @Test
    void toStringContieneElEnunciado() {
        Pregunta p = crearPreguntaEjemplo();
        String resultado = p.toString();
        assertTrue(resultado.contains("¿Cuál es la capital de España?"));
        assertTrue(resultado.startsWith("Pregunta: "));
    }
}
