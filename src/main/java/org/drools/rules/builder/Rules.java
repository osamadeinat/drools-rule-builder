package org.drools.rules.builder;

import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import org.drools.builder.ResourceType;
import org.drools.io.impl.ReaderResource;
import org.drools.lang.DrlDumper;
import org.drools.lang.api.CEDescrBuilder;
import org.drools.lang.api.DescrFactory;
import org.drools.lang.api.PackageDescrBuilder;
import org.drools.lang.api.RuleDescrBuilder;
import org.drools.lang.descr.AndDescr;
import org.drools.lang.descr.PackageDescr;
import org.drools.rules.builder.domain.RequestRules;
import org.drools.verifier.Verifier;
import org.drools.verifier.VerifierError;
import org.drools.verifier.builder.VerifierBuilder;
import org.drools.verifier.builder.VerifierBuilderFactory;

import java.io.*;
import java.util.ArrayList;
/**
 * 
 * @author othenat
 * This Util Class to create dynamic rule and modify it through API with Rule-Engine
 * 
 */
public class Rules 
{

	private Sardine sardine = SardineFactory.begin();
	/**
	 * 
	 * @param ruleEngineUrl
	 * @param username
	 * @param password
	 * @author othenat
	 * @param buildUrl 
	 * This Helper function to add/edit rule into rule-engine
	 * @return 
	 * @throws Exception 
	 * @throws IOException 
	 */
	public synchronized void addARule(String ruleEngineUrl, String buildUrl, String username, String password, String assetsUrl, Integer port) throws Exception
	{
		PackageDescrBuilder packBuilder = packagDescrBuilder();

		PackageDescr pkg = packBuilder.getDescr();

		String drl = new DrlDumper().dump( pkg );

		System.out.println(drl);

		byte[] data = drl.getBytes();

		if(this.isVildDRL(drl)){
			sardine.setCredentials(username, password);
			sardine.put("http://guvnor-url/assets/file.drl", data);
		}else{
			throw new Exception("Rule is not valid");
		}
	}
	/**
	 * @param ruleEngineUrl
	 * @param username
	 * @param password
	 * @author othenat
	 * @param buildUrl 
	 * This Helper function to add/edit multi-rules into rule-engine
	 * @return 
	 * @throws Exception 
	 * @throws IOException 
	 */
	public synchronized void putRule(String ruleEngineUrl, String buildUrl, String username, String password, String assetsUrl, Integer port, String ruleId) throws Exception
	{
		PackageDescrBuilder packBuilder = packagDescrBuilder();

		PackageDescr pkg = packBuilder.getDescr();

		String drl = new DrlDumper().dump( pkg );

		System.out.println(drl);

		byte[] data = drl.getBytes();

		if(this.isVildDRL(drl)){
			sardine.put(ruleEngineUrl + ruleId +".drl", data);
		}else{
			throw new Exception("Rule is not valid");
		}
	}

	/**
	 * 
	 * @param id
	 * @author othenat
	 * @return 
	 */
	public String getRule (String url, String username, String password, String id)
	{
		//TODO : this function to get rule by name / UUID
		Sardine sardine = SardineFactory.begin(username, password);
		StringBuilder drlRule = new StringBuilder();

		try {
			InputStream rule = sardine.get(url + "" + id + ".drl");
			BufferedReader bufferReader = new BufferedReader(new InputStreamReader(rule));
			String read;

			while((read=bufferReader.readLine()) != null) {
				System.out.println(read);
				drlRule.append(read + "\n");   
			}

			bufferReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return drlRule.toString();
	}
	/**
	 * 
	 * @param id
	 * @author othenat
	 */
	public void delRule (String url, String username, String password, String id)
	{
		//TODO : this function to delete rule by name / UUID
		Sardine sardine = SardineFactory.begin(username, password);

		try{
			sardine.delete(url + "" + id + ".drl");
		}catch(IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @author othenat
	 * This function to build rule criteria and conditions / Actions
	 */
	public PackageDescrBuilder packagDescrBuilder ()
	{
		CEDescrBuilder<RuleDescrBuilder, AndDescr> descrBuilder = setRuleHeader("");

		descrBuilder = setLHS(descrBuilder);

		descrBuilder = setRHS(descrBuilder);

		PackageDescrBuilder packageDescrBuilder = descrBuilder.end().end();

		return packageDescrBuilder;
	}
	/**
	 * 
	 * @author othenat
	 * This function to build rule header
	 */
	public CEDescrBuilder<RuleDescrBuilder, AndDescr> setRuleHeader(String jsonParam)
	{
		CEDescrBuilder<RuleDescrBuilder, AndDescr> descrBuilder = DescrFactory.newPackage()
				.name( "org.drools.compiler" )
				.newRule().name( "Name")
				.attribute( "dialect" , "\"mvel\"")
				.attribute( "salience","10" )
				.attribute( "lock-on-active","true")
				.attribute( "no-loop","true")
				.attribute( "agenda-group", "Group#1")
				.attribute( "date-effective" , "")
				.attribute( "date-expires" , "")
				.attribute( "enabled" ,"true")
				.lhs();

		return descrBuilder;
	}
	/**
	 * 
	 * @author othenat
	 * This function to build rule left hand side static helpers
	 */
	public CEDescrBuilder<RuleDescrBuilder, AndDescr> setLHS(CEDescrBuilder<RuleDescrBuilder, AndDescr> descrBuilder)
	{
		descrBuilder
		.pattern( "Pattern #1" ).id( "id#1", false ).type( "Object" ).constraint("").end()
		.pattern( "Pattern #2" ).id( "id#2", false ).type( "Object" ).constraint("")
		.end();

		setLHSRules(descrBuilder);

		return descrBuilder;
	}
	/**
	 * 
	 * @author othenat
	 * This function to build rule left hand side complex rules 
	 */
	public CEDescrBuilder<RuleDescrBuilder, AndDescr> setLHSRules(CEDescrBuilder<RuleDescrBuilder, AndDescr> descrBuilder)
	{
		for(RequestRules rule : new ArrayList<RequestRules>()){
			descrBuilder = LHSRulesBuilder.setConditions(descrBuilder, rule);
		}

		return descrBuilder;
	}
	/**
	 * 
	 * @author othenat
	 * This function to build rule right hand side
	 */
	public CEDescrBuilder<RuleDescrBuilder, AndDescr> setRHS(CEDescrBuilder<RuleDescrBuilder, AndDescr> descrBuilder)
	{
		return RHSRulesBuilder.setRHS(descrBuilder);
	}

	private Boolean isVildDRL(String drl) {
		VerifierBuilder vBuilder = VerifierBuilderFactory.newVerifierBuilder();
		Verifier verifier = vBuilder.newVerifier();
		verifier.addResourcesToVerify(new ReaderResource(new StringReader(drl)), ResourceType.DRL);

		if (verifier.getErrors().size() == 0) {
			System.out.println("This rule Has No errors");
			return true;
		} 
		else {
			System.out.println("This rule has :" + verifier.getErrors().size() + " Errors,:-(");
		}

		for(VerifierError error : verifier.getErrors()) {
			System.out.println(error.getMessage());
		}

		return false;
	}
}