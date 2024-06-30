package com.binhnd.pmsbe.services.medical_order.impl;

import com.binhnd.pmsbe.common.constants.PMSConstants;
import com.binhnd.pmsbe.common.enums.EnumPMSException;
import com.binhnd.pmsbe.common.exception.PMSException;
import com.binhnd.pmsbe.dto.response.MedicalOrderDetailsResponse;
import com.binhnd.pmsbe.dto.response.MedicalOrderResponse;
import com.binhnd.pmsbe.entity.MedicalOrder;
import com.binhnd.pmsbe.entity.MedicalOrderDetails;
import com.binhnd.pmsbe.entity.Patient;
import com.binhnd.pmsbe.mapper.MedicalOrderMapper;
import com.binhnd.pmsbe.repositories.MedicalOrderDetailsRepository;
import com.binhnd.pmsbe.repositories.MedicalOrderRepository;
import com.binhnd.pmsbe.repositories.PatientRepository;
import com.binhnd.pmsbe.repositories.custom_repositories.CustomMedicalOrderRepository;
import com.binhnd.pmsbe.services.medical_order.MedicalOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicalOrderServiceImpl implements MedicalOrderService {

    private final MedicalOrderDetailsRepository medicalOrderDetailsRepository;
    private final MedicalOrderRepository medicalOrderRepository;
    private final PatientRepository patientRepository;
    private final CustomMedicalOrderRepository customMedicalOrderRepository;

    @Autowired
    public MedicalOrderServiceImpl(MedicalOrderDetailsRepository medicalOrderDetailsRepository,
                                   MedicalOrderRepository medicalOrderRepository,
                                   PatientRepository patientRepository, CustomMedicalOrderRepository customMedicalOrderRepository) {
        this.medicalOrderDetailsRepository = medicalOrderDetailsRepository;
        this.medicalOrderRepository = medicalOrderRepository;
        this.patientRepository = patientRepository;
        this.customMedicalOrderRepository = customMedicalOrderRepository;
    }

    @Override
    public MedicalOrderDetailsResponse findMedicalOrderDetailById(Long id) {

        Optional<MedicalOrderDetails> details = medicalOrderDetailsRepository.findById(id);
        if (details.isEmpty()) {
            throw new PMSException(EnumPMSException.MEDICAL_ORDER_NOT_EXISTED);
        }

        return MedicalOrderMapper.toOrderDetailsDto(details.get());
    }

    @Override
    public List<MedicalOrderDetailsResponse> findAllMedicalOrderDetails(Long medicalOrderId, Long type) {

        Optional<MedicalOrder> medicalOrder = medicalOrderRepository.findById(medicalOrderId);
        if (medicalOrder.isEmpty()) {
            throw new PMSException(EnumPMSException.MEDICAL_ORDER_NOT_EXISTED);
        }

        if (!PMSConstants.LIST_TYPE.contains(type)){
            throw new PMSException(EnumPMSException.MEDICAL_ORDER_TYPE_NOT_FOUND);
        }

        List<MedicalOrderDetails> details = customMedicalOrderRepository.findAllMedicalOrderDetail(medicalOrderId, type);

        return MedicalOrderMapper.mapAll(details);
    }

    @Override
    public MedicalOrderResponse findMedicalOrderByPatient(Long patientId) {

        Optional<Patient> patient = patientRepository.findById(patientId);
        if (patient.isEmpty()) {
            throw new PMSException(EnumPMSException.PATIENT_NOT_EXIST);
        }

        MedicalOrder medicalOrderByPatient = medicalOrderRepository.findMedicalOrderByPatient(patient.get());
        return MedicalOrderMapper.toOrderDto(medicalOrderByPatient);
    }


}
