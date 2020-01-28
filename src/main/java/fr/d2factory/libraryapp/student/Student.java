package fr.d2factory.libraryapp.student;

import fr.d2factory.libraryapp.commons.Constants;
import fr.d2factory.libraryapp.member.Member;
import fr.d2factory.libraryapp.member.MemberException;

public class Student extends Member{
	
	private int grade;

	public Student(Long id, String name, String lastName, float wallet, int grade) {
		super(id, name, lastName, wallet);
		this.grade = grade;
	}
	
	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	@Override
	public void payBook(int numberOfDays) throws MemberException{
		
		if (getGrade() != Constants.FIRST_CLASS) {
			retrieveAmountFromWallet(numberOfDays * Constants.AMOUT_10_CENTS);
		} else if(numberOfDays>Constants.PERIOD_15) {
			retrieveAmountFromWallet((numberOfDays - Constants.PERIOD_15) * Constants.AMOUT_10_CENTS);
		}
		
	}

	
	
}
