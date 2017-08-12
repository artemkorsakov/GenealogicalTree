/**
 * http://fonkost.ru
 */
package ru.fonkost.entities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс представителя династии
 *
 * @author Артём Корсаков
 */
public class Person {
    private int id;
    private static int count = 0;
    private String name;
    private URL url;
    private String nameUrl;
    private List<Integer> childrens = new ArrayList<Integer>();

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

    public boolean IsCorrectNameUrl() {
	Pattern p = Pattern.compile("^[\\D]+.+");
	Matcher m = p.matcher(nameUrl);
	return m.matches();
    }

    public void copyMainData(Person person) {
	this.name = person.name;
	this.url = person.url;
    }

    public List<Integer> getChildrens() {
	return childrens;
    }

    public void setChild(int childId) {
	if (!childrens.contains(childId)) {
	    childrens.add(childId);
	}
    }

    public void replaceChild(int oldId, int newId) {
	if (oldId == newId) {
	    return;
	}
	if (!childrens.contains(oldId)) {
	    return;
	}
	childrens.remove((Object) oldId);
	setChild(newId);
    }

    public static void ResetCount() {
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
	return "name=" + name + "; id=" + id + "; url=" + url + "; nameUrl=" + nameUrl + "; childrens=" + childrens;
    }
}