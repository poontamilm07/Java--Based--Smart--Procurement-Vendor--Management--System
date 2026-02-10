package com.procurement.procurement.controller.procurement;

import com.procurement.procurement.entity.procurement.Approval;
import com.procurement.procurement.entity.procurement.PurchaseOrder;
import com.procurement.procurement.entity.user.User;
import com.procurement.procurement.repository.procurement.ApprovalRepository;
import com.procurement.procurement.repository.procurement.PurchaseOrderRepository;
import com.procurement.procurement.repository.user.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/procurement/approval")
public class ApprovalController {

    private final ApprovalRepository approvalRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final UserRepository userRepository;

    public ApprovalController(ApprovalRepository approvalRepository,
                              PurchaseOrderRepository purchaseOrderRepository,
                              UserRepository userRepository) {
        this.approvalRepository = approvalRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.userRepository = userRepository;
    }

    // ===================== GET ALL APPROVALS =====================
    @GetMapping("/all")
    public ResponseEntity<List<Approval>> getAllApprovals() {
        return ResponseEntity.ok(approvalRepository.findAll());
    }

    // ===================== GET APPROVALS BY STATUS =====================
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Approval>> getApprovalsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(
                approvalRepository.findByStatus(status.toUpperCase())
        );
    }

    // ===================== GET APPROVALS BY PURCHASE ORDER =====================
    @GetMapping("/purchase-order/{poId}")
    public ResponseEntity<List<Approval>> getApprovalsByPurchaseOrder(@PathVariable Long poId) {
        return ResponseEntity.ok(
                approvalRepository.findByPurchaseOrderId(poId)
        );
    }

    // ===================== APPROVE PURCHASE ORDER =====================
    @PostMapping("/approve/{poId}")
    public ResponseEntity<String> approvePurchaseOrder(
            @PathVariable Long poId,
            @RequestParam Long approverId
    ) {
        PurchaseOrder po = purchaseOrderRepository.findById(poId)
                .orElse(null);

        if (po == null) {
            return ResponseEntity.badRequest().body("Purchase Order not found");
        }

        User approver = userRepository.findById(approverId)
                .orElse(null);

        if (approver == null) {
            return ResponseEntity.badRequest().body("Approver user not found");
        }

        if (!"PENDING".equalsIgnoreCase(po.getStatus())) {
            return ResponseEntity.badRequest()
                    .body("Purchase Order already processed");
        }

        Approval approval = new Approval();
        approval.setPurchaseOrder(po);
        approval.setApprover(approver);
        approval.setStatus("APPROVED");
        approval.setApprovedAt(LocalDateTime.now());

        approvalRepository.save(approval);

        po.setStatus("APPROVED");
        purchaseOrderRepository.save(po);

        return ResponseEntity.ok("Purchase Order approved successfully");
    }

    // ===================== REJECT PURCHASE ORDER =====================
    @PostMapping("/reject/{poId}")
    public ResponseEntity<String> rejectPurchaseOrder(
            @PathVariable Long poId,
            @RequestParam Long approverId,
            @RequestParam String reason
    ) {
        PurchaseOrder po = purchaseOrderRepository.findById(poId)
                .orElse(null);

        if (po == null) {
            return ResponseEntity.badRequest().body("Purchase Order not found");
        }

        User approver = userRepository.findById(approverId)
                .orElse(null);

        if (approver == null) {
            return ResponseEntity.badRequest().body("Approver user not found");
        }

        Approval approval = new Approval();
        approval.setPurchaseOrder(po);
        approval.setApprover(approver);
        approval.setStatus("REJECTED");
        approval.setComments(reason);
        approval.setApprovedAt(LocalDateTime.now());

        approvalRepository.save(approval);

        po.setStatus("REJECTED");
        purchaseOrderRepository.save(po);

        return ResponseEntity.ok("Purchase Order rejected successfully");
    }
}
