package JUnitsTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import components.Control;
import components.Email;
import components.Email.Type;

@SuppressWarnings("serial")
public class ControlTest {

	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	public static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	public void setUp() throws Exception {
	}

	@AfterEach
	public void tearDown() throws Exception {
	}

	@Test
	public final void testCalculate() {
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
				"FREEMAIL_REPLYTO_END_DIGIT","MSOE_MID_WRONG_CASE", "NOT_RULE"); //6
		List<String> rulesOfEmailB = Arrays.asList("RDNS_NONE","FREEMAIL_REPLYTO_END_DIGIT",
				"MSOE_MID_WRONG_CASE","DATE_IN_PAST_24_48","T_LOTS_OF_MONEY", "NOT_RULE"); //-6

		//Caso em que há 1 FP e 1 FN
		Email emailA = new Email("aa", Type.HAM);
		Email emailB = new Email("bb", Type.SPAM);
		for(String s: rulesOfEmailA)
			emailA.addRule(s);
		for(String s: rulesOfEmailB)
			emailB.addRule(s);

		List<Email> emails = Arrays.asList(emailA, emailB);

		control.calculate(emails, rules);
		assertTrue(control.getFpos()==1);
		assertTrue(control.getFneg()==1);


		//Caso em que há 0 FP e 0 FN
		emailA = new Email("aa", Type.SPAM);
		emailB = new Email("bb", Type.HAM);
		for(String s: rulesOfEmailA)
			emailA.addRule(s);
		for(String s: rulesOfEmailB)
			emailB.addRule(s);

		emails = Arrays.asList(emailA, emailB);

		control = new Control();
		control.calculate(emails, rules);
		assertTrue(control.getFpos()==0);
		assertTrue(control.getFneg()==0);
	}

	@Test
	public final void testGetFpos() {
		Control control = new Control();
		assertTrue(control.getFpos()==0);
	}

	@Test
	public final void testGetFneg() {
		Control control = new Control();
		assertTrue(control.getFneg()==0);
	}

	@Test
	public final void testReadRules() {

		//Caso o argumento seja nulo
		Map<String, String> list = Control.readRules(null);
		assertNull(list);
		
		//Caso o argumento seja inválido, por inexistência do ficheiro
		list = Control.readRules("files/bababa.cf");
		assertNull(list);
		
		list = Control.readRules("files/rules_example.cf");
		//Número de regras é de 335
		assertTrue(list.size()==335);
	}

	@Test
	public final void testReadEmails() {
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
	public final void testAutomaticMode() {
		List<Email> emails = new ArrayList<Email>();
		emails.add(new Email("aaaaa", Type.HAM));
		emails.add(new Email("bbbbb", Type.HAM));
		emails.add(new Email("ccccc", Type.SPAM));
		emails.add(new Email("ddddd", Type.HAM));
		emails.add(new Email("eeeee", Type.SPAM));
		emails.add(new Email("fffff", Type.SPAM));
		emails.add(new Email("ggggg", Type.HAM));

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
		Control c = new Control();
		try {
			c.automaticMode(emails, rules);
		} catch (Exception e) {
			//JOptionPane.showMessageDialog(null, "Ocorreu um erro. Tente novamente.");
			e.printStackTrace();
		}

		//É preciso ver o que se mete aqui
	}

	@Test
	public final void testReadAutomatic() {
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

		List<Integer> values = Arrays.asList(4, 0, 0, -4, 1, -2, -2);

		Control c = new Control();
		assertNull(c.readAutomatic(rules, "aa", "experimentBaseDirectory/referenceFronts/AntiSpamFilterProblem_example.rs"));
		assertNull(c.readAutomatic(rules, "experimentBaseDirectory/referenceFronts/AntiSpamFilterProblem_example.rf", "bbb"));
		assertNotNull(c.readAutomatic(rules, "experimentBaseDirectory/referenceFronts/AntiSpamFilterProblem_example.rf",
				"experimentBaseDirectory/referenceFronts/AntiSpamFilterProblem_example.rs"));

		Map<String, String> map = c.changeListToMap(rules, values);

		assertEquals(c.readAutomatic(rules, "experimentBaseDirectory/referenceFronts/AntiSpamFilterProblem_example.rf",
				"experimentBaseDirectory/referenceFronts/AntiSpamFilterProblem_example.rs"),
				map);

	}

	@Test
	public final void testChangeListToMap() {
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

		List<Integer> values = Arrays.asList(4, 0, 0, -4, 1, -2, -2);

		Map<String, String> map = new HashMap<String, String>() {
			{
				put("BAYES_00","4");
				put("FREEMAIL_FROM","-4");
				put("RDNS_NONE","0");
				put("FREEMAIL_REPLYTO_END_DIGIT","-2");
				put("MSOE_MID_WRONG_CASE","0");
				put("DATE_IN_PAST_24_48","-2");
				put("T_LOTS_OF_MONEY","1");
			}
		};

		Control c = new Control();
		assertEquals(map, c.changeListToMap(rules, values));


	}

	@Test
	public final void testIsBest() {
		Control c = new Control();
		assertTrue(c.isBest(-1, -1, new int[]{1,2}));
		assertTrue(c.isBest(-1, 0, new int[]{1,2}));
		assertTrue(c.isBest(0, -1, new int[]{1,2}));

		assertFalse(c.isBest(1, 2, new int[]{0,2}));
		assertTrue(c.isBest(2, 1, new int[]{0,2}));
		assertTrue(c.isBest(2, 2, new int[]{1,1}));
		assertTrue(c.isBest(2, 2, new int[]{2,1}));
		assertFalse(c.isBest(2, 2, new int[]{3,1}));

	}

	@Test
	public final void testSaveConfigurations() {
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

		Control c = new Control();
		assertTrue(c.saveConfigurations("files\\rules_example2.cf", rules));
		assertFalse(c.saveConfigurations(null, rules));
		//c.saveConfigurations("aaaaa", rules);
	}

	@Test
	public final void testRemoveRowFile() {
		//ATENÇÃO MELHORAR VER COM O PROF
		Control c = new Control();
		assertTrue(c.removeRowFile("files\\rules.cf"));
		assertFalse(c.removeRowFile(null));
		assertFalse(c.removeRowFile("aaaaaa.cf"));
	}
	
	@Test
	public final void testEquals() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testToString() {
		fail("Not yet implemented"); // TODO
	}

}
