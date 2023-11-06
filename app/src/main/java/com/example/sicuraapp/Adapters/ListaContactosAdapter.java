package com.example.sicuraapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sicuraapp.Entities.Usuario;
import com.example.sicuraapp.R;

import java.util.ArrayList;

public class ListaContactosAdapter extends RecyclerView.Adapter<ListaContactosAdapter.ViewHolder> {

    private ArrayList<Usuario> listaUsuarios;
    private Context context;


    public ListaContactosAdapter(Context contexto, ArrayList<Usuario> dataSet){
        context = contexto;
        listaUsuarios = dataSet;
        for (Usuario u : listaUsuarios){
            Log.d("msg","usuarios"+u.getNombreApellidos());
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.rv_contactos, parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaContactosAdapter.ViewHolder holder, int position) {
        Usuario usuario = listaUsuarios.get(position);
        holder.usuario = usuario;
        ImageView imageView =holder.imageView;
        TextView nombreUsuario = holder.nombrePersona;
        TextView celularUsuario = holder.celularPersona;
        String nombreUsuarioString =usuario.getNombreApellidos();
        String celularUsuarioString = usuario.getCelular();
        if(usuario.getFotoUrl()!=null){
            String urlImage = usuario.getFotoUrl();
            //Glide.with(imageView).load(urlImage).into(imageView);

        }
        nombreUsuario.setText(nombreUsuarioString);
        celularUsuario.setText(celularUsuarioString);
    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        Usuario usuario;
        ImageView imageView;
        TextView nombrePersona;
        TextView celularPersona;
        Button btnDetallesPersona;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            imageView =(ImageView) itemView.findViewById(R.id.imageViewProfile) ;
            nombrePersona = (TextView) itemView.findViewById(R.id.textviewNombrePersona);
            celularPersona =(TextView)  itemView.findViewById(R.id.textViewCelular);
            btnDetallesPersona = (Button) itemView.findViewById(R.id.btnDetallesPersona);
            btnDetallesPersona.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("msg","nombre:"+usuario.getNombreApellidos());

                    //Intent intent = new Intent(view.getContext(), Membresia_CoordinadoresDetalle.class);
                    //intent.putExtra("usuarioDetalle", usuario);

                }
            });

        }
    }
}
