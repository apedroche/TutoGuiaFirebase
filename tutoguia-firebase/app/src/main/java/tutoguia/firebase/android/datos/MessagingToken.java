package tutoguia.firebase.android.datos;

/**
 * Definición del tipo de datos "MessagingToken"
 * Se utiliza para guardar el token necesario para enviar mensajes Cloud Messaging
 */
public class MessagingToken {

    // Datos
    private String id, name, token;

    /**
     * @return Identificador del dispositivo
     */
    public String getId() {
        return id;
    }

    /**
     * @param id Identificador del dispositivo
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return Nombre del usuario
     */
    public String getName() {
        return name;
    }

    /**
     * @param name Nombre del usuario
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Token para enviar mensajes
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token Token para enviar mensajes
     */
    public void setToken(String token) {
        this.token = token;
    }
}
