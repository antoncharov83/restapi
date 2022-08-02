package ru.antoncharov.restapi.storage;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.PropertyTemplate;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.antoncharov.restapi.model.GeologicalClass;
import ru.antoncharov.restapi.model.Section;
import ru.antoncharov.restapi.model.dto.GeologicalClassDto;
import ru.antoncharov.restapi.model.dto.SectionDto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class XlsxHelper {

    private String filename;

    public List<SectionDto> readFromExcel(String file) throws Exception {

        Workbook ExcelBook = new XSSFWorkbook(new FileInputStream(file));
        Sheet sheet = ExcelBook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.rowIterator();

        if(!rowIterator.hasNext()){
            throw new Exception("Неверный формат файла");
        }

        rowIterator.next();

        if(!rowIterator.hasNext()){
            throw new Exception("Неверный формат файла");
        }

        List<SectionDto> sections = new ArrayList<>();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            Iterator<Cell> cellIterator = row.cellIterator();
            Cell cell = cellIterator.next();

            SectionDto section = new SectionDto();
            if(cell.getCellType() == CellType.NUMERIC){
                section.setName(String.valueOf(cell.getNumericCellValue()));
            }
            else {
                section.setName(cell.getStringCellValue());
            }

            if(!cellIterator.hasNext()){
                throw new Exception("Неверный формат файла");
            }
            List<GeologicalClassDto> geologicalClasses = new ArrayList<>();
            while (cellIterator.hasNext()) {
                cell = cellIterator.next();

                GeologicalClassDto geologicalClass = new GeologicalClassDto();
                if(cell.getCellType() == CellType.NUMERIC){
                    geologicalClass.setName(String.valueOf(cell.getNumericCellValue()));
                }
                else {
                    geologicalClass.setName(cell.getStringCellValue());
                }

                if(!cellIterator.hasNext()){
                    throw new Exception("Неверный формат файла");
                }

                cell = cellIterator.next();
                if(cell.getCellType() == CellType.NUMERIC){
                    geologicalClass.setCode(String.valueOf(cell.getNumericCellValue()));
                }
                else {
                    geologicalClass.setCode(cell.getStringCellValue());
                }
                geologicalClasses.add(geologicalClass);
            }
            section.setGeologicalClasses(geologicalClasses);
            sections.add(section);
        }
        return sections;
    }

    public String generateXlsx(List<Section> sections, UUID idJob) throws IOException {
        Workbook ExcelBook = new XSSFWorkbook();
        Sheet sheet = ExcelBook.createSheet();
        Row row = sheet.createRow((short)0);
        Cell cell = row.createCell(0);

        Font font = ExcelBook.createFont();
        font.setBold(true);
        CellStyle style = ExcelBook.createCellStyle();
        style.setFont(font);
        style.setWrapText(true);
        style.setAlignment(HorizontalAlignment.CENTER);

        cell.setCellStyle(style);
        cell.setCellValue("Section Name");

        int columnNum = getMaxLength(sections)*2;
        for(int i = 1; i <= columnNum; i++) {
            cell = row.createCell(i);
            cell.setCellValue(i % 2 == 1 ?
                            "class " + i % 2 + i / 2 + " name" :
                            "class " + i % 2 + i / 2 + " code");
            cell.setCellStyle(style);
        }
        short rowNum = 1;
        for (Section section : sections) {
            row = sheet.createRow(rowNum++);
            cell = row.createCell(0);
            cell.setCellValue(section.getName());
            int cellNum = 1;
            for (GeologicalClass geologicalClass : section.getGeologicalClasses()) {
                cell = row.createCell(cellNum++);
                cell.setCellValue(geologicalClass.getName());
                cell = row.createCell(cellNum++);
                cell.setCellValue(geologicalClass.getCode());
            }
        }

        for(int i = 0; i <= columnNum; i++){
            sheet.autoSizeColumn(i);
        }

        PropertyTemplate propertyTemplate=new PropertyTemplate();
        propertyTemplate.drawBorders(new CellRangeAddress(0,0,0,columnNum),
                BorderStyle.MEDIUM,BorderExtent.ALL);
        propertyTemplate.drawBorders(new CellRangeAddress(0,sections.size(),0,0),
                BorderStyle.MEDIUM,BorderExtent.ALL);
        propertyTemplate.drawBorders(new CellRangeAddress(0,sections.size(),0,columnNum),
                BorderStyle.MEDIUM,BorderExtent.BOTTOM);
        propertyTemplate.applyBorders(sheet);

        filename = "filestorage/workbook_"+idJob+".xlsx";
        File file = new File(filename);
        file.getParentFile().mkdirs();
        file.createNewFile();
        FileOutputStream fileOut = new FileOutputStream(file);
        ExcelBook.write(fileOut);
        fileOut.close();
        ExcelBook.close();
        return filename;
    }

    @Transactional
    int getMaxLength(List<Section> sections){
        Comparator<Section> comparator = Comparator.comparingInt(s -> s.getGeologicalClasses().size());
        int maxSize = sections.stream().max(comparator).get().getGeologicalClasses().size();

        return maxSize;
    }
}
