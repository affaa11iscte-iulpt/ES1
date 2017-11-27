package junits;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
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
		List<String> rulesOfEmail = Arrays.asList("BAYES_00","FREEMAIL_FROM","RDNS_NONE",
				"FREEMAIL_REPLYTO_END_DIGIT","MSOE_MID_WRONG_CASE");
		Email email = new Email("aa", Type.HAM);
		for(String s: rulesOfEmail)
			email.addRule(s);
		
		Control control = new Control();
	}

	@Test
	final void testGetFpos() {
		Control control = new Control();
		assertTrue(control.getFpos()==0);
	}

	@Test
	final void testGetFneg() {
		Control control = new Control();
		assertTrue(control.getFneg()==0);
	}

	@Test
	final void testReadRules() {
		Map<String, String> list = Control.readRules("files/rules.cf");
		assertTrue(list.size()==335);
	}

	@Test
	final void testReadEmails() {
		fail("Not yet implemented"); // TODO
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
