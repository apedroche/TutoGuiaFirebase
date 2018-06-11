package tutoguia.firebase.android.datos;

import java.util.ArrayList;
import java.util.List;

/**
 * Definición del tipo de datos "Alumno"
 */
public class Alumno {

    // Datos
    private String dni, name, birthCity;
    private int age;
    private List<String> asignaturas;

    /**
     * @return DNI del alumno
     */
    public String getDni() {
        return dni;
    }

    /**
     * @param dni DNI del alumno
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * @return Nombre del alumno
     */
    public String getName() {
        return name;
    }

    /**
     * @param name Nombre del alumno
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Ciudad de nacimiento del alumno
     */
    public String getBirthCity() {
        return birthCity;
    }

    /**
     * @param birthCity Ciudad de nacimiento del alumno
     */
    public void setBirthCity(String birthCity) {
        this.birthCity = birthCity;
    }

    /**
     * @return Edad del alumno
     */
    public int getAge() {
        return age;
    }

    /**
     * @param age Edad del alumno
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * @return Lista de asignaturas a las que está apuntado
     */
    public List<String> getAsignaturas() {
        return asignaturas;
    }

    /**
     * @param asignaturas Lista de asignaturas
     */
    public void setAsignaturas(List<String> asignaturas) {
        this.asignaturas = asignaturas;
    }

    /**
     * @param asignaturaName Nombre de la asignatura que se está buscando
     * @return true si el alumno está apuntado a esa asignatura
     */
    public boolean hasAsignatura(String asignaturaName){
        if(asignaturas==null) return false;
        return asignaturas.contains(asignaturaName);
    }

    /**
     * @param asignaturaName Nombre de la asignatura que se va a añadir
     */
    public void insertAsignatura(String asignaturaName){
        if(asignaturas==null)
            asignaturas = new ArrayList<>();
        asignaturas.add(asignaturaName);
    }

    /**
     * @param asignaturaName Nombre de la asignatura que se va a eliminar
     */
    public void deleteAsignatura(String asignaturaName){
        if(asignaturas==null) return;
        asignaturas.remove(asignaturaName);
    }
}
