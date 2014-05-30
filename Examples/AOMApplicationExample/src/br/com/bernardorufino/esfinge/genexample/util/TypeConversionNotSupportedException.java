package br.com.bernardorufino.esfinge.genexample.util;

public class TypeConversionNotSupportedException extends UnsupportedOperationException {

    public TypeConversionNotSupportedException() {
        super();
    }

    public TypeConversionNotSupportedException(String message) {
        super(message);
    }

    public TypeConversionNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }

    public TypeConversionNotSupportedException(Throwable cause) {
        super(cause);
    }
}
