package fr.d2factory.libraryapp.member;

import fr.d2factory.libraryapp.library.Library;

/**
 * A member is a person who can borrow and return books to a {@link Library}
 * A member can be either a student or a resident
 */
public abstract class Member {
	
	private Long id;
	private String name;
	private String lastName;
    /**
     * An initial sum of money the member has
     */
    private float wallet;
    
    public Member() {
    	
    }
    
    public Member(Long id, String name, String lastName, float wallet) {
    	this.id = id;
    	this.name = name;
    	this.lastName = lastName;
    	this.wallet = wallet;
    } 

    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public float getWallet() {
        return wallet;
    }

    public void setWallet(float wallet) {
        this.wallet = wallet;
    }

	/**
     * The member should pay their books when they are returned to the library
     *
     * @param numberOfDays the number of days they kept the bookn 
	 * @throws MemberException 
     */
    public abstract void payBook(int numberOfDays) throws MemberException;

    public void retrieveAmountFromWallet(float amount) throws MemberException {
		if (getWallet() >= amount) {
			setWallet(wallet - amount);
		} else {
			throw new MemberException("The amount of wallet is insuficient");
		}
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Member other = (Member) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
    
    
    
}
