package com.sui.example.exporter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.sui.Client;

public class Main {
    public static void main(String[] args) {
        String username = "";
        String password = "";
        String accountBook = "默认账本";
        String filename = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd.xls"));

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-u")) {
                username = args[++i];
            } else if (args[i].equals("-p")) {
                password = args[++i];
            } else if (args[i].equals("-b")) {
                accountBook = args[++i];
            } else if (args[i].equals("-f")) {
                filename = args[++i];
            }
        }

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Usage: -u username -p password [-b accountBook] [-f filename]");
            return;
        }

        try {
            Client client = new Client(username, password);

            client.syncAccountBookList();

            client.switchAccountBook(accountBook);

            client.exportToFile(filename);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}