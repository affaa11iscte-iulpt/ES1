package junits;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import components.Control;
import components.Email;
import components.Email.Type;

class TesteControl {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	final void testCalculate() {
		
	}

	@Test
	final void testGetFpos() {
		Control control = new Control();
		//No início, os falsos positivos são 0
		assertTrue(control.getFpos()==0);
		
		Map<String, String> rules = new HashMap<String, String>() {
			{
		        put("BAYES_00","10");
		        put("FREEMAIL_FROM","-5");
		        put("RDNS_NONE","6");
		        put("FREEMAIL_REPLYTO_END_DIGIT","2");
		        put("MSOE_MID_WRONG_CASE","-7");
		        put("DATE_IN_PAST_24_48","2");
		        put("T_LOTS_OF_MONEY","-9");
		}
		};
		
		List<String> rulesOfEmailA = Arrays.asList("BAYES_00","FREEMAIL_FROM","RDNS_NONE",
				"FREEMAIL_REPLYTO_END_DIGIT","MSOE_MID_WRONG_CASE"); //6
		List<String> rulesOfEmailB = Arrays.asList("RDNS_NONE","FREEMAIL_REPLYTO_END_DIGIT",
				"MSOE_MID_WRONG_CASE","DATE_IN_PAST_24_48","T_LOTS_OF_MONEY"); //-6
		
		//Caso em que há 1 FP
		Email emailA = new Email("aa", Type.HAM);
		Email emailB = new Email("bb", Type.SPAM);
		for(String s: rulesOfEmailA)
			emailA.addRule(s);
		for(String s: rulesOfEmailB)
			emailB.addRule(s);
		
		List<Email> emails = Arrays.asList(emailA, emailB);

		control = new Control();
		control.calculate(emails, rules);
		assertTrue(control.getFpos()==1);
		
		//Caso em que há 0 FP
				emailA = new Email("aa", Type.SPAM);
				emailB = new Email("bb", Type.HAM);
				for(String s: rulesOfEmailA)
					emailA.addRule(s);
				for(String s: rulesOfEmailB)
					emailB.addRule(s);
				
				emails = Arrays.asList(emailA, emailB);

				control = new Control();
				control.calculate(emails, rules);
				assertTrue(control.getFpos()==0); //aquiiiiiiiiiiiiiiiiiiiii
	}

	@Test
	final void testGetFneg() {
		Control control = new Control();
		//No início, os falsos negativos são 0
		assertTrue(control.getFneg()==0);
		
		Map<String, String> rules = new HashMap<String, String>() {
			{
		        put("BAYES_00","10");
		        put("FREEMAIL_FROM","-5");
		        put("RDNS_NONE","6");
		        put("FREEMAIL_REPLYTO_END_DIGIT","2");
		        put("MSOE_MID_WRONG_CASE","-7");
		        put("DATE_IN_PAST_24_48","2");
		        put("T_LOTS_OF_MONEY","-9");
		}
		};
		
		List<String> rulesOfEmailA = Arrays.asList("BAYES_00","FREEMAIL_FROM","RDNS_NONE",
				"FREEMAIL_REPLYTO_END_DIGIT","MSOE_MID_WRONG_CASE"); //6
		List<String> rulesOfEmailB = Arrays.asList("RDNS_NONE","FREEMAIL_REPLYTO_END_DIGIT",
				"MSOE_MID_WRONG_CASE","DATE_IN_PAST_24_48","T_LOTS_OF_MONEY"); //-6
		
		//Caso em que há 1 FP
		Email emailA = new Email("aa", Type.HAM);
		Email emailB = new Email("bb", Type.SPAM);
		for(String s: rulesOfEmailA)
			emailA.addRule(s);
		for(String s: rulesOfEmailB)
			emailB.addRule(s);
		
		List<Email> emails = Arrays.asList(emailA, emailB);

		control = new Control();
		control.calculate(emails, rules);
		assertTrue(control.getFneg()==1);
		
		//Caso em que há 0 FP
				emailA = new Email("aa", Type.SPAM);
				emailB = new Email("bb", Type.HAM);
				for(String s: rulesOfEmailA)
					emailA.addRule(s);
				for(String s: rulesOfEmailB)
					emailB.addRule(s);
				
				emails = Arrays.asList(emailA, emailB);

				control = new Control();
				control.calculate(emails, rules);
				assertTrue(control.getFneg()==0);
	}

	@Test
	final void testReadRules() {
		Map<String, String> list = Control.readRules("files/rules.cf");
		//Número de regras é de 335
		assertTrue(list.size()==335);
		//Caso o argumento seja nulo
		list = Control.readRules(null);
		assertNull(list);
		//Caso o argumento seja inválido, por inexistência do ficheiro
		list = Control.readRules("files/bababa.cf");
		assertNull(list);
	}

	@Test
	final void testReadEmails() {
		List<Email> hamList = Control.readEmails("files/ham.log", false);
		List<Email> spamList = Control.readEmails("files/spam.log", true);
		//Numero de emails do tipo HAM é de 695
		assertTrue(hamList.size()==695);
		//Numero de emails do tipo SPAM é de 239
		assertTrue(spamList.size()==239);
		//Caso o argumento da localizacao do fiheiro seja nulo
			//Ham
			hamList = Control.readEmails(null, false);
			assertNull(hamList);
			//Spam
			spamList = Control.readEmails(null, true);
			assertNull(spamList);
		//Caso o argumento da localizacao do ficheiro seja invalido, por inexistência do fichiero
			//Ham
			hamList = Control.readEmails("files/xxx.log", false);
			assertNull(hamList);
			//Spam
			spamList = Control.readEmails("files/xxx.log", true);
			assertNull(spamList);
	}

	@Test
	final void testEquals() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testToString() {
		fail("Not yet implemented"); // TODO
	}

}
