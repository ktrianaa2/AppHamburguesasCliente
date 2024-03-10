package com.example.apphamburguesascliente.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphamburguesascliente.Modelos.ComboModelo;
import com.example.apphamburguesascliente.R;

import java.util.List;

public class ComboAdaptador extends RecyclerView.Adapter<ComboAdaptador.ComboViewHolder> {

    private List<ComboModelo.Combo> combos;
    private Context context;

    public ComboAdaptador(List<ComboModelo.Combo> combos, Context context) {
        this.combos = combos;
        this.context = context;
    }

    @NonNull
    @Override
    public ComboViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.combos_recycler_items, parent, false);
        return new ComboViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComboViewHolder holder, int position) {
        ComboModelo.Combo combo = combos.get(position);

        holder.comboName.setText(combo.getNombreCb());
        holder.comboDescription.setText(combo.getDescripcionCombo());
        holder.comboPrice.setText("$" + combo.getPrecioUnitario());
    }

    @Override
    public int getItemCount() {
        return combos.size();
    }

    public void setCombos(List<ComboModelo.Combo> listaCombos) {
        combos.clear();
        combos.addAll(listaCombos);
        notifyDataSetChanged();
    }


    public static class ComboViewHolder extends RecyclerView.ViewHolder {
        TextView comboName, comboDescription, comboPrice;

        public ComboViewHolder(@NonNull View itemView) {
            super(itemView);
            comboName = itemView.findViewById(R.id.combo_name);
            comboDescription = itemView.findViewById(R.id.combo_description);
            comboPrice = itemView.findViewById(R.id.combo_price);
        }
    }

}
