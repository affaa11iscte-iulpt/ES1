package junits;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import components.Email;
import components.Email.Type;

public class TesteEmail {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testEmail() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetId() {
		Email email = new Email("xval_initial/9/_spam_/00221.775c", Type.SPAM );
		assertNotNull(email.getId());
		assertEquals("xval_initial/9/_spam_/00221.775c", email.getId());
	}

	@Test
	public final void testGetEmailType() {
		//Email do tipo SPAM
		Email spamEmail = new Email("xval_initial/9/_spam_/00221.775c", Type.SPAM);
		assertNotNull(spamEmail.getEmailType());
		assertEquals(Type.SPAM, spamEmail.getEmailType());
		//Email do tipo HAM
		Email hamEmail = new Email("xval_initial/9/_ham_/00370.65", Type.HAM);
		assertNotNull(hamEmail.getEmailType());
		assertEquals(Type.HAM, hamEmail.getEmailType());
	}

	@Test
	public final void testAddRule() {
		Email email = new Email("xval_initial/9/_ham_/00370.65", Type.HAM);
		email.addRule(null);
		assertTrue(email.getEmailRules().isEmpty());
	}

	@Test
	public final void testGetEmailRules() {
		fail("Not yet implemented"); // TODO
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
