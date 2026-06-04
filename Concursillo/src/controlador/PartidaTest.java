package controlador;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import basedatos.GestorPreguntas;
import basedatos.GestorPuntuaciones;
import modelo.Pregunta;
import modelo.Puntuacion;

public class PartidaTest {

    private Pregunta preguntaBase;
    private StubGestorPreguntas stubPreguntas;
    private StubGestorPuntuaciones stubPuntuaciones;
    private Partida partida;

    @BeforeEach
    void setUp() {
        preguntaBase = new Pregunta(
            "¿Cuál es la capital de España?",
            "Barcelona", "Madrid", "Valencia", "Sevilla",
            "B", 1, "Centro de la península", "Geografía"
        );
        stubPreguntas = new StubGestorPreguntas(preguntaBase);
        stubPuntuaciones = new StubGestorPuntuaciones();
        partida = new Partida("TestPlayer", stubPreguntas, stubPuntuaciones, preguntaBase);
    }

    // ---------- Estado inicial ----------

    @Test
    void estadoInicialEsCorrecto() {
        assertEquals("TestPlayer", partida.getNombreJugador());
        assertEquals(1, partida.getNivelActual());
        assertEquals(0, partida.getDineroAcumulado());
        assertFalse(partida.isPartidaTerminada());
        assertNotNull(partida.getPreguntaActual());
    }

    @Test
    void comodinesNoUsadosAlInicio() {
        assertFalse(partida.isComodin5050Usado());
        assertFalse(partida.isComodinPublicoUsado());
        assertFalse(partida.isComodinLlamadaUsado());
        assertFalse(partida.isComodinSacrificioUsado());
        assertFalse(partida.isComodinRuletaUsado());
        assertFalse(partida.isComodinMagoUsado());
    }

    @Test
    void preguntaNulaTerminaPartida() {
        Partida p = new Partida("Test", stubPreguntas, stubPuntuaciones, null);
        assertTrue(p.isPartidaTerminada());
    }

    // ---------- comprobarRespuesta ----------

    @Test
    void respuestaCorrectaAvanzaNivel() {
        assertTrue(partida.comprobarRespuesta("B"));
        assertEquals(2, partida.getNivelActual());
        assertEquals(100, partida.getDineroAcumulado());
        assertFalse(partida.isPartidaTerminada());
    }

    @Test
    void respuestaCorrectaMinusculaFunciona() {
        assertTrue(partida.comprobarRespuesta("b"));
        assertEquals(2, partida.getNivelActual());
    }

    @Test
    void respuestaIncorrectaTerminaPartida() {
        assertFalse(partida.comprobarRespuesta("A"));
        assertTrue(partida.isPartidaTerminada());
    }

    @Test
    void respuestaIncorrectaNivel1DaCeroDinero() {
        partida.comprobarRespuesta("A");
        assertEquals(0, partida.getDineroAcumulado());
    }

    @Test
    void respuestaNullDevuelveFalse() {
        assertFalse(partida.comprobarRespuesta(null));
        assertFalse(partida.isPartidaTerminada());
    }

    @Test
    void respuestaConPartidaTerminadaDevuelveFalse() {
        partida.plantarse();
        assertFalse(partida.comprobarRespuesta("B"));
    }

    // ---------- Premio seguro tras fallo ----------

    @Test
    void falloEnNivel6DaPremioPregunta5() {
        avanzarHastaNivel(6);
        partida.comprobarRespuesta("X");
        assertEquals(1500, partida.getDineroAcumulado());
    }

    @Test
    void falloEnNivel11DaPremioPregunta10() {
        avanzarHastaNivel(11);
        partida.comprobarRespuesta("X");
        assertEquals(30000, partida.getDineroAcumulado());
    }

    @Test
    void falloEnNivel5DaCeroDinero() {
        avanzarHastaNivel(5);
        partida.comprobarRespuesta("X");
        assertEquals(0, partida.getDineroAcumulado());
    }

    // ---------- Premios acumulados por acierto ----------

    @Test
    void premioNivel1Es100() {
        partida.comprobarRespuesta("B");
        assertEquals(100, partida.getDineroAcumulado());
    }

    @Test
    void premioNivel5Es1500() {
        avanzarHastaNivel(5);
        partida.comprobarRespuesta("B");
        assertEquals(1500, partida.getDineroAcumulado());
    }

    // ---------- plantarse ----------

    @Test
    void plantarseTerminaPartida() {
        partida.plantarse();
        assertTrue(partida.isPartidaTerminada());
    }

    @Test
    void plantarseConservaDineroAcumulado() {
        partida.comprobarRespuesta("B"); // nivel 1 → 100€
        partida.plantarse();
        assertEquals(100, partida.getDineroAcumulado());
        assertTrue(partida.isPartidaTerminada());
    }

    @Test
    void plantarseGuardaPuntuacion() {
        partida.comprobarRespuesta("B");
        partida.plantarse();
        assertTrue(stubPuntuaciones.puntuacionGuardada);
        assertEquals("TestPlayer", stubPuntuaciones.ultimaPuntuacion.getNombre());
        assertEquals(100, stubPuntuaciones.ultimaPuntuacion.getPuntos());
    }

