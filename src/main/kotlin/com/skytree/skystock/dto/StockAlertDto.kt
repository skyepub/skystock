package com.skytree.skystock.dto

import com.skytree.skystock.entity.AlertLevel
import com.skytree.skystock.entity.StockAlert
import java.time.LocalDateTime

// ── Request ──

data class CreateStockAlertRequest(
    val skymallProductId: Int,
    val skymallProductName: String,
    val safetyStock: Int = 10,
    val reorderPoint: Int = 20,
    val reorderQuantity: Int = 50,
    val alertLevel: AlertLevel = AlertLevel.NORMAL
)

data class UpdateStockAlertRequest(
    val skymallProductName: String? = null,
    val safetyStock: Int? = null,
    val reorderPoint: Int? = null,
    val reorderQuantity: Int? = null,
    val alertLevel: AlertLevel? = null
)

data class UpdateAlertLevelRequest(
    val alertLevel: AlertLevel
)

// ── Response ──

data class StockAlertResponse(
    val id: Int,
    val skymallProductId: Int,
    val skymallProductName: String,
    val safetyStock: Int,
    val reorderPoint: Int,
    val reorderQuantity: Int,
    val alertLevel: AlertLevel,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(alert: StockAlert) = StockAlertResponse(
            id = alert.id,
            skymallProductId = alert.skymallProductId,
            skymallProductName = alert.skymallProductName,
            safetyStock = alert.safetyStock,
            reorderPoint = alert.reorderPoint,
            reorderQuantity = alert.reorderQuantity,
            alertLevel = alert.alertLevel,
            createdAt = alert.createdAt,
            updatedAt = alert.updatedAt
        )
    }
}
