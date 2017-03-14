package org.drools.rules.builder.domain;

import java.util.List;


public class RequestRules {


    private boolean isFunction;
    private String action;
    private String value;
    private String subRuleOperator;
    private String operator;
    private String attribute;
    private String sign;
    private List<RequestRules> subRules;

    public boolean getIsFunction() {
        return isFunction;
    }

    public void setFunction(boolean isFunction) {
        this.isFunction = isFunction;
    }

    public String getAction() {
        return action;
    }

    public String getValue() {
        return value;
    }

    public String getSubRuleOperator() {
        return subRuleOperator;
    }

    public String getOperator() {
        return operator;
    }

    public String getAttribute() {
        return attribute;
    }

    public String getSign() {
        return sign;
    }

    public List<RequestRules> getSubRules() {
        return subRules;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
