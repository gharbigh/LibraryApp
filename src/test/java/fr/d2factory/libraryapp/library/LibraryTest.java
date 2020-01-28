package fr.d2factory.libraryapp.library;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookException;
import fr.d2factory.libraryapp.book.BookIsNotAvailable;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.commons.Constants;
import fr.d2factory.libraryapp.member.Member;
import fr.d2factory.libraryapp.member.MemberException;
import fr.d2factory.libraryapp.resident.Resident;
import fr.d2factory.libraryapp.student.Student;

/**
 * Do not forget to consult the README.md :)
 */
public class LibraryTest {
    private Library library ;
    private BookRepository bookRepository;
    private static List<Book> books;
    private Member studentFirstGrade;
    private Member studentSecGrade;
    private Member resident;


    @BeforeEach
    void setup() throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        File booksJson = new File("src/test/resources/books.json");
        books = mapper.readValue(booksJson, new TypeReference<List<Book>>() {
        });
        
        studentFirstGrade = new Student(1L, "Stud", "first", 500, 1);
        studentSecGrade = new Student(2L, "Stud", "second", 500, 2);
        resident = new Resident(1L, "resid", "resident", 500);
    	bookRepository = new BookRepository();
    	bookRepository.addBooks(books);    	
    	library = new LibraryImpl();
        
    }

    @Test
    void member_can_borrow_a_book_if_book_is_available() throws HasLateBooksException, Exception{
    	Book bookTobeBorrowed = books.get(0);
    	assertEquals(bookTobeBorrowed, 
    			library.borrowBook(bookTobeBorrowed.getIsbn().getIsbnCode(), studentFirstGrade, LocalDate.now()));
    }

    @Test
    void borrowed_book_is_no_longer_available() throws HasLateBooksException, BookException, MemberException, BookIsNotAvailable{
    	Book borrowedBook = library.borrowBook(books.get(0).getIsbn().getIsbnCode(), studentFirstGrade, LocalDate.now());
    	Assertions.assertThrows(BookIsNotAvailable.class, 
    			() -> bookRepository.findBook(borrowedBook.getIsbn().getIsbnCode()));
    }

    @Test
    void residents_are_taxed_10cents_for_each_day_they_keep_a_book() throws HasLateBooksException, Exception{
    	LocalDate date = LocalDate.now();  
        LocalDate dateOfBorrow = date.minusDays(Constants.PERIOD_15);
    	float wallet1 = resident.getWallet();
    	resident.payBook((int) ChronoUnit.DAYS.between(dateOfBorrow, LocalDate.now()));
    	assertEquals(wallet1-Constants.PERIOD_15*Constants.AMOUT_10_CENTS, resident.getWallet());
    }

    @Test
    void students_pay_10_cents_the_first_30days() throws MemberException{
    	LocalDate date = LocalDate.now();  
        LocalDate dateOfBorrow = date.minusDays(Constants.PERIOD_30);
        float wallet1 = studentFirstGrade.getWallet();
        studentFirstGrade.payBook((int) ChronoUnit.DAYS.between(dateOfBorrow, LocalDate.now()));
        assertEquals(wallet1-(15*Constants.AMOUT_10_CENTS), studentFirstGrade.getWallet());
    }

    @Test
    void students_in_1st_year_are_not_taxed_for_the_first_15days() throws MemberException{
        LocalDate date = LocalDate.now();  
        LocalDate dateOfBorrow = date.minusDays(Constants.PERIOD_15);
        float wallet1 = studentFirstGrade.getWallet();
        studentSecGrade.payBook((int) ChronoUnit.DAYS.between(dateOfBorrow, LocalDate.now()));
        assertEquals(wallet1, studentFirstGrade.getWallet());
    }
    
	@Test
	void residents_pay_20cents_for_each_day_they_keep_a_book_after_the_initial_60days() throws MemberException {
		LocalDate date = LocalDate.now();
		LocalDate dateOfBorrow = date.minusDays(66);
		float wallet1 = resident.getWallet();
		resident.payBook((int) ChronoUnit.DAYS.between(dateOfBorrow, LocalDate.now()));
		assertEquals(wallet1 - (Constants.PERIOD_60 * Constants.AMOUT_10_CENTS) - (6 * Constants.AMOUNT_20_CENTS),
				resident.getWallet());
	}

    @Test
    void members_cannot_borrow_book_if_they_have_late_books() throws HasLateBooksException, Exception{
    	LocalDate dateOfBorrow = LocalDate.now().minusDays(66);
    	library.borrowBook(books.get(0).getIsbn().getIsbnCode(), resident, dateOfBorrow);
    	Assertions.assertThrows(HasLateBooksException.class, 
    			() ->  library.borrowBook(books.get(3).getIsbn().getIsbnCode(), resident, dateOfBorrow));
    }
}
