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

public class ActividadActivity extends AppCompatActivity implements ActividadAdapter.OnActividadClickListener {

    DBHelper dbHelper;
    RecyclerView recyclerView;
    ActividadAdapter adapter;
    ArrayList<Actividad> listaActividades;
    int idProyecto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_activity);

        dbHelper = new DBHelper(this);
        idProyecto = getIntent().getIntExtra("id_proyecto", -1);

        if (idProyecto == -1) {
            Toast.makeText(this, "Error: Proyecto no encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        recyclerView = findViewById(R.id.recyclerActividades);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listaActividades = cargarActividadesDesdeDB(idProyecto);

        adapter = new ActividadAdapter(this, listaActividades, this);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fabAddActividad = findViewById(R.id.fabAddActividad);
        fabAddActividad.setOnClickListener(v -> {
            Intent intent = new Intent(this, CrearActividadActivity.class);
            intent.putExtra("id_proyecto", idProyecto);
            startActivity(intent);
        });
    }

    private ArrayList<Actividad> cargarActividadesDesdeDB(int idProyecto) {
        ArrayList<Actividad> lista = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM Actividades WHERE id_proyecto = ?", new String[]{String.valueOf(idProyecto)});
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
            String descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"));
            String fechaInicio = cursor.getString(cursor.getColumnIndexOrThrow("fecha_inicio"));
            String fechaFin = cursor.getString(cursor.getColumnIndexOrThrow("fecha_fin"));
            String estado = cursor.getString(cursor.getColumnIndexOrThrow("estado"));

            lista.add(new Actividad(id, idProyecto, nombre, descripcion, fechaInicio, fechaFin, estado));
        }
        cursor.close();
        return lista;
    }

    @Override
    protected void onResume() {
        super.onResume();
        listaActividades = cargarActividadesDesdeDB(idProyecto);
        adapter = new ActividadAdapter(this, listaActividades, this);
        recyclerView.setAdapter(adapter);
    }

    // CRUD Botones
    @Override
    public void onEditarClick(Actividad actividad) {
        Intent intent = new Intent(this, EditarActividadActivity.class);
        intent.putExtra("id_actividad", actividad.getId());
        startActivity(intent);
    }

    @Override
    public void onEliminarClick(Actividad actividad) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("Actividades", "id = ?", new String[]{String.valueOf(actividad.getId())});
        listaActividades.remove(actividad);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Actividad eliminada", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCambiarEstadoClick(Actividad actividad) {
        // Cambiar el estado en orden: Planificado → En ejecución → Realizado
        String nuevoEstado;
        switch (actividad.getEstado()) {
            case "Planificado":
                nuevoEstado = "En ejecución";
                break;
            case "En ejecución":
                nuevoEstado = "Realizado";
                break;
            default:
                nuevoEstado = "Planificado";
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("UPDATE Actividades SET estado = ? WHERE id = ?", new Object[]{nuevoEstado, actividad.getId()});
        Toast.makeText(this, "Estado actualizado a: " + nuevoEstado, Toast.LENGTH_SHORT).show();
        onResume(); // Refrescar lista
    }
}
