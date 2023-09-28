package com.file.upload.demo.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class TestDto {
    String test;
}
