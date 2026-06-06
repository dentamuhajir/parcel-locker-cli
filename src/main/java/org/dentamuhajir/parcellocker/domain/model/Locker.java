package org.dentamuhajir.parcellocker.domain.model;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

    public class Locker {

        private final String lockerId;

        private User assignedUser;

        private final Queue<User> waitingQueue;

        public Locker(String lockerId) {
            this.lockerId = lockerId;
            this.waitingQueue = new LinkedList<>();
        }

        public String getLockerId() {
            return lockerId;
        }

        public Optional<User> getAssignedUser() {
            return Optional.ofNullable(assignedUser);
        }

        public boolean isAvailable() {
            return assignedUser == null;
        }

        public boolean isReserved() {
            return assignedUser != null;
        }

        public User getAssignedUserOrNull() {
            return assignedUser;
        }

        public void assign(User user) {
            this.assignedUser = user;
        }

        public void release() {
            this.assignedUser = null;
        }

        public Queue<User> getWaitingQueue() {
            return waitingQueue;
        }
    }