package com.st.lmssql.menus;

import java.sql.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import com.st.lmssql.models.Author;
import com.st.lmssql.models.Book;
import com.st.lmssql.models.BookLoans;
import com.st.lmssql.models.Borrower;
import com.st.lmssql.models.LibraryBranch;
import com.st.lmssql.models.Publisher;
import com.st.lmssql.service.AdminService;
import com.st.lmssql.utils.DateCalculations;

public class AdminMenu {

	private Scanner scan = new Scanner(System.in);
	private String selection;
	private AdminService adminService;
	
	public AdminMenu(AdminService adminService) {
		this.adminService = adminService;
	}

	public void adminSubMenu() {
		do {
			System.out.println();
			System.out.println("What would you like to do?");
			System.out.println("1) - Add...");
			System.out.println("2) - Update...");
			System.out.println("3) - Delete...");  //not recommended due to scenarios
			System.out.println("4) - Change Due Date for a Book Loan");
			System.out.println("5) - Quit to previous");
			
			selection = scan.nextLine();
			
			if(selection.equals("1")) {
				adminAddOptions();
			}
			else if(selection.equals("2")) {
				adminUpdateOptions();
			}
			else if(selection.equals("3")) {
				adminDeleteOptions();
			}
			else if(selection.equals("4")) {
				adminChangeDueDateLoan();
			}
			else if(selection.equals("5")) {
				return;
			}
			else {
				System.out.println("Invalid selection! Try Again."); 
			}
		}
		while(!selection.equals("5"));
	}
	
	public void adminAddOptions() {
		do {
			System.out.println();
			System.out.println("What do you want to add?");
			System.out.println("1) A book");
			System.out.println("2) A author");
			System.out.println("3) A publisher");
			System.out.println("4) A library branch");
			System.out.println("5) A borrower");
			System.out.println("6) Quit to previous");
			selection = scan.nextLine();
			
			if(selection.equals("1")) {
				adminAddBook(); 
				return;
			}
			else if(selection.equals("2")) {
				adminAddAuthor(); 
				return;
			}
			else if(selection.equals("3")) {
				adminAddPublisher(); 
				return;
			}
			else if(selection.equals("4")) {
				adminAddBranch(); 
				return;
			}
			else if(selection.equals("5")) {
				adminAddBorrower();
				return;
			}
			else if(selection.equals("6")) {
				return;
			}
			else {
				System.out.println("Invalid selection! Try Again."); 
			}
		}
		while(!selection.equals("6"));
	}
	
	public void adminAddBook() {
		String bookTitle, authName, pubName;
		
		printAllBooks();
		System.out.print("Enter new book title: ");
		bookTitle = scan.nextLine();
		System.out.print("Enter author name: ");
		authName = scan.nextLine();
		
		//check if author exists, add if not
		boolean authNameExists = adminService.authNameExists(authName);
		if(!authNameExists) {
			adminService.addAuthor(authName);
		}
		
		//check if publisher exists, add if not
		System.out.print("Enter publisher name: ");
		pubName = scan.nextLine();
		boolean pubNameExists = adminService.pubNameExists(pubName);
		if(!pubNameExists) {
			String pubAddr;
			String pubPhone;
			System.out.print("Enter publisher address: ");
			pubAddr = scan.nextLine();
			System.out.print("Enter publisher phone: ");
			pubPhone = scan.nextLine();
			
			adminService.addPublisher(pubName, pubAddr, pubPhone);	
		}
		
		//find the corresponding authId and pubId
		int authId=0, pubId=0;
		List<Author> authors = adminService.getAllAuthors();
		List<Publisher> pubs = adminService.getAllPublishers();
		
		for(Author a : authors) {
			if(a.getAuthorName().equals(authName))
				authId = a.getAuthorId();
		}
		for(Publisher p : pubs) {
			if(p.getPublisherName().equals(pubName))
				pubId = p.getPublisherId();
		}
		
		//finally add the book
		adminService.addBook(bookTitle, authId, pubId);
	}
	
