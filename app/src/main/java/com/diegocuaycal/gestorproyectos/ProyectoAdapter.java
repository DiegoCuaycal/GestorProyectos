package com.diegocuaycal.gestorproyectos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProyectoAdapter extends RecyclerView.Adapter<ProyectoAdapter.ProyectoViewHolder> {
    // Atributos
    private Context context;
    private List<Proyecto> listaProyectos;
    private OnProyectoClickListener listener;

    // Constructor
    public ProyectoAdapter(Context context, List<Proyecto> listaProyectos, OnProyectoClickListener listener) {
        this.context = context;
        this.listaProyectos = listaProyectos;
        this.listener = listener;
    }
    // Métodos
    @NonNull
    @Override
    public ProyectoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(context).inflate(R.layout.item_proyecto, parent, false);
        return new ProyectoViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ProyectoViewHolder holder, int position) {
        Proyecto proyecto = listaProyectos.get(position);

        holder.tvNombre.setText(proyecto.getNombre());
        holder.tvDescripcion.setText(proyecto.getDescripcion());
        holder.tvFechas.setText(proyecto.getFechaInicio() + " → " + proyecto.getFechaFin());

        holder.btnVer.setOnClickListener(v -> listener.onVerClick(proyecto));
        holder.btnEditar.setOnClickListener(v -> listener.onEditarClick(proyecto));
        holder.btnEliminar.setOnClickListener(v -> listener.onEliminarClick(proyecto));
        holder.btnIrActividades.setOnClickListener(v -> listener.onActividadesClick(proyecto));
    }

    @Override
    public int getItemCount() {
        return listaProyectos.size();
    }

    public static class ProyectoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvDescripcion, tvFechas;
        ImageButton btnVer, btnEditar, btnEliminar;
        Button btnIrActividades;

        public ProyectoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreProyecto);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvFechas = itemView.findViewById(R.id.tvFechas);
            btnVer = itemView.findViewById(R.id.btnVer);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
            btnIrActividades = itemView.findViewById(R.id.btnIrActividades);
        }
    }

    // Interfaz para manejar clics desde el MainActivity
    public interface OnProyectoClickListener {
        void onVerClick(Proyecto proyecto);
        void onEditarClick(Proyecto proyecto);
        void onEliminarClick(Proyecto proyecto);
        void onActividadesClick(Proyecto proyecto);
    }
}
