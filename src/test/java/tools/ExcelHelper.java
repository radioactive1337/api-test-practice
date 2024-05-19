package tools;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelHelper {
    public static List<Object[]> readExcelFile(String filePath) {
        List<Object[]> data = new ArrayList<>();
        try (FileInputStream file = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            //  Skip the title, if there is one
            if (rows.hasNext()) rows.next();

            //  extract data from xlsx to list
            while (rows.hasNext()) {
                Row row = rows.next();
                List<Object> rowData = new ArrayList<>();
                for (Cell cell : row) {
                    switch (cell.getCellType()) {
                        case STRING:
                            rowData.add(cell.getStringCellValue());
                            break;
                        case NUMERIC:
                            rowData.add(cell.getNumericCellValue());
                            break;
                        case BOOLEAN:
                            rowData.add(cell.getBooleanCellValue());
                            break;
                        case FORMULA:
                            switch (cell.getCachedFormulaResultType()) {
                                case NUMERIC:
                                    rowData.add(cell.getNumericCellValue());
                                    break;
                                case STRING:
                                    rowData.add(cell.getRichStringCellValue().getString());
                                    break;
                            }
                            break;
                        default:
                            rowData.add(null);
                    }
                }
                data.add(rowData.toArray());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}