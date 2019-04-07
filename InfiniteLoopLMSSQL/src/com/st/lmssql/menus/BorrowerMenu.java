package com.st.lmssql.menus;

import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import com.st.lmssql.dto.BkCopiesDTO;
import com.st.lmssql.dto.BkLoansBkAuthDTO;
import com.st.lmssql.dto.BkLoansBranchDTO;
import com.st.lmssql.models.LibraryBranch;
import com.st.lmssql.service.BorrowerService;
import com.st.lmssql.utils.DateCalculations;

public class BorrowerMenu {
	
	private Scanner scan = new Scanner(System.in);
	private String selection;
	private BorrowerService borrowerService;
	
	public BorrowerMenu(BorrowerService borrowerService) {
		this.borrowerService = borrowerService;
	}

	public void borrowerSubMenu(String cardNo) {
		do {
			System.out.println();
			System.out.println("1) Check out a Book");
			System.out.println("2) Return a Book");
			System.out.println("3) Quit to previous");
			
			selection = scan.nextLine();
			
			if(selection.equals("1")) {
				borrCheckoutPickBranch(cardNo);
			}
			else if(selection.equals("2")) {
				borrReturnPickBranch(cardNo);
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
	
	public void borrCheckoutPickBranch(String cardNo) {
		List<LibraryBranch> results = borrowerService.getAllBranches();
		LibraryBranch picked;
		HashSet<String> strSet = new HashSet<>();
		
		do {
			System.out.println("Pick the Branch you want to check out from: ");
			for(int i=0; i<results.size(); i++) {
				String branchName = results.get(i).getBranchName();
				String branchAddr = results.get(i).getBranchAddress();
				System.out.println((i+1) + ") " + branchName + ", " + branchAddr);
				strSet.add(Integer.toString(i+1));
			}
			System.out.println(results.size()+1 + ") Quit to previous"); 	
			selection = scan.nextLine();
			
			if(strSet.contains(selection)) {
				picked = new LibraryBranch(
					results.get(Integer.parseInt(selection)-1).getBranchId(),
					results.get(Integer.parseInt(selection)-1).getBranchName(),
					results.get(Integer.parseInt(selection)-1).getBranchAddress()
				);
				//pass picked
				borrCheckoutPickBook(cardNo, picked);
				selection = "";
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
	
	public void borrCheckoutPickBook(String cardNo, LibraryBranch picked) {
		List<BkCopiesDTO> results = borrowerService.getBkCopiesGreater1BookAndTitle(picked.getBranchId());
		HashSet<String> strSet = new HashSet<>();
		
		do {
			System.out.println("Pick the Book you want to check out: ");
			for(int i=0; i<results.size(); i++) {
				String bookTitle = results.get(i).getBook().getTitle();
				String authName =  results.get(i).getAuthor().getAuthorName();
				System.out.println((i+1) + ") " + bookTitle + " by " + authName);
				strSet.add(Integer.toString(i+1));
			}
			System.out.println(results.size()+1 + ") Quit to previous"); 	
			selection = scan.nextLine();
			
			if(strSet.contains(selection)) {
				Date dateOut = (Date) DateCalculations.getCurrentTime();
				Date dueDate = (Date) DateCalculations.getTodayPlus7();
				borrowerService.checkOutBook(
					results.get(Integer.parseInt(selection)-1).getBookCopies().getBookId(),
					results.get(Integer.parseInt(selection)-1).getBookCopies().getBranchId(),	
					Integer.parseInt(cardNo), 
					dateOut, 
					dueDate,
					results.get(Integer.parseInt(selection)-1).getBookCopies().getNoOfCopies()-1
				);
				selection = ""; //reset before returning. fixes bug where menu may return too far back
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
	
	public void borrReturnPickBranch(String cardNo) {
		List<BkLoansBranchDTO> results = borrowerService.getBranchesWithBkLoans(Integer.parseInt(cardNo));
		LibraryBranch picked;
		HashSet<String> strSet = new HashSet<>();
		
		if(results.size() == 0) {
			System.out.println("You don't have any books checked out!");
			return;
		}
		
		do {
			System.out.println("Pick the Branch you want to return a book to: ");
			for(int i=0; i<results.size(); i++) {
				String branchName = results.get(i).getLibBranch().getBranchName();
				String branchAddr = results.get(i).getLibBranch().getBranchAddress();
				System.out.println((i+1) + ") " + branchName + ", " + branchAddr);
				strSet.add(Integer.toString(i+1));
			}
			System.out.println(results.size()+1 + ") Quit to previous"); 
			selection = scan.nextLine();
			
			if(strSet.contains(selection)) {
				picked = new LibraryBranch(
					results.get(Integer.parseInt(selection)-1).getLibBranch().getBranchId(),
					results.get(Integer.parseInt(selection)-1).getLibBranch().getBranchName(),
					results.get(Integer.parseInt(selection)-1).getLibBranch().getBranchAddress()
				);
				borrReturnPickBook(cardNo, picked);
				selection = "";
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
	
	public void borrReturnPickBook(String cardNo, LibraryBranch picked) {
		List<BkLoansBkAuthDTO> results = borrowerService.getBooksFromBranch(Integer.parseInt(cardNo), picked.getBranchId());
		HashSet<String> strSet = new HashSet<>();
		
		do {
			System.out.println("Pick the Book you want to return: ");
			for(int i=0; i<results.size(); i++) {
				String bookTitle = results.get(i).getBook().getTitle();
				String authName =  results.get(i).getAuthor().getAuthorName();
				System.out.println((i+1) + ") " + bookTitle + " by " + authName);
				strSet.add(Integer.toString(i+1));
			}
			System.out.println(results.size()+1 + ") Quit to previous"); 	
			selection = scan.nextLine();
			
			if(strSet.contains(selection)) {
				int noOfCopies = borrowerService.getNoOfCopies(
					results.get(Integer.parseInt(selection)-1).getBook().getBookId(), 
					picked.getBranchId()
				);
				
				borrowerService.returnBook(
					results.get(Integer.parseInt(selection)-1).getBook().getBookId(),
					picked.getBranchId(),
					Integer.parseInt(cardNo),
					noOfCopies+1
				);	
				selection = "";  //reset
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