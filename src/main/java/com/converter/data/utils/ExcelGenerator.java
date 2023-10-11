package com.converter.data.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import com.converter.data.dto.ExampleDataDTO;
import com.converter.data.model.MisuraStampa;
import com.converter.data.model.TipoAbbigliamento;
import com.converter.data.repository.PrintSizeRepository;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ExcelGenerator {

    private List<ExampleDataDTO> listRecords;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    
    @Autowired
    protected PrintSizeRepository printSizeRepository;

    public ExcelGenerator(List<ExampleDataDTO> listRecords) {
        this.listRecords = listRecords;
        workbook = new XSSFWorkbook();
    }

    private void writeHeader() {
        sheet = workbook.createSheet("Ordine");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        font.setColor(IndexedColors.RED.getIndex());
        style.setFont(font);

        createCell(row, 0, "Nome cliente", style);
        createCell(row, 1, "Cognome cliente", style);
        createCell(row, 2, "Cellulare cliente", style);
        createCell(row, 3, "Tipo abbigliamento", style);
        createCell(row, 4, "Taglia", style);
        createCell(row, 5, "Tipi stampa", style);
        createCell(row, 6, "Quantità", style);
        createCell(row, 7, "Costo totale", style);
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void write() {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (ExampleDataDTO record : listRecords) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, record.getNomeCliente(), style);
            createCell(row, columnCount++, record.getCognomeCliente(), style);
            createCell(row, columnCount++, record.getCellulareCliente(), style);
            createCell(row, columnCount++, String.valueOf(record.getTipoAbbigliamento()), style);
            createCell(row, columnCount++, String.valueOf(record.getTagliaAbbigliamento()), style);
            createCell(row, columnCount++, convertListToString(record.getMisuraStampa()), style);
            createCell(row, columnCount++, record.getQuantita(), style);
            createCell(row, columnCount++, calcoloCostoTotale(record), style);
        }
    }

    public InputStream generateAsInputStream() throws IOException {
        writeHeader();
        write();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        workbook.close();

        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    public String convertListToString(List<MisuraStampa> stampe) {
        String str = "";
        for (MisuraStampa stampa : stampe) {
            if (stampa.equals(MisuraStampa.GRANDE)) {
                str += String.valueOf(stampa) + "(10€)" + "," + " ";
            } else if (stampa.equals(MisuraStampa.MEDIA)) {
                str += String.valueOf(stampa) + "(7€)" + "," + " ";
            } else if (stampa.equals(MisuraStampa.PICCOLA)) {
                str += String.valueOf(stampa) + "(5€)" + "," + " ";
            }
        }
        str = str.substring(0, str.length() - 1);
        return str;
    }

    private String calcoloCostoTotale(ExampleDataDTO data) {
        double costoTotale = 0;

        for (MisuraStampa misura : data.getMisuraStampa()) {
            if (misura.equals(MisuraStampa.GRANDE)) {
                costoTotale = costoTotale + 10;
            } else if (misura.equals(MisuraStampa.MEDIA)) {
                costoTotale = costoTotale + 7;
            } else if (misura.equals(MisuraStampa.PICCOLA)) {
                costoTotale = costoTotale + 5;
            }
        }

        if (data.getTipoAbbigliamento().equals(TipoAbbigliamento.FELPA_CON_CAPPUCCIO)) {
            costoTotale = costoTotale + 18;
        } else if (data.getTipoAbbigliamento().equals(TipoAbbigliamento.FELPA_SENZA_CAPPUCCIO)) {
            costoTotale = costoTotale + 15;
        } else if (data.getTipoAbbigliamento().equals(TipoAbbigliamento.MAGLIA)) {
            costoTotale = costoTotale + 10;
        }

        costoTotale = costoTotale * data.getQuantita();

        return String.valueOf(costoTotale) + "€";
    }
}
