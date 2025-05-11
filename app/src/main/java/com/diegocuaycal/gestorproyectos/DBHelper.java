package com.diegocuaycal.gestorproyectos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    // Atributos
    public static final String DB_NAME = "GestorProyectos.db";
    public static final int DB_VERSION = 2;
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
                "nombre TEXT NOT NULL," +
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

    // Metodo para insertar un nuevo usuario
    public boolean insertUser(String nombre, String usuario, String contrasena) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("usuario", usuario);
        values.put("contrasena", contrasena);
        long result = db.insert("Usuarios", null, values);
        return result != -1;
    }


    // Metodo para verificar si el usuario ya existe
    public boolean checkEmailExists(String usuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Usuarios WHERE usuario = ?", new String[]{usuario});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean validarUsuario(String usuario, String contrasena) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Usuarios WHERE usuario = ? AND contrasena = ?", new String[]{usuario, contrasena});
        boolean existe = cursor.getCount() > 0;
        cursor.close();
        return existe;
    }

    public String obtenerContrasena(String usuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT contrasena FROM Usuarios WHERE usuario = ?", new String[]{usuario});
        if (cursor.moveToFirst()) {
            String pass = cursor.getString(0);
            cursor.close();
            return pass;
        } else {
            cursor.close();
            return null;
        }
    }



}

