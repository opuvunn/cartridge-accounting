
package com.example.cartridges.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "operations")
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cartridge_id", nullable = false)
    private Cartridge cartridge;

    @Column(nullable = false)
    private String operationType;

    @Column(nullable = false)
    private int quantity;

    private String room;
    private String printerModel;

    @Column(nullable = false)
    private LocalDateTime performedAt;

    @Column(nullable = false)
    private int beforeQuantity;

    @Column(nullable = false)
    private int afterQuantity;

    protected Operation() {
    }

    public Operation(Cartridge cartridge, String operationType,
                     int quantity, String room, String printerModel,
                     int beforeQuantity, int afterQuantity) {
        this.cartridge = cartridge;
        this.operationType = operationType;
        this.quantity = quantity;
        this.room = room;
        this.printerModel = printerModel;
        this.beforeQuantity = beforeQuantity;
        this.afterQuantity = afterQuantity;
        this.performedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Cartridge getCartridge() {
        return cartridge;
    }

    public String getOperationType() {
        return operationType;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getRoom() {
        return room;
    }

    public String getPrinterModel() {
        return printerModel;
    }

    public LocalDateTime getPerformedAt() {
        return performedAt;
    }

    public int getBeforeQuantity() {
        return beforeQuantity;
    }

    public int getAfterQuantity() {
        return afterQuantity;
    }
}
