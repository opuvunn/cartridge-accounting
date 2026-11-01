
package com.example.cartridges.controller;

import com.example.cartridges.model.Operation;
import com.example.cartridges.service.CartridgeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class CartridgeController {

    private final CartridgeService service;

    public CartridgeController(CartridgeService service) {
        this.service = service;
    }

    @PostMapping("/operations")
    public OperationResponse register(
            @RequestBody OperationRequest request) {

        Operation operation = service.registerOperation(
                request.cartridgeModel(),
                request.operationType(),
                request.quantity(),
                request.type(),
                request.minStock(),
                request.room(),
                request.printerModel()
        );

        return new OperationResponse(
                operation.getId(),
                operation.getCartridge().getModel(),
                operation.getOperationType(),
                operation.getQuantity(),
                operation.getAfterQuantity()
        );
    }

    @GetMapping("/state")
    public CartridgeService.StateResponse getState() {
        return service.getState();
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            ArithmeticException.class
    })
    public ResponseEntity<Map<String, String>> handleError(
            RuntimeException exception) {

        return ResponseEntity.badRequest()
                .body(Map.of("error", exception.getMessage()));
    }

    public record OperationRequest(
            String cartridgeModel,
            String operationType,
            Integer quantity,
            String type,
            Integer minStock,
            String room,
            String printerModel
    ) {
    }

    public record OperationResponse(
            Long id,
            String model,
            String operationType,
            int quantity,
            int balance
    ) {
    }
}
