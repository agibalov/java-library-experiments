grammar Filter;

@header {
package io.agibalov.filterexpression;
}

expression: left=IDENTIFIER operation right=literal EOF;

fragment LETTER: [a-zA-Z$_];
fragment DIGIT: [0-9];
fragment LETTER_OR_DIGIT: LETTER | DIGIT;
IDENTIFIER: LETTER LETTER_OR_DIGIT*;

fragment QUOTATION: '\'';
STRING_LITERAL: QUOTATION .+? QUOTATION;
NUMBER_LITERAL: DIGIT+;
literal: STRING_LITERAL|NUMBER_LITERAL;

OP_EQUAL: '=';
OP_NOT_EQUAL: '!=';
OP_LESS: '<';
OP_LESS_OR_EQUAL: '<=';
OP_GREATER: '>';
OP_GREATER_OR_EQUAL: '>=';
OP_CONTAINS: '~';
operation: OP_EQUAL|OP_NOT_EQUAL|OP_LESS|OP_LESS_OR_EQUAL|OP_GREATER|OP_GREATER_OR_EQUAL|OP_CONTAINS;