	public void adminAddAuthor() {
		List<Author> authors = adminService.getAllAuthors();
		printAllAuthors(authors);
		System.out.print("Enter new author name: ");
		String authName = scan.nextLine();
		boolean authNameExists;
		authNameExists = adminService.authNameExists(authName);
		
		while(true) {
			if(authNameExists) {
				System.out.println("Author name already exists! Try Again.");
				System.out.print("Enter new author name: ");
				authName = scan.nextLine();
				authNameExists = adminService.authNameExists(authName);
			}
			else {
				adminService.addAuthor(authName);
				selection = "";
				break;
			}
		}		
	}
	
	public void adminAddPublisher() {
		String pubName, pubAddr, pubPhone;
		
		List<Publisher> pubs = adminService.getAllPublishers();
		printAllPublishers(pubs);
		System.out.print("Enter new publisher name: ");
		pubName = scan.nextLine();
		boolean pubNameExists;
		pubNameExists = adminService.pubNameExists(pubName);
		
		while(true) {
			if(pubNameExists) {
				System.out.println("Publisher name already exists! Try Again.");
				System.out.print("Enter new publisher name: ");
				pubName = scan.nextLine();
				pubNameExists = adminService.pubNameExists(pubName);
			}
			else {
				System.out.print("Enter new publisher address: ");
				pubAddr = scan.nextLine();
				System.out.print("Enter new publisher phone: ");
				pubPhone = scan.nextLine();
				
				adminService.addPublisher(pubName, pubAddr, pubPhone);
				selection = "";
				break;
			}
		}		
	}
	
	public void adminAddBranch() {
		String branchName, branchAddr;
		
		List<LibraryBranch> libBranches = adminService.getAllBranches();
		printAllBranches(libBranches);
		System.out.print("Enter new branch name: ");
		branchName = scan.nextLine();
		System.out.print("Enter new branch address: ");
		branchAddr = scan.nextLine();
		
		boolean branchInfoExists;
		branchInfoExists = adminService.branchInfoExists(branchName, branchAddr);
		
		while(true) {
			if(branchInfoExists) {
				System.out.println("Branch name and address already exists! Try Again.");
				System.out.print("Enter new library branch name: ");
				branchName = scan.nextLine();
				System.out.print("Enter library branch address: ");
				branchAddr = scan.nextLine();
				branchInfoExists = adminService.branchInfoExists(branchName, branchAddr);
			}
			else {
				adminService.addLibraryBranch("'"+branchName+"'", "'"+branchAddr+"'");
				selection = "";
				break;
			}
		}		
	}
	
	public void adminAddBorrower() {
		String borrName, borrAddr, borrPhone;
		
		List<Borrower> borrs = adminService.getAllBorrowers();
		printAllBorrowers(borrs);
		
		//validation doesn't seem that necessary for borrower
		System.out.print("Enter new borrower name: ");
		borrName = scan.nextLine();
		System.out.print("Enter new borrower address: ");
		borrAddr = scan.nextLine();
		System.out.print("Enter new borrower phone: ");
		borrPhone = scan.nextLine();
		
		adminService.addBorrower(borrName, borrAddr, borrPhone);
		selection = "";	
	}
	
	public void adminUpdateOptions() {
		do {
			System.out.println();
			System.out.println("What do you want to update?");
			System.out.println("1) A book");
			System.out.println("2) A author");
			System.out.println("3) A publisher");
			System.out.println("4) A library branch");
			System.out.println("5) A borrower");
			System.out.println("6) Quit to previous");
			
			selection = scan.nextLine();
			
			if(selection.equals("1")) {
				adminUpdateBook();
				return;
			}
			else if(selection.equals("2")) {
				adminUpdateAuthor();
				return;
			}
			else if(selection.equals("3")) {
				adminUpdatePublisher();
				return;
			}
			else if(selection.equals("4")) {
				adminUpdateBranch();
				return;
			}
			else if(selection.equals("5")) {
				adminUpdateBorrower();
				return;
			}
			else if(selection.equals("6")) {
				return;
			}
			else {
				System.out.println("Invalid selection! Try Again."); 
			}
		}
		while(!selection.equals("6"));
	}
	
