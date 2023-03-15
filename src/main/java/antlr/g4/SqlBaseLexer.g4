/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * This file is an adaptation of Presto's presto-parser/src/main/antlr4/com/facebook/presto/sql/parser/SqlBase.g4 grammar.
 */

lexer grammar SqlBaseLexer;

@members {
  /**
   * When true, parser should throw ParseExcetion for unclosed bracketed comment.
   */
  public boolean has_unclosed_bracketed_comment = false;

  /**
   * Verify whether current token is a valid decimal token (which contains dot).
   * Returns true if the character that follows the token is not a digit or letter or underscore.
   *
   * For example:
   * For char stream "2.3", "2." is not a valid decimal token, because it is followed by digit '3'.
   * For char stream "2.3_", "2.3" is not a valid decimal token, because it is followed by '_'.
   * For char stream "2.3W", "2.3" is not a valid decimal token, because it is followed by 'W'.
   * For char stream "12.0D 34.E2+0.12 "  12.0D is a valid decimal token because it is followed
   * by a space. 34.E2 is a valid decimal token because it is followed by symbol '+'
   * which is not a digit or letter or underscore.
   */
  public boolean isValidDecimal() {
    int nextChar = _input.LA(1);
    if (nextChar >= 'A' && nextChar <= 'Z' || nextChar >= '0' && nextChar <= '9' ||
      nextChar == '_') {
      return false;
    } else {
      return true;
    }
  }

  /**
   * This method will be called when we see '/*' and try to match it as a bracketed comment.
   * If the next character is '+', it should be parsed as hint later, and we cannot match
   * it as a bracketed comment.
   *
   * Returns true if the next character is '+'.
   */
  public boolean isHint() {
    int nextChar = _input.LA(1);
    if (nextChar == '+') {
      return true;
    } else {
      return false;
    }
  }

  /**
   * This method will be called when the character stream ends and try to find out the
   * unclosed bracketed comment.
   * If the method be called, it means the end of the entire character stream match,
   * and we set the flag and fail later.
   */
  public void markUnclosedComment() {
    has_unclosed_bracketed_comment = true;
  }
}

SEMICOLON: ';';

LEFT_PAREN: '(';
RIGHT_PAREN: ')';
COMMA: ',';
DOT: '.';
LEFT_BRACKET: '[';
RIGHT_BRACKET: ']';

// NOTE: If you add a new token in the list below, you should update the list of keywords
// and reserved tag in `docs/sql-ref-ansi-compliance.md#sql-keywords`.

