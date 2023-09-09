package com.DropCloudBE.DropCloud.models;

import com.DropCloudBE.DropCloud.dto.FileMetadata;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileStore {
    private UUID fileId;
    private byte[] content;
    private FileMetadata fileMetadata;
    private String fileName;
}
