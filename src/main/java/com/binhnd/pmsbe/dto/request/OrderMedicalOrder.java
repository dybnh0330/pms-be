package com.binhnd.pmsbe.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class OrderMedicalOrder {
    private List<Long> medicalOrderIds;

}
