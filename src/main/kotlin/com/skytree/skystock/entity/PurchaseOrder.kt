package com.skytree.skystock.entity

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

enum class PurchaseOrderStatus {
    REQUESTED, APPROVED, SHIPPED, RECEIVED, CANCELLED;

    fun canTransitionTo(target: PurchaseOrderStatus): Boolean = when (this) {
        REQUESTED -> target == APPROVED || target == CANCELLED
        APPROVED -> target == SHIPPED || target == CANCELLED
        SHIPPED -> target == RECEIVED
        RECEIVED -> false
        CANCELLED -> false
    }
}

@Entity
@Table(name = "purchase_orders")
class PurchaseOrder(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_order_id")
    val id: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    var supplier: Supplier,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: PurchaseOrderStatus = PurchaseOrderStatus.REQUESTED,

    @Column(name = "ordered_by", nullable = false, length = 50)
    var orderedBy: String,

    @Column(name = "total_cost", nullable = false)
    var totalCost: Double = 0.0,

    @Column(name = "expected_date")
    var expectedDate: LocalDate? = null,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "purchaseOrder", cascade = [CascadeType.ALL], orphanRemoval = true)
    val items: MutableList<PurchaseItem> = mutableListOf(),

    @OneToMany(mappedBy = "purchaseOrder", cascade = [CascadeType.ALL], orphanRemoval = true)
    val receivingLogs: MutableList<ReceivingLog> = mutableListOf()
)
