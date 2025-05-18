package com.diegocuaycal.gestorproyectos;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class EditarActividadActivity extends AppCompatActivity {

    EditText etNombre, etDescripcion, etFechaInicio, etFechaFin;
    Spinner spinnerEstado;
    Button btnActualizar;
    DBHelper dbHelper;
    int idActividad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_actividad);

        dbHelper = new DBHelper(this);
        idActividad = getIntent().getIntExtra("id_actividad", -1);

        if (idActividad == -1) {
            Toast.makeText(this, "Error: Actividad no encontrada", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Referencias
        etNombre = findViewById(R.id.etNombreActividad);
        etDescripcion = findViewById(R.id.etDescripcionActividad);
        etFechaInicio = findViewById(R.id.etFechaInicioActividad);
        etFechaFin = findViewById(R.id.etFechaFinActividad);
        spinnerEstado = findViewById(R.id.spinnerEstado);
        btnActualizar = findViewById(R.id.btnActualizarActividad);

        String[] estados = {"Planificado", "En ejecución", "Realizado"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, estados);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(adapter);

        // Mostrar calendario al hacer clic en las fechas
        etFechaInicio.setOnClickListener(v -> mostrarCalendario(etFechaInicio));
        etFechaFin.setOnClickListener(v -> mostrarCalendario(etFechaFin));

        cargarDatosActividad();

        btnActualizar.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString().trim();
            String descripcion = etDescripcion.getText().toString().trim();
            String fechaInicio = etFechaInicio.getText().toString().trim();
            String fechaFin = etFechaFin.getText().toString().trim();
            String estado = spinnerEstado.getSelectedItem().toString();

            if (nombre.isEmpty() || fechaInicio.isEmpty() || fechaFin.isEmpty()) {
                Toast.makeText(this, "Campos obligatorios vacíos", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                sdf.setLenient(false);
                java.util.Date inicio = sdf.parse(fechaInicio);
                java.util.Date fin = sdf.parse(fechaFin);

                if (fin.before(inicio)) {
                    Toast.makeText(this, "La fecha de fin no puede ser anterior a la fecha de inicio", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (Exception e) {
                Toast.makeText(this, "Formato de fecha inválido", Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("nombre", nombre);
            values.put("descripcion", descripcion);
            values.put("fecha_inicio", fechaInicio);
            values.put("fecha_fin", fechaFin);
            values.put("estado", estado);

            int res = db.update("Actividades", values, "id = ?", new String[]{String.valueOf(idActividad)});
            if (res > 0) {
                Toast.makeText(this, "Actividad actualizada", Toast.LENGTH_SHORT).show();
                finish(); // volver
            } else {
                Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarDatosActividad() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Actividades WHERE id = ?", new String[]{String.valueOf(idActividad)});
        if (cursor.moveToFirst()) {
            etNombre.setText(cursor.getString(cursor.getColumnIndexOrThrow("nombre")));
            etDescripcion.setText(cursor.getString(cursor.getColumnIndexOrThrow("descripcion")));
            etFechaInicio.setText(cursor.getString(cursor.getColumnIndexOrThrow("fecha_inicio")));
            etFechaFin.setText(cursor.getString(cursor.getColumnIndexOrThrow("fecha_fin")));
            String estado = cursor.getString(cursor.getColumnIndexOrThrow("estado"));

            int pos = 0;
            if (estado.equals("En ejecución")) pos = 1;
            else if (estado.equals("Realizado")) pos = 2;

            spinnerEstado.setSelection(pos);
        }
        cursor.close();
    }

    private void mostrarCalendario(EditText campoFecha) {
        final Calendar calendario = Calendar.getInstance();
        int anio = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialogoFecha = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    String fecha = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                    campoFecha.setText(fecha);
                },
                anio, mes, dia
        );

        dialogoFecha.show();
    }
}

