parser grammar MyDSLParser;

options {
    tokenVocab = MyDSLLexer;
}

mutilroots: root (SEMICOLON root)* SEMICOLON? ;
root: relation (JOIN relation)* ;
relation: TB (JOIN TB)*
        | relation JOIN relation
        | LP relation RP ;







