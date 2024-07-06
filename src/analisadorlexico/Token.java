//autor Otlia Marques
package analisadorlexico;

import java.util.ArrayList;

public class Token {
    public int linha;
    public String tipo;
    public String lexema;
    public static int i=0;
    
    public Token(){
        
    }
    
    public Token(String lexema, String tipo, int linha){
        this.tipo = tipo;
        this.lexema = lexema;
        this.linha = linha;
    }

    public static void imprimirTabeladeTokens(ArrayList <Token> tabelaToken ){
        System.out.println("\n\n_____________________________________________________________________________________________________");
        System.out.println("                                 TABELA ANALISADOR LÉXICO");
        System.out.println("_____________________________________________________________________________________________________");
        System.out.format("%-7s %30s %30s %30s\n", "ID:", "TOKEN:", "LEXEMA: ", "LINHA:");
        System.out.println("_____________________________________________________________________________________________________");
        for(Token token: tabelaToken){
            i++;
            System.out.format("%-7s %30s %30s %30s\n",i, token.tipo,token.lexema,token.linha);
        System.out.println("_____________________________________________________________________________________________________");
        }
    }
}