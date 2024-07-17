package pl.domain.numberreceiver;

import java.util.Set;

//encja bazy danych
public record Ticket(String ticketId, java.time.LocalDateTime drawDate, Set<Integer> numbersFromUser) {
}
