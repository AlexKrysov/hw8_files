package com.krysov;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.krysov.model.MenuJson;

import static org.assertj.core.api.Assertions.assertThat;


public class FilesParsingTest {

    static ClassLoader cl = FilesParsingTest.class.getClassLoader();

    @Test
    public void zipTest() throws Exception {
        {
            try (
                    InputStream resources = cl.getResourceAsStream("example/hw8.zip");
                    ZipInputStream zis = new ZipInputStream(resources)
            ) {
                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    if (entry.getName().contains("hw8.pdf")) {
                        PDF contentPDF = new PDF(zis);
                        assertThat(contentPDF.text).contains("высшее профессиональное");
                    } else if (entry.getName().contains("hw8.xlsx")) {
                        XLS contentXLS = new XLS(zis);
                        assertThat(contentXLS.excel.getSheetAt(0).getRow(11).getCell(2).getStringCellValue()).contains("Bouska");
                    } else if (entry.getName().contains("hw8.csv")) {
                        CSVReader reader = new CSVReader(new InputStreamReader(zis));
                        List<String[]> contentCSV = reader.readAll();
                        assertThat(contentCSV.get(3)[2]).contains("Лукашенко");
                    }

                }

            }
        }
    }

    @Test
    void jsonParseTest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        try (
                InputStream resources = cl.getResourceAsStream("example/hw8.json");
        ) {
            MenuJson menuJson = objectMapper.readValue(resources, MenuJson.class);
            assertThat(menuJson.menu).isTrue();
            assertThat(menuJson.id).isEqualTo(2022);
            assertThat(menuJson.value).isEqualTo("File");
            assertThat(menuJson.menuitem.get(0).value).isEqualTo("New");
            assertThat(menuJson.menuitem.get(1).value).isEqualTo("Open");
            assertThat(menuJson.menuitem.get(2).value).isEqualTo("Save");
            assertThat(menuJson.menuitem.get(0).onclick).isEqualTo("CreateDoc()");
            assertThat(menuJson.menuitem.get(1).onclick).isEqualTo("OpenDoc()");
            assertThat(menuJson.menuitem.get(2).onclick).isEqualTo("SaveDoc()");
        }
    }
}
