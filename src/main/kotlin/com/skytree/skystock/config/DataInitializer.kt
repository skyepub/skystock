package com.skytree.skystock.config

import com.skytree.skystock.entity.*
import com.skytree.skystock.repository.*
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime

@Component
class DataInitializer(
    private val userRepo: UserRepository,
    private val supplierRepo: SupplierRepository,
    private val supplierProductRepo: SupplierProductRepository,
    private val poRepo: PurchaseOrderRepository,
    private val alertRepo: StockAlertRepository,
    private val receivingLogRepo: ReceivingLogRepository,
    private val passwordEncoder: PasswordEncoder
) : CommandLineRunner {

    override fun run(vararg args: String) {
        if (userRepo.count() > 0) return  // 이미 데이터가 있으면 스킵

        // ── 사용자 5명 ──
        val admin = userRepo.save(User(username = "admin", email = "admin@skystock.com", password = passwordEncoder.encode("admin123")!!, role = UserRole.ADMIN))
        val wm1 = userRepo.save(User(username = "warehouse1", email = "wm1@skystock.com", password = passwordEncoder.encode("wm1pass")!!, role = UserRole.WAREHOUSE_MANAGER))
        val wm2 = userRepo.save(User(username = "warehouse2", email = "wm2@skystock.com", password = passwordEncoder.encode("wm2pass")!!, role = UserRole.WAREHOUSE_MANAGER))
        val viewer1 = userRepo.save(User(username = "viewer1", email = "viewer1@skystock.com", password = passwordEncoder.encode("viewer1pass")!!, role = UserRole.VIEWER))
        val viewer2 = userRepo.save(User(username = "viewer2", email = "viewer2@skystock.com", password = passwordEncoder.encode("viewer2pass")!!, role = UserRole.VIEWER))

        // ── 공급사 5개 ──
        val samsung = supplierRepo.save(Supplier(name = "Samsung Electronics", contactEmail = "supply@samsung.com", contactPhone = "02-1234-5678", address = "서울 서초구 삼성로 129", leadTimeDays = 3))
        val lg = supplierRepo.save(Supplier(name = "LG Electronics", contactEmail = "supply@lg.com", contactPhone = "02-2345-6789", address = "서울 영등포구 여의대로 128", leadTimeDays = 4))
        val fashion = supplierRepo.save(Supplier(name = "Global Fashion Co.", contactEmail = "order@globalfashion.com", contactPhone = "02-3456-7890", address = "서울 강남구 압구정로 340", leadTimeDays = 7))
        val bookworld = supplierRepo.save(Supplier(name = "BookWorld Distribution", contactEmail = "supply@bookworld.com", contactPhone = "031-456-7890", address = "경기도 파주시 출판로 201", leadTimeDays = 2))
        val homeEss = supplierRepo.save(Supplier(name = "Home Essentials Ltd.", contactEmail = "order@homeessentials.com", contactPhone = "02-5678-9012", address = "서울 마포구 상암로 330", leadTimeDays = 5))

        // ── 공급사-상품 매핑 15개 (skymall productId 참조) ──
        // Samsung: 전자제품
        val sp1 = supplierProductRepo.save(SupplierProduct(supplier = samsung, skymallProductId = 1, skymallProductName = "갤럭시 S25 Ultra", unitCost = 990000.0))
        val sp2 = supplierProductRepo.save(SupplierProduct(supplier = samsung, skymallProductId = 2, skymallProductName = "갤럭시 탭 S10", unitCost = 720000.0))
        val sp3 = supplierProductRepo.save(SupplierProduct(supplier = samsung, skymallProductId = 3, skymallProductName = "갤럭시 워치 7", unitCost = 250000.0))

        // LG: 가전
        val sp4 = supplierProductRepo.save(SupplierProduct(supplier = lg, skymallProductId = 4, skymallProductName = "LG 그램 17인치", unitCost = 1350000.0))
        val sp5 = supplierProductRepo.save(SupplierProduct(supplier = lg, skymallProductId = 5, skymallProductName = "LG 올레드 TV 65인치", unitCost = 2100000.0))
        val sp6 = supplierProductRepo.save(SupplierProduct(supplier = lg, skymallProductId = 6, skymallProductName = "LG 퓨리케어 공기청정기", unitCost = 380000.0))

        // Global Fashion: 의류
        val sp7 = supplierProductRepo.save(SupplierProduct(supplier = fashion, skymallProductId = 7, skymallProductName = "프리미엄 캐시미어 코트", unitCost = 180000.0))
        val sp8 = supplierProductRepo.save(SupplierProduct(supplier = fashion, skymallProductId = 8, skymallProductName = "클래식 데님 자켓", unitCost = 55000.0))
        val sp9 = supplierProductRepo.save(SupplierProduct(supplier = fashion, skymallProductId = 9, skymallProductName = "스포츠 러닝화", unitCost = 65000.0))

        // BookWorld: 도서
        val sp10 = supplierProductRepo.save(SupplierProduct(supplier = bookworld, skymallProductId = 10, skymallProductName = "클린 코드", unitCost = 22000.0))
        val sp11 = supplierProductRepo.save(SupplierProduct(supplier = bookworld, skymallProductId = 11, skymallProductName = "디자인 패턴", unitCost = 28000.0))
        val sp12 = supplierProductRepo.save(SupplierProduct(supplier = bookworld, skymallProductId = 12, skymallProductName = "리팩터링 2판", unitCost = 30000.0))

        // Home Essentials: 생활용품
        val sp13 = supplierProductRepo.save(SupplierProduct(supplier = homeEss, skymallProductId = 13, skymallProductName = "프리미엄 이불 세트", unitCost = 85000.0))
        val sp14 = supplierProductRepo.save(SupplierProduct(supplier = homeEss, skymallProductId = 14, skymallProductName = "스테인리스 냄비 세트", unitCost = 95000.0))
        val sp15 = supplierProductRepo.save(SupplierProduct(supplier = homeEss, skymallProductId = 15, skymallProductName = "LED 스탠드 조명", unitCost = 42000.0))

        // ── 발주서 8건 ──

        // PO1: Samsung, RECEIVED
        val po1 = PurchaseOrder(supplier = samsung, status = PurchaseOrderStatus.RECEIVED, orderedBy = "warehouse1", totalCost = 0.0, expectedDate = LocalDate.of(2026, 2, 10), createdAt = LocalDateTime.of(2026, 2, 5, 10, 0), updatedAt = LocalDateTime.of(2026, 2, 10, 14, 0))
        val po1Items = listOf(
            PurchaseItem(purchaseOrder = po1, skymallProductId = 1, skymallProductName = "갤럭시 S25 Ultra", quantity = 20, unitCost = 990000.0, subtotal = 19800000.0),
            PurchaseItem(purchaseOrder = po1, skymallProductId = 3, skymallProductName = "갤럭시 워치 7", quantity = 30, unitCost = 250000.0, subtotal = 7500000.0)
        )
        po1.items.addAll(po1Items)
        po1.totalCost = po1Items.sumOf { it.subtotal }
        poRepo.save(po1)

        // PO2: LG, RECEIVED
        val po2 = PurchaseOrder(supplier = lg, status = PurchaseOrderStatus.RECEIVED, orderedBy = "warehouse1", totalCost = 0.0, expectedDate = LocalDate.of(2026, 2, 12), createdAt = LocalDateTime.of(2026, 2, 6, 9, 0), updatedAt = LocalDateTime.of(2026, 2, 12, 11, 0))
        val po2Items = listOf(
            PurchaseItem(purchaseOrder = po2, skymallProductId = 4, skymallProductName = "LG 그램 17인치", quantity = 10, unitCost = 1350000.0, subtotal = 13500000.0)
        )
        po2.items.addAll(po2Items)
        po2.totalCost = po2Items.sumOf { it.subtotal }
        poRepo.save(po2)

        // PO3: Fashion, SHIPPED
        val po3 = PurchaseOrder(supplier = fashion, status = PurchaseOrderStatus.SHIPPED, orderedBy = "warehouse2", totalCost = 0.0, expectedDate = LocalDate.of(2026, 2, 20), createdAt = LocalDateTime.of(2026, 2, 10, 14, 0), updatedAt = LocalDateTime.of(2026, 2, 15, 9, 0))
        val po3Items = listOf(
            PurchaseItem(purchaseOrder = po3, skymallProductId = 7, skymallProductName = "프리미엄 캐시미어 코트", quantity = 15, unitCost = 180000.0, subtotal = 2700000.0),
            PurchaseItem(purchaseOrder = po3, skymallProductId = 8, skymallProductName = "클래식 데님 자켓", quantity = 25, unitCost = 55000.0, subtotal = 1375000.0)
        )
        po3.items.addAll(po3Items)
        po3.totalCost = po3Items.sumOf { it.subtotal }
        poRepo.save(po3)

        // PO4: BookWorld, APPROVED
        val po4 = PurchaseOrder(supplier = bookworld, status = PurchaseOrderStatus.APPROVED, orderedBy = "warehouse1", totalCost = 0.0, expectedDate = LocalDate.of(2026, 2, 22), createdAt = LocalDateTime.of(2026, 2, 12, 11, 0), updatedAt = LocalDateTime.of(2026, 2, 13, 10, 0))
        val po4Items = listOf(
            PurchaseItem(purchaseOrder = po4, skymallProductId = 10, skymallProductName = "클린 코드", quantity = 50, unitCost = 22000.0, subtotal = 1100000.0),
            PurchaseItem(purchaseOrder = po4, skymallProductId = 11, skymallProductName = "디자인 패턴", quantity = 30, unitCost = 28000.0, subtotal = 840000.0),
            PurchaseItem(purchaseOrder = po4, skymallProductId = 12, skymallProductName = "리팩터링 2판", quantity = 40, unitCost = 30000.0, subtotal = 1200000.0)
        )
        po4.items.addAll(po4Items)
        po4.totalCost = po4Items.sumOf { it.subtotal }
        poRepo.save(po4)

        // PO5: HomeEss, REQUESTED
        val po5 = PurchaseOrder(supplier = homeEss, status = PurchaseOrderStatus.REQUESTED, orderedBy = "warehouse2", totalCost = 0.0, expectedDate = LocalDate.of(2026, 2, 25), createdAt = LocalDateTime.of(2026, 2, 14, 16, 0), updatedAt = LocalDateTime.of(2026, 2, 14, 16, 0))
        val po5Items = listOf(
            PurchaseItem(purchaseOrder = po5, skymallProductId = 13, skymallProductName = "프리미엄 이불 세트", quantity = 20, unitCost = 85000.0, subtotal = 1700000.0),
            PurchaseItem(purchaseOrder = po5, skymallProductId = 15, skymallProductName = "LED 스탠드 조명", quantity = 40, unitCost = 42000.0, subtotal = 1680000.0)
        )
        po5.items.addAll(po5Items)
        po5.totalCost = po5Items.sumOf { it.subtotal }
        poRepo.save(po5)

        // PO6: Samsung, REQUESTED
        val po6 = PurchaseOrder(supplier = samsung, status = PurchaseOrderStatus.REQUESTED, orderedBy = "warehouse1", totalCost = 0.0, expectedDate = LocalDate.of(2026, 2, 28), createdAt = LocalDateTime.of(2026, 2, 15, 10, 0), updatedAt = LocalDateTime.of(2026, 2, 15, 10, 0))
        val po6Items = listOf(
            PurchaseItem(purchaseOrder = po6, skymallProductId = 2, skymallProductName = "갤럭시 탭 S10", quantity = 15, unitCost = 720000.0, subtotal = 10800000.0)
        )
        po6.items.addAll(po6Items)
        po6.totalCost = po6Items.sumOf { it.subtotal }
        poRepo.save(po6)

        // PO7: LG, CANCELLED
        val po7 = PurchaseOrder(supplier = lg, status = PurchaseOrderStatus.CANCELLED, orderedBy = "warehouse2", totalCost = 0.0, expectedDate = LocalDate.of(2026, 2, 18), createdAt = LocalDateTime.of(2026, 2, 8, 13, 0), updatedAt = LocalDateTime.of(2026, 2, 9, 10, 0))
        val po7Items = listOf(
            PurchaseItem(purchaseOrder = po7, skymallProductId = 5, skymallProductName = "LG 올레드 TV 65인치", quantity = 5, unitCost = 2100000.0, subtotal = 10500000.0)
        )
        po7.items.addAll(po7Items)
        po7.totalCost = po7Items.sumOf { it.subtotal }
        poRepo.save(po7)

        // PO8: HomeEss, RECEIVED
        val po8 = PurchaseOrder(supplier = homeEss, status = PurchaseOrderStatus.RECEIVED, orderedBy = "warehouse1", totalCost = 0.0, expectedDate = LocalDate.of(2026, 2, 8), createdAt = LocalDateTime.of(2026, 2, 3, 9, 0), updatedAt = LocalDateTime.of(2026, 2, 8, 15, 0))
        val po8Items = listOf(
            PurchaseItem(purchaseOrder = po8, skymallProductId = 14, skymallProductName = "스테인리스 냄비 세트", quantity = 25, unitCost = 95000.0, subtotal = 2375000.0)
        )
        po8.items.addAll(po8Items)
        po8.totalCost = po8Items.sumOf { it.subtotal }
        poRepo.save(po8)

        // ── 입고 기록 3건 (RECEIVED 발주에 대해) ──
        receivingLogRepo.save(ReceivingLog(purchaseOrder = po1, receivedBy = "warehouse1", receivedAt = LocalDateTime.of(2026, 2, 10, 14, 0), notes = "삼성 전자제품 입고 완료. 포장 상태 양호."))
        receivingLogRepo.save(ReceivingLog(purchaseOrder = po2, receivedBy = "warehouse1", receivedAt = LocalDateTime.of(2026, 2, 12, 11, 0), notes = "LG 그램 10대 입고. 1대 외관 스크래치 확인."))
        receivingLogRepo.save(ReceivingLog(purchaseOrder = po8, receivedBy = "warehouse1", receivedAt = LocalDateTime.of(2026, 2, 8, 15, 0), notes = "냄비 세트 25세트 정상 입고."))

        // ── 재고 알림 10개 ──
        alertRepo.save(StockAlert(skymallProductId = 1, skymallProductName = "갤럭시 S25 Ultra", safetyStock = 10, reorderPoint = 20, reorderQuantity = 50, alertLevel = AlertLevel.CRITICAL))
        alertRepo.save(StockAlert(skymallProductId = 2, skymallProductName = "갤럭시 탭 S10", safetyStock = 5, reorderPoint = 10, reorderQuantity = 30, alertLevel = AlertLevel.CRITICAL))
        alertRepo.save(StockAlert(skymallProductId = 4, skymallProductName = "LG 그램 17인치", safetyStock = 5, reorderPoint = 10, reorderQuantity = 20, alertLevel = AlertLevel.WARNING))
        alertRepo.save(StockAlert(skymallProductId = 5, skymallProductName = "LG 올레드 TV 65인치", safetyStock = 3, reorderPoint = 5, reorderQuantity = 10, alertLevel = AlertLevel.WARNING))
        alertRepo.save(StockAlert(skymallProductId = 7, skymallProductName = "프리미엄 캐시미어 코트", safetyStock = 10, reorderPoint = 15, reorderQuantity = 30, alertLevel = AlertLevel.WARNING))
        alertRepo.save(StockAlert(skymallProductId = 9, skymallProductName = "스포츠 러닝화", safetyStock = 15, reorderPoint = 25, reorderQuantity = 50, alertLevel = AlertLevel.WARNING))
        alertRepo.save(StockAlert(skymallProductId = 10, skymallProductName = "클린 코드", safetyStock = 20, reorderPoint = 30, reorderQuantity = 100, alertLevel = AlertLevel.NORMAL))
        alertRepo.save(StockAlert(skymallProductId = 13, skymallProductName = "프리미엄 이불 세트", safetyStock = 10, reorderPoint = 15, reorderQuantity = 40, alertLevel = AlertLevel.NORMAL))
        alertRepo.save(StockAlert(skymallProductId = 14, skymallProductName = "스테인리스 냄비 세트", safetyStock = 10, reorderPoint = 15, reorderQuantity = 40, alertLevel = AlertLevel.NORMAL))
        alertRepo.save(StockAlert(skymallProductId = 15, skymallProductName = "LED 스탠드 조명", safetyStock = 15, reorderPoint = 20, reorderQuantity = 50, alertLevel = AlertLevel.NORMAL))

        println("=== SkyStock 샘플 데이터 초기화 완료 ===")
        println("  사용자: ${userRepo.count()}명")
        println("  공급사: ${supplierRepo.count()}개")
        println("  공급사-상품: ${supplierProductRepo.count()}개")
        println("  발주서: ${poRepo.count()}건")
        println("  입고기록: ${receivingLogRepo.count()}건")
        println("  재고알림: ${alertRepo.count()}개")
    }
}
