package fr.d2factory.libraryapp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookException;
import fr.d2factory.libraryapp.book.BookIsNotAvailable;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.library.HasLateBooksException;
import fr.d2factory.libraryapp.library.Library;
import fr.d2factory.libraryapp.library.LibraryImpl;
import fr.d2factory.libraryapp.member.MemberException;
import fr.d2factory.libraryapp.member.MemberRepository;
import fr.d2factory.libraryapp.resident.Resident;
import fr.d2factory.libraryapp.student.Student;

public class Application {
	
	
	public static void main(String[] args) {
		Student student = new Student(1L, "student", "firstGrade", 500, 1);
		Resident resident = new Resident(3L, "res", "resident", 500);

		MemberRepository memberRepository = new MemberRepository();

		try {
			memberRepository.addMember(student);
			memberRepository.addMember(resident);

			resident.retrieveAmountFromWallet(100);
			resident.payBook(66);
		} catch (MemberException e) {
			e.printStackTrace();
		}

		Book book1 = new Book(1L, "title1", "author1");
		Book book2 = new Book(2L, "title2", "author2");
		List<Book> books = new ArrayList<Book>();
		books.add(book1);
		books.add(book2);
		BookRepository bookRep = new BookRepository();
		bookRep.addBooks(books);
		bookRep.getAvailableBooksIsbnName();

		Library lib = new LibraryImpl();
		try {
			Book b = lib.borrowBook(book1.getIsbn().getIsbnCode(), student, LocalDate.now());
		} catch (HasLateBooksException | BookException | MemberException | BookIsNotAvailable e) {
			e.printStackTrace();
		}

		bookRep.getAvailableBooksIsbnName();

	}

}
