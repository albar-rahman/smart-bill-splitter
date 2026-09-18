package com.smartbillsplitter.model;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a line item in an expense record, including its price,
 * participants sharing the cost, and the person who initially paid.
 */
public class Item {
    private final String name;
    private final BigDecimal price;
    private final List<String> sharers;
    private final String payer;

    public Item(String name, BigDecimal price, List<String> sharers, String payer) {
        this.name = Objects.requireNonNull(name, "Item name cannot be null").trim();
        this.price = Objects.requireNonNull(price, "Price cannot be null");
        this.sharers = Collections.unmodifiableList(Objects.requireNonNull(sharers, "Sharers list cannot be null"));
        this.payer = Objects.requireNonNull(payer, "Payer cannot be null").trim();
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public List<String> getSharers() {
        return sharers;
    }

    public String getPayer() {
        return payer;
    }

    @Override
    public String toString() {
        return String.format("%s [Price: %s, Paid by: %s, Shared by: %s]", name, price, payer, sharers);
    }
}