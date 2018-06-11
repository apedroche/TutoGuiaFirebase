package tutoguia.firebase.android.datos;

/**
 * Definición del tipo de datos "Asignatura"
 */
public class Asignatura {

    // Datos
    private String name, teacher;

    /**
     * @return Nombre de la asignatura
     */
    public String getName() {
        return name;
    }

    /**
     * @param name Nombre de la asignatura
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Profesor de la asignatura
     */
    public String getTeacher() {
        return teacher;
    }

    /**
     * @param teacher Profesor de la asignatura
     */
    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
}
