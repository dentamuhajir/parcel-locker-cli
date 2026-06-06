package org.dentamuhajir.parcellocker;

import org.dentamuhajir.parcellocker.application.AuthService;
import org.dentamuhajir.parcellocker.application.LockerService;
import org.dentamuhajir.parcellocker.cli.CommandRouter;
import org.dentamuhajir.parcellocker.domain.model.User;
import org.dentamuhajir.parcellocker.domain.repository.LockerRepository;
import org.dentamuhajir.parcellocker.domain.repository.UserRepository;
import org.dentamuhajir.parcellocker.infrastructure.memory.InMemoryLockerRepository;
import org.dentamuhajir.parcellocker.infrastructure.memory.InMemoryUserRepository;
import org.dentamuhajir.parcellocker.session.UserSession;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        CommandRouter commandRouter = createCommandRouter();
        Scanner scanner = new Scanner(System.in);

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

    private static CommandRouter createCommandRouter() {

        UserRepository userRepository = new InMemoryUserRepository();

        userRepository.save(new User("admin"));

        UserSession userSession = new UserSession();

        AuthService authService =
                new AuthService(
                        userRepository,
                        userSession
                );

        LockerRepository lockerRepository = new InMemoryLockerRepository();

        LockerService lockerService = new LockerService(lockerRepository);


        return new CommandRouter(
                authService,
                lockerService
        );
    }
}