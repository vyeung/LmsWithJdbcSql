package services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.st.lmssql.models.Author;
import com.st.lmssql.service.AdminService;

public class AdminServiceTest {

	private int authTestId = 0;
	
	@Test
	public void addAuthorTest() {
		AdminService adminServ = new AdminService();
		adminServ.addAuthor("Billy Bob");
		
		List<Author> authors = adminServ.getAllAuthors();
		for(Author a : authors) {
			if(a.getAuthorName().equals("Billy Bob"))
				authTestId = a.getAuthorId();
		}
		
		assertTrue(adminServ.getAuthor(authTestId).getAuthorName().equals("Billy Bob"));
	}
	
	@Test
	public void getAuthorTest() {
		int authorTestId = 1;
		String authorTestName = "Lois McMaster Bujold";
		
		AdminService adminServ = new AdminService();
		Author a = adminServ.getAuthor(authorTestId);
		assertEquals(a.getAuthorName(), authorTestName);
	}
	
	
	
}