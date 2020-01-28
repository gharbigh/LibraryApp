package fr.d2factory.libraryapp.member;

import java.util.ArrayList;
import java.util.List;

public class MemberRepository {

	private List<Member> members = new ArrayList<Member>();

	public void addMember(Member member) throws MemberException {

		if (existMember(member)) {
			throw new MemberException("Member with id=" + member.getId() + " already exists");
		} else {
			members.add(member);
		}

	}

	public boolean existMember(Member member) throws MemberException {

		return members.stream().filter(x -> x.getId().compareTo(member.getId()) == 0).findFirst().isPresent();

	}
	
	public Member findMember(Member member) throws MemberException {

		if (existMember(member)) {
			return member;

		} else {
			throw new MemberException("Unfound member");
		}
	}

	public void removeMember(Member member) {
		members.remove(member);
	}
}
