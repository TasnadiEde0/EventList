package edu.bbte.idde.teim2310.spring.exception;

public class NoElementToDeleteException extends Exception {
    public NoElementToDeleteException() {
        super("The given Event wasn't found by id");
    }

    public NoElementToDeleteException(Throwable throwable) {
        super(throwable);
    }
}
