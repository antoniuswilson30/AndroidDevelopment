package com.example.tubespppb2;

import com.example.tubespppb2.pojo.AcademicYearGetResponse;
import com.example.tubespppb2.pojo.AnnouncementGetDaftarResponse;
import com.example.tubespppb2.pojo.AnnouncementGetResponse;
import com.example.tubespppb2.pojo.AnnouncementPostRequest;
import com.example.tubespppb2.pojo.AnnouncementPostResponse;
import com.example.tubespppb2.pojo.AppointmentDeleteResponse;
import com.example.tubespppb2.pojo.AppointmentGetDaftarOwnerAndParticipantResponse;
import com.example.tubespppb2.pojo.AppointmentGetDaftarPesertaResponse;
import com.example.tubespppb2.pojo.AppointmentGetDaftarUndanganResponse;
import com.example.tubespppb2.pojo.AppointmentPatchMengubahAppointmentRequest;
import com.example.tubespppb2.pojo.AppointmentPatchMengubahAppointmentResponse;
import com.example.tubespppb2.pojo.AppointmentPostPesertaRequest;
import com.example.tubespppb2.pojo.AppointmentPostPesertaResponse;
import com.example.tubespppb2.pojo.AppointmentPostRequest;
import com.example.tubespppb2.pojo.AppointmentPostResponse;
import com.example.tubespppb2.pojo.AuthenticatePostRequest;
import com.example.tubespppb2.pojo.AuthenticatePostResponse;
import com.example.tubespppb2.pojo.CoursesGetDaftarResponse;
import com.example.tubespppb2.pojo.EnrolledCourseGetResponse;
import com.example.tubespppb2.pojo.EnrollmentDeleteResponse;
import com.example.tubespppb2.pojo.EnrollmentPostRequest;
import com.example.tubespppb2.pojo.EnrollmentPostResponse;
import com.example.tubespppb2.pojo.LecturerDeleteTimeSlotsResponse;
import com.example.tubespppb2.pojo.LecturerGetTimeSlotsResponse;
import com.example.tubespppb2.pojo.LecturerPostTimeSlotsRequest;
import com.example.tubespppb2.pojo.LecturerPostTimeSlotsResponse;
import com.example.tubespppb2.pojo.PrasyaratGetResponse;
import com.example.tubespppb2.pojo.StudentByIdGetResponse;
import com.example.tubespppb2.pojo.TagGetResponse;
import com.example.tubespppb2.pojo.UsersGetByEmailResponse;
import com.example.tubespppb2.pojo.UsersGetSelfResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {
    //endpoint authenticate
    //err E_AUTH_FAILED
    @POST("authenticate")
    Call<AuthenticatePostResponse> getUserToken(@Body AuthenticatePostRequest authenticatePostRequest);

    //endpoint users
    //err E_NOT_EXIST
    @GET("users/email/{email}")
    Call<UsersGetByEmailResponse> getUserByEmail(@Header("Authorization") String token, @Path("email") String email);

    //err E_NOT_EXIST
    @GET("users/self")
    Call<UsersGetSelfResponse> getUsersSelf(@Header("Authorization") String token);

    //endpoint tag
    @GET("tags")
    Call<List<TagGetResponse>> getTag(@Header("Authorization") String token);

    //endpoint announcement
    @GET("announcements/{announcement_id}")
    Call<AnnouncementGetResponse> getAnnouncementDetail(@Header("Authorization") String token,@Path("announcement_id") String announcement_id);

    @GET("announcements")
    Call<AnnouncementGetDaftarResponse> getDaftarAnnoucement(@Header("Authorization") String token, @Query("filter[title]") String title, @Query("filter[tags][]") List<String> tags, @Query("cursor")String cursor, @Query("limit") String limit);

    @POST("announcements")
    Call<AnnouncementPostResponse> postAnnouncement(@Header("Authorization") String token, @Body AnnouncementPostRequest announcementPostRequest);

    //endpoint lecturer time slots
    //err E_OVERLAPPING_SCHEDULE
    @POST("lecturer-time-slots")
    Call<LecturerPostTimeSlotsResponse> postLecturerTimeSlots(@Header("Authorization") String token, @Body LecturerPostTimeSlotsRequest lecturerPostTimeSlotsRequest);

    //err E_UNAUTHORIZED
    @DELETE("lecturer-time-slots/{time_slots_id}")
    Call<LecturerDeleteTimeSlotsResponse> deleteLecturerTimeSlots(@Header("Authorization") String token, @Path("time_slots_id") String timeSlotsId);

    @GET("lecturer-time-slots/lecturers/{lecturer_id}")
    Call<List<LecturerGetTimeSlotsResponse>> getLecturerTimeSlots(@Header("Authorization") String token, @Path("lecturer_id") String lecturerId);

    //endpoint appointment
    //err E_OVERLAPPING_SCHEDULE
    //err E_NOT_EXIST
    @POST("appointments")
    Call<AppointmentPostResponse> postAppointment(@Header("Authorization") String token, @Body AppointmentPostRequest appointmentPostRequest);

    //err E_NOT_EXIST
    //err E_UNAUTHORIZED
    @DELETE("appointments/{appointment_id}")
    Call<AppointmentDeleteResponse> deleteAppointment(@Header("Authorization") String token,@Path("appointment_id") String appointmentId);

    //err E_INVALID_VALUE
    @GET("appointments/start-date/{start-date}/end-date/{end-date}")
    Call<List<AppointmentGetDaftarOwnerAndParticipantResponse>> getDaftarAppointmentOwnerParticipant(@Header("Authorization") String token,@Path("start-date")String startDate, @Path("end-date")String endDate);

    //err E_NOT_EXIST : appointment tidak ditemukan
    //err E_NOT_EXIST : pengguna yang diundang tidak ditemukan
    //err E_EXIST       :   pengguna sudah pernah diundang
    //err E_UNAUTHORIZED
    @POST("appointments/{appointment_id}/participants")
    Call<List<AppointmentPostPesertaResponse>> postPesertaAppointment(@Header("Authorization") String token, @Path("appointment_id")String appointmentId, @Body AppointmentPostPesertaRequest appointmentPostPesertaRequest);

    //untuk mengubah status kehadiran (RSVP)
    //err E_NOT_EXIST : pengguna tidak ditemukan
    //err E_UNAUTHORIZED
    //err E_OVERLAPPING_SCHEDULE
    @PATCH("appointments/{appointment_id}/participants/{participant_id}")
    Call<AppointmentPatchMengubahAppointmentResponse> patchUbahAppointment(@Header("Authorization") String token, @Path("appointment_id")String appointmentId, @Path("participant_id") String participantId, @Body AppointmentPatchMengubahAppointmentRequest appointmentPatchMengubahAppointmentRequest);

    //untuk melihat daftar peserta pada sebuah appointments
    //err E_NOT_EXIST
    @GET("appointments/{appointment_id}/participants")
    Call<List<AppointmentGetDaftarPesertaResponse>> getDaftarPesertaAppointment(@Header("Authorization") String token, @Path("appointment_id")String appointmentId);

    @GET("appointments/invitations")
    Call<List<AppointmentGetDaftarUndanganResponse>> getDaftarUndanganAppointment(@Header("Authorization") String token);


    //endpoint academic-years
    //err E_INVALID_SETTINGS : salah konfigurasi server
    @GET("academic-years")
    Call<AcademicYearGetResponse> getActiveAcademicYear(@Header("Authorization") String token);

    //endpoint courses
    //filter code and name
    @GET("courses")
    Call<List<CoursesGetDaftarResponse>> getCourses(@Header("Authorization") String token,@Query("filter[name]")String name);

    //melihat daftar prasyarat mata kuliah
    @GET("courses/{course_id}/prerequisites")
    Call<List<PrasyaratGetResponse>> getPrasyaratMatkul(@Header("Authorization") String token, @Path("course_id") String id);

    //endpoint student
    //melihat student berdasarkan id
    @GET("students/id/{id}")
    Call<StudentByIdGetResponse> getStudentById(@Header("Authorization") String token, @Path("id")String studentId);

    //endpoint enrollment
    //melihat daftar enrollment
    @GET("enrolments/academic-years/{academic_year}")
    Call<List<EnrolledCourseGetResponse>> getEnrolledCourse(@Header("Authorization") String token, @Path("academic_year")String academic_year);

    //endpoint enrollment
    //menambakan enrollment
    @POST("enrolments")
    Call<EnrollmentPostResponse> postEnrollment(@Header("Authorization") String token, @Body EnrollmentPostRequest enrollmentPostRequest);

    //endpoint enrollment
    //menghapus enrollment
    @DELETE("enrolments/{course_id}/academic-years/{academic_year}")
    Call<EnrollmentDeleteResponse> deleteEnrollment(@Header("Authorization") String token, @Path("course_id") String id, @Path("academic_year") int tahun);
}
