/**
 * http://fonkost.ru
 */
package ru.fonkost.entities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * В классе описывается ссылка на персону, состоящая из имени и урла. Например,
 * строки "Пётр" и "https://ru.wikipedia.org/wiki/%D0%9F%D1%91%D1%82%D1%80_I"
 * могут задавать ссылку на Петра I
 *
 * @author Артём Корсаков
 */
public class PersonLink {
    private String name;
    private String url;

    /**
     * Инициализация ссылки на персону.
     * 
     *
     * @param name
     *            имя ссылки. При пустом или null значении присваивается
     *            "Неизвестно"
     * @param url
     *            the url
     * @throws IllegalArgumentException
     *             выдается в случае, если url пустой или null
     */
    public PersonLink(String name, String url) throws IllegalArgumentException {
	if (url == null || url.trim().isEmpty()) {
	    throw new IllegalArgumentException("Url должен иметь непустое значение");
	}
	this.url = url;

	if (name == null || name.trim().isEmpty()) {
	    this.name = "Неизвестно";
	} else {
	    this.name = name;
	}
    }

    /**
     * Возвращает true, если имя ссылки начинается не с числа
     */
    public boolean IsCorrectName() {
	Pattern p = Pattern.compile("^[\\D]+.+");
	Matcher m = p.matcher(name);
	return m.matches();
    }

    public String getName() {
	return name;
    }

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

    @Override
    public String toString() {
	return "linkName=" + name + "; linkUrl=" + url;
    }
}