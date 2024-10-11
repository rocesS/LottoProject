package pl.domain.resultannouncer;

public enum MessageResponse {

    HASH_DOES_NOTE_EXIST_MESSAGE("Given ticket does not exist"),
    WAIT_MESSAGE("Result are being calculated, please come back later"),
    WIN_MESSAGE("Congratulations, you won!"),
    LOSE_MESSAGE("No luck, try again!"),
    ALREADY_CHECKED("You have already checked your ticket, please come back later");

    final String info;

    MessageResponse(String info) {
        this.info = info;
    }
}
