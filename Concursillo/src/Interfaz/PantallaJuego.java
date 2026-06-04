package Interfaz;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import controlador.Partida;
import modelo.Pregunta;

import basedatos.GestorPuntuaciones;
import modelo.Puntuacion;

public class PantallaJuego extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;

	private Partida partida;

	private JButton btnPreguntas;
	private JButton btnRespuestaA;
	private JButton btnRespuestaB;
	private JButton btnRespuestaC;
	private JButton btnRespuestaD;

	private JLabel lblInfo;

	private ArrayList<String> opcionesEliminadas = new ArrayList<String>();

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PantallaJuego frame = new PantallaJuego();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public PantallaJuego() {
		this("Jugador");
	}

	public PantallaJuego(String nombreJugador) {

		try {
			this.partida = new Partida(nombreJugador);
		} catch (Exception e) {
			System.err.println("Error al iniciar la partida: " + e.getMessage());
			JOptionPane.showMessageDialog(null,
					"No se ha podido iniciar la partida.\nRevisa que MongoDB est\u00e9 funcionando.",
					"Error",
					JOptionPane.ERROR_MESSAGE);
			EventQueue.invokeLater(() -> {
				PantallaPrin menu = new PantallaPrin();
				menu.setVisible(true);
				dispose();
			});
			return;
		}

		URL iconoVentana = getClass().getResource("/assets/Logo Grande.png");

		if (iconoVentana != null) {
			setIconImage(Toolkit.getDefaultToolkit().getImage(iconoVentana));
		} else {
			System.out.println("No se encontró el icono de la ventana.");
		}

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 846, 555);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// --- BOTONES DE COMODINES Y UTILIDADES ---

		JButton btnVerDinero = new JButton("");
		btnVerDinero.setIcon(cargarIcono("/assets/Ver dinero.png"));
		configurarBotonTransparente(btnVerDinero);
		btnVerDinero.setBounds(10, 0, 93, 77);
		contentPane.add(btnVerDinero);

		btnVerDinero.addActionListener(e -> {
			Dinero ventanaDinero = new Dinero(
					partida.getNombreJugador(),
					partida.getNivelActual(),
					partida.getDineroAcumulado()
			);
			ventanaDinero.setVisible(true);
		});

		JButton btnSalir = new JButton("");
		btnSalir.setIcon(cargarIcono("/assets/Salir.png"));
		configurarBotonTransparente(btnSalir);
		btnSalir.setBounds(97, 0, 93, 70);
		contentPane.add(btnSalir);

		btnSalir.addActionListener(e -> plantarse());

		lblInfo = new JLabel("", SwingConstants.CENTER);
		lblInfo.setForeground(Color.WHITE);
		lblInfo.setFont(new Font("Arial", Font.BOLD, 14));
		lblInfo.setBounds(210, 10, 420, 30);
		contentPane.add(lblInfo);

		JButton btnComodin50 = new JButton("");
		btnComodin50.setIcon(cargarIcono("/assets/Comodin 50-50.png"));
		configurarBotonTransparente(btnComodin50);
		btnComodin50.setBounds(544, 0, 93, 70);
		contentPane.add(btnComodin50);

		btnComodin50.addActionListener(e -> {

			String[] eliminadas = partida.usarComodin5050(opcionesEliminadas);

			if (eliminadas.length == 0) {
				mostrarVentanaComodin(
						"Comodín 50:50",
						"Este comodín ya no está disponible."
				);
				return;
			}

			StringBuilder mensaje = new StringBuilder();
			mensaje.append("El comodín 50:50 ha eliminado dos respuestas incorrectas.\n\n");
			mensaje.append("Opciones eliminadas:\n");

			for (String opcion : eliminadas) {
				opcionesEliminadas.add(opcion);
				ocultarRespuesta(opcion);
				mensaje.append("- Opción ").append(opcion).append("\n");
			}

			btnComodin50.setEnabled(false);

			mostrarVentanaComodin("Comodín 50:50", mensaje.toString());
		});

		JButton btnComodinPublico = new JButton("");
		btnComodinPublico.setIcon(cargarIcono("/assets/Comodin Publico.png"));
		configurarBotonTransparente(btnComodinPublico);
		btnComodinPublico.setBounds(636, 10, 93, 56);
		contentPane.add(btnComodinPublico);

		btnComodinPublico.addActionListener(e -> {

			int[] porcentajes = partida.usarComodinChat();

			if (porcentajes.length == 0) {
				mostrarVentanaComodin(
						"Comodín del público",
						"Este comodín ya no está disponible."
				);
				return;
			}

			String mensaje = "El público ha votado lo siguiente:\n\n"
					+ "A: " + porcentajes[0] + "%\n"
					+ "B: " + porcentajes[1] + "%\n"
					+ "C: " + porcentajes[2] + "%\n"
					+ "D: " + porcentajes[3] + "%\n\n"
					+ "Piensa bien tu respuesta antes de elegir.";

			mostrarVentanaComodin("Comodín del público", mensaje);

			btnComodinPublico.setEnabled(false);
		});

		JButton btnComodinLlamada = new JButton("");
		btnComodinLlamada.setIcon(cargarIcono("/assets/Comodin Llamada.png"));
		configurarBotonTransparente(btnComodinLlamada);
		btnComodinLlamada.setBounds(729, 10, 93, 56);
		contentPane.add(btnComodinLlamada);

		btnComodinLlamada.addActionListener(e -> {

			String mensaje = partida.usarComodinLlamada();

			mostrarVentanaComodin(
					"Comodín de la llamada",
					"Estás llamando a tu contacto...\n\n" + mensaje
			);

			btnComodinLlamada.setEnabled(false);
		});

		// --- LOGO Y CAJA DE PREGUNTAS ---

		JLabel lblLogoMedio = new JLabel("");
		lblLogoMedio.setIcon(cargarIcono("/assets/Logo Pequeño.png"));
		lblLogoMedio.setBounds(291, 41, 250, 233);
		contentPane.add(lblLogoMedio);

		btnPreguntas = new JButton("");
		btnPreguntas.setIcon(cargarIcono("/assets/Caja Preguntas.png"));
		configurarBotonConTexto(btnPreguntas, 15);
		btnPreguntas.setBounds(71, 296, 700, 89);
		contentPane.add(btnPreguntas);

		// --- BOTONES DE RESPUESTAS ---

		btnRespuestaA = new JButton("");
		btnRespuestaA.setIcon(cargarIcono("/assets/Caja Respuestas.png"));
		configurarBotonConTexto(btnRespuestaA, 13);
		btnRespuestaA.setBounds(24, 395, 373, 56);
		btnRespuestaA.addActionListener(e -> responder("A"));
		contentPane.add(btnRespuestaA);

		btnRespuestaB = new JButton("");
		btnRespuestaB.setIcon(cargarIcono("/assets/Caja Respuestas.png"));
		configurarBotonConTexto(btnRespuestaB, 13);
		btnRespuestaB.setBounds(427, 395, 373, 56);
		btnRespuestaB.addActionListener(e -> responder("B"));
		contentPane.add(btnRespuestaB);

		btnRespuestaC = new JButton("");
		btnRespuestaC.setIcon(cargarIcono("/assets/Caja Respuestas.png"));
		configurarBotonConTexto(btnRespuestaC, 13);
		btnRespuestaC.setBounds(24, 452, 373, 56);
		btnRespuestaC.addActionListener(e -> responder("C"));
		contentPane.add(btnRespuestaC);

		btnRespuestaD = new JButton("");
		btnRespuestaD.setIcon(cargarIcono("/assets/Caja Respuestas.png"));
		configurarBotonConTexto(btnRespuestaD, 13);
		btnRespuestaD.setBounds(427, 452, 373, 56);
		btnRespuestaD.addActionListener(e -> responder("D"));
		contentPane.add(btnRespuestaD);

		// --- LÍNEAS DE DISEÑO ---

		JLabel lblLinea1 = new JLabel("");
		lblLinea1.setIcon(cargarIcono("/assets/Lineas Gorda.png"));
		lblLinea1.setBounds(0, 330, 93, 28);
		contentPane.add(lblLinea1);

		JLabel lblLinea2 = new JLabel("");
		lblLinea2.setIcon(cargarIcono("/assets/Lineas Fina.png"));
		lblLinea2.setBounds(0, 418, 76, 12);
		contentPane.add(lblLinea2);

		JLabel lblLinea3 = new JLabel("");
		lblLinea3.setIcon(cargarIcono("/assets/Lineas Gorda.png"));
		lblLinea3.setBounds(755, 330, 93, 28);
		contentPane.add(lblLinea3);

		JLabel lblLinea4 = new JLabel("");
		lblLinea4.setIcon(cargarIcono("/assets/Lineas Fina.png"));
		lblLinea4.setBounds(-19, 476, 76, 12);
		contentPane.add(lblLinea4);

		JLabel lblLinea5 = new JLabel("");
		lblLinea5.setIcon(cargarIcono("/assets/Lineas Fina.png"));
		lblLinea5.setBounds(789, 476, 76, 12);
		contentPane.add(lblLinea5);

		JLabel lblLinea6 = new JLabel("");
		lblLinea6.setIcon(cargarIcono("/assets/Lineas Fina.png"));
		lblLinea6.setBounds(789, 418, 76, 12);
		contentPane.add(lblLinea6);

		JLabel lblLinea7 = new JLabel("");
		lblLinea7.setIcon(cargarIcono("/assets/Lineas Fina.png"));
		lblLinea7.setBounds(387, 418, 76, 12);
		contentPane.add(lblLinea7);

		JLabel lblLinea8 = new JLabel("");
		lblLinea8.setIcon(cargarIcono("/assets/Lineas Fina.png"));
		lblLinea8.setBounds(387, 476, 76, 12);
		contentPane.add(lblLinea8);

		// --- FONDO ---

		JLabel lblFondo = new JLabel("");
		lblFondo.setIcon(cargarIcono("/assets/Fondo .jpg"));
		lblFondo.setBounds(0, 0, 832, 518);
		contentPane.add(lblFondo);

		cargarPreguntaEnPantalla();
	}

	private void configurarBotonTransparente(JButton boton) {

		boton.setOpaque(false);
		boton.setFocusPainted(false);
		boton.setContentAreaFilled(false);
		boton.setBorderPainted(false);
	}

	private void configurarBotonConTexto(JButton boton, int tamanoFuente) {

		boton.setForeground(Color.WHITE);
		boton.setFont(new Font("Arial", Font.BOLD, tamanoFuente));
		boton.setHorizontalTextPosition(SwingConstants.CENTER);
		boton.setVerticalTextPosition(SwingConstants.CENTER);
		boton.setHorizontalAlignment(SwingConstants.CENTER);
		boton.setVerticalAlignment(SwingConstants.CENTER);
		boton.setOpaque(false);
		boton.setFocusPainted(false);
		boton.setContentAreaFilled(false);
		boton.setBorderPainted(false);
	}

	private void cargarPreguntaEnPantalla() {

		Pregunta pregunta = partida.getPreguntaActual();

		restaurarRespuestas();

		if (pregunta == null) {
			JOptionPane.showMessageDialog(this, "No se ha podido cargar ninguna pregunta.");
			volverAlMenu();
			return;
		}

		btnPreguntas.setText("<html><div style='text-align:center; width:600px;'>"
				+ pregunta.getPregunta()
				+ "</div></html>");

		btnRespuestaA.setText("<html><div style='text-align:center; width:280px;'>A: "
				+ pregunta.getOpcionA()
				+ "</div></html>");

		btnRespuestaB.setText("<html><div style='text-align:center; width:280px;'>B: "
				+ pregunta.getOpcionB()
				+ "</div></html>");

		btnRespuestaC.setText("<html><div style='text-align:center; width:280px;'>C: "
				+ pregunta.getOpcionC()
				+ "</div></html>");

		btnRespuestaD.setText("<html><div style='text-align:center; width:280px;'>D: "
				+ pregunta.getOpcionD()
				+ "</div></html>");

		actualizarInfo();
	}

	private void responder(String opcion) {

		if (opcionesEliminadas.contains(opcion)) {
			JOptionPane.showMessageDialog(this, "Esa respuesta ha sido eliminada por un comodín.");
			return;
		}

		Pregunta preguntaAnterior = partida.getPreguntaActual();

		if (preguntaAnterior == null) {
			JOptionPane.showMessageDialog(this, "No hay pregunta cargada.");
			return;
		}

		boolean acierto = partida.comprobarRespuesta(opcion);

		if (acierto) {

			if (partida.isPartidaTerminada()) {
				mostrarVentanaResultado(
						"¡VICTORIA!",
						"Has completado el juego.\n\n"
								+ "Dinero final: " + formatearDinero(partida.getDineroAcumulado()),
						true
				);
				volverAlMenu();

			} else {
				mostrarVentanaResultado(
						"¡CORRECTO!",
						"Has acertado la pregunta.\n\n"
								+ "Subes al nivel " + partida.getNivelActual() + ".\n"
								+ "Dinero acumulado: " + formatearDinero(partida.getDineroAcumulado()),
						true
				);

				cargarPreguntaEnPantalla();
			}

		} else {

			mostrarVentanaResultado(
					"INCORRECTO",
					"Has fallado la pregunta.\n\n"
							+ "La respuesta correcta era: " + preguntaAnterior.getCorrecta() + ".\n"
							+ "Dinero final: " + formatearDinero(partida.getDineroAcumulado()),
					false
			);

			volverAlMenu();
		}
	}

	private void actualizarInfo() {

		lblInfo.setText(
				"Nivel: " + partida.getNivelActual()
						+ " | Dinero: " + formatearDinero(partida.getDineroAcumulado())
		);
	}

	private void volverAlMenu() {

		PantallaPrin menu = new PantallaPrin();
		menu.setVisible(true);
		dispose();
	}

	private void ocultarRespuesta(String opcion) {

		if (opcion.equalsIgnoreCase("A")) {
			btnRespuestaA.setText("");
			btnRespuestaA.setEnabled(false);

		} else if (opcion.equalsIgnoreCase("B")) {
			btnRespuestaB.setText("");
			btnRespuestaB.setEnabled(false);

		} else if (opcion.equalsIgnoreCase("C")) {
			btnRespuestaC.setText("");
			btnRespuestaC.setEnabled(false);

		} else if (opcion.equalsIgnoreCase("D")) {
			btnRespuestaD.setText("");
			btnRespuestaD.setEnabled(false);
		}
	}

	private void restaurarRespuestas() {

		opcionesEliminadas.clear();

		btnRespuestaA.setEnabled(true);
		btnRespuestaB.setEnabled(true);
		btnRespuestaC.setEnabled(true);
		btnRespuestaD.setEnabled(true);
	}

	private String formatearDinero(int cantidad) {
		return String.format("%,d €", cantidad).replace(",", ".");
	}

	private ImageIcon cargarIcono(String ruta) {

		URL url = getClass().getResource(ruta);

		if (url == null) {
			System.out.println("No se encontró la imagen: " + ruta);
			return null;
		}

		return new ImageIcon(url);
	}

	private void mostrarVentanaComodin(String titulo, String mensaje) {
		JDialog dialogo = new JDialog(this, titulo, true);
		dialogo.setSize(470, 330);
		dialogo.setLocationRelativeTo(this);
		dialogo.setResizable(false);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBackground(new Color(5, 15, 55));
		dialogo.setContentPane(panel);

		JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
		lblTitulo.setForeground(new Color(255, 220, 80));
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 23));
		lblTitulo.setBounds(20, 20, 420, 35);
		panel.add(lblTitulo);

		JTextArea areaTexto = new JTextArea(mensaje);
		areaTexto.setEditable(false);
		areaTexto.setLineWrap(true);
		areaTexto.setWrapStyleWord(true);
		areaTexto.setFont(new Font("Arial", Font.BOLD, 15));
		areaTexto.setForeground(Color.WHITE);
		areaTexto.setBackground(new Color(8, 25, 80));
		areaTexto.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		JScrollPane scroll = new JScrollPane(areaTexto);
		scroll.setBounds(35, 75, 385, 150);
		scroll.setBorder(BorderFactory.createLineBorder(new Color(255, 220, 80), 2));
		panel.add(scroll);

		JButton btnCerrar = crearBotonDialogo("Cerrar");
		btnCerrar.setBounds(175, 245, 110, 35);
		btnCerrar.addActionListener(e -> dialogo.dispose());
		panel.add(btnCerrar);

		dialogo.getRootPane().setDefaultButton(btnCerrar);
		dialogo.setVisible(true);
	}

	private JButton crearBotonDialogo(String texto) {
		JButton boton = new JButton(texto);
		boton.setFont(new Font("Arial", Font.BOLD, 13));
		boton.setForeground(Color.WHITE);
		boton.setBackground(new Color(0, 70, 150));
		boton.setFocusPainted(false);
		boton.setBorder(BorderFactory.createLineBorder(new Color(255, 220, 80), 1));
		return boton;
	}

	private void mostrarVentanaResultado(String titulo, String mensaje, boolean acierto) {
		JDialog dialogo = new JDialog(this, titulo, true);
		dialogo.setSize(470, 310);
		dialogo.setLocationRelativeTo(this);
		dialogo.setResizable(false);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBackground(new Color(5, 15, 55));
		dialogo.setContentPane(panel);

		JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);

		if (acierto) {
			lblTitulo.setForeground(new Color(80, 255, 120));
		} else {
			lblTitulo.setForeground(new Color(255, 90, 90));
		}

		lblTitulo.setFont(new Font("Arial", Font.BOLD, 26));
		lblTitulo.setBounds(20, 20, 420, 40);
		panel.add(lblTitulo);

		JTextArea areaTexto = new JTextArea(mensaje);
		areaTexto.setEditable(false);
		areaTexto.setLineWrap(true);
		areaTexto.setWrapStyleWord(true);
		areaTexto.setFont(new Font("Arial", Font.BOLD, 15));
		areaTexto.setForeground(Color.WHITE);
		areaTexto.setBackground(new Color(8, 25, 80));
		areaTexto.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		JScrollPane scroll = new JScrollPane(areaTexto);
		scroll.setBounds(35, 75, 385, 120);
		scroll.setBorder(BorderFactory.createLineBorder(new Color(255, 220, 80), 2));
		panel.add(scroll);

		JButton btnCerrar = crearBotonDialogo("Continuar");
		btnCerrar.setBounds(175, 215, 120, 35);
		btnCerrar.addActionListener(e -> dialogo.dispose());
		panel.add(btnCerrar);

		dialogo.getRootPane().setDefaultButton(btnCerrar);
		dialogo.setVisible(true);
	}

	private void plantarse() {

		boolean confirmar = mostrarConfirmacionPlantarse();

		if (!confirmar) {
			return;
		}

		partida.plantarse();

		String mensajeRanking = obtenerMensajeRankingTrasPlantarse();

		mostrarVentanaResultado(
				"TE HAS PLANTADO",
				"Has decidido plantarte.\n\n"
						+ "Dinero acumulado: " + formatearDinero(partida.getDineroAcumulado()) + "\n\n"
						+ mensajeRanking,
				true
		);

		volverAlMenu();
	}

	private String obtenerMensajeRankingTrasPlantarse() {

		try {
			GestorPuntuaciones gestor = new GestorPuntuaciones();
			ArrayList<Puntuacion> ranking = gestor.obtenerRanking();

			int posicion = obtenerPosicionEnRanking(ranking);

			if (posicion == -1) {
				return "Tu puntuación se ha guardado correctamente.";
			}

			if (posicion <= 5) {
				return "¡Enhorabuena! Has entrado en el TOP 5 del ranking.\n"
						+ "Posición actual: " + posicion + ".";
			}

			return "Tu puntuación se ha guardado correctamente.\n"
					+ "Posición actual en el ranking: " + posicion + ".";

		} catch (Exception e) {
			System.err.println("Error al comprobar el ranking: " + e.getMessage());
			return "Tu partida ha terminado, pero no se ha podido comprobar el ranking.";
		}
	}

	private int obtenerPosicionEnRanking(ArrayList<Puntuacion> ranking) {

		String nombreJugador = partida.getNombreJugador();
		int dineroJugador = partida.getDineroAcumulado();

		for (int i = 0; i < ranking.size(); i++) {
			Puntuacion p = ranking.get(i);

			if (p.getNombre().equalsIgnoreCase(nombreJugador)
					&& p.getPuntos() == dineroJugador) {
				return i + 1;
			}
		}

		return -1;
	}

	private boolean mostrarConfirmacionPlantarse() {

		final boolean[] confirmado = { false };

		JDialog dialogo = new JDialog(this, "Plantarse", true);
		dialogo.setSize(470, 300);
		dialogo.setLocationRelativeTo(this);
		dialogo.setResizable(false);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBackground(new Color(5, 15, 55));
		dialogo.setContentPane(panel);

		JLabel lblTitulo = new JLabel("¿TE QUIERES PLANTAR?", SwingConstants.CENTER);
		lblTitulo.setForeground(new Color(255, 220, 80));
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
		lblTitulo.setBounds(20, 20, 420, 35);
		panel.add(lblTitulo);

		JTextArea areaTexto = new JTextArea(
				"Si te plantas ahora, terminarás la partida y conservarás tu dinero acumulado.\n\n"
						+ "Dinero actual: " + formatearDinero(partida.getDineroAcumulado()) + "\n\n"
						+ "¿Seguro que quieres plantarte?"
		);
		areaTexto.setEditable(false);
		areaTexto.setLineWrap(true);
		areaTexto.setWrapStyleWord(true);
		areaTexto.setFont(new Font("Arial", Font.BOLD, 15));
		areaTexto.setForeground(Color.WHITE);
		areaTexto.setBackground(new Color(8, 25, 80));
		areaTexto.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		JScrollPane scroll = new JScrollPane(areaTexto);
		scroll.setBounds(35, 70, 385, 120);
		scroll.setBorder(BorderFactory.createLineBorder(new Color(255, 220, 80), 2));
		panel.add(scroll);

		JButton btnPlantarse = crearBotonDialogo("Plantarse");
		btnPlantarse.setBounds(100, 215, 120, 35);
		panel.add(btnPlantarse);

		JButton btnSeguir = crearBotonDialogo("Seguir jugando");
		btnSeguir.setBounds(240, 215, 130, 35);
		panel.add(btnSeguir);

		btnPlantarse.addActionListener(e -> {
			confirmado[0] = true;
			dialogo.dispose();
		});

		btnSeguir.addActionListener(e -> {
			confirmado[0] = false;
			dialogo.dispose();
		});

		dialogo.getRootPane().setDefaultButton(btnSeguir);
		dialogo.setVisible(true);

		return confirmado[0];
	}

}