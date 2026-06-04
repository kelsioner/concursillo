package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import modelo.Puntuacion;

public class PuntuacionTest {

    // ---------- Constructores ----------

    @Test
    void constructorVacioDevuelveValoresPorDefecto() {
        Puntuacion p = new Puntuacion();
        assertNull(p.getNombre());
        assertEquals(0, p.getPuntos());
    }

    @Test
    void constructorCompletoAsignaValores() {
        Puntuacion p = new Puntuacion("Paula", 10000);
        assertEquals("Paula", p.getNombre());
        assertEquals(10000, p.getPuntos());
    }

    // ---------- Getters y Setters ----------

    @Test
    void setNombreActualizaValor() {
        Puntuacion p = new Puntuacion();
        p.setNombre("Carlos");
        assertEquals("Carlos", p.getNombre());
    }

    @Test
    void setPuntosActualizaValor() {
        Puntuacion p = new Puntuacion();
        p.setPuntos(5000);
        assertEquals(5000, p.getPuntos());
    }

    @Test
    void setPuntosAceptaCero() {
        Puntuacion p = new Puntuacion("Test", 1000);
        p.setPuntos(0);
        assertEquals(0, p.getPuntos());
    }

    // ---------- toString ----------

    @Test
    void toStringFormatoEsperado() {
        Puntuacion p = new Puntuacion("Paula", 10000);
        assertEquals("Paula - 10000 puntos", p.toString());
    }

    @Test
    void toStringConCeroPuntos() {
        Puntuacion p = new Puntuacion("Jugador", 0);
        assertEquals("Jugador - 0 puntos", p.toString());
    }

    @Test
    void toStringReflejaCambiosDeSetter() {
        Puntuacion p = new Puntuacion("Inicial", 100);
        p.setNombre("Cambiado");
        p.setPuntos(999);
        assertEquals("Cambiado - 999 puntos", p.toString());
    }
}
