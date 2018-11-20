/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidad;

/**
 *
 * @author Ema
 */
public class Elemento {
    
    private int idelemento;
    private String nombre;
    private String descripcion;
    private String estado;

    public Elemento() {
        
    }

    /**
     * @return the idelemento
     */
    public int getIdelemento() {
        return idelemento;
    }

    /**
     * @param idelemento the idelemento to set
     */
    public void setIdelemento(int idelemento) {
        this.idelemento = idelemento;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return the estado
     */
    public String getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    
    
}
