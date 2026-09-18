package com.smartbillsplitter;

import java.awt.GraphicsEnvironment;

/**
 * Universal Entry Point for Smart Bill Splitter.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("        SMART BILL SPLITTER (JAVA)       ");
        System.out.println("=========================================");

        if (!GraphicsEnvironment.isHeadless()) {
            System.out.println("Launching AWT Graphical Interface...");
            SmartBillSplitterAWT.main(args);
        } else {
            System.out.println("Running in Headless mode (No GUI display server available).");
            System.out.println("To open the graphical interface, run on a desktop environment.");
        }
    }
}