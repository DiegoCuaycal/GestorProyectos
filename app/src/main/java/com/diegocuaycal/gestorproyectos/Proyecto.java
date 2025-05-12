package com.diegocuaycal.gestorproyectos;

public class Proyecto {
    //Atributos
    private int id;
    private int idUsuario;
    private String nombre;
    private String descripcion;
    private String fechaInicio;
    private String fechaFin;

    //Constructor
    public Proyecto(int id, int idUsuario, String nombre, String descripcion, String fechaInicio, String fechaFin) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    // Getters y setters
    public int getId() { return id; }
    public int getIdUsuario() { return idUsuario; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getFechaInicio() { return fechaInicio; }
    public String getFechaFin() { return fechaFin; }
}
