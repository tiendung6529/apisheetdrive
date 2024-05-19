package com.example.demo;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.HttpTransport;




public class GoogleDriveService {

    private static final String APPLICATION_NAME = "Demo10";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String SERVICE_ACCOUNT_KEY_FILE_PATH = "D://demo10-423612-b381e593f585.json";

    public static Drive getDriveService() throws GeneralSecurityException, IOException {
        // Khởi tạo transport
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        // Load service account key JSON file
        java.io.File serviceAccountFile = new java.io.File(SERVICE_ACCOUNT_KEY_FILE_PATH);

        // Load credentials
        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(serviceAccountFile))
                .createScoped(Collections.singleton(DriveScopes.DRIVE));

        // Build Google Drive service
        return new Drive.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static void uploadImageToDrive(Drive service, byte[] imageData, String folderId, String imageName) throws IOException {
        // Create file metadata
        File fileMetadata = new File();
        fileMetadata.setName(imageName);
        fileMetadata.setParents(Collections.singletonList(folderId));

        // Create file content from image data
        ByteArrayContent mediaContent = new ByteArrayContent("image/jpeg", imageData);

        // Upload file to Google Drive
        File file = service.files().create(fileMetadata, mediaContent)
                .setFields("id")
                .execute();

        System.out.println("File ID: " + file.getId());
    }
}
