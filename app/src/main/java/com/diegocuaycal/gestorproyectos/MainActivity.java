package com.diegocuaycal.gestorproyectos;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
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

    // Metodo para cargar los proyectos desde la base de datos
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

            int total = 0;
            int realizadas = 0;
            Cursor actCursor = db.rawQuery("SELECT estado FROM Actividades WHERE id_proyecto = ?", new String[]{String.valueOf(id)});
            while (actCursor.moveToNext()) {
                total++;
                String estado = actCursor.getString(0);
                if ("Realizado".equalsIgnoreCase(estado)) {
                    realizadas++;
                }
            }
            actCursor.close();

            int porcentaje = (total > 0) ? (realizadas * 100 / total) : 0;

            Proyecto proyecto = new Proyecto(id, idUsuario, nombre, descripcion, fechaInicio, fechaFin);
            proyecto.setPorcentajeAvance(porcentaje); // Este método debe existir en tu clase Proyecto
            lista.add(proyecto);
        }
        cursor.close();
        return lista;
    }


    // Métodos del listener (acciones de los botones)
    @Override
    public void onVerClick(Proyecto proyecto) {
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT nombre, descripcion, fecha_inicio, fecha_fin FROM Proyectos WHERE id = ?",
                new String[]{String.valueOf(proyecto.getId())});

        if (cursor.moveToFirst()) {
            String nombre = cursor.getString(0);
            String descripcion = cursor.getString(1);
            String fechaInicio = cursor.getString(2);
            String fechaFin = cursor.getString(3);

            View view = getLayoutInflater().inflate(R.layout.bottom_ver_proyecto, null);

            TextView tvNombre = view.findViewById(R.id.tvNombreModal);
            TextView tvDescripcion = view.findViewById(R.id.tvDescripcionModal);
            TextView tvFechas = view.findViewById(R.id.tvFechasModal);

            tvNombre.setText(nombre);
            tvDescripcion.setText(descripcion);
            tvFechas.setText(fechaInicio + " → " + fechaFin);

            BottomSheetDialog dialog = new BottomSheetDialog(this);
            dialog.setContentView(view);
            dialog.show();
        } else {
            Toast.makeText(this, "No se encontró el proyecto", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        db.close();
    }


    @Override
    public void onEditarClick(Proyecto proyecto) {
        Intent intent = new Intent(this, EditarProyectoActivity.class);
        intent.putExtra("id_proyecto", proyecto.getId());
        startActivity(intent);
    }


    @Override
    public void onEliminarClick(Proyecto proyecto) {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Eliminar proyecto")
                .setMessage("¿Estás seguro de que deseas eliminar \"" + proyecto.getNombre() + "\"?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.delete("Proyectos", "id = ?", new String[]{String.valueOf(proyecto.getId())});
                    listaProyectos.remove(proyecto);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "Proyecto eliminado", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }


    @Override
    public void onActividadesClick(Proyecto proyecto) {
        Intent intent = new Intent(this, ActividadActivity.class);
        intent.putExtra("id_proyecto", proyecto.getId());
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        listaProyectos = cargarProyectosDesdeDB(idUsuario);
        adapter = new ProyectoAdapter(this, listaProyectos, this);
        recyclerView.setAdapter(adapter);
    }

}

