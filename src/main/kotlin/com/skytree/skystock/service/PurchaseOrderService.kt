package com.skytree.skystock.service

import com.skytree.skystock.dto.*
import com.skytree.skystock.entity.*
import com.skytree.skystock.exception.BusinessException
import com.skytree.skystock.exception.EntityNotFoundException
import com.skytree.skystock.exception.InvalidStateTransitionException
import com.skytree.skystock.repository.PurchaseOrderRepository
import com.skytree.skystock.repository.ReceivingLogRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class PurchaseOrderService(
    private val poRepo: PurchaseOrderRepository,
    private val receivingLogRepo: ReceivingLogRepository,
    private val supplierService: SupplierService
) {
    // ══════════════════════════════════════
    //  발주 생성
    // ══════════════════════════════════════

    @Transactional
    fun createPurchaseOrder(req: CreatePurchaseOrderRequest): PurchaseOrderResponse {
        if (req.items.isEmpty()) {
            throw BusinessException("발주 항목이 비어있습니다")
        }

        val supplier = supplierService.findSupplierOrThrow(req.supplierId)
        val username = currentUsername()

        val po = PurchaseOrder(
            supplier = supplier,
            orderedBy = username,
            expectedDate = req.expectedDate
        )

        var totalCost = 0.0
        for (itemReq in req.items) {
            val subtotal = itemReq.unitCost * itemReq.quantity
            totalCost += subtotal

            val item = PurchaseItem(
                purchaseOrder = po,
                skymallProductId = itemReq.skymallProductId,
                skymallProductName = itemReq.skymallProductName,
                quantity = itemReq.quantity,
                unitCost = itemReq.unitCost,
                subtotal = subtotal
            )
            po.items.add(item)
        }
        po.totalCost = totalCost

        val saved = poRepo.save(po)
        return PurchaseOrderResponse.from(saved)
    }

    // ══════════════════════════════════════
    //  상태 전이
    // ══════════════════════════════════════

    @Transactional
    fun approve(id: Int): PurchaseOrderResponse =
        transition(id, PurchaseOrderStatus.APPROVED)

    @Transactional
    fun ship(id: Int): PurchaseOrderResponse =
        transition(id, PurchaseOrderStatus.SHIPPED)

    @Transactional
    fun receive(id: Int, req: ReceiveRequest?): PurchaseOrderResponse {
        val po = findPOOrThrow(id)
        validateTransition(po, PurchaseOrderStatus.RECEIVED)
        po.status = PurchaseOrderStatus.RECEIVED
        po.updatedAt = LocalDateTime.now()

        val log = ReceivingLog(
            purchaseOrder = po,
            receivedBy = currentUsername(),
            notes = req?.notes
        )
        po.receivingLogs.add(log)

        return PurchaseOrderResponse.from(po)
    }

    @Transactional
    fun cancel(id: Int): PurchaseOrderResponse =
        transition(id, PurchaseOrderStatus.CANCELLED)

    private fun transition(id: Int, target: PurchaseOrderStatus): PurchaseOrderResponse {
        val po = findPOOrThrow(id)
        validateTransition(po, target)
        po.status = target
        po.updatedAt = LocalDateTime.now()
        return PurchaseOrderResponse.from(po)
    }

    private fun validateTransition(po: PurchaseOrder, target: PurchaseOrderStatus) {
        if (!po.status.canTransitionTo(target)) {
            throw InvalidStateTransitionException(po.status.name, target.name)
        }
    }

    // ══════════════════════════════════════
    //  조회
    // ══════════════════════════════════════

    fun getPurchaseOrder(id: Int): PurchaseOrderResponse =
        PurchaseOrderResponse.from(findPOOrThrow(id))

    fun getAllPurchaseOrders(pageable: Pageable): Page<PurchaseOrderResponse> =
        poRepo.findAll(pageable).map { PurchaseOrderResponse.from(it) }

    fun getByStatus(status: PurchaseOrderStatus, pageable: Pageable): Page<PurchaseOrderResponse> =
        poRepo.findByStatus(status, pageable).map { PurchaseOrderResponse.from(it) }

    fun getBySupplier(supplierId: Int, pageable: Pageable): Page<PurchaseOrderResponse> =
        poRepo.findBySupplierId(supplierId, pageable).map { PurchaseOrderResponse.from(it) }

    fun getByDateRange(from: LocalDateTime, to: LocalDateTime, pageable: Pageable): Page<PurchaseOrderResponse> =
        poRepo.findByCreatedAtBetween(from, to, pageable).map { PurchaseOrderResponse.from(it) }

    fun getReceivingLogs(purchaseOrderId: Int): List<ReceivingLogResponse> {
        findPOOrThrow(purchaseOrderId)
        return receivingLogRepo.findByPurchaseOrderId(purchaseOrderId)
            .map { ReceivingLogResponse.from(it) }
    }

    // ══════════════════════════════════════
    //  내부 헬퍼
    // ══════════════════════════════════════

    private fun findPOOrThrow(id: Int): PurchaseOrder =
        poRepo.findById(id)
            .orElseThrow { EntityNotFoundException("발주서", id) }

    private fun currentUsername(): String {
        val auth = SecurityContextHolder.getContext().authentication
        return auth?.details as? String ?: "system"
    }
}
