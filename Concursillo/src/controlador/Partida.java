package controlador;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import basedatos.GestorPreguntas;
import basedatos.GestorPuntuaciones;
import modelo.Pregunta;
import modelo.Puntuacion;
import util.ConstantesJuego;

@SuppressWarnings("unused")
public class Partida {

	private GestorPreguntas gestorPreguntas;
	private GestorPuntuaciones gestorPuntuaciones;

	private String nombreJugador;
	private int nivelActual; // Pregunta actual: de 1 a 15
	private int dineroAcumulado = 0;
	private Pregunta preguntaActual;
	private int ultimoNumeroRuleta;
	private boolean recuperacionComodinUsada;

	private boolean partidaTerminada;
	private boolean puntuacionGuardada;

	private Random random = new Random();

	private final int[] PREMIOS = ConstantesJuego.PREMIOS;

	// Estado de los comodines
	private boolean comodin5050Usado;
	private boolean comodinPublicoUsado;
	private boolean comodinLlamadaUsado;
	private boolean comodinSacrificioUsado; //No implementado en la interfaz
	private boolean comodinRuletaUsado; //No implementado en la interfaz
	private boolean comodinMagoUsado; //No implementado en la interfaz

	public Partida(String nombreJugador) {
		this.nombreJugador = nombreJugador;
		this.gestorPreguntas = new GestorPreguntas();
		this.gestorPuntuaciones = new GestorPuntuaciones();

		this.nivelActual = 1;
		this.dineroAcumulado = 0;
		this.partidaTerminada = false;
		this.puntuacionGuardada = false;

		this.comodin5050Usado = false;
		this.comodinPublicoUsado = false;
		this.comodinLlamadaUsado = false;
		this.comodinSacrificioUsado = false;
		this.comodinRuletaUsado = false;
		this.comodinMagoUsado = false;
		this.ultimoNumeroRuleta = -1;
		this.recuperacionComodinUsada = false;

		cargarNuevaPregunta();
	}

	private void cargarNuevaPregunta() {
		int nivelDificultad = calcularNivelDificultad();
		this.preguntaActual = gestorPreguntas.obtenerPreguntaAleatoria(nivelDificultad);
		if (this.preguntaActual == null) {
			this.partidaTerminada = true;
		}
	}

	private int calcularNivelDificultad() {
		if (nivelActual <= 5) {
			return 1;
		} else if (nivelActual <= 10) {
			return 2;
		} else {
			return 3;
		}
	}

	public Pregunta getPreguntaActual() {
		return preguntaActual;
	}

	public String getNombreJugador() {
		return nombreJugador;
	}

	public int getNivelActual() {
		return nivelActual;
	}

	public int getDineroAcumulado() {
		return dineroAcumulado;
	}

	public boolean isPartidaTerminada() {
		return partidaTerminada;
	}
	public boolean comprobarRespuesta(String respuestaElegida) {

		if (partidaTerminada || preguntaActual == null) {
			return false;
		}
		if (respuestaElegida == null) {
			return false;
		}
		respuestaElegida = respuestaElegida.toUpperCase();

		if (preguntaActual.getCorrecta().equalsIgnoreCase(respuestaElegida)) {
			dineroAcumulado = PREMIOS[nivelActual];
			if (nivelActual == 15) {
				partidaTerminada = true;
				guardarPuntuacion();
			} else {
				nivelActual++;
				cargarNuevaPregunta();
			}

			return true;

		} else {
			dineroAcumulado = calcularPremioSeguro();
			partidaTerminada = true;
			guardarPuntuacion();

			return false;
		}
	}

	private int calcularPremioSeguro() {
		if (nivelActual > 10) {
			return PREMIOS[10];
		} else if (nivelActual > 5) {
			return PREMIOS[5];
		} else {
			return 0;
		}
	}

	public void plantarse() {
		if (!partidaTerminada) {
			partidaTerminada = true;
			guardarPuntuacion();
		}
	}

	private void guardarPuntuacion() {
		if (!puntuacionGuardada) {
			Puntuacion p = new Puntuacion(nombreJugador, dineroAcumulado);
			gestorPuntuaciones.guardarPuntuacion(p);
			puntuacionGuardada = true;
		}
	}

	// ---------------- COMODIN 50:50 ----------------

	public String[] usarComodin5050(ArrayList<String> opcionesYaEliminadas) {
		if (comodin5050Usado || partidaTerminada || preguntaActual == null) {
			return new String[0];
		}
		comodin5050Usado = true;
		return eliminarOpcionesIncorrectas(opcionesYaEliminadas, 2);
	}

