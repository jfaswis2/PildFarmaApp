package com.example.pildfarmaapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

public class PostAdapter extends FirestoreRecyclerAdapter<DatosAlarma,PostAdapter.ViewHolder> {

    Context context;

    public PostAdapter(FirestoreRecyclerOptions<DatosAlarma> options, Context context){
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull DatosAlarma post) {
        holder.textViewNombre.setText(post.getMedicamento());
        holder.textViewEstad.setText(post.getEstado());
        if(holder.textViewEstad.getText().toString().equals("Activo")){
            holder.textViewEstad.setTextColor(Color.parseColor("#00FF00"));
        }else if(holder.textViewEstad.getText().toString().equals("Inactivo")){
            holder.textViewEstad.setTextColor(Color.parseColor("#FF0000"));
        }

        if(post.getImagen() !=null){
            if(!post.getImagen().isEmpty()){
                Picasso.with(context).load(post.getImagen()).into(holder.imageViewImagen);
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_post,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewNombre;
        TextView textViewEstad;
        ImageView imageViewImagen;

        public ViewHolder(View view){
            super(view);
            textViewNombre = view.findViewById(R.id.TituloCardView);
            textViewEstad = view.findViewById(R.id.EstadoCardView);
            imageViewImagen = view.findViewById(R.id.ImagenCardView);
        }
    }
}
