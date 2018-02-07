package me.loki2302;

public aspect TraceApiCalls {    
    pointcut runPointcut(Api api): call(* Api.*(..)) && target(api);
    
    Object around(Api api): runPointcut(api) {
        String methodName = thisJoinPointStaticPart.getSignature().getName();
        Object[] args = thisJoinPoint.getArgs();
        
        throw new CallDetailsException(methodName, args);        
    }
}
