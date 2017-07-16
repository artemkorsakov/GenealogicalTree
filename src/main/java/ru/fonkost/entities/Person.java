/**
 * http://fonkost.ru
 */
package ru.fonkost.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс представителя династии
 *
 * @author Артём Корсаков
 */
public class Person {
    private int id;
    private static int count = 0;
    private String name;
    private PersonLink link;
    private List<Integer> childrens = new ArrayList<Integer>();

    /**
     * Инициализация экземпляра класса по имени и урлу.
     *
     * @param name
     *            the name
     * @param url
     *            the url
     */
    public Person(String name, PersonLink link) throws IllegalArgumentException {
	if (name == null || link == null || name.isEmpty()) {
	    throw new IllegalArgumentException("Оба аргумента должны иметь значение");
	}
	this.name = name;
	this.link = link;
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
     * Возвращает имя
     *
     * @return имя
     */
    public String getName() {
	return name;
    }

    /**
     * Возвращает url.
     *
     * @return the url
     */
    public String getUrl() {
	return link.getUrl();
    }

    /**
     * Возвращает имя ссылки.
     *
     * @return the url
     */
    public String getUrlName() {
	return link.getName();
    }

    /**
     * Возвращает список идентификаторов детей
     *
     * @return детей персоны
     */
    public List<Integer> getChildrens() {
	return childrens;
    }

    /**
     * Добавляет ребенка в список
     *
     * @param childId
     *            идентификатор ребенка
     */
    public void setChild(int childId) {
	if ((childId != id) && (!childrens.contains(childId))) {
	    childrens.add(childId);
	}
    }

    /**
     * Обнуляет счетчик идентификаторов
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
	return this.link.equals(person.link);
    }

    @Override
    public int hashCode() {
	return this.link.hashCode();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "name=" + name + "; id=" + id + "; link=" + link;
    }
}