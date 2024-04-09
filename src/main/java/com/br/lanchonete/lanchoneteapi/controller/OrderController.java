package com.br.lanchonete.lanchoneteapi.controller;

import com.br.lanchonete.lanchoneteapi.config.exception.DefaultException;
import com.br.lanchonete.lanchoneteapi.dto.AddProductDTO;
import com.br.lanchonete.lanchoneteapi.dto.CreateOrderDTO;
import com.br.lanchonete.lanchoneteapi.dto.CreateProductDTO;
import com.br.lanchonete.lanchoneteapi.service.OrderService;
import com.br.lanchonete.lanchoneteapi.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Controller
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
}
