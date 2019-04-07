package com.st.lmssql.menus;

import java.util.Scanner;

import com.st.lmssql.service.AdminService;
import com.st.lmssql.service.BorrowerService;
import com.st.lmssql.service.LibrarianService;

public class Menu {
	
	static Scanner scan = new Scanner(System.in);
	private static String selection;
	private static String otherInput;
	
	public void mainMenu() {
		do {
			System.out.println("1) Librarian");
			System.out.println("2) Administrator");
			System.out.println("3) Borrower");
			System.out.println("4) Exit");

			selection = scan.nextLine();
			
			if(selection.equals("1")) {
				LibrarianService ls = new LibrarianService(); //making service methods not needing static
				LibrarianMenu lm = new LibrarianMenu(ls);
				lm.librarianSubMenu();
			}
			else if(selection.equals("2")) {
				AdminService as = new AdminService();
				AdminMenu am = new AdminMenu(as);
				am.adminSubMenu();
			}
			else if(selection.equals("3")) {
				BorrowerService bs = new BorrowerService();
				BorrowerMenu bm = new BorrowerMenu(bs);
				boolean cardNoExists;
				
				System.out.print("Enter your Card Number: ");
				otherInput = scan.nextLine();
				cardNoExists = bs.borCardNoExists(Integer.parseInt(otherInput)); //assume input is an int
				
				while(true) {
					if(cardNoExists) {
						break;
					}
					else {
						System.out.println("Card Not Found! Try Again.");
						System.out.print("Enter your Card Number: ");
						otherInput = scan.nextLine();
						cardNoExists = bs.borCardNoExists(Integer.parseInt(otherInput));
					}
				}
				bm.borrowerSubMenu(otherInput);  //valid cardNo entered
			}
			else if(selection.equals("4")) {
				System.out.println("Goodbye!");
				scan.close();
				System.exit(0);
			}
			else {
				System.out.println("Invalid selection! Try Again.");
			}
		}
		while(!selection.equals("4"));
	}
}