//============================
// Start of the keywords list
//============================
//--SPARK-KEYWORD-LIST-START
ADD : A D D ;
AFTER : A F T E R ;
ALL : A L L ;
ALTER : A L T E R ;
ANALYZE : A N A L Y Z E ;
AND : A N D ;
ANTI : A N T I ;
ANY : A N Y ;
ANY_VALUE : A N Y '_' V A L U E ;
ARCHIVE : A R C H I V E ;
ARRAY : A R R A Y ;
AS : A S ;
ASC : A S C ;
AT : A T ;
AUTHORIZATION : A U T H O R I Z A T I O N ;
BETWEEN : B E T W E E N ;
BOTH : B O T H ;
BUCKET : B U C K E T ;
BUCKETS : B U C K E T S ;
BY : B Y ;
CACHE : C A C H E ;
CASCADE : C A S C A D E ;
CASE : C A S E ;
CAST : C A S T ;
CATALOG : C A T A L O G ;
CATALOGS : C A T A L O G S ;
CHANGE : C H A N G E ;
CHECK : C H E C K ;
CLEAR : C L E A R ;
CLUSTER : C L U S T E R ;
CLUSTERED : C L U S T E R E D ;
CODEGEN : C O D E G E N ;
COLLATE : C O L L A T E ;
COLLECTION : C O L L E C T I O N ;
COLUMN : C O L U M N ;
COLUMNS : C O L U M N S ;
COMMENT : C O M M E N T ;
COMMIT : C O M M I T ;
COMPACT : C O M P A C T ;
COMPACTIONS : C O M P A C T I O N S ;
COMPUTE : C O M P U T E ;
CONCATENATE : C O N C A T E N A T E ;
CONSTRAINT : C O N S T R A I N T ;
COST : C O S T ;
CREATE : C R E A T E ;
CROSS : C R O S S ;
CUBE : C U B E ;
CURRENT : C U R R E N T ;
CURRENT_DATE : C U R R E N T '_' D A T E ;
CURRENT_TIME : C U R R E N T '_' T I M E ;
CURRENT_TIMESTAMP : C U R R E N T '_' T I M E S T A M P ;
CURRENT_USER : C U R R E N T '_' U S E R ;
DAY : D A Y ;
DAYOFYEAR : D A Y O F Y E A R ;
DATA : D A T A ;
DATABASE : D A T A B A S E ;
DATABASES : D A T A B A S E S ;
DATEADD : D A T E A D D ;
DATEDIFF : D A T E D I F F ;
DBPROPERTIES : D B P R O P E R T I E S ;
DEFAULT : D E F A U L T ;
DEFINED : D E F I N E D ;
DELETE : D E L E T E ;
DELIMITED : D E L I M I T E D ;
DESC : D E S C ;
DESCRIBE : D E S C R I B E ;
DFS : D F S ;
DIRECTORIES : D I R E C T O R I E S ;
DIRECTORY : D I R E C T O R Y ;
DISTINCT : D I S T I N C T ;
DISTRIBUTE : D I S T R I B U T E ;
DIV : D I V ;
DROP : D R O P ;
ELSE : E L S E ;
END : E N D ;
ESCAPE : E S C A P E ;
ESCAPED : E S C A P E D ;
EXCEPT : E X C E P T ;
EXCHANGE : E X C H A N G E ;
EXISTS : E X I S T S ;
EXPLAIN : E X P L A I N ;
EXPORT : E X P O R T ;
EXTENDED : E X T E N D E D ;
EXTERNAL : E X T E R N A L ;
EXTRACT : E X T R A C T ;
FALSE : F A L S E ;
FETCH : F E T C H ;
FIELDS : F I E L D S ;
FILTER : F I L T E R ;
FILEFORMAT : F I L E F O R M A T ;
FIRST : F I R S T ;
FOLLOWING : F O L L O W I N G ;
FOR : F O R ;
FOREIGN : F O R E I G N ;
FORMAT : F O R M A T ;
FORMATTED : F O R M A T T E D ;
FROM : F R O M ;
FULL : F U L L ;
FUNCTION : F U N C T I O N ;
FUNCTIONS : F U N C T I O N S ;
GLOBAL : G L O B A L ;
GRANT : G R A N T ;
GROUP : G R O U P ;
GROUPING : G R O U P I N G ;
HAVING : H A V I N G ;
HOUR : H O U R ;
IF : I F ;
IGNORE : I G N O R E ;
IMPORT : I M P O R T ;
IN : I N ;
INDEX : I N D E X ;
INDEXES : I N D E X E S ;
INNER : I N N E R ;
INPATH : I N P A T H ;
INPUTFORMAT : I N P U T F O R M A T ;
INSERT : I N S E R T ;
INTERSECT : I N T E R S E C T ;
INTERVAL : I N T E R V A L ;
INTO : I N T O ;
IS : I S ;
ITEMS : I T E M S ;
JOIN : J O I N ;
KEYS : K E Y S ;
LAST : L A S T ;
LATERAL : L A T E R A L ;
LAZY : L A Z Y ;
LEADING : L E A D I N G ;
LEFT : L E F T ;
LIKE : L I K E ;
ILIKE : I L I K E ;
LIMIT : L I M I T ;
LINES : L I N E S ;
LIST : L I S T ;
LOAD : L O A D ;
LOCAL : L O C A L ;
LOCATION : L O C A T I O N ;
LOCK : L O C K ;
LOCKS : L O C K S ;
LOGICAL : L O G I C A L ;
MACRO : M A C R O ;
MAP : M A P ;
MATCHED : M A T C H E D ;
MERGE : M E R G E ;
MICROSECOND : M I C R O S E C O N D ;
MILLISECOND : M I L L I S E C O N D ;
MINUTE : M I N U T E ;
MONTH : M O N T H ;
MSCK : M S C K ;
NAMESPACE : N A M E S P A C E ;
NAMESPACES : N A M E S P A C E S ;
NATURAL : N A T U R A L ;
NO : N O ;
NOT : N O T  | '!';
NULL : N U L L ;
NULLS : N U L L S ;
OF : O F ;
OFFSET : O F F S E T ;
ON : O N ;
ONLY : O N L Y ;
OPTION : O P T I O N ;
OPTIONS : O P T I O N S ;
OR : O R ;
ORDER : O R D E R ;
OUT : O U T ;
OUTER : O U T E R ;
OUTPUTFORMAT : O U T P U T F O R M A T ;
OVER : O V E R ;
OVERLAPS : O V E R L A P S ;
OVERLAY : O V E R L A Y ;
OVERWRITE : O V E R W R I T E ;
PARTITION : P A R T I T I O N ;
PARTITIONED : P A R T I T I O N E D ;
PARTITIONS : P A R T I T I O N S ;
PERCENTILE_CONT : P E R C E N T I L E '_' C O N T ;
PERCENTILE_DISC : P E R C E N T I L E '_' D I S C ;
PERCENTLIT : P E R C E N T ;
PIVOT : P I V O T ;
PLACING : P L A C I N G ;
POSITION : P O S I T I O N ;
PRECEDING : P R E C E D I N G ;
PRIMARY : P R I M A R Y ;
PRINCIPALS : P R I N C I P A L S ;
PROPERTIES : P R O P E R T I E S ;
PURGE : P U R G E ;
QUARTER : Q U A R T E R ;
QUERY : Q U E R Y ;
RANGE : R A N G E ;
RECORDREADER : R E C O R D R E A D E R ;
RECORDWRITER : R E C O R D W R I T E R ;
RECOVER : R E C O V E R ;
REDUCE : R E D U C E ;
REFERENCES : R E F E R E N C E S ;
REFRESH : R E F R E S H ;
RENAME : R E N A M E ;
REPAIR : R E P A I R ;
REPEATABLE : R E P E A T A B L E ;
REPLACE : R E P L A C E ;
RESET : R E S E T ;
RESPECT : R E S P E C T ;
RESTRICT : R E S T R I C T ;
REVOKE : R E V O K E ;
RIGHT : R I G H T ;
RLIKE : R L I K E  | R E G E X P ;
ROLE : R O L E ;
ROLES : R O L E S ;
ROLLBACK : R O L L B A C K ;
ROLLUP : R O L L U P ;
ROW : R O W ;
ROWS : R O W S ;
SECOND : S E C O N D ;
SCHEMA : S C H E M A ;
SCHEMAS : S C H E M A S ;
SELECT : S E L E C T ;
SEMI : S E M I ;
SEPARATED : S E P A R A T E D ;
SERDE : S E R D E ;
SERDEPROPERTIES : S E R D E P R O P E R T I E S ;
SESSION_USER : S E S S I O N '_' U S E R ;
SET : S E T ;
SETMINUS : M I N U S ;
SETS : S E T S ;
SHOW : S H O W ;
SKEWED : S K E W E D ;
SOME : S O M E ;
SORT : S O R T ;
SORTED : S O R T E D ;
START : S T A R T ;
STATISTICS : S T A T I S T I C S ;
STORED : S T O R E D ;
STRATIFY : S T R A T I F Y ;
STRUCT : S T R U C T ;
SUBSTR : S U B S T R ;
SUBSTRING : S U B S T R I N G ;
SYNC : S Y N C ;
SYSTEM_TIME : S Y S T E M '_' T I M E ;
SYSTEM_VERSION : S Y S T E M '_' V E R S I O N ;
TABLE : T A B L E ;
TABLES : T A B L E S ;
TABLESAMPLE : T A B L E S A M P L E ;
TBLPROPERTIES : T B L P R O P E R T I E S ;
TEMPORARY : T E M P O R A R Y  | T E M P ;
TERMINATED : T E R M I N A T E D ;
THEN : T H E N ;
TIME : T I M E ;
TIMESTAMP : T I M E S T A M P ;
TIMESTAMPADD : T I M E S T A M P A D D ;
TIMESTAMPDIFF : T I M E S T A M P D I F F ;
TO : T O ;
TOUCH : T O U C H ;
TRAILING : T R A I L I N G ;
TRANSACTION : T R A N S A C T I O N ;
TRANSACTIONS : T R A N S A C T I O N S ;
TRANSFORM : T R A N S F O R M ;
TRIM : T R I M ;
TRUE : T R U E ;
TRUNCATE : T R U N C A T E ;
TRY_CAST : T R Y '_' C A S T ;
TYPE : T Y P E ;
UNARCHIVE : U N A R C H I V E ;
UNBOUNDED : U N B O U N D E D ;
UNCACHE : U N C A C H E ;
UNION : U N I O N ;
UNIQUE : U N I Q U E ;
UNKNOWN : U N K N O W N ;
UNLOCK : U N L O C K ;
UNSET : U N S E T ;
UPDATE : U P D A T E ;
USE : U S E ;
USER : U S E R ;
USING : U S I N G ;
VALUES : V A L U E S ;
VERSION : V E R S I O N ;
VIEW : V I E W ;
VIEWS : V I E W S ;
WEEK : W E E K ;
WHEN : W H E N ;
WHERE : W H E R E ;
WINDOW : W I N D O W ;
WITH : W I T H ;
WITHIN : W I T H I N ;
YEAR : Y E A R ;
ZONE : Z O N E ;
//--SPARK-KEYWORD-LIST-END
//============================
// End of the keywords list
//============================

