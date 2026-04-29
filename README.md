![El Concursillo Banner](Concursillo/src/assets/banner.png)

Este proyecto es una aplicación desarrollada en Java que simula un juego de preguntas y respuestas inspirado en el famoso formato televisivo *"¿Quién quiere ser millonario?"*, pero adaptado y personalizado al estilo del streamer **IlloJuan**.

El desarrollo forma parte de un **Proyecto Académico** para poner en práctica los conocimientos adquiridos en las asignaturas de Programación, Base de DatosEntornos de Desarrollo en la EFA Moratalaz.

---

## 🚀 Características del Juego

El objetivo es responder correctamente a una serie de preguntas de opción múltiple (cuatro opciones) con una dificultad creciente. 

*   **Temáticas Personalizadas:** Al inicio de la partida, el jugador puede elegir temáticas adaptadas al entorno del streaming (Videojuegos, Deportes, Política, etc.).
*   **Sistema de Riesgo y Recompensa:** Cada acierto acumula puntuación/dinero. Fallar supone perder lo acumulado (salvo en los tramos de premios garantizados).
*   **Opción de Plantarse:** El jugador puede decidir no responder y llevarse el premio acumulado hasta ese momento.

### 🃏 Comodines Disponibles
Durante el juego, la interfaz mostrará visualmente los comodines disponibles:
1.  **50:50:** Elimina dos de las opciones incorrectas.
2.  **Comodín del Chat:** Los espectadores del programa votan la respuesta correcta (sustituye al comodín del público).
3.  **Comodín de la Llamada:** 30 segundos para llamar a una persona y obtener la respuesta.
4.  **Comodín del Sacrificio:** Un espectador responde por el jugador; si falla, recibe un castigo.
5.  **Comodín de la Ruleta:** Se lanza una ruleta que puede otorgar de 0 a 3 comodines extra, o incluso aplicar penalizaciones.
6.  **Comodín del Mago:** Disponible a partir de cierto nivel, permite cambiar la pregunta por otra de la misma temática y dificultad.

---

## 🛠️ Tecnologías y Herramientas Usadas

El proyecto está construido utilizando las siguientes tecnologías (puedes hacer clic en ellas para más información):

*   **[Java](https://www.java.com/es/):** Lenguaje de programación principal utilizado para la lógica de la aplicación y la interfaz gráfica.
*   **[MongoDB](https://www.mongodb.com/es):** Sistema de base de datos NoSQL orientado a documentos, utilizado por su flexibilidad y rapidez para almacenar las preguntas y el ranking de jugadores.
*   **[Git](https://git-scm.com/) y [GitHub](https://github.com/):** Control de versiones para el trabajo colaborativo en equipo y alojamiento del repositorio.
*   **[Eclipse IDE](https://www.eclipse.org/ide/) / [Visual Studio Code](https://code.visualstudio.com/):** Entornos de desarrollo integrados para la escritura y depuración del código.

---

## 📂 Estructura del Proyecto

Actualmente, el código fuente está organizado siguiendo el patrón de diseño Modelo-Vista-Controlador (MVC) y gestión de acceso a datos:

*   **`modelo/`**: Contiene las entidades principales del juego.
    *   `Pregunta.java`: Define la estructura de las preguntas (enunciado, opciones, respuesta correcta, nivel, categoría, etc.).
    *   `Puntuacion.java`: Define la estructura para guardar los resultados de las partidas.
*   **`basedatos/`**: Gestiona la conexión y las consultas a la base de datos MongoDB.
    *   `ConexionMongo.java`: Patrón Singleton para gestionar la conexión a la base de datos `millonarioDB`.
    *   `GestorPreguntas.java`: Se encarga de extraer preguntas aleatorias de la base de datos por nivel de dificultad, evitando repeticiones.
    *   `GestorPuntuaciones.java`: Permite guardar las puntuaciones y obtener el ranking ordenado de jugadores.
*   **`test/`**:
    *   `testMongo.java`: Clase de pruebas para verificar la correcta extracción de preguntas aleatorias desde la base de datos.

---

## ⚙️ Instalación y Configuración

1.  **Clonar el repositorio:**
    ```bash
    git clone [https://github.com/tu-usuario/ProyectoIntermodular_Concursillo.git](https://github.com/tu-usuario/ProyectoIntermodular_Concursillo.git)
    ```
2.  **Configurar la Base de Datos:**
    *   Asegúrate de tener MongoDB instalado y ejecutándose en tu máquina local (`mongodb://localhost:27017`).
    *   La base de datos debe llamarse `millonarioDB`.
    *   Se requiere tener la colección `preguntas` poblada con documentos que contengan los campos: `pregunta`, `opcionA`, `opcionB`, `opcionC`, `opcionD`, `correcta`, `nivel`, `pista` y `categoria`.
3.  **Ejecutar el proyecto:**
    *   Abre el proyecto en tu IDE favorito y ejecuta la clase principal (interfaz gráfica pendiente de integración).

---

## 👥 Equipo y Colaboradores

*   **Alumnos:** [Cristopher, Leandro, Paula, Gaspar, Yael]
*   **Profesores Evaluadores:**
    *   Yolanda Moreno (@YMorenoG) - Programación / Base de Datos
    *   David García (@DavidGarciaEFA) - Entornos de Desarrollo
    *   Jesús Santiago Rico (@JesusEFA) - Digitalización / Proyecto Intermodular
"""
