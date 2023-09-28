package com.file.upload.demo.controller;


import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.file.upload.demo.model.dto.TestDto;
import com.file.upload.demo.model.enumerator.ItemType;
import com.file.upload.demo.model.folder.FolderSearchCriteria;
import com.file.upload.demo.model.folder.FolderSortCriteria;
import com.file.upload.demo.model.folder.FolderStructure;
import com.file.upload.demo.service.rootpath.RootPathService;
import com.file.upload.demo.util.Utilities;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/folder")
@Slf4j
public class FolderRestController {

    private final RootPathService service;

    @Autowired
    public FolderRestController(RootPathService service) {
        this.service = service;
    }


    @PostMapping("/test")
    public ResponseEntity<Void> testController(@RequestBody TestDto testDto) {
        try {
            // Your controller logic here

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // Log the error
            log.error("An error occurred in the testController:", e);
            // You can also customize the error response here if needed
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/sub-items")
    public ResponseEntity<List<FolderStructure>> getChildren(@RequestParam(required = false) String name,
                                                             @RequestParam(required = false) String path,
                                                             @RequestParam(required = false) ItemType type,
                                                             @RequestParam(required = false) String sizeUnit,
                                                             @RequestParam(required = false) String creationDate,
                                                             @RequestParam(required = false) String lastModifiedDate,
                                                             @RequestParam(required = false) FolderSortCriteria sortCriteria,
                                                             @RequestParam(required = false) Boolean ascendingSort
    ) {
        FolderSearchCriteria.FolderSearchCriteriaBuilder builder = FolderSearchCriteria.builder();

        if (Objects.nonNull(name) && StringUtils.isNotBlank(name)) {
            builder.name(name);
        }

        if (Objects.nonNull(type)) {
            builder.type(type);
        }

        if (Objects.nonNull(path)) {
            builder.path(path);
        }

        if (Objects.nonNull(sizeUnit) && StringUtils.isNotBlank(sizeUnit)) {
            builder.sizeUnit(sizeUnit);
        }

        if (Objects.nonNull(creationDate)) {
            builder.creationDate(Utilities.toLocalDate(creationDate));
        }

        if (Objects.nonNull(lastModifiedDate)) {
            builder.lastModifiedDate(Utilities.toLocalDate(lastModifiedDate));
        }

        boolean sort = Objects.isNull(ascendingSort) || ascendingSort;

        return ResponseEntity.ok(service.getChildren(builder.build(), sortCriteria, sort));
    }
}
