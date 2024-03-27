parser grammar MyDSLParser;

options {
    tokenVocab = MyDSLLexer;
}

root:


relation: TB JOI
join_module : join module ;
module : LEFT_PAREN TB LEFT_JOIN TB RIGHT_PAREN ;
join : LEFT_JOIN | RIGHT_JOIN | FULL_JOIN ;
