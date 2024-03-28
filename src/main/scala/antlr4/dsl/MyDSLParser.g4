parser grammar MyDSLParser;

options {
    tokenVocab = MyDSLLexer;
}

mutilroots: root (SEMICOLON root)* SEMICOLON? ;
root: relation (join relation)* ;
relation: TB (join TB)*
        | relation join relation
        | LP relation RP ;

join: JOIN | LEFT_JOIN | RIGHT_JOIN | FULL_JOIN  ;






