/**
 * http://fonkost.ru
 */
package ru.fonkost.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс исторической личности.
 *
 * @author Артём Корсаков
 */
public class Person {
    private String url;
    private String name;
    private List<Integer> childrens = new ArrayList<Integer>();
    private int id;
    private static int count = 0;

    /**
     * Инициализация экземпляра класса по имени и урлу.
     *
     * @param name
     *            the name
     * @param url
     *            the url
     */
    public Person(String name, String url) throws IllegalArgumentException {
	if (name == null || url == null || name.isEmpty() || url.isEmpty()) {
	    throw new IllegalArgumentException("Оба аргумента должны иметь значение");
	}
	this.name = name;
	this.url = url;
	count++;
	this.id = count;
    }

    /**
     * Возвращает идентификатор персоны.
     *
     * @return идентификатор
     */
    public int getId() {
	return id;
    }

    /**
     * Возвращает список идентификаторов детей.
     *
     * @return детей персоны
     */
    public List<Integer> getChildrens() {
	return childrens;
    }

    /**
     * Добавляет ребенка в список.
     *
     * @param person
     *            персона
     */
    public void setChildren(int childId) {
	if ((childId != id) && (!childrens.contains(childId))) {
	    childrens.add(childId);
	}
    }

    /**
     * Возвращает url.
     *
     * @return the url
     */
    public String getUrl() {
	return url;
    }

    /**
     * Возвращает name.
     *
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * Обнуляет счетчик идентификаторов.
     */
    public static void ResetCount() {
	count = 0;
    }

    @Override
    public boolean equals(Object object) {
	if ((object == null) || (!(object instanceof Person))) {
	    return false;
	}

	Person person = (Person) object;
	return this.url.equals(person.url);
    }

    @Override
    public int hashCode() {
	return this.url.hashCode();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "name=" + name + "; count=" + count + "; id=" + id + "; url=" + url;
    }
}