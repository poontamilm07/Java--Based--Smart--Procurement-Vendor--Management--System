package com.procurement.procurement.controller.vendor;

import com.procurement.procurement.entity.vendor.Vendor;
import com.procurement.procurement.repository.vendor.VendorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/vendor")
public class VendorController {

    private final VendorRepository vendorRepository;

    public VendorController(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    // ===================== Create Vendor =====================
    @PostMapping("/create")
    public ResponseEntity<Vendor> createVendor(@RequestBody Vendor vendor) {
        vendor.setStatus("ACTIVE");
        Vendor savedVendor = vendorRepository.save(vendor);
        return ResponseEntity.ok(savedVendor);
    }

    // ===================== Get all Vendors =====================
    @GetMapping("/all")
    public ResponseEntity<List<Vendor>> getAllVendors() {
        return ResponseEntity.ok(vendorRepository.findAll());
    }

    // ===================== Get Vendor by ID =====================
    @GetMapping("/{id}")
    public ResponseEntity<?> getVendorById(@PathVariable Long id) {
        Optional<Vendor> vendorOpt = vendorRepository.findById(id);
        if (vendorOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Vendor not found");
        }
        return ResponseEntity.ok(vendorOpt.get());
    }

    // ===================== Update Vendor =====================
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateVendor(
            @PathVariable Long id,
            @RequestBody Vendor vendor) {

        Optional<Vendor> vendorOpt = vendorRepository.findById(id);
        if (vendorOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Vendor not found");
        }

        Vendor existing = vendorOpt.get();
        existing.setName(vendor.getName());
        existing.setEmail(vendor.getEmail());
        existing.setContactNumber(vendor.getContactNumber());
        existing.setAddress(vendor.getAddress());
        existing.setStatus(vendor.getStatus());

        Vendor updatedVendor = vendorRepository.save(existing);
        return ResponseEntity.ok(updatedVendor);
    }

    // ===================== Delete Vendor =====================
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVendor(@PathVariable Long id) {
        if (!vendorRepository.existsById(id)) {
            return ResponseEntity.badRequest().body("Vendor not found");
        }
        vendorRepository.deleteById(id);
        return ResponseEntity.ok("Vendor deleted successfully");
    }
}
