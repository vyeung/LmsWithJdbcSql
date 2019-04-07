package com.st.lmssql.app;

import com.st.lmssql.menus.Menu;

public class Main {

	public static void main(String[] args) {
		
		System.out.println("Welcome to the Smoothstack Library Management System. Which category of user are you? \n");
		Menu m = new Menu();
		m.mainMenu();		
	}
}