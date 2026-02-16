package com.skytree.skystock.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "suppliers")
class Supplier(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplier_id")
    val id: Int = 0,

    @Column(nullable = false, length = 100)
    var name: String,

    @Column(name = "contact_email", length = 100)
    var contactEmail: String? = null,

    @Column(name = "contact_phone", length = 30)
    var contactPhone: String? = null,

    @Column(length = 255)
    var address: String? = null,

    @Column(name = "lead_time_days")
    var leadTimeDays: Int = 7,

    @Column(name = "is_active", nullable = false)
    var isActive: Boolean = true,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "supplier", cascade = [CascadeType.ALL], orphanRemoval = true)
    val products: MutableList<SupplierProduct> = mutableListOf()
)
