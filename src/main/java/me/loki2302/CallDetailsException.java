package me.loki2302;

public class CallDetailsException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    private final String methodName;
    private final Object[] args;
    
    public CallDetailsException(String methodName, Object[] args) {
        this.methodName = methodName;
        this.args = args;
    }
    
    public String getMethodName() {
        return methodName;
    }
    
    public Object[] getArgs() {
        return args;
    }
}