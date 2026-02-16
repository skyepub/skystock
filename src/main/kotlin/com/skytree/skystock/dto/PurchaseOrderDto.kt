package com.skytree.skystock.dto

import com.skytree.skystock.entity.PurchaseItem
import com.skytree.skystock.entity.PurchaseOrder
import com.skytree.skystock.entity.PurchaseOrderStatus
import com.skytree.skystock.entity.ReceivingLog
import java.time.LocalDate
import java.time.LocalDateTime

// ── Request ──

data class CreatePurchaseOrderRequest(
    val supplierId: Int,
    val expectedDate: LocalDate? = null,
    val items: List<PurchaseItemRequest>
)

data class PurchaseItemRequest(
    val skymallProductId: Int,
    val skymallProductName: String,
    val quantity: Int,
    val unitCost: Double
)

data class ReceiveRequest(
    val notes: String? = null
)

// ── Response ──

data class PurchaseOrderResponse(
    val id: Int,
    val supplierId: Int,
    val supplierName: String,
    val status: PurchaseOrderStatus,
    val orderedBy: String,
    val totalCost: Double,
    val expectedDate: LocalDate?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val items: List<PurchaseItemResponse>
) {
    companion object {
        fun from(po: PurchaseOrder) = PurchaseOrderResponse(
            id = po.id,
            supplierId = po.supplier.id,
            supplierName = po.supplier.name,
            status = po.status,
            orderedBy = po.orderedBy,
            totalCost = po.totalCost,
            expectedDate = po.expectedDate,
            createdAt = po.createdAt,
            updatedAt = po.updatedAt,
            items = po.items.map { PurchaseItemResponse.from(it) }
        )
    }
}

data class PurchaseItemResponse(
    val id: Long,
    val skymallProductId: Int,
    val skymallProductName: String,
    val quantity: Int,
    val unitCost: Double,
    val subtotal: Double
) {
    companion object {
        fun from(item: PurchaseItem) = PurchaseItemResponse(
            id = item.id,
            skymallProductId = item.skymallProductId,
            skymallProductName = item.skymallProductName,
            quantity = item.quantity,
            unitCost = item.unitCost,
            subtotal = item.subtotal
        )
    }
}

data class ReceivingLogResponse(
    val id: Int,
    val purchaseOrderId: Int,
    val receivedBy: String,
    val receivedAt: LocalDateTime,
    val notes: String?
) {
    companion object {
        fun from(log: ReceivingLog) = ReceivingLogResponse(
            id = log.id,
            purchaseOrderId = log.purchaseOrder?.id ?: 0,
            receivedBy = log.receivedBy,
            receivedAt = log.receivedAt,
            notes = log.notes
        )
    }
}
