package analisadorlexico;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Analex {
    
    //Vector com todas as Palavras Reservadas de C#
    static final String[] Token_Palavras_Reservadas = {"abstract","as","base",
        "bool","break","byte","case","catch","char","checked","class","const","continue",
        "decimal","default","delegate","do","double","else","enum","event","explicit","extern",
        "false", "finally","fixed","float","for","foreach","goto","if","implicit","in","int","interface",
        "internal","is","lock","long","namespace","new","null","object","operator","out","override",
        "params","private","protected","public","readonly","ref","return","sbyte","sealed","short",
        "sizeof","stackallocstatic","string","struct","switch","this","throw","true","try","typeof",
        "uint","ulong","unchecked","unsafe","ushort","using","virtual","void","volatile","while"};
    
    static ArrayList <Token> tabelaTokens = new ArrayList();
    static Token token = new Token();
    
    public static void verificarLexema(String lexema, int linha) {
        int i;
        boolean palavra = false;
        for (i = 0; i < Token_Palavras_Reservadas.length; i++) {
            if (lexema.equals(Token_Palavras_Reservadas[i])) {
                palavra = true;
                token = new Token(lexema, "PALAVRA RESERVADA", linha);
                tabelaTokens.add(token);
            }
        }
        
        if (!palavra) {
            token = new Token(lexema, "IDENTIFICADOR", linha);
            tabelaTokens.add(token);
        }
    }

    public static ArrayList analisadorLexico() throws FileNotFoundException {
        int i, estado = 0, linha = 1;
        String lexema = "";
        String clickers="'oi";
        FileReader ficheiro = new FileReader("otilia.txt");
        Scanner leitor = new Scanner(ficheiro);
        while (leitor.hasNextLine()) {
            String frase = leitor.nextLine();
            frase = frase.concat(" ");
            for (i = 0; i < frase.length(); i++){
                switch (estado) {
                    case 0:
                        if((frase.charAt(i) == ' ') || (frase.charAt(i) == '\t') || (frase.charAt(i) == '\n') ){
                            estado = 0;
                        } else if ((frase.charAt(i) >= 65 && frase.charAt(i) <= 90) || (frase.charAt(i) >= 97 && frase.charAt(i) <= 122) || (frase.charAt(i) == '_') || (frase.charAt(i) == 95)) {
                            //Esse else é para verificar se é string
                            if(i == frase.length()-1){
                                lexema += frase.charAt(i);
                                token = new Token(lexema, "IDENTIFICADOR", linha);
                                tabelaTokens.add(token);
                            }else{
                                lexema += frase.charAt(i);
                                estado = 1;
                            }
                        } else if (frase.charAt(i) >= 48 && frase.charAt(i) <= 57) {
                            //Esse else é para verificar se é número int
                            if(i == frase.length()-1){
                                lexema += frase.charAt(i);
                                token = new Token(lexema, "NÚMERO INTEIRO", linha);
                                tabelaTokens.add(token);
                            }else{
                                lexema += frase.charAt(i);
                                estado = 3;
                            }
                        } else if (frase.charAt(i) == '/') {
                            if(i == frase.length()-1){
                                lexema += frase.charAt(i);
                                token = new Token(lexema, "BARRA", linha);
                                tabelaTokens.add(token);
                            }else{
                                lexema += frase.charAt(i);
                                estado = 4;
                            }
                        } else if (frase.charAt(i) == '+') {
                            if(i == frase.length()-1){
                                lexema += frase.charAt(i);
                                token = new Token(lexema, "MAIS", linha);
                                tabelaTokens.add(token);
                            }else{
                                lexema += frase.charAt(i);
                                estado = 14;
                            }
                        } else if (frase.charAt(i) == '-') {
                            lexema += frase.charAt(i);
                            estado = 18;
                        } else if (frase.charAt(i) == '*') {
                            if(i == frase.length()-1){
                                lexema += frase.charAt(i);
                                token = new Token(lexema, "ASTERÍSTICO", linha);
                                tabelaTokens.add(token);
                            }else{
                                lexema += frase.charAt(i);
                                estado = 23;
                            }
                        } else if (frase.charAt(i) == '>') {
                            if(i == frase.length()-2){
                                lexema += frase.charAt(i);
                                token = new Token(lexema, "MAIOR", linha);
                                tabelaTokens.add(token);
                            }else{
                                lexema += frase.charAt(i);
                                estado = 26;
                            }
                            
                        } else if (frase.charAt(i) == '<') {
                            if(i == frase.length()-1){
                                lexema += frase.charAt(i);
                                token = new Token(lexema, "MENOR", linha);
                                tabelaTokens.add(token);
                            }else{
                                lexema += frase.charAt(i);
                                estado = 32;
                            }
                        } else if (frase.charAt(i) == '=') {
                            if(i == frase.length()-1){
                                lexema += frase.charAt(i);
                                token = new Token(lexema, "ATRIBUIÇÃO", linha);
                                tabelaTokens.add(token);
                            }else{
                                lexema += frase.charAt(i);
                                estado = 36;
                            }
                        } else if (frase.charAt(i) == '!') {
                            if(i == frase.length()-1){
                                lexema += frase.charAt(i);
                                token = new Token(lexema, "EXCLAMAÇÃO", linha);
                                tabelaTokens.add(token);
                            }else{
                                lexema += frase.charAt(i);
                                estado = 39;
                            }
                        } else if (frase.charAt(i) == '&') {
                            if(i == frase.length()-1){
                                lexema += frase.charAt(i);
                                token = new Token(lexema, "BIT A BIT AND", linha);
                                tabelaTokens.add(token);
                            }else{
                                lexema += frase.charAt(i);
                                estado = 45;
                            }
                        } else if (frase.charAt(i) == '^') {
                            if(i == frase.length()-1){
                                lexema += frase.charAt(i);
                                token = new Token(lexema, "BIT A BIT OR EXCLUSIVO", linha);
                                tabelaTokens.add(token);
                            }else{
                                lexema += frase.charAt(i);
                                estado = 49;
                            }
                        } else if (frase.charAt(i) == '|') {
                            if(i == frase.length()-1){
                                lexema += frase.charAt(i);
                                token = new Token(lexema, "BIT A BIT OR INCLUSIVO", linha);
                                tabelaTokens.add(token);
                            }else{
                                lexema += frase.charAt(i);
                                estado = 52;
                            }
                        } else if (frase.charAt(i) == '~') {
                            lexema += frase.charAt(i);
                            estado = 56;
                            i--;
                        } else if (frase.charAt(i) == '%') {
                            if(i == frase.length()-1){
                                lexema += frase.charAt(i);
                                token = new Token(lexema, "RESTO DA DIVISÃO", linha);
                                tabelaTokens.add(token);
                            }else{
                                lexema += frase.charAt(i);
                                estado = 57;
                            }
                        } else if (frase.charAt(i) == '(') {
                            lexema += frase.charAt(i);
                            estado = 60;
                            i--;
                        } else if (frase.charAt(i) == ')') {
                            lexema += frase.charAt(i);
                            estado = 61;
                            i--;
                        } else if (frase.charAt(i) == '{') {
                            lexema += frase.charAt(i);
                            estado = 62;
                            i--;
                        } else if (frase.charAt(i) == '}') {
                            lexema += frase.charAt(i);
                            estado = 63;
                            i--;
                        } else if (frase.charAt(i) == ';') {
                            lexema += frase.charAt(i);
                            estado = 64;
                            i--;
                        } else if (frase.charAt(i) == ',') {
                            lexema += frase.charAt(i);
                            estado = 65;
                            i--;
                        } else if (frase.charAt(i) == '?') {
                            lexema += frase.charAt(i);
                            estado = 66;
                            i--;
                        } else if (frase.charAt(i) == '.') {
                            lexema += frase.charAt(i);
                            estado = 67;
                            i--;
                        } else if (frase.charAt(i) == '"') {
                            lexema += frase.charAt(i);
                            estado = 68;
                        } else if (frase.charAt(i) == '[') {
                            lexema += frase.charAt(i);
                            estado = 72;
                            i--;
                        } else if (frase.charAt(i) == ']') {
                            lexema += frase.charAt(i);
                            estado = 73;
                            i--;
                        } else if (frase.charAt(i) == clickers.charAt(0)){
                            lexema += frase.charAt(i);
                            estado = 74;
                        } else if (frase.charAt(i) == ':'){
                            lexema += frase.charAt(i);
                            estado = 78;
                            i--;
                        }
                        break;

                    case 1:
                        while((frase.charAt(i) >= 65 && frase.charAt(i) <= 90) || (frase.charAt(i) >= 97 && frase.charAt(i) <= 122) || (frase.charAt(i) >= 48 && frase.charAt(i) <= 57) || (frase.charAt(i) == 95) ){
                            lexema += frase.charAt(i);
                            if(i != frase.length()-1)
                                i++;
                            else
                                break;
                        }
                        i--;
                        estado = 2;
                        break;

                    case 2:
                        verificarLexema(lexema, linha);
                        lexema = "";
                        estado = 0;
                        i--;
                        break;

                    case 3:
                        while(frase.charAt(i) >= 48 && frase.charAt(i) <= 57) {
                            lexema += frase.charAt(i);
                            if(i != frase.length()-1)
                                i++;
                            else
                                break;
                        }
                        if (frase.charAt(i) == '.') {
                            lexema += frase.charAt(i);
                            estado = 5;
                        }else{
                            i--;
                            estado = 7;
                        }
                        
                        break;

                    case 4:
                        if (frase.charAt(i) == '/') {
                            lexema += frase.charAt(i);
                            estado = 8;
                        } else if (frase.charAt(i) == '*') {
                            lexema += frase.charAt(i);
                            estado = 9;
                        } else if (frase.charAt(i) == '=') {
                            if(i == frase.length()-1){
                                lexema += frase.charAt(i);
                                token = new Token(lexema, "DIVISÃO", linha);
                                tabelaTokens.add(token);
                            }else{
                                lexema += frase.charAt(i);
                                estado = 11;
                            }
                        } else {
                            i--;
                            estado = 22;
                        }
                        break;

                    case 5:
                        if(frase.charAt(i) >= 48 && frase.charAt(i) <= 57) {
                            lexema += frase.charAt(i);
                            estado = 6;
                        }else{
                            token = new Token(lexema,"NÚMERO REAL",linha);
                            tabelaTokens.add(token);
                            lexema = "";
                            estado = 0;
                            i--;
                        }
                        break;

                    case 6:
                        while(frase.charAt(i) >= 48 && frase.charAt(i) <= 57) {
                            lexema += frase.charAt(i);
                            if(i != frase.length()-1)
                                i++;
                            else
                                break;
                        }
                            token = new Token(lexema,"NÚMERO REAL",linha);
                            tabelaTokens.add(token);
                            lexema = "";
                            estado = 0;
                            i--;
                        break;

                    case 7:
                        token = new Token(lexema,"NÚMERO INTEIRO",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        i--;
                        break;

                    case 8:
                        while (frase.charAt(i) != '\n') {
                            lexema += frase.charAt(i);
                            estado = 8;
                            if(i != frase.length()-1)
                                i++;
                            else
                                break;
                        }
                            estado = 10;
                        break;

                    case 9:
                        while(frase.charAt(i) != '*'){
                            lexema += frase.charAt(i);
                            estado = 9;
                            if(i != frase.length()-1)
                                i++;
                            else
                                break;
                        }
                        if (frase.charAt(i) == '*') {
                            lexema += frase.charAt(i);
                            estado = 12;
                        }
                        break;

                    case 10:
                        token = new Token(lexema,"COMENTÁRIO SIMPLES",linha);
                        tabelaTokens.add(token);
                        i--;
                        lexema = "";
                        estado = 0;
                        break;

                    case 11:
                        token = new Token(lexema,"DIVISÃO",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        i--;
                        break;

                    case 12:
                        if (frase.charAt(i) == '/') {
                            lexema += frase.charAt(i);
                            estado = 13;
                            i--;
                        } else if (frase.charAt(i) != '/') {
                            lexema += frase.charAt(i);
                            estado = 9;
                        }
                        break;
                    case 13:
                        token = new Token(lexema,"COMENTÁRIO",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        //i--;
                        break;

                    case 14:
                        if (frase.charAt(i) == '+') {
                            lexema += frase.charAt(i);
                            estado = 15;
                            i--;
                        } else if (frase.charAt(i) == '=') {
                            lexema += frase.charAt(i);
                            estado = 16;
                            i--;
                        } else {
                            i--;
                            estado = 17;
                        }
                        break;

                    case 15:
                        token = new Token(lexema,"INCREMENTO",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        break;

                    case 16:
                        token = new Token(lexema,"OPERADOR DE ADIÇÃO",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        break;
                    case 17:
                        token = new Token(lexema,"MAIS",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        i--;
                        break;

                    case 18:
                        if (frase.charAt(i) == '-') {
                            lexema += frase.charAt(i);
                            estado = 19;
                        } else if (frase.charAt(i) == '=') {
                            lexema += frase.charAt(i);
                            estado = 21;
                        } else {
                            i--;
                            estado = 20;
                        }
                        break;

                    case 19:
                        token = new Token(lexema,"DECREMENTO",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        i--;
                        break;

                    case 20:
                        token = new Token(lexema,"MENOS",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        i--;
                        break;

                    case 21:                        token = new Token(lexema,"OPERADOR DE SUBTRAÇÃO",linha);

                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        i--;
                        break;

                    case 22:
                        token = new Token(lexema,"BARRA",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        i--;
                        break;

                    case 23:
                        if (frase.charAt(i) == '=') {
                            lexema += frase.charAt(i);
                            estado = 24;
                        } else {
                            i--;
                            estado = 25;
                        }
                        break;

                    case 24:
                        token = new Token(lexema,"OPERADOR DE MULTIPLICAÇÃO",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        i--;
                        break;
                    case 25:
                        token = new Token(lexema,"ASTERÍSTICO",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        i--;
                        break;

                    case 26:
                        if (frase.charAt(i) == '>') {
                            lexema += frase.charAt(i);
                            estado = 27;
                          //i--;
                        } else if (frase.charAt(i) == '=') {
                            lexema += frase.charAt(i);
                            estado = 29;
                        } else {
                            estado = 30;
                        }

                    case 27:
                        if (frase.charAt(i) == '>') {
                            lexema += frase.charAt(i);
                            estado = 28;
                        } else if (frase.charAt(i) == '=') {
                            lexema += frase.charAt(i);
                            estado = 29;
                            //i--;
                        } else {
                            i--;
                            estado = 31;
                        }
                        break;

                    case 28:
                        if (frase.charAt(i) == '=') {
                            lexema += frase.charAt(i);
                            estado = 42;
                        } else {
                            i--;
                            estado = 43;
                        }
                        break;
                    case 29:
                        token = new Token(">=","MAIOR OU IGUAL",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        i--;
                        break;

                    case 30:
                        token = new Token(">","MAIOR",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        i--;
                        break;

                    case 31:
                        token = new Token(lexema,"MUDANÇA A DIREITA",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        i--;
                        break;

                    case 32:
                        if (frase.charAt(i) == '=') {
                            lexema += frase.charAt(i);
                            estado = 33;
                        } else if (frase.charAt(i) == '<') {
                            lexema += frase.charAt(i);
                            estado = 35;
                        } else {
                            i--;
                            estado = 34;
                        }
                        break;

                    case 33:
                        token = new Token(lexema,"MENOR OU IGUAL",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        i--;
                        break;

                    case 34:
                        token = new Token(lexema,"MENOR",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        i--;
                        break;

                    case 35:
                        token = new Token(lexema,"MUDANÇA A ESQUERDA",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        i--;
                        break;

                    case 36:
                        if (frase.charAt(i) == '=') {
                            lexema += frase.charAt(i);
                            estado = 37;
                        } else {
                            i--;
                            estado = 38;
                        }
                        break;

                    case 37:
                        token = new Token(lexema,"COMPARAÇÃO DE IGUALDADE",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        i--;
                        break;

                    case 38:
                        token = new Token(lexema,"ATRIBUIÇÃO",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        i--;
                        break;

                    case 39:
                        if (frase.charAt(i) == '=') {
                            lexema += frase.charAt(i);
                            estado = 40;
                        } else {
                            i--;
                            estado = 41;
                        }
                        break;

                    case 40:
                        token = new Token(lexema,"DIFERENTE",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        i--;
                        break;

                    case 41:
                        token = new Token(lexema,"EXCLAMAÇÃO",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        i--;
                        break;

                    case 42:
                        token = new Token(">>>=","MUDANÇA A DIREITA",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        i--;
                        estado = 0;
                        break;

                    case 43:
                        token = new Token(">>>","MUDANÇA A DIREITA",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        i--;
                        break;

                    case 44:
                        token = new Token(">>=","MUDANÇA A DIREITA",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        i--;
                        break;

                    case 45:
                        if (frase.charAt(i) == '&') {
                            lexema += frase.charAt(i);
                            estado = 46;
                        } else if (frase.charAt(i) == '=') {
                            lexema += frase.charAt(i);
                            estado = 47;
                        } else {
                            i--;
                            estado = 48;
                        }
                        break;
                    case 46:
                        token = new Token(lexema,"AND LÓGICO",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        i--;
                        break;

                    case 47:
                        token = new Token(lexema,"E COMERCIA IGUAL",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        i--;
                        break;

                    case 48:
                        token = new Token(lexema,"BIT A BIT AND",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        i--;
                        break;

                    case 49:
                        if (frase.charAt(i) == '=') {
                            lexema += frase.charAt(i);
                            estado = 50;
                        } else {
                            i--;
                            estado = 51;
                        }
                        break;

                    case 50:
                        token = new Token(lexema,"BIT A BIT OR",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        i--;
                        break;

                    case 51:
                        token = new Token(lexema,"BIT A BIT OR EXCLUSIVO",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        i--;
                        break;

                    case 52:
                        if (frase.charAt(i) == '|') {
                            lexema += frase.charAt(i);
                            estado = 53;
                            i--;
                        } else if (frase.charAt(i) == '=') {
                            lexema += frase.charAt(i);
                            estado = 54;
                            i--;
                        } else {
                            i--;
                            estado = 55;
                        }
                        break;

                    case 53:
                        token = new Token(lexema,"OR LÓGICO",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        break;

                    case 54:
                        token = new Token(lexema,"OPERADOR OR LÓGICO",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        //i--;
                        break;

                    case 55:
                        token = new Token(lexema,"BIT A BIT OR INCLUSIVO",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        //i--;
                        break;

                    case 56:
                        token = new Token(lexema,"INVERTER O SINAL E SUBTRAIR",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        //i--;
                        break;

                    case 57:
                        if (frase.charAt(i) == '=') {
                            lexema += frase.charAt(i);
                            estado = 58;
                        } else {
                            i--;
                            estado = 59;
                        }
                        break;

                    case 58:
                        token = new Token(lexema,"PERCENTAGEM IGUAL",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        i--;
                        break;

                    case 59:
                        token = new Token(lexema,"RESTO DA DIVISÃO",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        i--;
                        break;

                    case 60:
                        token = new Token(lexema,"ABRE PARÊNTESES",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        break;

                    case 61:
                        token = new Token(lexema,"FECHA PARÊNTESES",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        break;

                    case 62:
                        token = new Token(lexema,"ABRE CHAVETA",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        break;

                    case 63:
                        token = new Token(lexema,"FECHA CHAVETA",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        break;

                    case 64:
                        token = new Token(lexema,"PONTO E VÍRGULA",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        break;

                    case 65:
                        token = new Token(lexema,"VÍRGULA",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        break;

                    case 66:
                        token = new Token(lexema,"PONTO DE INTERROGAÇÃO / TERNÁRIO",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        break;

                    case 67:
                        token = new Token(lexema,"PONTO",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        break;

                    case 68:
                        if (frase.charAt(i) != '"') {
                            lexema += frase.charAt(i);
                            estado = 69;
                        } else if (frase.charAt(i) == '"') {
                            estado = 71;
                        }
                        break;

                    case 69:
                        while (frase.charAt(i) != '"') {
                            lexema += frase.charAt(i);
                            if(i != frase.length()-1)
                                i++;
                            else
                                break;
                        }
                        if (frase.charAt(i) == '"') {
                            lexema += frase.charAt(i);
                            estado = 70;
                        }
                        break;

                    case 70:
                        token = new Token(lexema,"STRING",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        i--;
                        break;

                    case 71:
                        token = new Token(lexema,"ASPAS",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        i--;
                        break;

                    case 72:
                        token = new Token(lexema,"ABRE PARÊNTESES RETOS",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        break;

                    case 73:
                        token = new Token(lexema,"FECHA PARÊNTESES RETOS",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        break;
                        
                    case 74:
                        if (frase.charAt(i) != clickers.charAt(0)) {
                            lexema += frase.charAt(i);
                            estado = 75;
                        } else if (frase.charAt(i) == clickers.charAt(0)) {
                            lexema += frase.charAt(i);
                            estado = 77;
                        }
                    break;
                    
                    case 75:
                        if(frase.charAt(i) == clickers.charAt(0)){
                            if(i == frase.length()-1){
                                lexema += frase.charAt(i);
                                token = new Token(lexema, "CHAR", linha);
                                tabelaTokens.add(token);
                            }else{
                                lexema += frase.charAt(i);
                                estado = 76;
                            }
                        }else{
                            estado = 0;
                        }
                    break;
                    
                    case 76:
                        token = new Token(lexema,"CHAR",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        i--;
                    break;   
                        
                    case 77:
                        token = new Token(lexema,"Clickers",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                        i--;
                    break;    
                    
                    case 78:
                        token = new Token(lexema,"DOIS PONTOS",linha);
                        tabelaTokens.add(token);
                        lexema = "";
                        estado = 0;
                    break;
                }
            }
            linha++;
        }
        return tabelaTokens;
    }
}