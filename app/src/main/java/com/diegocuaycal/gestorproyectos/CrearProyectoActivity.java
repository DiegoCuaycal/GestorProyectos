package com.diegocuaycal.gestorproyectos;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class CrearProyectoActivity extends AppCompatActivity {

    EditText etNombreProyecto, etDescripcion, etFechaInicio, etFechaFin;
    TextInputLayout tilFechaInicio, tilFechaFin;
    Button btnGuardar;
    DBHelper dbHelper;
    int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_proyecto);

        String nombreUsuario = getIntent().getStringExtra("usuario");

        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT id FROM Usuarios WHERE usuario = ?", new String[]{nombreUsuario});
        if (cursor.moveToFirst()) {
            idUsuario = cursor.getInt(0);
        } else {
            Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
            finish();
        }
        cursor.close();

        // Vistas
        etNombreProyecto = findViewById(R.id.etNombreProyecto);
        etDescripcion = findViewById(R.id.etDescripcion);
        etFechaInicio = findViewById(R.id.etFechaInicio);
        etFechaFin = findViewById(R.id.etFechaFin);
        tilFechaInicio = findViewById(R.id.textInputLayoutFechaInicio);
        tilFechaFin = findViewById(R.id.textInputLayoutFechaFin);
        btnGuardar = findViewById(R.id.btnGuardarProyecto);

        // Mostrar calendario al tocar campo o icono
        etFechaInicio.setOnClickListener(v -> mostrarCalendario(etFechaInicio));
        etFechaFin.setOnClickListener(v -> mostrarCalendario(etFechaFin));
        tilFechaInicio.setEndIconOnClickListener(v -> mostrarCalendario(etFechaInicio));
        tilFechaFin.setEndIconOnClickListener(v -> mostrarCalendario(etFechaFin));

        // Guardar
        btnGuardar.setOnClickListener(v -> {
            String nombre = etNombreProyecto.getText().toString().trim();
            String descripcion = etDescripcion.getText().toString().trim();
            String fechaInicio = etFechaInicio.getText().toString().trim();
            String fechaFin = etFechaFin.getText().toString().trim();

            if (nombre.isEmpty() || fechaInicio.isEmpty() || fechaFin.isEmpty()) {
                Toast.makeText(this, "Nombre y fechas son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
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

            SQLiteDatabase writableDB = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("id_usuario", idUsuario);
            values.put("nombre", nombre);
            values.put("descripcion", descripcion);
            values.put("fecha_inicio", fechaInicio);
            values.put("fecha_fin", fechaFin);

            long resultado = writableDB.insert("Proyectos", null, values);

            if (resultado != -1) {
                Toast.makeText(this, "Proyecto guardado", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al guardar proyecto", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarCalendario(EditText campoFecha) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                CrearProyectoActivity.this,
                (view, year1, month1, dayOfMonth) -> {
                    String fechaSeleccionada = String.format("%02d/%02d/%04d", dayOfMonth, month1 + 1, year1);
                    campoFecha.setText(fechaSeleccionada);
                },
                year, month, day
        );

        datePickerDialog.show();
    }
}

