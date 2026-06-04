package util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

public class UIUtils {

    private UIUtils() {
    }

    public static String formatearDinero(int cantidad) {
        return String.format("%,d €", cantidad).replace(",", ".");
    }

    public static ImageIcon cargarIcono(String ruta, Class<?> contexto) {
        URL url = contexto.getResource(ruta);
        if (url == null) {
            System.out.println("No se encontró la imagen: " + ruta);
            return null;
        }
        return new ImageIcon(url);
    }

    public static void configurarIconoVentana(JFrame ventana, Class<?> contexto) {
        URL iconoVentana = contexto.getResource("/assets/Logo Grande.png");
        if (iconoVentana != null) {
            ventana.setIconImage(Toolkit.getDefaultToolkit().getImage(iconoVentana));
        } else {
            System.out.println("No se encontró el icono de la ventana.");
        }
    }

    public static JButton crearBotonDialogo(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 13));
        boton.setForeground(Color.WHITE);
        boton.setBackground(new Color(0, 70, 150));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createLineBorder(new Color(255, 220, 80), 1));
        return boton;
    }
}
