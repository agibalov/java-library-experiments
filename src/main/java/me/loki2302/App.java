package me.loki2302;

public class App {
    public static void main(String[] args) {        
        Api api = new Api();
        try {
            api.add(2, 3);
        } catch(CallDetailsException e) {
            System.out.printf("Method name: %s\n", e.getMethodName());
            for(Object arg : e.getArgs()) {
                System.out.printf("  %s\n", arg);
            }
        }
    }
}
