package org.dentamuhajir.parcellocker.cli;

import org.dentamuhajir.parcellocker.application.AuthService;
import org.dentamuhajir.parcellocker.domain.model.User;

public class CommandRouter {

    private final AuthService authService;

    public CommandRouter(AuthService authService) {
        this.authService = authService;
    }

    public boolean route(String command) {

        command = command.trim();

        if (command.isBlank()) {
            return true;
        }

        if (command.startsWith("login ")) {

            String username = command.substring(6).trim();

            if (username.isBlank()) {
                System.out.println("Username is required.");
                return true;
            }

            User user = authService.login(username);

            System.out.println("Hello, " + user.getUsername() + "!");
            return true;
        }

        switch (command.toLowerCase()) {

            case "logout":
                handleLogout();
                return true;

            case "whoami":
                handleWhoAmI();
                return true;

            case "help":
                showHelp();
                return true;

            case "exit":
                System.out.println("Goodbye.");
                return false;

            default:
                System.out.println("Unknown command.");
                System.out.println("Type 'help' to see available commands.");
                return true;
        }
    }

    private void handleLogout() {

        if (!authService.isLoggedIn()) {
            System.out.println("No user is currently logged in.");
            return;
        }

        String username =
                authService.getCurrentUser().getUsername();

        authService.logout();

        System.out.println("Goodbye, " + username + "!");
    }

    private void handleWhoAmI() {

        if (!authService.isLoggedIn()) {
            System.out.println("Not logged in.");
            return;
        }

        System.out.println(
                authService.getCurrentUser().getUsername()
        );
    }

    private void showHelp() {

        System.out.println();
        System.out.println("Available Commands:");
        System.out.println("login <user>");
        System.out.println("logout");
        System.out.println("whoami");
        System.out.println("help");
        System.out.println("exit");
        System.out.println();
    }
}