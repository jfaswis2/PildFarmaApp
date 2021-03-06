package com.example.pildfarmaapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.pildfarmaapp.adapters.PostsAdapter;
import com.example.pildfarmaapp.R;
import com.example.pildfarmaapp.models.PostAlarma;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;


//Clase para inflar el cardview con los datos solicitados
public class PostsAdapter extends FirestoreRecyclerAdapter<PostAlarma, PostsAdapter.ViewHolder> {

    //Declaración de variables
    Context context;

    public PostsAdapter(FirestoreRecyclerOptions<PostAlarma> options, Context context){
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull PostsAdapter.ViewHolder holder, int position, @NonNull PostAlarma post) {
        holder.textViewNombre.setText(post.getMedicamento());
        holder.textViewEstad.setText(post.getEstado());
        if(holder.textViewEstad.getText().toString().equals("Activo")){
            holder.textViewEstad.setTextColor(Color.parseColor("#00FF00"));
        }else if(holder.textViewEstad.getText().toString().equals("Finalizado")){
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
    public PostsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_post,parent,false);
        return new PostsAdapter.ViewHolder(view);
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
