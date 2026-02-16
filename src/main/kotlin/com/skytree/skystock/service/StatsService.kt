package com.skytree.skystock.service

import com.skytree.skystock.dto.DashboardResponse
import com.skytree.skystock.dto.SupplierPerformanceResponse
import com.skytree.skystock.entity.AlertLevel
import com.skytree.skystock.entity.PurchaseOrderStatus
import com.skytree.skystock.repository.PurchaseOrderRepository
import com.skytree.skystock.repository.StockAlertRepository
import com.skytree.skystock.repository.SupplierRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class StatsService(
    private val supplierRepo: SupplierRepository,
    private val poRepo: PurchaseOrderRepository,
    private val alertRepo: StockAlertRepository,
    private val supplierService: SupplierService
) {
    fun getDashboard(): DashboardResponse {
        val ordersByStatus = PurchaseOrderStatus.entries.associate {
            it.name to poRepo.countByStatus(it)
        }
        val costByStatus = PurchaseOrderStatus.entries.associate {
            it.name to (poRepo.sumTotalCostByStatus(it) ?: 0.0)
        }
        val alertsByLevel = AlertLevel.entries.associate {
            it.name to alertRepo.countByAlertLevel(it)
        }

        return DashboardResponse(
            totalSuppliers = supplierRepo.count(),
            activeSuppliers = supplierRepo.findByIsActiveTrue().size.toLong(),
            totalPurchaseOrders = poRepo.count(),
            purchaseOrdersByStatus = ordersByStatus,
            totalPurchaseCostByStatus = costByStatus,
            stockAlertsByLevel = alertsByLevel,
            criticalAlerts = alertRepo.countByAlertLevel(AlertLevel.CRITICAL),
            warningAlerts = alertRepo.countByAlertLevel(AlertLevel.WARNING)
        )
    }

    fun getAllSupplierPerformance(): List<SupplierPerformanceResponse> =
        supplierRepo.findAll().map { buildPerformance(it.id, it.name) }

    fun getSupplierPerformance(supplierId: Int): SupplierPerformanceResponse {
        val supplier = supplierService.findSupplierOrThrow(supplierId)
        return buildPerformance(supplier.id, supplier.name)
    }

    private fun buildPerformance(supplierId: Int, supplierName: String): SupplierPerformanceResponse {
        val total = poRepo.countBySupplierId(supplierId)
        val received = poRepo.countBySupplierIdAndStatus(supplierId, PurchaseOrderStatus.RECEIVED)
        val cancelled = poRepo.countBySupplierIdAndStatus(supplierId, PurchaseOrderStatus.CANCELLED)
        val totalSpent = poRepo.sumTotalCostBySupplierIdAndStatus(supplierId, PurchaseOrderStatus.RECEIVED) ?: 0.0
        val fulfillmentRate = if (total > 0) received.toDouble() / total * 100 else 0.0

        return SupplierPerformanceResponse(
            supplierId = supplierId,
            supplierName = supplierName,
            totalOrders = total,
            receivedOrders = received,
            cancelledOrders = cancelled,
            totalSpent = totalSpent,
            fulfillmentRate = fulfillmentRate
        )
    }
}
