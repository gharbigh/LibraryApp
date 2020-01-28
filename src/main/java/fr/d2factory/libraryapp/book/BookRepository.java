package fr.d2factory.libraryapp.book;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookRepository {

	private static Map<ISBN, Book> availableBooks = new HashMap<>();
	private static Map<Book, LocalDate> borrowedBooks = new HashMap<>();

	public void addBooks(List<Book> books) {
		for (Book book : books) {

			availableBooks.put(book.getIsbn(), book);

		}
	}

	public void returnBook(Book book) {
		borrowedBooks.remove(book);
		availableBooks.put(book.getIsbn(), book);
	}

	public Book findBook(long isbnCode) throws BookIsNotAvailable {
		if (availableBooks.containsKey(new ISBN(isbnCode))) {
			return availableBooks.get(new ISBN(isbnCode));
		} else {
			throw new BookIsNotAvailable("Book is not available");
		}
	}

	public void saveBookBorrow(Book book, LocalDate borrowedAt) {
		availableBooks.remove(book.getIsbn());
		borrowedBooks.put(book, borrowedAt);
	}

	public LocalDate findBorrowedBookDate(Book book) throws BookException {
		LocalDate date = borrowedBooks.get(book);
		if (date != null) {
			return date;
		} else {
			throw new BookException("Book is not borrowed");
		}
	}

	public void getAvailableBooksIsbnName() {
		if (availableBooks.isEmpty()) {
			System.out.println("NO AVAILABLE BOOKS");
		} else {
			System.out.println("AVAILABLE BOOKS");
			availableBooks.forEach((key, value) -> System.out
					.println("ISBN: " + value.getIsbn().getIsbnCode() + " TITLE: " + value.getTitle()));
		}
	}

	public void getBorrowedBooksIsbnName() {
		if (borrowedBooks.isEmpty()) {
			System.out.println("NO BORROWED BOOKS");
		} else {
			System.out.println("BORROWED BOOKS");
			borrowedBooks.forEach((key, value) -> System.out
					.println("ISBN: " + key.getIsbn().getIsbnCode() + " TITLE: " + key.getTitle()));
		}

	}

	public Map<Book, LocalDate> getBorrowedBooks() {
		return borrowedBooks;
	}

}
