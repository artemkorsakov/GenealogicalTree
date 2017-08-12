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
    private int indexCurrentUnvisitedPerson;
    private boolean isCurrentPersonDeleted;

    public GenealogicalTree(Person person) {
	if (person == null) {
	    throw new IllegalArgumentException("Укажите непустого основателя династии");
	}
	AllPersons = new ArrayList<Person>();
	AllPersons.add(person);
	indexCurrentUnvisitedPerson = 0;
	isCurrentPersonDeleted = false;
    }

    public void setCurrentPerson(Person currentPerson) {
	int indexDuplicate = AllPersons.indexOf(currentPerson);
	// Персона встречается раньше текущей?
	if ((0 <= indexDuplicate) && (indexDuplicate < indexCurrentUnvisitedPerson)) {
	    removePerson(indexDuplicate); // Значит будет удалена
	} else {
	    // Значит текущая персона будет заменена
	    AllPersons.get(indexCurrentUnvisitedPerson).copyMainData(currentPerson);
	    isCurrentPersonDeleted = false;
	}
    }

    private void removePerson(int indexDuplicate) {
	// Нужно заменить идентификаторы детей на новый айдишник
	int oldIdPerson = AllPersons.get(indexCurrentUnvisitedPerson).getId();
	int newIdPerson = AllPersons.get(indexDuplicate).getId();
	for (int i = 0; i < indexCurrentUnvisitedPerson; i++) {
	    Person person = AllPersons.get(i);
	    person.replaceChild(oldIdPerson, newIdPerson);
	}
	AllPersons.remove(indexCurrentUnvisitedPerson);
	isCurrentPersonDeleted = true;
    }

    public void setChildrens(List<Person> childrens) {
	if (isCurrentPersonDeleted) {
	    throw new IllegalArgumentException("Нельзя установить детей удаленной персоне. Текущая персона уже другая");
	}

	for (Person person : childrens) {
	    int index = AllPersons.indexOf(person);
	    int id;
	    if (index >= 0) {
		id = AllPersons.get(index).getId();
	    } else {
		AllPersons.add(person);
		id = person.getId();
	    }
	    AllPersons.get(indexCurrentUnvisitedPerson).setChild(id);
	}
    }

    public void calculateNextUnvisitingPerson() {
	if (isCurrentPersonDeleted) {
	    isCurrentPersonDeleted = false;
	} else {
	    indexCurrentUnvisitedPerson++;
	}
    }

    public boolean isCurrentPersonDeleted() {
	return isCurrentPersonDeleted;
    }

    public boolean hasUnvisitingPerson() {
	return indexCurrentUnvisitedPerson < AllPersons.size();
    }

    public String getUnvisitingUrl() {
	return AllPersons.get(indexCurrentUnvisitedPerson).getUrl();
    }

    public List<Person> getAllPersons() {
	return AllPersons;
    }
}