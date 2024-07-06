//autor Otlia Marques
package analisadorsintatico;

import static analisadorlexico.Analex.analisadorLexico;
import static analisadorsintatico.Anasin.analisadorSintatico;
import static analisadorsintatico.Sintaxe.imprimirTabelaSintaxe;
import java.io.FileNotFoundException;
import analisadorlexico.Token;
import java.util.ArrayList;
import javax.script.ScriptException;

public class MainSemantico {
  
    public static void main(String[] args) throws FileNotFoundException, ScriptException {
        ArrayList <Token> arraylist = new ArrayList<Token>();
        arraylist = analisadorLexico();
        //imprimirTabelaTokens(arraylist);
        ArrayList <Sintaxe> sintaxe = analisadorSintatico(arraylist);
        //imprimirTabelaSintaxe(sintaxe);
    }
}