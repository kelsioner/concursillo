import java.awt.EventQueue;

import Interfaz.PantallaPrin;

public class PrincipalApp {

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PantallaPrin ventana = new PantallaPrin();
					ventana.setVisible(true);
				} catch (Exception e) {
					System.err.println("Error al iniciar la aplicación: " + e.getMessage());
				}
			}
		});
	}
}