    @Test
    void plantarseDosVecesNoGuardaDoble() {
        partida.plantarse();
        int vecesGuardada = stubPuntuaciones.vecesGuardada;
        partida.plantarse();
        assertEquals(vecesGuardada, stubPuntuaciones.vecesGuardada);
    }

    // ---------- Comodín 50:50 ----------

    @Test
    void comodin5050EliminaDosOpciones() {
        String[] eliminadas = partida.usarComodin5050(null);
        assertEquals(2, eliminadas.length);
        assertTrue(partida.isComodin5050Usado());
    }

    @Test
    void comodin5050NoEliminaRespuestaCorrecta() {
        String[] eliminadas = partida.usarComodin5050(null);
        for (String e : eliminadas) {
            assertNotEquals("B", e);
        }
    }

    @Test
    void comodin5050SegundoUsoDevuelveVacio() {
        partida.usarComodin5050(null);
        String[] segundoUso = partida.usarComodin5050(null);
        assertEquals(0, segundoUso.length);
    }

    @Test
    void comodin5050ConOpcionesYaEliminadas() {
        ArrayList<String> yaEliminadas = new ArrayList<>();
        yaEliminadas.add("A");
        String[] eliminadas = partida.usarComodin5050(yaEliminadas);
        for (String e : eliminadas) {
            assertNotEquals("A", e);
        }
    }

    @Test
    void comodin5050ConPartidaTerminadaDevuelveVacio() {
        partida.plantarse();
        String[] eliminadas = partida.usarComodin5050(null);
        assertEquals(0, eliminadas.length);
    }

    // ---------- Comodín Público / Chat ----------

    @Test
    void comodinPublicoDevuelveCuatroPorcentajes() {
        int[] porcentajes = partida.usarComodinPublico();
        assertEquals(4, porcentajes.length);
        assertTrue(partida.isComodinPublicoUsado());
    }

    @Test
    void comodinPublicoPorcentajesSumanCien() {
        int[] porcentajes = partida.usarComodinPublico();
        int suma = 0;
        for (int p : porcentajes) {
            suma += p;
        }
        assertEquals(100, suma);
    }

    @Test
    void comodinChatEsEquivalenteAPublico() {
        int[] porcentajes = partida.usarComodinChat();
        assertEquals(4, porcentajes.length);
        assertTrue(partida.isComodinPublicoUsado());
    }

    @Test
    void comodinPublicoSegundoUsoDevuelveVacio() {
        partida.usarComodinPublico();
        int[] segundoUso = partida.usarComodinPublico();
        assertEquals(0, segundoUso.length);
    }

    @Test
    void comodinPublicoConPartidaTerminadaDevuelveVacio() {
        partida.plantarse();
        int[] porcentajes = partida.usarComodinPublico();
        assertEquals(0, porcentajes.length);
    }

    // ---------- Comodín Llamada ----------

    @Test
    void comodinLlamadaDevuelveMensaje() {
        String resultado = partida.usarComodinLlamada();
        assertNotNull(resultado);
        assertTrue(resultado.contains("Tu contacto cree que la respuesta es la"));
        assertTrue(partida.isComodinLlamadaUsado());
    }

    @Test
    void comodinLlamadaSegundoUsoDevuelveMensajeNoDisponible() {
        partida.usarComodinLlamada();
        String segundo = partida.usarComodinLlamada();
        assertTrue(segundo.contains("no esta disponible"));
    }

    @Test
    void comodinLlamadaConPartidaTerminadaDevuelveMensajeNoDisponible() {
        partida.plantarse();
        String resultado = partida.usarComodinLlamada();
        assertTrue(resultado.contains("no esta disponible"));
    }

    // ---------- Comodín Sacrificio ----------

    @Test
    void comodinSacrificioDevuelveLetra() {
        String resultado = partida.usarComodinSacrificio();
        assertNotNull(resultado);
        assertTrue(resultado.length() == 1);
        assertTrue("ABCD".contains(resultado));
        assertTrue(partida.isComodinSacrificioUsado());
    }

    @Test
    void comodinSacrificioSegundoUsoDevuelveVacio() {
        partida.usarComodinSacrificio();
        String segundo = partida.usarComodinSacrificio();
        assertEquals("", segundo);
    }

    // ---------- Comodín Ruleta ----------

    @Test
    void comodinRuletaDevuelveArrayDeEliminadas() {
        String[] eliminadas = partida.usarComodinRuleta(null);
        assertNotNull(eliminadas);
        assertTrue(partida.isComodinRuletaUsado());
    }

    @Test
    void comodinRuletaNoEliminaRespuestaCorrecta() {
        String[] eliminadas = partida.usarComodinRuleta(null);
        for (String e : eliminadas) {
            assertNotEquals("B", e);
        }
    }

    @Test
    void comodinRuletaSegundoUsoDevuelveVacio() {
        partida.usarComodinRuleta(null);
        String[] segundo = partida.usarComodinRuleta(null);
        assertEquals(0, segundo.length);
    }

