parser grammar MyDSLParser;

options {
    tokenVocab = MyDSLLexer;
}

root: relation ;
relation: TB join TB | TB join module | module join TB ;
join_module : join module ;
module : LEFT_PAREN relation RIGHT_PAREN ;
join : LEFT_JOIN | RIGHT_JOIN | FULL_JOIN ;
