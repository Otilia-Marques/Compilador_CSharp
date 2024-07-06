//autor Otlia Marques
package analisadorsintatico;

import static analisadorlexico.Analex.analisadorLexico;
import analisadorlexico.Token;
import static analisadorsintatico.Anasin.analisadorSintatico;
import static analisadorsintatico.Sintaxe.imprimirTabelaSintaxe;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import javax.script.ScriptException;

public class MainSintatico {

    public static void main(String[] args) throws FileNotFoundException, ScriptException {
        ArrayList <Token> arraylist = new ArrayList<Token>();
        arraylist = analisadorLexico();
        //imprimirTabelaTokens(arraylist);
        ArrayList <Sintaxe> sintaxe = analisadorSintatico(arraylist);
        imprimirTabelaSintaxe(sintaxe);
    }
}