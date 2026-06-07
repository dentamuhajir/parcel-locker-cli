package org.dentamuhajir.parcellocker.cli;

import org.dentamuhajir.parcellocker.application.AuthService;
import org.dentamuhajir.parcellocker.application.LockerService;
import org.dentamuhajir.parcellocker.domain.model.User;

public class CommandRouter {

    private final AuthService authService;
    private final LockerService lockerService;

    public CommandRouter(AuthService authService, LockerService lockerService) {
        this.authService = authService;
        this.lockerService = lockerService;

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

        if (command.startsWith("add-locker ")) {

            if (!authService.isLoggedIn()) {
                System.out.println("Please login first.");
                return true;
            }

            String username =
                    authService.getCurrentUser()
                            .getUsername();

            if (!"admin".equalsIgnoreCase(username)) {
                System.out.println(
                        "Only admin can register lockers."
                );
                return true;
            }

            String lockerId =
                    command.substring(11).trim();

            try {

                lockerService.addLocker(lockerId);

                System.out.println(
                        "Locker " + lockerId +
                                " has been registered."
                );

            } catch (IllegalArgumentException e) {

                System.out.println(e.getMessage());
            }

            return true;
        }

        if (command.equalsIgnoreCase("list-lockers")) {

            if (lockerService.getAllLockers().isEmpty()) {

                System.out.println(
                        "No lockers registered."
                );

                return true;
            }

            lockerService
                    .getAllLockers()
                    .forEach(locker -> {

                        String status;

                        if (locker.isAvailable()) {

                            status = "AVAILABLE";

                        } else {

                            status =
                                    "RESERVED by "
                                            + locker.getAssignedUser()
                                            .map(User::getUsername)
                                            .orElse("UNKNOWN");
                        }

                        System.out.println(
                                locker.getLockerId()
                                        + " - "
                                        + status
                        );
                    });

            return true;
        }

        if (command.startsWith("reserve ")) {

            if (!authService.isLoggedIn()) {
                System.out.println("Please login first.");
                return true;
            }

            String lockerId =
                    command.substring(8).trim();

            User currentUser =
                    authService.getCurrentUser();

            try {

                lockerService.reserveLocker(
                        lockerId,
                        currentUser
                );

                System.out.println(
                        "Locker "
                                + lockerId
                                + " has been reserved for you."
                );

            } catch (IllegalArgumentException e) {

                System.out.println(
                        e.getMessage()
                );
            }

            return true;
        }

        if (command.startsWith("queue ")) {

            if (!authService.isLoggedIn()) {

                System.out.println(
                        "Please login first."
                );

                return true;
            }

            String lockerId =
                    command.substring(6).trim();

            try {

                lockerService.joinQueue(
                        lockerId,
                        authService.getCurrentUser()
                );

                System.out.println(
                        "Added to waiting queue for locker "
                                + lockerId
                );

            } catch (IllegalArgumentException e) {

                System.out.println(
                        e.getMessage()
                );
            }

            return true;
        }

        if (command.startsWith("release ")) {

            if (!authService.isLoggedIn()) {

                System.out.println(
                        "Please login first."
                );

                return true;
            }

            String lockerId =
                    command.substring(8).trim();

            try {

                String assignedUser =
                        lockerService.releaseLocker(
                                lockerId,
                                authService.getCurrentUser()
                        );

                System.out.println(
                        "Locker "
                                + lockerId
                                + " released."
                );

                if (assignedUser != null) {

                    System.out.println(
                            "Locker automatically assigned to "
                                    + assignedUser
                    );
                }

            } catch (IllegalArgumentException e) {

                System.out.println(
                        e.getMessage()
                );
            }

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
        System.out.println("add-locker <locker-id>");
        System.out.println("reserve <locker-id>");
        System.out.println("list-lockers");
        System.out.println("queue <locker-id>");
        System.out.println("release <locker-id>");
        System.out.println("help");
        System.out.println("exit");
        System.out.println();
    }
}