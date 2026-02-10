package com.procurement.procurement.service.procurement;

import com.procurement.procurement.entity.procurement.Approval;
import com.procurement.procurement.entity.procurement.PurchaseOrder;
import com.procurement.procurement.entity.user.User;
import com.procurement.procurement.repository.procurement.ApprovalRepository;
import com.procurement.procurement.repository.procurement.PurchaseOrderRepository;
import com.procurement.procurement.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ApprovalService {

    private final ApprovalRepository approvalRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final UserRepository userRepository;

    public ApprovalService(ApprovalRepository approvalRepository,
                           PurchaseOrderRepository purchaseOrderRepository,
                           UserRepository userRepository) {
        this.approvalRepository = approvalRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.userRepository = userRepository;
    }

    // ===================== APPROVE PURCHASE ORDER =====================
    public void approvePurchaseOrder(Long poId, Long approverId) {

        PurchaseOrder po = purchaseOrderRepository.findById(poId)
                .orElseThrow(() ->
                        new RuntimeException("Purchase Order not found with id: " + poId));

        if (!"PENDING".equalsIgnoreCase(po.getStatus())) {
            throw new RuntimeException("Purchase Order already " + po.getStatus());
        }

        User approver = userRepository.findById(approverId)
                .orElseThrow(() ->
                        new RuntimeException("Approver not found with id: " + approverId));

        Approval approval = new Approval();
        approval.setPurchaseOrder(po);
        approval.setApprover(approver);
        approval.setStatus("APPROVED");
        approval.setApprovedAt(LocalDateTime.now());

        approvalRepository.save(approval);

        po.setStatus("APPROVED");
        purchaseOrderRepository.save(po);
    }

    // ===================== REJECT PURCHASE ORDER =====================
    public void rejectPurchaseOrder(Long poId, Long approverId, String reason) {

        PurchaseOrder po = purchaseOrderRepository.findById(poId)
                .orElseThrow(() ->
                        new RuntimeException("Purchase Order not found with id: " + poId));

        if (!"PENDING".equalsIgnoreCase(po.getStatus())) {
            throw new RuntimeException("Purchase Order already " + po.getStatus());
        }

        User approver = userRepository.findById(approverId)
                .orElseThrow(() ->
                        new RuntimeException("Approver not found with id: " + approverId));

        Approval approval = new Approval();
        approval.setPurchaseOrder(po);
        approval.setApprover(approver);
        approval.setStatus("REJECTED");
        approval.setComments(reason);
        approval.setApprovedAt(LocalDateTime.now());

        approvalRepository.save(approval);

        po.setStatus("REJECTED");
        purchaseOrderRepository.save(po);
    }

    // ===================== GET ALL APPROVALS =====================
    public List<Approval> getAllApprovals() {
        return approvalRepository.findAll();
    }
}
