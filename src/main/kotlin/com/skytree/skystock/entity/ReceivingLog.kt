package com.skytree.skystock.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "receiving_logs")
class ReceivingLog(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "receiving_log_id")
    val id: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_id", nullable = false)
    var purchaseOrder: PurchaseOrder? = null,

    @Column(name = "received_by", nullable = false, length = 50)
    var receivedBy: String,

    @Column(name = "received_at")
    val receivedAt: LocalDateTime = LocalDateTime.now(),

    @Column(columnDefinition = "TEXT")
    var notes: String? = null
)
