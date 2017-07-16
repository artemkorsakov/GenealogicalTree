/**
 * http://fonkost.ru
 */
package ru.fonkost.entities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс ссылки на персону
 *
 * @author Артём Корсаков
 */
public class PersonLink {
    private String name;
    private String url;

    /**
     * Instantiates a new person link.
     *
     * @param name
     *            the name
     * @param url
     *            the url
     * @throws IllegalArgumentException
     *             the illegal argument exception
     */
    public PersonLink(String name, String url) throws IllegalArgumentException {
	if (name == null || url == null || name.isEmpty() || url.isEmpty()) {
	    throw new IllegalArgumentException("Оба аргумента должны иметь значение");
	}
	this.name = name;
	this.url = url;
    }

    /**
     * Возвращает true, если текст элемента начинается не с числа
     */
    public boolean IsCorrectName() {
	Pattern p = Pattern.compile("^[\\D]+.+");
	Matcher m = p.matcher(name);
	return m.matches();
    }

    /**
     * Возвращает имя.
     *
     * @return the name
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
	return url;
    }

    @Override
    public boolean equals(Object object) {
	if ((object == null) || (!(object instanceof PersonLink))) {
	    return false;
	}

	PersonLink personLink = (PersonLink) object;
	return this.url.equals(personLink.url);
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
	return "linkName=" + name + "; linkUrl=" + url;
    }
}