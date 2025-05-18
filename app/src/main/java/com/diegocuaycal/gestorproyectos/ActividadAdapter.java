package com.diegocuaycal.gestorproyectos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ActividadAdapter extends RecyclerView.Adapter<ActividadAdapter.ActividadViewHolder> {

    private Context context;
    private List<Actividad> lista;
    private OnActividadClickListener listener;

    public interface OnActividadClickListener {
        void onEditarClick(Actividad actividad);
        void onEliminarClick(Actividad actividad);
        void onCambiarEstadoClick(Actividad actividad);
    }

    public ActividadAdapter(Context context, List<Actividad> lista, OnActividadClickListener listener) {
        this.context = context;
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ActividadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(context).inflate(R.layout.item_actividad, parent, false);
        return new ActividadViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ActividadViewHolder holder, int position) {
        Actividad actividad = lista.get(position);
        holder.tvNombre.setText(actividad.getNombre());
        holder.tvDescripcion.setText(actividad.getDescripcion());
        holder.tvFechas.setText(actividad.getFechaInicio() + " → " + actividad.getFechaFin());
        holder.tvEstado.setText("Estado: " + actividad.getEstado());

        holder.btnEditar.setOnClickListener(v -> listener.onEditarClick(actividad));
        holder.btnEliminar.setOnClickListener(v -> listener.onEliminarClick(actividad));
        holder.btnCambiarEstado.setOnClickListener(v -> listener.onCambiarEstadoClick(actividad));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ActividadViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvDescripcion, tvFechas, tvEstado;
        ImageButton btnEditar, btnEliminar, btnCambiarEstado;

        public ActividadViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreActividad);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcionActividad);
            tvFechas = itemView.findViewById(R.id.tvFechasActividad);
            tvEstado = itemView.findViewById(R.id.tvEstadoActividad);
            btnEditar = itemView.findViewById(R.id.btnEditarActividad);
            btnEliminar = itemView.findViewById(R.id.btnEliminarActividad);
            btnCambiarEstado = itemView.findViewById(R.id.btnCambiarEstadoActividad);
        }
    }
}
