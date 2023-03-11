package com.example.tubespppb2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import com.example.tubespppb2.databinding.ActivityMainBinding;
import com.example.tubespppb2.pojo.AnnouncementGetDaftarResponse;
import com.example.tubespppb2.pojo.AnnouncementGetResponse;
import com.example.tubespppb2.pojo.AppointmentGetDaftarOwnerAndParticipantResponse;
import com.example.tubespppb2.pojo.AppointmentGetDaftarPesertaResponse;
import com.example.tubespppb2.pojo.AppointmentGetDaftarUndanganResponse;
import com.example.tubespppb2.pojo.CoursesGetDaftarResponse;
import com.example.tubespppb2.pojo.EnrolledCourseGetResponse;
import com.example.tubespppb2.pojo.EnrollmentPostResponse;
import com.example.tubespppb2.pojo.EnrollmentReason;
import com.example.tubespppb2.pojo.LecturerGetTimeSlotsResponse;
import com.example.tubespppb2.pojo.PrasyaratGetResponse;
import com.example.tubespppb2.pojo.TagGetResponse;
import com.example.tubespppb2.pojo.UsersGetByEmailResponse;
import com.example.tubespppb2.presenter.PresenterFRS;
import com.example.tubespppb2.presenter.PresenterMainActivityAccountPage;
import com.example.tubespppb2.presenter.PresenterMainActivityAnnouncement;
import com.example.tubespppb2.presenter.PresenterMainActivityAppointment;
import com.example.tubespppb2.presenter.PresenterMainActivityTimeSlot;
import com.example.tubespppb2.service.SharedPreferenceStore;
import com.example.tubespppb2.view.accountPage.PageAccountFragment;
import com.example.tubespppb2.view.announcement.HalamanPengumumanFragment;
import com.example.tubespppb2.view.announcement.IAnnouncement;
import com.example.tubespppb2.view.announcement.ITag;
import com.example.tubespppb2.view.appointment.DaftarPertemuanFragment;
import com.example.tubespppb2.view.appointment.IAppointment;
import com.example.tubespppb2.view.appointment.IAppointmentDetailPeserta;
import com.example.tubespppb2.view.appointment.IAppointmentJadwalDosen;
import com.example.tubespppb2.view.appointment.IUndanganAppointment;
import com.example.tubespppb2.view.appointment.UndanganPertemuanFragment;
import com.example.tubespppb2.view.frsPage.HalamanFRSFragment;
import com.example.tubespppb2.view.frsPage.IEnrolledCourse;
import com.example.tubespppb2.view.frsPage.IMatKul;
import com.example.tubespppb2.view.frsPage.IYear;
import com.example.tubespppb2.view.timeSlot.ITimeSlot;
import com.example.tubespppb2.view.timeSlot.TimeSlotFragment;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IError, IAnnouncement, ITag, IAppointment, IEnrolledCourse, IYear, IMatKul, IAppointmentJadwalDosen, IUndanganAppointment, ITimeSlot, IAppointmentDetailPeserta {

    private ActivityMainBinding binding;
    private Toolbar toolbar;
    private HashMap<String, Fragment> mp;
    private FragmentManager fm;
    private PresenterMainActivityAccountPage presenterMainActivityAccountPage;
    private PresenterMainActivityAnnouncement presenterMainActivityAnnouncement;
    private PresenterMainActivityAppointment presenterMainActivityAppointment;
    private PresenterMainActivityTimeSlot presenterMainActivityTimeSlot;
    private PresenterFRS presenterFRS;
    private SharedPreferenceStore spStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityMainBinding.inflate(getLayoutInflater());
        View activity_main_view = this.binding.getRoot();
        setContentView(activity_main_view);

        //initialization
        this.mp = new HashMap<>();
        this.spStore = new SharedPreferenceStore(getApplicationContext());
        this.presenterMainActivityAccountPage = new PresenterMainActivityAccountPage(getApplicationContext(), this);
        this.presenterMainActivityAccountPage.setGetUsersSelf();
        this.fm = this.getSupportFragmentManager();
        this.presenterFRS = new PresenterFRS(getApplicationContext(), this, this, this, this);
        this.presenterMainActivityAnnouncement = new PresenterMainActivityAnnouncement(getApplicationContext(), this, this, this);
        this.presenterMainActivityTimeSlot = new PresenterMainActivityTimeSlot(getApplicationContext(), this, this);
        this.presenterMainActivityAppointment = new PresenterMainActivityAppointment(getApplicationContext(), this, this, this, this, this);

        //set toolbar
        this.toolbar = this.binding.toolbar;
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);

        //tombol garis tiga
        ActionBarDrawerToggle abdt = new ActionBarDrawerToggle(this, this.binding.drawerLayout, this.toolbar, R.string.open_drawer, R.string.close_drawer);
        this.binding.drawerLayout.addDrawerListener(abdt);

        abdt.syncState();

        //memasukkan fragment ke dalam hashmap
        HalamanPengumumanFragment halamanPengumumanFragment = HalamanPengumumanFragment.newInstance(this.presenterMainActivityAnnouncement);
        this.mp.put("HalamanPengumumanFragment", halamanPengumumanFragment);
        this.fm = this.getSupportFragmentManager();
        FragmentTransaction ft = this.fm.beginTransaction();
        ft.add(this.binding.fragmentContainer.getId(), this.mp.get("HalamanPengumumanFragment"), "2")
                .commit();

        DaftarPertemuanFragment daftarPertemuanFragment = DaftarPertemuanFragment.newInstance(this.presenterMainActivityAppointment, this.fm, this);
        this.mp.put("DaftarPertemuanFragment", daftarPertemuanFragment);

        UndanganPertemuanFragment undanganPertemuanFragment = UndanganPertemuanFragment.newInstance(this.presenterMainActivityAppointment, this);
        this.mp.put("UndanganPertemuanFragment", undanganPertemuanFragment);

        TimeSlotFragment timeSlotFragment = TimeSlotFragment.newInstance(this.presenterMainActivityTimeSlot, this, this.fm);
        this.mp.put("TimeSlotFragment", timeSlotFragment);

        PageAccountFragment pageAccountFragment = PageAccountFragment.newInstance(this.presenterMainActivityAccountPage);
        this.mp.put("PageAccountFragment", pageAccountFragment);

        HalamanFRSFragment halamanFRSFragment = HalamanFRSFragment.newInstance(presenterFRS);
        this.mp.put("HalamanFRSFragment", halamanFRSFragment);

        //change page
        this.fm.setFragmentResultListener("changePage", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                int page = result.getInt("page");
                changePage(page);
            }
        });

    }

    private void changePage(int page) {
        this.binding.drawerLayout.closeDrawers();
        this.hideKeyboard(this);
        if (page == 1) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
        FragmentTransaction ft = this.fm.beginTransaction();
        if (page == 2) {
            this.fm.popBackStack();
            ft.replace(this.binding.fragmentContainer.getId(), this.mp.get("HalamanPengumumanFragment"));
        } else if (page == 3) {
            this.fm.popBackStack();
            ft.replace(this.binding.fragmentContainer.getId(), this.mp.get("DaftarPertemuanFragment"))
                    .addToBackStack("3");
        } else if (page == 4) {
            this.fm.popBackStack();
            ft.replace(this.binding.fragmentContainer.getId(), this.mp.get("UndanganPertemuanFragment"))
                    .addToBackStack("4");
        } else if (page == 5) {
            this.fm.popBackStack();
            ft.replace(this.binding.fragmentContainer.getId(), this.mp.get("HalamanFRSFragment"))
                    .addToBackStack("5");
        } else if (page == 6) {
            this.fm.popBackStack();
            ft.replace(this.binding.fragmentContainer.getId(), this.mp.get("PageAccountFragment"))
                    .addToBackStack("6");
        } else if (page == 7) {
            this.fm.popBackStack();
            ft.replace(this.binding.fragmentContainer.getId(), this.mp.get("TimeSlotFragment"))
                    .addToBackStack("7");
        }
        ft.setReorderingAllowed(true);
        ft.commit();
    }

    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void showError(String msg) {
        Context context = getApplicationContext();
        CharSequence text = msg;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    @Override
    public void addData(AnnouncementGetDaftarResponse data) {
        HalamanPengumumanFragment fragment = (HalamanPengumumanFragment) this.mp.get("HalamanPengumumanFragment");
        fragment.addData(data);
    }

    @Override
    public void showDetail(AnnouncementGetResponse data) {
        HalamanPengumumanFragment fragment = (HalamanPengumumanFragment) this.mp.get("HalamanPengumumanFragment");
        fragment.showDetail(data);
    }

    @Override
    public void makeAnnouncementSuccess() {
        HalamanPengumumanFragment fragment = (HalamanPengumumanFragment) this.mp.get("HalamanPengumumanFragment");
        fragment.makeAnnouncementSuccess();
    }

    @Override
    public void loadTag(List<TagGetResponse> data) {
        HalamanPengumumanFragment fragment = (HalamanPengumumanFragment) this.mp.get("HalamanPengumumanFragment");
        fragment.loadTag(data);
    }

    @Override
    public void addData(List<AppointmentGetDaftarOwnerAndParticipantResponse> data) {
        DaftarPertemuanFragment fragment = (DaftarPertemuanFragment) this.mp.get("DaftarPertemuanFragment");
        fragment.addData(data);
    }

    @Override
    public void loadEnrolment(List<EnrolledCourseGetResponse> data) {
        HalamanFRSFragment fragment = (HalamanFRSFragment) this.mp.get("HalamanFRSFragment");
        fragment.loadEnrolment(data);
    }

    @Override
    public void deleteEnrolmentSuccess() {
        HalamanFRSFragment fragment = (HalamanFRSFragment) this.mp.get("HalamanFRSFragment");
        fragment.deleteEnrolmentSuccess();
    }

    @Override
    public void loadActiveYear(String year) {
        HalamanFRSFragment fragment = (HalamanFRSFragment) this.mp.get("HalamanFRSFragment");
        fragment.loadActiveYear(year);
    }

    @Override
    public void loadStudentYear(String year) {
        HalamanFRSFragment fragment = (HalamanFRSFragment) this.mp.get("HalamanFRSFragment");
        fragment.loadStudentYear(year);
    }

    @Override
    public void loadPrasyarat(List<PrasyaratGetResponse> data, int choice) {
        HalamanFRSFragment fragment = (HalamanFRSFragment) this.mp.get("HalamanFRSFragment");
        fragment.loadPrasyarat(data, choice);
    }

    @Override
    public void addEnrollment(EnrollmentPostResponse data, String nama) {
        HalamanFRSFragment fragment = (HalamanFRSFragment) this.mp.get("HalamanFRSFragment");
        fragment.addEnrollment(data, nama);
    }

    @Override
    public void showPrasyaratGagal(List<EnrollmentReason> dataErr) {
        HalamanFRSFragment fragment = (HalamanFRSFragment) this.mp.get("HalamanFRSFragment");
        fragment.showPrasyaratGagal(dataErr);
    }

    @Override
    public void getCourseSuccess(List<CoursesGetDaftarResponse> data, int choice) {
        HalamanFRSFragment fragment = (HalamanFRSFragment) this.mp.get("HalamanFRSFragment");
        fragment.getCourseSuccess(data, choice);
    }

    public void addAppointmentSuccess(String appointmentId) {
        DaftarPertemuanFragment fragment = (DaftarPertemuanFragment) this.mp.get("DaftarPertemuanFragment");
        fragment.addAppointmentSuccess(appointmentId);
    }

    @Override
    public void emailExist(UsersGetByEmailResponse data) {
        DaftarPertemuanFragment fragment = (DaftarPertemuanFragment) this.mp.get("DaftarPertemuanFragment");
        fragment.emailExist(data);
    }

    @Override
    public void getLecturerId(UsersGetByEmailResponse data) {
        DaftarPertemuanFragment fragment = (DaftarPertemuanFragment) this.mp.get("DaftarPertemuanFragment");
        fragment.getLecturerId(data);
    }

    @Override
    public void addParticipantsSuccess() {
        DaftarPertemuanFragment fragment = (DaftarPertemuanFragment) this.mp.get("DaftarPertemuanFragment");
        fragment.addParticipantsSuccess();
    }

    @Override
    public void deleteAppointmentSuccess() {
        DaftarPertemuanFragment fragment = (DaftarPertemuanFragment) this.mp.get("DaftarPertemuanFragment");
        fragment.deleteAppointmentSuccess();
    }

    @Override
    public void loadDaftarPesertaDetail(List<AppointmentGetDaftarPesertaResponse> data) {
        DaftarPertemuanFragment fragment = (DaftarPertemuanFragment) this.mp.get("DaftarPertemuanFragment");
        fragment.loadDaftarPesertaDetail(data);
    }

    @Override
    public void getDaftarUndanganSuccess(List<AppointmentGetDaftarUndanganResponse> data) {
        UndanganPertemuanFragment fragment = (UndanganPertemuanFragment) this.mp.get("UndanganPertemuanFragment");
        fragment.getDaftarUndanganSuccess(data);
    }

    @Override
    public void patchAppointmentRSVPSuccess() {
        UndanganPertemuanFragment fragment = (UndanganPertemuanFragment) this.mp.get("UndanganPertemuanFragment");
        fragment.patchAppointmentRSVPSuccess();
    }

    @Override
    public void loadLecturerTimeSlots(List<LecturerGetTimeSlotsResponse> data) {
        DaftarPertemuanFragment fragment = (DaftarPertemuanFragment) this.mp.get("DaftarPertemuanFragment");
        fragment.loadLecturerTimeSlots(data);
    }

    @Override
    public void loadTimeSlots(List<LecturerGetTimeSlotsResponse> data) {
        TimeSlotFragment fragment = (TimeSlotFragment) this.mp.get("TimeSlotFragment");
        fragment.loadTimeSlots(data);
    }

    @Override
    public void deleteTimeSlotSuccess() {
        TimeSlotFragment fragment = (TimeSlotFragment) this.mp.get("TimeSlotFragment");
        fragment.deleteTimeSlotSuccess();
    }

    @Override
    public void postTimeSlotSuccess() {
        TimeSlotFragment fragment = (TimeSlotFragment) this.mp.get("TimeSlotFragment");
        fragment.postTimeSlotSuccess();
    }
}