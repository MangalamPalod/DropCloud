package com.DropCloudBE.DropCloud.services.implementation;

import com.DropCloudBE.DropCloud.dto.ResponseDTO;
import com.DropCloudBE.DropCloud.models.FileStore;
import com.DropCloudBE.DropCloud.services.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

//    @Value("s3.path")
//    private String uploadDir;

    @Override
    public ResponseDTO uploadFile(MultipartFile file) {
        try {
            // Check if the file is not empty
            if (!file.isEmpty()) {
                ExecutorService executorService = Executors.newFixedThreadPool(2);
                CompletionService<ResponseEntity<String>> completionService = new ExecutorCompletionService<>(executorService);
                String uploadDir = "path/to/upload/directory";

                // Create the directory if it doesn't exist
                java.io.File directory = new java.io.File(uploadDir);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                InputStream inputStream = file.getInputStream(); // Get the file's input stream

                int bufferSize = 204800; // Define the buffer size for reading chunks
                byte[] buffer = new byte[bufferSize];


                OutputStream outputStream = new FileOutputStream(uploadDir + "/" + file.getOriginalFilename()); // Create an output stream for writing chunks to the local disk

                int totalChunks = 0;
                int chunkSize;
                while ((chunkSize = inputStream.read(buffer)) != -1) {
                    try {
                        totalChunks++;
                        int finalBytesRead = chunkSize;
                        completionService.submit(()->writeChunk(outputStream, buffer, finalBytesRead));
                    } catch (Exception e) {
                        deleteFile();
                        throw new RuntimeException("Failed to upload the file");
                    }
                }

                while(totalChunks>0){
                    Future<ResponseEntity<String>> future = completionService.take();
                    if(future!=null){
                        ResponseEntity<String> response= future.get();
                        if(response.getStatusCode().is2xxSuccessful()){
                            return null;
                        }else{
                            return null;
                        }
                    }
                }


                inputStream.close();
                outputStream.close();

                return null;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Failed to upload the file: " + e.getMessage());
        }
    }

    @Override
    public FileStore getFile(UUID fileId) {
        return null;
    }

    @Override
    public List<FileStore> getAllFiles() {
        return null;
    }

    @Override
    public Object updateFile() {
        return null;
    }

    @Override
    public Object deleteFile() {
        return null;
    }


    @Retryable(value = RuntimeException.class, maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 2))
    private ResponseEntity<String> writeChunk(OutputStream outputStream, byte[] buffer, int bytesRead) {
        try {
            outputStream.write(buffer, 0, bytesRead);
        } catch (IOException e) {
            log.error("Failed to write the file");
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>("Uploaded", HttpStatus.OK);
    }
}
