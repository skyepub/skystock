package com.skytree.skystock.dto

data class DashboardResponse(
    val totalSuppliers: Long,
    val activeSuppliers: Long,
    val totalPurchaseOrders: Long,
    val purchaseOrdersByStatus: Map<String, Long>,
    val totalPurchaseCostByStatus: Map<String, Double>,
    val stockAlertsByLevel: Map<String, Long>,
    val criticalAlerts: Long,
    val warningAlerts: Long
)

data class SupplierPerformanceResponse(
    val supplierId: Int,
    val supplierName: String,
    val totalOrders: Long,
    val receivedOrders: Long,
    val cancelledOrders: Long,
    val totalSpent: Double,
    val fulfillmentRate: Double
)