EQ  : '=' | '==';
NSEQ: '<=>';
NEQ : '<>';
NEQJ: '!=';
LT  : '<';
LTE : '<=' | '!>';
GT  : '>';
GTE : '>=' | '!<';

PLUS: '+';
MINUS: '-';
ASTERISK: '*';
SLASH: '/';
PERCENT: '%';
TILDE: '~';
AMPERSAND: '&';
PIPE: '|';
CONCAT_PIPE: '||';
HAT: '^';
COLON: ':';
ARROW: '->';
HENT_START: '/*+';
HENT_END: '*/';

STRING
    : '\'' ( ~('\''|'\\') | ('\\' .) )* '\''
    | '"' ( ~('"'|'\\') | ('\\' .) )* '"'
    | 'R\'' (~'\'')* '\''
    | 'R"'(~'"')* '"'
    ;

BIGINT_LITERAL
    : DIGIT+ 'L'
    ;

SMALLINT_LITERAL
    : DIGIT+ 'S'
    ;

TINYINT_LITERAL
    : DIGIT+ 'Y'
    ;

INTEGER_VALUE
    : DIGIT+
    ;

EXPONENT_VALUE
    : DIGIT+ EXPONENT
    | DECIMAL_DIGITS EXPONENT {isValidDecimal()}?
    ;

