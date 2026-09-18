package com.smartbillsplitter;

import com.smartbillsplitter.model.Item;
import com.smartbillsplitter.model.PersonResult;
import com.smartbillsplitter.service.BillSplitterService;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * SmartBillSplitterAWT
 * Graphical User Interface built with Java Abstract Window Toolkit (AWT).
 *
 * Project by:
 * - ALBAR RAHMAN A (2403811710621004)
 * - CAUVIRISELVAN K (2403811710621014)
 * - DINESHKAR M (2403811710621026)
 *
 * Department of Electronics and Communication Engineering
 * K. Ramakrishnan College of Technology (Autonomous)
 */
public class SmartBillSplitterAWT extends Frame implements ActionListener {
    private TextField peopleField, itemsField, taxField, tipField;
    private TextArea itemDataArea, outputArea;
    private Button calculateBtn, clearBtn;

    public SmartBillSplitterAWT() {
        setTitle("Smart Bill Splitter (AWT)");
        setSize(740, 620);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // ===== TOP PANEL =====
        Panel top = new Panel(new GridLayout(2, 4, 10, 10));
        top.add(new Label("People Count:"));
        peopleField = new TextField("3");
        top.add(peopleField);

        top.add(new Label("Items Count:"));
        itemsField = new TextField("2");
        top.add(itemsField);

        top.add(new Label("Tax %:"));
        taxField = new TextField("5.0");
        top.add(taxField);

        top.add(new Label("Tip %:"));
        tipField = new TextField("10.0");
        top.add(tipField);

        add(top, BorderLayout.NORTH);

        // ===== CENTER PANEL =====
        Panel center = new Panel(new GridLayout(2, 1, 10, 10));
        itemDataArea = new TextArea(
            "Enter item data in this format:\n\n" +
            "ItemName,Price,Sharer1|Sharer2|Sharer3,Payer\n\n" +
            "Example:\n" +
            "Pizza,450,Alice|Bob,Alice\n" +
            "Burger,200,Bob|Charlie,Bob\n",
            8, 60
        );
        center.add(itemDataArea);

        outputArea = new TextArea("", 8, 60);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        center.add(outputArea);

        add(center, BorderLayout.CENTER);

        // ===== BOTTOM PANEL =====
        Panel bottom = new Panel();
        calculateBtn = new Button("Calculate");
        clearBtn = new Button("Clear");

        calculateBtn.addActionListener(this);
        clearBtn.addActionListener(this);

        bottom.add(calculateBtn);
        bottom.add(clearBtn);

        add(bottom, BorderLayout.SOUTH);

        // Window close action
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == clearBtn) {
            itemDataArea.setText("");
            outputArea.setText("");
            return;
        }

        try {
            String taxText = taxField.getText().trim();
            String tipText = tipField.getText().trim();

            BigDecimal taxPercent = taxText.isEmpty() ? BigDecimal.ZERO : new BigDecimal(taxText);
            BigDecimal tipPercent = tipText.isEmpty() ? BigDecimal.ZERO : new BigDecimal(tipText);

            List<Item> items = BillSplitterService.parseItems(itemDataArea.getText());
            if (items.isEmpty()) {
                outputArea.setText("Please enter at least one valid item line.");
                return;
            }

            Map<String, PersonResult> results = BillSplitterService.splitBill(items, taxPercent, tipPercent);
            outputArea.setText(BillSplitterService.formatSummary(results));

        } catch (NumberFormatException nfe) {
            outputArea.setText("Error: Invalid numeric input format: " + nfe.getMessage());
        } catch (Exception ex) {
            outputArea.setText("Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new SmartBillSplitterAWT());
    }
}