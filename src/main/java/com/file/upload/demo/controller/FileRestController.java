package com.file.upload.demo.controller;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.file.upload.demo.model.UploadedFile;
import com.file.upload.demo.model.dto.TestDto;
import com.file.upload.demo.model.exception.FileException;

import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/file")
@Slf4j
@Validated
public class FileRestController {

    @PostMapping("/upload")
    public ResponseEntity<UploadedFile> upload(HttpServletRequest request, @RequestBody byte[] file) {
        if (file == null || file.length == 0) {
            throw new FileException("File cannot be empty", BAD_REQUEST);
        }

        return ResponseEntity.ok(UploadedFile.builder()
                                             .basicAuthorization(request.getHeader(AUTHORIZATION))
                                             .size(file.length)
                                             .contentType(request.getContentType())
                                             .file(UploadedFile.File.builder().fileBytes(file).build())
                                             .build());
    }

    @GetMapping("/download")
    @SneakyThrows
    public ResponseEntity<Resource> download(@RequestParam String path) {
        @Cleanup
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            Path filePath = Paths.get(path);
            byte[] fileContent = Files.readAllBytes(filePath);

            String fileName = path.substring(path.lastIndexOf("/") + 1);
            try (ZipOutputStream zos = new ZipOutputStream(bos)) {
                zos.putNextEntry(new ZipEntry(fileName));
                zos.write(fileContent);
                zos.closeEntry();
            }
        } catch (Exception e) {
            throw new FileException(e.getMessage(), BAD_REQUEST);
        }

        ByteArrayResource resource = new ByteArrayResource(bos.toByteArray());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"file.zip\"");
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

}