	// ---------------- COMODIN PUBLICO / CHAT ----------------

	public int[] usarComodinPublico() {
		return usarComodinChat();
	}

	public int[] usarComodinChat() {
		if (comodinPublicoUsado || partidaTerminada || preguntaActual == null) {
			return new int[0];
		}
		comodinPublicoUsado = true;
		int[] porcentajes = new int[4];
		String correcta = preguntaActual.getCorrecta().toUpperCase();
		int indiceCorrecta = correcta.charAt(0) - 'A';
		int porcentajeCorrecta = 45 + random.nextInt(31); // De 45 a 75
		int restante = 100 - porcentajeCorrecta;

		ArrayList<Integer> indicesIncorrectos = new ArrayList<Integer>();

		for (int i = 0; i < 4; i++) {
			if (i != indiceCorrecta) {
				indicesIncorrectos.add(i);
			}
		}

		int porcentaje1 = random.nextInt(restante + 1);
		int porcentaje2 = random.nextInt(restante - porcentaje1 + 1);
		int porcentaje3 = restante - porcentaje1 - porcentaje2;

		porcentajes[indiceCorrecta] = porcentajeCorrecta;
		porcentajes[indicesIncorrectos.get(0)] = porcentaje1;
		porcentajes[indicesIncorrectos.get(1)] = porcentaje2;
		porcentajes[indicesIncorrectos.get(2)] = porcentaje3;
		return porcentajes;
	}

	// ---------------- COMODIN LLAMADA ----------------

	public String usarComodinLlamada() {
		if (comodinLlamadaUsado || partidaTerminada || preguntaActual == null) {
			return "Este comodin ya no esta disponible.";
		}
		comodinLlamadaUsado = true;
		String respuestaSugerida;
		if (random.nextInt(100) < 70) {
			respuestaSugerida = preguntaActual.getCorrecta().toUpperCase();
		} else {
			respuestaSugerida = obtenerRespuestaIncorrectaAleatoria();
		}
		return "Tu contacto cree que la respuesta es la " + respuestaSugerida + ": "
				+ obtenerTextoRespuesta(respuestaSugerida);
	}

	// ---------------- COMODIN SACRIFICIO ----------------
	//No implementado en la interfaz

	public String usarComodinSacrificio() {

		if (comodinSacrificioUsado || partidaTerminada || preguntaActual == null) {
			return "";
		}

		comodinSacrificioUsado = true;

		String respuestaSacrificio;

		if (random.nextInt(100) < 75) {
			respuestaSacrificio = preguntaActual.getCorrecta().toUpperCase();
		} else {
			respuestaSacrificio = obtenerRespuestaIncorrectaAleatoria();
		}

		return respuestaSacrificio;
	}

	// ---------------- COMODIN RULETA ----------------
	//No implementado en la interfaz

	public String[] usarComodinRuleta(ArrayList<String> opcionesYaEliminadas) {
		if (comodinRuletaUsado || partidaTerminada || preguntaActual == null) {
			ultimoNumeroRuleta = -1;
			return new String[0];
		}
		comodinRuletaUsado = true;
		ultimoNumeroRuleta = random.nextInt(4);
		return eliminarOpcionesIncorrectas(opcionesYaEliminadas, ultimoNumeroRuleta);
	}

	// ---------------- COMODIN MAGO ----------------
	//No implementado en la interfaz

	public boolean usarComodinMago() {
		if (comodinMagoUsado || partidaTerminada || preguntaActual == null) {
			return false;
		}
		// El mago solo se puede usar a partir de la pregunta 7
		if (nivelActual < 7) {
			return false;
		}
		int nivelDificultad = calcularNivelDificultad();
		String categoriaActual = preguntaActual.getCategoria();
		Pregunta nuevaPregunta = gestorPreguntas.obtenerPreguntaAleatoriaPorNivelYCategoria(
				nivelDificultad,
				categoriaActual);
		if (nuevaPregunta != null) {
			preguntaActual = nuevaPregunta;
			comodinMagoUsado = true;
			return true;
		}
		return false;
	}

	// ---------------- METODOS AUXILIARES ----------------

	private String[] eliminarOpcionesIncorrectas(ArrayList<String> opcionesYaEliminadas, int cantidad) {
		ArrayList<String> incorrectas = obtenerOpcionesIncorrectas();
		if (opcionesYaEliminadas != null) {
			for (String opcion : opcionesYaEliminadas) {
				incorrectas.remove(opcion);
			}
		}
		Collections.shuffle(incorrectas);
		int cantidadEliminar = Math.min(cantidad, incorrectas.size());
		String[] eliminadas = new String[cantidadEliminar];
		for (int i = 0; i < cantidadEliminar; i++) {
			eliminadas[i] = incorrectas.get(i);
		}
		return eliminadas;
	}

