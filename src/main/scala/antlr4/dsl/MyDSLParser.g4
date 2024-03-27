parser grammar MyDSLParser;

options {
    tokenVocab = MyDSLLexer;
}

root:  DB_TB join_module + ;
join_module : join module ;
module : LEFT_PAREN DB_TB LEFT_JOIN DB_TB RIGHT_PAREN ;
join : LEFT_JOIN | RIGHT_JOIN | FULL_JOIN ;
