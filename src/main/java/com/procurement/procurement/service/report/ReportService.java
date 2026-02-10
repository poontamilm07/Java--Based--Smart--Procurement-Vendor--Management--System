package com.procurement.procurement.service.report;

import com.procurement.procurement.dto.report.ReportRequestDTO;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    // ===================== COMMON JASPER GENERATOR =====================
    public JasperPrint generateReport(
            String reportPath,
            List<?> data,
            Map<String, Object> parameters) throws JRException {

        if (data == null || data.isEmpty()) {
            throw new JRException("No data available for report");
        }

        JRBeanCollectionDataSource dataSource =
                new JRBeanCollectionDataSource(data);

        // Jasper requires mutable params
        Map<String, Object> reportParams =
                parameters != null ? new HashMap<>(parameters) : new HashMap<>();

        InputStream reportStream = getClass().getResourceAsStream(reportPath);
        if (reportStream == null) {
            throw new JRException("JRXML not found at path: " + reportPath);
        }

        JasperReport jasperReport =
                JasperCompileManager.compileReport(reportStream);

        return JasperFillManager.fillReport(
                jasperReport,
                reportParams,
                dataSource
        );
    }

    // ===================== EXPORT PDF =====================
    public byte[] exportReportToPdf(JasperPrint jasperPrint)
            throws JRException {

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    // ===================== EXPORT EXCEL =====================
    public byte[] exportReportToExcel(JasperPrint jasperPrint)
            throws JRException {

        JRXlsxExporter exporter = new JRXlsxExporter();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        exporter.setExporterInput(
                new SimpleExporterInput(jasperPrint)
        );
        exporter.setExporterOutput(
                new SimpleOutputStreamExporterOutput(outputStream)
        );

        // ✅ Recommended Excel settings
        exporter.setConfiguration(new net.sf.jasperreports.export.SimpleXlsxReportConfiguration() {{
            setDetectCellType(true);
            setCollapseRowSpan(false);
            setWhitePageBackground(false);
        }});

        exporter.exportReport();
        return outputStream.toByteArray();
    }

    // ===================== VENDOR REPORT =====================
    public byte[] generateVendorReport(
            ReportRequestDTO request,
            String format) {

        try {
            // 🔹 TEMP DUMMY DATA (safe for testing)
            List<Map<String, Object>> data = List.of(
                    Map.of("vendorName", "ABC Traders", "rating", 4.5, "status", "ACTIVE"),
                    Map.of("vendorName", "XYZ Supplies", "rating", 4.2, "status", "ACTIVE")
            );

            Map<String, Object> params = new HashMap<>();
            params.put("title", "Vendor Report");

            JasperPrint jasperPrint = generateReport(
                    "/jasper/vendor_report.jrxml",
                    data,
                    params
            );

            if ("excel".equalsIgnoreCase(format)) {
                return exportReportToExcel(jasperPrint);
            }

            return exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            // 🔥 SHOW REAL ERROR IN LOGS
            e.printStackTrace();
            throw new RuntimeException("Report generation failed: " + e.getMessage());
        }
    }
}
