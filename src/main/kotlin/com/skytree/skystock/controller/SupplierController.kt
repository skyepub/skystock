package com.skytree.skystock.controller

import com.skytree.skystock.dto.*
import com.skytree.skystock.service.SupplierService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/suppliers")
class SupplierController(
    private val supplierService: SupplierService
) {
    // ── Supplier ──

    @GetMapping
    fun getAllSuppliers(
        @PageableDefault(size = 20) pageable: Pageable
    ): Page<SupplierResponse> = supplierService.getAllSuppliers(pageable)

    @GetMapping("/{id}")
    fun getSupplier(@PathVariable id: Int): SupplierResponse =
        supplierService.getSupplier(id)

    @GetMapping("/active")
    fun getActiveSuppliers(): List<SupplierResponse> =
        supplierService.getActiveSuppliers()

    @GetMapping("/search")
    fun searchSuppliers(
        @RequestParam keyword: String,
        @PageableDefault(size = 20) pageable: Pageable
    ): Page<SupplierResponse> = supplierService.searchSuppliers(keyword, pageable)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createSupplier(@RequestBody req: CreateSupplierRequest): SupplierResponse =
        supplierService.createSupplier(req)

    @PatchMapping("/{id}")
    fun updateSupplier(
        @PathVariable id: Int,
        @RequestBody req: UpdateSupplierRequest
    ): SupplierResponse = supplierService.updateSupplier(id, req)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteSupplier(@PathVariable id: Int) =
        supplierService.deleteSupplier(id)

    // ── SupplierProduct ──

    @GetMapping("/{id}/products")
    fun getSupplierProducts(@PathVariable id: Int): List<SupplierProductResponse> =
        supplierService.getSupplierProducts(id)

    @GetMapping("/by-product/{skymallProductId}")
    fun getSuppliersByProduct(@PathVariable skymallProductId: Int): List<SupplierProductResponse> =
        supplierService.getSuppliersByProduct(skymallProductId)

    @PostMapping("/{id}/products")
    @ResponseStatus(HttpStatus.CREATED)
    fun createSupplierProduct(
        @PathVariable id: Int,
        @RequestBody req: CreateSupplierProductRequest
    ): SupplierProductResponse = supplierService.createSupplierProduct(id, req)

    @PatchMapping("/products/{id}")
    fun updateSupplierProduct(
        @PathVariable id: Int,
        @RequestBody req: UpdateSupplierProductRequest
    ): SupplierProductResponse = supplierService.updateSupplierProduct(id, req)

    @DeleteMapping("/products/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteSupplierProduct(@PathVariable id: Int) =
        supplierService.deleteSupplierProduct(id)
}
