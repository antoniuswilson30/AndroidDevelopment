package com.example.tubespppb2.model.frs;

import java.util.ArrayList;
import java.util.List;

public class MakeAcademicYears {
    public List<AcademicYear> getAcademicYears(String tahunAktif, String tahunMurid){
        List<AcademicYear> arrTahunAjaran = new ArrayList<>();
        if(tahunAktif != null && tahunMurid != null){
            int tahunAktifInt = Integer.parseInt(tahunAktif);
            int tahunMuridInt = Integer.parseInt(tahunMurid);
            int tahunSementara = tahunMuridInt;
            for(int i =0; i <= 17; i++){
                String displayTahunAjaran = "Semester ";
                if(tahunSementara % 10 == 1){
                    displayTahunAjaran += "Ganjil ";
                }else if(tahunSementara%10 == 2){
                    displayTahunAjaran += "Genap ";
                }else{
                    displayTahunAjaran += "Pendek ";
                }
                displayTahunAjaran += ((tahunSementara/10) + " - "+ ((tahunSementara/10)+1) );
                if(tahunSementara < tahunAktifInt){
                    arrTahunAjaran.add(new AcademicYear(displayTahunAjaran, 2, String.valueOf(tahunSementara)));
                }else if(tahunSementara == tahunAktifInt){
                    arrTahunAjaran.add(new AcademicYear(displayTahunAjaran, 1, String.valueOf(tahunSementara)));
                }else{
                    arrTahunAjaran.add(new AcademicYear(displayTahunAjaran, 0, String.valueOf(tahunSementara)));
                }
                tahunSementara++;
                if(tahunSementara%10 == 4){
                    tahunSementara += 7;
                }
            }
        }
        return arrTahunAjaran;
    }
}
