package com.binhnd.pmsbe.services.dashboard;

import com.binhnd.pmsbe.common.constants.PMSConstants;
import com.binhnd.pmsbe.dto.response.DashboardResponse;
import com.binhnd.pmsbe.dto.response.StaffPerDepartment;
import com.binhnd.pmsbe.repositories.PatientRepository;
import com.binhnd.pmsbe.repositories.custom_repositories.CustomAccountRepository;
import com.binhnd.pmsbe.repositories.custom_repositories.CustomDepartmentRepository;
import com.binhnd.pmsbe.repositories.custom_repositories.CustomMedicalStaffRepository;
import com.binhnd.pmsbe.repositories.custom_repositories.CustomPatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final CustomPatientRepository customPatientRepository;
    private final CustomMedicalStaffRepository medicalStaffRepository;
    private final CustomDepartmentRepository departmentRepository;
    private final CustomAccountRepository accountRepository;
    private final PatientRepository patientRepository;
    private final DecimalFormat decimalFormat;

    @Autowired
    public DashboardServiceImpl(CustomPatientRepository customPatientRepository,
                                CustomMedicalStaffRepository medicalStaffRepository,
                                CustomDepartmentRepository departmentRepository,
                                CustomAccountRepository accountRepository,
                                PatientRepository patientRepository, DecimalFormat decimalFormat) {
        this.customPatientRepository = customPatientRepository;
        this.medicalStaffRepository = medicalStaffRepository;
        this.departmentRepository = departmentRepository;
        this.accountRepository = accountRepository;
        this.patientRepository = patientRepository;
        this.decimalFormat = decimalFormat;
    }

    @Override
    public DashboardResponse findInformation() {

        DashboardResponse dashboardResponse = new DashboardResponse();

        Long patientBeingTreated = patientRepository.countPatientByStatus(PMSConstants.UNDER_TREATMENT);
        Long patientBeenDischarged = patientRepository.countPatientByStatus(PMSConstants.LEAVE_HOSPITAL);
        Long countPatient = customPatientRepository.countPatient();

        dashboardResponse.setCountAccount(accountRepository.countAccount());
        dashboardResponse.setCountMedicalStaff(medicalStaffRepository.countMedicalStaff());
        dashboardResponse.setCountDepartment(departmentRepository.countDepartment());
        dashboardResponse.setCountPatient(countPatient);
        dashboardResponse.setPatientBeingTreated(patientBeingTreated);
        dashboardResponse.setPatientBeenDischarged(patientBeenDischarged);

        double percentBeingTreated = (double) patientBeingTreated / countPatient * 100;
        double percentBeenDischarged = (double) patientBeenDischarged / countPatient * 100;


        dashboardResponse.setPercentBeingTreated(Double.parseDouble(decimalFormat.format(percentBeingTreated)));
        dashboardResponse.setPercentBeenDischarged(Double.parseDouble(decimalFormat.format(percentBeenDischarged)));

        List<StaffPerDepartment> staffPerDepartments = medicalStaffRepository.countMedicalStaffPerDepartment();
        dashboardResponse.setStaffPerDepartments(staffPerDepartments);

        return dashboardResponse;
    }

    @Override
    public StaffPerDepartment findStaffPerDepartment() {
        return null;
    }
}
