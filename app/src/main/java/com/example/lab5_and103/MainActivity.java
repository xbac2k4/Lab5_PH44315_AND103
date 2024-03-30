package com.example.lab5_and103;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab5_and103.adapter.AdapterDistributor;
import com.example.lab5_and103.databinding.ActivityMainBinding;
import com.example.lab5_and103.model.Distributor;
import com.example.lab5_and103.service.APIService;
import com.example.lab5_and103.service.HttpRequest;
import com.example.lab5_and103.service.OnClickListen;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView rcv;
    FloatingActionButton flbtnAdd;
    HttpRequest httpRequest;
    ArrayList<Distributor> list = new ArrayList<>();
    ArrayList<Distributor> listSeacrch;
    AdapterDistributor adapterDistributor;
//    EditText edt_search;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        anhxa();
        handleCallData();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                filterList(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterList(s);
                return false;
            }
        });
//        binding.edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    String key = edt_search.getText().toString().trim();
//                    Toast.makeText(MainActivity.this, "Key: " + key, Toast.LENGTH_SHORT).show();
//                    httpRequest.callAPI().searchDistributor(key).enqueue(new Callback<com.example.lab5_and103.model.Response<ArrayList<Distributor>>>() {
//                        @Override
//                        public void onResponse(Call<com.example.lab5_and103.model.Response<ArrayList<Distributor>>> call, Response<com.example.lab5_and103.model.Response<ArrayList<Distributor>>> response) {
//                            if (response.isSuccessful()) {
//                                if (response.body().getStatus() == 200) {
//                                    ArrayList<Distributor> distributors = response.body().getData();
////                                    list = response.body().getData();
//                                    AdapterDistributor adapter = new AdapterDistributor(MainActivity.this, distributors);
//                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
//                                    rcv.setLayoutManager(linearLayoutManager);
//                                    rcv.setAdapter(adapter);
//                                    adapterDistributor.setOnClickListen(new OnClickListen() {
//                                        @Override
//                                        public void UpdateItem(Distributor distributor) {
//                                            Dialog_Add_Update(1, distributor);
//                                        }
//                                    });
//                                    adapterDistributor.notifyDataSetChanged();
//                                    Toast.makeText(MainActivity.this, "OKOK", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    Log.e(TAG, "Lỗi khi tìm kiếm: " + response.message());
//                                    Toast.makeText(MainActivity.this, "Lỗi khi tìm kiếm", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<com.example.lab5_and103.model.Response<ArrayList<Distributor>>> call, Throwable t) {
//
//                        }
//                    });
//                    return true;
//                }
//                return false;
//            }
//        });

        flbtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Distributor distributor = new Distributor();
                Dialog_Add_Update(0, distributor);
            }
        });
    }
    private void filterList(String text) {
        if (!text.equals("")) {
            listSeacrch = new ArrayList<>();
            httpRequest.callAPI().searchDistributor(text).enqueue(new Callback<com.example.lab5_and103.model.Response<ArrayList<Distributor>>>() {
                @Override
                public void onResponse(Call<com.example.lab5_and103.model.Response<ArrayList<Distributor>>> call, Response<com.example.lab5_and103.model.Response<ArrayList<Distributor>>> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 200) {
                            listSeacrch = response.body().getData();
                           getData(listSeacrch);
                        }
                    }
                }

                @Override
                public void onFailure(Call<com.example.lab5_and103.model.Response<ArrayList<Distributor>>> call, Throwable t) {

                }
            });
        } else {
            handleCallData();
        }
    }
    private void anhxa() {
        rcv = findViewById(R.id.rcv);
        flbtnAdd = findViewById(R.id.flbtn_add);
        httpRequest = new HttpRequest();
//        edt_search = findViewById(R.id.edt_search);
        searchView = findViewById(R.id.edt_search);
        //
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");
        getSupportActionBar().setSubtitle("Management");
    }

    private void handleCallData() {
        httpRequest.callAPI().getDistributor().enqueue(new Callback<com.example.lab5_and103.model.Response<ArrayList<Distributor>>>() {
            @Override
            public void onResponse(Call<com.example.lab5_and103.model.Response<ArrayList<Distributor>>> call, Response<com.example.lab5_and103.model.Response<ArrayList<Distributor>>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 200) {
                        list = response.body().getData();
                        getData(list);
                    }
                }
            }

            @Override
            public void onFailure(Call<com.example.lab5_and103.model.Response<ArrayList<Distributor>>> call, Throwable t) {

            }
        });
    }
    private void getData(ArrayList<Distributor> arrayList) {
        adapterDistributor = new AdapterDistributor(MainActivity.this, arrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        rcv.setLayoutManager(linearLayoutManager);
        rcv.setAdapter(adapterDistributor);
        adapterDistributor.setOnClickListen(new OnClickListen() {
            @Override
            public void UpdateItem(Distributor distributor) {
                Dialog_Add_Update(1, distributor);
            }
        });
        adapterDistributor.notifyDataSetChanged();
    }
    private void Dialog_Add_Update(int type, Distributor distributor) {
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_add_update, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tvID = view.findViewById(R.id.tv_id);
        TextView tieude = view.findViewById(R.id.tv_tieude);
        EditText edtTitle = view.findViewById(R.id.edt_tilte);
        Button btnAdd = view.findViewById(R.id.btn_add);
        Button btnCancel = view.findViewById(R.id.btn_cancle);

        tvID.setVisibility(View.INVISIBLE);

        if (type != 0) {
            tvID.setVisibility(View.VISIBLE);
            tvID.setText("ID: " + distributor.get_id());
            tieude.setText("UPDATE DISTRIBUTOR");
            btnAdd.setText("UPDATE");
            String strDistributor = distributor.getTitle();
            edtTitle.setText(strDistributor);
        }

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = edtTitle.getText().toString().trim();
                if (type == 0) {
                    if (!title.isEmpty()) {
                        distributor.setTitle(title);
                        httpRequest.callAPI().addDistributor(distributor).enqueue(new Callback<com.example.lab5_and103.model.Response<Distributor>>() {
                            @Override
                            public void onResponse(Call<com.example.lab5_and103.model.Response<Distributor>> call, Response<com.example.lab5_and103.model.Response<Distributor>> response) {
                                if (response.isSuccessful()) {
                                    handleCallData();
                                    dialog.dismiss();
                                    Toast.makeText(MainActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<com.example.lab5_and103.model.Response<Distributor>> call, Throwable t) {

                            }
                        });
                    } else {
                        Toast.makeText(MainActivity.this, "Vui lòng nhập thông tin", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (!title.isEmpty()) {
                        distributor.setTitle(title);
                        httpRequest.callAPI().updateDistributor(distributor.get_id(), distributor).enqueue(new Callback<com.example.lab5_and103.model.Response<Distributor>>() {
                            @Override
                            public void onResponse(Call<com.example.lab5_and103.model.Response<Distributor>> call, Response<com.example.lab5_and103.model.Response<Distributor>> response) {
                                if (response.isSuccessful()) {
                                    handleCallData();
                                    dialog.dismiss();
                                    Toast.makeText(MainActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<com.example.lab5_and103.model.Response<Distributor>> call, Throwable t) {

                            }
                        });
                    } else {
                        Toast.makeText(MainActivity.this, "Vui lòng nhập thông tin", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}