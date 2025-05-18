package com.diegocuaycal.gestorproyectos;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class CrearActividadActivity extends AppCompatActivity {

    EditText etNombre, etDescripcion, etFechaInicio, etFechaFin;
    Spinner spinnerEstado;
    Button btnGuardar;
    DBHelper dbHelper;
    int idProyecto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_actividad);

        dbHelper = new DBHelper(this);
        idProyecto = getIntent().getIntExtra("id_proyecto", -1);

        if (idProyecto == -1) {
            Toast.makeText(this, "Error: Proyecto no válido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Referencias
        etNombre = findViewById(R.id.etNombreActividad);
        etDescripcion = findViewById(R.id.etDescripcionActividad);
        etFechaInicio = findViewById(R.id.etFechaInicioActividad);
        etFechaFin = findViewById(R.id.etFechaFinActividad);
        spinnerEstado = findViewById(R.id.spinnerEstado);
        btnGuardar = findViewById(R.id.btnGuardarActividad);

        // Desactivar edición manual y permitir solo mediante calendario
        etFechaInicio.setFocusable(false);
        etFechaInicio.setInputType(0);
        etFechaFin.setFocusable(false);
        etFechaFin.setInputType(0);

        // Mostrar calendario al tocar los campos
        etFechaInicio.setOnClickListener(view -> mostrarCalendario(etFechaInicio));
        etFechaFin.setOnClickListener(view -> mostrarCalendario(etFechaFin));

        // Estados posibles
        String[] estados = {"Planificado", "En ejecución", "Realizado"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, estados);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(adapter);

        // Guardar actividad
        btnGuardar.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString().trim();
            String descripcion = etDescripcion.getText().toString().trim();
            String fechaInicio = etFechaInicio.getText().toString().trim();
            String fechaFin = etFechaFin.getText().toString().trim();
            String estado = spinnerEstado.getSelectedItem().toString();

            if (nombre.isEmpty() || fechaInicio.isEmpty() || fechaFin.isEmpty()) {
                Toast.makeText(this, "Complete los campos obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("id_proyecto", idProyecto);
            values.put("nombre", nombre);
            values.put("descripcion", descripcion);
            values.put("fecha_inicio", fechaInicio);
            values.put("fecha_fin", fechaFin);
            values.put("estado", estado);

            long res = db.insert("Actividades", null, values);
            if (res != -1) {
                Toast.makeText(this, "Actividad registrada", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Metodo para mostrar el calendario
    private void mostrarCalendario(EditText campoFecha) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                CrearActividadActivity.this,
                (view, year1, month1, dayOfMonth) -> {
                    String fechaSeleccionada = String.format("%02d/%02d/%04d", dayOfMonth, month1 + 1, year1);
                    campoFecha.setText(fechaSeleccionada);
                },
                year, month, day
        );

        datePickerDialog.show();
    }
}