	private ArrayList<String> obtenerOpcionesIncorrectas() {
		ArrayList<String> incorrectas = new ArrayList<String>();
		incorrectas.add("A");
		incorrectas.add("B");
		incorrectas.add("C");
		incorrectas.add("D");
		incorrectas.remove(preguntaActual.getCorrecta().toUpperCase());
		return incorrectas;
	}

	private String obtenerRespuestaIncorrectaAleatoria() {
		ArrayList<String> incorrectas = obtenerOpcionesIncorrectas();
		Collections.shuffle(incorrectas);
		return incorrectas.get(0);
	}

	private String obtenerTextoRespuesta(String letra) {
		if (letra.equalsIgnoreCase("A")) {
			return preguntaActual.getOpcionA();
		} else if (letra.equalsIgnoreCase("B")) {
			return preguntaActual.getOpcionB();
		} else if (letra.equalsIgnoreCase("C")) {
			return preguntaActual.getOpcionC();
		} else if (letra.equalsIgnoreCase("D")) {
			return preguntaActual.getOpcionD();
		} else {
			return "";
		}
	}

	private boolean estaComodinUsado(String comodin) {

		if (comodin.equalsIgnoreCase("5050")) {
			return comodin5050Usado;
		} else if (comodin.equalsIgnoreCase("chat") || comodin.equalsIgnoreCase("publico")) {
			return comodinPublicoUsado;
		} else if (comodin.equalsIgnoreCase("llamada")) {
			return comodinLlamadaUsado;
		} else if (comodin.equalsIgnoreCase("sacrificio")) {
			return comodinSacrificioUsado;
		} else if (comodin.equalsIgnoreCase("mago")) {
			return comodinMagoUsado;
		} else if (comodin.equalsIgnoreCase("ruleta")) {
			return comodinRuletaUsado;
		}
		return false;
	}

	// ---------------- RECUPERAR COMODIN ----------------
	//No implementado en la interfaz

	private void recuperarComodin(String comodin) {
		if (comodin.equalsIgnoreCase("5050")) {
			comodin5050Usado = false;
		} else if (comodin.equalsIgnoreCase("chat") || comodin.equalsIgnoreCase("publico")) {
			comodinPublicoUsado = false;
		} else if (comodin.equalsIgnoreCase("llamada")) {
			comodinLlamadaUsado = false;
		} else if (comodin.equalsIgnoreCase("sacrificio")) {
			comodinSacrificioUsado = false;
		} else if (comodin.equalsIgnoreCase("mago")) {
			comodinMagoUsado = false;
		} else if (comodin.equalsIgnoreCase("ruleta")) {
			comodinRuletaUsado = false;
		}
	}

	public boolean recuperarComodinElegido(String comodin) {
		if (comodin == null || comodin.equals("")) {
			return false;
		}
		if (partidaTerminada) {
			return false;
		}
		if (nivelActual < 8) {
			return false;
		}
		if (recuperacionComodinUsada) {
			return false;
		}
		if (!estaComodinUsado(comodin)) {
			return false;
		}
		recuperarComodin(comodin);
		recuperacionComodinUsada = true;
		return true;
	}

	// ---------------- GETTERS DE COMODINES ----------------

	public boolean isComodin5050Usado() {
		return comodin5050Usado;
	}

	public boolean isComodinPublicoUsado() {
		return comodinPublicoUsado;
	}

	public boolean isComodinLlamadaUsado() {
		return comodinLlamadaUsado;
	}

	public boolean isComodinSacrificioUsado() {
		return comodinSacrificioUsado;
	}

	public boolean isComodinRuletaUsado() {
		return comodinRuletaUsado;
	}

	public int getUltimoNumeroRuleta() {
		return ultimoNumeroRuleta;
	}

	public boolean isComodinMagoUsado() {
		return comodinMagoUsado;
	}

	public boolean puedeRecuperarComodin() {
		return nivelActual >= 8 && !recuperacionComodinUsada && !partidaTerminada;
	}

	public boolean isRecuperacionComodinUsada() {
		return recuperacionComodinUsada;
	}

	public boolean puedeUsarComodinMago() {
		return !comodinMagoUsado && nivelActual >= 7 && !partidaTerminada;
	}
}