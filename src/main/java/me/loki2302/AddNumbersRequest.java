package me.loki2302;

import org.codehaus.jackson.annotate.JsonProperty;

public class AddNumbersRequest implements CalculatorRequest { 
    @JsonProperty
    public int a;
    
    @JsonProperty
    public int b;
}