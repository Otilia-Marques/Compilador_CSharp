//autor Otlia Marques
package analisadorsintatico;

import java.util.ArrayList;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;    
import analisadorlexico.Analex.*;
import analisadorlexico.Token;

public class Anasin {

    int escopo;
    public static int i = 0, indiceTabela = 0, lexemaSintatico, indiceEscopo;
    public static int numeroErros = 0;
    public static String tipoDados = "", op = "";
    public static boolean loop = false, Classe = false, Final = false, Static = false; 
    public static boolean Public = false, Private = false, If = false, doWhile = false;
    
    public static ArrayList<Sintaxe> tabelaSintatica = new ArrayList();
    public static ArrayList<Sintaxe> tabelaAuxiliar = new ArrayList();
    public static ArrayList<String> caminhos = new ArrayList();
    public static Sintaxe sintax = new Sintaxe();
    public static int controlaEscopo = 0;

    public static ArrayList<Sintaxe> analisadorSintatico(ArrayList<Token> arraylist) {
        inicio(arraylist);
        Fim(arraylist);
        return tabelaSintatica;
    }

    public static void redirecionaEscopo(ArrayList<Token> arraylist) {
        if (caminhos.get(controlaEscopo).equals("funcao")) {
            corpoFuncao(arraylist);
        } else if (caminhos.get(controlaEscopo).equals("for")) {
            corpoFor(arraylist);
        } else if (caminhos.get(controlaEscopo).equals("while")) {
            corpoWhile(arraylist);
        } else if (caminhos.get(controlaEscopo).equals("switch")) {
            corpoSwitch(arraylist);
        } else if (caminhos.get(controlaEscopo).equals("if")) {
            corpoIf(arraylist);
        } else if (caminhos.get(controlaEscopo).equals("do")) {
            corpoDoWhile(arraylist);
        }
    }

    // Regular Colors
    public static final String RESET = "\033[0m";  // Text Reset
    public static final String RED = "\033[0;31m";     // RED
    public static final String GREEN = "\033[0;32m";   // GREEN

    public static void inicio(ArrayList<Token> arraylist) {
        caminhos.add("funcao");
        Program(arraylist);
    }

    public static void Fim(ArrayList<Token> arraylist) {
        //verificacoesFinais(arraylist);
        System.out.println(RED + "NÚMERO DE ERROS: " + numeroErros + RESET);
    }
    
