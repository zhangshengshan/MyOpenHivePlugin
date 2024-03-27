lexer grammar MyDSLLexer;

TB : [a-zA-Z0-9_]+ '.' [a-zA-Z0-9_]+;
JOIN: '-';
LEFT_JOIN: '->';
RIGHT_JOIN: '<-';
FULL_JOIN: '<->';
SEMICOLON: ';';
LEFT_PAREN: '(';
RIGHT_PAREN: ')';
COMMA: ',';
DOT: '.';
LEFT_BRACKET: '[';
RIGHT_BRACKET: ']';
LEFT_BRACE: '{';
RIGHT_BRACE: '}';
SPACE              : [ \t\r\n]+     -> channel(HIDDEN);
