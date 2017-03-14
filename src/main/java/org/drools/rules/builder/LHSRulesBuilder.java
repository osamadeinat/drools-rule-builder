package org.drools.rules.builder;

import java.util.Iterator;

import org.drools.lang.api.CEDescrBuilder;
import org.drools.lang.api.RuleDescrBuilder;
import org.drools.lang.descr.AndDescr;
import org.drools.rules.builder.domain.RequestRules;

/**
 * @author othenat@souq.com
 */
public class LHSRulesBuilder {

	private static final String OBJECT_PARAM = "funs";
	
	public static CEDescrBuilder<RuleDescrBuilder, AndDescr> setConditions(CEDescrBuilder<RuleDescrBuilder, AndDescr> descrBuilder, RequestRules rule)
	{
		String condition = "";

		if(rule.getIsFunction()){
			condition = OBJECT_PARAM + "."+ rule.getAction() +" ( \"" + rule.getValue() + "\","  + OBJECT_PARAM +  ") ";

			if(rule.getOperator().equals("")){
				descrBuilder = getFunctionalRuleByOpreator(descrBuilder, condition, rule.getSubRuleOperator());
			}else{
				descrBuilder = getFunctionalRuleByOpreator(descrBuilder, condition, rule.getOperator());
			}

			condition = setSubRules("", rule, false);

		}else{
			condition = rule.getAction() + " ( " + rule.getAttribute() + " " + rule.getSign() + " " + rule.getValue() + " ) ";

			condition = setSubRules(condition, rule, true);
		}

		if(!condition.isEmpty()){
			descrBuilder = getRuleByOpreator(descrBuilder, condition, rule.getOperator());
		}		

		return descrBuilder;
	}

	private static String setSubRules(String condition, RequestRules rule, Boolean appendOperator)
	{
		if(!rule.getSubRules().isEmpty()){
			
			if(appendOperator){
				condition += " " + rule.getSubRuleOperator() + " ( ";
			}else{
				condition += " ( ";
			}
			
			for(Iterator<RequestRules> iterator = rule.getSubRules().iterator();  iterator.hasNext();)
			{
				RequestRules subRule = iterator.next();

				if(!iterator.hasNext()) {
					subRule.setOperator("");
				}

				if(subRule.getIsFunction()){
					condition += "eval(" + OBJECT_PARAM + "."+ subRule.getAction() +" ( \"" + subRule.getValue() + "\","  + OBJECT_PARAM +  ") ) ";
				}else{
					condition +=  subRule.getAction() + " ( " + subRule.getAttribute() + " " + subRule.getSign() + " " + subRule.getValue() + " ) ";
				}

				if(!subRule.getSubRules().isEmpty()){
					condition = setSubRules(condition, subRule, true);
				}else{
					condition += subRule.getOperator() + " ";
				}
			}
			condition += " )";
		}

		return condition;
	}

	private static CEDescrBuilder<RuleDescrBuilder, AndDescr> getFunctionalRuleByOpreator(CEDescrBuilder<RuleDescrBuilder, AndDescr> descrBuilder, String condition, String operator)
	{
		if(operator.equals("or")){
			descrBuilder.or().eval().constraint(condition).end();
		}else if(operator.equals("not")){
			descrBuilder.not().eval().constraint(condition).end();
		}else{
			descrBuilder.and().eval().constraint(condition).end();
		}

		return descrBuilder;
	}

	private static CEDescrBuilder<RuleDescrBuilder, AndDescr> getRuleByOpreator(CEDescrBuilder<RuleDescrBuilder, AndDescr> descrBuilder, String condition, String operator)
	{
		if(operator.equals("or")){
			descrBuilder.or().pattern("").constraint(condition).end();
		}else if(operator.equals("not")){
			descrBuilder.not().pattern("").constraint(condition).end();
		}else{
			descrBuilder.and().pattern("").constraint(condition).end();
		}

		return descrBuilder;
	}
}