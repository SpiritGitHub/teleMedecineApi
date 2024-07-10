package com.sp.telemedecine.controller;

import com.sp.telemedecine.models.Doctor;
import com.sp.telemedecine.models.Patient;
import com.sp.telemedecine.models.User;
import com.sp.telemedecine.services.Admin.UserServiceInterface;
//import com.sp.telemedecine.services.Patient.PatientService;
import com.sp.telemedecine.services.doctor.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    @Autowired
    private UserServiceInterface userService;
    @Autowired
    private DoctorService doctorService;
//    @Autowired
//    private PatientService patientService;


    @GetMapping("/hiadmin")
    public ResponseEntity<String> sayHello(){
        return ResponseEntity.ok("Hello admin");
    }

    @GetMapping("/alluser")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/finduser/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }


    @DeleteMapping("/deleteuser/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/alldocor")
    public List<Doctor> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    @GetMapping("finddoctor/{id}")
    public Doctor getDoctorById(@PathVariable Long id) {
        return doctorService.getDoctorById(id);
    }

//    @GetMapping("/allpatient")
//    public List<Patient> getAllPatients() {
//        return patientService.getAllPatients();
//    }
//
//    @GetMapping("/find-patient/{id}")
//    public Patient getPatientById(@PathVariable Long id) {
//        return patientService.getPatientById(id);
//    }
//
//    @GetMapping("/find-patient-username/{username}")
//    public Optional<Patient> getPatientByUsername(@PathVariable String username) {
//        return patientService.findByUsername(username);
//    }
}
