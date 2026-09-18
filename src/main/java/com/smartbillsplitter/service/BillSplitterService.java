package com.smartbillsplitter.service;

import com.smartbillsplitter.model.Item;
import com.smartbillsplitter.model.PersonResult;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Service engine providing high-precision monetary calculations for dividing group expenses,
 * tax distribution, tip allocation, and net debt settlement.
 */
public class BillSplitterService {

    private static final int CALCULATION_SCALE = 10;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    /**
     * Parses raw input text where each line conforms to:
     * ItemName,Price,Sharer1|Sharer2|Sharer3,Payer
     */
    public static List<Item> parseItems(String text) {
        List<Item> items = new ArrayList<>();
        if (text == null || text.trim().isEmpty()) {
            return items;
        }

        String[] lines = text.split("\n");
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("Enter") || trimmed.startsWith("Example")) {
                continue;
            }

            String[] parts = trimmed.split(",");
            if (parts.length < 4) {
                throw new IllegalArgumentException("Invalid line format: '" + trimmed + "'. Expected: ItemName,Price,Sharers,Payer");
            }

            String name = parts[0].trim();
            BigDecimal price = new BigDecimal(parts[1].trim());
            String[] rawSharers = parts[2].trim().split("\\|");
            List<String> sharers = new ArrayList<>();
            for (String s : rawSharers) {
                if (!s.trim().isEmpty()) {
                    sharers.add(s.trim());
                }
            }

            if (sharers.isEmpty()) {
                throw new IllegalArgumentException("Item '" + name + "' must have at least one sharer.");
            }

            String payer = parts[3].trim();
            items.add(new Item(name, price, sharers, payer));
        }

        return items;
    }

    /**
     * Calculates the individual breakdown and balances for all participants.
     */
    public static Map<String, PersonResult> splitBill(List<Item> items,
                                                     BigDecimal taxPercent,
                                                     BigDecimal tipPercent) {
        if (items == null || items.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, PersonResult> results = new TreeMap<>();
        Set<String> people = new HashSet<>();

        for (Item item : items) {
            people.addAll(item.getSharers());
            people.add(item.getPayer());
        }

        for (String person : people) {
            results.put(person, new PersonResult());
        }

        // Divide item shares
        for (Item item : items) {
            BigDecimal share = item.getPrice().divide(
                BigDecimal.valueOf(item.getSharers().size()), CALCULATION_SCALE, ROUNDING_MODE
            );

            for (String sharer : item.getSharers()) {
                PersonResult pr = results.get(sharer);
                pr.setSubtotal(pr.getSubtotal().add(share));
            }

            PersonResult payerResult = results.get(item.getPayer());
            payerResult.setPaid(payerResult.getPaid().add(item.getPrice()));
        }

        // Calculate proportional tax & tip
        BigDecimal taxRate = taxPercent != null 
            ? taxPercent.divide(BigDecimal.valueOf(100), CALCULATION_SCALE, ROUNDING_MODE)
            : BigDecimal.ZERO;
        BigDecimal tipRate = tipPercent != null
            ? tipPercent.divide(BigDecimal.valueOf(100), CALCULATION_SCALE, ROUNDING_MODE)
            : BigDecimal.ZERO;

        for (PersonResult pr : results.values()) {
            pr.setTax(pr.getSubtotal().multiply(taxRate).setScale(2, ROUNDING_MODE));
            pr.setTip(pr.getSubtotal().multiply(tipRate).setScale(2, ROUNDING_MODE));
            pr.setTotal(pr.getSubtotal().add(pr.getTax()).add(pr.getTip()).setScale(2, ROUNDING_MODE));
            pr.setNet(pr.getTotal().subtract(pr.getPaid()).setScale(2, ROUNDING_MODE));
        }

        return results;
    }

    /**
     * Formats calculation results into a human-readable tabular ASCII report.
     */
    public static String formatSummary(Map<String, PersonResult> results) {
        StringBuilder sb = new StringBuilder();
        sb.append("=================================== BILL SUMMARY ===================================\n");
        sb.append(String.format("%-14s %12s %10s %10s %12s %12s\n",
            "Name", "Subtotal", "Tax", "Tip", "Paid", "Net Balance"));
        sb.append("------------------------------------------------------------------------------------\n");

        for (Map.Entry<String, PersonResult> e : results.entrySet()) {
            PersonResult r = e.getValue();
            sb.append(String.format("%-14s %12.2f %10.2f %10.2f %12.2f %12.2f\n",
                e.getKey(),
                r.getSubtotal(),
                r.getTax(),
                r.getTip(),
                r.getPaid(),
                r.getNet()));
        }
        sb.append("====================================================================================\n");
        sb.append("Note: Negative Net Balance indicates money to be received; Positive indicates amount owed.\n");

        return sb.toString();
    }
}