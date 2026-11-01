
package com.example.cartridges.service;

import com.example.cartridges.model.Cartridge;
import com.example.cartridges.model.Operation;
import com.example.cartridges.repository.CartridgeRepository;
import com.example.cartridges.repository.OperationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartridgeServiceTest {

    @Mock
    private CartridgeRepository cartridges;

    @Mock
    private OperationRepository operations;

    @InjectMocks
    private CartridgeService service;

    @Test
    void receiptIncreasesStock() {
        when(cartridges.findByModelIgnoreCase("TN-3480"))
                .thenReturn(Optional.empty());

        when(operations.save(any(Operation.class)))
                .thenAnswer(call -> call.getArgument(0));

        Operation result = service.registerOperation(
                "TN-3480", "RECEIPT", 10,
                "CARTRIDGE", 2, null, null
        );

        assertEquals(10, result.getAfterQuantity());
        assertEquals(0, result.getBeforeQuantity());
        assertEquals("RECEIPT", result.getOperationType());

        verify(cartridges).save(any(Cartridge.class));
        verify(operations).save(any(Operation.class));
    }

    @Test
    void cannotReplaceMoreThanAvailable() {
        Cartridge drum = new Cartridge();
        drum.setModel("DR-3400");
        drum.setType("DRUM");
        drum.setQuantity(3);
        drum.setMinStock(1);

        when(cartridges.findByModelIgnoreCase("DR-3400"))
                .thenReturn(Optional.of(drum));

        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> service.registerOperation(
                        "DR-3400", "REPLACEMENT", 4,
                        null, null, "Кабинет 101", "Принтер 1"
                )
        );


        assertEquals(
                "Недостаточно фотобарабанов на складе",
                error.getMessage()
        );

        assertEquals(3, drum.getQuantity());
        verify(operations, never()).save(any());
        verify(cartridges, never()).save(any());
    }

    @Test
    void replacementDecreasesStock() {
        Cartridge cartridge = new Cartridge();
        cartridge.setModel("TN-3480");
        cartridge.setType("CARTRIDGE");
        cartridge.setQuantity(8);
        cartridge.setMinStock(2);

        when(cartridges.findByModelIgnoreCase("TN-3480"))
                .thenReturn(Optional.of(cartridge));

        when(operations.save(any(Operation.class)))
                .thenAnswer(call -> call.getArgument(0));

        Operation result = service.registerOperation(
                "TN-3480", "REPLACEMENT", 2,
                null, null, "Кабинет 101", "Принтер 1"
        );

        assertEquals(8, result.getBeforeQuantity());
        assertEquals(6, result.getAfterQuantity());
        assertEquals(6, cartridge.getQuantity());
        assertEquals("Кабинет 101", result.getRoom());
        assertEquals("Принтер 1", result.getPrinterModel());

        verify(cartridges).save(cartridge);
        verify(operations).save(any(Operation.class));
    }

    @Test
    void inventoryCorrectsStock() {
        Cartridge cartridge = new Cartridge();
        cartridge.setModel("TN-3480");
        cartridge.setType("CARTRIDGE");
        cartridge.setQuantity(9);
        cartridge.setMinStock(2);

        when(cartridges.findByModelIgnoreCase("TN-3480"))
                .thenReturn(Optional.of(cartridge));

        when(operations.save(any(Operation.class)))
                .thenAnswer(call -> call.getArgument(0));

        Operation result = service.registerOperation(
                "TN-3480", "INVENTORY", 8,
                null, null, "Кабинет 101", "Принтер 1"
        );

        assertEquals(9, result.getBeforeQuantity());
        assertEquals(8, result.getAfterQuantity());
        assertEquals(8, cartridge.getQuantity());
        assertNull(result.getRoom());
        assertNull(result.getPrinterModel());

        verify(cartridges).save(cartridge);
        verify(operations).save(any(Operation.class));
    }

    @Test
    void negativeQuantityIsRejected() {
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> service.registerOperation(
                        "TN-3480", "RECEIPT", -1,
                        "CARTRIDGE", 2, null, null
                )
        );

        assertEquals("Некорректное количество",
                error.getMessage());

        verifyNoInteractions(cartridges, operations);
    }

    @Test
    void inventoryAllowsZeroStock() {
        Cartridge cartridge = new Cartridge();
        cartridge.setModel("DR-3400");
        cartridge.setType("DRUM");
        cartridge.setQuantity(3);
        cartridge.setMinStock(1);

        when(cartridges.findByModelIgnoreCase("DR-3400"))
                .thenReturn(Optional.of(cartridge));

        when(operations.save(any(Operation.class)))
                .thenAnswer(call -> call.getArgument(0));

        Operation result = service.registerOperation(
                "DR-3400", "INVENTORY", 0,
                null, null, null, null
        );

        assertEquals(3, result.getBeforeQuantity());
        assertEquals(0, result.getAfterQuantity());
        assertEquals(0, cartridge.getQuantity());

        verify(cartridges).save(cartridge);
        verify(operations).save(any(Operation.class));
    }

}
