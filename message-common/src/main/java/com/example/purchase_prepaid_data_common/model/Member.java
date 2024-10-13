package com.example.purchase_prepaid_data_common.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Member {
    private String phoneNumber;
    private String name;
    private String chatRoomId;
}