	public void adminUpdateBook() {
		int bookId=0;
		String title, author, publisher, newTitle, newAuthor, newPublisher;
		List<Book> books = adminService.getAllBooks();
		
		printAllBooks();
		System.out.print("What book do you want to update? ");
		title = scan.nextLine();
		
		//validation check for book title
		boolean titleExists;
		titleExists = adminService.bookTitleExists(title);
		while(true) {
			if(!titleExists) {
				System.out.println("Book title not found! Try Again.");
				System.out.print("What book do you want to update? ");
				title = scan.nextLine();
				titleExists = adminService.authNameExists(title);
			}
			else {
				for(Book b: books) {
					if(b.getTitle().equals(title))
						bookId = b.getBookId();
				}
				break;
			}
		}
		
		//get the current author and publisher name
		System.out.print("What is the current author name? ");
		author = scan.nextLine();
		System.out.print("What is the current publisher name? ");
		publisher = scan.nextLine();
		System.out.println();
		
		//start prompting them for new data
		System.out.println("Leave any field empty to let it remain the same.");
		System.out.print("Update title to: ");
		newTitle = scan.nextLine();
		if(newTitle.equals(""))
			newTitle = title; 
			
		System.out.print("Update author to: ");
		newAuthor = scan.nextLine();
		int authFlag = 0;
		if(newAuthor.equals("")) {
			newAuthor = author;
			authFlag = 1;
		}
		
		//check if author exists, add if not
		boolean authNameExists = adminService.authNameExists(newAuthor);
		if(!authNameExists && authFlag==0) {
			adminService.addAuthor(newAuthor);
		}
		
		System.out.print("Update publisher to: ");
		newPublisher = scan.nextLine();
		int pubFlag = 0;
		if(newPublisher.equals("")) {
			newPublisher = publisher;
			pubFlag = 1;
		}
		
		//check if publisher exists, add if not
		boolean pubNameExists = adminService.pubNameExists(newPublisher);
		if(!pubNameExists && pubFlag==0) {
			String pubAddr;
			String pubPhone;
			System.out.print("Enter publisher address: ");
			pubAddr = scan.nextLine();
			System.out.print("Enter publisher phone: ");
			pubPhone = scan.nextLine();
			adminService.addPublisher(newPublisher, pubAddr, pubPhone);	
		}
		
		//find the corresponding authId and pubId
		List<Author> authors = adminService.getAllAuthors();
		List<Publisher> pubs = adminService.getAllPublishers();
		int authId=0, pubId=0;
		for(Author a : authors) {
			if(a.getAuthorName().equals(newAuthor))
				authId = a.getAuthorId();
		}
		for(Publisher p : pubs) {
			if(p.getPublisherName().equals(newPublisher))
				pubId = p.getPublisherId();
		}
		
		//finally update book
		adminService.updateBook(bookId, newTitle, authId, pubId);
		selection = "";
	}
	
	public void adminUpdateAuthor() {
		String authName, newAuthName;
		
		List<Author> authors = adminService.getAllAuthors();
		printAllAuthors(authors);
		System.out.print("What author do you want to update? ");
		authName = scan.nextLine();
		
		//validation check for author name
		boolean authNameExists;
		authNameExists = adminService.authNameExists(authName);
		while(true) {
			if(!authNameExists) {
				System.out.println("Author not found! Try Again.");
				System.out.print("What author do you want to update? ");
				authName = scan.nextLine();
				authNameExists = adminService.authNameExists(authName);
			}
			else {
				break;
			}
		}		
		
		//find corresponding authorId
		int authId=0;
		for(Author a : authors) {
			if(a.getAuthorName().equals(authName))
				authId = a.getAuthorId();
		}
		
		System.out.print("Update name to: ");
		newAuthName = scan.nextLine();
		adminService.updateAuthor(authId, newAuthName);
		selection = "";
	}
	
