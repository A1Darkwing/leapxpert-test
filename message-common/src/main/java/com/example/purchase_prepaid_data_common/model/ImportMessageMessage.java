package com.example.purchase_prepaid_data_common.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class ImportMessageMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private Message message;
}
