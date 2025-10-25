package analisadorLexico;

public class LexicalException extends RuntimeException {
    private final int line;
    private final int column;
    private final String sourceLine;

    public LexicalException(String message, int line, int column, String sourceLine) {
        super(message);
        this.line = line;
        this.column = column;
        this.sourceLine = sourceLine;
    }

    @Override
    public String getMessage() {
        StringBuilder msg = new StringBuilder();
        msg.append(String.format("Erro léxico na linha %d, coluna %d:%n\n", line, column));
        msg.append(sourceLine).append(System.lineSeparator());
        msg.append(" ".repeat(Math.max(0, column - 1))).append("^").append(System.lineSeparator());
        msg.append(super.getMessage());
        return msg.toString();
    }
}
