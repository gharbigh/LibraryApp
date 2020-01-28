package fr.d2factory.libraryapp.library;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookException;
import fr.d2factory.libraryapp.book.BookIsNotAvailable;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.commons.Constants;
import fr.d2factory.libraryapp.member.Member;
import fr.d2factory.libraryapp.member.MemberException;
import fr.d2factory.libraryapp.member.MemberRepository;
import fr.d2factory.libraryapp.resident.Resident;
import fr.d2factory.libraryapp.student.Student;

public class LibraryImpl implements Library {
	
	private static Map<Member, List<Book>> list = new HashMap<Member, List<Book>>();
	MemberRepository memberRepository = new MemberRepository();
	BookRepository bookRepository = new BookRepository();

	@Override
	public Book borrowBook(long isbnCode, Member member, LocalDate borrowedAt) throws BookIsNotAvailable, BookException, MemberException {
	
			//check if member has borrowed books
			List<Book> memberBorrowedBooks = list.entrySet().stream().filter(x -> x.getKey() == member).flatMap(p -> p.getValue().stream())
			         .collect(Collectors.toList());
			
			if(memberBorrowedBooks.isEmpty()) {
				Book bookToBorrow = bookRepository.findBook(isbnCode);
				
				if(bookToBorrow != null) {
					bookRepository.saveBookBorrow(bookToBorrow, borrowedAt);
					// add to list
					saveInBorrowList(member, new Book(isbnCode));
					return bookToBorrow;
				}
			}else {
				if(hasLateBooks(member)) {
					for(Book book : memberBorrowedBooks) {
						returnBook(book, member);
					}
				}
			}		
		
		return null;
	}

	@Override
	public void returnBook(Book book, Member member) throws BookException, MemberException {
		LocalDate borrowedAt = bookRepository.findBorrowedBookDate(book);
		
		long daysOfBorrow = ChronoUnit.DAYS.between(borrowedAt, LocalDate.now());
		if(member instanceof Student) {
			Student student = (Student) member;
			student.payBook((int)daysOfBorrow);
		}else {
			Resident resident = (Resident) member;
			resident.payBook((int)daysOfBorrow);
		}
		bookRepository.returnBook(book);
		
	}

	@Override
	public boolean hasLateBooks(Member member) {
		List<Book> memberBorrowedBooks = list.entrySet().stream().filter(x -> x.getKey() == member).flatMap(p -> p.getValue().stream())
	             .collect(Collectors.toList());
		int maxDaysOfBorrow = getMaxDaysOfBorrow(member);

		List<Book> lateBooks = memberBorrowedBooks.stream().filter(book -> bookRepository.getBorrowedBooks().get(book).plusDays(maxDaysOfBorrow).isAfter(LocalDate.now()))
	     .collect(Collectors.toList());
		
		if(lateBooks.isEmpty()) {
			throw new HasLateBooksException("Can't borrow. Member has borrowed late book");
		}
		return true;
	}

	public int getMaxDaysOfBorrow(Member member) {
		if(member instanceof Student) {
			return Constants.PERIOD_30;
		}else {
			return Constants.PERIOD_60;
		}
	}
	
	public void saveInBorrowList(Member member, Book book) {
		if (list.containsKey(member)) {
			List<Book> books = list.get(member);
			books.add(book);
		} else {
			List<Book> books = new ArrayList<Book>();
			books.add(book);
			list.put(member, books);
		}

	}
	
	
}
