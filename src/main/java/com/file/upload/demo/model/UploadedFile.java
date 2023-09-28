package com.file.upload.demo.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(of = {"file"})
public class UploadedFile {

    @NotNull(message = "contentType cannot be null")
    String contentType;

    @NotNull(message = "basicAuthorization cannot be null")
    String basicAuthorization;

    File file;

    int size;

    @Data
    @Builder(toBuilder = true)
    public static class File {
        byte[] fileBytes;
    }
}
