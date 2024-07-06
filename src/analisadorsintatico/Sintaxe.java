//autor Otlia Marques
package analisadorsintatico;

import java.util.ArrayList;

public class Sintaxe {
    public String lexema;
    public String token;
    public String tipo;
    public String valorDeAtribuicao;
    public String tipoAtribuicao;
    public int escopo;
    
    public Sintaxe(){
        
    }
    
    public Sintaxe(String token, String lexema , String valorDeAtribuicao, String tipo, String tipoAtribuicao, int escopo){
        this.lexema = lexema;
        this.token = token;
        this.valorDeAtribuicao = valorDeAtribuicao;
        this.tipo = tipo;
        this.tipoAtribuicao = tipoAtribuicao;
        this.escopo = escopo;
    }
    
    public static void imprimirTabelaSintaxe(ArrayList <Sintaxe> tabelaSintaxe ){
        System.out.println("\n\n_____________________________________________________________________________________________________");
        System.out.format("%-25s %15s %15s %15s %15s %8s\n", "TOKEN:", "LEXEMA: ", "TIPO: ", "V.ATRIBUICAO:", "T.ATRIBUIÇÃO:", " ESCOPO:");
        System.out.println("_____________________________________________________________________________________________________");
        for(Sintaxe sintaxes: tabelaSintaxe){
            System.out.format("%-25s %15s %15s %15s %16s %9s\n",sintaxes.token,sintaxes.lexema,sintaxes.tipo,sintaxes.valorDeAtribuicao,sintaxes.tipoAtribuicao,sintaxes.escopo);
            System.out.println("_____________________________________________________________________________________________________");
        }
    }
}