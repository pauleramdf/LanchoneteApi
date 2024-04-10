package com.br.lanchonete.lanchoneteapi.controller;

import com.br.lanchonete.lanchoneteapi.config.exception.DefaultException;
import com.br.lanchonete.lanchoneteapi.dto.*;
import com.br.lanchonete.lanchoneteapi.model.Order;
import com.br.lanchonete.lanchoneteapi.service.OrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<CreateOrderDTO> createOrder(@Valid @RequestBody CreateOrderDTO request) throws DefaultException {
        log.info("Creating order");
        try {
            return new ResponseEntity<>(orderService.createOrder(request), HttpStatus.CREATED);
        } catch (DefaultException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PostMapping("/add-product")
    public ResponseEntity<AddProductDTO> addProduct(@Valid @RequestBody AddProductDTO request) throws DefaultException {
        log.info("Adding product to order");
        try {
            return new ResponseEntity<>(orderService.addProduct(request), HttpStatus.CREATED);
        } catch (DefaultException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PostMapping("/remove-product")
    public ResponseEntity<Order> removeProduct(@Valid @RequestBody RemoveProductDTO request) {
        log.info("Adding product to order {}", request.getOrderId());
        try {
            return new ResponseEntity<>(orderService.removeProduct(request), HttpStatus.OK);
        } catch (DefaultException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @GetMapping("/total/{order_id}")
    public ResponseEntity<Order> getTotalPrice(@PathVariable(name = "order_id") String orderId) {
        log.info("Getting total price of order {}", orderId);
        try {
            return new ResponseEntity<>(orderService.getTotalPrice(orderId), HttpStatus.OK);
        } catch (DefaultException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PostMapping("/close")
    public ResponseEntity<CloseOrderReturnDTO> closeOrder(@RequestBody CloseOrderDTO request) {
        log.info("Closing order {}", request.getOrderId());
        try {
            return new ResponseEntity<>(orderService.closeOrder(request), HttpStatus.OK);
        } catch (DefaultException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PostMapping("/total_batch")
    public ResponseEntity<Order> getTotalPriceBatch(@RequestBody OrderTotalDTO request) {
        log.info("Getting total price of order batch {}", request.getOrderId());
        try {
            return new ResponseEntity<>(orderService.getTotalPriceBatch(request), HttpStatus.OK);
        } catch (DefaultException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}
