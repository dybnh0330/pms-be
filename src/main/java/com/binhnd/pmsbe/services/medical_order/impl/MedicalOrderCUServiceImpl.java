package com.binhnd.pmsbe.services.medical_order.impl;

import com.binhnd.pmsbe.common.constants.PMSConstants;
import com.binhnd.pmsbe.common.enums.EnumPMSException;
import com.binhnd.pmsbe.common.exception.PMSException;
import com.binhnd.pmsbe.common.utils.jwt.SecurityUtils;
import com.binhnd.pmsbe.dto.request.MedicalOrderRequest;
import com.binhnd.pmsbe.dto.response.MedicalOrderDetailsResponse;
import com.binhnd.pmsbe.entity.Category;
import com.binhnd.pmsbe.entity.MedicalOrder;
import com.binhnd.pmsbe.entity.MedicalOrderDetails;
import com.binhnd.pmsbe.mapper.MedicalOrderMapper;
import com.binhnd.pmsbe.repositories.CategoryRepository;
import com.binhnd.pmsbe.repositories.MedicalOrderDetailsRepository;
import com.binhnd.pmsbe.repositories.MedicalOrderRepository;
import com.binhnd.pmsbe.services.medical_order.MedicalOrderCUService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class MedicalOrderCUServiceImpl implements MedicalOrderCUService {

    private final CategoryRepository categoryRepository;
    private final MedicalOrderRepository medicalOrderRepository;
    private final MedicalOrderDetailsRepository medicalOrderDetailsRepository;

    @Autowired
    public MedicalOrderCUServiceImpl(CategoryRepository categoryRepository,
                                     MedicalOrderRepository medicalOrderRepository,
                                     MedicalOrderDetailsRepository medicalOrderDetailsRepository) {
        this.categoryRepository = categoryRepository;
        this.medicalOrderRepository = medicalOrderRepository;
        this.medicalOrderDetailsRepository = medicalOrderDetailsRepository;
    }

    @Override
    public void addMedicalOrderDetail(List<MedicalOrderRequest> requests) {

        for (MedicalOrderRequest request: requests) {
            validateAndCorrectData(request);

            Optional<Category> category = categoryRepository.findById(request.getCategoryId());
            if (category.isEmpty()) {
                throw new PMSException(EnumPMSException.CATEGORY_NOT_EXISTED);
            }

            Optional<MedicalOrder> medicalOrder = medicalOrderRepository.findById(request.getMedicalOrderId());
            if (medicalOrder.isEmpty()) {
                throw new PMSException(EnumPMSException.MEDICAL_ORDER_NOT_EXISTED);
            }

            if (!PMSConstants.LIST_TYPE.contains(request.getType())) {
                throw new PMSException(EnumPMSException.MEDICAL_ORDER_TYPE_NOT_FOUND);
            }

            MedicalOrderDetails details = new MedicalOrderDetails();
            details.setMedicalOrder(medicalOrder.get());
            details.setCategory(category.get());
            details.setNote(request.getNote());
            details.setType(request.getType());
            details.setUnit(request.getUnit());
            details.setQuantity(request.getQuantity());
            details.setCreateTime(Timestamp.from(Instant.now()));
            details.setCreateBy(SecurityUtils.getCurrentUsername());

            medicalOrderDetailsRepository.save(details);
        }

    }

    @Override
    public void cancelMedicalOrderDetails(Long id) {

        Optional<MedicalOrderDetails> details = medicalOrderDetailsRepository.findById(id);
        if (details.isEmpty()) {
            throw new PMSException(EnumPMSException.MEDICAL_ORDER_NOT_EXISTED);
        }

        medicalOrderDetailsRepository.delete(details.get());
    }

    private void validateAndCorrectData(MedicalOrderRequest request) {
        if (ObjectUtils.isEmpty(request)
                || ObjectUtils.isEmpty(request.getType())
                || ObjectUtils.isEmpty(request.getMedicalOrderId())
                || ObjectUtils.isEmpty(request.getNote())
                || ObjectUtils.isEmpty(request.getUnit())
                || ObjectUtils.isEmpty(request.getQuantity())
                || ObjectUtils.isEmpty(request.getCategoryId())) {
            throw new PMSException(EnumPMSException.MEDICAL_ORDER_DATA_INVALID);
        }
    }
}
