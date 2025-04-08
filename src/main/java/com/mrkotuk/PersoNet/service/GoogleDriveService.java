package com.mrkotuk.PersoNet.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.client.http.FileContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

@Service
public class GoogleDriveService {
    public static final String DEFAULT_PHOTO_URL =
            "https://drive.google.com/file/d/1QMAvRnqeHTO7YRGEGfbucAYMvj6MzBm4/view?usp=drivesdk";

    private final Logger logger = LoggerFactory.getLogger(GoogleDriveService.class);

    @Value("${GOOGLE_DRIVE_CREDITALS_PATH}")
    private final String credentialsPath;

    @Value("${GOOGLE_DRIVE_FOLDER_ID}")
    private final String folderId;

    private final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private final Drive DRIVE_SERVICE;

    public GoogleDriveService(@Value("${GOOGLE_DRIVE_CREDITALS_PATH}") String credentialsPath,
            @Value("${GOOGLE_DRIVE_FOLDER_ID}") String folderId) {
        this.credentialsPath = credentialsPath;
        this.folderId = folderId;
        this.DRIVE_SERVICE = createDriveService();
    }

    public List<String> uploadFiles(List<MultipartFile> files) {
        List<String> urls = new ArrayList<>();

        for (MultipartFile file : files)
            urls.add(uploadFile(file));

        return urls;
    }

    public String deleteFileByUrl(String fileUrl) {
        if (fileUrl.equals(DEFAULT_PHOTO_URL))
            return "Default photo cannot be deleted!";

        try {
            DRIVE_SERVICE.files().delete(extractFileIdFromUrl(fileUrl)).execute();
            return "File with URL deleted successfully!";
        } catch (IllegalArgumentException e) {
            logger.error("Failed to extract file ID from URL: {}", e.getMessage());
        } catch (IOException e) {
            logger.error("Error deleting file from URL", e);
        }

        return "Error deleting file from URL: " + fileUrl;
    }

    private String uploadFile(MultipartFile file) {
        try {
            File fileMetadata = new File();
            fileMetadata.setName(file.getOriginalFilename());
            fileMetadata.setParents(Collections.singletonList(folderId));

            java.io.File tempFile = java.io.File.createTempFile("upload-", file.getOriginalFilename());
            file.transferTo(tempFile);
            FileContent fileContent = new FileContent(file.getContentType(), tempFile);

            File uploadedFile = DRIVE_SERVICE.files().create(fileMetadata, fileContent)
                    .setFields("id, webViewLink")
                    .execute();

            tempFile.delete();
            return uploadedFile.getWebViewLink();
        } catch (IOException e) {
            logger.error("Failed to upload file to Google Drive", e);
            return null;
        }
    }

    private String extractFileIdFromUrl(String fileUrl) {
        Matcher matcher = Pattern.compile("[-\\w]{25,}").matcher(fileUrl);

        return matcher.find()
                ? matcher.group()
                : null;
    }

    private Drive createDriveService() {
        try {
            GoogleCredentials credentials = GoogleCredentials
                    .fromStream(new FileInputStream(Path.of(credentialsPath).toFile()))
                    .createScoped(Collections.singletonList(DriveScopes.DRIVE_FILE));

            return new Drive.Builder(new com.google.api.client.http.javanet.NetHttpTransport(),
                    JSON_FACTORY,
                    new HttpCredentialsAdapter(credentials))
                    .setApplicationName("PersoNet")
                    .build();
        } catch (IOException e) {
            logger.error("Failed to create Google Drive service", e);
            return null;
        }
    }
}
