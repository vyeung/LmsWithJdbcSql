package com.st.lmssql.menus;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import com.st.lmssql.dto.BkCopiesDTO;
import com.st.lmssql.models.LibraryBranch;
import com.st.lmssql.service.LibrarianService;

public class LibrarianMenu {
	
	private Scanner scan = new Scanner(System.in);
	private String selection;
	private LibrarianService libService;
	
	public LibrarianMenu(LibrarianService libService) {
		this.libService = libService;
	}

	public void librarianSubMenu() {
		do {
			System.out.println("1) Pick the Branch you manage");
			System.out.println("2) Quit to previous");
			selection = scan.nextLine();
			
			if(selection.equals("1")) {
				librarianSubMenu2();
			}
			else if(selection.equals("2")) {
				return;
			}
			else {
				System.out.println("Invalid selection! Try Again.");
			}
		}
		while(!selection.equals("2"));
	}
	
	public void librarianSubMenu2() {
		List<LibraryBranch> results = libService.getAllBranches();
		LibraryBranch picked;
		HashSet<String> strSet = new HashSet<>();
		
		do {
			for(int i=0; i<results.size(); i++) {
				String branchName = results.get(i).getBranchName();
				String branchAddr = results.get(i).getBranchAddress();
				System.out.println((i+1) + ") " + branchName + ", " + branchAddr);
				strSet.add(Integer.toString(i+1));
			}
			System.out.println(results.size()+1 + ") Quit to previous"); 
			selection = scan.nextLine();
			
			//ex: 1,2,3,4
			if(strSet.contains(selection)) {
				picked = new LibraryBranch(
					results.get(Integer.parseInt(selection)-1).getBranchId(),
					results.get(Integer.parseInt(selection)-1).getBranchName(),
					results.get(Integer.parseInt(selection)-1).getBranchAddress()
				);
				//pass picked
				librarianSubMenu3(picked);
			}
			else if(selection.equals(Integer.toString(results.size()+1))) {  //selected quit
				return;
			}
			else {
				System.out.println("Invalid selection! Try Again."); 
			}	
		}
		while(!selection.equals(Integer.toString(results.size()+1))); //ex: 5
	}
	
	public void librarianSubMenu3(LibraryBranch picked) {
		do {
			System.out.println();
			System.out.println("1) Update details of your Branch");
			System.out.println("2) Add copies of Book to your Branch");
			System.out.println("3) Quit to previous");
			selection = scan.nextLine();
			
			if(selection.equals("1")) {
				librarianUpdateBranch(picked);
			}
			else if(selection.equals("2")) {
				librarianAddCopies(picked);
			}
			else if(selection.equals("3")) {
				return;
			}
			else {
				System.out.println("Invalid selection! Try Again."); 
			}
		}
		while(!selection.equals("3"));	
	}
	
	public void librarianUpdateBranch(LibraryBranch picked) {
		String newBranchName;
		String newBranchAddr;
		
		System.out.println("You have chosen to update the Branch with BranchId: " + picked.getBranchId() + " and BranchName: " + picked.getBranchName());
		System.out.println("Enter 'quit' at any prompt to cancel operation \n");
		
		System.out.print("Enter new branch name or N/A for no change: ");
		String bName = scan.nextLine();
		
		if(bName.equals("quit")) {
			selection = "";
			return;
		}
		else if(bName.equals("N/A")) {
			newBranchName = "'" + picked.getBranchName() + "'";
		}
		else {
			newBranchName = "'" + bName + "'";
		}
		
		System.out.print("Enter new branch address or N/A for no change: ");
		String bAddr = scan.nextLine();
		
		if(bAddr.equals("quit")) {
			selection = "";
			return;
		}
		else if(bAddr.equals("N/A")) {
			newBranchAddr = "'" + picked.getBranchAddress() + "'";
		}
		else {
			newBranchAddr = "'" + bAddr + "'";
		}
		
		libService.updateBranch(picked.getBranchId(), newBranchName, newBranchAddr);
	}
	
	public void librarianAddCopies(LibraryBranch picked) {
		List<BkCopiesDTO> results = libService.getBookCopiesBookAndTitle(picked.getBranchId());
		HashSet<String> strSet = new HashSet<>();
		
		do {
			System.out.println("Pick the Book you want to add copies of to your Branch:");
			for(int i=0; i<results.size(); i++) {
				String bookTitle = results.get(i).getBook().getTitle();
				String authName =  results.get(i).getAuthor().getAuthorName();
				System.out.println((i+1) + ") " + bookTitle + " by " + authName);
				strSet.add(Integer.toString(i+1));
			}
			System.out.println(results.size()+1 + ") Quit to previous"); 	
			selection = scan.nextLine();
			
			if(strSet.contains(selection)) {
				int N = results.get(Integer.parseInt(selection)-1).getBookCopies().getNoOfCopies();
				System.out.println("Existing number of copies: " + N);
				System.out.print("Enter new number of copies: ");
				String noOfCopies = scan.nextLine(); //assume they enter a valid int, otherwise it breaks here
				
				libService.updateNumCopies(
					results.get(Integer.parseInt(selection)-1).getBookCopies().getBookId(), 
					results.get(Integer.parseInt(selection)-1).getBookCopies().getBranchId(),
					Integer.parseInt(noOfCopies)
				);
				selection = ""; //reset
				return;
			}
			else if(selection.equals(Integer.toString(results.size()+1))) {  //selected quit
				return;
			}
			else {
				System.out.println("Invalid selection! Try Again."); 
			}	
		}
		while(!selection.equals(Integer.toString(results.size()+1)));
	}
}