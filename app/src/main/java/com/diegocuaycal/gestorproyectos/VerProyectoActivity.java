package com.diegocuaycal.gestorproyectos;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class VerProyectoActivity extends AppCompatActivity {

    DBHelper dbHelper;
    TextView tvNombre, tvDescripcion, tvFechas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_proyecto);

        tvNombre = findViewById(R.id.tvNombreVer);
        tvDescripcion = findViewById(R.id.tvDescripcionVer);
        tvFechas = findViewById(R.id.tvFechasVer);

        dbHelper = new DBHelper(this);

        int idProyecto = getIntent().getIntExtra("id_proyecto", -1);
        if (idProyecto == -1) {
            Toast.makeText(this, "Proyecto no válido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        cargarDatosProyecto(idProyecto);
    }

    private void cargarDatosProyecto(int idProyecto) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT nombre, descripcion, fecha_inicio, fecha_fin FROM Proyectos WHERE id = ?",
                new String[]{String.valueOf(idProyecto)});

        if (cursor.moveToFirst()) {
            String nombre = cursor.getString(0);
            String descripcion = cursor.getString(1);
            String fechaInicio = cursor.getString(2);
            String fechaFin = cursor.getString(3);

            tvNombre.setText(nombre);
            tvDescripcion.setText(descripcion);
            tvFechas.setText(fechaInicio + " → " + fechaFin);
        }
        cursor.close();
    }
}
