package com.example.semester_6_oop_lab_1_back.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Conversation {
    private int id;
    private int userId;
    private int paymentId;
    private Double priceByMinute;
    private Long minutes;
}
