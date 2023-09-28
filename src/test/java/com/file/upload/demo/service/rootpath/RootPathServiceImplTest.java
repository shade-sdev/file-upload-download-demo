package com.file.upload.demo.service.rootpath;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.file.upload.demo.model.enumerator.ItemType;
import com.file.upload.demo.model.folder.FolderStructure;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RootPathServiceImplTest {

    @Test
    void testGetChildren() {
        List<FolderStructure> folderStructures = List.of(FolderStructure.builder()
                                                                        .path("path")
                                                                        .size(1L)
                                                                        .type(ItemType.FILE)
                                                                        .sizeUnit("100GB")
                                                                        .lastModifiedDate(LocalDate.now())
                                                                        .creationDate(LocalDate.now())
                                                                        .build());
        assertNotNull(folderStructures);
        assertEquals("path", folderStructures.stream().findFirst().map(FolderStructure::getPath).orElse(""));

    }
}
