package com.procurement.procurement.controller.procurement;

import com.procurement.procurement.entity.procurement.Requisition;
import com.procurement.procurement.repository.procurement.RequisitionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/procurement/requisition")
public class RequisitionController {

    private final RequisitionRepository requisitionRepository;

    public RequisitionController(RequisitionRepository requisitionRepository) {
        this.requisitionRepository = requisitionRepository;
    }

    // ===================== CREATE REQUISITION =====================
    @PostMapping("/create")
    public ResponseEntity<?> createRequisition(@RequestBody Requisition requisition) {

        // Basic validation
        if (requisition.getRequestedBy() == null ||
                requisition.getRequestedBy().getId() == null) {
            return ResponseEntity.badRequest()
                    .body("requestedBy (user id) is required");
        }

        if (requisition.getRequisitionNumber() == null ||
                requisition.getRequisitionNumber().isBlank()) {
            return ResponseEntity.badRequest()
                    .body("requisitionNumber is required");
        }

        requisition.setStatus("PENDING");

        Requisition saved = requisitionRepository.save(requisition);
        return ResponseEntity.ok(saved);
    }

    // ===================== GET ALL REQUISITIONS =====================
    @GetMapping("/all")
    public ResponseEntity<List<Requisition>> getAllRequisitions() {
        return ResponseEntity.ok(requisitionRepository.findAll());
    }

    // ===================== GET REQUISITION BY ID =====================
    @GetMapping("/{id}")
    public ResponseEntity<?> getRequisitionById(@PathVariable Long id) {
        Optional<Requisition> reqOpt = requisitionRepository.findById(id);

        if (reqOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Requisition not found");
        }

        return ResponseEntity.ok(reqOpt.get());
    }

    // ===================== UPDATE REQUISITION STATUS =====================
    @PatchMapping("/update-status/{id}")
    public ResponseEntity<?> updateRequisitionStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        Optional<Requisition> reqOpt = requisitionRepository.findById(id);

        if (reqOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Requisition not found");
        }

        Requisition requisition = reqOpt.get();
        requisition.setStatus(status);
        requisitionRepository.save(requisition);

        return ResponseEntity.ok(
                "Requisition status updated to " + status
        );
    }
}
