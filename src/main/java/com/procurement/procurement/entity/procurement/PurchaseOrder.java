package com.procurement.procurement.entity.procurement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.procurement.procurement.entity.vendor.Vendor;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "purchase_orders")
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String poNumber;

    @ManyToOne
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;

    private String status; // PENDING, APPROVED, REJECTED, COMPLETED

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ✅ ITEMS (DO NOT replace collection)
    @JsonIgnore
    @OneToMany(
            mappedBy = "purchaseOrder",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<PurchaseOrderItem> items = new ArrayList<>();

    // approvals ignored in JSON
    @JsonIgnore
    @OneToMany(
            mappedBy = "purchaseOrder",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Approval> approvals = new ArrayList<>();

    // ===================== AUTO TIMESTAMPS =====================
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ===================== GETTERS & SETTERS =====================

    public Long getId() {
        return id;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<PurchaseOrderItem> getItems() {
        return items;
    }

    // 🔥 MOST IMPORTANT FIX
    public void setItems(List<PurchaseOrderItem> items) {
        this.items.clear();   // keep SAME Hibernate collection

        if (items != null) {
            for (PurchaseOrderItem item : items) {
                item.setPurchaseOrder(this); // parent reference
                this.items.add(item);
            }
        }
    }

    public List<Approval> getApprovals() {
        return approvals;
    }
}
