grammar Calculator;

@header {
  package me.loki2302.calcgrammar;
}

program
  : expression EOF
  ;

expression
  : left=expression operation=(OP_MUL|OP_DIV) right=expression    #operationExpression
  | left=expression operation=(OP_ADD|OP_SUB) right=expression    #operationExpression
  | '(' expression ')'                                            #parenthesesExpression
  | atom=NUM                                                      #atomExpression
  ;

OP_ADD      : '+';
OP_SUB      : '-';
OP_MUL      : '*';
OP_DIV      : '/';

NUM   : [0-9]+;
WS    : [ \t\n\r]+ -> skip;
