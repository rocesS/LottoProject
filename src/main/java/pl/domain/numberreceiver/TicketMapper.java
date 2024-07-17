package pl.domain.numberreceiver;

import pl.domain.numberreceiver.dto.TicketDto;

class TicketMapper {
    public static TicketDto mapFromTicket(Ticket ticket) {
        return TicketDto.builder()
                .numbersFromUser(ticket.numbersFromUser())
                .ticketId(ticket.ticketId())
                .drawDate(ticket.drawDate())
                .build();
    }

}