	public void adminUpdatePublisher() {
		String pubName, newPubName, newPubAddr, newPubPhone;
		
		List<Publisher> pubs = adminService.getAllPublishers();
		printAllPublishers(pubs);
		System.out.print("What publisher do you want to update? ");
		pubName = scan.nextLine();
		
		boolean pubNameExists;
		pubNameExists = adminService.pubNameExists(pubName);
		while(true) {
			if(!pubNameExists) {
				System.out.println("Publisher not found! Try Again.");
				System.out.print("What publisher do you want to update? ");
				pubName = scan.nextLine();
				pubNameExists = adminService.pubNameExists(pubName);
			}
			else {
				break;
			}
		}
		
		int pubId=0;
		for(Publisher pub : pubs) {
			if(pub.getPublisherName().equals(pubName))
				pubId = pub.getPublisherId();
		}
		
		System.out.print("Update name to: ");
		newPubName = scan.nextLine();
		System.out.print("Update address to: ");
		newPubAddr = scan.nextLine();
		System.out.print("Update phone to: ");
		newPubPhone = scan.nextLine();
		adminService.updatePublisher(pubId, newPubName, newPubAddr, newPubPhone);
		selection = "";
	}
	
	public void adminUpdateBranch() {
		String bName, newBName, newBAddr;
		
		List<LibraryBranch> libBranches = adminService.getAllBranches();
		printAllBranches(libBranches);
		System.out.print("What branch do you want to update? ");
		bName = scan.nextLine();
		
		boolean bNameExists;
		bNameExists = adminService.branchNameExists(bName);
		while(true) {
			if(!bNameExists) {
				System.out.println("Branch not found! Try Again.");
				System.out.print("What branch do you want to update? ");
				bName = scan.nextLine();
				bNameExists = adminService.branchNameExists(bName);
			}
			else {
				break;
			}
		}
		
		int branchId=0;
		for(LibraryBranch lb : libBranches) {
			if(lb.getBranchName().equals(bName))
				branchId = lb.getBranchId();
		}
		
		System.out.print("Update name to: ");
		newBName = scan.nextLine();
		System.out.print("Update address to: ");
		newBAddr = scan.nextLine();
		adminService.updateLibraryBranch(branchId, "'"+newBName+"'", "'"+newBAddr+"'");
		selection = "";
	}
	
	public void adminUpdateBorrower() {
		String borrName, newBorrName, newBorrAddr, newBorrPhone;
		
		List<Borrower> borrowers = adminService.getAllBorrowers();
		printAllBorrowers(borrowers);
		System.out.print("Which borrower do you want to update? ");
		borrName = scan.nextLine();
		
		boolean borrNameExists;
		borrNameExists = adminService.borrowerNameExists(borrName);
		while(true) {
			if(!borrNameExists) {
				System.out.println("Borrower not found! Try Again.");
				System.out.print("Which borrower do you want to update? ");
				borrName = scan.nextLine();
				borrNameExists = adminService.borrowerNameExists(borrName);
			}
			else {
				break;
			}
		}
		
		int borrId=0;
		for(Borrower borr : borrowers) {
			if(borr.getName().equals(borrName))
				borrId = borr.getCardNo();
		}
		
		System.out.print("Update name to: ");
		newBorrName = scan.nextLine();
		System.out.print("Update address to: ");
		newBorrAddr = scan.nextLine();
		System.out.print("Update phone to: ");
		newBorrPhone = scan.nextLine();
		adminService.updateBorrower(borrId, newBorrName, newBorrAddr, newBorrPhone);
		selection = "";
	}
	
