package basedatos;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class ConexionMongo {

    private static MongoClient cliente;
    private static MongoDatabase baseDatos;

    public static MongoDatabase conectar() {

        if (cliente == null) {

            String uri = "mongodb+srv://app_millonario:app_millonario@concursillo.5owslqy.mongodb.net/?retryWrites=true&w=majority&appName=Concursillo";

            try {
                cliente = MongoClients.create(uri);
                baseDatos = cliente.getDatabase("millonarioDB");
            } catch (Exception e) {
                System.err.println("Error al conectar con MongoDB: " + e.getMessage());
                cliente = null;
                baseDatos = null;
                throw new RuntimeException("No se pudo conectar con la base de datos.", e);
            }
        }

        return baseDatos;
    }

    public static void cerrar() {

        if (cliente != null) {
            try {
                cliente.close();
            } catch (Exception e) {
                System.err.println("Error al cerrar la conexión con MongoDB: " + e.getMessage());
            } finally {
                cliente = null;
                baseDatos = null;
            }
        }
    }
}