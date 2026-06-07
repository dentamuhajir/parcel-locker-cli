# Parcel Locker CLI

A command-line application for managing parcel lockers.

## Features

- User login/logout
- Admin-only locker registration
- Locker reservation
- Waiting queue management
- Automatic locker assignment
- User status tracking
- Assignment notifications

All data is stored in memory and will be reset when the application stops.

---

# Requirements

- Java 17+

---

# Run Application

1. Open the project using IntelliJ IDEA.
2. Locate:

```text
src/main/java/org/dentamuhajir/parcellocker/Main.java
```

3. Run `Main.java`.

The application will start in the terminal:

```text
=== Parcel Locker CLI ===
Type 'help' to see available commands.
```

---

# Available Commands

## Authentication

```text
login <user>
logout
whoami
```

## Locker Management

```text
add-locker <locker-id>
list-lockers
```

## Parcel Pickup Flow

```text
reserve <locker-id>
queue <locker-id>
release <locker-id>
```

## User Status

```text
status
```

## Utility

```text
help
exit
```

---

# Example Session

```text
login admin
add-locker A101

logout

login Alice
reserve A101

logout

login Bob
queue A101

logout

login Alice
release A101

logout

login Bob

[NOTIFICATION]
Locker A101 has been automatically assigned to you.
```

---
