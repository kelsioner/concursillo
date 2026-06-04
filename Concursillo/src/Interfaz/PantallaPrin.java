package Interfaz;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
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
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import basedatos.GestorPuntuaciones;
import modelo.Puntuacion;

public class PantallaPrin extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PantallaPrin frame = new PantallaPrin();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public PantallaPrin() {

		URL iconoVentana = getClass().getResource("/assets/Logo Grande.png");

		if (iconoVentana != null) {
			setIconImage(Toolkit.getDefaultToolkit().getImage(iconoVentana));
		} else {
			System.out.println("No se encontró el icono de la ventana.");
		}

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 846, 556);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblLogoGrande = new JLabel("");
		lblLogoGrande.setIcon(cargarIcono("/assets/Logo Grande.png"));
		lblLogoGrande.setBounds(262, 35, 300, 320);
		contentPane.add(lblLogoGrande);

		JButton btnJugar = crearBotonMenu("Jugar");
		btnJugar.addActionListener(e -> {

			String nombre = pedirNombreJugador();

			if (nombre != null && !nombre.trim().equals("")) {
				PantallaJuego ventanaJuego = new PantallaJuego(nombre.trim());
				ventanaJuego.setVisible(true);
				dispose();
			}
		});
		btnJugar.setBounds(323, 350, 180, 50);
		contentPane.add(btnJugar);

		JButton btnRanking = crearBotonMenu("Ranking");
		btnRanking.addActionListener(e -> mostrarRanking());
		btnRanking.setBounds(323, 405, 180, 50);
		contentPane.add(btnRanking);

		JButton btnInfo = crearBotonMenu("Información");
		btnInfo.addActionListener(e -> mostrarInformacionJuego());
		btnInfo.setBounds(323, 460, 180, 50);
		contentPane.add(btnInfo);

		JLabel lblPersonaje2 = new JLabel("");
		lblPersonaje2.setIcon(cargarIcono("/assets/Personaje2.png"));
		lblPersonaje2.setBounds(581, 256, 178, 283);
		contentPane.add(lblPersonaje2);

		JLabel lblPersonaje1 = new JLabel("");
		lblPersonaje1.setIcon(cargarIcono("/assets/Personaje1 .png"));
		lblPersonaje1.setBounds(46, 253, 210, 276);
		contentPane.add(lblPersonaje1);

		JLabel lblFondo = new JLabel("");
		lblFondo.setIcon(cargarIcono("/assets/Fondo .jpg"));
		lblFondo.setBounds(0, 0, 841, 539);
		contentPane.add(lblFondo);
	}

	private JButton crearBotonMenu(String texto) {

		JButton boton = new JButton(texto);

		ImageIcon iconoOriginal = cargarIcono("/assets/CAJA AZUL.png");

		if (iconoOriginal == null) {
			iconoOriginal = cargarIcono("/assets/Boton Jugar.png");
		}

		if (iconoOriginal != null) {
			Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(180, 50, Image.SCALE_SMOOTH);
			boton.setIcon(new ImageIcon(imagenEscalada));
		}

		boton.setFont(new Font("Arial", Font.BOLD, 17));
		boton.setForeground(Color.WHITE);
		boton.setHorizontalTextPosition(SwingConstants.CENTER);
		boton.setVerticalTextPosition(SwingConstants.CENTER);

		boton.setOpaque(false);
		boton.setFocusPainted(false);
		boton.setBorderPainted(false);
		boton.setContentAreaFilled(false);
		boton.setBorder(null);

		return boton;
	}

	private void mostrarInformacionJuego() {

		String mensaje = "EL CONCURSILLO\n\n"
				+ "Responde correctamente a las preguntas para subir de nivel\n"
				+ "y acumular más dinero.\n\n"
				+ "Funcionamiento:\n"
				+ "- Cada pregunta tiene 4 posibles respuestas: A, B, C y D.\n"
				+ "- Si aciertas, pasas al siguiente nivel.\n"
				+ "- Si fallas, la partida termina.\n"
				+ "- Puedes consultar tu dinero pulsando el botón del euro.\n\n"
				+ "Comodines:\n"
				+ "- 50:50: elimina dos respuestas incorrectas.\n"
				+ "- Público/Chat: muestra porcentajes de ayuda.\n"
				+ "- Llamada: una persona te sugiere una respuesta.\n\n"
				+ "Objetivo:\n"
				+ "Llegar al último nivel y conseguir el máximo premio.";

		mostrarVentanaTexto("Información del juego", mensaje);
	}

	private void mostrarRanking() {

		try {
			GestorPuntuaciones gestor = new GestorPuntuaciones();
			ArrayList<Puntuacion> ranking = gestor.obtenerRanking();

			if (ranking.isEmpty()) {
				mostrarVentanaTexto("Ranking", "Todavía no hay puntuaciones guardadas.");
				return;
			}

			StringBuilder sb = new StringBuilder();
			sb.append("RANKING DE JUGADORES\n\n");

			int posicion = 1;

			for (Puntuacion p : ranking) {

				sb.append(posicion)
						.append(". ")
						.append(p.getNombre())
						.append(" - ")
						.append(formatearDinero(p.getPuntos()))
						.append("\n");

				posicion++;

				if (posicion > 10) {
					break;
				}
			}

			mostrarVentanaTexto("Ranking", sb.toString());

		} catch (Exception e) {
			System.err.println("Error al cargar el ranking: " + e.getMessage());
			JOptionPane.showMessageDialog(
					this,
					"No se ha podido cargar el ranking.\nRevisa que MongoDB esté funcionando.",
					"Error",
					JOptionPane.ERROR_MESSAGE
			);
		}
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

	private String pedirNombreJugador() {
		JDialog dialogo = new JDialog(this, "Nueva partida", true);
		dialogo.setSize(360, 220);
		dialogo.setLocationRelativeTo(this);
		dialogo.setResizable(false);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBackground(new Color(5, 15, 55));
		dialogo.setContentPane(panel);

		JLabel lblTitulo = new JLabel("EL CONCURSILLO", SwingConstants.CENTER);
		lblTitulo.setForeground(new Color(255, 220, 80));
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
		lblTitulo.setBounds(20, 20, 310, 30);
		panel.add(lblTitulo);

		JLabel lblTexto = new JLabel("Introduce tu nombre:", SwingConstants.CENTER);
		lblTexto.setForeground(Color.WHITE);
		lblTexto.setFont(new Font("Arial", Font.BOLD, 15));
		lblTexto.setBounds(20, 65, 310, 25);
		panel.add(lblTexto);

		JTextField txtNombre = new JTextField();
		txtNombre.setHorizontalAlignment(SwingConstants.CENTER);
		txtNombre.setFont(new Font("Arial", Font.BOLD, 15));
		txtNombre.setBounds(70, 100, 220, 32);
		panel.add(txtNombre);

		final String[] nombre = { null };

		JButton btnAceptar = crearBotonDialogo("Aceptar");
		btnAceptar.setBounds(65, 145, 100, 30);
		panel.add(btnAceptar);

		JButton btnCancelar = crearBotonDialogo("Cancelar");
		btnCancelar.setBounds(185, 145, 100, 30);
		panel.add(btnCancelar);

		btnAceptar.addActionListener(e -> {
			if (!txtNombre.getText().trim().isEmpty()) {
				nombre[0] = txtNombre.getText().trim();
				dialogo.dispose();
			} else {
				JOptionPane.showMessageDialog(dialogo, "Debes introducir un nombre.");
			}
		});

		btnCancelar.addActionListener(e -> dialogo.dispose());

		dialogo.getRootPane().setDefaultButton(btnAceptar);
		dialogo.setVisible(true);

		return nombre[0];
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

	private void mostrarVentanaTexto(String titulo, String texto) {
		JDialog dialogo = new JDialog(this, titulo, true);
		dialogo.setSize(520, 430);
		dialogo.setLocationRelativeTo(this);
		dialogo.setResizable(false);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBackground(new Color(5, 15, 55));
		dialogo.setContentPane(panel);

		JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
		lblTitulo.setForeground(new Color(255, 220, 80));
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
		lblTitulo.setBounds(20, 20, 465, 35);
		panel.add(lblTitulo);

		JTextArea areaTexto = new JTextArea(texto);
		areaTexto.setEditable(false);
		areaTexto.setLineWrap(true);
		areaTexto.setWrapStyleWord(true);
		areaTexto.setFont(new Font("Arial", Font.BOLD, 14));
		areaTexto.setForeground(Color.WHITE);
		areaTexto.setBackground(new Color(8, 25, 80));
		areaTexto.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		JScrollPane scroll = new JScrollPane(areaTexto);
		scroll.setBounds(35, 70, 435, 250);
		scroll.setBorder(BorderFactory.createLineBorder(new Color(255, 220, 80), 2));
		panel.add(scroll);

		JButton btnCerrar = crearBotonDialogo("Cerrar");
		btnCerrar.setBounds(200, 345, 110, 35);
		btnCerrar.addActionListener(e -> dialogo.dispose());
		panel.add(btnCerrar);

		dialogo.getRootPane().setDefaultButton(btnCerrar);
		dialogo.setVisible(true);
	}

}