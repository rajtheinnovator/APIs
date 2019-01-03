package com.enpassio.apis.googlespreadsheet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.enpassio.apis.R;
import com.enpassio.apis.googlespreadsheet.model.Employee;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


// Code referenced from:
// https://www.callicoder.com/java-write-excel-file-apache-poi/
// https://stackoverflow.com/questions/30968735/using-com-bea-xml-stream-package-on-android
public class CreateAndReadSpreadsheetUsingApachePOI extends AppCompatActivity {

    private static String[] columns = {"Name", "Email", "Date Of Birth", "Salary"};
    private static List<Employee> employees = new ArrayList<>();
    Button writeDataButton;
    Button readDataButton;
    TextView spreadsheetDataTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_and_read_spreadsheet_using_apache_poi);

        setupDataForSpreadsheet();
        writeDataButton = findViewById(R.id.write_data_to_spreadsheet_button);
        readDataButton = findViewById(R.id.read_data_from_spreadsheet_button);
        spreadsheetDataTextView = findViewById(R.id.spreadsheet_data_text_view);
//        System.setProperty("javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
//        System.setProperty("javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
//        System.setProperty("javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");
        writeDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createWorkbookFromAvailableData();
            }
        });
        readDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    modifyExistingWorkbook();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InvalidFormatException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void modifyExistingWorkbook() throws IOException, InvalidFormatException {
        // Obtain a workbook from the excel file
        Workbook workbook = WorkbookFactory.create(new File("poi-generated-file.xlsx"));

        // Get Sheet at index 0
        Sheet sheet = workbook.getSheetAt(0);

        // Get Row at index 1
        Row row = sheet.getRow(1);

        // Get the Cell at index 2 from the above row
        Cell cell = row.getCell(2);

        // Create the cell if it doesn't exist
        if (cell == null)
            cell = row.createCell(2);

        // Update the cell's value
        cell.setCellValue("Updated Value");

        // Write the output to the file
        FileOutputStream fileOut = new FileOutputStream("poi-generated-file.xlsx");
        workbook.write(fileOut);
        fileOut.close();

        // Closing the workbook
        workbook.close();
    }

    private void createWorkbookFromAvailableData() {
        // Create a Workbook
        Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

        /* CreationHelper helps us create instances of various things like DataFormat,
           Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way */
        CreationHelper createHelper = workbook.getCreationHelper();

        // Create a Sheet
        Sheet sheet = workbook.createSheet("Employee");

        // Create a Font for styling header cells
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.RED.getIndex());

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Create a Row
        Row headerRow = sheet.createRow(0);

        // Create cells
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

        // Create Cell Style for formatting Date
        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

        // Create Other rows and cells with employees data
        int rowNum = 1;
        for (Employee employee : employees) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0)
                    .setCellValue(employee.getName());

            row.createCell(1)
                    .setCellValue(employee.getEmail());

            Cell dateOfBirthCell = row.createCell(2);
            dateOfBirthCell.setCellValue(employee.getDateOfBirth());
            dateOfBirthCell.setCellStyle(dateCellStyle);

            row.createCell(3)
                    .setCellValue(employee.getSalary());
        }

        // Resize all columns to fit the content size
        for (int i = 0; i < columns.length; i++) {
            //refer to: https://stackoverflow.com/a/37070120
            sheet.setColumnWidth(i, 14);
        }

        // Write the output to a file
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream("poi-generated-file.xlsx");
            workbook.write(fileOut);
            fileOut.close();
            // Closing the workbook
            workbook.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

    }

    private void setupDataForSpreadsheet() {
        Calendar dateOfBirth = Calendar.getInstance();
        dateOfBirth.set(1992, 7, 21);
        employees.add(new Employee("Rajeev Singh", "rajeev@example.com",
                dateOfBirth.getTime(), 1200000.0));

        dateOfBirth.set(1965, 10, 15);
        employees.add(new Employee("Thomas cook", "thomas@example.com",
                dateOfBirth.getTime(), 1500000.0));

        dateOfBirth.set(1987, 4, 18);
        employees.add(new Employee("Steve Maiden", "steve@example.com",
                dateOfBirth.getTime(), 1800000.0));
    }
}
