package com.st.lmssql.models;

public class Book {
	private int bookId;
	private String title;
	private int authoId;
	private int pubId;
	
	public Book() {
	}
	
	public Book(int bookId, String title, int authoId, int pubId) {
		super();
		this.bookId = bookId;
		this.title = title;
		this.authoId = authoId;
		this.pubId = pubId;
	}
	
	public int getBookId() {
		return bookId;
	}
	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getAuthoId() {
		return authoId;
	}
	public void setAuthoId(int authoId) {
		this.authoId = authoId;
	}
	public int getPubId() {
		return pubId;
	}
	public void setPubId(int pubId) {
		this.pubId = pubId;
	}
}