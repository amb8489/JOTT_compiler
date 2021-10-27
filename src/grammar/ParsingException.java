package grammar;

public class ParsingException extends Throwable {
    public ParsingException(String toString) {
        System.err.println(toString);
    }
}
