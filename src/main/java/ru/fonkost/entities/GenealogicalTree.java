/**
 * http://fonkost.ru
 */
package ru.fonkost.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Родословное древо.
 *
 * @author Артём Корсаков
 */
public final class GenealogicalTree {
    private List<Person> AllPersons;
    private int counter;
    private Person currentPerson;

    /**
     * Instantiates a new genealogical tree.
     *
     * @param url
     *            the url
     */
    public GenealogicalTree(String url) {
	AllPersons = new ArrayList<Person>();
	counter = 0;
	currentPerson = new Person(url);
	AllPersons.add(currentPerson);
    }

    /**
     * Существуют ли ещё непосещенные персоны в древе?
     *
     * @return true, если существуют
     */
    public boolean hasUnvisitingPerson() {
	return counter < AllPersons.size();
    }

    /**
     * Возвращает родословное древо
     *
     * @return the all persons
     */
    public List<Person> getAllPersons() {
	return AllPersons;
    }
}