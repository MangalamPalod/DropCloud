package com.DropCloudBE.DropCloud.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileMetadata {
    private UUID fileId;
    private Date createdAt;
    private Date updatedAt;
    private Date lastOpenAt;
    private String fileType;
    private long size;
}
