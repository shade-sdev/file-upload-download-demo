package com.file.upload.demo.service.rootpath;

import java.util.List;

import com.file.upload.demo.model.folder.FolderSearchCriteria;
import com.file.upload.demo.model.folder.FolderSortCriteria;
import com.file.upload.demo.model.folder.FolderStructure;

public interface RootPathService {
    List<FolderStructure> getChildren(FolderSearchCriteria searchCriteria, FolderSortCriteria sortCriteria, boolean ascendingSort);
}
