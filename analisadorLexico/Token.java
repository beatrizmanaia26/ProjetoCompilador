package analisadorLexico;

public class Token{
    String tipo;
    String lexema;

    public Token(String tipo, String lexema){
        this.tipo = tipo;
        this.lexema = lexema;
    }

    @Override
    public String toString(){
        return "<"+tipo+","+lexema+">";
    }
    //especifico token com exp regular -> transformo exp regular em codigo com afd
}

