/**
 * Артём Корсаков 
 * site: http://fonkost.ru
 * email: artemkorsakov@mail.ru
 */
package ru.fonkost.entities;

/**
 * Класс исторической личности.
 *
 * @author Артём Корсаков
 */
public class Person {
    private String url;
    private String name;

    /**
     * Инициализация исторической личности пустыми значениями.
     */
    public Person() {
	url = "";
	name = "";
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
     * Устанавливает url.
     *
     * @param url
     *            the new url
     */
    public void setUrl(String url) {
	this.url = url;
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
     * Устанавливает name.
     *
     * @param name
     *            the new name
     */
    public void setName(String name) {
	this.name = name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return name + " (" + url + ")";
    }
}