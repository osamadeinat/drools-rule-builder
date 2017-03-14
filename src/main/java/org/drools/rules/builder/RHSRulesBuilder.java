package org.drools.rules.builder;

import org.drools.lang.api.CEDescrBuilder;
import org.drools.lang.api.RuleDescrBuilder;
import org.drools.lang.descr.AndDescr;
/**
 * @author othenat@souq.com
 */
public class RHSRulesBuilder {

	public static CEDescrBuilder<RuleDescrBuilder, AndDescr> setRHS(CEDescrBuilder<RuleDescrBuilder, AndDescr> descrBuilder)
	{

		StringBuilder rhs = new StringBuilder();
		rhs.append("\t Actions actions = new actions();");
		rhs.append(System.getProperty("line.separator"));

		descrBuilder.end().rhs(rhs.toString()).end();

		return descrBuilder;
	}
}