	public void adminDeleteOptions() {
		do {
			//skipping validation for this set
			System.out.println("What do you want to delete?");
			System.out.println("1) A book");
			System.out.println("2) A author");
			System.out.println("3) A publisher");
			System.out.println("4) A library branch");
			System.out.println("5) A borrower");
			System.out.println("6) Quit to previous");
			
			selection = scan.nextLine();
			
			if(selection.equals("1")) {
				adminDeleteBook();
				return;
			}
			else if(selection.equals("2")) {
				adminDeleteAuthor();
				return;
			}
			else if(selection.equals("3")) {
				adminDeletePublisher();
				return;
			}
			else if(selection.equals("4")) {
				adminDeleteBranch();
				return;
			}
			else if(selection.equals("5")) {
				adminDeleteBorrower();
				return;
			}
			else if(selection.equals("6")) {
				return;
			}
			else {
				System.out.println("Invalid selection! Try Again."); 
			}
		}
		while(!selection.equals("6"));
	}
	
	public void adminDeleteBook() {
		String title;
		
		printAllBooks();
		System.out.println("Enter book title to delete: ");
		title = scan.nextLine();
		
		List<Book> books = adminService.getAllBooks();
		int bookId=0;
		for(Book b : books) {
			if(b.getTitle().equals(title))
				bookId = b.getBookId();
		}
		adminService.deleteBook(bookId);
		selection = "";
	}
	
	public void adminDeleteAuthor() {
		String authName;
		
		List<Author> authors = adminService.getAllAuthors();
		printAllAuthors(authors);
		System.out.println("Enter author name to delete: ");
		authName = scan.nextLine();
		
		int authId=0;
		for(Author a : authors) {
			if(a.getAuthorName().equals(authName))
				authId = a.getAuthorId();
		}
		adminService.deleteAuthor(authId);
		selection = "";
	}
	
	public void adminDeletePublisher() {
		String pubName;
		
		List<Publisher> publishers = adminService.getAllPublishers();
		printAllPublishers(publishers);
		System.out.println("Enter publisher name to delete: ");
		pubName = scan.nextLine();
		
		int pubId=0;
		for(Publisher p : publishers) {
			if(p.getPublisherName().equals(pubName))
				pubId = p.getPublisherId();
		}
		adminService.deletePublisher(pubId);
		selection = "";
	}
	
	public void adminDeleteBranch() {
		String branchName;
		
		List<LibraryBranch> libBranches = adminService.getAllBranches();
		printAllBranches(libBranches);
		System.out.println("Enter branch name to delete: ");
		branchName = scan.nextLine();
		
		int branchId=0;
		for(LibraryBranch lb : libBranches) {
			if(lb.getBranchName().equals(branchName))
				branchId = lb.getBranchId();
		}
		adminService.deleteLibraryBranch(branchId);
		selection = "";
	}
	
	public void adminDeleteBorrower() {
		String borrName;
		
		List<Borrower> borrowers = adminService.getAllBorrowers();
		printAllBorrowers(borrowers);
		System.out.println("Enter borrower name to delete: ");
		borrName = scan.nextLine();
		
		int borrId=0;
		for(Borrower borr : borrowers) {
			if(borr.getName().equals(borrName))
				borrId = borr.getCardNo();
		}
		adminService.deleteBorrower(borrId);
		selection = "";
	}
	
