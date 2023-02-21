package com.example.docx4j.web;

import com.example.docx4j.utils.FileUtils;
import com.example.docx4j.web.request.ExportRequest;
import jakarta.validation.Valid;
import org.docx4j.model.datastorage.migration.VariablePrepare;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

import static java.util.Map.entry;

@RestController
@RequestMapping("/export")
public class ExporterApi {

    private static final String DOCX_TEMPLATE_FILE_PATH = "templates/docx/template.docx";
    private static final String TEMPLATE_DOCX = "template.docx";

    @PostMapping
    public ResponseEntity<byte[]> exportToDocx(@RequestBody @Valid ExportRequest request) {
        try (InputStream is = FileUtils.getFileContentAsStream(DOCX_TEMPLATE_FILE_PATH)) {
            WordprocessingMLPackage wordMLPackage;
            wordMLPackage = WordprocessingMLPackage.load(is);
            MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();

            VariablePrepare.prepare(wordMLPackage);
            Map<String, String> variables = Map.ofEntries(
                    entry("firstName", request.getFirstName()),
                    entry("lastName", request.getLastName()),
                    entry("message", request.getMessage()),
                    entry("salutation", request.getSalutation()));
            documentPart.variableReplace(variables);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            wordMLPackage.save(outputStream);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + TEMPLATE_DOCX)
                    .header(HttpHeaders.CONTENT_TYPE, "application/x-download")
                    .body(outputStream.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
