package com.skytree.skystock.repository

import com.skytree.skystock.entity.ReceivingLog
import org.springframework.data.jpa.repository.JpaRepository

interface ReceivingLogRepository : JpaRepository<ReceivingLog, Int> {

    fun findByPurchaseOrderId(purchaseOrderId: Int): List<ReceivingLog>
}
