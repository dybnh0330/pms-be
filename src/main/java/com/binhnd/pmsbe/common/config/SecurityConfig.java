package com.binhnd.pmsbe.common.config;


import com.binhnd.pmsbe.common.constants.PMSConstants;
import com.binhnd.pmsbe.common.constants.RequestAction;
import com.binhnd.pmsbe.common.constants.RoleName;
import com.binhnd.pmsbe.common.filter.JwtFilter;
import com.binhnd.pmsbe.services.authentication.impl.AuthenticationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    public static final String S_S_S = "%s%s%s/**";
    public static final String S_S = "%s%s/**";

    private final JwtFilter jwtFilter;

    @Autowired
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http = http.cors()
                .and()
                .csrf(AbstractHttpConfigurer::disable);

        http = http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and();

        http = http.exceptionHandling()
                .authenticationEntryPoint(
                        ((request, response, authException) -> response.sendError(
                                HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage()
                        ))
                ).and();

        http.authorizeHttpRequests()
                .antMatchers("/swagger/**").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/v3/api-docs/**").permitAll()
                .antMatchers(String.format(S_S, PMSConstants.PREFIX_URL, RequestAction.Authentication.LOGIN)).permitAll()
                .antMatchers(String.format(S_S, PMSConstants.PREFIX_URL, RequestAction.Authentication.REFRESH_TOKEN)).permitAll()
                .antMatchers(String.format(S_S, PMSConstants.PREFIX_URL, RequestAction.DASHBOARD)).authenticated()

                //Author category
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.CATEGORY, RequestAction.Category.CREATE_CATEGORY))
                .hasAuthority(RoleName.ROLE_ADMIN)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.CATEGORY, RequestAction.Category.UPDATE_CATEGORY))
                .hasAuthority(RoleName.ROLE_ADMIN)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.CATEGORY, RequestAction.Category.DELETE_CATEGORY))
                .hasAuthority(RoleName.ROLE_ADMIN)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.CATEGORY, RequestAction.Category.FIND_BY_ID))
                .hasAuthority(RoleName.ROLE_ADMIN)

                //Author medical staff
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.MEDICAL_STAFF, RequestAction.MedicalStaff.CREATE_MEDICAL_STAFF))
                .hasAuthority(RoleName.ROLE_ADMIN)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.MEDICAL_STAFF, RequestAction.MedicalStaff.UPDATE_MEDICAL_STAFF))
                .hasAuthority(RoleName.ROLE_ADMIN)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.MEDICAL_STAFF, RequestAction.MedicalStaff.DELETE_MEDICAL_STAFF))
                .hasAuthority(RoleName.ROLE_ADMIN)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.MEDICAL_STAFF, RequestAction.MedicalStaff.FIND_BY_ID))
                .hasAuthority(RoleName.ROLE_ADMIN)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.MEDICAL_STAFF, RequestAction.MedicalStaff.FIND_ALL))
                .hasAnyAuthority(RoleName.ROLE_ADMIN, RoleName.ROLE_NURSE, RoleName.ROLE_SPECIALIST, RoleName.ROLE_EXAM_DOCTOR)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.MEDICAL_STAFF, RequestAction.MedicalStaff.FIND_ALL_PAGE))
                .hasAnyAuthority(RoleName.ROLE_ADMIN, RoleName.ROLE_NURSE, RoleName.ROLE_SPECIALIST, RoleName.ROLE_EXAM_DOCTOR)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.MEDICAL_STAFF, RequestAction.MedicalStaff.FIND_ALL_DOCTOR))
                .hasAnyAuthority(RoleName.ROLE_ADMIN, RoleName.ROLE_NURSE, RoleName.ROLE_SPECIALIST, RoleName.ROLE_EXAM_DOCTOR)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.MEDICAL_STAFF, RequestAction.MedicalStaff.DOWNLOAD_EXCEL))
                .hasAnyAuthority(RoleName.ROLE_ADMIN)


                //Author account
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.ACCOUNT, RequestAction.Account.CREATE_ACCOUNT))
                .hasAuthority(RoleName.ROLE_ADMIN)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.ACCOUNT, RequestAction.Account.UPDATE_ACCOUNT))
                .hasAuthority(RoleName.ROLE_ADMIN)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.ACCOUNT, RequestAction.Account.DELETE_ACCOUNT))
                .hasAuthority(RoleName.ROLE_ADMIN)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.ACCOUNT, RequestAction.Account.FIND_BY_ID))
                .hasAuthority(RoleName.ROLE_ADMIN)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.ACCOUNT, RequestAction.Account.RESET_PASSWORD))
                .hasAuthority(RoleName.ROLE_ADMIN)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.ACCOUNT, RequestAction.Account.UNLOCK))
                .hasAuthority(RoleName.ROLE_ADMIN)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.ACCOUNT, RequestAction.Account.FIND_ALL_PAGE))
                .hasAuthority(RoleName.ROLE_ADMIN)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.ACCOUNT, RequestAction.Account.FIND_ALL))
                .hasAuthority(RoleName.ROLE_ADMIN)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.ROLE, RequestAction.Role.FIND_ALL))
                .hasAuthority(RoleName.ROLE_ADMIN)

                //Author department
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.DEPARTMENT, RequestAction.Department.CREATE_DEPARTMENT))
                .hasAuthority(RoleName.ROLE_ADMIN)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.DEPARTMENT, RequestAction.Department.UPDATE_DEPARTMENT))
                .hasAuthority(RoleName.ROLE_ADMIN)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.DEPARTMENT, RequestAction.Department.DELETE_DEPARTMENT))
                .hasAuthority(RoleName.ROLE_ADMIN)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.DEPARTMENT, RequestAction.Department.FIND_BY_ID))
                .hasAuthority(RoleName.ROLE_ADMIN)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.DEPARTMENT, RequestAction.Department.FIND_ALL))
                .hasAnyAuthority(RoleName.ROLE_ADMIN, RoleName.ROLE_EXAM_DOCTOR, RoleName.ROLE_SPECIALIST, RoleName.ROLE_NURSE)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.DEPARTMENT, RequestAction.Department.FIND_ALL_PAGE))
                .hasAnyAuthority(RoleName.ROLE_ADMIN, RoleName.ROLE_EXAM_DOCTOR, RoleName.ROLE_SPECIALIST, RoleName.ROLE_NURSE)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.DEPARTMENT, RequestAction.Department.DOWNLOAD_EXCEL))
                .hasAnyAuthority(RoleName.ROLE_ADMIN)

                //Author patient room
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.PATIENT_ROOM, RequestAction.PatientRoom.CREATE_ROOM))
                .hasAuthority(RoleName.ROLE_ADMIN)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.PATIENT_ROOM, RequestAction.PatientRoom.UPDATE_ROOM))
                .hasAuthority(RoleName.ROLE_ADMIN)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.PATIENT_ROOM, RequestAction.PatientRoom.DELETE_ROOM))
                .hasAuthority(RoleName.ROLE_ADMIN)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.PATIENT_ROOM, RequestAction.PatientRoom.FIND_BY_ID))
                .hasAnyAuthority(RoleName.ROLE_ADMIN, RoleName.ROLE_SPECIALIST, RoleName.ROLE_NURSE)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.PATIENT_ROOM, RequestAction.PatientRoom.FIND_BY_DEPARTMENT_PAGE))
                .hasAnyAuthority(RoleName.ROLE_ADMIN, RoleName.ROLE_SPECIALIST, RoleName.ROLE_NURSE)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.PATIENT_ROOM, RequestAction.PatientRoom.FIND_BY_DEPARTMENT))
                .hasAnyAuthority(RoleName.ROLE_ADMIN, RoleName.ROLE_SPECIALIST, RoleName.ROLE_NURSE)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.PATIENT_ROOM, RequestAction.PatientRoom.FIND_ROOM_EMPTY_BY_DEPARTMENT))
                .hasAnyAuthority(RoleName.ROLE_ADMIN, RoleName.ROLE_SPECIALIST, RoleName.ROLE_NURSE)

                //Author patient bed
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.PATIENT_BED, RequestAction.PatientBed.CREATE_BED))
                .hasAuthority(RoleName.ROLE_ADMIN)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.PATIENT_BED, RequestAction.PatientBed.UPDATE_BED))
                .hasAuthority(RoleName.ROLE_ADMIN)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.PATIENT_BED, RequestAction.PatientBed.DELETE_BED))
                .hasAuthority(RoleName.ROLE_ADMIN)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.PATIENT_BED, RequestAction.PatientBed.FIND_BY_ID))
                .hasAuthority(RoleName.ROLE_ADMIN)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.PATIENT_BED, RequestAction.PatientBed.FIND_BY_ROOM))
                .hasAnyAuthority(RoleName.ROLE_ADMIN, RoleName.ROLE_SPECIALIST, RoleName.ROLE_NURSE)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.PATIENT_BED, RequestAction.PatientBed.FIND_EMPTY_BY_ROOM))
                .hasAnyAuthority(RoleName.ROLE_ADMIN, RoleName.ROLE_SPECIALIST, RoleName.ROLE_NURSE)

                // Author staff room
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.STAFF_ROOM, RequestAction.StaffRoom.UPDATE_STAFF_ROOM))
                .hasAuthority(RoleName.ROLE_SPECIALIST)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.STAFF_ROOM, RequestAction.StaffRoom.FIND_STAFF_IN_ROOM))
                .hasAnyAuthority(RoleName.ROLE_ADMIN, RoleName.ROLE_SPECIALIST, RoleName.ROLE_NURSE)

                //Author receive patient
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.RECEIVE_PATIENT, RequestAction.ReceivePatient.CREATE_PATIENT))
                .hasAnyAuthority(RoleName.ROLE_EXAM_DOCTOR, RoleName.ROLE_MEDICAL_STAFF)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.RECEIVE_PATIENT, RequestAction.ReceivePatient.ORDER_MEDICAL_ORDER))
                .hasAuthority(RoleName.ROLE_EXAM_DOCTOR)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.RECEIVE_PATIENT, RequestAction.ReceivePatient.ORDER_DEPARTMENT))
                .hasAuthority(RoleName.ROLE_EXAM_DOCTOR)

                //Author patient
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.PATIENT, RequestAction.Patient.UPDATE_IN_DEPARTMENT))
                .hasAuthority(RoleName.ROLE_SPECIALIST)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.PATIENT, RequestAction.Patient.UPDATE_INFO))
                .hasAnyAuthority(RoleName.ROLE_SPECIALIST, RoleName.ROLE_NURSE)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.PATIENT, RequestAction.Patient.FIND_PATIENTS_ADMISSION))
                .hasAnyAuthority(RoleName.ROLE_ADMIN, RoleName.ROLE_EXAM_DOCTOR, RoleName.ROLE_NURSE, RoleName.ROLE_MEDICAL_STAFF)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.PATIENT, RequestAction.Patient.FIND_PATIENTS_ADMISSION_DEPARTMENT))
                .hasAnyAuthority(RoleName.ROLE_ADMIN, RoleName.ROLE_SPECIALIST, RoleName.ROLE_NURSE)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.PATIENT, RequestAction.Patient.FIND_BY_DEPARTMENT_PAGE))
                .hasAnyAuthority(RoleName.ROLE_ADMIN, RoleName.ROLE_SPECIALIST, RoleName.ROLE_NURSE)

                //Author medical record
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.MEDICAL_RECORD, RequestAction.MedicalRecord.UPDATE_RECORD))
                .hasAnyAuthority(RoleName.ROLE_SPECIALIST, RoleName.ROLE_NURSE)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.MEDICAL_RECORD, RequestAction.MedicalRecord.CREATE_RECORD_DETAIL))
                .hasAnyAuthority(RoleName.ROLE_SPECIALIST, RoleName.ROLE_NURSE)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.MEDICAL_RECORD, RequestAction.MedicalRecord.UPDATE_RECORD_DETAIL))
                .hasAnyAuthority(RoleName.ROLE_SPECIALIST, RoleName.ROLE_NURSE)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.MEDICAL_RECORD, RequestAction.MedicalRecord.DELETE_RECORD_DETAIL))
                .hasAnyAuthority(RoleName.ROLE_SPECIALIST, RoleName.ROLE_NURSE)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.MEDICAL_RECORD, RequestAction.MedicalRecord.FIND_BY_PATIENT_ID))
                .hasAnyAuthority(RoleName.ROLE_SPECIALIST, RoleName.ROLE_NURSE)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.MEDICAL_RECORD, RequestAction.MedicalRecord.FIND_DETAIL_BY_ID))
                .hasAnyAuthority(RoleName.ROLE_SPECIALIST, RoleName.ROLE_NURSE)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.MEDICAL_RECORD, RequestAction.MedicalRecord.FIND_ALL_DETAIL))
                .hasAnyAuthority(RoleName.ROLE_SPECIALIST, RoleName.ROLE_NURSE)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.MEDICAL_RECORD, RequestAction.MedicalRecord.FIND_BY_ID))
                .hasAnyAuthority(RoleName.ROLE_SPECIALIST, RoleName.ROLE_NURSE)

                //Author result
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.RESULT, RequestAction.Result.ADD_RESULT))
                .hasAnyAuthority(RoleName.ROLE_SPECIALIST, RoleName.ROLE_NURSE)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.RESULT, RequestAction.Result.DELETE_RESULT))
                .hasAnyAuthority(RoleName.ROLE_SPECIALIST, RoleName.ROLE_NURSE)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.RESULT, RequestAction.Result.DOWNLOAD_RESULT)).permitAll()
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.RESULT, RequestAction.Result.FIND_ALL))
                .hasAnyAuthority(RoleName.ROLE_SPECIALIST, RoleName.ROLE_NURSE)

                //Author medical record
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.MEDICAL_ORDER, RequestAction.MedicalOrder.ADD_DETAIL))
                .hasAuthority(RoleName.ROLE_SPECIALIST)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.MEDICAL_ORDER, RequestAction.MedicalOrder.CANCEL_ORDER))
                .hasAuthority(RoleName.ROLE_SPECIALIST)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.MEDICAL_ORDER, RequestAction.MedicalOrder.FIND_BY_PATIENT))
                .hasAnyAuthority(RoleName.ROLE_SPECIALIST, RoleName.ROLE_NURSE)
                .antMatchers(String.format(S_S_S, PMSConstants.PREFIX_URL, RequestAction.MEDICAL_ORDER, RequestAction.MedicalOrder.FIND_BY_PATIENT))
                .hasAnyAuthority(RoleName.ROLE_SPECIALIST, RoleName.ROLE_NURSE)
                .anyRequest().authenticated();
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity,
                                                       AuthenticationUserService authentication,
                                                       BCryptPasswordEncoder passwordEncoder) throws Exception {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(authentication)
                .passwordEncoder(passwordEncoder)
                .and().build();
    }
}
