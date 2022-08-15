package diversanto.gdmanager;

public class NoSuchLevelException extends Exception {
    public NoSuchLevelException( ) {
        super();
    }

    public NoSuchLevelException(String errorMessage) {
        super(errorMessage);
    }
}
