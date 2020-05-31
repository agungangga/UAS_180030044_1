package com.aa183.AnggaPratama;

import java.io.Serializable;

public class ModelList implements Serializable {
    public String IdNgr;
    public String getIdNgr() {
        return IdNgr;
    }
    public void setIdNgr(String idNgr) {
        IdNgr = idNgr;
    }

    public String NmNgr;
    public String getNmNgr() {
        return NmNgr;
    }
    public void setNmNgr(String nmNgr) {
        NmNgr = nmNgr;
    }

    public String IbuKota;
    public String getIbuKota() {
        return IbuKota;
    }
    public void setIbuKota(String ibuKota) {
        IbuKota = ibuKota;
    }

    public String Bahasa;
    public String getBahasa() {
        return Bahasa;
    }
    public void setBahasa(String bahasa) {
        Bahasa = bahasa;
    }

    public String Uang;
    public String getUang() {
        return Uang;
    }
    public void setUang(String uang) {
        Uang = uang;
    }

    public String Benua;
    public String getBenua() {
        return Benua;
    }
    public void setBenua(String benua) {
        Benua = benua;
    }

    private String UPLDIMG;
    public String getUPLDIMG() {
        return UPLDIMG;
    }
    public void setUPLDIMG(String UPLDIMG) {
        this.UPLDIMG = UPLDIMG;
    }

}
