package fr.d2factory.libraryapp.book;

/**
 * A simple representation of a book
 */
public class Book {
	
	private String title;	
	private String author;	
	private ISBN isbn;

    public Book() {}

    public Book(long isbn) {
    	this.isbn = new ISBN(isbn);
    }
    
    public Book(long isbn, String title, String author) {
    	this.isbn = new ISBN(isbn);
    	this.title = title;
    	this.author = author;
    }
    
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public ISBN getIsbn() {
		return isbn;
	}

	public void setIsbn(ISBN isbn) {
		this.isbn = isbn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((isbn == null) ? 0 : isbn.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Book other = (Book) obj;
		if (isbn == null) {
			if (other.isbn != null)
				return false;
		} else if (!isbn.equals(other.isbn))
			return false;
		return true;
	}
    
	
    
}
