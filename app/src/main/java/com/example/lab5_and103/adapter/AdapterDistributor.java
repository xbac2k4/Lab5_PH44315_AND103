package com.example.lab5_and103.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab5_and103.MainActivity;
import com.example.lab5_and103.R;
import com.example.lab5_and103.model.Distributor;
import com.example.lab5_and103.service.APIService;
import com.example.lab5_and103.service.HttpRequest;
import com.example.lab5_and103.service.OnClickListen;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdapterDistributor extends RecyclerView.Adapter<AdapterDistributor.ViewHolder>{
    private final Context context;
    private final ArrayList<Distributor> list;
    Distributor distributor;
    APIService apiService;
    HttpRequest httpRequest = new HttpRequest();
    OnClickListen onClickListen;
    public void setOnClickListen(OnClickListen onClickListen) {
        this.onClickListen = onClickListen;
    }

    public AdapterDistributor(Context context, ArrayList<Distributor> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tvID.setText("ID: " + list.get(position).get_id());
        holder.tvTitle.setText(list.get(position).getTitle());
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                distributor = list.get(position);
                if (onClickListen !=null){
                    onClickListen.UpdateItem(distributor);
                }
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                distributor = list.get(position);
                Dialod_Delete(distributor);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvID;
        TextView tvTitle;
        ImageButton edit, delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvID = itemView.findViewById(R.id.tv_id);
            tvTitle = itemView.findViewById(R.id.tv_title);
            edit = itemView.findViewById(R.id.ibtn_edit);
            delete = itemView.findViewById(R.id.ibtn_delete);
        }
    }
    private void Dialod_Delete(Distributor distributor) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Bạn có chắc chắn muốn xóa không ?");
        builder.setIcon(R.drawable.warning).setTitle("Cảnh Báo");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                httpRequest.callAPI().deleteDistributor(distributor.get_id()).enqueue(new Callback<com.example.lab5_and103.model.Response<Distributor>>() {
                    @Override
                    public void onResponse(Call<com.example.lab5_and103.model.Response<Distributor>> call, Response<com.example.lab5_and103.model.Response<Distributor>> response) {
                        if (response.isSuccessful()) {
                            list.remove(distributor);
                            notifyDataSetChanged();
                            Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(context, "Xóa thật bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<com.example.lab5_and103.model.Response<Distributor>> call, Throwable t) {
                        Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                        Log.e("onResponse: ", t.getMessage());
                    }
                });
            }
        }).setNegativeButton("Cancel", null);
        builder.show();
    }
}
