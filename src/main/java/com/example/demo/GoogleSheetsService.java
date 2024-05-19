package com.example.demo;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GoogleSheetsService {
    private final String APPLICATION_NAME = "Demo10";
    private final String SPREADSHEET_ID = "1MDReWo4q2lETxj_IdBUbvw0VDalUP2kJIArPrydfb0M";

    private Sheets sheetsService;

    public GoogleSheetsService() throws IOException, GeneralSecurityException {
        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream("D://demo10-423612-b381e593f585.json"))
                .createScoped(Collections.singletonList("https://www.googleapis.com/auth/spreadsheets"));
        sheetsService = new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public void writeToSheet(String username, String password, String loginResult) throws IOException {
        String sheetName = "Sheet1";
        int currentRowCount = getCurrentRowCount(SPREADSHEET_ID, sheetName);
        String range = sheetName + "!A" + (currentRowCount + 1) + ":C" + (currentRowCount + 1);

        ValueRange body = new ValueRange()
                .setValues(Arrays.asList(Arrays.asList(username, password, loginResult)));

        sheetsService.spreadsheets().values()
                .append(SPREADSHEET_ID, range, body)
                .setValueInputOption("RAW")
                .execute();
    }

    public int getCurrentRowCount(String spreadsheetId, String sheetName) throws IOException {
        String range = sheetName + "!A:A";

        ValueRange response = sheetsService.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();

        List<List<Object>> values = response.getValues();

        return values != null ? values.size() : 0;
    }




}
