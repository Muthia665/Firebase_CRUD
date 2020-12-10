package com.example.firebasecrud;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainAdapter.FirebaseDataListener {

    private ExtendedFloatingActionButton mFloatingActionButton;
    private EditText mEditNama;
    private EditText mEditMerk;
    private EditText mEditHarga;
    private RecyclerView mRecyclerView;
    private MainAdapter mAdapter;
    private ArrayList<ModelBarang> daftarBarang;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseInstance;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }

        if (Build.VERSION.SDK_INT >= 19){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                getWindow().getDecorView()
                        .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
        if (Build.VERSION.SDK_INT >= 21){
            setWindowFlags(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false),
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseApp.initializeApp(this);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseInstance.getReference("barang");
        mDatabaseReference.child("data_barang").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                daftarBarang = new ArrayList<>();
                for (DataSnapshot mDataSnapshot : dataSnapshot.getChildren()){
                    ModelBarang barang = mDataSnapshot.getValue(ModelBarang.class);
                    barang.setKey(mDataSnapshot.getKey());
                    daftarBarang.add(barang);
                }

                mAdapter = new MainAdapter(MainActivity.this, daftarBarang);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this,databaseError.getDetails() + " " +
                                databaseError.getMessage(),  Toast.LENGTH_SHORT).show();
            }
        });

        mFloatingActionButton = findViewById(R.id.tambah_barang);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogTambahBarang();
            }
        });

    }

    private void setWindowFlag(MainActivity mainActivity, int flagTranslucentStatus, boolean b) {
        Window window = mainActivity.getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        if (b){
            windowParams.flags |= flagTranslucentStatus;
        } else {
            windowParams.flags &= ~flagTranslucentStatus;
        }
        window.setAttributes(windowParams);
    }

    @Override
    public void onDataClick(@Nullable final ModelBarang barang, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilih Aksi");

        builder.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogUpdateBarang(barang);
            }
        });
        builder.setNegativeButton("HAPUS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        Dialog dialog =  builder.create();
        dialog.show();

    }

    private void dialogTambahBarang() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tambah Data Model Barang");
        View view = getLayoutInflater().inflate(R.layout.layout_edit_barang);

        mEditNama = view.findViewById(R.id.nama_barang);
        mEditMerk = view.findViewById(R.id.merk_barang);
        mEditHarga = view.findViewById(R.id.harga_barang);
        builder.setView(view);

        builder.setPositiveButton("SIMPAN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String namaBarang = mEditNama.getText().toString();
                String merkBarang = mEditMerk.getText().toString();
                String hargaBarang = mEditHarga.getText().toString();

                if (!namaBarang.isEmpty() && !hargaBarang.isEmpty()){
                    submitDataBarang(new ModelBarang, merkBarang, hargaBarang))
                } else {
                    Toast.makeText(MainActivity.this, "Data harus di isi !", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }); 
    }

    private void hapusDataBarang(ModelBarang barang) {

    }

    private void dialogUpdateBarang(ModelBarang barang) {
    }


}






























