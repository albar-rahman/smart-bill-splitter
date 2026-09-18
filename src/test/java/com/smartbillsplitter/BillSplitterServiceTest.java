package com.smartbillsplitter;

import com.smartbillsplitter.model.Item;
import com.smartbillsplitter.model.PersonResult;
import com.smartbillsplitter.service.BillSplitterService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class BillSplitterServiceTest {

    @Test
    public void testEqualSplitWithPayer() {
        // Pizza cost 400, shared by Alice and Bob, paid by Alice
        Item item1 = new Item("Pizza", new BigDecimal("400.00"), Arrays.asList("Alice", "Bob"), "Alice");
        
        Map<String, PersonResult> results = BillSplitterService.splitBill(
            List.of(item1),
            BigDecimal.ZERO,
            BigDecimal.ZERO
        );

        assertNotNull(results.get("Alice"));
        assertNotNull(results.get("Bob"));

        assertEquals(new BigDecimal("200.0000000000"), results.get("Alice").getSubtotal());
        assertEquals(new BigDecimal("200.0000000000"), results.get("Bob").getSubtotal());
        
        // Alice paid 400, owes 200 => Net = 200 - 400 = -200 (Alice receives 200)
        assertEquals(new BigDecimal("-200.00"), results.get("Alice").getNet());
        // Bob paid 0, owes 200 => Net = 200 - 0 = 200 (Bob pays 200)
        assertEquals(new BigDecimal("200.00"), results.get("Bob").getNet());
    }

    @Test
    public void testTaxAndTipCalculation() {
        Item item = new Item("Dinner", new BigDecimal("100.00"), List.of("Charlie"), "Charlie");
        
        Map<String, PersonResult> results = BillSplitterService.splitBill(
            List.of(item),
            new BigDecimal("10.0"), // 10% tax
            new BigDecimal("15.0")  // 15% tip
        );

        PersonResult r = results.get("Charlie");
        assertEquals(new BigDecimal("10.00"), r.getTax());
        assertEquals(new BigDecimal("15.00"), r.getTip());
        assertEquals(new BigDecimal("125.00"), r.getTotal());
        assertEquals(new BigDecimal("25.00"), r.getNet()); // 125 total - 100 paid = 25 net
    }
}