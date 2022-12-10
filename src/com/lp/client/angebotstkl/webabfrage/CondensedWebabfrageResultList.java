package com.lp.client.angebotstkl.webabfrage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class CondensedWebabfrageResultList implements List<IWebabfrageResult>,
		Serializable {

	private static final long serialVersionUID = 4157246413231074425L;

	private List<CondensedWebabfrageResultItem> heads;
	
	public CondensedWebabfrageResultList(List<IWebabfrageResult> uncondensedList) {
		heads = new ArrayList<CondensedWebabfrageResultItem>();
		for (IWebabfrageResult webabfrageResult : uncondensedList) {
			add(webabfrageResult);
		}
	}

	private CondensedWebabfrageResultItem findMatchingHead(IWebabfrageResult result) {
		for (IWebabfrageResult head : heads) {
			if (matches(head, result)) {
				return (CondensedWebabfrageResultItem) head;
			}
		}
		return null;
	}

	private boolean matches(IWebabfrageResult head, IWebabfrageResult result) {
		if (head.hasArtikelDto() && result.hasArtikelDto() 
//				&& head.getArtikelDto().getIId() == result.getArtikelDto().getIId()) { 
				&& head.getArtikelDto().getIId().equals(result.getArtikelDto().getIId())) { 
			return true;
		}
		
		String headHerstellerArtNr = head.getEinkaufsangebotpositionDto().getCArtikelnrhersteller() == null 
				? "" : head.getEinkaufsangebotpositionDto().getCArtikelnrhersteller();
		String resultHerstellerArtNr = result.getEinkaufsangebotpositionDto().getCArtikelnrhersteller() == null 
				? "" : result.getEinkaufsangebotpositionDto().getCArtikelnrhersteller();
		if (!headHerstellerArtNr.trim().isEmpty() && headHerstellerArtNr.trim().equals(resultHerstellerArtNr.trim())) {
			return true;
		}
		
		String headBez = head.getEinkaufsangebotpositionDto().getCBez() == null 
				? "" : head.getEinkaufsangebotpositionDto().getCBez();
		String resultBez = result.getEinkaufsangebotpositionDto().getCBez() == null 
				? "" : result.getEinkaufsangebotpositionDto().getCBez();
		String headZBez = head.getEinkaufsangebotpositionDto().getCZusatzbez() == null 
				? "" : head.getEinkaufsangebotpositionDto().getCZusatzbez();
		String resultZBez = result.getEinkaufsangebotpositionDto().getCZusatzbez() == null 
				? "" : result.getEinkaufsangebotpositionDto().getCZusatzbez();
		if (!(headBez.isEmpty() && headZBez.isEmpty()) && headBez.equals(resultBez) && headZBez.equals(resultZBez)) {
			return true;
		}
		
		return false;
	}

	@Override
	public boolean add(IWebabfrageResult result) {
		if (result instanceof CondensedWebabfrageResultItem) {
			heads.add((CondensedWebabfrageResultItem) result);
			return true;
		}
		CondensedWebabfrageResultItem head = findMatchingHead(result);
		if (head == null) {
			head = new CondensedWebabfrageResultItem(result);
			heads.add(head);
		} else {
			List<IWebabfrageResult> list = head.getList();
			list.add(result);
			head.setList(list);
		}
		return true;
	}

	public List<IWebabfrageResult> convertToNormalList() {
		List<IWebabfrageResult> list = new ArrayList<IWebabfrageResult>();
		for (CondensedWebabfrageResultItem head : heads) {
			list.addAll(head.getList());
		}
		return list;
	}

	@Override
	public void add(int arg0, IWebabfrageResult arg1) {
		throw new UnsupportedOperationException("use add(IWebabfrageResult e)");
	}

	@Override
	public boolean addAll(Collection<? extends IWebabfrageResult> c) {
		for (IWebabfrageResult result : c) {
			add(result);
		}
		return true;
	}

	@Override
	public boolean addAll(int arg0, Collection<? extends IWebabfrageResult> arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		heads.clear();
	}

	@Override
	public boolean contains(Object o) {
		return heads.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return heads.containsAll(c);
	}

	@Override
	public IWebabfrageResult get(int index) {
		// TODO Auto-generated method stub
		return heads.get(index);
	}

	@Override
	public int indexOf(Object o) {
		return heads.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		return heads.isEmpty();
	}

	@Override
	public Iterator<IWebabfrageResult> iterator() {
		return new ArrayList<IWebabfrageResult>(heads).iterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		return heads.lastIndexOf(o);
	}

	@Override
	public ListIterator<IWebabfrageResult> listIterator() {
		return new ArrayList<IWebabfrageResult>(heads).listIterator();
	}

	@Override
	public ListIterator<IWebabfrageResult> listIterator(int index) {
		return new ArrayList<IWebabfrageResult>(heads).listIterator(index);
	}

	@Override
	public boolean remove(Object o) {
		return heads.remove(o);
	}

	@Override
	public IWebabfrageResult remove(int index) {
		return heads.remove(index);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return heads.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return heads.retainAll(c);
	}

	@Override
	public IWebabfrageResult set(int index, IWebabfrageResult element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		return heads.size();
	}

	@Override
	public List<IWebabfrageResult> subList(int fromIndex, int toIndex) {
		return new ArrayList<IWebabfrageResult>(heads.subList(fromIndex, toIndex));
	}

	@Override
	public Object[] toArray() {
		return heads.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return heads.toArray(a);
	}

}
