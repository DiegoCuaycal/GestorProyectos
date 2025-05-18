package com.diegocuaycal.gestorproyectos;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class EditarProyectoActivity extends AppCompatActivity {

    DBHelper dbHelper;
    EditText etNombre, etDescripcion, etFechaInicio, etFechaFin;
    Button btnActualizar;
    int idProyecto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_proyecto);

        dbHelper = new DBHelper(this);

        // Vincular vistas
        etNombre = findViewById(R.id.etNombre);
        etDescripcion = findViewById(R.id.etDescripcion);
        etFechaInicio = findViewById(R.id.etFechaInicio);
        etFechaFin = findViewById(R.id.etFechaFin);
        btnActualizar = findViewById(R.id.btnActualizar);

        // Mostrar calendario al hacer clic
        etFechaInicio.setOnClickListener(v -> mostrarCalendario(etFechaInicio));
        etFechaFin.setOnClickListener(v -> mostrarCalendario(etFechaFin));

        // Obtener el ID del proyecto
        idProyecto = getIntent().getIntExtra("id_proyecto", -1);
        if (idProyecto == -1) {
            Toast.makeText(this, "Error al cargar proyecto", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        cargarDatosProyecto();

        btnActualizar.setOnClickListener(v -> actualizarProyecto());
    }

    private void cargarDatosProyecto() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Proyectos WHERE id = ?", new String[]{String.valueOf(idProyecto)});
        if (cursor.moveToFirst()) {
            etNombre.setText(cursor.getString(cursor.getColumnIndexOrThrow("nombre")));
            etDescripcion.setText(cursor.getString(cursor.getColumnIndexOrThrow("descripcion")));
            etFechaInicio.setText(cursor.getString(cursor.getColumnIndexOrThrow("fecha_inicio")));
            etFechaFin.setText(cursor.getString(cursor.getColumnIndexOrThrow("fecha_fin")));
        }
        cursor.close();
    }

    private void actualizarProyecto() {
        String nombre = etNombre.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();
        String fechaInicio = etFechaInicio.getText().toString().trim();
        String fechaFin = etFechaFin.getText().toString().trim();

        if (nombre.isEmpty() || fechaInicio.isEmpty() || fechaFin.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);

            java.util.Date inicio = sdf.parse(fechaInicio);
            java.util.Date fin = sdf.parse(fechaFin);

            if (fin.before(inicio)) {
                Toast.makeText(this, "La fecha de fin no puede ser anterior a la de inicio", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e) {
            Toast.makeText(this, "Formato de fecha inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("UPDATE Proyectos SET nombre = ?, descripcion = ?, fecha_inicio = ?, fecha_fin = ? WHERE id = ?",
                new Object[]{nombre, descripcion, fechaInicio, fechaFin, idProyecto});

        Toast.makeText(this, "Proyecto actualizado correctamente", Toast.LENGTH_SHORT).show();
        finish();
    }


    private void mostrarCalendario(EditText campoFecha) {
        final Calendar calendario = Calendar.getInstance();
        int anio = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    String fecha = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                    campoFecha.setText(fecha);
                },
                anio, mes, dia
        );
        datePickerDialog.show();
    }
}

