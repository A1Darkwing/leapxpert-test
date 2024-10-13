package com.example.message_service.domain.dto.chat;

import com.example.message_service.validation.ValidPhone;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;

@Data
public class CreateChatRoomRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "ID cannot be blank")
    @Pattern(regexp = "^[0-9]{12}$", message = "ID must be a 12-digit number")
    private String id;

    @NotNull(message = "Name cannot be null")
    private String name;

    @NotNull(message = "Type cannot be null")
    @Positive(message = "Type must be a positive integer")
    private int type;

    @NotNull(message = "Timestamp cannot be null")
    @Positive(message = "Timestamp must be a positive number")
    private long timestamp;
    private List<Member> members;

    @Data
    public static class Member {

        @ValidPhone
        @NotBlank(message = "Phone number can not be blank!")
        @Size(min=8, max=11, message = "Please enter 8-11 characters")
        private String phoneNumber;

        @NotBlank(message = "Name can not be blank!")
        private String name;
    }
}

