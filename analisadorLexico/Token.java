package analisadorLexico;

public class Token{
    public String tipo;
    public String lexema;

    public Token(String tipo, String lexema){
        this.tipo = tipo;
        this.lexema = lexema;
    }

    @Override
    public String toString(){
        return "<"+tipo+","+"'" + lexema +"'"+">";
    }
    //especifico token com exp regular -> transformo exp regular em codigo com afd
}

