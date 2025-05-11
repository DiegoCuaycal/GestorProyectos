package com.diegocuaycal.gestorproyectos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    // Atributos
    public static final String DB_NAME = "GestorProyectos.db";
    public static final int DB_VERSION = 1;
    // Constructor
    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    // Metodo para crear las tablas
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Tabla Usuarios
        db.execSQL("CREATE TABLE Usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "usuario TEXT NOT NULL UNIQUE," +
                "contrasena TEXT NOT NULL)");

        // Tabla Proyectos
        db.execSQL("CREATE TABLE Proyectos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_usuario INTEGER NOT NULL," +
                "nombre TEXT NOT NULL," +
                "descripcion TEXT," +
                "fecha_inicio TEXT," +
                "fecha_fin TEXT," +
                "FOREIGN KEY (id_usuario) REFERENCES Usuarios(id))");

        // Tabla Actividades
        db.execSQL("CREATE TABLE Actividades (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_proyecto INTEGER NOT NULL," +
                "nombre TEXT NOT NULL," +
                "descripcion TEXT," +
                "fecha_inicio TEXT," +
                "fecha_fin TEXT," +
                "estado TEXT CHECK(estado IN ('Planificado','En ejecución','Realizado'))," +
                "FOREIGN KEY (id_proyecto) REFERENCES Proyectos(id))");
    }
    // Metodo para cambiar la version
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Actividades");
        db.execSQL("DROP TABLE IF EXISTS Proyectos");
        db.execSQL("DROP TABLE IF EXISTS Usuarios");
        onCreate(db);
    }
}

