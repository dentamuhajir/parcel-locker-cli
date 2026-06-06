package org.dentamuhajir.parcellocker.cli;

public class CommandRouter {

    public boolean route(String command) {

        switch (command.toLowerCase()) {

            case "help":
                showHelp();
                return true;

            case "exit":
                System.out.println("Goodbye.");
                return false;

            case "":
                return true;

            default:
                System.out.println("Unknown command.");
                System.out.println("Type 'help' to see available commands.");
                return true;
        }
    }

    private void showHelp() {

        System.out.println();
        System.out.println("Available Commands:");
        System.out.println("help  - Show available commands");
        System.out.println("exit  - Exit application");
        System.out.println();
    }
}