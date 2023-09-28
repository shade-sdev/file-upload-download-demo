package com.file.upload.demo.model.folder;

import java.time.LocalDate;

import com.file.upload.demo.model.enumerator.ItemType;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class FolderStructure {
    String name;
    String path;
    ItemType type;
    Long size;
    String sizeUnit;
    LocalDate creationDate;
    LocalDate lastModifiedDate;
}
