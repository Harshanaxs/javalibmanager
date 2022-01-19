package com.libraryapp.utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.libraryapp.entities.Book;
import com.libraryapp.entities.User;

@Component
public class FineCalculator {

	@Autowired
	DateTracker dateTracker;
	BigDecimal fineAmount = BigDecimal.valueOf(0.20);
	
	public BigDecimal getFineOfBook(Book<Collection<E>> book) {
		long daysTooLate = dateTracker.daysTooLate(book.getReturnDate());
		BigDecimal fine = fineAmount.multiply(BigDecimal.valueOf(daysTooLate));
		return fine;
	}
	
	//Returns a map of all users with the amount of fines in $.
	public LinkedHashMap<User, BigDecimal> getAllUsersWithFines(List<User> users){
		LinkedHashMap<User, BigDecimal> allUsersWithFines = new LinkedHashMap<User, BigDecimal>();
		
		for (User user : users) {
			
			if (user.getBooks().size() == 0) {
				allUsersWithFines.put(user, BigDecimal.valueOf(0.00));
			
			} else {	
				long totalDaysTooLate = 0;
				for (Book<Collection<E>> book : user.getBooks()) {
					long daysTooLate = dateTracker.differenceInDays(book.getReturnDate());
					if (daysTooLate > 0) totalDaysTooLate += daysTooLate;
				}
				
				BigDecimal totalDaysTooLateInBD = new BigDecimal(totalDaysTooLate);
				BigDecimal totalFineInDollar = fineAmount.multiply(totalDaysTooLateInBD); 
				allUsersWithFines.put(user, totalFineInDollar);
			}
		}	
		return allUsersWithFines;
	}	
	
	//Returns a map of books with the amount of fines in $.
	public LinkedHashMap<Book<Collection<E>>, BigDecimal> getBooksWithFines(List<Book<Collection<E>>> books){
		LinkedHashMap<Book<Collection<E>>, BigDecimal> booksWithFines = new LinkedHashMap<Book<Collection<E>>, BigDecimal>();
		
		for(Book<Collection<E>> book : books) {
			long daysTooLate = dateTracker.differenceInDays(book.getReturnDate());
			if (daysTooLate > 0) {
				BigDecimal daysTooLateInBD = new BigDecimal(daysTooLate);
				BigDecimal totalFineInDollar = fineAmount.multiply(daysTooLateInBD);
				booksWithFines.put(book, totalFineInDollar);
			} else {
				BigDecimal noFine = BigDecimal.valueOf(0.00);
				booksWithFines.put(book, noFine);
			}
		}
		return booksWithFines;
	}
	
	//Returns a map containing only books that have a fine > $0.00.
	public LinkedHashMap<Book<Collection<E>>, BigDecimal> selectBooksWithFines(List<Book<Collection<E>>> books){
		LinkedHashMap<Book<Collection<E>>, BigDecimal> booksWithFines = new LinkedHashMap<Book<Collection<E>>, BigDecimal>();
		
		for (Book<Collection<E>> book : books) {
			long daysTooLate = dateTracker.differenceInDays(book.getReturnDate());
			if (daysTooLate > 0) {
				BigDecimal daysTooLateInBD = new BigDecimal(daysTooLate);
				BigDecimal totalFineInDollar = fineAmount.multiply(daysTooLateInBD);
				booksWithFines.put(book, totalFineInDollar);
			}
		}
		return booksWithFines;
	}
	
	public boolean hasFineOrNot(User user) {
		boolean hasFine = false;
		for (Book<Collection<E>> book : user.getBooks()) {
			if (book.getReturnDate().compareTo(LocalDate.now()) < 0) {
				hasFine = true;
			}
		}
		return hasFine;
	}
	
	public BigDecimal getTotalFine(List<Book<Collection<E>>> books) {
		
		BigDecimal totalFine = BigDecimal.valueOf(0.0);
		
		for (Book<Collection<E>> book : books) {
			long daysTooLate = dateTracker.differenceInDays(book.getReturnDate());
			if (daysTooLate > 0) {
				BigDecimal daysTooLateInBD = new BigDecimal(daysTooLate);
				BigDecimal fineOfBook = fineAmount.multiply(daysTooLateInBD);
				totalFine = totalFine.add(fineOfBook);
			}
		}
		return totalFine;
	}
}
