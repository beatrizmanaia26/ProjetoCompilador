public class Token {

  public String tipo;
  public String lexema;

  public Token(String tipo, String lexema) {
    this.lexema = lexema;
    this.tipo = tipo;
  }

  @Override
  public String toString() {
    return "<" + this.tipo + ", " + this.lexema + ">";
  }
}
