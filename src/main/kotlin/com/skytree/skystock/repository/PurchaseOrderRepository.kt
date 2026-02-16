package com.skytree.skystock.repository

import com.skytree.skystock.entity.PurchaseOrder
import com.skytree.skystock.entity.PurchaseOrderStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface PurchaseOrderRepository : JpaRepository<PurchaseOrder, Int> {

    fun findByStatus(status: PurchaseOrderStatus, pageable: Pageable): Page<PurchaseOrder>

    fun findBySupplierId(supplierId: Int, pageable: Pageable): Page<PurchaseOrder>

    fun findByCreatedAtBetween(from: LocalDateTime, to: LocalDateTime, pageable: Pageable): Page<PurchaseOrder>

    fun countByStatus(status: PurchaseOrderStatus): Long

    fun countBySupplierId(supplierId: Int): Long

    @Query("SELECT SUM(po.totalCost) FROM PurchaseOrder po WHERE po.status = :status")
    fun sumTotalCostByStatus(@Param("status") status: PurchaseOrderStatus): Double?

    @Query("SELECT SUM(po.totalCost) FROM PurchaseOrder po WHERE po.supplier.id = :supplierId AND po.status = :status")
    fun sumTotalCostBySupplierIdAndStatus(@Param("supplierId") supplierId: Int, @Param("status") status: PurchaseOrderStatus): Double?

    fun countBySupplierIdAndStatus(supplierId: Int, status: PurchaseOrderStatus): Long
}
