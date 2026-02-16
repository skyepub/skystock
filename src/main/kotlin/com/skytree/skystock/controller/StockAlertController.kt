package com.skytree.skystock.controller

import com.skytree.skystock.dto.*
import com.skytree.skystock.entity.AlertLevel
import com.skytree.skystock.service.StockAlertService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/stock-alerts")
class StockAlertController(
    private val alertService: StockAlertService
) {
    @GetMapping
    fun getAllAlerts(
        @PageableDefault(size = 20) pageable: Pageable
    ): Page<StockAlertResponse> = alertService.getAllAlerts(pageable)

    @GetMapping("/{id}")
    fun getAlert(@PathVariable id: Int): StockAlertResponse =
        alertService.getAlert(id)

    @GetMapping("/product/{skymallProductId}")
    fun getAlertByProduct(@PathVariable skymallProductId: Int): StockAlertResponse =
        alertService.getAlertByProduct(skymallProductId)

    @GetMapping("/level/{level}")
    fun getAlertsByLevel(
        @PathVariable level: AlertLevel,
        @PageableDefault(size = 20) pageable: Pageable
    ): Page<StockAlertResponse> = alertService.getAlertsByLevel(level, pageable)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createAlert(@RequestBody req: CreateStockAlertRequest): StockAlertResponse =
        alertService.createAlert(req)

    @PatchMapping("/{id}")
    fun updateAlert(
        @PathVariable id: Int,
        @RequestBody req: UpdateStockAlertRequest
    ): StockAlertResponse = alertService.updateAlert(id, req)

    @PatchMapping("/{id}/level")
    fun updateAlertLevel(
        @PathVariable id: Int,
        @RequestBody req: UpdateAlertLevelRequest
    ): StockAlertResponse = alertService.updateAlertLevel(id, req)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteAlert(@PathVariable id: Int) =
        alertService.deleteAlert(id)
}