    public static boolean abreParenteses(ArrayList<Token> arraylist) {
        if (compararPalavra(arraylist, "(")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean fechaParenteses(ArrayList<Token> arraylist) {
        if (compararPalavra(arraylist, ")")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean abreParentesesRetos(ArrayList<Token> arraylist) {
        if (compararPalavra(arraylist, "[")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean fechaParentesesRetos(ArrayList<Token> arraylist) {
        if (compararPalavra(arraylist, "]")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean abreChaveta(ArrayList<Token> arraylist) {
        if (compararPalavra(arraylist, "{")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean fechaChaveta(ArrayList<Token> arraylist) {
        if (compararPalavra(arraylist, "}")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean limites(ArrayList<Token> arraylist) {
        if (i < arraylist.size()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean ID(ArrayList<Token> arraylist) {
        if (arraylist.get(i).tipo.equals("IDENTIFICADOR")) {
            i++;
            return true;
        } else {
            return false;
        }
    }

    public static boolean ponto(ArrayList<Token> arraylist) {
        if (arraylist.get(i).lexema.equals(".")) {
            i++;
            return true;
        } else {
            return false;
        }
    }

    public static boolean pontoEvirgula(ArrayList<Token> arraylist) {
        if (arraylist.get(i).lexema.equals(";")) {
            i++;
            return true;
        } else {
            return false;
        }
    }

    public static boolean compararPalavra(ArrayList<Token> arraylist, String palavra) {
        if (arraylist.get(i).lexema.equals(palavra)) {
            i++;
            return true;
        } else {
            return false;
        }
    }

    public static boolean compararTipo(ArrayList<Token> arraylist, String palavra) {
        if (arraylist.get(i).tipo.equals(palavra)) {
            i++;
            return true;
        } else {
            return false;
        }
    }

    
    
    public static void Program(ArrayList<Token> arraylist) {
        if (limites(arraylist)) {
            if (compararTipo(arraylist, "COMENTÁRIO") || compararTipo(arraylist, "COMENTÁRIO SIMPLES")) {
                Program(arraylist);
            } else if (arraylist.get(i).lexema.equals("using")) {
                i++;
                if (limites(arraylist)) {
                    if (ID(arraylist)) {
                        if (limites(arraylist)) {
                            if (ponto(arraylist)) {
                                loop = true;
                                Program(arraylist);
                            } else if (pontoEvirgula(arraylist)) {
                                //System.out.println(GREEN + "PACKAGE BEM DECLARADA");
                                loop = false;
                                if (limites(arraylist)) {
                                    if (arraylist.get(i).lexema.equals("namespace")) {
                                        Using(arraylist);
                                    }
                                } else {
                                    System.out.println(RED + "ESPERAVA-SE UM NAMESPACE, DECLARAÇÃO DE UMA CLASSE OU INTERFACE APÓS A EXPRESSÃO: " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                    numeroErros++;
                                }
                            }
                        } else {
                            System.out.println(RED + "ESPERAVA-SE UMA PONTUAÇÃO APÓS A EXPRESSÃO: " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                            numeroErros++;
                        }
                    }
                } else {
                    System.out.println(RED + "ESPERAVA-SE UM ID APÓS A EXPRESSÃO: " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                    numeroErros++;
                }
            } else if (loop && ID(arraylist)) {
                if (limites(arraylist)) {
                    if (ponto(arraylist)) {
                        Program(arraylist);
                    } else if (pontoEvirgula(arraylist)) {
                        ////System.out.println(GREEN + "PACKAGE BEM DECLARADA" + RESET);
                        loop = false;
                        if (limites(arraylist)) {
                            if (arraylist.get(i).lexema.equals("namespace")) {
                                Using(arraylist);
                            } else if (arraylist.get(i).lexema.equals("public")) {
                                i++;
                                if (limites(arraylist)) {
                                    if (arraylist.get(i).lexema.equals("abstract")) {
                                        i++;
                                        if (limites(arraylist)) {
                                            if (compararPalavra(arraylist, "class")) {
                                                i--;
                                                Class(arraylist);
                                            } else {
                                                System.out.println(RED + "ESPERAVA-SE A EXPRESSÃO class APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                                numeroErros++;
                                                i++;
                                            }
                                        } else {
                                            System.out.println(RED + "ESPERAVA-SE A EXPRESSÃO class APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                            numeroErros++;
                                            i++;
                                        }
                                    } else if (arraylist.get(i).lexema.equals("class")) {
                                        Class(arraylist);
                                    } else {
                                        System.out.println(RED + "ESPERAVA-SE DECLARAÇÃO DE UMA CLASSE OU INTERFACE APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                        numeroErros++;
                                        i++;
                                    }
                                } else {
                                    System.out.println(RED + "ESPERAVA-SE DECLARAÇÃO DE UMA CLASSE OU INTERFACE APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                    numeroErros++;
                                    i++;
                                }
                            } else if (arraylist.get(i).lexema.equals("class")) {
                                Class(arraylist);
                            } else if (arraylist.get(i).lexema.equals("abstract")) {
                                i++;
                                if (limites(arraylist)) {
                                    if (compararPalavra(arraylist, "class")) {
                                        i--;
                                        Class(arraylist);
                                    } else {
                                        System.out.println(RED + "ESPERAVA-SE A EXPRESSÃO class APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                        numeroErros++;
                                        i++;
                                    }
                                } else {
                                    System.out.println(RED + "ESPERAVA-SE A EXPRESSÃO class APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                    numeroErros++;
                                    i++;
                                }
                            } else {
                                System.out.println(RED + "ESPERAVA-SE IMPORTAÇÃO, DECLARAÇÃO DE UMA CLASSE OU INTERFACE APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                numeroErros++;
                                i++;
                            }
                        } else {
                            System.out.println(RED + "ESPERAVA-SE IMPORTAÇÃO, DECLARAÇÃO DE UMA CLASSE OU INTERFACE APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                            numeroErros++;
                            i++;
                        }
                    }
                } else {
                    System.out.println(RED + "ESPERAVA-SE UMA PONTUAÇÃO APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                    numeroErros++;
                    i++;
                }
            }
        } else {
            System.out.println(RED + "DOCUMENTO ESTÁ VAZIO" + RESET);
            numeroErros++;
            i++;
        }
    }

    public static void Using(ArrayList<Token> arraylist) {
        if (limites(arraylist)) {
            if (arraylist.get(i).lexema.equals("using")) {
                i++;
                if (limites(arraylist)) {
                    if (ID(arraylist)) {
                        if (limites(arraylist)) {
                            if (ponto(arraylist)) {
                                if (limites(arraylist)) {
                                    if (compararPalavra(arraylist, "*")) {
                                        if (limites(arraylist)) {
                                            if (compararPalavra(arraylist, ";")) {
                                                //System.out.println(GREEN+"IMPORT BEM DECLARADO"+RESET);
                                                nextUsing(arraylist);
                                            } else {
                                                System.out.println(RED + "ESPERAVA-SE ; APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                                numeroErros++;
                                                i++;
                                            }
                                        } else {
                                            System.out.println(RED + "ESPERAVA-SE ; APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                            numeroErros++;
                                        }
                                    } else if (ID(arraylist)) {
                                        i--;
                                        loop = true;
                                        Using(arraylist);
                                    } else {
                                        System.out.println(RED + "ESPERAVA-SE DECLARAÇÃO DE UMA CLASSE OU INTERFACE APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                        numeroErros++;
                                        i++;
                                    }
                                } else {
                                    System.out.println(RED + "ESPERAVA-SE DECLARAÇÃO DE UMA CLASSE OU INTERFACE APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                    numeroErros++;
                                }
                            } else if (pontoEvirgula(arraylist)) {
                                ////System.out.println(GREEN + "IMPORT BEM DECLARADO");
                                loop = false;
                                //aqui vamos testar algo
                                nextUsing(arraylist);
                            }
                        } else {
                            System.out.println(RED + "ESPERAVA-SE UMA PONTUAÇÃO APÓS A EXPRESSÃO: " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                            numeroErros++;
                        }
                    }
                } else {
                    System.out.println(RED + "ESPERAVA-SE UM ID APÓS A EXPRESSÃO: " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                    numeroErros++;
                }
            } else if (loop && ID(arraylist)) {
                if (limites(arraylist)) {
                    if (ponto(arraylist)) {
                        if (limites(arraylist)) {
                            if (compararPalavra(arraylist, "*")) {
                                if (limites(arraylist)) {
                                    if (compararPalavra(arraylist, ";")) {
                                        ////System.out.println(GREEN + "IMPORT BEM DECLARADO" + RESET);
                                        nextUsing(arraylist);
                                    } else {
                                        System.out.println(RED + "ESPERAVA-SE ; APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                        numeroErros++;
                                        i++;
                                    }
                                } else {
                                    System.out.println(RED + "ESPERAVA-SE ; APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                    numeroErros++;
                                }
                            } else if (ID(arraylist)) {
                                i--;
                                loop = true;
                                Using(arraylist);
                            } else {
                                System.out.println(RED + "ESPERAVA-SE DECLARAÇÃO DE UMA CLASSE OU INTERFACE APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                numeroErros++;
                                i++;
                            }
                        } else {
                            System.out.println(RED + "ESPERAVA-SE DECLARAÇÃO DE UMA CLASSE OU INTERFACE APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                            numeroErros++;
                        }
                    } else if (pontoEvirgula(arraylist)) {
                        ////System.out.println(GREEN + "IMPORT BEM DECLARADO");
                        loop = false;
                        nextUsing(arraylist);
                    }
                } else {
                    System.out.println(RED + "ESPERAVA-SE UMA PONTUAÇÃO APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                    numeroErros++;
                }
            }
        } else {
            System.out.println(RED + "ESPERAVA-SE ENCONTRAR UM IMPORT" + RESET);
            numeroErros++;
        }
    }

    public static void nextUsing(ArrayList<Token> arraylist) {
        if (limites(arraylist)) {
            if (arraylist.get(i).lexema.equals("using")) {
                Using(arraylist);
            } else if (arraylist.get(i).lexema.equals("public")) {
                i++;
                if (limites(arraylist)) {
                    if (arraylist.get(i).lexema.equals("abstract")) {
                        i++;
                        if (limites(arraylist)) {
                            if (compararPalavra(arraylist, "class")) {
                                i--;
                                Class(arraylist);
                            } else {
                                System.out.println(RED + "ESPERAVA-SE A EXPRESSÃO class APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                numeroErros++;
                                i++;
                            }
                        } else {
                            System.out.println(RED + "ESPERAVA-SE A EXPRESSÃO class APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                            numeroErros++;
                        }
                    } else if (arraylist.get(i).lexema.equals("class")) {
                        Class(arraylist);
                    } else {
                        System.out.println(RED + "ESPERAVA-SE DECLARAÇÃO DE UMA CLASSE OU INTERFACE APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                        numeroErros++;
                    }
                } else {
                    System.out.println(RED + "ESPERAVA-SE DECLARAÇÃO DE UMA CLASSE OU INTERFACE APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                    numeroErros++;
                }
            } else if (arraylist.get(i).lexema.equals("class")) {
                Class(arraylist);
            } else if (arraylist.get(i).lexema.equals("abstract")) {
                i++;
                if (limites(arraylist)) {
                    if (compararPalavra(arraylist, "class")) {
                        i--;
                        Class(arraylist);
                    } else {
                        System.out.println(RED + "ESPERAVA-SE A EXPRESSÃO class APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                        numeroErros++;
                        i++;
                    }
                } else {
                    System.out.println(RED + "ESPERAVA-SE A EXPRESSÃO class APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                    numeroErros++;
                }
            } else {
                System.out.println(RED + "ESPERAVA-SE IMPORTAÇÃO, DECLARAÇÃO DE UMA CLASSE OU INTERFACE APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                numeroErros++;
                i++;
            }
        } else {
            System.out.println(RED + "ESPERAVA-SE IMPORTAÇÃO, DECLARAÇÃO DE UMA CLASSE OU INTERFACE APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
            numeroErros++;
        }
    }

    public static void Class(ArrayList<Token> arraylist) {
        if (compararPalavra(arraylist, "class")) {
            if (limites(arraylist)) {
                if (ID(arraylist)) {
                    if (limites(arraylist)) {
                        if (compararPalavra(arraylist, "extends")) {
                            if (limites(arraylist)) {
                                if (ID(arraylist)) {
                                    if (limites(arraylist)) {
                                        if (abreChaveta(arraylist)) {
                                            if (limites(arraylist)) {
                                                corpoClass(arraylist);
                                            } else {
                                                System.out.println(RED + "ESPERAVA-SE POR } A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                                                numeroErros++;
                                                i++;
                                            }
                                        } else {
                                            System.out.println(RED + "ESPERAVA-SE POR ABRE CHAVETA APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                            numeroErros++;
                                            i++;
                                        }
                                    } else {
                                        System.out.println(RED + "ESPERAVA-SE POR ABRE CHAVETA APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                        numeroErros++;
                                        i++;
                                    }
                                }
                            } else {
                                System.out.println(RED + "ESPERAVA-SE POR UM IDENTIFICADOR APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                numeroErros++;
                                i++;
                            }
                        } else if (compararPalavra(arraylist, "implements")) {
                            if (limites(arraylist)) {
                                if (ID(arraylist)) {
                                    if (limites(arraylist)) {
                                        if (abreChaveta(arraylist)) {
                                            if (limites(arraylist)) {
                                                corpoClass(arraylist);
                                            } else {
                                                System.out.println(RED + "ESPERAVA-SE POR } A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                                                numeroErros++;
                                                i++;
                                            }
                                        } else {
                                            System.out.println(RED + "ESPERAVA-SE POR ABRE CHAVETA APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                            numeroErros++;
                                            i++;
                                        }
                                    } else {
                                        System.out.println(RED + "ESPERAVA-SE POR ABRE CHAVETA APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                        numeroErros++;
                                        i++;
                                    }
                                }
                            } else {
                                System.out.println(RED + "ESPERAVA-SE POR UM IDENTIFICADOR APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                numeroErros++;
                                i++;
                            }
                        } else if (abreChaveta(arraylist)) {
                            if (limites(arraylist)) {
                                corpoClass(arraylist);
                            } else {
                                System.out.println(RED + "ESPERAVA-SE POR } A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                                numeroErros++;
                                i++;
                            }
                        } else {
                            System.out.println(RED + "ESPERAVA-SE POR ABRE CHAVETA, EXTENDS OU IMPLEMENTS APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                            numeroErros++;
                            i++;
                        }
                    } else {
                        System.out.println(RED + "ESPERAVA-SE POR ABRE CHAVETA, EXTENDS OU IMPLEMENTS APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                        numeroErros++;
                        i++;
                    }
                }
            } else {
                System.out.println(RED + "ESPERAVA-SE UM IDENTIFICADOR APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                numeroErros++;
            }
        } else {
            System.out.println(RED + "ESPERAVA-SE 'CLASS' APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
            numeroErros++;
            i++;
        }
    }

    public static void corpoClass(ArrayList<Token> arraylist) {
        Classe = true;
        while (i < arraylist.size()) {
            if (compararPalavra(arraylist, "}")) {
                if (i != arraylist.size()) {
                    System.out.println(RED + "NÃO PODE ESCREVER UM PROGRAMA FORA DO ESCOPO DA CLASSE" + RESET);
                    numeroErros++;
                }
                i = arraylist.size();
            } else if (pontoEvirgula(arraylist) || compararTipo(arraylist, "COMENTÁRIO") || compararTipo(arraylist, "COMENTÁRIO SIMPLES")) {
                i = i;
            } else {
                declaracoes(arraylist);
            }
        }
    }

    public static void corpoDoWhile(ArrayList<Token> arraylist) {
        while (i < arraylist.size()) {
            if (pontoEvirgula(arraylist) || compararTipo(arraylist, "COMENTÁRIO") || compararTipo(arraylist, "COMENTÁRIO SIMPLES")) {
                i = i;
            } else if (compararPalavra(arraylist, "public") || compararPalavra(arraylist, "int") || compararPalavra(arraylist, "float") || compararPalavra(arraylist, "double")
                    || compararPalavra(arraylist, "String") || compararPalavra(arraylist, "boolean") || compararPalavra(arraylist, "long")
                    || compararPalavra(arraylist, "short") || compararPalavra(arraylist, "byte")) {
                i--;
                declaracoes(arraylist);
            } else if (compararPalavra(arraylist, "for")) {
                For(arraylist);
            } else if (compararPalavra(arraylist, "while")) {
                If = false;
                if (limites(arraylist)) {
                    if (compararPalavra(arraylist, "(")) {
                        if (limites(arraylist)) {
                            condicao(arraylist);
                        } else {
                            System.out.println(RED + "ESPERAVA-SE POR UMA EXPRESSÃO LÓGICA  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                            numeroErros++;
                            i++;
                        }
                    }
                } else {
                    System.out.println(RED + "ESPERAVA-SE POR ( APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                    numeroErros++;
                    i++;
                }
            } else if (compararPalavra(arraylist, "if")) {
                If = true;
                if (limites(arraylist)) {
                    if (compararPalavra(arraylist, "(")) {
                        if (limites(arraylist)) {
                            condicao(arraylist);
                        } else {
                            System.out.println(RED + "ESPERAVA-SE POR UMA EXPRESSÃO LÓGICA  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                            numeroErros++;
                            i++;
                        }
                    }
                } else {
                    System.out.println(RED + "ESPERAVA-SE POR ( APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                    numeroErros++;
                    i++;
                }
            } else if (compararPalavra(arraylist, "do")) {
                if (limites(arraylist)) {
                    if (compararPalavra(arraylist, "{")) {
                        if (limites(arraylist)) {
                            corpoDoWhile(arraylist);
                        } else {
                            System.out.println(RED + "ESPERAVA-SE POR { APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                            numeroErros++;
                            i++;
                        }
                    }
                } else {
                    System.out.println(RED + "ESPERAVA-SE POR { APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                    numeroErros++;
                    i++;
                }
            } else if ((arraylist.get(i).tipo.equals("IDENTIFICADOR")) && (arraylist.get(i + 1).lexema.equals("="))) {
                String tipo = "";
                tipoDados = arraylist.get(i).lexema;
                if (verificarExistencia(arraylist.get(i).lexema)) {
                    for (Sintaxe sintax : tabelaSintatica) {
                        if (sintax.lexema.equals(arraylist.get(i).lexema)) {
                            System.out.println("tipo de dados: " + sintax.tipo);
                            tipo = sintax.tipo;
                            break;
                        }
                    }

                    lexemaSintatico = i + 2;
                    i = i + 2;

                    if (limites(arraylist)) {
                        operacoesSemanticas(arraylist, tipo);
                    } else {
                        System.out.println(RED + "ESPERAVA-SE ALGUM VALOR PARA O PARÂMETRO DA FUNÇAÕ OU )  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                        numeroErros++;
                    }
                } else {
                    System.out.println(RED + "VARIÁVEL NÃO CRIADA APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                    numeroErros++;
                    i++;
                }
            } else if ((arraylist.get(i).tipo.equals("IDENTIFICADOR")) && (arraylist.get(i + 1).lexema.equals("("))) {
                if (verificarExistencia(arraylist.get(i).lexema)) {
                    lexemaSintatico = i + 2;
                    i = i + 2;
                    if (limites(arraylist)) {
                        tabelaAuxiliar.clear();
                        tabelaAuxiliar.add(new Sintaxe("TOK_IDENTIFICADOR", arraylist.get(i - 2).lexema, "-", "-", "-", 107));
                        tabelaAuxiliar.add(new Sintaxe("TOK_ABRE_PARENTESES", arraylist.get(i - 1).lexema, "-", "-", "-", 107));
                        chamadaFuncao(arraylist, arraylist.get(i - 2).lexema);
                    } else {
                        System.out.println(RED + "ESPERAVA-SE ALGUM VALOR PARA O PARÂMETRO DA FUNÇAÕ OU )  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                        numeroErros++;
                    }
                } else {
                    System.out.println(RED + "ESPERAVA-SE ALGUMA FUNÇÃO EXISTENTE  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                    numeroErros++;
                    i++;
                }
            } else if ((arraylist.get(i).tipo.equals("IDENTIFICADOR")) && (arraylist.get(i + 1).lexema.equals("["))) {
                if (verificarExistencia(arraylist.get(i).lexema)) {
                    i = i + 2;
                    if (limites(arraylist)) {

                    } else {
                        System.out.println(RED + "ESPERAVA-SE ALGUM VETOR OU MATRIZ APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                        numeroErros++;
                    }
                } else {
                    System.out.println(RED + "ESPERAVA-SE ALGUM VETOR OU MATRIZ EXISTENTE  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                    numeroErros++;
                    i++;
                }
            } else if (fechaChaveta(arraylist)) {
                if (limites(arraylist)) {
                    if (compararPalavra(arraylist, "while")) {
                        if (limites(arraylist)) {
                            if (compararPalavra(arraylist, "(")) {
                                if (limites(arraylist)) {
                                    doWhile = true;
                                    condicao(arraylist);
                                } else {
                                    System.out.println(RED + "ESPERAVA-SE UMA CONDIÇÃO APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                                    numeroErros++;
                                }
                            } else {
                                System.out.println(RED + "ESPERAVA-SE ( APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                                numeroErros++;
                            }
                        } else {
                            System.out.println(RED + "ESPERAVA-SE ( APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                            numeroErros++;
                        }
                    } else {
                        System.out.println(RED + "DO SEM WHILE, ESPERAVA-SE 'WHILE' A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                        numeroErros++;
                    }
                } else {
                    System.out.println(RED + "ESPERAVA-SE } DA CLASSE APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                    numeroErros++;
                }
            } else {
                System.out.println(RED + "ESPERAVA-SE } DA FUNÇÃO APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                numeroErros++;
                i++;
            }
        }
    }

    public static void corpoForWhileUnico(ArrayList<Token> arraylist) {
        if (pontoEvirgula(arraylist) || compararTipo(arraylist, "COMENTÁRIO") || compararTipo(arraylist, "COMENTÁRIO SIMPLES")) {
            i = i;
        } else if (compararPalavra(arraylist, "public") || compararPalavra(arraylist, "int") || compararPalavra(arraylist, "float") || compararPalavra(arraylist, "double")
                || compararPalavra(arraylist, "String") || compararPalavra(arraylist, "boolean") || compararPalavra(arraylist, "long")
                || compararPalavra(arraylist, "short") || compararPalavra(arraylist, "byte")) {
            i--;
            declaracoes(arraylist);
        } else if (compararPalavra(arraylist, "for")) {
            For(arraylist);
        } else if (compararPalavra(arraylist, "while")) {
            if (limites(arraylist)) {
                if (compararPalavra(arraylist, "(")) {
                    if (limites(arraylist)) {
                        condicao(arraylist);
                    } else {
                        System.out.println(RED + "ESPERAVA-SE POR UMA EXPRESSÃO LÓGICA  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                        numeroErros++;
                        i++;
                    }
                }
            } else {
                System.out.println(RED + "ESPERAVA-SE POR ( APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                numeroErros++;
                i++;
            }
        } else if (compararPalavra(arraylist, "if")) {
            if (limites(arraylist)) {
                if (compararPalavra(arraylist, "(")) {
                    if (limites(arraylist)) {
                        condicao(arraylist);
                    } else {
                        System.out.println(RED + "ESPERAVA-SE POR UMA EXPRESSÃO LÓGICA  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                        numeroErros++;
                        i++;
                    }
                }
            } else {
                System.out.println(RED + "ESPERAVA-SE POR ( APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                numeroErros++;
                i++;
            }
        } else {
            System.out.println(RED + "ESPERAVA-SE }  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
            numeroErros++;
            i++;
        }
    }

    public static void corpoSwitch(ArrayList<Token> arraylist) {

    }

    public static boolean verificarExistencia(String lexema) {
        boolean existencia = false;
        for (Sintaxe sintax : tabelaSintatica) {
            if (sintax.lexema.equals(lexema)) {
                existencia = true;
                if (sintax.escopo == controlaEscopo) {
                    numeroErros++;
                    System.out.println(RED + "VARIÁVEL '" + sintax.lexema + "' JÁ EXISTENTE NO MESMO ESCOPO!" + RESET);
                    break;
                }
            }
        }

        if (existencia) {
            return true;
        } else {
            return false;
        }

    }

    public static void adicionarNaTabelaSintatica() {
        for (Sintaxe auxiliar : tabelaAuxiliar) {
            tabelaSintatica.add(auxiliar);
        }

        tabelaAuxiliar.clear();
    }

    public static void declaracoes(ArrayList<Token> arraylist) {
        //indiceLexema, indiceTipo, indiceValorAtribuicao, indiceTipoAtribuicao, indiceEscopo;
        String lexema = "", tipoA = "";
        if (pontoEvirgula(arraylist) || compararTipo(arraylist, "COMENTÁRIO") || compararTipo(arraylist, "COMENTÁRIO SIMPLES")) {
            declaracoes(arraylist);
        } else if (compararPalavra(arraylist, "int") || compararPalavra(arraylist, "short") || compararPalavra(arraylist, "byte")) {
            lexema = "int";
            sintax = new Sintaxe("TOK_PALAVRA_RESERVADA", "int", "-", "-", "-", controlaEscopo);
            tabelaAuxiliar.add(sintax);
            tipoA = "NÚMERO INTEIRO";
            if (limites(arraylist)) {
                verificarExistencia(arraylist.get(i).lexema);
                if (ID(arraylist)) {
                    lexemaSintatico = i - 1;
                    if (limites(arraylist)) {
                        sintax = new Sintaxe("TOK_ID", arraylist.get(lexemaSintatico).lexema, "-", lexema, "-", controlaEscopo);
                        if (compararPalavra(arraylist, "(") && Classe) {
                            tabelaAuxiliar.add(sintax);
                            sintax = new Sintaxe("TOK_ABRE_PARENTESES", arraylist.get(i - 1).lexema, "-", "-", "-", 0);
                            tabelaAuxiliar.add(sintax);
                            if (limites(arraylist)) {
                                parametro(arraylist);
                            } else {
                                System.out.println(RED + "ESPERAVA-SE ( APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                numeroErros++;
                                tabelaAuxiliar.add(sintax);
                            }
                        } else if (compararPalavra(arraylist, ";")) {
                            if (verificarExistencia(arraylist.get(i - 1).lexema)) {
                                System.out.println(RED + "VARIÁVEL '" + lexema + "' JÁ FOI DECLARADA" + RESET);
                                numeroErros++;
                                tabelaAuxiliar.clear();
                            } else {
                                tabelaAuxiliar.add(sintax);
                                ////System.out.println(GREEN + "SUCESSO, VARIÁVEL BEM DECLARADA" + RESET);
                                adicionarNaTabelaSintatica();
                                if (!limites(arraylist)) {
                                    System.out.println(RED + "ESPERAVA-SE }  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                                    numeroErros++;
                                    i++;
                                }
                            }
                        } else if (compararPalavra(arraylist, ",")) {
                            tabelaAuxiliar.add(sintax);
                            declaracaoVariaveis(arraylist, "NÚMERO INTEIRO");
                        } else if (compararPalavra(arraylist, "=")) {
                            if (limites(arraylist)) {
                                if (compararTipo(arraylist, "NÚMERO INTEIRO")) {
                                    sintax = new Sintaxe("TOK_ID", arraylist.get(lexemaSintatico).lexema, arraylist.get(lexemaSintatico + 2).lexema, "int", "int", controlaEscopo);
                                    if (limites(arraylist)) {
                                        if (pontoEvirgula(arraylist)) {
                                            tabelaAuxiliar.add(sintax);
                                            adicionarNaTabelaSintatica();
                                            ////System.out.println(GREEN + "SUCESSO, VARIÁVEL BEM DECLARADA" + RESET);
                                        } else if (compararPalavra(arraylist, ",")) {
                                            if (limites(arraylist)) {
                                                tabelaAuxiliar.add(sintax);
                                                declaracaoVariaveis(arraylist, "NÚMERO INTEIRO");
                                            } else {
                                                System.out.println(RED + "ESPERAVA-SE UM IDENTIFICADOR APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                                numeroErros++;
                                                tabelaAuxiliar.clear();
                                            }
                                        }
                                    } else {
                                        System.out.println(RED + "ESPERAVA-SE ; OU , APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                        numeroErros++;
                                        tabelaAuxiliar.clear();
                                    }
                                } else {
                                    System.out.println(RED + "ESPERAVA-SE UM DADO DO TIPO " + lexema + " APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                    numeroErros++;
                                    tabelaAuxiliar.clear();
                                    i++;
                                }
                            } else {
                                System.out.println(RED + "ESPERAVA-SE UM DADO DO TIPO " + lexema + " APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                numeroErros++;
                                tabelaAuxiliar.clear();
                            }
                        } else if (compararPalavra(arraylist, "[")) {
                            if (limites(arraylist)) {
                                if (fechaParentesesRetos(arraylist)) {
                                    if (limites(arraylist)) {
                                        if (compararPalavra(arraylist, "[")) {
                                            if (limites(arraylist)) {
                                                if (fechaParentesesRetos(arraylist)) {
                                                    if (limites(arraylist)) {
                                                        if (compararPalavra(arraylist, ";")) {
                                                            ////System.out.println(GREEN + "SUCESSO, MATRIZ DECLARADA" + RESET);
                                                            if (!limites(arraylist)) {
                                                                System.out.println(RED + "ESPERAVA-SE }  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                                                                numeroErros++;
                                                                i++;
                                                            }
                                                        } else if (compararPalavra(arraylist, ",")) {
                                                            declaracaoVariaveis(arraylist, tipoA);
                                                        } else if (compararPalavra(arraylist, "=")) {
                                                            if (limites(arraylist)) {
                                                                matriz(arraylist, "int");
                                                            } else {
                                                                System.out.println(RED + "ESPERAVA-SE NEW OU { APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                                                numeroErros++;
                                                                i++;
                                                            }
                                                        } else {
                                                            System.out.println(RED + "3 ESPERAVA-SE ; OU , OU = APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                                            numeroErros++;
                                                            i++;
                                                        }
                                                    } else {
                                                        System.out.println(RED + "4 ESPERAVA-SE ; OU , OU = APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                                        numeroErros++;
                                                        i++;
                                                    }
                                                } else {
                                                    System.out.println(RED + "ESPERAVA-SE ] APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                                    numeroErros++;
                                                    i++;
                                                }
                                            } else {
                                                System.out.println(RED + "ESPERAVA-SE ] APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                                numeroErros++;
                                                i++;
                                            }
                                        } else if (compararPalavra(arraylist, ";")) {
                                            ////System.out.println(GREEN + "SUCESSO, VETOR DECLARADO" + RESET);
                                            if (!limites(arraylist)) {
                                                System.out.println(RED + "ESPERAVA-SE }  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                                                numeroErros++;
                                                i++;
                                            }
                                        } else if (compararPalavra(arraylist, ",")) {
                                            declaracaoVariaveis(arraylist, "NÚMERO INTEIRO");
                                        } else if (compararPalavra(arraylist, "=")) {
                                            if (limites(arraylist)) {
                                                vetor(arraylist, "NÚMERO INTEIRO");
                                            } else {
                                                System.out.println(RED + "ESPERAVA-SE NEW OU { APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                                numeroErros++;
                                                i++;
                                            }
                                        } else {
                                            System.out.println(RED + "ESPERAVA-SE , OU ; APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                            numeroErros++;
                                            i++;
                                        }
                                        //experimento terminou na linha acima
                                    } else {
                                        System.out.println(RED + "ESPERAVA-SE UM IDENTIFICADOR APÓS A EXPRESSÃO " + arraylist.get(i - 2).lexema + " NA LINHA " + arraylist.get(i - 2).linha + RESET);
                                        numeroErros++;
                                        i++;
                                    }
                                } else {
                                    System.out.println(RED + "ESPERAVA-SE ] APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                    numeroErros++;
                                    i++;
                                }
                            } else {
                                System.out.println(RED + "ESPERAVA-SE ] APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                numeroErros++;
                                i++;
                            }
                        } else {
                            System.out.println(RED + "5 ESPERAVA-SE ; OU , OU = APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                            numeroErros++;
                            i++;
                        }
                    } else {
                        System.out.println(RED + "ESPERAVA-SE ; OU = APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                        numeroErros++;
                        i++;
                    }
                } else if (compararPalavra(arraylist, "[")) {
                    if (limites(arraylist)) {
                        if (fechaParentesesRetos(arraylist)) {
                            if (limites(arraylist)) {
                                if (ID(arraylist)) {
                                    if (limites(arraylist)) {
                                        if (compararPalavra(arraylist, ";")) {
                                            ////System.out.println(GREEN + "SUCESSO, VETOR DECLARADO" + RESET);
                                            if (!limites(arraylist)) {
                                                System.out.println(RED + "ESPERAVA-SE }  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                                                numeroErros++;
                                                i++;
                                            }
                                        } else if (compararPalavra(arraylist, ",")) {
                                            declaracaoVariaveis(arraylist, "NÚMERO INTEIRO");
                                        } else if (compararPalavra(arraylist, "=")) {
                                            if (limites(arraylist)) {
                                                vetor(arraylist, "int");
                                            } else {
                                                System.out.println(RED + "ESPERAVA-SE NEW OU { APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                                numeroErros++;
                                                i++;
                                            }
                                        } else {
                                            System.out.println(RED + "6 ESPERAVA-SE ; OU , OU = APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                            numeroErros++;
                                            i++;
                                        }
                                    } else {
                                        System.out.println(RED + "ESPERAVA-SE ; OU = APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                        numeroErros++;
                                        i++;
                                    }
                                }//aqui em baixo tem experimento 
                                else if (compararPalavra(arraylist, "[")) {
                                    if (limites(arraylist)) {
                                        if (fechaParentesesRetos(arraylist)) {
                                            if (limites(arraylist)) {
                                                if (ID(arraylist)) {
                                                    if (limites(arraylist)) {
                                                        if (compararPalavra(arraylist, ";")) {
                                                            ////System.out.println(GREEN + "SUCESSO, MATRIZ DECLARADA" + RESET);
                                                            if (!limites(arraylist)) {
                                                                System.out.println(RED + "ESPERAVA-SE }  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                                                                numeroErros++;
                                                                i++;
                                                            }
                                                        } else if (compararPalavra(arraylist, ",")) {
                                                            declaracaoVariaveis(arraylist, "NÚMERO INTEIRO");
                                                        } else if (compararPalavra(arraylist, "=")) {
                                                            if (limites(arraylist)) {
                                                                matriz(arraylist, "int");
                                                            } else {
                                                                System.out.println(RED + "ESPERAVA-SE NEW OU { APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                                                numeroErros++;
                                                                i++;
                                                            }
                                                        } else {
                                                            System.out.println(RED + "7 ESPERAVA-SE ; OU , OU = APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                                            numeroErros++;
                                                            i++;
                                                        }
                                                    } else {
                                                        System.out.println(RED + "ESPERAVA-SE ; OU = APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                                        numeroErros++;
                                                        i++;
                                                    }
                                                } else {
                                                    System.out.println(RED + "ESPERAVA-SE UM IDENTIFICADOR OU [ APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                                    numeroErros++;
                                                    i++;
                                                }
                                            } else {
                                                System.out.println(RED + "ESPERAVA-SE UM IDENTIFICADOR APÓS A EXPRESSÃO " + arraylist.get(i - 2).lexema + " NA LINHA " + arraylist.get(i - 2).linha + RESET);
                                                numeroErros++;
                                                i++;
                                            }
                                        } else {
                                            System.out.println(RED + "ESPERAVA-SE ] APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                            numeroErros++;
                                            i++;
                                        }
                                    } else {
                                        System.out.println(RED + "ESPERAVA-SE ] APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                        numeroErros++;
                                        i++;
                                    }
                                } else {
                                    System.out.println(RED + "ESPERAVA-SE UM IDENTIFICADOR OU [ APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                    numeroErros++;
                                    i++;
                                }
                                //experimento terminou na linha acima
                            } else {
                                System.out.println(RED + "ESPERAVA-SE UM IDENTIFICADOR APÓS A EXPRESSÃO " + arraylist.get(i - 2).lexema + " NA LINHA " + arraylist.get(i - 2).linha + RESET);
                                numeroErros++;
                                i++;
                            }
                        } else {
                            System.out.println(RED + "ESPERAVA-SE ] APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                            numeroErros++;
                            i++;
                        }
                    } else {
                        System.out.println(RED + "ESPERAVA-SE ] APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                        numeroErros++;
                        i++;
                    }
                } else {
                    System.out.println(RED + "ESPERAVA-SE UM IDENTIFICADOR OU [  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                    numeroErros++;
                    i++;
                }
            } else {
                System.out.println(RED + "ESPERAVA-SE UM IDENTIFICADOR OU [  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                numeroErros++;
                i++;
            }
        } else if (compararPalavra(arraylist, "public")) {
            if (ID(arraylist)) {
                if (limites(arraylist)) {
                    if (compararPalavra(arraylist, "(")) {
                        if (limites(arraylist)) {
                            construtor(arraylist);
                        } else {
                            System.out.println(RED + "ESPERAVA-SE ( APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                            numeroErros++;
                            i++;
                        }
                    } else {
                        System.out.println(RED + "ESPERAVA-SE ( APÓS O NOME DA CLASSE '" + arraylist.get(i - 1).lexema + "' NA LINHA " + arraylist.get(i - 1).linha + RESET);
                        numeroErros++;
                        i++;
                    }
                } else {
                    System.out.println(RED + "ESPERAVA-SE O NOME DA CLASSE APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                    numeroErros++;
                    i++;
                }
            } else if (compararPalavra(arraylist, "static")) {
                if (limites(arraylist)) {
                    declaracoes(arraylist);
                } else {
                    System.out.println(RED + "ESPERAVA-SE UM TIPO DE DADOS OU TIPO DE RETORNO APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                    numeroErros++;
                    i++;
                }
            } else if (compararPalavra(arraylist, "final")) {
                if (limites(arraylist)) {
                    declaracoes(arraylist);
                } else {
                    System.out.println(RED + "ESPERAVA-SE UM TIPO DE DADOS OU TIPO DE RETORNO APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                    numeroErros++;
                    i++;
                }
            } else if (compararPalavra(arraylist, "int") || compararPalavra(arraylist, "double") || compararPalavra(arraylist, "float") || compararPalavra(arraylist, "long") || compararPalavra(arraylist, "short")
                    || compararPalavra(arraylist, "byte") || compararPalavra(arraylist, "boolean")) {
                i--;
                if (limites(arraylist)) {
                    declaracoes(arraylist);
                } else {
                    System.out.println(RED + "ESPERAVA-SE UM TIPO DE DADOS OU TIPO DE RETORNO APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                    numeroErros++;
                    i++;
                }
            } else if (compararPalavra(arraylist, "void")) {
                i--;
                declaracoes(arraylist);
            } else {
                System.out.println(RED + "ESPERAVA-SE UM TIPO DE DADOS, NOME DE CLASSE, STATIC OU FINAL APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                numeroErros++;
                i++;
            }
        } else if (compararPalavra(arraylist, "private")) {
            if (limites(arraylist)) {
                declaracoes(arraylist);
            } else {
                System.out.println(RED + "ESPERAVA-SE UM TIPO DE DADOS, STATIC OU FINAL APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                numeroErros++;
                i++;
            }
        } else if (compararPalavra(arraylist, "void")) {
            if (limites(arraylist)) {
                if (ID(arraylist)) {
                    if (limites(arraylist)) {
                        if (abreParenteses(arraylist)) {
                            if (limites(arraylist)) {
                                parametro(arraylist);
                            } else {
                                System.out.println(RED + "ESPERAVA-SE ) OU VARIÁVEIS APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                numeroErros++;
                                i++;
                            }
                        }
                    } else {
                        System.out.println(RED + "ESPERAVA-SE ( APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                        numeroErros++;
                        i++;
                    }
                } else {
                    System.out.println(RED + "ESPERAVA-SE UM IDENTIFICADOR APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                    numeroErros++;
                    i++;
                }
            } else {
                System.out.println(RED + "ESPERAVA-SE UM IDENTIFICADOR APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                numeroErros++;
                i++;
            }
        } else if (compararPalavra(arraylist, "String")) {
            tipoA = "STRING";
            tipoDados = "String";
            sintax = new Sintaxe("TOK_IDENTIFICADOR", "String", "-", "-", "-", controlaEscopo);
            tabelaAuxiliar.add(sintax);
            declaracoesAux(arraylist, tipoA);
        } else if (compararPalavra(arraylist, "double")) {
            sintax = new Sintaxe("TOK_PALAVRA_RESERVADA", "double", "-", "-", "-", controlaEscopo);
            tipoA = "double";
            tipoDados = "double";
            tabelaAuxiliar.add(sintax);
            declaracoesAux(arraylist, tipoA);
        } else if (compararPalavra(arraylist, "boolean")) {
            sintax = new Sintaxe("TOK_PALAVRA_RESERVADA", "boolean", "-", "-", "-", controlaEscopo);
            tipoA = "PALAVRA RESERVADA";
            tipoDados = "boolean";
            tabelaAuxiliar.add(sintax);
            declaracoesAux(arraylist, tipoA);
        } else if (compararPalavra(arraylist, "long")) {
            sintax = new Sintaxe("TOK_PALAVRA_RESERVADA", "long", "-", "-", "-", controlaEscopo);
            tipoA = "NÚMERO INTEIRO";
            tipoDados = "long";
            tabelaAuxiliar.add(sintax);
            declaracoesAux(arraylist, tipoA);
        } else if (compararPalavra(arraylist, "float")) {
            sintax = new Sintaxe("TOK_PALAVRA_RESERVADA", "float", "-", "-", "-", controlaEscopo);
            tabelaAuxiliar.add(sintax);
            tipoA = "NÚMERO REAL";
            tipoDados = "float";
            declaracoesAux(arraylist, tipoA);
        } else {
            System.out.println(RED + "EXPRESSÃO INESPERADA, APENAS PODE DECLARAR VARIÁVEIS E FUNÇÕES NA CLASSE. VERIFIQUE APÓS O LEXEMA " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
            numeroErros++;
            i++;
        }
    }

    public static void declaracoesAux(ArrayList<Token> arraylist, String tipo) {
        String lexema = "";
        if (tipo.equals("NÚMERO INTEIRO")) {
            lexema = "int";
        } else if (tipo.equals("String")) {
            lexema = "String";
        } else if (tipo.equals("NÚMERO REAL")) {
            lexema = "double";
        } else if (tipo.equals("IDENTIFICADOR")) {
            lexema = "IDENTIFICADOR";
        } else if (tipo.equals("PALAVRA RESERVADA")) {
            lexema = "PALAVRA RESERVADA";
        }

        if (pontoEvirgula(arraylist) || compararTipo(arraylist, "COMENTÁRIO") || compararTipo(arraylist, "COMENTÁRIO SIMPLES")) {
            i = i;
        } else if (limites(arraylist)) {
            if (ID(arraylist)) {
                lexemaSintatico = i - 1;
                if (limites(arraylist)) {
                    if (compararPalavra(arraylist, "(") && Classe) {
                        sintax = new Sintaxe("TOK_ABRE_PARENTESES", arraylist.get(lexemaSintatico).lexema, "-", tipo, "-", controlaEscopo);
                        if (limites(arraylist)) {
                            parametro(arraylist);
                        } else {
                            System.out.println(RED + "ESPERAVA-SE ( APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                            numeroErros++;
                            i++;
                        }
                    } else if (compararPalavra(arraylist, ";")) {
                        sintax = new Sintaxe("TOK_ID", arraylist.get(lexemaSintatico).lexema, "-", tipoDados, "-", controlaEscopo);
                        tabelaAuxiliar.add(sintax);
                        adicionarNaTabelaSintatica();
                        if (!limites(arraylist)) {
                            System.out.println(RED + "ESPERAVA-SE }  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                            numeroErros++;
                            i++;
                        }
                    } else if (compararPalavra(arraylist, ",")) {
                        sintax = new Sintaxe("TOK_ID", arraylist.get(lexemaSintatico).lexema, "-", tipoDados, "-", controlaEscopo);
                        tabelaAuxiliar.add(sintax);
                        declaracaoVariaveis(arraylist, tipo);
                    } else if (compararPalavra(arraylist, "=")) {
                        if (limites(arraylist)) {
                            if (compararTipo(arraylist, tipo)) {
                                if (limites(arraylist)) {
                                    if (tipoDados.equals("float")) {
                                        if (limites(arraylist)) {
                                            if (compararPalavra(arraylist, "f")) {
                                                if (limites(arraylist)) {
                                                    if (compararPalavra(arraylist, ",")) {
                                                        sintax = new Sintaxe("TOK_ID", arraylist.get(lexemaSintatico).lexema, arraylist.get(i - 3).lexema, "float", "-", controlaEscopo);
                                                        tabelaAuxiliar.add(sintax);
                                                        declaracaoVariaveis(arraylist, tipo);
                                                    } else if (compararPalavra(arraylist, ";")) {
                                                        adicionarNaTabelaSintatica();
                                                        ////System.out.println(GREEN + "SUCESSO, FLOAT BEM DECLARADO" + RESET);
                                                    } else {
                                                        System.out.println(RED + "ESPERAVA-SE ; OU , APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                                        numeroErros++;
                                                    }
                                                } else {
                                                    System.out.println(RED + "ESPERAVA-SE ; OU , APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                                    numeroErros++;
                                                }
                                            } else {
                                                System.out.println(RED + "ESPERAVA-SE UM f APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                                numeroErros++;
                                            }
                                        } else {
                                            System.out.println(RED + "ESPERAVA-SE UM f APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                            numeroErros++;
                                        }
                                    } else if (lexema.equals("long")) {
                                        if (limites(arraylist)) {
                                            if (compararPalavra(arraylist, "L")) {
                                                System.out.println("chega aqui");
                                                if (limites(arraylist)) {
                                                    if (compararPalavra(arraylist, ",")) {
                                                        sintax = new Sintaxe("TOK_ID", arraylist.get(lexemaSintatico).lexema, arraylist.get(i - 3).lexema, tipoDados, "-", controlaEscopo);
                                                        tabelaAuxiliar.add(sintax);
                                                        declaracaoVariaveis(arraylist, tipo);
                                                    } else if (compararPalavra(arraylist, ";")) {
                                                        sintax = new Sintaxe("TOK_ID", arraylist.get(lexemaSintatico).lexema, arraylist.get(i - 3).lexema, tipoDados, "-", controlaEscopo);
                                                        tabelaAuxiliar.add(sintax);
                                                        adicionarNaTabelaSintatica();
                                                        ////System.out.println(GREEN + "SUCESSO, LONG BEM DECLARADO" + RESET);
                                                    } else {
                                                        System.out.println(RED + "ESPERAVA-SE ; OU , APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                                        numeroErros++;
                                                        tabelaAuxiliar.clear();
                                                    }
                                                } else {
                                                    System.out.println(RED + "ESPERAVA-SE ; OU , APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                                    numeroErros++;
                                                    tabelaAuxiliar.clear();
                                                }
                                            } else {
                                                System.out.println(RED + "ESPERAVA-SE UM L APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                                numeroErros++;
                                                tabelaAuxiliar.clear();
                                            }
                                        } else {
                                            System.out.println(RED + "ESPERAVA-SE UM L APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                            numeroErros++;
                                        }
                                    } else if (pontoEvirgula(arraylist)) {
                                        sintax = new Sintaxe("TOK_ID", arraylist.get(lexemaSintatico).lexema, "-", tipoDados, "-", controlaEscopo);
                                        tabelaAuxiliar.add(sintax);
                                        adicionarNaTabelaSintatica();
                                        ////System.out.println(GREEN + "SUCESSO, VARIÁVEL BEM DECLARADA" + RESET);
                                    } else if (compararPalavra(arraylist, ",")) {
                                        sintax = new Sintaxe("TOK_ID", arraylist.get(lexemaSintatico).lexema, "-", tipoDados, "-", controlaEscopo);
                                        tabelaAuxiliar.add(sintax);
                                        if (limites(arraylist)) {
                                            declaracaoVariaveis(arraylist, tipo);
                                        } else {
                                            System.out.println(RED + "ESPERAVA-SE UM IDENTIFICADOR APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                            numeroErros++;
                                            tabelaAuxiliar.clear();
                                        }
                                    }
                                } else {
                                    System.out.println(RED + "ESPERAVA-SE ; OU , APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                    numeroErros++;
                                }
                            } else {
                                System.out.println(RED + "ESPERAVA-SE UM DADO DO TIPO " + lexema + " APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                numeroErros++;
                                i++;
                            }
                        } else {
                            System.out.println(RED + "ESPERAVA-SE UM DADO DO TIPO " + lexema + " APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                            numeroErros++;
                        }
                    } else if (compararPalavra(arraylist, "[")) {
                        if (limites(arraylist)) {
                            if (fechaParentesesRetos(arraylist)) {
                                if (limites(arraylist)) {
                                    if (compararPalavra(arraylist, "[")) {
                                        if (limites(arraylist)) {
                                            if (fechaParentesesRetos(arraylist)) {
                                                if (limites(arraylist)) {
                                                    if (compararPalavra(arraylist, ";")) {
                                                        ////System.out.println(GREEN + "SUCESSO, MATRIZ DECLARADA" + RESET);
                                                        if (!limites(arraylist)) {
                                                            System.out.println(RED + "ESPERAVA-SE }  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                                                            numeroErros++;
                                                            i++;
                                                        }
                                                    } else if (compararPalavra(arraylist, ",")) {
                                                        declaracaoVariaveis(arraylist, tipo);
                                                    } else if (compararPalavra(arraylist, "=")) {
                                                        if (limites(arraylist)) {
                                                            matriz(arraylist, lexema);
                                                        } else {
                                                            System.out.println(RED + "ESPERAVA-SE NEW OU { APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                                            numeroErros++;
                                                            i++;
                                                        }
                                                    } else {
                                                        System.out.println(RED + "8 ESPERAVA-SE ; OU , OU = APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                                        numeroErros++;
                                                        i++;
                                                    }
                                                } else {
                                                    System.out.println(RED + "9 ESPERAVA-SE ; OU , OU = APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                                    numeroErros++;
                                                    i++;
                                                }
                                            } else {
                                                System.out.println(RED + "ESPERAVA-SE ] APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                                numeroErros++;
                                                i++;
                                            }
                                        } else {
                                            System.out.println(RED + "ESPERAVA-SE ] APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                            numeroErros++;
                                            i++;
                                        }
                                    } else if (compararPalavra(arraylist, ";")) {
                                        ////System.out.println(GREEN + "SUCESSO, VETOR DECLARADO" + RESET);
                                        if (!limites(arraylist)) {
                                            System.out.println(RED + "ESPERAVA-SE }  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                                            numeroErros++;
                                            i++;
                                        }
                                    } else if (compararPalavra(arraylist, ",")) {
                                        declaracaoVariaveis(arraylist, tipo);
                                    } else if (compararPalavra(arraylist, "=")) {
                                        if (limites(arraylist)) {
                                            vetor(arraylist, lexema);
                                        } else {
                                            System.out.println(RED + "ESPERAVA-SE NEW OU { APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                            numeroErros++;
                                            i++;
                                        }
                                    } else {
                                        System.out.println(RED + "ESPERAVA-SE , OU ; APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                        numeroErros++;
                                        i++;
                                    }
                                    //experimento terminou na linha acima
                                } else {
                                    System.out.println(RED + "ESPERAVA-SE UM IDENTIFICADOR APÓS A EXPRESSÃO " + arraylist.get(i - 2).lexema + " NA LINHA " + arraylist.get(i - 2).linha + RESET);
                                    numeroErros++;
                                    i++;
                                }
                            } else {
                                System.out.println(RED + "ESPERAVA-SE ] APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                numeroErros++;
                                i++;
                            }
                        } else {
                            System.out.println(RED + "ESPERAVA-SE ] APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                            numeroErros++;
                            i++;
                        }
                    } else {
                        System.out.println(RED + "10 ESPERAVA-SE ; OU , OU = APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                        numeroErros++;
                        i++;
                    }
                } else {
                    System.out.println(RED + "ESPERAVA-SE ; OU = APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                    numeroErros++;
                    i++;
                }
            } else if (compararPalavra(arraylist, "[")) {
                if (limites(arraylist)) {
                    if (fechaParentesesRetos(arraylist)) {
                        if (limites(arraylist)) {
                            if (ID(arraylist)) {
                                if (limites(arraylist)) {
                                    if (compararPalavra(arraylist, ";")) {
                                        ////System.out.println(GREEN + "SUCESSO, VETOR DECLARADO" + RESET);
                                        if (!limites(arraylist)) {
                                            System.out.println(RED + "ESPERAVA-SE }  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                                            numeroErros++;
                                            i++;
                                        }
                                    } else if (compararPalavra(arraylist, ",")) {
                                        declaracaoVariaveis(arraylist, tipo);
                                    } else if (compararPalavra(arraylist, "=")) {
                                        if (limites(arraylist)) {
                                            vetor(arraylist, lexema);
                                        } else {
                                            System.out.println(RED + "ESPERAVA-SE NEW OU { APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                            numeroErros++;
                                            i++;
                                        }
                                    } else {
                                        System.out.println(RED + " 1 ESPERAVA-SE ; OU , OU = APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                        numeroErros++;
                                        i++;
                                    }
                                } else {
                                    System.out.println(RED + "ESPERAVA-SE ; OU = APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                    numeroErros++;
                                    i++;
                                }
                            }//aqui em baixo tem experimento 
                            else if (compararPalavra(arraylist, "[")) {
                                if (limites(arraylist)) {
                                    if (fechaParentesesRetos(arraylist)) {
                                        if (limites(arraylist)) {
                                            if (ID(arraylist)) {
                                                if (limites(arraylist)) {
                                                    if (compararPalavra(arraylist, ";")) {
                                                        ////System.out.println(GREEN + "SUCESSO, MATRIZ DECLARADA" + RESET);
                                                        if (!limites(arraylist)) {
                                                            System.out.println(RED + "ESPERAVA-SE }  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                                                            numeroErros++;
                                                            i++;
                                                        }
                                                    } else if (compararPalavra(arraylist, ",")) {
                                                        declaracaoVariaveis(arraylist, tipo);
                                                    } else if (compararPalavra(arraylist, "=")) {
                                                        if (limites(arraylist)) {
                                                            matriz(arraylist, lexema);
                                                        } else {
                                                            System.out.println(RED + "ESPERAVA-SE NEW OU { APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                                            numeroErros++;
                                                            i++;
                                                        }
                                                    } else {
                                                        System.out.println(RED + "2 ESPERAVA-SE ; OU , OU = APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                                        numeroErros++;
                                                        i++;
                                                    }
                                                } else {
                                                    System.out.println(RED + "ESPERAVA-SE ; OU = APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                                    numeroErros++;
                                                    i++;
                                                }
                                            } else {
                                                System.out.println(RED + "ESPERAVA-SE UM IDENTIFICADOR OU [ APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                                numeroErros++;
                                                i++;
                                            }
                                        } else {
                                            System.out.println(RED + "ESPERAVA-SE UM IDENTIFICADOR APÓS A EXPRESSÃO " + arraylist.get(i - 2).lexema + " NA LINHA " + arraylist.get(i - 2).linha + RESET);
                                            numeroErros++;
                                            i++;
                                        }
                                    } else {
                                        System.out.println(RED + "ESPERAVA-SE ] APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                        numeroErros++;
                                        i++;
                                    }
                                } else {
                                    System.out.println(RED + "ESPERAVA-SE ] APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                    numeroErros++;
                                    i++;
                                }
                            } else {
                                System.out.println(RED + "ESPERAVA-SE UM IDENTIFICADOR OU [ APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                numeroErros++;
                                i++;
                            }
                            //experimento terminou na linha acima
                        } else {
                            System.out.println(RED + "ESPERAVA-SE UM IDENTIFICADOR APÓS A EXPRESSÃO " + arraylist.get(i - 2).lexema + " NA LINHA " + arraylist.get(i - 2).linha + RESET);
                            numeroErros++;
                            i++;
                        }
                    } else {
                        System.out.println(RED + "ESPERAVA-SE ] APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                        numeroErros++;
                        i++;
                    }
                } else {
                    System.out.println(RED + "ESPERAVA-SE ] APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                    numeroErros++;
                    i++;
                }
            } else {
                System.out.println(RED + "ESPERAVA-SE UM IDENTIFICADOR OU [  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                numeroErros++;
                i++;
            }
        } else {
            System.out.println(RED + "ESPERAVA-SE UM IDENTIFICADOR OU [  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
            numeroErros++;
            i++;
        }
    }

    public static void construtor(ArrayList<Token> arraylist) {
        if (compararTipo(arraylist, "COMENTÁRIO") || compararTipo(arraylist, "COMENTÁRIO SIMPLES")) {
            i = i;
        } else if (compararPalavra(arraylist, "int") || compararPalavra(arraylist, "float") || compararPalavra(arraylist, "double")
                || compararPalavra(arraylist, "String") || compararPalavra(arraylist, "boolean") || compararPalavra(arraylist, "long")
                || compararPalavra(arraylist, "short") || compararPalavra(arraylist, "byte")) {
            if (limites(arraylist)) {
                if (ID(arraylist)) {
                    if (limites(arraylist)) {
                        if (compararPalavra(arraylist, ",")) {
                            parametro(arraylist);
                        } else if (compararPalavra(arraylist, ")")) {
                            if (limites(arraylist)) {
                                if (compararPalavra(arraylist, "{")) {
                                    ////System.out.println(GREEN + "SUCESSO, ENTRANDO NO CORPO DO CONSTRUTOR" + RESET);
                                    corpoFuncao(arraylist);
                                }
                            } else {
                                System.out.println(RED + "ESPERAVA-SE { APÓS O LEXEMA: " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                numeroErros++;
                                i++;
                            }
                        }
                    } else {
                        System.out.println(RED + "ESPERAVA-SE ) OU , APÓS O LEXEMA: " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                        numeroErros++;
                        i++;
                    }
                } else {
                    System.out.println(RED + "EXPRESSÃO INESPERADA, ESPERAVA-SE UM IDENTIFICADOR APÓS O LEXEMA: " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                    numeroErros++;
                    i++;
                }
            } else {
                System.out.println(RED + "ESPERAVA-SE UM IDENTIFICADOR APÓS O LEXEMA: " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                numeroErros++;
                i++;
            }
        } else if (compararPalavra(arraylist, ")")) {
            if (limites(arraylist)) {
                if (compararPalavra(arraylist, "{")) {
                    ////System.out.println(GREEN + "SUCESSO, ENTRANDO NO CORPO DO CONSTRUTOR" + RESET);
                    if (limites(arraylist)) {
                        corpoFuncao(arraylist);
                    } else {
                        System.out.println(RED + "ESPERAVA-SE }  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                        numeroErros++;
                        i++;
                    }
                }
            } else {
                System.out.println(RED + "ESPERAVA-SE { APÓS O LEXEMA: " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                numeroErros++;
                i++;
            }
        } else {
            System.out.println(RED + "EXPRESSÃO INESPERADA, ESPERAVA-SE UM IDENTIFICADOR APÓS O LEXEMA: " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
            numeroErros++;
            i++;
        }
    }

    public static void parametro(ArrayList<Token> arraylist) {
        if (pontoEvirgula(arraylist) || compararTipo(arraylist, "COMENTÁRIO") || compararTipo(arraylist, "COMENTÁRIO SIMPLES")) {
            i = i;
        } else if (compararPalavra(arraylist, "int") || compararPalavra(arraylist, "float") || compararPalavra(arraylist, "double")
                || compararPalavra(arraylist, "String") || compararPalavra(arraylist, "boolean") || compararPalavra(arraylist, "long")
                || compararPalavra(arraylist, "short") || compararPalavra(arraylist, "byte")) {
            if (limites(arraylist)) {
                if (ID(arraylist)) {
                    sintax = new Sintaxe("TOK_ID", arraylist.get(i - 1).lexema, "-", arraylist.get(i - 2).lexema, arraylist.get(i - 2).lexema, -1);
                    if (limites(arraylist)) {
                        if (compararPalavra(arraylist, ",")) {
                            tabelaAuxiliar.add(sintax);
                            parametro(arraylist);
                        } else if (compararPalavra(arraylist, ")")) {
                            tabelaAuxiliar.add(sintax);
                            sintax = new Sintaxe("TOK_FECHA_PARENTESES", ")", "-", "-", "-", 0);
                            if (limites(arraylist)) {
                                if (compararPalavra(arraylist, "{")) {
                                    controlaEscopo++;
                                    tabelaAuxiliar.add(sintax);
                                    adicionarNaTabelaSintatica();
                                    ////System.out.println(GREEN + "SUCESSO, ENTRANDO NO CORPO DA FUNÇÃO" + RESET);
                                    corpoFuncao(arraylist);
                                } else {
                                    System.out.println(RED + "ESPERAVA-SE { APÓS O LEXEMA: " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                    numeroErros++;
                                }
                            } else {
                                System.out.println(RED + "ESPERAVA-SE { APÓS O LEXEMA: " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                numeroErros++;
                            }
                        }
                    } else {
                        System.out.println(RED + "ESPERAVA-SE ) OU , APÓS O LEXEMA: " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                        numeroErros++;
                        i++;
                    }
                } else {
                    System.out.println(RED + "EXPRESSÃO INESPERADA, ESPERAVA-SE UM IDENTIFICADOR APÓS O LEXEMA: " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                    numeroErros++;
                    i++;
                }
            } else {
                System.out.println(RED + "ESPERAVA-SE UM IDENTIFICADOR APÓS O LEXEMA: " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                numeroErros++;
                i++;
            }
        } else if (compararPalavra(arraylist, ")")) {
            sintax = new Sintaxe("TOK_FECHA_PARENTESES", ")", "-", "-", "-", 0);
            tabelaAuxiliar.add(sintax);
            if (limites(arraylist)) {
                if (compararPalavra(arraylist, "{")) {
                    ////System.out.println(GREEN + "SUCESSO, ENTRANDO NO CORPO DA FUNÇÃO" + RESET);
                    if (limites(arraylist)) {
                        adicionarNaTabelaSintatica();
                        controlaEscopo++;
                        corpoFuncao(arraylist);
                    } else {
                        System.out.println(RED + "ESPERAVA-SE }  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                        numeroErros++;
                        i++;
                    }
                } else {
                    System.out.println(RED + "ESPERAVA-SE {  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                    numeroErros++;
                }
            } else {
                System.out.println(RED + "ESPERAVA-SE { APÓS O LEXEMA: " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                numeroErros++;
                i++;
            }
        } else {
            System.out.println(RED + "EXPRESSÃO INESPERADA, ESPERAVA-SE UM IDENTIFICADOR APÓS O LEXEMA: " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
            numeroErros++;
            i++;
        }
    }

    //tenho de aprimorar bem o escopo quando entra numa função, sai e depois volta novamente numa função
    public static void operacoesSemanticas(ArrayList<Token> arraylist, String tipoDeDados) {
        Sintaxe sintaxes = new Sintaxe();
        String tipoD = "";
        for (Sintaxe sintax : tabelaSintatica) {
            if (sintax.lexema.equals(tipoDados)) {
                sintaxes = sintax;
                tipoD = sintaxes.tipo;
                break;
            }
        }
        if (limites(arraylist)) {
            if (ID(arraylist)) {
                for (Sintaxe sintax : tabelaSintatica) {
                    if (sintax.lexema.equals(tipoDados)) {
                        for (Sintaxe teste : tabelaSintatica) {
                            if (teste.lexema.equals(arraylist.get(i - 1).lexema)) {
                                op = op.concat(teste.valorDeAtribuicao);
                                break;
                            }
                        }
                    }
                }
                if (verificarExistencia(arraylist.get(i - 1).lexema)) {
                    if (limites(arraylist)) {
                        System.out.println("lexema: " + arraylist.get(i).lexema);
                        if (compararPalavra(arraylist, "/") || compararPalavra(arraylist, "*") || compararPalavra(arraylist, "-") || compararPalavra(arraylist, "+") || compararPalavra(arraylist, "%")) {
                            if (limites(arraylist)) {
                                op = op.concat(arraylist.get(i - 1).lexema);
                                operacoesSemanticas(arraylist, tipoDeDados);
                            } else {
                                System.out.println(RED + "ESPERAVA-SE UM NÚMERO OU UM IDENTIFICADOR " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                                numeroErros++;
                                op = "";
                            }
                        } else if (pontoEvirgula(arraylist)) {
                            for (Sintaxe sintax : tabelaSintatica) {
                                if (sintax.lexema.equals(tipoDados)) {
                                    sintax.lexema = operacoesSemanticasAux();
                                }
                            }
                            op = "";
                            System.out.println("tá fixe");
                        } else {
                            System.out.println(RED + "ESPERAVA-SE UM OPERADOR OU ; " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                            numeroErros++;
                        }
                    } else {
                        System.out.println(RED + "ESPERAVA-SE UM OPERADOR OU ; " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                        numeroErros++;
                    }
                } else {
                    System.out.println(RED + "ESPERAVA-SE UMA VARIÁVEL JÁ DECLARADA OU TIPO DE DADOS " + sintaxes.tipo + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                    numeroErros++;
                }
            } else if (compararTipo(arraylist, arraylist.get(i).tipo)) {
                /*for(Sintax sintax: tabelaSintatica){
                    if(sintax.lexema.equals(tipoDados)){
                        sintax.valorDeAtribuicao = arraylist.get(i-1).lexema;
                    }
                }*/
                op = arraylist.get(i - 1).lexema;
                if (verificarExistencia(arraylist.get(i - 1).lexema)) {
                    if (limites(arraylist)) {
                        if (compararPalavra(arraylist, "/") || compararPalavra(arraylist, "*") || compararPalavra(arraylist, "-") || compararPalavra(arraylist, "+") || compararPalavra(arraylist, "%")) {
                            if (limites(arraylist)) {
                                op = arraylist.get(i - 1).lexema;
                                operacoesSemanticas(arraylist, tipoDeDados);
                            } else {
                                System.out.println(RED + "ESPERAVA-SE UM NÚMERO OU UM IDENTIFICADOR " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                                numeroErros++;
                                op = "";
                            }
                        } else if (pontoEvirgula(arraylist)) {
                            for (Sintaxe sintax : tabelaSintatica) {
                                if (sintax.lexema.equals(tipoDados)) {
                                    sintax.lexema = operacoesSemanticasAux();
                                }
                            }
                            op = "";
                            System.out.println("tá fixe, chegou aqui!");
                        } else {
                            System.out.println(RED + "ESPERAVA-SE UM OPERADOR OU ; " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                            numeroErros++;
                        }
                    } else {
                        System.out.println(RED + "ESPERAVA-SE UM OPERADOR OU ; " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                        numeroErros++;
                    }
                } else {
                    System.out.println(RED + "ESPERAVA-SE UMA VARIÁVEL JÁ DECLARADA OU TIPO DE DADOS " + sintaxes.tipo + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                    numeroErros++;
                    i++;
                }
            } else {
                System.out.println(RED + "ESPERAVA-SE UMA VARIÁVEL JÁ DECLARADA OU TIPO DE DADOS " + sintaxes.tipo + " APÓS " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                numeroErros++;
            }
        } else {
            System.out.println(RED + "ESPERAVA-SE UM IDENTIFICADOR OU TIPO DE DADOS " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
            numeroErros++;
        }
    }

    public static String operacoesSemanticasAux() {
        String resultadoInt = "";
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        System.out.println("entrou aqui, operação: " + op);
        try {
            // Avaliar a expressão e obter o resultado
            Object resultadoObj = engine.eval(op);

            // Converter o resultado para um inteiro
            resultadoInt = (resultadoObj.toString());

            // Exibir o resultado
            //System.out.println("O resultado da expressão é: " + resultadoInt);
        } catch (ScriptException e) {
            System.out.println("Erro ao avaliar a expressão: " + e.getMessage());
        }

        return resultadoInt;
    }

    public static void corpoFuncao(ArrayList<Token> arraylist) {
        Classe = false;
        while (i < arraylist.size()) {
            if (pontoEvirgula(arraylist) || compararTipo(arraylist, "COMENTÁRIO") || compararTipo(arraylist, "COMENTÁRIO SIMPLES")) {
                i = i;
            } else if (compararPalavra(arraylist, "public") || compararPalavra(arraylist, "int") || compararPalavra(arraylist, "float") || compararPalavra(arraylist, "double")
                    || compararPalavra(arraylist, "String") || compararPalavra(arraylist, "boolean") || compararPalavra(arraylist, "long")
                    || compararPalavra(arraylist, "short") || compararPalavra(arraylist, "byte")) {
                i--;
                declaracoes(arraylist);
            } else if (compararPalavra(arraylist, "for")) {
                For(arraylist);
            } else if (compararPalavra(arraylist, "while")) {
                If = false;
                if (limites(arraylist)) {
                    if (compararPalavra(arraylist, "(")) {
                        if (limites(arraylist)) {
                            condicao(arraylist);
                        } else {
                            System.out.println(RED + "ESPERAVA-SE POR UMA EXPRESSÃO LÓGICA  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                            numeroErros++;
                            i++;
                        }
                    }
                } else {
                    System.out.println(RED + "ESPERAVA-SE POR ( APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                    numeroErros++;
                    i++;
                }
            } else if (compararPalavra(arraylist, "if")) {
                If = true;
                if (limites(arraylist)) {
                    if (compararPalavra(arraylist, "(")) {
                        if (limites(arraylist)) {
                            condicao(arraylist);
                        } else {
                            System.out.println(RED + "ESPERAVA-SE POR UMA EXPRESSÃO LÓGICA  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                            numeroErros++;
                            i++;
                        }
                    }
                } else {
                    System.out.println(RED + "ESPERAVA-SE POR ( APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                    numeroErros++;
                    i++;
                }
            } else if (compararPalavra(arraylist, "do")) {
                if (limites(arraylist)) {
                    if (compararPalavra(arraylist, "{")) {
                        if (limites(arraylist)) {
                            corpoDoWhile(arraylist);
                        } else {
                            System.out.println(RED + "ESPERAVA-SE { APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                            numeroErros++;
                            i++;
                        }
                    }
                } else {
                    System.out.println(RED + "ESPERAVA-SE POR ( APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                    numeroErros++;
                    i++;
                }
            } else if ((arraylist.get(i).tipo.equals("IDENTIFICADOR")) && (arraylist.get(i + 1).lexema.equals("="))) {
                String tipo = "";
                tipoDados = arraylist.get(i).lexema;
                if (verificarExistencia(arraylist.get(i).lexema)) {
                    for (Sintaxe sintax : tabelaSintatica) {
                        if (sintax.lexema.equals(arraylist.get(i).lexema)) {
                            System.out.println("tipo de dados: " + sintax.tipo);
                            tipo = sintax.tipo;
                            break;
                        }
                    }
                    lexemaSintatico = i + 2;
                    i = i + 2;
                    if (limites(arraylist)) {
                        operacoesSemanticas(arraylist, tipo);
                    } else {
                        System.out.println(RED + "ESPERAVA-SE ALGUM VALOR PARA O PARÂMETRO DA FUNÇAÕ OU )  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                        numeroErros++;
                    }
                } else {
                    System.out.println(RED + "VARIÁVEL NÃO CRIADA APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                    numeroErros++;
                    i++;
                }
            } else if ((arraylist.get(i).tipo.equals("IDENTIFICADOR")) && (arraylist.get(i + 1).lexema.equals("("))) {
                if (verificarExistencia(arraylist.get(i).lexema)) {
                    lexemaSintatico = i + 2;
                    i = i + 2;
                    if (limites(arraylist)) {
                        tabelaAuxiliar.clear();
                        tabelaAuxiliar.add(new Sintaxe("TOK_IDENTIFICADOR", arraylist.get(i - 2).lexema, "-", "-", "-", 0));
                        tabelaAuxiliar.add(new Sintaxe("TOK_ABRE_PARENTESES", arraylist.get(i - 1).lexema, "-", "-", "-", 0));
                        chamadaFuncao(arraylist, arraylist.get(i - 2).lexema);
                    } else {
                        System.out.println(RED + "ESPERAVA-SE ALGUM VALOR PARA O PARÂMETRO DA FUNÇAÕ OU )  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                        numeroErros++;
                    }
                } else {
                    System.out.println(RED + "ESPERAVA-SE ALGUMA FUNÇÃO EXISTENTE  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                    numeroErros++;
                    i++;
                }
            } else if ((arraylist.get(i).tipo.equals("IDENTIFICADOR")) && (arraylist.get(i + 1).lexema.equals("["))) {
                if (verificarExistencia(arraylist.get(i).lexema)) {
                    i = i + 2;
                    if (limites(arraylist)) {

                    } else {
                        System.out.println(RED + "ESPERAVA-SE ALGUM VETOR OU MATRIZ APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                        numeroErros++;
                    }
                } else {
                    System.out.println(RED + "ESPERAVA-SE ALGUM VETOR OU MATRIZ EXISTENTE  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                    numeroErros++;
                    i++;
                }
            } else if (fechaChaveta(arraylist)) {
                i--;
                if (limites(arraylist)) {
                    corpoClass(arraylist);
                } else {
                    System.out.println(RED + "ESPERAVA-SE } DA CLASSE APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                    numeroErros++;
                    i++;
                }
            } else {
                System.out.println(RED + "ESPERAVA-SE } DA FUNÇÃO APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                numeroErros++;
                i++;
            }
        }
    }

    public static void validarParametros(ArrayList<Token> arraylist, String nomeFuncao) {
        int j = 0;
        boolean procura = true;
        boolean encontrou = false;
        boolean fim = false;
        if (compararTipo(arraylist, "COMENTÁRIO") || compararTipo(arraylist, "COMENTÁRIO SIMPLES")) {
            i = i;
        }

        for (Sintaxe sintax : tabelaSintatica) {
            if (procura && sintax.lexema.equals(nomeFuncao)) {
                encontrou = true;
                j++;
                procura = false;
            } else if (encontrou && !fim) {
                if (sintax.equals(")")) {
                    if (tabelaAuxiliar.get(j).lexema.equals(")")) {
                        fim = true;
                        System.out.println("terminou");
                        break;
                    }
                } else {
                    if (j >= tabelaAuxiliar.size()) {
                        break;
                    } else if (sintax.lexema.equals(")") && j < tabelaAuxiliar.size() - 1) {
                        System.out.println(RED + "MUITOS ARGUMENTOS PRA FUNÇÃO " + RESET);
                        numeroErros++;
                        tabelaAuxiliar.clear();
                        break;
                    } else if (j < tabelaAuxiliar.size() && !tabelaAuxiliar.get(j).equals(")") && sintax.tipo.equals(tabelaAuxiliar.get(j).tipo)) {
                        j++;
                    } else if (!sintax.tipo.equals(tabelaAuxiliar.get(j).tipo) && j < arraylist.size() && !tabelaAuxiliar.get(j).lexema.equals(")")) {
                        System.out.println(RED + "TOKEN INESPERADO '" + tabelaAuxiliar.get(j).lexema + "' NA CHAMADA DA FUNÇÃO NA LINHA " + arraylist.get(i - tabelaAuxiliar.size()).linha + "\nESPERAVA-SE UM DADO DO TIPO " + sintax.tipo + RESET);
                        numeroErros++;
                        tabelaAuxiliar.clear();
                    } else {
                        System.out.println(RED + "FALTAM ARGUMENTOS PRA FUNÇÃO " + nomeFuncao + " NA LINHA " + arraylist.get(i - tabelaAuxiliar.size()).linha + RESET);
                        numeroErros++;
                        tabelaAuxiliar.clear();
                        break;
                    }
                }
            }
        }
    }

    //implementando a lógica pro caso da chamada de função
    public static void chamadaFuncao(ArrayList<Token> arraylist, String nomeFuncao) {
        int contChamadaFuncao = 2;
        if (compararTipo(arraylist, "COMENTÁRIO") || compararTipo(arraylist, "COMENTÁRIO SIMPLES")) {
            i = i;
        } else if (compararTipo(arraylist, "NÚMERO INTEIRO") || compararTipo(arraylist, "NÚMERO REAL") || compararTipo(arraylist, "IDENTIFICADOR")
                || compararTipo(arraylist, "STRING") || compararTipo(arraylist, "CHAR")) {
            String tipo = arraylist.get(i - 1).tipo;
            contChamadaFuncao++;
            if (arraylist.get(i - 1).tipo.equals("IDENTIFICADOR")) {
                for (Sintaxe sintax : tabelaSintatica) {
                    if (sintax.lexema.equals(arraylist.get(i - 1).lexema)) {
                        tabelaAuxiliar.add(sintax);
                        break;
                    }
                }
            } else if (tipo.equals("NÚMERO INTEIRO")) {
                tipo = "int";
                tabelaAuxiliar.add(new Sintaxe("TOK_".concat(tipo.toUpperCase()), arraylist.get(i - 1).lexema, "-", tipo, "-", -1));
            } else if (tipo.equals("NÚMERO REAL")) {
                tipo = "double";
                tabelaAuxiliar.add(new Sintaxe("TOK_".concat(tipo.toUpperCase()), arraylist.get(i - 1).lexema, "-", tipo, "-", -1));
            } else if (tipo.equals("STRING")) {
                tipo = "String";
                tabelaAuxiliar.add(new Sintaxe("TOK_".concat(tipo.toUpperCase()), arraylist.get(i - 1).lexema, "-", tipo, "-", -1));
            } else if (tipo.equals("CHAR")) {
                tipo = "char";
                tabelaAuxiliar.add(new Sintaxe("TOK_".concat(tipo.toUpperCase()), arraylist.get(i - 1).lexema, "-", tipo, "-", -1));
            }

            //validarParametros(arraylist, arraylist.get(i-1).tipo);
            if (limites(arraylist)) {
                if (compararPalavra(arraylist, ",")) {
                    if (limites(arraylist)) {
                        chamadaFuncao(arraylist, nomeFuncao);
                    } else {
                        System.out.println(RED + "ESPERAVA-SE UM VALOR PRO PARÂMETRO DA FUNÇÃO APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                        numeroErros++;
                    }
                } else if (compararPalavra(arraylist, ")")) {
                    tabelaAuxiliar.add(new Sintaxe("TOK_FECHA_PARENTESES", ")", "-", "-", "-", -1));
                    contChamadaFuncao++;
                    if (limites(arraylist)) {
                        if (compararPalavra(arraylist, ";")) {
                            validarParametros(arraylist, nomeFuncao);
                            //System.out.println(GREEN+"SUCESSO, CHAMADA DE FUNÇÃO. quantos mambos: "+contChamadaFuncao+RESET);
                        } else {
                            System.out.println(RED + "ESPERAVA-SE ; APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                            numeroErros++;
                            i++;
                        }
                    } else {
                        System.out.println(RED + "ESPERAVA-SE ; APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                        numeroErros++;
                    }
                } else {
                    System.out.println(RED + "ESPERAVA-SE , OU )  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                    numeroErros++;
                    i++;
                }
            } else {
                System.out.println(RED + "ESPERAVA-SE UM  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                numeroErros++;
            }
        } else if (compararPalavra(arraylist, ")")) {
            if (limites(arraylist)) {
                if (compararPalavra(arraylist, ";")) {
                    validarParametros(arraylist, nomeFuncao);
                    //System.out.println(GREEN+"SUCESSO, CHAMADA DE FUNÇÃO."+RESET);
                } else {
                    System.out.println(RED + "ESPERAVA-SE ; APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                    numeroErros++;
                    i++;
                }
            } else {
                System.out.println(RED + "ESPERAVA-SE ; APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                numeroErros++;
            }
        } else {
            System.out.println(RED + "ESPERAVA-SE UM VALOR PRO PARÂMETRO OU ) DA FUNÇÃO APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
            numeroErros++;
            i++;
        }
    }

    public static void corpoFor(ArrayList<Token> arraylist) {
        while (i < arraylist.size()) {
            if (pontoEvirgula(arraylist) || compararTipo(arraylist, "COMENTÁRIO") || compararTipo(arraylist, "COMENTÁRIO SIMPLES")) {
                i = i;
            } else if (compararPalavra(arraylist, "public") || compararPalavra(arraylist, "int") || compararPalavra(arraylist, "float") || compararPalavra(arraylist, "double")
                    || compararPalavra(arraylist, "String") || compararPalavra(arraylist, "boolean") || compararPalavra(arraylist, "long")
                    || compararPalavra(arraylist, "short") || compararPalavra(arraylist, "byte")) {
                i--;
                declaracoes(arraylist);
            } else if (compararPalavra(arraylist, "for")) {
                For(arraylist);
            } else if (compararPalavra(arraylist, "while")) {
                if (limites(arraylist)) {
                    if (compararPalavra(arraylist, "(")) {
                        if (limites(arraylist)) {
                            condicao(arraylist);
                        } else {
                            System.out.println(RED + "ESPERAVA-SE POR UMA EXPRESSÃO LÓGICA  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                            numeroErros++;
                        }
                    }
                } else {
                    System.out.println(RED + "ESPERAVA-SE POR ( APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                    numeroErros++;
                }
            } else if (compararPalavra(arraylist, "if")) {
                if (limites(arraylist)) {
                    if (compararPalavra(arraylist, "(")) {
                        if (limites(arraylist)) {
                            condicao(arraylist);
                        } else {
                            System.out.println(RED + "ESPERAVA-SE POR UMA EXPRESSÃO LÓGICA  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                            numeroErros++;
                        }
                    }
                } else {
                    System.out.println(RED + "ESPERAVA-SE POR ( APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                    numeroErros++;
                }
            } else if ((arraylist.get(i).tipo.equals("IDENTIFICADOR")) && (arraylist.get(i + 1).lexema.equals("("))) {
                i = i + 2;
                if (verificarExistencia(arraylist.get(i).lexema)) {
                    if (limites(arraylist)) {
                        chamadaFuncao(arraylist, arraylist.get(i - 2).lexema);
                    } else {
                        System.out.println(RED + "ESPERAVA-SE ALGUM VALOR PARA O PARÂMETRO DA FUNÇÃO OU )  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                        numeroErros++;
                    }
                } else {
                    System.out.println(RED + "ESPERAVA-SE ALGUM VALOR PARA O PARÂMETRO DA FUNÇÃO OU )  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                    numeroErros++;
                }
            } else if (fechaChaveta(arraylist)) {
                if (limites(arraylist)) {
                    //inicialmente aqui está a classe mas não não se sabe o q virá. 
                    controlaEscopo--;
                    redirecionaEscopo(arraylist);
                } else {
                    System.out.println(RED + "ESPERAVA-SE }  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                    numeroErros++;
                }
            } else {
                System.out.println(RED + "ESPERAVA-SE }  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                numeroErros++;
                i++;
            }
        }
    }

    public static void corpoWhile(ArrayList<Token> arraylist) {
        while (i < arraylist.size()) {
            if (pontoEvirgula(arraylist) || compararTipo(arraylist, "COMENTÁRIO") || compararTipo(arraylist, "COMENTÁRIO SIMPLES")) {
                i = i;
            } else if (compararPalavra(arraylist, "public") || compararPalavra(arraylist, "int") || compararPalavra(arraylist, "float") || compararPalavra(arraylist, "double")
                    || compararPalavra(arraylist, "String") || compararPalavra(arraylist, "boolean") || compararPalavra(arraylist, "long")
                    || compararPalavra(arraylist, "short") || compararPalavra(arraylist, "byte")) {
                i--;
                declaracoes(arraylist);
            } else if (compararPalavra(arraylist, "for")) {
                For(arraylist);
            } else if (compararPalavra(arraylist, "while")) {
                if (limites(arraylist)) {
                    if (compararPalavra(arraylist, "(")) {
                        if (limites(arraylist)) {
                            condicao(arraylist);
                        } else {
                            System.out.println(RED + "ESPERAVA-SE POR UMA EXPRESSÃO LÓGICA  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                            numeroErros++;
                        }
                    }
                } else {
                    System.out.println(RED + "ESPERAVA-SE POR ( APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                    numeroErros++;
                }
            } else if (compararPalavra(arraylist, "if")) {
                if (limites(arraylist)) {
                    if (compararPalavra(arraylist, "(")) {
                        if (limites(arraylist)) {
                            condicao(arraylist);
                        } else {
                            System.out.println(RED + "ESPERAVA-SE POR UMA EXPRESSÃO LÓGICA  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                            numeroErros++;
                        }
                    }
                } else {
                    System.out.println(RED + "ESPERAVA-SE POR ( APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                    numeroErros++;
                }
            } else if ((arraylist.get(i).tipo.equals("IDENTIFICADOR")) && (arraylist.get(i + 1).lexema.equals("("))) {
                i = i + 2;
                if (limites(arraylist)) {
                    chamadaFuncao(arraylist, arraylist.get(i - 2).lexema);
                } else {
                    System.out.println(RED + "ESPERAVA-SE ALGUM VALOR PARA O PARÂMETRO DA FUNÇAÕ OU )  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                    numeroErros++;
                }
            } else if (fechaChaveta(arraylist)) {
                if (limites(arraylist)) {
                    controlaEscopo--;
                    redirecionaEscopo(arraylist);
                } else {
                    System.out.println(RED + "ESPERAVA-SE }  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                    numeroErros++;
                }
            } else {
                System.out.println(RED + "ESPERAVA-SE }  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                numeroErros++;
                i++;
            }
        }
    }

    public static void corpoIf(ArrayList<Token> arraylist) {
        while (i < arraylist.size()) {
            if (pontoEvirgula(arraylist) || compararTipo(arraylist, "COMENTÁRIO") || compararTipo(arraylist, "COMENTÁRIO SIMPLES")) {
                i = i;
            } else if (compararPalavra(arraylist, "public") || compararPalavra(arraylist, "int") || compararPalavra(arraylist, "float") || compararPalavra(arraylist, "double")
                    || compararPalavra(arraylist, "String") || compararPalavra(arraylist, "boolean") || compararPalavra(arraylist, "long")
                    || compararPalavra(arraylist, "short") || compararPalavra(arraylist, "byte")) {
                i--;
                declaracoes(arraylist);
            } else if (compararPalavra(arraylist, "for")) {
                For(arraylist);
            } else if (compararPalavra(arraylist, "while")) {
                If = false;
                if (limites(arraylist)) {
                    if (compararPalavra(arraylist, "(")) {
                        if (limites(arraylist)) {
                            condicao(arraylist);
                        } else {
                            System.out.println(RED + "ESPERAVA-SE POR UMA EXPRESSÃO LÓGICA  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                            numeroErros++;
                        }
                    }
                } else {
                    System.out.println(RED + "ESPERAVA-SE POR ( APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                    numeroErros++;
                }
            } else if (compararPalavra(arraylist, "if")) {
                if (limites(arraylist)) {
                    if (compararPalavra(arraylist, "(")) {
                        if (limites(arraylist)) {
                            condicao(arraylist);
                        } else {
                            System.out.println(RED + "ESPERAVA-SE POR UMA EXPRESSÃO LÓGICA  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                            numeroErros++;
                        }
                    }
                } else {
                    System.out.println(RED + "ESPERAVA-SE POR ( APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                    numeroErros++;
                }
            } else if ((arraylist.get(i).tipo.equals("IDENTIFICADOR")) && (arraylist.get(i + 1).lexema.equals("("))) {
                i = i + 2;
                if (limites(arraylist)) {
                    chamadaFuncao(arraylist, arraylist.get(i - 2).lexema);
                } else {
                    System.out.println(RED + "ESPERAVA-SE ALGUM VALOR PARA O PARÂMETRO DA FUNÇAÕ OU )  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                    numeroErros++;
                }
            } else if (fechaChaveta(arraylist)) {
                If = false;
                if (limites(arraylist)) {
                    if (compararPalavra(arraylist, "else")) {
                        if (limites(arraylist)) {
                            if (compararPalavra(arraylist, "if")) {
                                If = true;
                                if (limites(arraylist)) {
                                    if (compararPalavra(arraylist, "(")) {
                                        if (limites(arraylist)) {
                                            condicao(arraylist);
                                        } else {
                                            System.out.println(RED + "ESPERAVA-SE POR UMA EXPRESSÃO LÓGICA  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                            numeroErros++;
                                        }
                                    }
                                } else {
                                    System.out.println(RED + "ESPERAVA-SE POR ( APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                    numeroErros++;
                                }
                            } else {
                                if (limites(arraylist)) {
                                    condicao(arraylist);
                                } else {
                                    System.out.println(RED + "ESPERAVA-SE ( APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                                    numeroErros++;
                                }
                            }
                        } else {
                            System.out.println(RED + "ESPERAVA-SE if, { OU UM COMANDO ÚNICO APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                            numeroErros++;
                        }
                    } else {
                        controlaEscopo--;
                        redirecionaEscopo(arraylist);
                    }
                } else {
                    System.out.println(RED + "ESPERAVA-SE } OU ALGUM OUTRO TOKEN APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                    numeroErros++;
                }
            } else {
                System.out.println(RED + "ESPERAVA-SE }  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                numeroErros++;
                i++;
            }
        }
    }

    public static void declaracaoVariaveis(ArrayList<Token> arraylist, String tipo) {
        String lexema = "";
        if (tipo.equals("NÚMERO INTEIRO")) {
            lexema = "int";
        } else if (tipo.equals("String")) {
            lexema = "String";
        } else if (tipo.equals("NÚMERO REAL")) {
            lexema = "double";
        }

        if (pontoEvirgula(arraylist) || compararTipo(arraylist, "COMENTÁRIO") || compararTipo(arraylist, "COMENTÁRIO SIMPLES")) {
            i = i;
        } else if (ID(arraylist)) {
            lexemaSintatico = i - 1;
            sintax = new Sintaxe("TOK_ID", arraylist.get(lexemaSintatico).lexema, "-", lexema, "-", 0);
            if (limites(arraylist)) {
                if (compararPalavra(arraylist, "=")) {
                    if (limites(arraylist)) {
                        if (compararTipo(arraylist, tipo)) {
                            sintax = new Sintaxe("TOK_ID", arraylist.get(lexemaSintatico).lexema, arraylist.get(i - 1).lexema, lexema, "-", 0);
                            if (limites(arraylist)) {
                                if (pontoEvirgula(arraylist)) {
                                    tabelaAuxiliar.add(sintax);
                                    adicionarNaTabelaSintatica();
                                    ////System.out.println(GREEN + "SUCESSO, VARIÁVEL BEM DECLARADA" + RESET);
                                } else if (compararPalavra(arraylist, ",")) {
                                    if (limites(arraylist)) {
                                        tabelaAuxiliar.add(sintax);
                                        declaracaoVariaveis(arraylist, tipo);
                                    } else {
                                        System.out.println(RED + "ESPERAVA-SE UM IDENTIFICADOR APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                        numeroErros++;
                                        tabelaAuxiliar.clear();
                                    }
                                }
                            } else {
                                System.out.println(RED + "ESPERAVA-SE ; OU , APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                numeroErros++;
                                tabelaAuxiliar.clear();
                            }
                        } else {
                            System.out.println(RED + "ESPERAVA-SE UM DADO DO TIPO " + lexema + " APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                            numeroErros++;
                            i++;
                            tabelaAuxiliar.clear();
                        }
                    } else {
                        System.out.println(RED + "ESPERAVA-SE UM DADO DO TIPO " + lexema + " APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                        numeroErros++;
                        tabelaAuxiliar.clear();
                    }
                } else if (pontoEvirgula(arraylist)) {
                    tabelaAuxiliar.add(sintax);
                    adicionarNaTabelaSintatica();
                    ////System.out.println(GREEN + "SUCESSO, VARIÁVEL BEM DECLARADA" + RESET);
                } else if (compararPalavra(arraylist, ",")) {
                    tabelaAuxiliar.add(sintax);
                    if (limites(arraylist)) {
                        declaracaoVariaveis(arraylist, tipo);
                    } else {
                        System.out.println(RED + "ESPERAVA-SE UM IDENTIFICADOR APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                        numeroErros++;
                        tabelaAuxiliar.clear();
                    }
                } else {
                    System.out.println(RED + "ESPERAVA-SE , OU ; OU = APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                    numeroErros++;
                    tabelaAuxiliar.clear();
                }
            } else {
                System.out.println(RED + "ESPERAVA-SE , OU ; OU = APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                numeroErros++;
                tabelaAuxiliar.clear();
            }
        } else {
            System.out.println(RED + "ESPERAVA-SE UM IDENTIFICADOR APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
            numeroErros++;
            i++;
            tabelaAuxiliar.clear();
        }
    }

    public static void vetor(ArrayList<Token> arraylist, String tipo) {
        String lexema = "";
        if (compararPalavra(arraylist, "new")) {
            if (tipo.equals("NÚMERO INTEIRO")) {
                lexema = "int";
            } else if (tipo.equals("String")) {
                lexema = "String";
            } else if (tipo.equals("NÚMERO REAL")) {
                lexema = "double";
            }

            if (compararTipo(arraylist, "COMENTÁRIO") || compararTipo(arraylist, "COMENTÁRIO SIMPLES")) {
                i = i;
            } else if (limites(arraylist)) {
                if (compararPalavra(arraylist, lexema)) {
                    if (limites(arraylist)) {
                        if (abreParentesesRetos(arraylist)) {
                            if (limites(arraylist)) {
                                if (compararTipo(arraylist, "NÚMERO INTEIRO")) {

                                } else {
                                    System.out.println(RED + "ESPERAVA-SE O TAMANHO DO VETOR APÓS O LEXEMA " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                    numeroErros++;
                                    i++;
                                }
                            } else {
                                System.out.println(RED + "ESPERAVA-SE O TAMANHO DO VETOR APÓS O LEXEMA " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                numeroErros++;
                            }
                        } else {
                            System.out.println(RED + "ESPERAVA-SE [ APÓS O LEXEMA " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                            numeroErros++;
                            i++;
                        }
                    } else {
                        System.out.println(RED + "ESPERAVA-SE [ APÓS O LEXEMA " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                        numeroErros++;
                    }
                } else {
                    System.out.println(RED + "ESPERAVA-SE " + lexema + " APÓS O LEXEMA " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                    numeroErros++;
                    i++;
                }
            } else {
                System.out.println(RED + "ESPERAVA-SE " + lexema + " APÓS O LEXEMA " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                numeroErros++;
            }
        } else if (compararPalavra(arraylist, "{")) {
            if (tipo.equals("NÚMERO INTEIRO")) {
                lexema = "int";
            } else if (tipo.equals("String")) {
                lexema = "String";
            } else if (tipo.equals("NÚMERO REAL")) {
                lexema = "double";
            }
            if (limites(arraylist)) {
                if (compararTipo(arraylist, tipo)) {
                    if (limites(arraylist)) {
                        if (compararPalavra(arraylist, ",")) {
                            loop = true;
                            if (limites(arraylist)) {
                                vetor(arraylist, tipo);
                            } else {
                                System.out.println(RED + "ESPERAVA-SE UM DADO DO TIPO " + lexema + " APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                numeroErros++;
                            }
                        } else if (compararPalavra(arraylist, "}")) {
                            if (limites(arraylist)) {
                                if (compararPalavra(arraylist, ";")) {
                                    ////System.out.println(GREEN + "SUCESSO, VETOR BEM DECLARADO" + RESET);
                                } else {
                                    System.out.println(RED + "ESPERAVA-SE ; APÓS O LEXEMA " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                    numeroErros++;
                                    //i++;
                                }
                            } else {
                                System.out.println(RED + "ESPERAVA-SE ;" + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                numeroErros++;
                            }
                        } else {
                            System.out.println(RED + "ESPERAVA-SE , OU } " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                            numeroErros++;
                            i++;
                        }
                    } else {
                        System.out.println(RED + "ESPERAVA-SE , OU } " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                        numeroErros++;
                    }
                } else {
                    System.out.println(RED + "TIPO DE DADOS INESPERADO" + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                    numeroErros++;
                    i++;
                }
            } else {
                System.out.println(RED + "ESPERAVA-SE ALGUM VALOR " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                numeroErros++;
            }
        } else if (loop) {
            if (tipo.equals("NÚMERO INTEIRO")) {
                lexema = "int";
            } else if (tipo.equals("String")) {
                lexema = "String";
            } else if (tipo.equals("NÚMERO REAL")) {
                lexema = "double";
            }
            if (limites(arraylist)) {
                if ((compararTipo(arraylist, tipo))) {
                    if (limites(arraylist)) {
                        if (compararPalavra(arraylist, ",")) {
                            vetor(arraylist, tipo);
                        } else if (compararPalavra(arraylist, "}")) {
                            if (limites(arraylist)) {
                                if (pontoEvirgula(arraylist)) {
                                    ////System.out.println(GREEN + "SUCESSO, VETOR BEM DECLARADO" + RESET);
                                    loop = false;
                                } else if (compararPalavra(arraylist, ",")) {
                                    declaracaoVariaveis(arraylist, tipo);
                                } else {
                                    System.out.println(RED + "ESPERAVA-SE ; APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                    numeroErros++;
                                }
                            } else {
                                System.out.println(RED + "ESPERAVA-SE ; APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                numeroErros++;
                            }
                        } else {
                            System.out.println(RED + "ESPERAVA-SE , OU } APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                            numeroErros++;
                        }
                    } else {
                        System.out.println(RED + "ESPERAVA-SE , OU } APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                        numeroErros++;
                    }
                } else {
                    System.out.println(RED + "ESPERAVA-SE UM DADO DO TIPO " + lexema + " APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                    numeroErros++;
                    i++;
                }
            } else {
                System.out.println(RED + "ESPERAVA-SE UM DADO DO TIPO " + lexema + " APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                numeroErros++;
            }
        } else {
            System.out.println(RED + "EXPRESSÃO INESPERADA, ESPERAVA-SE NEW OU { " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
            numeroErros++;
            i++;
        }
    }

    public static void matriz(ArrayList<Token> arraylist, String tipo) {
        if (limites(arraylist)) {
            String lexema = "";
            if (compararPalavra(arraylist, "new")) {
                if (tipo.equals("NÚMERO INTEIRO")) {
                    lexema = "int";
                } else if (tipo.equals("String")) {
                    lexema = "String";
                } else if (tipo.equals("NÚMERO REAL")) {
                    lexema = "double";
                }

                if (compararTipo(arraylist, "COMENTÁRIO") || compararTipo(arraylist, "COMENTÁRIO SIMPLES")) {
                    i = i;
                } else if (limites(arraylist)) {
                    if (compararPalavra(arraylist, lexema)) {
                        if (limites(arraylist)) {
                            if (abreParentesesRetos(arraylist)) {
                                if (limites(arraylist)) {
                                    if (compararTipo(arraylist, "NÚMERO INTEIRO")) {

                                    } else {
                                        System.out.println(RED + "ESPERAVA-SE O TAMANHO DO VETOR APÓS O LEXEMA " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                        numeroErros++;
                                        i++;
                                    }
                                } else {
                                    System.out.println(RED + "ESPERAVA-SE O TAMANHO DO VETOR APÓS O LEXEMA " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                    numeroErros++;
                                }
                            } else {
                                System.out.println(RED + "ESPERAVA-SE [ APÓS O LEXEMA " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                numeroErros++;
                                i++;
                            }
                        } else {
                            System.out.println(RED + "ESPERAVA-SE [ APÓS O LEXEMA " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                            numeroErros++;
                        }
                    } else {
                        System.out.println(RED + "ESPERAVA-SE " + lexema + " APÓS O LEXEMA " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                        numeroErros++;
                        i++;
                    }
                } else {
                    System.out.println(RED + "ESPERAVA-SE " + lexema + " APÓS O LEXEMA " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                    numeroErros++;
                }
            } else if (compararPalavra(arraylist, "{")) {
                if (tipo.equals("NÚMERO INTEIRO")) {
                    lexema = "int";
                } else if (tipo.equals("String")) {
                    lexema = "String";
                } else if (tipo.equals("NÚMERO REAL")) {
                    lexema = "double";
                }
                if (limites(arraylist)) {
                    if (compararTipo(arraylist, tipo)) {
                        if (limites(arraylist)) {
                            if (compararPalavra(arraylist, ",")) {
                                loop = true;
                                if (limites(arraylist)) {
                                    vetor(arraylist, tipo);
                                } else {
                                    System.out.println(RED + "ESPERAVA-SE UM DADO DO TIPO " + lexema + " APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                    numeroErros++;
                                }
                            } else if (compararPalavra(arraylist, "}")) {
                                if (limites(arraylist)) {
                                    if (compararPalavra(arraylist, ";")) {
                                        ////System.out.println(GREEN + "SUCESSO, VETOR BEM DECLARADO" + RESET);
                                    } else {
                                        System.out.println(RED + "ESPERAVA-SE ; APÓS O LEXEMA " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                        numeroErros++;
                                        //i++;
                                    }
                                } else {
                                    System.out.println(RED + "ESPERAVA-SE ;" + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                    numeroErros++;
                                }
                            } else {
                                System.out.println(RED + "ESPERAVA-SE , OU } " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                numeroErros++;
                                i++;
                            }
                        } else {
                            System.out.println(RED + "ESPERAVA-SE , OU } " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                            numeroErros++;
                        }
                    } else {
                        System.out.println(RED + "TIPO DE DADOS INESPERADO" + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                        numeroErros++;
                        i++;
                    }
                } else {
                    System.out.println(RED + "ESPERAVA-SE ALGUM VALOR " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                    numeroErros++;
                }
            } else if (loop) {
                if (tipo.equals("NÚMERO INTEIRO")) {
                    lexema = "int";
                } else if (tipo.equals("String")) {
                    lexema = "String";
                } else if (tipo.equals("NÚMERO REAL")) {
                    lexema = "double";
                }
                if (limites(arraylist)) {
                    if ((compararTipo(arraylist, tipo))) {
                        if (limites(arraylist)) {
                            if (compararPalavra(arraylist, ",")) {
                                vetor(arraylist, tipo);
                            } else if (compararPalavra(arraylist, "}")) {
                                if (limites(arraylist)) {
                                    if (pontoEvirgula(arraylist)) {
                                        ////System.out.println(GREEN + "SUCESSO, VETOR BEM DECLARADO" + RESET);
                                        loop = false;
                                    } else if (compararPalavra(arraylist, ",")) {
                                        declaracaoVariaveis(arraylist, tipo);
                                    } else {
                                        System.out.println(RED + "ESPERAVA-SE ; APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                        numeroErros++;
                                    }
                                } else {
                                    System.out.println(RED + "ESPERAVA-SE ; APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                    numeroErros++;
                                }
                            } else {
                                System.out.println(RED + "ESPERAVA-SE , OU } APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                                numeroErros++;
                            }
                        } else {
                            System.out.println(RED + "ESPERAVA-SE , OU } APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                            numeroErros++;
                        }
                    } else {
                        System.out.println(RED + "ESPERAVA-SE UM DADO DO TIPO " + lexema + " APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                        numeroErros++;
                        i++;
                    }
                } else {
                    System.out.println(RED + "ESPERAVA-SE UM DADO DO TIPO " + lexema + " APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                    numeroErros++;
                }
            } else {
                System.out.println(RED + "EXPRESSÃO INESPERADA, ESPERAVA-SE NEW OU { " + arraylist.get(i - 1).lexema + " NA LINHA " + arraylist.get(i - 1).linha + RESET);
                numeroErros++;
                i++;
            }
        }
    }

    //ainda falta acrescer algo
    public static void condicao(ArrayList<Token> arraylist) {
        if (compararTipo(arraylist, "COMENTÁRIO") || compararTipo(arraylist, "COMENTÁRIO SIMPLES")) {
            i = i;
        } else if (arraylist.get(i).lexema.equals("!")) {
            i++;
            condicao(arraylist);
        } else if ((arraylist.get(i).tipo.equals("NÚMERO INTEIRO") || arraylist.get(i).tipo.equals("NÚMERO REAL")) && !(arraylist.get(i - 1).lexema.equals("!"))) {
            i++;
            if (i < arraylist.size()) {
                //aqui é depois de aparecer um id
                if (arraylist.get(i).lexema.equals(")")) {
                    i++;
                    if (limites(arraylist)) {
                        if (abreChaveta(arraylist)) {
                            controlaEscopo++;
                            if (limites(arraylist)) {
                                ////System.out.println(GREEN + "ENTRANDO NO CORPO DO WHILE" + RESET);
                                if (If) {
                                    caminhos.add("if");
                                    corpoIf(arraylist);
                                } else {
                                    caminhos.add("while");
                                    corpoWhile(arraylist);
                                }
                            } else {
                                System.out.println(RED + "ESPERAVA-SE }  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                                numeroErros++;
                                i++;
                            }
                        } else {
                            System.out.println(RED + "ESPERAVA-SE {  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                            numeroErros++;
                            i++;
                        }
                    } else {
                        System.out.println(RED + "ESPERAVA-SE {  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                        numeroErros++;
                        i++;
                    }
                } else if (arraylist.get(i).lexema.equals(">") || arraylist.get(i).lexema.equals("<") || arraylist.get(i).lexema.equals(">=") || arraylist.get(i).lexema.equals("<=") || arraylist.get(i).lexema.equals("==") || arraylist.get(i).lexema.equals("!=")) {
                    i++;
                    if (i < arraylist.size()) {
                        //após ter aparecido uma condição
                        if (arraylist.get(i).tipo.equals("NÚMERO INTEIRO") || arraylist.get(i).tipo.equals("NÚMERO REAL")) {
                            i++;
                            if (i < arraylist.size()) {
                                if (arraylist.get(i).lexema.equals("||") || arraylist.get(i).lexema.equals("&&")) {
                                    condicao(arraylist);
                                } else if (arraylist.get(i).lexema.equals(")")) {
                                    i++;
                                    if (limites(arraylist)) {
                                        if (abreChaveta(arraylist)) {
                                            controlaEscopo++;
                                            if (limites(arraylist)) {
                                                ////System.out.println(GREEN + "ENTRANDO NO CORPO DO WHILE" + RESET);
                                                if (If) {
                                                    caminhos.add("if");
                                                    corpoIf(arraylist);
                                                } else {
                                                    caminhos.add("while");
                                                    corpoWhile(arraylist);
                                                }
                                            } else {
                                                System.out.println(RED + "ESPERAVA-SE }  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                                                numeroErros++;
                                                i++;
                                            }
                                        } else {
                                            System.out.println(RED + "ESPERAVA-SE {  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                                            numeroErros++;
                                            i++;
                                        }
                                    } else {
                                        System.out.println(RED + "ESPERAVA-SE {  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                                        numeroErros++;
                                        i++;
                                    }
                                }
                            } else {
                                System.out.println(RED + "esperava-se ) após a expressão " + arraylist.get(i).lexema + " na linha " + arraylist.get(i).linha + RESET);
                                i++;
                                numeroErros++;
                            }
                        } else if (arraylist.get(i).tipo.equals("IDENTIFICADOR")) {
                            i++;
                            if (i < arraylist.size()) {
                                if (arraylist.get(i).lexema.equals("||") || arraylist.get(i).lexema.equals("&&")) {
                                    condicao(arraylist);
                                } else if (arraylist.get(i).lexema.equals(")")) {
                                    i++;
                                    if (limites(arraylist)) {
                                        if (abreChaveta(arraylist)) {
                                            controlaEscopo++;
                                            if (limites(arraylist)) {
                                                ////System.out.println(GREEN + "ENTRANDO NO CORPO DO WHILE" + RESET);
                                                if (If) {
                                                    caminhos.add("if");
                                                    corpoIf(arraylist);
                                                } else {
                                                    caminhos.add("while");
                                                    corpoWhile(arraylist);
                                                }
                                            } else {
                                                System.out.println(RED + "ESPERAVA-SE }  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                                                numeroErros++;
                                                i++;
                                            }
                                        } else {
                                            System.out.println(RED + "ESPERAVA-SE {  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                                            numeroErros++;
                                            i++;
                                        }
                                    } else {
                                        System.out.println(RED + "ESPERAVA-SE {  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                                        numeroErros++;
                                        i++;
                                    }
                                }
                            } else {
                                System.out.println(RED + "esperava-se ) após a expressão " + arraylist.get(i).lexema + " na linha " + arraylist.get(i).linha + RESET);
                                i++;
                                numeroErros++;
                            }
                        } else {
                            System.out.println(RED + "esperava-se uma variável ou número após a expressão" + arraylist.get(i).lexema + " na linha " + arraylist.get(i).linha + RESET);
                            i++;
                            numeroErros++;
                        }
                    } else {
                        System.out.println(RED + "esperava-se uma variável ou número após a expressão " + arraylist.get(i).lexema + " na linha " + arraylist.get(i).linha + RESET);
                        numeroErros++;
                        i++;
                    }
                } else {
                    System.out.println(RED + "token inesperado " + arraylist.get(i).lexema + " na linha " + arraylist.get(i).linha + RESET);
                    numeroErros++;
                    i++;
                }
            } else {
                System.out.println(RED + "Esperava-se ) ou um sinal de comparação apôs a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
                numeroErros++;
                i++;
            }
        } else if (arraylist.get(i).tipo.equals("IDENTIFICADOR")) {
            i++;
            if (i < arraylist.size()) {
                //aqui é depois de aparecer um id
                if (arraylist.get(i).lexema.equals(")")) {
                    i++;
                    if (limites(arraylist)) {
                        if (abreChaveta(arraylist)) {
                            controlaEscopo++;
                            if (limites(arraylist)) {
                                ////System.out.println(GREEN + "ENTRANDO NO CORPO DO WHILE" + RESET);
                                if (If) {
                                    caminhos.add("if");
                                    corpoIf(arraylist);
                                } else {
                                    caminhos.add("while");
                                    corpoWhile(arraylist);
                                }
                            } else {
                                System.out.println(RED + "ESPERAVA-SE }  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                                numeroErros++;
                                i++;
                            }
                        } else {
                            System.out.println(RED + "ESPERAVA-SE {  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                            numeroErros++;
                            i++;
                        }
                    } else {
                        System.out.println(RED + "ESPERAVA-SE {  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                        numeroErros++;
                        i++;
                    }
                } else if (arraylist.get(i).lexema.equals(">") || arraylist.get(i).lexema.equals("<") || arraylist.get(i).lexema.equals(">=") || arraylist.get(i).lexema.equals("<=") || arraylist.get(i).lexema.equals("==") || arraylist.get(i).lexema.equals("!=")) {
                    i++;
                    if (i < arraylist.size()) {
                        //após ter aparecido uma condição
                        if (arraylist.get(i).tipo.equals("NÚMERO INTEIRO") || arraylist.get(i).tipo.equals("NÚMERO REAL")) {
                            i++;
                            if (i < arraylist.size()) {
                                if (arraylist.get(i).lexema.equals("||") || arraylist.get(i).lexema.equals("&&")) {
                                    condicao(arraylist);
                                } else if (arraylist.get(i).lexema.equals(")")) {
                                    i++;
                                    if (limites(arraylist)) {
                                        if (abreChaveta(arraylist)) {
                                            controlaEscopo++;
                                            if (limites(arraylist)) {
                                                ////System.out.println(GREEN + "ENTRANDO NO CORPO DO WHILE" + RESET);
                                                if (If) {
                                                    caminhos.add("if");
                                                    corpoIf(arraylist);
                                                } else {
                                                    caminhos.add("while");
                                                    corpoWhile(arraylist);
                                                }
                                            } else {
                                                System.out.println(RED + "ESPERAVA-SE }  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                                                numeroErros++;
                                                i++;
                                            }
                                        } else {
                                            System.out.println(RED + "ESPERAVA-SE {  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                                            numeroErros++;
                                            i++;
                                        }
                                    } else {
                                        System.out.println(RED + "ESPERAVA-SE {  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                                        numeroErros++;
                                        i++;
                                    }
                                }
                            } else {
                                System.out.println(RED + "esperava-se ) " + arraylist.get(i).lexema + " na linha " + arraylist.get(i).linha + RESET);
                                i++;
                                numeroErros++;
                            }
                        } else if (arraylist.get(i).tipo.equals("IDENTIFICADOR")) {
                            i++;
                            if (i < arraylist.size()) {
                                if (arraylist.get(i).lexema.equals("||") || arraylist.get(i).lexema.equals("&&")) {
                                    condicao(arraylist);
                                } else if (arraylist.get(i).lexema.equals(")")) {
                                    i++;
                                    if (limites(arraylist)) {
                                        if (abreChaveta(arraylist)) {
                                            controlaEscopo++;
                                            if (limites(arraylist)) {
                                                ////System.out.println(GREEN + "ENTRANDO NO CORPO DO WHILE" + RESET);
                                                if (If) {
                                                    caminhos.add("if");
                                                    corpoIf(arraylist);
                                                } else {
                                                    caminhos.add("while");
                                                    corpoWhile(arraylist);
                                                }
                                            } else {
                                                System.out.println(RED + "ESPERAVA-SE }  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                                                numeroErros++;
                                                i++;
                                            }
                                        } else {
                                            System.out.println(RED + "ESPERAVA-SE {  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                                            numeroErros++;
                                            i++;
                                        }
                                    } else {
                                        System.out.println(RED + "ESPERAVA-SE {  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                                        numeroErros++;
                                        i++;
                                    }
                                }
                            } else {
                                System.out.println(RED + "esperava-se ) após a expressão " + arraylist.get(i).lexema + " na linha " + arraylist.get(i).linha + RESET);
                                i++;
                                numeroErros++;
                            }
                        } else {
                            System.out.println(RED + "esperava-se uma variável ou número " + arraylist.get(i).lexema + " na linha " + arraylist.get(i).linha + RESET);
                            numeroErros++;
                            i++;
                        }
                    } else {
                        System.out.println(RED + "esperava-se uma variável ou número " + arraylist.get(i).lexema + " na linha " + arraylist.get(i).linha + RESET);
                        numeroErros++;
                        i++;
                    }
                } else {
                    System.out.println(RED + "token inesperado " + arraylist.get(i).lexema + " na linha " + arraylist.get(i).linha + RESET);
                    numeroErros++;
                    i++;
                }
            } else {
                System.out.println(RED + "Esperava-se ) ou um sinal de comparação apôs a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
                numeroErros++;
                i++;
            }
        } else {
            System.out.println(RED + "Esperava-se uma variável ou boolean apôs a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
            numeroErros++;
            i++;
        }
    }

    public static void condicaoFor(ArrayList<Token> arraylist) {
        if (compararTipo(arraylist, "COMENTÁRIO") || compararTipo(arraylist, "COMENTÁRIO SIMPLES")) {
            i = i;
        } else if (arraylist.get(i).lexema.equals("!")) {
            i++;
            condicaoFor(arraylist);
        } else if ((arraylist.get(i).tipo.equals("NÚMERO INTEIRO") || arraylist.get(i).tipo.equals("NÚMERO REAL")) && !(arraylist.get(i - 1).lexema.equals("!"))) {
            i++;
            if (i < arraylist.size()) {
                //aqui é depois de aparecer um id
                if (arraylist.get(i).lexema.equals(";")) {
                    ////System.out.println(GREEN + "Segundo argumento for correcto" + RESET);
                    i++;
                } else if (arraylist.get(i).lexema.equals(">") || arraylist.get(i).lexema.equals("<") || arraylist.get(i).lexema.equals(">=") || arraylist.get(i).lexema.equals("<=") || arraylist.get(i).lexema.equals("==") || arraylist.get(i).lexema.equals("!=")) {
                    i++;
                    if (i < arraylist.size()) {
                        //após ter aparecido uma condição
                        if (arraylist.get(i).tipo.equals("NÚMERO INTEIRO") || arraylist.get(i).tipo.equals("NÚMERO REAL")) {
                            i++;
                            if (i < arraylist.size()) {
                                if (arraylist.get(i).lexema.equals("||") || arraylist.get(i).lexema.equals("&&")) {
                                    condicaoFor(arraylist);
                                } else if (arraylist.get(i).lexema.equals(";")) {
                                    ////System.out.println(GREEN + "Segundo argumento for correcto" + RESET);
                                    i++;
                                }
                            } else {
                                System.out.println(RED + "esperava-se ) " + arraylist.get(i).lexema + " na linha " + arraylist.get(i).linha + RESET);
                                numeroErros++;
                                i++;
                            }
                        } else if (arraylist.get(i).tipo.equals("IDENTIFICADOR")) {
                            i++;
                            if (i < arraylist.size()) {
                                if (arraylist.get(i).lexema.equals("||") || arraylist.get(i).lexema.equals("&&")) {
                                    condicao(arraylist);
                                } else if (arraylist.get(i).lexema.equals(";")) {
                                    ////System.out.println(GREEN + "Segundo argumento for correcto" + RESET);
                                    i++;
                                }
                            } else {
                                System.out.println(RED + "esperava-se ) após a expressão " + arraylist.get(i).lexema + " na linha " + arraylist.get(i).linha + RESET);
                                numeroErros++;
                            }
                        } else {
                            System.out.println(RED + "esperava-se uma variável ou número " + arraylist.get(i).lexema + " na linha " + arraylist.get(i).linha + RESET);
                            numeroErros++;
                        }
                    } else {
                        System.out.println(RED + "esperava-se uma variável ou número " + arraylist.get(i).lexema + " na linha " + arraylist.get(i).linha + RESET);
                        numeroErros++;
                    }
                } else {
                    System.out.println(RED + "token inesperado " + arraylist.get(i).lexema + " na linha " + arraylist.get(i).linha + RESET);
                    numeroErros++;
                }
            } else {
                System.out.println(RED + "Esperava-se ) ou um sinal de comparação apôs a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
                numeroErros++;
            }
        } else if (arraylist.get(i).tipo.equals("IDENTIFICADOR")) {
            i++;
            if (i < arraylist.size()) {
                //aqui é depois de aparecer um id
                if (arraylist.get(i).lexema.equals(";")) {
                    ////System.out.println(GREEN + "Segundo argumento for correcto" + RESET);
                    i++;
                } else if (arraylist.get(i).lexema.equals(">") || arraylist.get(i).lexema.equals("<") || arraylist.get(i).lexema.equals(">=") || arraylist.get(i).lexema.equals("<=") || arraylist.get(i).lexema.equals("==") || arraylist.get(i).lexema.equals("!=")) {
                    i++;
                    if (i < arraylist.size()) {
                        //após ter aparecido uma condição
                        if (arraylist.get(i).tipo.equals("NÚMERO INTEIRO") || arraylist.get(i).tipo.equals("NÚMERO REAL")) {
                            i++;
                            if (i < arraylist.size()) {
                                if (arraylist.get(i).lexema.equals("||") || arraylist.get(i).lexema.equals("&&")) {
                                    condicaoFor(arraylist);
                                } else if (arraylist.get(i).lexema.equals(";")) {
                                    ////System.out.println(GREEN + "Segundo argumento for correcto" + RESET);
                                    i++;
                                }
                            } else {
                                System.out.println(RED + "esperava-se ) " + arraylist.get(i).lexema + " na linha " + arraylist.get(i).linha + RESET);
                                numeroErros++;
                            }
                        } else {
                            System.out.println(RED + "esperava-se uma variável ou número " + arraylist.get(i).lexema + " na linha " + arraylist.get(i).linha + RESET);
                            numeroErros++;
                        }
                    } else {
                        System.out.println(RED + "esperava-se uma variável ou número " + arraylist.get(i).lexema + " na linha " + arraylist.get(i).linha + RESET);
                        numeroErros++;
                    }
                } else {
                    System.out.println(RED + "token inesperado " + arraylist.get(i).lexema + " na linha " + arraylist.get(i).linha + RESET);
                    numeroErros++;
                }
            } else {
                System.out.println(RED + "Esperava-se ) ou um sinal de comparação apôs a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
                numeroErros++;
            }
        } else {
            System.out.println(RED + "Esperava-se uma variável ou boolean apôs a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
            numeroErros++;
            i++;
        }
    }

    public static void incrementoFor(ArrayList<Token> arraylist) {
        if (compararTipo(arraylist, "COMENTÁRIO") || compararTipo(arraylist, "COMENTÁRIO SIMPLES")) {
            i = i;
        } else if (i < arraylist.size()) {
            if (arraylist.get(i).tipo.equals("IDENTIFICADOR")) {
                i++;
                if (i < arraylist.size()) {
                    if (arraylist.get(i).lexema.equals("++") || arraylist.get(i).lexema.equals("--")) {
                        i++;
                        if (i < arraylist.size()) {
                            if (arraylist.get(i).lexema.equals(")")) {
                                i++;
                                ////System.out.println(GREEN + "FOR terminado com sucesso");
                                if (limites(arraylist)) {
                                    if (abreChaveta(arraylist)) {
                                        controlaEscopo++;
                                        if (limites(arraylist)) {
                                            caminhos.add("for");
                                            corpoFor(arraylist);
                                        } else {
                                            System.out.println(RED + "ESPERAVA-SE }  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                                            numeroErros++;
                                        }
                                    } else {
                                        System.out.println(RED + "ESPERAVA-SE {  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                                        numeroErros++;
                                    }
                                } else {
                                    System.out.println(RED + "ESPERAVA-SE {  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                                    numeroErros++;
                                }
                            } else {
                                System.out.println(RED + "Esperava-se ) apôs a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
                                numeroErros++;
                            }
                        } else {
                            System.out.println(RED + "Esperava-se um incremento ou decremento apôs a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
                            numeroErros++;
                        }
                    } else if (arraylist.get(i).lexema.equals("+=") || arraylist.get(i).lexema.equals("-=") || arraylist.get(i).lexema.equals("*=") || arraylist.get(i).lexema.equals("/=")) {
                        i++;
                        if (i < arraylist.size()) {
                            if (arraylist.get(i).tipo.equals("IDENTIFICADOR") || arraylist.get(i).tipo.equals("NÚMERO INTEIRO")) {
                                i++;
                                if (i < arraylist.size()) {
                                    if (arraylist.get(i).lexema.equals(")")) {
                                        i++;
                                        ////System.out.println(GREEN + "FOR terminado com sucesso");
                                        if (limites(arraylist)) {
                                            if (abreChaveta(arraylist)) {
                                                controlaEscopo++;
                                                if (limites(arraylist)) {
                                                    caminhos.add("for");
                                                    corpoFor(arraylist);
                                                } else {
                                                    System.out.println(RED + "ESPERAVA-SE }  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                                                    numeroErros++;
                                                }
                                            } else {
                                                System.out.println(RED + "ESPERAVA-SE {  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                                                numeroErros++;
                                            }
                                        } else {
                                            System.out.println(RED + "ESPERAVA-SE {  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                                            numeroErros++;
                                        }
                                    } else {
                                        System.out.println(RED + "Esperava-se ) apôs a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
                                        numeroErros++;
                                    }
                                } else {
                                    System.out.println(RED + "Esperava-se ) apôs a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
                                    numeroErros++;
                                }
                            } else {
                                System.out.println(RED + "Esperava-se ) apôs a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
                                numeroErros++;
                            }
                        } else {
                            System.out.println(RED + "Esperava-se um incremento ou decremento apôs a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
                            numeroErros++;
                        }
                    } else if (arraylist.get(i).lexema.equals("=")) {
                        i++;
                        operacaoFor(arraylist);
                    } else {
                        System.out.println(RED + "Esperava-se um incremento, decremento ou operação apôs a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
                        numeroErros++;
                    }
                } else {
                    System.out.println(RED + "Esperava-se um identificador apôs a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
                    numeroErros++;
                }
            } else {
                System.out.println(RED + "Esperava-se um identificador, incremento ou operação apôs a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
                numeroErros++;
                i++;
            }
        }

    }

    public static void operacaoFor(ArrayList<Token> arraylist) {
        if (compararTipo(arraylist, "COMENTÁRIO") || compararTipo(arraylist, "COMENTÁRIO SIMPLES")) {
            i = i;
        } else if (i < arraylist.size()) {
            if (arraylist.get(i).tipo.equals("IDENTIFICADOR") || arraylist.get(i).tipo.equals("NÚMERO INTEIRO") || arraylist.get(i).tipo.equals("NÚMERO REAL")) {
                i++;
                if (i < arraylist.size()) {
                    if (arraylist.get(i).lexema.equals("+") || arraylist.get(i).lexema.equals("-") || arraylist.get(i).lexema.equals("/") || arraylist.get(i).lexema.equals("*")) {
                        i++;
                        if (i < arraylist.size()) {
                            if (arraylist.get(i).tipo.equals("IDENTIFICADOR") || arraylist.get(i).tipo.equals("NÚMERO INTEIRO") || arraylist.get(i).tipo.equals("NÚMERO REAL")) {
                                i++;
                                if (i < arraylist.size()) {
                                    if (arraylist.get(i).lexema.equals(")")) {
                                        i++;
                                        ////System.out.println(GREEN + "For terminado com sucesso");
                                        if (limites(arraylist)) {
                                            if (abreChaveta(arraylist)) {
                                                controlaEscopo++;
                                                if (limites(arraylist)) {
                                                    caminhos.add("for");
                                                    corpoFor(arraylist);
                                                } else {
                                                    System.out.println(RED + "ESPERAVA-SE }  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                                                    numeroErros++;
                                                }
                                            } else {
                                                System.out.println(RED + "ESPERAVA-SE {  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                                                numeroErros++;
                                            }
                                        } else {
                                            System.out.println(RED + "ESPERAVA-SE {  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                                            numeroErros++;
                                        }
                                    } else if (arraylist.get(i).lexema.equals("+") || arraylist.get(i).lexema.equals("-") || arraylist.get(i).lexema.equals("/") || arraylist.get(i).lexema.equals("*")) {
                                        i++;
                                        operacaoFor2(arraylist);
                                    } else {
                                        System.out.println(RED + "Esperava-se ) ou um operador apôs a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
                                        numeroErros++;;
                                    }
                                } else {
                                    System.out.println(RED + "Esperava-se ) ou um operador apôs a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
                                    numeroErros++;;
                                }
                            } else {
                                System.out.println(RED + "Esperava-se um ID ou um número apôs a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
                                numeroErros++;;
                            }
                        } else {
                            System.out.println(RED + "Esperava-se um ID ou um número apôs a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
                            numeroErros++;;
                        }
                    } else {
                        System.out.println(RED + "Esperava-se um operador apôs a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
                        numeroErros++;;
                    }
                } else {
                    System.out.println(RED + "Esperava-se um operador apôs a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
                    numeroErros++;;
                }
            } else {
                System.out.println(RED + "Esperava-se um ID ou um número apôs a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
                numeroErros++;;
            }
        } else {
            System.out.println(RED + "Esperava-se um ID ou um número apôs a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
            numeroErros++;;
            i++;
        }
    }

    public static void operacaoFor2(ArrayList<Token> arraylist) {
        if (compararTipo(arraylist, "COMENTÁRIO") || compararTipo(arraylist, "COMENTÁRIO SIMPLES")) {
            i = i;
        } else if (arraylist.get(i).tipo.equals("IDENTIFICADOR") || arraylist.get(i).tipo.equals("NÚMERO INTEIRO") || arraylist.get(i).tipo.equals("NÚMERO REAL")) {
            i++;
            if (i < arraylist.size()) {
                if (arraylist.get(i).lexema.equals(")")) {
                    i++;
                    ////System.out.println(GREEN + "For terminado com sucesso");
                    if (limites(arraylist)) {
                        if (abreChaveta(arraylist)) {
                            controlaEscopo++;
                            if (limites(arraylist)) {
                                caminhos.add("for");
                                corpoFor(arraylist);
                            } else {
                                System.out.println(RED + "ESPERAVA-SE }  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                                numeroErros++;
                            }
                        } else {
                            System.out.println(RED + "ESPERAVA-SE {  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                            numeroErros++;
                        }
                    } else {
                        System.out.println(RED + "ESPERAVA-SE {  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                        numeroErros++;
                    }
                } else if (arraylist.get(i).lexema.equals("+") || arraylist.get(i).lexema.equals("-") || arraylist.get(i).lexema.equals("/") || arraylist.get(i).lexema.equals("*")) {
                    i++;
                    operacaoFor(arraylist);
                } else {
                    System.out.println(RED + "Esperava-se ) ou um operador apôs a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
                    numeroErros++;;
                }
            } else {
                System.out.println(RED + "Esperava-se ) ou um operador apôs a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
                numeroErros++;;
                i++;
            }
        }
    }

    public static void For(ArrayList<Token> arraylist) {
        if (compararTipo(arraylist, "COMENTÁRIO") || compararTipo(arraylist, "COMENTÁRIO SIMPLES")) {
            i = i;
        } else if (i < arraylist.size()) {
            if (arraylist.get(i).lexema.equals("(")) {
                i++;
                if (i < arraylist.size()) {
                    if (arraylist.get(i).lexema.equals("int") || arraylist.get(i).lexema.equals("float") || arraylist.get(i).lexema.equals("double")) {
                        i++;
                        if (arraylist.get(i).tipo.equals("IDENTIFICADOR")) {
                            i++;
                            if (i < arraylist.size()) {
                                if (arraylist.get(i).lexema.equals("=")) {
                                    i++;
                                    if (i < arraylist.size()) {
                                        if (arraylist.get(i).tipo.equals("NÚMERO INTEIRO") || arraylist.get(i).tipo.equals("NÚMERO REAL")) {
                                            i++;
                                            if (i < arraylist.size()) {
                                                if (arraylist.get(i).lexema.equals(";")) {
                                                    i++;
                                                    condicaoFor(arraylist);
                                                    incrementoFor(arraylist);
                                                } else {
                                                    System.out.println(RED + "Esperava-se ; apôs a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
                                                    numeroErros++;;
                                                }
                                            } else {
                                                System.out.println(RED + "Esperava-se ; apôs a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
                                                numeroErros++;;
                                            }
                                        } else {
                                            System.out.println(RED + "Esperava-se um número apôs a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
                                            numeroErros++;;
                                        }
                                    } else {
                                        System.out.println(RED + "Esperava-se um número apôs a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
                                        numeroErros++;;
                                    }
                                } else if (arraylist.get(i).tipo.equals("PONTO E VÍRGULA")) {
                                    if (i < arraylist.size()) {
                                        if (arraylist.get(i).lexema.equals(";")) {
                                            ////System.out.println(GREEN + "for correcto");
                                            i++;
                                            condicaoFor(arraylist);
                                            incrementoFor(arraylist);
                                        } else {
                                            System.out.println(RED + "Esperava-se ; apôs a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
                                            numeroErros++;;
                                        }
                                    }
                                } else if (arraylist.get(i).lexema.equals(":")) {
                                    i++;
                                    if (i < arraylist.size()) {
                                        if (arraylist.get(i).tipo.equals("IDENTIFICADOR")) {
                                            i++;
                                            if (i < arraylist.size()) {
                                                if (arraylist.get(i).lexema.equals(")")) {
                                                    i++;
                                                    ////System.out.println(GREEN + "foreach correcto");
                                                    if (limites(arraylist)) {
                                                        if (abreChaveta(arraylist)) {
                                                            controlaEscopo++;
                                                            if (limites(arraylist)) {
                                                                caminhos.add("for");
                                                                corpoFor(arraylist);
                                                            } else {
                                                                System.out.println(RED + "ESPERAVA-SE }  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                                                                numeroErros++;
                                                            }
                                                        } else {
                                                            System.out.println(RED + "ESPERAVA-SE {  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                                                            numeroErros++;
                                                        }
                                                    } else {
                                                        System.out.println(RED + "ESPERAVA-SE {  APÓS A EXPRESSÃO " + arraylist.get(i - 1).lexema + " NA LINHA " + (arraylist.get(i - 1).linha + 1) + RESET);
                                                        numeroErros++;
                                                    }
                                                } else {
                                                    System.out.println(RED + "Esperava-se ) a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
                                                    numeroErros++;;
                                                }
                                            } else {
                                                System.out.println(RED + "Esperava-se ) a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
                                                numeroErros++;;
                                            }
                                        } else {
                                            System.out.println(RED + "Esperava-se um identificador a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
                                            numeroErros++;;
                                        }
                                    } else {
                                        System.out.println(RED + "Esperava-se um identificador a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
                                        numeroErros++;;
                                    }
                                } else {
                                    System.out.println(RED + "Esperava-se = ou ; apôs a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
                                    numeroErros++;
                                }
                            } else {
                                System.out.println(RED + "Esperava-se = ou ; apôs a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
                                numeroErros++;;
                            }
                        } else {
                            System.out.println(RED + "Esperava-se um identificador apôs a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
                            numeroErros++;;
                        }
                    } else if (arraylist.get(i).tipo.equals("IDENTIFICADOR")) {
                        i++;
                        if (i < arraylist.size()) {
                            if (arraylist.get(i).lexema.equals("=")) {
                                i++;
                                if (i < arraylist.size()) {
                                    if (arraylist.get(i).tipo.equals("NÚMERO INTEIRO") || arraylist.get(i).tipo.equals("NÚMERO REAL")) {
                                        i++;
                                        if (i < arraylist.size()) {
                                            if (arraylist.get(i).lexema.equals(";")) {
                                                ////System.out.println(GREEN + "primeiro argumento do for correcto");
                                                i++;
                                                condicaoFor(arraylist);
                                                incrementoFor(arraylist);
                                            } else {
                                                System.out.println(RED + "Esperava-se ; apôs a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
                                                numeroErros++;;
                                            }
                                        } else {
                                            System.out.println(RED + "Esperava-se ; apôs a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
                                            numeroErros++;;
                                        }
                                    } else {
                                        System.out.println(RED + "Esperava-se um número apôs a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
                                        numeroErros++;;
                                    }
                                } else {
                                    System.out.println(RED + "Esperava-se um número apôs a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
                                    numeroErros++;;
                                }
                            } else if (arraylist.get(i).tipo.equals("PONTO E VÍRGULA")) {
                                if (i < arraylist.size()) {
                                    if (arraylist.get(i).lexema.equals(";")) {
                                        i++;
                                        condicaoFor(arraylist);
                                        incrementoFor(arraylist);
                                    } else {
                                        System.out.println(RED + "Esperava-se ; apôs a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
                                        numeroErros++;
                                    }
                                }
                            } else {
                                System.out.println(RED + "Esperava-se = ou ; apôs a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
                                numeroErros++;
                            }
                        } else {
                            System.out.println(RED + "Esperava-se = ou ; apôs a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
                            numeroErros++;
                        }
                    } else {
                        System.out.println(RED + "Esperava-se declaração de variável ou inicialização de uma variável apôs a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
                        numeroErros++;
                    }
                } else {
                    System.out.println(RED + "Esperava-se declaração de variável ou inicialização de uma variável apôs a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
                    numeroErros++;;
                }
            } else {
                System.out.println(RED + "Esperava-se ( apôs a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
                numeroErros++;;
            }
        } else {
            System.out.println(RED + "Esperava-se ( apôs a expressão " + arraylist.get(i - 1).lexema + " na linha " + arraylist.get(i - 1).linha + RESET);
            numeroErros++;;
            i++;
        }
    }
}