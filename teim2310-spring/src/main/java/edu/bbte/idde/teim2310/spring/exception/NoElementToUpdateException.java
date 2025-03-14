package edu.bbte.idde.teim2310.spring.exception;

public class NoElementToUpdateException extends Exception {
    public NoElementToUpdateException() {
        super("The given Event wasn't found by id");
    }

    public NoElementToUpdateException(Throwable throwable) {
        super(throwable);
    }
}
