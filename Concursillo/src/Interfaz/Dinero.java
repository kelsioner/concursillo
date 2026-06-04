package Interfaz;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import util.ConstantesJuego;
import util.UIUtils;

public class Dinero extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;

	private final int[] premios = ConstantesJuego.PREMIOS;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Dinero frame = new Dinero("Jugador", 1, 0);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Dinero() {
		this("Jugador", 1, 0);
	}

	public Dinero(String nombreJugador, int nivelActual, int dineroAcumulado) {

		UIUtils.configurarIconoVentana(this, getClass());

		setTitle("Dinero acumulado");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 300, 455);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		pintarPremios(nivelActual);
		pintarInformacion(nombreJugador, nivelActual, dineroAcumulado);

		JLabel lblFondo = new JLabel("");
		lblFondo.setIcon(cargarIcono("/assets/Fondo .jpg"));
		lblFondo.setBounds(0, 0, 286, 418);
		contentPane.add(lblFondo);
	}

	private void pintarPremios(int nivelActual) {

		int y = 18;

		for (int i = 15; i >= 1; i--) {

			JLabel lblPremio = new JLabel(i + "     " + formatearDinero(premios[i]));
			lblPremio.setFont(new Font("Arial", Font.BOLD, 12));
			lblPremio.setBounds(35, y, 210, 20);
			lblPremio.setHorizontalAlignment(SwingConstants.LEFT);

			if (i == nivelActual) {
				lblPremio.setOpaque(true);
				lblPremio.setBackground(new Color(255, 150, 0));
				lblPremio.setForeground(Color.WHITE);

			} else if (i == 5 || i == 10 || i == 15) {
				lblPremio.setForeground(new Color(255, 220, 80));

			} else {
				lblPremio.setForeground(Color.WHITE);
			}

			contentPane.add(lblPremio);
			y += 21;
		}
	}

	private void pintarInformacion(String nombreJugador, int nivelActual, int dineroAcumulado) {

		JLabel lblJugador = new JLabel("Jugador: " + nombreJugador, SwingConstants.CENTER);
		lblJugador.setForeground(Color.WHITE);
		lblJugador.setFont(new Font("Arial", Font.BOLD, 12));
		lblJugador.setBounds(20, 340, 245, 20);
		contentPane.add(lblJugador);

		JLabel lblDinero = new JLabel("Dinero: " + formatearDinero(dineroAcumulado), SwingConstants.CENTER);
		lblDinero.setForeground(Color.WHITE);
		lblDinero.setFont(new Font("Arial", Font.BOLD, 12));
		lblDinero.setBounds(20, 363, 245, 20);
		contentPane.add(lblDinero);

		JLabel lblNivel = new JLabel("Nivel actual: " + nivelActual, SwingConstants.CENTER);
		lblNivel.setForeground(Color.WHITE);
		lblNivel.setFont(new Font("Arial", Font.BOLD, 12));
		lblNivel.setBounds(20, 386, 245, 20);
		contentPane.add(lblNivel);
	}

	private String formatearDinero(int cantidad) {
		return UIUtils.formatearDinero(cantidad);
	}

	private ImageIcon cargarIcono(String ruta) {
		return UIUtils.cargarIcono(ruta, getClass());
	}
}