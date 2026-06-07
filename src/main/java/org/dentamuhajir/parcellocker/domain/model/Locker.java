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

        public void addToQueue(User user) {
            if (waitingQueue.contains(user)) {
                throw new IllegalArgumentException(
                        "User is already in queue."
                );
            }
            waitingQueue.offer(user);
        }

        public boolean isUserQueued(User user) {
            return waitingQueue.contains(user);
        }

        public int getQueuePosition(User user) {
            int position = 1;
            for (User queuedUser : waitingQueue) {
                if (queuedUser.equals(user)) {
                    return position;
                }
                position++;
            }
            return -1;
        }

        public User pollNextQueuedUser() {
            return waitingQueue.poll();
        }
    }