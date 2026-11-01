
package com.example.cartridges.service;

import com.example.cartridges.model.Cartridge;
import com.example.cartridges.model.Operation;
import com.example.cartridges.repository.CartridgeRepository;
import com.example.cartridges.repository.OperationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.time.LocalDateTime;

@Service
public class CartridgeService {

    private final CartridgeRepository cartridges;
    private final OperationRepository operations;

    public CartridgeService(CartridgeRepository cartridges,
                            OperationRepository operations) {
        this.cartridges = cartridges;
        this.operations = operations;
    }

    @Transactional
    public Operation registerOperation(
            String model,
            String operationType,
            Integer quantity,
            String type,
            Integer minStock,
            String room,
            String printerModel) {

        String name = required(model, "Модель расходного материала")
                .toUpperCase(Locale.ROOT);

        String action = required(operationType, "Тип операции")
                .toUpperCase(Locale.ROOT);

        if (!List.of("RECEIPT", "REPLACEMENT", "INVENTORY")
                .contains(action)) {
            throw new IllegalArgumentException("Неизвестная операция");
        }

        if (quantity == null || quantity < 0 ||
                (!action.equals("INVENTORY") && quantity == 0)) {
            throw new IllegalArgumentException(
                    "Некорректное количество");
        }

        Cartridge cartridge = cartridges
                .findByModelIgnoreCase(name)
                .orElse(null);

        if (cartridge == null) {
            if (!action.equals("RECEIPT")) {
                throw new IllegalArgumentException(
                        "Сначала зарегистрируйте поступление");
            }

            String materialType = required(type, "Тип расходного материала")
                    .toUpperCase(Locale.ROOT);

            if (!List.of("CARTRIDGE", "DRUM")
                    .contains(materialType)) {
                throw new IllegalArgumentException(
                        "Тип должен быть «Картридж» или «Фотобарабан»");
            }

            if (minStock == null || minStock < 0) {
                throw new IllegalArgumentException(
                        "Некорректный минимальный остаток");
            }

            cartridge = new Cartridge();
            cartridge.setModel(name);
            cartridge.setType(materialType);
            cartridge.setQuantity(0);
            cartridge.setMinStock(minStock);
        }

        int before = cartridge.getQuantity();
        int after;

        switch (action) {
            case "RECEIPT":
                after = Math.addExact(before, quantity);
                break;

            case "REPLACEMENT":
                room = required(room, "Кабинет");
                printerModel = required(printerModel, "Принтер");


                if (quantity > before) {
                    String materialName = "DRUM".equals(cartridge.getType())
                            ? "фотобарабанов" : "картриджей";

                    throw new IllegalArgumentException(
                            "Недостаточно " + materialName + " на складе");
                }


                after = before - quantity;
                break;

            case "INVENTORY":
                after = quantity;
                break;

            default:
                throw new IllegalArgumentException(
                        "Неизвестная операция");
        }

        cartridge.setQuantity(after);
        cartridges.save(cartridge);

        if (!action.equals("REPLACEMENT")) {
            room = null;
            printerModel = null;
        }

        Operation operation = new Operation(
                cartridge, action, quantity,
                room, printerModel, before, after
        );

        return operations.save(operation);
    }

    @Transactional(readOnly = true)
    public List<Cartridge> getCartridges() {
        return cartridges.findAll();
    }

    @Transactional(readOnly = true)
    public List<Operation> getOperations() {
        return operations.findAll();
    }

    private String required(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Не заполнено поле: " + field);
        }

        String result = value.trim();

        if (result.length() > 255) {
            throw new IllegalArgumentException(
                    "Слишком длинное значение: " + field);
        }

        return result;
    }

    public record CartridgeInfo(
            String model,
            String type,
            int quantity,
            int minStock,
            boolean lowStock
    ) {}

    public record OperationInfo(
            Long id,
            String model,
            String type,
            String operationType,
            int quantity,
            String room,
            String printerModel,
            LocalDateTime performedAt,
            int beforeQuantity,
            int afterQuantity
    ) {}

    public record StateResponse(
            List<CartridgeInfo> cartridges,
            List<OperationInfo> operations
    ) {}

    @Transactional(readOnly = true)
    public StateResponse getState() {

        List<CartridgeInfo> stock = cartridges.findAll()
                .stream()
                .map(c -> new CartridgeInfo(
                        c.getModel(),
                        c.getType(),
                        c.getQuantity(),
                        c.getMinStock(),
                        c.getQuantity() <= c.getMinStock()
                ))
                .toList();

        List<OperationInfo> history = operations.findAll()
                .stream()
                .map(o -> new OperationInfo(
                        o.getId(),
                        o.getCartridge().getModel(),
                        o.getCartridge().getType(),
                        o.getOperationType(),
                        o.getQuantity(),
                        o.getRoom(),
                        o.getPrinterModel(),
                        o.getPerformedAt(),
                        o.getBeforeQuantity(),
                        o.getAfterQuantity()
                ))
                .toList();

        return new StateResponse(stock, history);
    }

}
