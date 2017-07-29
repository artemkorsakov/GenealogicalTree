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
    private String url;
    private String nameUrl;
    private List<Integer> childrens = new ArrayList<Integer>();

    /**
     * Инициализация представителя династии по имени, урлу и наименовании урла
     *
     * @param name
     *            имя персоны
     * @param url
     *            the url
     * @param nameUrl
     *            имя ссылки
     */
    public Person(String name, String url, String nameUrl) throws IllegalArgumentException {
	setName(name);
	setUrl(url);
	setNameUrl(nameUrl);
	count++;
	this.id = count;
    }

    public int getId() {
	return id;
    }

    public String getName() {
	return name;
    }

    public String getUrl() {
	return url;
    }

    public String getNameUrl() {
	return nameUrl;
    }

    private void setName(String name) {
	if (name == null || name.trim().isEmpty()) {
	    throw new IllegalArgumentException("Имя должно иметь непустое значение");
	}
	this.name = name;
    }

    private void setUrl(String url) {
	if (url == null || url.trim().isEmpty()) {
	    throw new IllegalArgumentException("Url должен иметь непустое значение");
	}
	this.url = url;
    }

    private void setNameUrl(String nameUrl) {
	if (nameUrl == null || nameUrl.trim().isEmpty()) {
	    this.nameUrl = "Неизвестно";
	} else {
	    this.nameUrl = nameUrl;
	}
    }

    public List<Integer> getChildrens() {
	return childrens;
    }

    /**
     * Добавляет ребенка в список только если ребенка там ещё нет
     */
    public void setChild(int childId) {
	if (!childrens.contains(childId)) {
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
	return this.url.equals(person.url);
    }

    @Override
    public int hashCode() {
	return this.url.hashCode();
    }

    @Override
    public String toString() {
	return "name=" + name + "; id=" + id + "; url=" + url + "; nameUrl=" + nameUrl + "; childrens=" + childrens;
    }
}