package junits;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Interface.Interface;

class InterfaceTest {

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
	void testInterface() {
		Interface i = new Interface();
		assertNotNull(i);
	}

	@Test
	void testOpen() {
		Interface i = new Interface();
		i.open();
		assertTrue(i.isVisible());
	}

	@Test
	void testAddPanels() {
		fail("Not yet implemented");
	} 

	@Test
	void testSetFp() {
	
	}

	@Test
	void testSetFn() {
		fail("Not yet implemented");
	}

	@Test
	void testAddRules() {
		Interface i = new Interface();
		boolean b = i.addRules(null);
		assertFalse(b);
	}

	@Test
	void testGetRules() {
	}

	@Test
	void testGetEmails() {
		Interface i = new Interface();
		assertTrue(i.getEmails().isEmpty());
	}

}
