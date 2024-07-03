//autor Otlia Marques
package analisadorlexico;

import static analisadorlexico.Analex.analisadorLexico;
import static analisadorlexico.Token.imprimirTabeladeTokens;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MainLexico {
    public static void main(String[] args) throws FileNotFoundException {
        ArrayList <Token> arraylist;
        arraylist = analisadorLexico();
        imprimirTabeladeTokens(arraylist);
    }
}