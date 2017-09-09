/** http://fonkost.ru */
package ru.fonkost.entities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Класс представителя династии */
public class Person {
    private int id;
    private static int count = 0;
    private String name;
    private URL url;
    private String nameUrl;
    private List<Integer> children = new ArrayList<Integer>();
    private List<Integer> parents = new ArrayList<Integer>();
    private int numberGeneration = 0;

    /** Инициализация персоны по её странице в Wikipedia */
    public Person(String url) throws MalformedURLException {
	setUrl(url);
	count++;
	this.id = count;
    }

    public int getId() {
	return id;
    }

    public String getUrl() {
	return url.toString();
    }

    public void setUrl(String url) throws MalformedURLException {
	this.url = new URL(url);
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	if (name == null || name.trim().isEmpty()) {
	    throw new IllegalArgumentException("Имя должно иметь непустое значение");
	}
	this.name = name;
    }

    /**
     * Возвращает наименование ссылки, которое имеет персона на странице
     * родителя
     */
    public String getNameUrl() {
	return nameUrl;
    }

    public void setNameUrl(String nameUrl) {
	if (nameUrl == null || nameUrl.trim().isEmpty()) {
	    this.nameUrl = "";
	} else {
	    this.nameUrl = nameUrl;
	}
    }

    /**
     * Корректно ли наименование ссылки?! Наименование считается корректным,
     * если начинается не с числа.
     *
     * @return true, if successful
     */
    public boolean isCorrectNameUrl() {
	Pattern p = Pattern.compile("^[\\D]+.+");
	Matcher m = p.matcher(nameUrl);
	return m.matches();
    }

    /**
     * Копирование заданной персоны в текущую. <br>
     * Копируются все поля, кроме наименования ссылки и детей по причине
     * описанной в методе
     * {@link ru.fonkost.pageObjects.PersonPage#getPerson(String) getPerson}
     * класса PersonPage
     */
    public void copyMainData(Person person) {
	this.name = person.name;
	this.url = person.url;
    }

    public List<Integer> getChildren() {
	return children;
    }

    public void setChild(int childId) {
	if (!children.contains(childId)) {
	    children.add(childId);
	}
    }

    public void replaceChild(int oldId, int newId) {
	if (oldId == newId) {
	    return;
	}
	if (!children.contains(oldId)) {
	    return;
	}
	children.remove((Object) oldId);
	setChild(newId);
    }

    /** Возвращает список родителей */
    public List<Integer> getParents() {
	return parents;
    }

    /** Устанавливает идентификатор родителя */
    public void setParent(int parent) {
	parents.add(parent);
    }

    /** Возвращает номер колена */
    public int getNumberGeneration() {
	return numberGeneration;
    }

    /**
     * Устанавливает номер колена. Если номер колена не равен 0, то значит он
     * уже установлен другим родителем и необходимости в его обновлении нет.
     */
    public void setNumberGeneration(int numberGeneration) {
	if (this.numberGeneration == 0) {
	    this.numberGeneration = numberGeneration;
	}
    }

    /** Используется исключительно для тестирования */
    public static void resetCount() {
	count = 0;
    }

    @Override
    public boolean equals(Object object) {
	if ((object == null) || (!(object instanceof Person))) {
	    return false;
	}

	Person person = (Person) object;
	return this.getUrl().equals(person.getUrl());
    }

    @Override
    public int hashCode() {
	return this.getUrl().hashCode();
    }

    @Override
    public String toString() {
	return "name=" + name + "; id=" + id + "; url=" + url + "; nameUrl=" + nameUrl + "; children=" + children;
    }
}