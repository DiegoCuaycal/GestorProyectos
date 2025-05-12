package com.diegocuaycal.gestorproyectos;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ProyectoAdapter.OnProyectoClickListener {

    DBHelper dbHelper;
    RecyclerView recyclerView;
    ProyectoAdapter adapter;
    ArrayList<Proyecto> listaProyectos;
    int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtener el usuario logueado
        String usuario = getIntent().getStringExtra("usuario");
        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Obtener ID del usuario
        Cursor cursor = db.rawQuery("SELECT id FROM Usuarios WHERE usuario = ?", new String[]{usuario});
        if (cursor.moveToFirst()) {
            idUsuario = cursor.getInt(0);
        } else {
            Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        cursor.close();

        // Inicializar RecyclerView
        recyclerView = findViewById(R.id.recyclerProyectos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Cargar proyectos desde SQLite
        listaProyectos = cargarProyectosDesdeDB(idUsuario);

        // Adaptador
        adapter = new ProyectoAdapter(this, listaProyectos, this);
        recyclerView.setAdapter(adapter);

        // Botón para añadir proyecto
        FloatingActionButton fabAddProyecto = findViewById(R.id.fabAddProyecto);
        fabAddProyecto.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CrearProyectoActivity.class);
            intent.putExtra("usuario", usuario);
            startActivity(intent);
        });
    }

    // Método para cargar los proyectos desde la base de datos
    private ArrayList<Proyecto> cargarProyectosDesdeDB(int idUsuario) {
        ArrayList<Proyecto> lista = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM Proyectos WHERE id_usuario = ?", new String[]{String.valueOf(idUsuario)});
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
            String descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"));
            String fechaInicio = cursor.getString(cursor.getColumnIndexOrThrow("fecha_inicio"));
            String fechaFin = cursor.getString(cursor.getColumnIndexOrThrow("fecha_fin"));

            lista.add(new Proyecto(id, idUsuario, nombre, descripcion, fechaInicio, fechaFin));
        }
        cursor.close();
        return lista;
    }

    // Métodos del listener (acciones de los botones)
    @Override
    public void onVerClick(Proyecto proyecto) {
        Toast.makeText(this, "Ver: " + proyecto.getNombre(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEditarClick(Proyecto proyecto) {
        Toast.makeText(this, "Editar: " + proyecto.getNombre(), Toast.LENGTH_SHORT).show();
        // Aquí podrías abrir una pantalla de edición
    }

    @Override
    public void onEliminarClick(Proyecto proyecto) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("Proyectos", "id = ?", new String[]{String.valueOf(proyecto.getId())});
        listaProyectos.remove(proyecto);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Proyecto eliminado", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActividadesClick(Proyecto proyecto) {
        Toast.makeText(this, "Actividades de: " + proyecto.getNombre(), Toast.LENGTH_SHORT).show();
        // Aquí podrías abrir una nueva pantalla para ver las actividades
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Recargar los proyectos del usuario
        listaProyectos = cargarProyectosDesdeDB(idUsuario);
        adapter = new ProyectoAdapter(this, listaProyectos, this);
        recyclerView.setAdapter(adapter);
    }

}

