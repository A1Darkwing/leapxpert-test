package com.example.purchase_prepaid_data_common.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class CreateChatRoomMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private ChatRoom chatRoom;
    private List<Member> members;
}