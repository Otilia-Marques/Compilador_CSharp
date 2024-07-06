# Mini-Compilador C#

Este projeto é um mini-compilador desenvolvido em Java que analisa e valida código-fonte escrito em C#. 
Ele inclui analisadores léxico, sintático e semântico, com foco em verificar a declaração e uso de variáveis, tipos de dados, estrutura de controle, e mais.

## Funcionalidades

- **Analisador Léxico**: Tokeniza o código-fonte em C#.
- **Analisador Sintático**: Verifica a estrutura do código conforme a gramática definida.
- **Analisador Semântico**: Valida tipos de dados, declarações de variáveis, expressões booleanas e compatibilidade de parâmetros.

## Pré-requisitos

- **NetBeans IDE**: Certifique-se de ter o NetBeans IDE instalado no seu computador.
- **JDK**: O Java Development Kit (JDK) deve estar instalado e configurado.

## Gramática

A gramática utilizada para a análise sintática é a seguinte:

```
<program> ::= <usingDirective>* <namespaceDeclaration>*

<usingDirective> ::= "using" <namespaceName> ";"

<namespaceDeclaration> ::= "namespace" <namespaceName> "{" <typeDeclaration>* "}"

<namespaceName> ::= <identifier> ("." <identifier>)*

<typeDeclaration> ::= <classDeclaration> | <interfaceDeclaration> | <enumDeclaration> | <delegateDeclaration> | <structDeclaration>

<classDeclaration> ::= <classModifiers> "class" <identifier> <typeParameters>? <baseClass>? <interfaces>? <classBody>

<classModifiers> ::= <classModifier>*

<classModifier> ::= "public" | "private" | "protected" | "internal" | "abstract" | "sealed" | "static" | ...

<typeParameters> ::= "<" <typeParameterList> ">"

<typeParameterList> ::= <identifier> ("," <identifier>)*

<baseClass> ::= ":" <type> <interfaces>?

<interfaces> ::= "," <type> ("," <type>)*

<classBody> ::= "{" <classMember>* "}"

<classMember> ::= <fieldDeclaration> | <methodDeclaration> | <propertyDeclaration> | <constructorDeclaration> | <mainMethod> | <eventDeclaration>

<fieldDeclaration> ::= <modifiers> <type> <variableDeclarators> ";"

<propertyDeclaration> ::= <modifiers> <type> <identifier> "{" <accessorDeclaration> <accessorDeclaration> "}"

<accessorDeclaration> ::= "get" <methodBody> | "set" <methodBody>

<methodDeclaration> ::= <modifiers> <type> <identifier> <typeParameters>? <parameters> <methodBody>

<constructorDeclaration> ::= <modifiers> <identifier> <parameters> <constructorInitializer>? <methodBody>

<constructorInitializer> ::= ":" ("base" | "this") <argumentList>

<argumentList> ::= "(" <expressionList>? ")"

<expressionList> ::= <expression> ("," <expression>)*

<mainMethod> ::= "public" "static" "void" "Main" "(" "string" "[" "]" <identifier> ")" <methodBody>

<modifiers> ::= <modifier>*

<modifier> ::= "public" | "private" | "protected" | "internal" | "static" | "readonly" | "volatile" | ...

<type> ::= <primitiveType> | <classType> | <genericType> | <arrayType>

<primitiveType> ::= "int" | "double" | "bool" | "char" | "string" | ...

<classType> ::= <namespaceName> "." <identifier> | <identifier>

<genericType> ::= <classType> "<" <typeArgumentList> ">"

<typeArgumentList> ::= <type> ("," <type>)*

<arrayType> ::= <type> "[]"

<variableDeclarators> ::= <variableDeclarator> ("," <variableDeclarator>)*

<variableDeclarator> ::= <identifier> ("=" <expression>)?

<parameters> ::= "(" <parameterList>? ")"

<parameterList> ::= <parameter> ("," <parameter>)*

<parameter> ::= <type> <identifier>

<methodBody> ::= "{" <statements>* "}"

<statements> ::= <statement> | <localVariableDeclarationStatement>

<statement> ::= <expressionStatement> | <ifStatement> | <whileStatement> | <forStatement> | <foreachStatement> |
                <switchStatement> | <returnStatement> | <breakStatement> | <tryStatement> | <continueStatement> |
                <throwStatement> | <block> | <lockStatement> | <usingStatement> | <yieldStatement>

<whileStatement> ::= "while" "(" <expression> ")" <statement>

<forStatement> ::= "for" "(" <forInitializer>? ";" <expression>? ";" <forIterator>? ")" <statement>

<forInitializer> ::= <localVariableDeclaration> | <expressionList>

<forIterator> ::= <expressionList>

<foreachStatement> ::= "foreach" "(" <type> <identifier> "in" <expression> ")" <statement>

<switchStatement> ::= "switch" "(" <expression> ")" "{" <switchSection>* "}"

<switchSection> ::= <switchLabel> <statementList>

<switchLabel> ::= "case" <expression> ":" | "default" ":"

<statementList> ::= <statement>*

<returnStatement> ::= "return" <expression>? ";"

<breakStatement> ::= "break" ";"

<continueStatement> ::= "continue" ";"

<tryStatement> ::= "try" <block> <catchClauses> <finallyClause>?

<catchClauses> ::= <catchClause>+

<catchClause> ::= "catch" "(" <type> <identifier>? ")" <block>

<finallyClause> ::= "finally" <block>

<throwStatement> ::= "throw" <expression>? ";"

<lockStatement> ::= "lock" "(" <expression> ")" <statement>

<usingStatement> ::= "using" "(" <localVariableDeclaration> ")" <statement>

<yieldStatement> ::= "yield" ("return" <expression> | "break") ";"

<expressionStatement> ::= <expression> ";"

<localVariableDeclarationStatement> ::= <localVariableDeclaration> ";"

<localVariableDeclaration> ::= <type> <variableDeclarators>

<expression> ::= <assignmentExpression> | <conditionalExpression> | <lambdaExpression> |
                 <logicalOrExpression> | <logicalAndExpression> | <inclusiveOrExpression> | 
                 <exclusiveOrExpression> | <andExpression> | <equalityExpression> | 
                 <relationalExpression> | <shiftExpression> | <additiveExpression> | 
                 <multiplicativeExpression> | <unaryExpression> | <postfixExpression> | <primary>

<assignmentExpression> ::= <unaryExpression> <assignmentOperator> <expression>

<assignmentOperator> ::= "=" | "+=" | "-=" | "*=" | "/=" | "%=" | "&=" | "|=" | "^=" | "<<=" | ">>="

<conditionalExpression> ::= <logicalOrExpression> "?" <expression> ":" <conditionalExpression> | <logicalOrExpression>

<lambdaExpression> ::= <lambdaParameters> "=>" <expression>

<lambdaParameters> ::= <identifier> | "(" <parameterList> ")"

<logicalOrExpression> ::= <logicalAndExpression> ( "||" <logicalAndExpression> )*

<logicalAndExpression> ::= <inclusiveOrExpression> ( "&&" <inclusiveOrExpression> )*

<inclusiveOrExpression> ::= <exclusiveOrExpression> ( "|" <exclusiveOrExpression> )*

<exclusiveOrExpression> ::= <andExpression> ( "^" <andExpression> )*

<andExpression> ::= <equalityExpression> ( "&" <equalityExpression> )*

<equalityExpression> ::= <relationalExpression> ( ("==" | "!=") <relationalExpression> )*

<relationalExpression> ::= <shiftExpression> ( ("<" | ">" | "<=" | ">=") <shiftExpression> )*

<shiftExpression> ::= <additiveExpression> ( ("<<" | ">>") <additiveExpression> )*

<additiveExpression> ::= <multiplicativeExpression> ( ("+" | "-") <multiplicativeExpression> )*

<multiplicativeExpression> ::= <unaryExpression> ( ("*" | "/" | "%") <unaryExpression> )*

<unaryExpression> ::= <unaryOperator> <unaryExpression> | <primary>

<unaryOperator> ::= "+" | "-" | "!" | "~" | "++" | "--"

<postfixExpression> ::= <primary> <postfixOperator>*

<postfixOperator> ::= "++" | "--"

<primary> ::= <literal> | <identifier> | <parenthesizedExpression> | <objectCreationExpression> | 
              <arrayCreationExpression> | <typeOfExpression> | <checkedExpression> | <uncheckedExpression> | 
              <defaultExpression> | <memberAccess> | <invocationExpression> | <elementAccess> | <baseAccess>

<literal> ::= <integerLiteral> | <realLiteral> | <characterLiteral> | <stringLiteral> | <booleanLiteral> | "null"

<identifier> ::= <letter> (<letterOrDigit>)*

<letter> ::= [A-Za-z_]

<letterOrDigit> ::= <letter> | [0-9]

<parenthesizedExpression> ::= "(" <expression> ")"

<objectCreationExpression> ::= "new" <type> <argumentList> <objectOrCollectionInitializer>?

<objectOrCollectionInitializer> ::= "{" <memberInitializerList>? "}"

<memberInitializerList> ::= <memberInitializer> ("," <memberInitializer>)*



<memberInitializer> ::= <identifier> "=" <expression> | <identifier> ":" <expression>

<arrayCreationExpression> ::= "new" <type> "[" <expression> "]" <arrayInitializer>?

<typeOfExpression> ::= "typeof" "(" <type> ")"

<checkedExpression> ::= "checked" "(" <expression> ")"

<uncheckedExpression> ::= "unchecked" "(" <expression> ")"

<defaultExpression> ::= "default" "(" <type> ")"

<memberAccess> ::= <primary> "." <identifier>

<invocationExpression> ::= <primary> "(" <argumentList>? ")"

<elementAccess> ::= <primary> "[" <expressionList> "]"

<baseAccess> ::= "base" "." <identifier>
```

## Erros Comuns

- **Erro de Declaração Duplicada**: Variáveis declaradas mais de uma vez no mesmo escopo.
- **Erro de Atribuição de Tipo**: Variáveis de tipo inteiro recebendo valores string e vice-versa.
- **Erro de Expressão Booleana**: Expressões booleanas inválidas em estruturas de controle (if, for, while, etc.).
- **Erro de Parâmetros de Método**: Incompatibilidade de tipos de parâmetros em chamadas de método.
- **Erro de Formato de Saída/Entrada**: Uso incorreto de especificadores de formato em `printf` e `scanf`.
