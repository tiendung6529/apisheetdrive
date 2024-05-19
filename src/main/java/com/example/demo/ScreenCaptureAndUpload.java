package com.example.demo;

import com.google.api.services.drive.Drive;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;


import com.google.api.client.http.ByteArrayContent;
import com.google.api.services.drive.model.File;


public class ScreenCaptureAndUpload {

    private static final String FOLDER_ID = "1r2niG-rzBzrb0mpk_uc_YjuNmK-9dBQm";
    private static final String IMAGE_NAME = "anhdd.png";
    private static final String SPREADSHEET_ID = "1MDReWo4q2lETxj_IdBUbvw0VDalUP2kJIArPrydfb0M";

    public static void main(String[] args) throws IOException, GeneralSecurityException {
        try {
            WebDriverManager.chromedriver().setup();

            WebDriver chromeDriver = new ChromeDriver();

            chromeDriver.get("https://viaphi.com/login");
            String username = "dungntph22068";
            String password = "Qwertyu899";
            chromeDriver.findElement(By.xpath("//input[@name='username']")).sendKeys(username);
            chromeDriver.findElement(By.xpath("//input[@name='password']")).sendKeys(password);

            WebElement Message =  chromeDriver.findElement(By.xpath("//button[contains(@class, 'btn btn-hero-primary') and contains(., 'Đăng nhập')]"));
            Message.click();

            Thread.sleep(5000);

            byte[] screenshotBytes = takeChromeScreenshot(chromeDriver);

            Drive service = GoogleDriveService.getDriveService();
            uploadImageToDrive(service, screenshotBytes);

            String pageSource = chromeDriver.getPageSource();
            if (pageSource.contains("0395422681")) {
                writeToSheet(username,password,"Đăng nhập thành công");
            } else if(pageSource.contains("VIAPHI.COM")){
                writeToSheet(username,password,"Đăng nhập thất bại");
            }
            System.out.println("Chụp ảnh thành công và update lên google drive!");

            chromeDriver.quit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static byte[] takeChromeScreenshot(WebDriver driver) throws IOException {
        byte[] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        return screenshotBytes;
    }

    private static void uploadImageToDrive(Drive service, byte[] imageBytes) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName(IMAGE_NAME);
        fileMetadata.setParents(Collections.singletonList(FOLDER_ID));

        ByteArrayContent mediaContent = new ByteArrayContent("image/png", imageBytes);

        File file = service.files().create(fileMetadata, mediaContent)
                .setFields("id")
                .execute();

        System.out.println("File ID: " + file.getId());
    }

//    private static void writeToSheet(String status) throws IOException, GeneralSecurityException {
//        GoogleSheetsService sheetsService = new GoogleSheetsService();
//
//        sheetsService.writeToSheet(status);
//    }
private static void writeToSheet(String username, String password, String loginResult) throws IOException, GeneralSecurityException {
    GoogleSheetsService sheetsService = new GoogleSheetsService();

    sheetsService.writeToSheet(username, password, loginResult);
}
}
