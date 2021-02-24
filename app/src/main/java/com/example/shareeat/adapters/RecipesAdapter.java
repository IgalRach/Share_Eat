package com.example.shareeat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shareeat.R;
import com.example.shareeat.model.Recipe;
import com.google.firebase.database.core.Context;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecipesAdapter  extends RecyclerView.Adapter<RecipesViewHolder> {

    public List<Recipe> data;
    LayoutInflater inflater;

    public RecipesAdapter(LayoutInflater inflater) {
        this.inflater=inflater;
    }

    public interface OnItemClickListener{
        void OnItemClick(int position);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }

    @NonNull
    @Override
    public RecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_row,null);
        RecipesViewHolder holder = new RecipesViewHolder(view);
        holder.listener= listener;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesViewHolder holder, int position) {
        Recipe recipe = data.get(position);
        holder.bindData(recipe,position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
