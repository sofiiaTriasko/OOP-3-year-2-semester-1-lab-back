package com.example.semester_6_oop_lab_1_back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDataDTO {
    private int id;
    private String name;
    private Double money;
    private String password;
    private String number;
    private Boolean isBlocked;
    private List<ConversationDataDTO> conversations = new ArrayList<>();
    private List<ServicesDataDTO> services = new ArrayList<>();
}
