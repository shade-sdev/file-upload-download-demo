package com.file.upload.demo.service.rootpath;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.file.upload.demo.mapper.FolderStructureMapper;
import com.file.upload.demo.model.folder.FolderSearchCriteria;
import com.file.upload.demo.model.folder.FolderSortCriteria;
import com.file.upload.demo.model.folder.FolderStructure;
import com.file.upload.demo.util.Utilities;

import lombok.SneakyThrows;

@Service
public class RootPathServiceImpl implements RootPathService {

    private final String basePath;
    private final FolderStructureMapper mapper;

    @Autowired
    public RootPathServiceImpl(@Value("${app.base-url}") String basePath, FolderStructureMapper mapper) {
        this.basePath = basePath;
        this.mapper = mapper;
    }

    @SneakyThrows
    @Cacheable(cacheNames = "rootChildren", key = "{#root.methodName, #searchCriteria, #sortCriteria, #ascendingSort}")
    public List<FolderStructure> getChildren(FolderSearchCriteria searchCriteria, FolderSortCriteria sortCriteria, boolean ascendingSort) {

        List<FolderStructure> subfolders = new ArrayList<>();

        DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(basePath));

        Utilities.toStream(stream)
                 .forEach(it -> subfolders.add(mapper.mapToFolderStructure(it, Files.isDirectory(it))));

        List<Predicate<FolderStructure>> predicates = Utilities.generatePredicates(FolderStructure.class, searchCriteria);

        List<FolderStructure> toFilterSubFolders = subfolders.stream()
                                                             .filter(predicates.stream().reduce(Predicate::and)
                                                                               .orElse(x -> true))
                                                             .collect(Collectors.toList());

        Utilities.sortList(toFilterSubFolders, sortCriteria, ascendingSort);

        return toFilterSubFolders;
    }

}
