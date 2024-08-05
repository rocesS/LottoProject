package pl.domain.numberreceiver;

import java.time.LocalDateTime;
import java.util.Collection;

interface TicketRepository {

    Collection<Ticket> findAllTicketsByDrawDate(LocalDateTime drawTime);

    Ticket findByHash(String hash);

    Ticket save(Ticket savedTickets);

}
