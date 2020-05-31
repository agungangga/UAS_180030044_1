package com.aa183.AnggaPratama;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity implements ListAdapter.onSelectData{
    RecyclerView rv_list;
    List<ModelList> lsList = new ArrayList<>();
    SqlHelper helperDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        rv_list = findViewById(R.id.rv_list);
        rv_list.setHasFixedSize(true);
        rv_list.setLayoutManager(new LinearLayoutManager(this));
        helperDB = new SqlHelper(this);
        setToolbar();
    }

    private void setToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("List Negara");
        setList();
    }

    private void setList(){
        lsList.clear();
        SQLiteDatabase db = helperDB.getWritableDatabase();
        Cursor xDataList = db.rawQuery("SELECT * FROM tbNegara ORDER BY NM_NEGARA", null);
        while (xDataList.moveToNext()) {
            ModelList dataHistry = new ModelList();
            dataHistry.setIdNgr(xDataList.getString(0));
            dataHistry.setNmNgr(xDataList.getString(1));
            dataHistry.setIbuKota(xDataList.getString(2));
            dataHistry.setBahasa(xDataList.getString(3));
            dataHistry.setUang(xDataList.getString(4));
            dataHistry.setBenua(xDataList.getString(5));
            byte[] xUPLDIMG = xDataList.getBlob(6);
            String baseImg = Base64.encodeToString(xUPLDIMG, Base64.DEFAULT);
            dataHistry.setUPLDIMG(baseImg);
            lsList.add(dataHistry);
        }

        ListAdapter arrayAdapter = new ListAdapter(ListActivity.this, lsList, this);
        rv_list.setAdapter(arrayAdapter);
    }

    @Override
    public void onSelected(final ModelList mList) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
        builder.setMessage("Pilih salah satu:");
        builder.setCancelable(true);
        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getDelete(mList.getIdNgr());
            }
        });
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                startActivity(new Intent(ListActivity.this, MainActivity.class)
                        .putExtra("mList", mList));
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getDelete(final String xId){
        final AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
        builder.setMessage("Anda yakin ingin delete?");
        builder.setCancelable(true);
        builder.setNegativeButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String xDel = "DELETE FROM tbNegara WHERE ID_NEGARA = "+xId+" ";
                helperDB.DeleteData(xDel);
                if (VariableGlobal.varSqlHelper.equals("YA")){
                    Toast.makeText(ListActivity.this, "Delete sukses", Toast.LENGTH_SHORT).show();
                    setList();
                }
                else{
                    Toast.makeText(ListActivity.this, "Delete gagal!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setPositiveButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            ModelList mList= new ModelList();
            mList.setIdNgr("0");
            startActivity(new Intent(ListActivity.this, MainActivity.class)
                        .putExtra("mList", mList));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        ModelList mList= new ModelList();
        mList.setIdNgr("0");
        startActivity(new Intent(ListActivity.this, MainActivity.class)
                .putExtra("mList", mList));
    }

}
