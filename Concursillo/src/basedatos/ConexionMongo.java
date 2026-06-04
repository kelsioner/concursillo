package basedatos;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class ConexionMongo {

    private static MongoClient cliente;
    private static MongoDatabase baseDatos;

    public static MongoDatabase conectar() {

        if (cliente == null) {

            String uri = System.getenv("MONGO_URI");

            if (uri == null || uri.isEmpty()) {
                throw new IllegalStateException(
                    "La variable de entorno MONGO_URI no está configurada. "
                    + "Define MONGO_URI con la URI de conexión a MongoDB Atlas."
                );
            }

            cliente = MongoClients.create(uri);

            baseDatos = cliente.getDatabase("millonarioDB");
        }

        return baseDatos;
    }

    public static void cerrar() {

        if (cliente != null) {
            cliente.close();
            cliente = null;
            baseDatos = null;
        }
    }
}