DECIMAL_VALUE
    : DECIMAL_DIGITS {isValidDecimal()}?
    ;

FLOAT_LITERAL
    : DIGIT+ EXPONENT? 'F'
    | DECIMAL_DIGITS EXPONENT? 'F' {isValidDecimal()}?
    ;

DOUBLE_LITERAL
    : DIGIT+ EXPONENT? 'D'
    | DECIMAL_DIGITS EXPONENT? 'D' {isValidDecimal()}?
    ;

BIGDECIMAL_LITERAL
    : DIGIT+ EXPONENT? 'BD'
    | DECIMAL_DIGITS EXPONENT? 'BD' {isValidDecimal()}?
    ;

IDENTIFIER
    : (LETTER | DIGIT | '_')+
    ;

BACKQUOTED_IDENTIFIER
    : '`' ( ~'`' | '``' )* '`'
    ;

fragment DECIMAL_DIGITS
    : DIGIT+ '.' DIGIT*
    | '.' DIGIT+
    ;

fragment EXPONENT
    : 'E' [+-]? DIGIT+
    ;

fragment DIGIT
    : [0-9]
    ;

fragment LETTER
    : [A-Za-z]
    ;

SIMPLE_COMMENT
    : '--' ('\\\n' | ~[\r\n])* '\r'? '\n'? -> channel(HIDDEN)
    ;

BRACKETED_COMMENT
    : '/*' {!isHint()}? ( BRACKETED_COMMENT | . )*? ('*/' | {markUnclosedComment();} EOF) -> channel(HIDDEN)
    ;

WS
    : [ \r\n\t]+ -> channel(HIDDEN)
    ;

// Catch-all for anything we can't recognize.
// We use this to be able to ignore and recover all the text
// when splitting statements with DelimiterLexer
UNRECOGNIZED
    : .
    ;


fragment A : [aA]; // match either an 'a' or 'A'
fragment B : [bB];
fragment C : [cC];
fragment D : [dD];
fragment E : [eE];
fragment F : [fF];
fragment G : [gG];
fragment H : [hH];
fragment I : [iI];
fragment J : [jJ];
fragment K : [kK];
fragment L : [lL];
fragment M : [mM];
fragment N : [nN];
fragment O : [oO];
fragment P : [pP];
fragment Q : [qQ];
fragment R : [rR];
fragment S : [sS];
fragment T : [tT];
fragment U : [uU];
fragment V : [vV];
fragment W : [wW];
fragment X : [xX];
fragment Y : [yY];
fragment Z : [zZ];

