package daos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.Test;

import com.st.lmssql.dao.GenericDao;
import com.st.lmssql.daoImp.AuthorDaoImp;
import com.st.lmssql.models.Author;
import com.st.lmssql.utils.ConnectionFactory;

public class AuthorDaoTest {

	private static Connection con = ConnectionFactory.getMyConnection();
	private static GenericDao<Author> genDaoAuthor = new AuthorDaoImp(con);
	
	private int authTestId = 0;
	
	
	//do this after all test cases
	@After
	public void cleanUp() throws IOException, SQLException {
		Author clean = new Author();
		clean.setAuthorId(authTestId);
		genDaoAuthor.delete(clean);
		con.commit();
	}
	
	@Test
	public void addTest() throws SQLException {
		//add an author
		Author test = new Author();
		test.setAuthorName("foo");
		genDaoAuthor.add(test);
		
		List<Author> authors = genDaoAuthor.getAll();
		for(Author a : authors) {
			if(a.getAuthorName().equals("foo"))
				authTestId = a.getAuthorId();
		}
		
		//see if it's there
		Author found = genDaoAuthor.get(authTestId);
		con.commit();
		assertEquals(found.getAuthorName(), "foo");
	}
	
	@Test
	public void getTest() throws SQLException {
		Author found = genDaoAuthor.get(1);
		assertTrue(found.getAuthorName().equals("Lois McMaster Bujold"));
	}
	
	@Test public void getAllTest() throws SQLException {
		List<Author> all = genDaoAuthor.getAll();
		assertTrue(all.size() == 10);
	}
	
	@Test
	public void updateTest() throws SQLException {
		Author test = new Author();
		test.setAuthorName("foo");
		genDaoAuthor.add(test);
		
		List<Author> authors = genDaoAuthor.getAll();
		for(Author a : authors) {
			if(a.getAuthorName().equals("foo"))
				authTestId = a.getAuthorId();
		}
		
		Author updated = new Author(authTestId, "goo");
		genDaoAuthor.update(updated);
		con.commit();
		assertEquals(updated.getAuthorName(), "goo");
	}
	
	@Test
	public void deleteTest() throws SQLException {
		Author test = new Author();
		test.setAuthorName("foo");
		genDaoAuthor.add(test);
		
		List<Author> prev = genDaoAuthor.getAll();
		int previous = prev.size();
		for(Author a : prev) {
			if(a.getAuthorName().equals("foo"))
				authTestId = a.getAuthorId();
		}
		
		Author del = new Author();
		del.setAuthorId(authTestId);
		genDaoAuthor.delete(del);
		List<Author> curr = genDaoAuthor.getAll();
		int current = curr.size();
		con.commit();
		assertTrue(current == (previous-1));
	}
}