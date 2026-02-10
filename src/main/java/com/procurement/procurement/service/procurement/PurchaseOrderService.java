package com.procurement.procurement.service.procurement;

import com.procurement.procurement.entity.procurement.PurchaseOrder;
import com.procurement.procurement.entity.procurement.PurchaseOrderItem;
import com.procurement.procurement.entity.vendor.Vendor;
import com.procurement.procurement.repository.procurement.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseOrderService {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    // ===================== Create new Purchase Order =====================
    public PurchaseOrder createPurchaseOrder(PurchaseOrder purchaseOrder) {

        // 🔥 set parent reference for items
        if (purchaseOrder.getItems() != null) {
            purchaseOrder.getItems()
                    .forEach(item -> item.setPurchaseOrder(purchaseOrder));
        }

        // timestamps handled by @PrePersist
        return purchaseOrderRepository.save(purchaseOrder);
    }

    // ===================== Update Purchase Order =====================
    public PurchaseOrder updatePurchaseOrder(Long id, PurchaseOrder updatedPO) {

        PurchaseOrder po = purchaseOrderRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Purchase Order not found with id: " + id));

        // update vendor
        if (updatedPO.getVendor() != null) {
            po.setVendor(updatedPO.getVendor());
        }

        // update status
        if (updatedPO.getStatus() != null) {
            po.setStatus(updatedPO.getStatus());
        }

        // 🔥 SAFE update for items (orphanRemoval FIX)
        if (updatedPO.getItems() != null) {
            po.getItems().clear(); // keep same Hibernate collection

            for (PurchaseOrderItem item : updatedPO.getItems()) {
                item.setPurchaseOrder(po);
                po.getItems().add(item);
            }
        }

        // timestamps handled by @PreUpdate
        return purchaseOrderRepository.save(po);
    }

    // ===================== Get all Purchase Orders =====================
    public List<PurchaseOrder> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll();
    }

    // ===================== Get Purchase Orders by Vendor =====================
    public List<PurchaseOrder> getPurchaseOrdersByVendor(Vendor vendor) {
        return purchaseOrderRepository.findByVendor(vendor);
    }

    // ===================== Get Purchase Orders by Status =====================
    public List<PurchaseOrder> getPurchaseOrdersByStatus(String status) {
        return purchaseOrderRepository.findByStatus(status);
    }

    // ===================== Get Purchase Order by PO Number =====================
    public PurchaseOrder getPurchaseOrderByPoNumber(String poNumber) {
        return purchaseOrderRepository.findByPoNumber(poNumber)
                .orElseThrow(() ->
                        new RuntimeException("Purchase Order not found with PO number: " + poNumber));
    }
}
