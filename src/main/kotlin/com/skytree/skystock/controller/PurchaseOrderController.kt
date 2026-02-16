package com.skytree.skystock.controller

import com.skytree.skystock.dto.*
import com.skytree.skystock.entity.PurchaseOrderStatus
import com.skytree.skystock.service.PurchaseOrderService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/purchase-orders")
class PurchaseOrderController(
    private val poService: PurchaseOrderService
) {
    @GetMapping
    fun getAllPurchaseOrders(
        @PageableDefault(size = 20) pageable: Pageable
    ): Page<PurchaseOrderResponse> = poService.getAllPurchaseOrders(pageable)

    @GetMapping("/{id}")
    fun getPurchaseOrder(@PathVariable id: Int): PurchaseOrderResponse =
        poService.getPurchaseOrder(id)

    @GetMapping("/status/{status}")
    fun getByStatus(
        @PathVariable status: PurchaseOrderStatus,
        @PageableDefault(size = 20) pageable: Pageable
    ): Page<PurchaseOrderResponse> = poService.getByStatus(status, pageable)

    @GetMapping("/supplier/{supplierId}")
    fun getBySupplier(
        @PathVariable supplierId: Int,
        @PageableDefault(size = 20) pageable: Pageable
    ): Page<PurchaseOrderResponse> = poService.getBySupplier(supplierId, pageable)

    @GetMapping("/date-range")
    fun getByDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) from: LocalDateTime,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) to: LocalDateTime,
        @PageableDefault(size = 20) pageable: Pageable
    ): Page<PurchaseOrderResponse> = poService.getByDateRange(from, to, pageable)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createPurchaseOrder(@RequestBody req: CreatePurchaseOrderRequest): PurchaseOrderResponse =
        poService.createPurchaseOrder(req)

    @PostMapping("/{id}/approve")
    fun approve(@PathVariable id: Int): PurchaseOrderResponse =
        poService.approve(id)

    @PostMapping("/{id}/ship")
    fun ship(@PathVariable id: Int): PurchaseOrderResponse =
        poService.ship(id)

    @PostMapping("/{id}/receive")
    fun receive(
        @PathVariable id: Int,
        @RequestBody(required = false) req: ReceiveRequest?
    ): PurchaseOrderResponse = poService.receive(id, req)

    @PostMapping("/{id}/cancel")
    fun cancel(@PathVariable id: Int): PurchaseOrderResponse =
        poService.cancel(id)

    @GetMapping("/{id}/receiving-logs")
    fun getReceivingLogs(@PathVariable id: Int): List<ReceivingLogResponse> =
        poService.getReceivingLogs(id)
}
