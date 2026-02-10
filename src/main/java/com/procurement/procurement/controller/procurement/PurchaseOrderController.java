package com.procurement.procurement.controller.procurement;

import com.procurement.procurement.dto.procurement.PurchaseOrderRequestDTO;
import com.procurement.procurement.dto.procurement.PurchaseOrderResponseDTO;
import com.procurement.procurement.entity.procurement.PurchaseOrder;
import com.procurement.procurement.entity.vendor.Vendor;
import com.procurement.procurement.mapper.PurchaseOrderMapper;
import com.procurement.procurement.repository.procurement.PurchaseOrderRepository;
import com.procurement.procurement.repository.vendor.VendorRepository;
import com.procurement.procurement.service.procurement.PurchaseOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/procurement/purchase")
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;
    private final PurchaseOrderRepository purchaseOrderRepository;

    public PurchaseOrderController(
            PurchaseOrderService purchaseOrderService,
            PurchaseOrderRepository purchaseOrderRepository
    ) {
        this.purchaseOrderService = purchaseOrderService;
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    // ✅ GET ALL PURCHASE ORDERS
    @GetMapping("/all")
    public List<PurchaseOrder> getAll() {
        return purchaseOrderService.getAllPurchaseOrders();
    }

    // ✅ GET PURCHASE ORDER BY ID
    @GetMapping("/{id}")
    public PurchaseOrder getById(@PathVariable Long id) {
        return purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase Order not found"));
    }

    // ✅ UPDATE PURCHASE ORDER
    @PutMapping("/update/{id}")
    public PurchaseOrder update(
            @PathVariable Long id,
            @RequestBody PurchaseOrder po
    ) {
        return purchaseOrderService.updatePurchaseOrder(id, po);
    }
}
