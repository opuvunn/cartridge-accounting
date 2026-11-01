
package com.example.cartridges;

import com.example.cartridges.repository.CartridgeRepository;
import com.example.cartridges.repository.OperationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CartridgeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CartridgeRepository cartridges;

    @Autowired
    private OperationRepository operations;

    @BeforeEach
    void clearTestDatabase() {
        operations.deleteAll();
        cartridges.deleteAll();
    }

    @Test
    void receiptIsSavedAndReturnedByApi() throws Exception {

        String request = """
                {
                  "cartridgeModel": "DEMO-DR-3400",
                  "operationType": "RECEIPT",
                  "quantity": 3,
                  "type": "DRUM",
                  "minStock": 1
                }
                """;

        mockMvc.perform(post("/api/operations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(3));

        mockMvc.perform(get("/api/state"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartridges[0].model")
                        .value("DEMO-DR-3400"))
                .andExpect(jsonPath("$.cartridges[0].type")
                        .value("DRUM"))
                .andExpect(jsonPath("$.cartridges[0].quantity")
                        .value(3))
                .andExpect(jsonPath("$.operations[0].operationType")
                        .value("RECEIPT"));

        assertEquals(3, cartridges
                .findByModelIgnoreCase("DEMO-DR-3400")
                .orElseThrow()
                .getQuantity());
    }

    @Test
    void excessiveReplacementIsRejected() throws Exception {

        String receipt = """
                {
                  "cartridgeModel": "DEMO-TN-3480",
                  "operationType": "RECEIPT",
                  "quantity": 2,
                  "type": "CARTRIDGE",
                  "minStock": 1
                }
                """;

        mockMvc.perform(post("/api/operations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(receipt))
                .andExpect(status().isOk());

        String replacement = """
                {
                  "cartridgeModel": "DEMO-TN-3480",
                  "operationType": "REPLACEMENT",
                  "quantity": 3,
                  "room": "Кабинет 101",
                  "printerModel": "Принтер 1"
                }
                """;

        mockMvc.perform(post("/api/operations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(replacement))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("Недостаточно картриджей на складе"));

        assertEquals(2, cartridges
                .findByModelIgnoreCase("DEMO-TN-3480")
                .orElseThrow()
                .getQuantity());

        assertEquals(1L, operations.count());
    }
}
