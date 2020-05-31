package com.aa183.AnggaPratama;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqlHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "negara.db";
    private static final int DATABASE_VERSION = 1;

    public SqlHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String crtNegara = "CREATE TABLE IF NOT EXISTS tbNegara " +
                "(ID_NEGARA INTEGER PRIMARY KEY AUTOINCREMENT, NM_NEGARA VARCHAR(500) NOT NULL, " +
                "IBUKOTA VARCHAR(500) NOT NULL, BAHASA VARCHAR(500) NOT NULL, UANG VARCHAR(500) NOT NULL, "+
                "BENUA VARCHAR(500) NOT NULL, UPLDIMG BYTE NOT NULL);";
        db.execSQL(crtNegara);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public String cekEmpty() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor xEmpty = db.rawQuery("SELECT ID_NEGARA FROM tbNegara ", null);
        StringBuffer buffer = new StringBuffer();
        while (xEmpty.moveToNext()) {
            String xEmp = xEmpty.getString(xEmpty.getColumnIndex("ID_NEGARA"));
            buffer.append(xEmp + "#");
        }
        db.close();
        return buffer.toString();
    }

    public String cekData(String xNm) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor xData = db.rawQuery("SELECT ID_NEGARA FROM tbNegara " +
                "WHERE NM_NEGARA = '" + xNm + "' ", null);
        StringBuffer buffer = new StringBuffer();
        while (xData.moveToNext()) {
            String xVAR = xData.getString(xData.getColumnIndex("ID_NEGARA"));
            buffer.append(xVAR + "#");
        }
        db.close();
        return buffer.toString();
    }

    public String UpdateData(String iuQuery) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL(iuQuery);
            VariableGlobal.varSqlHelper = "YA";
        } catch (Exception e) {
            VariableGlobal.varSqlHelper = "NO";
        }
        db.close();
        return VariableGlobal.varSqlHelper;
    }

    public String DeleteData(String dQuery) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL(dQuery);
            VariableGlobal.varSqlHelper = "YA";
        } catch (Exception e) {
            VariableGlobal.varSqlHelper = "NO";
        }
        db.close();
        return VariableGlobal.varSqlHelper;
    }

    public long InsertImg(String xNm, String xIbu, String xBhs, String xUang, String xBenua, byte[] xUpldImg) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NM_NEGARA", xNm);
        contentValues.put("IBUKOTA", xIbu);
        contentValues.put("BAHASA", xBhs);
        contentValues.put("UANG", xUang);
        contentValues.put("BENUA", xBenua);
        contentValues.put("UPLDIMG", xUpldImg);
        long id = db.insert("tbNegara", null, contentValues);
        if (id >= 1)
            VariableGlobal.varSqlHelper = "YA";
        else
            VariableGlobal.varSqlHelper = "NO";
        return id;
    }

}
