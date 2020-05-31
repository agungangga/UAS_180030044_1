package com.aa183.AnggaPratama;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText etNama, etIbuKota, etBahasa, etUang, etBenua;
    Button btnSave, btnUpload;
    ImageView mPreview;
    SqlHelper helperDB;
    byte[] imageBytes;
    boolean doubleBackToExitPressedOnce = false;
    String sId = "0", sNama, sIbu, sBahasa, sUang, sBenua, cameraFilePath, encodedImage;
    ModelList mList;
    private static final int REQUEST_PICK_PHOTO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setID();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sNama = etNama.getText().toString().toUpperCase();
                sIbu = etIbuKota.getText().toString().toUpperCase();
                sBahasa = etBahasa.getText().toString().toUpperCase();
                sUang = etUang.getText().toString().toUpperCase();
                sBenua = etBenua.getText().toString().toUpperCase();
                if (!sNama.equals("") && !sIbu.equals("") && !sBahasa.equals("")
                        && !sUang.equals("") && !sBenua.equals("")){
                    if (!sId.equals("0")) {
                        updateDB(sIbu, sBahasa, sUang, sBenua);
                    }
                    else {
                        saveDB(sNama, sIbu, sBahasa, sUang, sBenua);
                    }
                }
                else {
                    Toast.makeText(MainActivity.this, "Semua harus di isi!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpload();
            }
        });
    }

    private void setID(){
        etNama = findViewById(R.id.etNama);
        etIbuKota = findViewById(R.id.etIbuKota);
        etBahasa = findViewById(R.id.etBahasa);
        etUang = findViewById(R.id.etUang);
        etBenua = findViewById(R.id.etBenua);
        btnSave = findViewById(R.id.btnSave);
        btnUpload = findViewById(R.id.btnUpload);
        mPreview = findViewById(R.id.mPreviewUpload);
        setModel();
    }

    private void setModel(){
        mList = (ModelList) getIntent().getSerializableExtra("mList");
        if (mList != null) {
            sId = mList.getIdNgr();
            if (!sId.equals("0")){
                etNama.setText(mList.getNmNgr());
                etIbuKota.setText(mList.getIbuKota());
                etBahasa.setText(mList.getBahasa());
                etUang.setText(mList.getUang());
                etBenua.setText(mList.getBenua());
                btnSave.setText("Update");
                etNama.setEnabled(false);
                btnUpload.setVisibility(View.GONE);

                Glide.with(this)
                        .load(Base64.decode(mList.getUPLDIMG(), Base64.DEFAULT))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.ic_account)
                        .into(mPreview);
            }
            else {
                setClear();
            }
        }
        setToolbar();
    }

    private void setClear (){
        etNama.setText("");
        etIbuKota.setText("");
        etBahasa.setText("");
        etUang.setText("");
        etBenua.setText("");
        btnSave.setText("Simpan Data");
        etNama.setEnabled(true);
        btnUpload.setVisibility(View.VISIBLE);
        mPreview.setImageResource(R.drawable.ic_account);
        sId = "0";
    }

    private void setToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle("NEGARA");

        helperDB = new SqlHelper(this);
    }

    private void saveDB(String xNm, String xIbu, String xBhs, String xUang, String xBenua){
        String ckData = helperDB.cekData(xNm);
        String[] dataArry = ckData.split("#");
        if (dataArry[0].isEmpty()){
            helperDB.InsertImg(xNm, xIbu, xBhs, xUang, xBenua, imageBytes);
            if (VariableGlobal.varSqlHelper.equals("YA")){
                Toast.makeText(MainActivity.this, "Save sukses", Toast.LENGTH_SHORT).show();
                setClear();
            }
            else{
                Toast.makeText(MainActivity.this, "Save gagal!", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(MainActivity.this, "Nama negara uda ada!", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateDB(String xIbu, String xBhs, String xUang, String xBenua){
        String xIns = " UPDATE tbNegara SET IBUKOTA = '"+xIbu+"', BAHASA = '"+xBhs+"', " +
                " UANG = '"+xUang+"', BENUA = '"+xBenua+"' WHERE ID_NEGARA = "+sId+" ";
        helperDB.UpdateData(xIns);
        if (VariableGlobal.varSqlHelper.equals("YA")){
            Toast.makeText(MainActivity.this, "Update sukses", Toast.LENGTH_SHORT).show();
            setClear();
        }
        else{
            Toast.makeText(MainActivity.this, "Update gagal!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpload(){
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(galleryIntent, REQUEST_PICK_PHOTO);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void convertImage(String urlImg) {
        File imgFile = new File(urlImg);
        if (imgFile.exists()) {
            long cekSize = imgFile.length();
            BitmapFactory.Options options = new BitmapFactory.Options();
            if (cekSize > 500000 && cekSize < 1000000)
                options.inSampleSize = 3;
            else if (cekSize > 1000000 && cekSize < 1500000)
                options.inSampleSize = 5;
            else if (cekSize > 1500000 && cekSize < 2000000)
                options.inSampleSize = 10;
            else if (cekSize > 2000000)
                options.inSampleSize = 20;
            else
                options.inSampleSize = 0;
            final Bitmap bitmap = BitmapFactory.decodeFile(cameraFilePath, options);

            mPreview.setImageBitmap(bitmap);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            imageBytes = baos.toByteArray();
            encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_PHOTO && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            assert selectedImage != null;
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String mediaPath = cursor.getString(columnIndex);
            cursor.close();
            cameraFilePath = mediaPath;
            convertImage(mediaPath);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.listNegara) {
            String ckRmp = helperDB.cekEmpty();
            String[] empArry = ckRmp.split("#");
            if (empArry[0].isEmpty()){
                Toast.makeText(MainActivity.this, "Belum ada data!", Toast.LENGTH_SHORT).show();
            }
            else {
                finish();
                startActivity(new Intent(MainActivity.this, ListActivity.class));
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Klik sekali lagi untuk menutup aplikasi", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

}
