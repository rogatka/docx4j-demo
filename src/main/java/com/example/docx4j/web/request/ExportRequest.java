package com.example.docx4j.web.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ExportRequest {

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String salutation;

    @NotNull
    private String message;
}
