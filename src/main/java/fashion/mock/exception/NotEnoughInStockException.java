package fashion.mock.exception;

public class NotEnoughInStockException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NotEnoughInStockException(String message) {
        super(message);
    }
}
