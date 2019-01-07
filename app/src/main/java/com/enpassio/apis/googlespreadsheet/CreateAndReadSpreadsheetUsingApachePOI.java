package com.enpassio.apis.googlespreadsheet;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.enpassio.apis.R;
import com.enpassio.apis.googlespreadsheet.model.Employee;
import com.enpassio.apis.googlespreadsheet.model.ValueRange;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
    // Write the output to a file
    FileOutputStream fileOut;
    ValueRange valueRange = new ValueRange();
    private List<List<String>> listList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_and_read_spreadsheet_using_apache_poi);

        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");

        valueRange = getIntent().getExtras().getParcelable("value_extras");
        try {
            createWorkbookFromAvailableData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        writeDataButton = findViewById(R.id.write_data_to_spreadsheet_button);
        readDataButton = findViewById(R.id.read_data_from_spreadsheet_button);
        spreadsheetDataTextView = findViewById(R.id.spreadsheet_data_text_view);

        setupDataForSpreadsheet();
        writeDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    createWorkbookFromAvailableData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS); // Folder Name
        File myFile = new File(folder, "poi-generated-file.xlsx"); // Filename

        // Obtain a workbook from the excel file
        Workbook workbook = WorkbookFactory.create(myFile);

        // Get Sheet at index 0
        Sheet sheet = workbook.getSheetAt(0);

        String data = "";
        for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
            // Get Row at index 1
            Row row = sheet.getRow(i);
            for (int j = 0; j < row.getLastCellNum(); j++) {

                Cell currentCell = row.getCell(j);
                if (currentCell.getCellTypeEnum() == CellType.STRING) {
                    data += currentCell.getStringCellValue() + " __ ";
                } else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                    data += currentCell.getNumericCellValue() + " __ ";
                }
            }
            data += "\n \n \n";
        }
        spreadsheetDataTextView.setText(data);

        // Closing the workbook
        workbook.close();
    }

    private void createWorkbookFromAvailableData() throws IOException {
        // Create a Workbook
        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);// Folder Name
        File myFile = new File(folder, "poi-generated-file.xlsx");// Filename
        Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

        /* CreationHelper helps us create instances of various things like DataFormat,
           Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way */
        CreationHelper createHelper = workbook.getCreationHelper();


        // Create a Sheet
        Sheet sheet = workbook.createSheet("Employee");

        // Create a Font for styling header cells
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 8);
        headerFont.setColor(IndexedColors.RED.getIndex());

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Create a Row
        Row headerRow = sheet.createRow(0);

//        // Create cells
//        for (int i = 0; i < columns.length; i++) {
//            Cell cell = headerRow.createCell(i);
//            cell.setCellValue(columns[i]);
//            cell.setCellStyle(headerCellStyle);
//        }
        // Create cells
        if (valueRange.getValues() != null && valueRange.getValues().size() > 0) {
            List<String> headerCells = valueRange.getValues().get(0);
            for (int i = 0; i < headerCells.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headerCells.get(i));
                cell.setCellStyle(headerCellStyle);
            }
        }
        //creating other rows
        if (valueRange.getValues() != null && valueRange.getValues().size() > 0) {
            for (int i = 1; i < valueRange.getValues().size(); i++) {
                if (valueRange.getValues().get(i).size() > 0) {
                    Log.v("my_taggsss", "Inside ROW Spreadsheet data value is: " + valueRange.getValues().get(i));
                }
                // Create cells
                List<String> currentCell = valueRange.getValues().get(i);
                Row row = sheet.createRow(i);
                for (int j = 0; j < currentCell.size(); j++) {
                    row.createCell(j)
                            .setCellValue(currentCell.get(j));
                    if (valueRange.getValues().get(i).size() > 0) {
                        Log.v("my_taggsss", "Inside COLUMN Spreadsheet data value is: " + valueRange.getValues().get(i));
                    }
                }
            }
        }
//        for (int i = 0; i < valueRange.getValues().size(); i++) {
//            if (valueRange.getValues().get(i).size() > 0) {
//                Log.v("my_taggsss", "Inside getIntent Spreadsheet data value is: " + valueRange.getValues().get(i));
//            }
//        }
//        // Create Cell Style for formatting Date
//        CellStyle dateCellStyle = workbook.createCellStyle();
//        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));
//
////        // Create Other rows and cells with employees data
////        int rowNum = 1;
////        Log.d("my_tagggg", "employees size is: " + employees.size());
////        for (Employee employee : employees) {
////            Row row = sheet.createRow(rowNum++);
////
////            row.createCell(0)
////                    .setCellValue(employee.getName());
////            Log.d("my_tagggg", "cell " + 0 + " row" + rowNum + " value is: " + employee.getName());
////            row.createCell(1)
////                    .setCellValue(employee.getEmail());
////            Log.d("my_tagggg", "cell " + 1 + " row" + rowNum + " value is: " + employee.getEmail());
////            Cell dateOfBirthCell = row.createCell(2);
////            dateOfBirthCell.setCellValue(employee.getDateOfBirth());
////            dateOfBirthCell.setCellStyle(dateCellStyle);
////            Log.d("my_tagggg", "cell " + 2 + " row" + rowNum + " value is: " + employee.getDateOfBirth());
////            row.createCell(3)
////                    .setCellValue(employee.getSalary());
////            Log.d("my_tagggg", "cell " + 3 + " row" + rowNum + " value is: " + employee.getSalary());
////        }

        // Resize all columns to fit the content size
        for (int i = 0; i < columns.length; i++) {
            //refer to: https://stackoverflow.com/a/37070120
            sheet.setColumnWidth(7, 10);
        }

        try {
            fileOut = new FileOutputStream(myFile);
            workbook.write(fileOut);
            // Closing the workbook
            fileOut.close();
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
        //second set of data
        dateOfBirth.set(1992, 7, 21);
        employees.add(new Employee("Rajeev Singh", "rajeev@example.com",
                dateOfBirth.getTime(), 1200000.0));

        dateOfBirth.set(1965, 10, 15);
        employees.add(new Employee("Thomas cook", "thomas@example.com",
                dateOfBirth.getTime(), 1500000.0));

        dateOfBirth.set(1987, 4, 18);
        employees.add(new Employee("Steve Maiden", "steve@example.com",
                dateOfBirth.getTime(), 1800000.0));
        //third set of data
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
