package junits;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import components.Control;

class ReaderTest {

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
	final void testReadRules() {
		Map<String, String> list = Control.readRules("files/rules.cf");
		System.out.println(list.size());
		//335 � o n�mero de regras da lista
		assertTrue(335== list.size());
		//Verificar se l� corretamente
		assertTrue(list.get(18).equals("MIME_HTML_ONLY"));
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
