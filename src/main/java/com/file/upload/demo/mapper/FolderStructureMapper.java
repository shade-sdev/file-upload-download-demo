package com.file.upload.demo.mapper;

import static com.file.upload.demo.model.enumerator.FileTimeChoice.CREATION;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.file.upload.demo.model.enumerator.FileTimeChoice;
import com.file.upload.demo.model.enumerator.ItemType;
import com.file.upload.demo.model.folder.FolderStructure;
import com.file.upload.demo.util.Utilities;

@Mapper(componentModel = "spring", imports = {Utilities.class, ItemType.class, FileTimeChoice.class})
public interface FolderStructureMapper {

    @Mapping(target = "name", expression = "java(path.getFileName().toString())")
    @Mapping(target = "path", expression = "java(path.toString())")
    @Mapping(target = "type", expression = "java(isFolder ? ItemType.FOLDER : ItemType.FILE)")
    @Mapping(target = "creationDate", expression = "java(mapToLocalDate(Utilities.toBasicFileAttributes(path), FileTimeChoice.CREATION))")
    @Mapping(target = "lastModifiedDate", expression = "java(mapToLocalDate(Utilities.toBasicFileAttributes(path), FileTimeChoice.MODIFIED))")
    @Mapping(target = "size", expression = "java(Utilities.toSize(Utilities.toBasicFileAttributes(path)))")
    @Mapping(target = "sizeUnit", expression = "java(Utilities.toSizeUnit.apply(Utilities.toSize(Utilities.toBasicFileAttributes(path))))")
    FolderStructure mapToFolderStructure(Path path, boolean isFolder);

    default LocalDate mapToLocalDate(Optional<BasicFileAttributes> basicFileAttributes, FileTimeChoice choice) {
        return basicFileAttributes.map(it -> CREATION == choice ? it.creationTime() : it.lastModifiedTime())
                                  .map(it -> it.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                                  .orElse(LocalDate.now());
    }
}
