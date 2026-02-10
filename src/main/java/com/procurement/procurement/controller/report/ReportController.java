package com.procurement.procurement.controller.report;

import com.procurement.procurement.dto.report.ReportRequestDTO;
import com.procurement.procurement.service.report.ReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping(value = "/vendor", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generateVendorReport(
            @RequestBody ReportRequestDTO request,
            @RequestParam(defaultValue = "pdf") String format
    ) {
        byte[] data = reportService.generateVendorReport(request, format);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=vendor_report." +
                                (format.equalsIgnoreCase("excel") ? "xlsx" : "pdf"))
                .contentType(
                        format.equalsIgnoreCase("excel")
                                ? MediaType.parseMediaType(
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                                : MediaType.APPLICATION_PDF
                )
                .body(data);
    }
}
