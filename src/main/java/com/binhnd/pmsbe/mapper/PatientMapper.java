package com.binhnd.pmsbe.mapper;


import com.binhnd.pmsbe.dto.request.PatientInfoRequest;
import com.binhnd.pmsbe.dto.request.ReceivePatientRequest;
import com.binhnd.pmsbe.dto.response.PatientAdmissionResponse;
import com.binhnd.pmsbe.dto.response.PatientResponse;
import com.binhnd.pmsbe.entity.*;
import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

public class PatientMapper {

    private PatientMapper() {
    }

    public static PatientInfoRequest toPatientRequest(ReceivePatientRequest request) {
        PatientInfoRequest dto = new PatientInfoRequest();
        dto.setPatientName(request.getPatientName());
        dto.setDob(request.getDob());
        dto.setPatientPhone(request.getPatientPhone());
        dto.setBhytCode(request.getBhytCode());
        dto.setCccdNumber(request.getCccdNumber());
        dto.setGuardianPhone(request.getGuardianPhone());

        return dto;
    }

    public static PatientResponse toDto(Patient entity) {
        PatientResponse patientResponse = new PatientResponse();
        List<PatientRoom> patientRooms = new ArrayList<>();

        patientResponse.setId(entity.getId());
        patientResponse.setPatientCode(entity.getPatientCode());
        patientResponse.setPatientName(entity.getName());
        patientResponse.setGender(entity.getGender());
        patientResponse.setDob(entity.getDateOfBirth());
        patientResponse.setAddress(entity.getAddress());
        patientResponse.setPatientPhone(entity.getPatientPhone());
        patientResponse.setGuardianPhone(entity.getGuardianPhone());
        patientResponse.setBhytCode(entity.getBhytCode());
        patientResponse.setCccdNumber(entity.getCccdNumber());

        Department department = entity.getDepartment();
        if (!ObjectUtils.isEmpty(department)) {
            patientResponse.setDepartmentId(department.getId());
            patientResponse.setDepartmentName(department.getName());
            patientRooms = department.getPatientRooms();
        }

        MedicalStaff medicalStaff = entity.getMedicalStaff();
        if (!ObjectUtils.isEmpty(medicalStaff)) {
            patientResponse.setMedicalStaffId(medicalStaff.getId());
            patientResponse.setMedicalStaffName(medicalStaff.getName());
        }

        PatientBed patientBed = entity.getPatientBed();


        if (!ObjectUtils.isEmpty(patientBed) && !ObjectUtils.isEmpty(patientRooms) && !patientRooms.isEmpty()) {
            for (PatientRoom room : patientRooms) {
                List<PatientBed> beds = room.getBeds();
                if (beds.contains(patientBed)) {
                    patientResponse.setRoomId(room.getId());
                    patientResponse.setRoomNumber(room.getRoomNumber());
                }
            }

            patientResponse.setPatientBedId(patientBed.getId());
            patientResponse.setBedNumber(patientBed.getBedNumber());
        }

        patientResponse.setStatus(entity.getStatus());
        patientResponse.setCreateTime(entity.getAdmissionTime());
        patientResponse.setCreateBy(entity.getCreateBy());
        patientResponse.setUpdateTime(entity.getUpdateTime());
        patientResponse.setUpdateBy(entity.getUpdateBy());


        return patientResponse;
    }

    public static PatientAdmissionResponse toPatientAdmissionDto(com.binhnd.pmsbe.entity.Patient entity) {

        PatientAdmissionResponse response = new PatientAdmissionResponse();

        response.setId(entity.getId());
        response.setPatientName(entity.getName());
        response.setGender(entity.getGender());
        response.setDob(entity.getDateOfBirth());
        response.setAddress(entity.getAddress());
        response.setAdmissionTime(entity.getAdmissionTime());

        com.binhnd.pmsbe.entity.MedicalRecord medicalRecord = entity.getMedicalRecord();
        if (!ObjectUtils.isEmpty(medicalRecord)) {
            response.setReason(medicalRecord.getReason());
            response.setMedicalHistory(medicalRecord.getMedicalHistory());
        }

        return response;
    }

    public static List<PatientResponse> mapAll(List<Patient> patients) {
        List<PatientResponse> responses = new ArrayList<>();
        for (Patient patient : patients) {
            responses.add(toDto(patient));
        }
        return responses;
    }

    public static List<PatientAdmissionResponse> mapAllPatientAdmission(List<com.binhnd.pmsbe.entity.Patient> patients) {
        List<PatientAdmissionResponse> responses = new ArrayList<>();
        for (Patient patient : patients) {
            responses.add(toPatientAdmissionDto(patient));
        }
        return responses;
    }

}
