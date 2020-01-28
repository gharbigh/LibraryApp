package fr.d2factory.libraryapp.resident;

import fr.d2factory.libraryapp.commons.Constants;
import fr.d2factory.libraryapp.member.Member;
import fr.d2factory.libraryapp.member.MemberException;

public class Resident extends Member{
	
	public Resident(Long id, String name, String lastName, float wallet){
		super(id, name, lastName, wallet);
	}

	@Override
	public void payBook(int numberOfDays) throws MemberException {
		if (numberOfDays > Constants.PERIOD_60) {
			retrieveAmountFromWallet((Constants.PERIOD_60 * Constants.AMOUT_10_CENTS)
					+ (numberOfDays - Constants.PERIOD_60) * Constants.AMOUNT_20_CENTS);
		} else {
			retrieveAmountFromWallet(numberOfDays * Constants.AMOUT_10_CENTS);
		}
	}

}
