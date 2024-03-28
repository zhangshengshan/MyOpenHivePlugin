parser grammar MyDSLParser;

options {
    tokenVocab = MyDSLLexer;
}

root: relation (JOIN relation)* ;
relation: TB (JOIN TB)*
        | relation JOIN relation
        | LP relation RP ;







