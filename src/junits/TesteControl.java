package junits;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import components.Control;

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
	final void testCalculateFP() {
		fail("Not yet implemented"); // TODO
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
