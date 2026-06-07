package org.dentamuhajir.parcellocker.cli;

import org.dentamuhajir.parcellocker.application.AuthService;
import org.dentamuhajir.parcellocker.application.LockerService;
import org.dentamuhajir.parcellocker.application.StatusService;
import org.dentamuhajir.parcellocker.domain.model.User;

public class CommandRouter {

    private final AuthService authService;
    private final LockerService lockerService;
    private final StatusService statusService;

    public CommandRouter(
            AuthService authService,
            LockerService lockerService,
            StatusService statusService
    ) {
        this.authService = authService;
        this.lockerService = lockerService;
        this.statusService = statusService;
    }

    public boolean route(String command) {

        command = command.trim();

        if (command.isBlank()) {
            return true;
        }

        String action = getAction(command);

        switch (action) {

            case "login":
                return handleLogin(command);

            case "logout":
                handleLogout();
                return true;

            case "whoami":
                handleWhoAmI();
                return true;

            case "add-locker":
                return handleAddLocker(command);

            case "list-lockers":
                return handleListLockers();

            case "reserve":
                return handleReserve(command);

            case "queue":
                return handleQueue(command);

            case "release":
                return handleRelease(command);

            case "status":
                return handleStatus();

            case "help":
                showHelp();
                return true;

            case "exit":
                System.out.println("Goodbye.");
                return false;

            default:
                System.out.println("Unknown command.");
                System.out.println(
                        "Type 'help' to see available commands."
                );
                return true;
        }
    }

    private boolean handleLogin(String command) {

        String username =
                extractArgument(command, "login");

        if (username.isBlank()) {

            System.out.println(
                    "Username is required."
            );

            return true;
        }

        User user =
                authService.login(username);

        System.out.println(
                "Hello, "
                        + user.getUsername()
                        + "!"
        );

        authService.consumeNotifications()
                .forEach(notification ->
                        System.out.println(
                                "[NOTIFICATION] "
                                        + notification
                        ));

        return true;
    }

    private boolean handleAddLocker(String command) {

        if (!requireLogin()) {
            return true;
        }

        if (!isAdmin()) {

            System.out.println(
                    "Only admin can register lockers."
            );

            return true;
        }

        String lockerId =
                extractArgument(
                        command,
                        "add-locker"
                );

        try {

            lockerService.addLocker(lockerId);

            System.out.println(
                    "Locker "
                            + lockerId
                            + " has been registered."
            );

        } catch (IllegalArgumentException e) {

            System.out.println(
                    e.getMessage()
            );
        }

        return true;
    }

    private boolean handleListLockers() {

        if (lockerService.getAllLockers().isEmpty()) {

            System.out.println(
                    "No lockers registered."
            );

            return true;
        }

        lockerService.getAllLockers()
                .forEach(locker -> {

                    String status;

                    if (locker.isAvailable()) {

                        status = "AVAILABLE";

                    } else {

                        status =
                                "RESERVED by "
                                        + locker
                                        .getAssignedUser()
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

    private boolean handleReserve(String command) {

        if (!requireLogin()) {
            return true;
        }

        String lockerId =
                extractArgument(
                        command,
                        "reserve"
                );

        try {

            lockerService.reserveLocker(
                    lockerId,
                    authService.getCurrentUser()
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

    private boolean handleQueue(String command) {

        if (!requireLogin()) {
            return true;
        }

        String lockerId =
                extractArgument(
                        command,
                        "queue"
                );

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

    private boolean handleRelease(String command) {

        if (!requireLogin()) {
            return true;
        }

        String lockerId =
                extractArgument(
                        command,
                        "release"
                );

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

    private boolean handleStatus() {

        if (!requireLogin()) {
            return true;
        }

        User currentUser =
                authService.getCurrentUser();

        System.out.println(
                "Assigned lockers:"
        );

        var assignedLockers =
                statusService.getAssignedLockers(
                        currentUser
                );

        if (assignedLockers.isEmpty()) {

            System.out.println("None");

        } else {

            for (int i = 0;
                 i < assignedLockers.size();
                 i++) {

                System.out.println(
                        (i + 1)
                                + ". "
                                + assignedLockers.get(i)
                );
            }
        }

        System.out.println();
        System.out.println(
                "Waiting queues:"
        );

        var queuePositions =
                statusService.getQueuePositions(
                        currentUser
                );

        if (queuePositions.isEmpty()) {

            System.out.println("None");

        } else {

            queuePositions.forEach(
                    System.out::println
            );
        }

        return true;
    }

    private void handleLogout() {

        if (!authService.isLoggedIn()) {

            System.out.println(
                    "No user is currently logged in."
            );

            return;
        }

        String username =
                authService.getCurrentUser()
                        .getUsername();

        authService.logout();

        System.out.println(
                "Goodbye, "
                        + username
                        + "!"
        );
    }

    private void handleWhoAmI() {

        if (!authService.isLoggedIn()) {

            System.out.println(
                    "Not logged in."
            );

            return;
        }

        System.out.println(
                authService.getCurrentUser()
                        .getUsername()
        );
    }

    private void showHelp() {

        System.out.println();
        System.out.println("Available Commands:");
        System.out.println("login <user>");
        System.out.println("logout");
        System.out.println("whoami");
        System.out.println("add-locker <locker-id>");
        System.out.println("list-lockers");
        System.out.println("reserve <locker-id>");
        System.out.println("queue <locker-id>");
        System.out.println("release <locker-id>");
        System.out.println("status");
        System.out.println("help");
        System.out.println("exit");
        System.out.println();
    }

    private boolean requireLogin() {

        if (!authService.isLoggedIn()) {

            System.out.println(
                    "Please login first."
            );

            return false;
        }

        return true;
    }

    private boolean isAdmin() {

        return "admin".equalsIgnoreCase(
                authService.getCurrentUser()
                        .getUsername()
        );
    }

    private String getAction(String command) {

        int index =
                command.indexOf(' ');

        if (index == -1) {
            return command.toLowerCase();
        }

        return command.substring(
                        0,
                        index
                )
                .toLowerCase();
    }

    private String extractArgument(
            String command,
            String action
    ) {
        return command
                .substring(action.length())
                .trim();
    }
}