    @Test
    void comodinRuletaNumeroEntre0Y3() {
        partida.usarComodinRuleta(null);
        int num = partida.getUltimoNumeroRuleta();
        assertTrue(num >= 0 && num <= 3);
    }

    // ---------- Comodín Mago ----------

    @Test
    void comodinMagoNoDisponibleAntesDePregunta7() {
        assertFalse(partida.usarComodinMago());
        assertFalse(partida.isComodinMagoUsado());
    }

    @Test
    void puedeUsarComodinMagoDesdeNivel7() {
        avanzarHastaNivel(7);
        assertTrue(partida.puedeUsarComodinMago());
    }

    @Test
    void comodinMagoDesdeNivel7CambiaPregunta() {
        Pregunta otraPregunta = new Pregunta(
            "¿Otra pregunta?", "A1", "B1", "C1", "D1",
            "A", 2, "Pista", "Ciencia"
        );
        stubPreguntas.preguntaParaMago = otraPregunta;
        avanzarHastaNivel(7);
        assertTrue(partida.usarComodinMago());
        assertTrue(partida.isComodinMagoUsado());
    }

    // ---------- Recuperar Comodín ----------

    @Test
    void noPuedeRecuperarComodinAntesDePregunta8() {
        partida.usarComodin5050(null);
        assertFalse(partida.recuperarComodinElegido("5050"));
    }

    @Test
    void recuperarComodinDesdeNivel8() {
        avanzarHastaNivel(8);
        partida.usarComodin5050(null);
        assertTrue(partida.isComodin5050Usado());
        assertTrue(partida.recuperarComodinElegido("5050"));
        assertFalse(partida.isComodin5050Usado());
    }

    @Test
    void recuperarComodinSoloSePuedeUsarUnaVez() {
        avanzarHastaNivel(8);
        partida.usarComodin5050(null);
        partida.usarComodinLlamada();
        assertTrue(partida.recuperarComodinElegido("5050"));
        assertFalse(partida.recuperarComodinElegido("llamada"));
    }

    @Test
    void recuperarComodinNoUsadoDevuelveFalse() {
        avanzarHastaNivel(8);
        assertFalse(partida.recuperarComodinElegido("5050"));
    }

    @Test
    void recuperarComodinNullDevuelveFalse() {
        avanzarHastaNivel(8);
        assertFalse(partida.recuperarComodinElegido(null));
    }

    @Test
    void recuperarComodinVacioDevuelveFalse() {
        avanzarHastaNivel(8);
        assertFalse(partida.recuperarComodinElegido(""));
    }

    @Test
    void puedeRecuperarComodinDesdeNivel8() {
        avanzarHastaNivel(8);
        assertTrue(partida.puedeRecuperarComodin());
    }

    @Test
    void noPuedeRecuperarComodinEnNivel7() {
        avanzarHastaNivel(7);
        assertFalse(partida.puedeRecuperarComodin());
    }

    // ---------- Ganar partida ----------

    @Test
    void ganarPartidaAlResponder15Correctas() {
        avanzarHastaNivel(15);
        assertTrue(partida.comprobarRespuesta("B"));
        assertTrue(partida.isPartidaTerminada());
        assertEquals(1000000, partida.getDineroAcumulado());
    }

    // ---------- Métodos auxiliares para tests ----------

    private void avanzarHastaNivel(int nivelObjetivo) {
        while (partida.getNivelActual() < nivelObjetivo && !partida.isPartidaTerminada()) {
            partida.comprobarRespuesta("B");
        }
    }

    // =========== Stubs internos ===========

    static class StubGestorPreguntas extends GestorPreguntas {
        private final Pregunta preguntaPorDefecto;
        Pregunta preguntaParaMago;

        StubGestorPreguntas(Pregunta preguntaPorDefecto) {
            this.preguntaPorDefecto = preguntaPorDefecto;
        }

        @Override
        public Pregunta obtenerPreguntaAleatoria(int nivelBuscado) {
            return new Pregunta(
                preguntaPorDefecto.getPregunta(),
                preguntaPorDefecto.getOpcionA(),
                preguntaPorDefecto.getOpcionB(),
                preguntaPorDefecto.getOpcionC(),
                preguntaPorDefecto.getOpcionD(),
                preguntaPorDefecto.getCorrecta(),
                nivelBuscado,
                preguntaPorDefecto.getPista(),
                preguntaPorDefecto.getCategoria()
            );
        }

        @Override
        public Pregunta obtenerPreguntaAleatoriaPorNivelYCategoria(int nivel, String categoria) {
            return preguntaParaMago;
        }
    }

    static class StubGestorPuntuaciones extends GestorPuntuaciones {
        boolean puntuacionGuardada = false;
        Puntuacion ultimaPuntuacion;
        int vecesGuardada = 0;

        @Override
        public void guardarPuntuacion(Puntuacion puntuacion) {
            puntuacionGuardada = true;
            ultimaPuntuacion = puntuacion;
            vecesGuardada++;
        }
    }
}
