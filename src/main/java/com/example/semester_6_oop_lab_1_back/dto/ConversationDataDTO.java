package com.example.semester_6_oop_lab_1_back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationDataDTO {
    private int id;
    private Double priceByMinute;
    private Long minutes;
    private PaymentDataDTO payment;
}
