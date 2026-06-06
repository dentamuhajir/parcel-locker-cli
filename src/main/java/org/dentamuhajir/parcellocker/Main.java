package org.dentamuhajir.parcellocker;

import org.dentamuhajir.parcellocker.cli.CommandRouter;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        CommandRouter commandRouter = new CommandRouter();

        boolean running = true;

        System.out.println("=== Parcel Locker CLI ===");
        System.out.println("Type 'help' to see available commands.");

        while (running) {

            System.out.print("> ");

            String input = scanner.nextLine().trim();

            running = commandRouter.route(input);
        }

        scanner.close();
    }
}