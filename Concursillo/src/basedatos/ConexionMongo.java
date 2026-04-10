package basedatos;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class ConexionMongo {

    private static MongoClient cliente;
    private static MongoDatabase baseDatos;

    public static MongoDatabase conectar() {
        if (cliente == null) {
            cliente = MongoClients.create("mongodb://localhost:27017");
            baseDatos = cliente.getDatabase("millonarioDB");
        }
        return baseDatos;
    }

    public static void cerrar() {
        if (cliente != null) {
            cliente.close();
        }
    }
}