	//note: doesn't have much validation
	public void adminChangeDueDateLoan() {
		List<BookLoans> bookLoans = adminService.getAllBookLoans();
		Book book;
		LibraryBranch libBranch;
		Borrower borrower;
		
		//remove null entries (people who haven't checked anything out, but have a card)
		Iterator<BookLoans> it = bookLoans.iterator();
		while(it.hasNext()) {
			BookLoans bl = it.next();
			if(bl.getDateOut()==null && bl.getDueDate()==null)
				it.remove();
		}
		
		System.out.println(String.format("%-30s %-5s %-20s %-5s %-18s %-5s %-13s %-5s %-13s", "     Book", "|", "Branch", "|", "Borrower", "|", "DateOut", "|", "DueDate"));
		System.out.println(String.format("%s", "     --------------------------------------------------------------------------------------------------------------------"));
		int i=1;
		for(BookLoans bl : bookLoans) {
			book = adminService.getBook(bl.getBookId());
			libBranch = adminService.getLibBranch(bl.getBranchId());
			borrower = adminService.getBorrower(bl.getCardNo());
			
			System.out.println(String.format("%-30s %-5s %-20s %-5s %-18s %-5s %-13s %-5s %-13s",
				"("+i+")  " + book.getTitle(), "|", 
				libBranch.getBranchName(), "|",
				borrower.getName(), "|",
				bl.getDateOut(), "|",
				bl.getDueDate())
			);
			i++;
		}
		System.out.println();
		
		String temp, newDate;
		System.out.print("Select the loan that you want to change the due date (1-"+(i-1)+"): ");
		temp = scan.nextLine();
		System.out.print("Enter new due date: ");
		newDate = scan.nextLine();  //assume entered in yyyy-mm-dd
		
		int choice = Integer.parseInt(temp);
		int bookId = bookLoans.get(choice-1).getBookId();
		int branchId = bookLoans.get(choice-1).getBranchId();
		int cardNo = bookLoans.get(choice-1).getCardNo();
		Date dateOut = bookLoans.get(choice-1).getDateOut();
		Date dueDate = (Date) DateCalculations.convertStringtoDate(newDate);
		
		adminService.changeDueDate(bookId, branchId, cardNo, dateOut, dueDate);
	}
	
	public void printAllBooks() {
		List<Book> books = adminService.getAllBooks();
		Author author;
		Publisher pub;
		
		System.out.println(String.format("%-30s %-5s %-20s %-5s %-18s", "Book", "|", "Author", "|", "Publisher"));
		System.out.println(String.format("%s", "-------------------------------------------------------------------------------"));
		for(Book b : books) {
			author = adminService.getAuthor(b.getAuthorId());
			pub = adminService.getPublisher(b.getPubId());
			
			System.out.println(String.format("%-30s %-5s %-20s %-5s %-18s",
				"" + b.getTitle(), "|", 
				author.getAuthorName(), "|",
				pub.getPublisherName())
			);
		}
		System.out.println();
	}
	
	public void printAllAuthors(List<Author> authors) {
		System.out.println(String.format("%-12s", "Authors"));
		System.out.println(String.format("%s", "--------------------------"));
		for(Author a : authors) {
			System.out.println(String.format("%-12s", a.getAuthorName()));
		}
		System.out.println();
	}
	
	public void printAllBranches(List<LibraryBranch> libBranches) {
		System.out.println(String.format("%-15s %-5s %-20s", "Branch Name", "|", "Branch Address"));
		System.out.println(String.format("%s", "-----------------------------------------------"));
		for(LibraryBranch lb : libBranches) {
			System.out.println(String.format("%-15s %-5s %-20s", lb.getBranchName(), "|", lb.getBranchAddress()));
		}
		System.out.println();
	}
	
	public void printAllPublishers(List<Publisher> pubs) {
		System.out.println(String.format("%-18s %-5s %-45s %-5s %-15s", "Name", "|", "Address", "|", "Phone"));
		System.out.println(String.format("%s", "-------------------------------------------------------------------------------------------------"));
		for(Publisher p : pubs) {
			System.out.println(String.format("%-18s %-5s %-45s %-5s %-15s", 
				p.getPublisherName(), "|", 
				p.getPublisherAddress(), "|",
				p.getPublisherPhone())
			);
		}
		System.out.println();
	}
	
	public void printAllBorrowers(List<Borrower> borrs) {
		System.out.println(String.format("%-18s %-5s %-45s %-5s %-15s", "Name", "|", "Address", "|", "Phone"));
		System.out.println(String.format("%s", "-------------------------------------------------------------------------------------------------"));
		for(Borrower borr : borrs) {
			System.out.println(String.format("%-18s %-5s %-45s %-5s %-15s", 
				borr.getName(), "|", 
				borr.getAddress(), "|",
				borr.getPhone())
			);
		}
		System.out.println();
	}
}