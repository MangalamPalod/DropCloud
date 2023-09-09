package com.DropCloudBE.DropCloud.services;

import com.DropCloudBE.DropCloud.dto.ResponseDTO;
import com.DropCloudBE.DropCloud.models.FileStore;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface FileService {
    public ResponseDTO uploadFile(MultipartFile multipartFile);
    public FileStore getFile(UUID fileId);

    public List<FileStore> getAllFiles();
    public Object updateFile();
    public Object deleteFile();
}
