package com.openchowski.carmanagement.exporter;

import com.openchowski.carmanagement.entity.Car;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.annotation.Bean;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public class CarExcelExporter {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Car> carlist;

    public CarExcelExporter(List<Car> carlist) {
        this.carlist = carlist;
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine(){
        sheet = workbook.createSheet("Cars");
        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();

        font.setBold(true);
        style.setFont(font);

        createCell(row, 0, "ID", style);
        createCell(row, 1, "Make", style);
        createCell(row, 2, "Model", style);
        createCell(row, 3, "Year", style);
        createCell(row, 4, "Status", style);


    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style){
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if(value instanceof Integer){
            cell.setCellValue((Integer) value);
        } else if(value instanceof Boolean){
            cell.setCellValue((Boolean) value);
        } else{
            cell.setCellValue((String) value);
        }

        cell.setCellStyle(style);

    }

    private void writeDataLines(){
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);


        for(Car tempCar : carlist) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, tempCar.getId(), style);
            createCell(row, columnCount++, tempCar.getMake(), style);
            createCell(row, columnCount++, tempCar.getModel(), style);
            createCell(row, columnCount++, tempCar.getYear(), style);
            createCell(row, columnCount++, tempCar.getStatus(), style);
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();

    }


}
