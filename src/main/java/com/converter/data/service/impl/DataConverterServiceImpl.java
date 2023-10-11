package com.converter.data.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.converter.data.dto.ExampleDataDTO;
import com.converter.data.dto.OrdineDTO;
import com.converter.data.entity.Ordine;
import com.converter.data.entity.PrintSize;
import com.converter.data.mapper.OrdineMapper;
import com.converter.data.model.MisuraStampa;
import com.converter.data.model.TipoAbbigliamento;
import com.converter.data.repository.OrdineRepository;
import com.converter.data.repository.PrintSizeRepository;
import com.converter.data.service.DataConverterService;
import com.converter.data.utils.ExcelGenerator;

@Service
public class DataConverterServiceImpl implements DataConverterService {

    @Autowired
    protected OrdineRepository ordineRepository;

    @Autowired
    protected PrintSizeRepository printSizeRepository;

    @Override
    public List<Ordine> findAll() {
        return ordineRepository.findAll();
    }

    @Override
    public List<OrdineDTO> insertOrders(List<ExampleDataDTO> orders) {
        List<Ordine> ordersNTT = new ArrayList<>();

        List<Long> existingOrderNumber = ordineRepository.findDistinctOrderNumber();

        Long orderNumber = generaNumeroEscluso(existingOrderNumber);

        for (ExampleDataDTO order : orders) {
            Ordine ordine = Ordine.builder().customer_name(order.getNomeCliente())
                    .customer_surname(order.getCognomeCliente()).customer_phone_number(order.getCellulareCliente())
                    .clothing_type(order.getTipoAbbigliamento()).quantity(order.getQuantita()).order_number(orderNumber)
                    .clothing_size(order.getTagliaAbbigliamento().toString()).build();

            Ordine ord = ordineRepository.save(ordine);
            ordersNTT.add(ord);

            for (MisuraStampa misuraStampa : order.getMisuraStampa()) {
                PrintSize printSize = PrintSize.builder()
                        .order_id(ord.getId())
                        .size(misuraStampa).build();
                printSizeRepository.save(printSize);
            }

        }

        return ordersNTT.stream().map((ordine) -> OrdineMapper.INSTANCE.entityToDto(ordine))
                .collect(Collectors.toList());
    }

    public static Long generaNumeroEscluso(List<Long> listaEsclusi) {
        Random rand = new Random();
        Long numeroCasuale;

        do {
            numeroCasuale = (long) (rand.nextInt(10000 - 1000 + 1) + 1000);
        } while (listaEsclusi.contains(numeroCasuale));

        return numeroCasuale;
    }

    @Override
    public byte[] generateExcel(List<ExampleDataDTO> orders) throws IOException {

        ExcelGenerator generator = new ExcelGenerator(orders);

        // Ottieni l'InputStream dal generatore
        InputStream inputStream = generator.generateAsInputStream();

        // Leggi l'InputStream in un array di byte
        byte[] excelBytes = org.apache.commons.io.IOUtils.toByteArray(inputStream);

        return excelBytes;
    }

    @Override
    public byte[] generateExcelByDB() throws IOException {
        // Crea un nuovo workbook ogni volta che il metodo viene chiamato
        XSSFWorkbook workbook = new XSSFWorkbook();

        // Ottieni l'InputStream dal generatore
        InputStream inputStream = generateAsInputStream(workbook);

        // Leggi l'InputStream in un array di byte
        byte[] excelBytes;
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            org.apache.commons.io.IOUtils.copy(inputStream, byteArrayOutputStream);
            excelBytes = byteArrayOutputStream.toByteArray();
        }

        // Chiudi il workbook e rilascia le risorse
        workbook.close();

        return excelBytes;
    }

    private InputStream generateAsInputStream(XSSFWorkbook workbook) throws IOException {
        XSSFSheet sheet = workbook.createSheet("Ordine");
        writeHeader(sheet);
        write(sheet);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);

        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    private void writeHeader(XSSFSheet sheet) {
        Row row = sheet.createRow(0);

        CellStyle style = sheet.getWorkbook().createCellStyle();
        XSSFFont font = sheet.getWorkbook().createFont();
        font.setBold(true);
        font.setFontHeight(16);
        font.setColor(IndexedColors.RED.getIndex());
        style.setFont(font);

        createCell(row, 0, "Nome cliente", style, sheet);
        createCell(row, 1, "Cognome cliente", style, sheet);
        createCell(row, 2, "Cellulare cliente", style, sheet);
        createCell(row, 3, "Tipo abbigliamento", style, sheet);
        createCell(row, 4, "Taglia", style, sheet);
        createCell(row, 5, "Tipi stampa", style, sheet);
        createCell(row, 6, "Quantità", style, sheet);
        createCell(row, 7, "Costo totale", style, sheet);
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style, XSSFSheet sheet) {
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

    private void write(XSSFSheet sheet) {
        int rowCount = 1;

        CellStyle style = sheet.getWorkbook().createCellStyle();
        XSSFFont font = sheet.getWorkbook().createFont();
        font.setFontHeight(14);
        style.setFont(font);

        List<Ordine> ordines = ordineRepository.findAll();

        for (Ordine record : ordines) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            List<MisuraStampa> misuraStampas = printSizeRepository.findSizeByOrderId(record.getId());

            String misuraStampa = "empty";

            if (!misuraStampas.isEmpty()) {
                misuraStampa = convertListToString(misuraStampas);
            }

            createCell(row, columnCount++, record.getCustomer_name(), style, sheet);
            createCell(row, columnCount++, record.getCustomer_surname(), style, sheet);
            createCell(row, columnCount++, record.getCustomer_phone_number(), style, sheet);
            createCell(row, columnCount++, String.valueOf(record.getClothing_type()), style, sheet);
            createCell(row, columnCount++, String.valueOf(record.getClothing_size()), style, sheet);
            createCell(row, columnCount++, misuraStampa, style, sheet);
            createCell(row, columnCount++, record.getQuantity(), style, sheet);
            createCell(row, columnCount++, calcoloCostoTotale(record, misuraStampas), style, sheet);
        }
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

    private String calcoloCostoTotale(Ordine data, List<MisuraStampa> misuraStampas) {
        double costoTotale = 0;

        for (MisuraStampa misura : misuraStampas) {
            if (misura.equals(MisuraStampa.GRANDE)) {
                costoTotale = costoTotale + 10;
            } else if (misura.equals(MisuraStampa.MEDIA)) {
                costoTotale = costoTotale + 7;
            } else if (misura.equals(MisuraStampa.PICCOLA)) {
                costoTotale = costoTotale + 5;
            }
        }

        if (data.getClothing_type().equals(TipoAbbigliamento.FELPA_CON_CAPPUCCIO)) {
            costoTotale = costoTotale + 18;
        } else if (data.getClothing_type().equals(TipoAbbigliamento.FELPA_SENZA_CAPPUCCIO)) {
            costoTotale = costoTotale + 15;
        } else if (data.getClothing_type().equals(TipoAbbigliamento.MAGLIA)) {
            costoTotale = costoTotale + 10;
        }

        costoTotale = costoTotale * data.getQuantity();

        return String.valueOf(costoTotale) + "€";
    }
}
