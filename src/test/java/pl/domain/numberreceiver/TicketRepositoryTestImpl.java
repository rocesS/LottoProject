package pl.domain.numberreceiver;

import pl.domain.numberreceiver.dto.TicketDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

class TicketRepositoryTestImpl implements TicketRepository {

    private final Map<String, Ticket> tickets = new ConcurrentHashMap<>();

    @Override
    public Collection<Ticket> findAllTicketsByDrawDate(LocalDateTime drawTime) {
        return tickets.values()
                .stream()
                .filter(ticket -> ticket.drawDate().equals(drawTime))
                .collect(Collectors.toList());
    }

    @Override
    public Ticket findByHash(String hash) {
        return tickets.get(hash);
    }

    @Override
    public Ticket save(Ticket savedTickets) {
        tickets.put(savedTickets.hash(), savedTickets);
        return savedTickets;
    }
}