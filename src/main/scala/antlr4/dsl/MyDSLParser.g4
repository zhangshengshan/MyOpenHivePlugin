parser grammar MyDSLParser;

options {
    tokenVocab = MyDSLLexer;
}

mutilroots: root (SEMICOLON root)* SEMICOLON? ;
root: relation (join_rel)* ;

join_rel: join relation ;
relation
    : TB (join TB)* #TbJoinTb
    | relation join relation #RelationJoinRelation
    | LP relation RP #ParenRelation
    ;

join: JOIN | LEFT_JOIN | RIGHT_JOIN | FULL_JOIN  ;