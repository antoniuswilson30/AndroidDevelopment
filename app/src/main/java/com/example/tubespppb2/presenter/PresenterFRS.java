package com.example.tubespppb2.presenter;

import android.content.Context;

import com.example.tubespppb2.IError;
import com.example.tubespppb2.exception.InvalidFieldException;
import com.example.tubespppb2.model.account_auth.GetUsersSelf;
import com.example.tubespppb2.model.frs.AcademicYear;
import com.example.tubespppb2.model.frs.DeleteEnrolment;
import com.example.tubespppb2.model.frs.GetActiveAcademicYears;
import com.example.tubespppb2.model.frs.GetCourses;
import com.example.tubespppb2.model.frs.GetEnrollment;
import com.example.tubespppb2.model.frs.GetPrasyaratMatkul;
import com.example.tubespppb2.model.frs.GetStudentYearById;
import com.example.tubespppb2.model.frs.MakeAcademicYears;
import com.example.tubespppb2.model.frs.PostEnrollment;
import com.example.tubespppb2.view.frsPage.IEnrolledCourse;
import com.example.tubespppb2.view.frsPage.IMatKul;
import com.example.tubespppb2.view.frsPage.IYear;

import java.util.List;

public class PresenterFRS {
    private GetActiveAcademicYears academicYears;
    private GetStudentYearById studentYear;
    private IError iError;
    private Context context;
    private IEnrolledCourse iEnrolledCourse;
    private IYear iYear;
    private GetUsersSelf getUsersSelf;
    private MakeAcademicYears makeAcademicYears;
    private GetEnrollment getEnrollment;
    private IMatKul iMatKul;
    private PostEnrollment postEnrollment;
    private GetPrasyaratMatkul getPrasyaratMatkul;
    private DeleteEnrolment deleteEnrolment;
    private GetCourses getCourses;
    public PresenterFRS(Context context, IError iError, IEnrolledCourse iEnrolledCourse, IYear iYear, IMatKul iMatKul){
        this.context = context;
        this.iError = iError;
        this.iYear = iYear;
        this.academicYears = new GetActiveAcademicYears(context, iError, iYear);
        this.studentYear = new GetStudentYearById(context,iError, iYear);
        this.makeAcademicYears  = new MakeAcademicYears();
        this.iEnrolledCourse = iEnrolledCourse;
        this.getEnrollment = new GetEnrollment(context, iError, iEnrolledCourse);
        this.getPrasyaratMatkul = new GetPrasyaratMatkul(context, iError, iMatKul);
        this.postEnrollment = new PostEnrollment(context, iError, iMatKul);
        this.deleteEnrolment = new DeleteEnrolment(context,iError,iEnrolledCourse);
        this.getCourses = new GetCourses(context,iError,iMatKul);
    }

    public void getStudentInitialYear(){
        studentYear.execute();
    }

    public void getActiveAcademicYear(){
        academicYears.execute();
    }

    public List<AcademicYear> getAcademicYears(String tahunAktif, String tahunMurid){
        return this.makeAcademicYears.getAcademicYears(tahunAktif,tahunMurid);
    }

    public void loadEnrolment(String year){
        this.getEnrollment.execute(year);
    }

    public void getPrasyarat(String courseId, int choice){
        this.getPrasyaratMatkul.execute(courseId,choice);
    }

    public void postEnrollment(String courseId, int academic_year, String nama){
        this.postEnrollment.execute(courseId,academic_year,nama);
    }

    public void getCourses(String namaMatkul, int choice){
        try {
            this.getCourses.execute(namaMatkul, choice);
        } catch (InvalidFieldException e) {
            this.iError.showError(e.getMessage());
        }
    }

    public void deleteEnrolment(String courseId, int academic_years){
        this.deleteEnrolment.execute(courseId,academic_years);
    }

    public void showError(String msg){
        this.iError.showError(msg);
    }
}
