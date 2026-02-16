package com.skytree.skystock.dto

import com.skytree.skystock.entity.Supplier
import com.skytree.skystock.entity.SupplierProduct
import java.time.LocalDateTime

// ── Request ──

data class CreateSupplierRequest(
    val name: String,
    val contactEmail: String? = null,
    val contactPhone: String? = null,
    val address: String? = null,
    val leadTimeDays: Int = 7
)

data class UpdateSupplierRequest(
    val name: String? = null,
    val contactEmail: String? = null,
    val contactPhone: String? = null,
    val address: String? = null,
    val leadTimeDays: Int? = null,
    val isActive: Boolean? = null
)

data class CreateSupplierProductRequest(
    val skymallProductId: Int,
    val skymallProductName: String,
    val unitCost: Double
)

data class UpdateSupplierProductRequest(
    val skymallProductName: String? = null,
    val unitCost: Double? = null,
    val isActive: Boolean? = null
)

// ── Response ──

data class SupplierResponse(
    val id: Int,
    val name: String,
    val contactEmail: String?,
    val contactPhone: String?,
    val address: String?,
    val leadTimeDays: Int,
    val isActive: Boolean,
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(supplier: Supplier) = SupplierResponse(
            id = supplier.id,
            name = supplier.name,
            contactEmail = supplier.contactEmail,
            contactPhone = supplier.contactPhone,
            address = supplier.address,
            leadTimeDays = supplier.leadTimeDays,
            isActive = supplier.isActive,
            createdAt = supplier.createdAt
        )
    }
}

data class SupplierProductResponse(
    val id: Int,
    val supplierId: Int,
    val supplierName: String,
    val skymallProductId: Int,
    val skymallProductName: String,
    val unitCost: Double,
    val isActive: Boolean
) {
    companion object {
        fun from(sp: SupplierProduct) = SupplierProductResponse(
            id = sp.id,
            supplierId = sp.supplier.id,
            supplierName = sp.supplier.name,
            skymallProductId = sp.skymallProductId,
            skymallProductName = sp.skymallProductName,
            unitCost = sp.unitCost,
            isActive = sp.isActive
        )
    }
}
