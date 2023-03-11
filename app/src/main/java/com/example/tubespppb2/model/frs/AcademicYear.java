package com.example.tubespppb2.model.frs;

public class AcademicYear {
    private String nama;
    private int status;
    private String tahun;

    public AcademicYear(String nama, int status, String tahun){
        this.nama = nama;
        this.status = status;
        this.tahun = tahun;
    }


    public String getNama() {
        return nama;
    }

    public int getStatus() {
        return status;
    }
    public String getTahun(){
        return  tahun;
    }
}
