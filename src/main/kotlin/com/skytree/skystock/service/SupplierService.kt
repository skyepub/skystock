package com.skytree.skystock.service

import com.skytree.skystock.dto.*
import com.skytree.skystock.entity.Supplier
import com.skytree.skystock.entity.SupplierProduct
import com.skytree.skystock.exception.EntityNotFoundException
import com.skytree.skystock.repository.SupplierProductRepository
import com.skytree.skystock.repository.SupplierRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class SupplierService(
    private val supplierRepo: SupplierRepository,
    private val supplierProductRepo: SupplierProductRepository
) {
    // ══════════════════════════════════════
    //  Supplier CRUD
    // ══════════════════════════════════════

    fun getAllSuppliers(pageable: Pageable): Page<SupplierResponse> =
        supplierRepo.findAll(pageable).map { SupplierResponse.from(it) }

    fun getSupplier(id: Int): SupplierResponse =
        SupplierResponse.from(findSupplierOrThrow(id))

    fun getActiveSuppliers(): List<SupplierResponse> =
        supplierRepo.findByIsActiveTrue().map { SupplierResponse.from(it) }

    fun searchSuppliers(keyword: String, pageable: Pageable): Page<SupplierResponse> =
        supplierRepo.searchByKeyword(keyword, pageable).map { SupplierResponse.from(it) }

    @Transactional
    fun createSupplier(req: CreateSupplierRequest): SupplierResponse {
        val supplier = supplierRepo.save(
            Supplier(
                name = req.name,
                contactEmail = req.contactEmail,
                contactPhone = req.contactPhone,
                address = req.address,
                leadTimeDays = req.leadTimeDays
            )
        )
        return SupplierResponse.from(supplier)
    }

    @Transactional
    fun updateSupplier(id: Int, req: UpdateSupplierRequest): SupplierResponse {
        val supplier = findSupplierOrThrow(id)
        req.name?.let { supplier.name = it }
        req.contactEmail?.let { supplier.contactEmail = it }
        req.contactPhone?.let { supplier.contactPhone = it }
        req.address?.let { supplier.address = it }
        req.leadTimeDays?.let { supplier.leadTimeDays = it }
        req.isActive?.let { supplier.isActive = it }
        return SupplierResponse.from(supplier)
    }

    @Transactional
    fun deleteSupplier(id: Int) {
        findSupplierOrThrow(id)
        supplierRepo.deleteById(id)
    }

    // ══════════════════════════════════════
    //  SupplierProduct CRUD
    // ══════════════════════════════════════

    fun getSupplierProducts(supplierId: Int): List<SupplierProductResponse> {
        findSupplierOrThrow(supplierId)
        return supplierProductRepo.findBySupplierId(supplierId)
            .map { SupplierProductResponse.from(it) }
    }

    fun getSuppliersByProduct(skymallProductId: Int): List<SupplierProductResponse> =
        supplierProductRepo.findBySkymallProductId(skymallProductId)
            .map { SupplierProductResponse.from(it) }

    @Transactional
    fun createSupplierProduct(supplierId: Int, req: CreateSupplierProductRequest): SupplierProductResponse {
        val supplier = findSupplierOrThrow(supplierId)
        val sp = supplierProductRepo.save(
            SupplierProduct(
                supplier = supplier,
                skymallProductId = req.skymallProductId,
                skymallProductName = req.skymallProductName,
                unitCost = req.unitCost
            )
        )
        return SupplierProductResponse.from(sp)
    }

    @Transactional
    fun updateSupplierProduct(id: Int, req: UpdateSupplierProductRequest): SupplierProductResponse {
        val sp = findSupplierProductOrThrow(id)
        req.skymallProductName?.let { sp.skymallProductName = it }
        req.unitCost?.let { sp.unitCost = it }
        req.isActive?.let { sp.isActive = it }
        return SupplierProductResponse.from(sp)
    }

    @Transactional
    fun deleteSupplierProduct(id: Int) {
        findSupplierProductOrThrow(id)
        supplierProductRepo.deleteById(id)
    }

    // ══════════════════════════════════════
    //  내부 헬퍼
    // ══════════════════════════════════════

    internal fun findSupplierOrThrow(id: Int): Supplier =
        supplierRepo.findById(id)
            .orElseThrow { EntityNotFoundException("공급사", id) }

    private fun findSupplierProductOrThrow(id: Int): SupplierProduct =
        supplierProductRepo.findById(id)
            .orElseThrow { EntityNotFoundException("공급사 상품", id) }
}
