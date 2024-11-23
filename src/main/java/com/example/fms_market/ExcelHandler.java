package com.example.fms_market;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelHandler {
    private static final String FILE_PATH = "users.xlsx";

    public static void saveUser(User user) throws IOException {
        File file = new File(FILE_PATH);
        Workbook workbook;
        Sheet sheet;

        if (file.exists()) {
            FileInputStream inputStream = new FileInputStream(file);
            workbook = new XSSFWorkbook(inputStream);
            sheet = workbook.getSheetAt(0);
            inputStream.close();
        } else {
            workbook = new XSSFWorkbook();
            sheet = workbook.createSheet("Users");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Email");
            headerRow.createCell(1).setCellValue("Password");
            headerRow.createCell(2).setCellValue("Role");
            headerRow.createCell(3).setCellValue("Phone");
            headerRow.createCell(4).setCellValue("Age");
            headerRow.createCell(5).setCellValue("Photo Path");
        }

        int rowNum = sheet.getLastRowNum() + 1;
        Row row = sheet.createRow(rowNum);

        row.createCell(0).setCellValue(user.getEmail());
        row.createCell(1).setCellValue(user.getPassword());
        row.createCell(2).setCellValue(user.getRole());
        row.createCell(3).setCellValue(user.getPhone());
        row.createCell(4).setCellValue(user.getAge());
        row.createCell(5).setCellValue(user.getUser_photo_path());

        FileOutputStream outputStream = new FileOutputStream(FILE_PATH);
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    public static boolean emailExists(String email) throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return false;
        }

        FileInputStream inputStream = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            Cell emailCell = row.getCell(0);
            if (emailCell != null && emailCell.getCellType() == CellType.STRING && emailCell.getStringCellValue().equals(email)) {
                workbook.close();
                inputStream.close();
                return true;
            }
        }

        workbook.close();
        inputStream.close();
        return false;
    }

    public User getUserByEmailAndPassword(String email, String password) throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return null;
        }

        FileInputStream inputStream = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            Cell emailCell = row.getCell(0);
            Cell passwordCell = row.getCell(1);

            if (emailCell != null && passwordCell != null &&
                    emailCell.getCellType() == CellType.STRING && emailCell.getStringCellValue().equals(email) &&
                    passwordCell.getCellType() == CellType.STRING && passwordCell.getStringCellValue().equals(password)) {

                String role = getStringCellValue(row.getCell(2));
                String phone = getStringCellValue(row.getCell(3));
                String age = getStringCellValue(row.getCell(4));
                String userPhotoPath = getStringCellValue(row.getCell(5));

                workbook.close();
                inputStream.close();
                return new User(email, password, role, phone, age, userPhotoPath);
            }
        }

        workbook.close();
        inputStream.close();
        return null;
    }

    // Helper method to safely extract cell value as String
    private String getStringCellValue(Cell cell) {
        if (cell != null) {
            if (cell.getCellType() == CellType.STRING) {
                return cell.getStringCellValue();
            } else if (cell.getCellType() == CellType.NUMERIC) {
                return String.valueOf(cell.getNumericCellValue());
            }
        }
        return "";
    }

    public static byte[] getImageFromFilePath(String filePath) throws IOException {
        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);
        return IOUtils.toByteArray(fis);
    }
}
