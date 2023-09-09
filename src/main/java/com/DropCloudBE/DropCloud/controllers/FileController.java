package com.DropCloudBE.DropCloud.controllers;

import com.DropCloudBE.DropCloud.dto.ResponseDTO;
import com.DropCloudBE.DropCloud.models.FileStore;
import com.DropCloudBE.DropCloud.services.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/file")
@Slf4j
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @GetMapping("/download/{fileId}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable("fileId") UUID fileId) {
        FileStore file = fileService.getFile(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getFileMetadata().getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment:filename\"" + file.getFileName() + "\"")
                .body(new ByteArrayResource(file.getContent()));
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(MultipartFile fileToUpload) {
        if (fileToUpload.isEmpty()) {
            return null;
        }
        ResponseDTO file = fileService.uploadFile(fileToUpload);
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>("Uploaded", headers, HttpStatus.CREATED);
    }

    @GetMapping("/allFiles")
    public ResponseEntity<String> getFile(MultipartFile fileToUpload) {
        if (fileToUpload.isEmpty()) {
            return null;
        }
        List<FileStore> file = fileService.getAllFiles();
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>("getFiles", headers, HttpStatus.CREATED);
    }

}
