package com.file.upload.demo.model.folder;

import java.time.LocalDate;

import com.file.upload.demo.model.enumerator.ItemType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class FolderSearchCriteria {
    String name;
    String path;
    String sizeUnit;
    ItemType type;
    LocalDate creationDate;
    LocalDate lastModifiedDate;
}
