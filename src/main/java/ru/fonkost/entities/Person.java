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
    private int numberGeneration = 1;
    private List<Person> childrens = new ArrayList<Person>();
    private int id;
    private int idParent = 0;
    private static int count = 0;

    /**
     * Инициализация экземпляра класса по имени и урлу.
     *
     * @param name
     *            the name
     * @param url
     *            the url
     */
    public Person(String name, String url) throws Exception {
	if (name == null || url == null || name.isEmpty() || url.isEmpty()) {
	    throw new Exception("Оба аргумента должны иметь значение");
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
     * Возвращает идентификатор родителя персоны.
     *
     * @return идентификатор родителя
     */
    public int getIdParent() {
	return idParent;
    }

    /**
     * Возвращает номер поколения, начиная с основателя династии.
     *
     * @return номер поколения
     */
    public int getNumberGeneration() {
	return this.numberGeneration;
    }

    /**
     * Возвращает список детей.
     *
     * @return детей персоны
     */
    public List<Person> getChildrens() {
	return childrens;
    }

    /**
     * Возвращает количество детей.
     *
     * @return количество детей
     */
    public int getCountOfChildrens() {
	return childrens.size();
    }

    /**
     * Добавляет ребенка в список.
     *
     * @param person
     *            персона
     */
    public void setChildren(Person person) {
	if (person.getUrl().equals(url)) {
	    return;
	}

	for (Person child : childrens) {
	    if (person.getUrl().equals(child.getUrl())) {
		return;
	    }
	}

	person.numberGeneration = this.numberGeneration + 1;
	person.idParent = this.id;
	childrens.add(person);
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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "name=" + name + "; count=" + count + "id=" + id + "; numberGeneration=" + numberGeneration
		+ "; getCountOfChildrens=" + getCountOfChildrens() + "; url=" + url;
    }
}