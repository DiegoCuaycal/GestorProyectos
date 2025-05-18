package com.diegocuaycal.gestorproyectos;

public class Actividad {
    private int id;
    private int idProyecto;
    private String nombre;
    private String descripcion;
    private String fechaInicio;
    private String fechaFin;
    private String estado;

    public Actividad(int id, int idProyecto, String nombre, String descripcion,
                     String fechaInicio, String fechaFin, String estado) {
        this.id = id;
        this.idProyecto = idProyecto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
    }

    // Getters (puedes agregar setters si deseas)
    public int getId() { return id; }
    public int getIdProyecto() { return idProyecto; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getFechaInicio() { return fechaInicio; }
    public String getFechaFin() { return fechaFin; }
    public String getEstado() { return estado; }
}

