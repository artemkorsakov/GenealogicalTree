/** http://fonkost.ru */
package ru.fonkost.entities;

import java.util.List;

public class JsonPerson {
    private static int count = 0;
    private int id;
    private Person person;
    private long descendants = -1;

    public JsonPerson(Person person) {
	this.person = person;
	id = count++;
    }

    /** Совпадает ли заданный id с id персоны */
    public boolean isPersonId(int id) {
	return person.getId() == id;
    }

    /** Является ли заданный родитель первым? */
    public boolean isFirstParent(int parent) {
	return person.isFirstParent(parent);
    }

    /** Вернуть идентификатор Person */
    public int getPersonId() {
	return person.getId();
    }

    /** Вернуть идентификатор */
    public int getId() {
	return id;
    }

    /** Вернуть детей персоны */
    public List<Integer> getChildren() {
	return person.getChildren();
    }

    public long getDescendants() {
	return descendants;
    }

    public void setDescendants(long descendants) {
	this.descendants = descendants;
    }

    /** Вернуть Json персоны */
    public String getPersonJson() {
	StringBuilder sb = new StringBuilder();
	sb.append(getIdJson());
	sb.append(getNameJson());
	sb.append(getDataJson());
	sb.append(getChildrenJson());
	return sb.toString();
    }

    /** Вернуть Json дубликата */
    public String getDublicateJson(String ancestor, int idParent) {
	StringBuilder sb = new StringBuilder();
	sb.append("{id:\\\"person" + id + "_" + idParent + "\\\", ");
	sb.append("name:\\\"Дубликат (" + person.getTitleName() + ") ");
	sb.append("<a href='http://fonkost.ru/genealogicaltree/");
	sb.append(ancestor + "/" + person.getId() + "'>link</a>\\\", ");
	sb.append("data:{ \\\"name\\\": \\\"Эта персона уже встречается в родословном древе ");
	sb.append("и она детализирована у своего первого родителя\\\" }, ");
	sb.append("children:[]}");
	return sb.toString();
    }

    /** Вернуть данные для идентификатора элемента */
    private String getIdJson() {
	StringBuilder sb = new StringBuilder();
	sb.append("{id:\\\"person");
	sb.append(id);
	sb.append("\\\", ");
	return sb.toString();
    }

    /** Вернуть данные для имени персоны со ссылкой на wiki */
    private String getNameJson() {
	StringBuilder sb = new StringBuilder();
	sb.append("name:\\\"");
	sb.append(person.getTitleName());
	sb.append(" <br><a href='");
	sb.append(person.getUrl());
	sb.append("'>wiki</a>");
	sb.append("\\\", ");
	return sb.toString();
    }

    /** Вернуть данные для всплывающего лэйбла */
    private String getDataJson() {
	StringBuilder sb = new StringBuilder();
	sb.append("data:{ ");
	sb.append("\\\"id\\\": \\\"");
	sb.append(person.getId());
	sb.append("\\\", ");
	sb.append("\\\"name\\\": \\\"");
	sb.append(person.getName());
	sb.append("\\\", ");
	sb.append("\\\"nameUrl\\\": \\\"");
	String nameUrl = person.getNameUrl();
	if (nameUrl == null || nameUrl.isEmpty()) {
	    nameUrl = "-";
	}
	sb.append(nameUrl);
	sb.append("\\\", ");
	sb.append("\\\"numGen\\\": \\\"");
	sb.append(person.getNumberGeneration());
	sb.append("\\\"");
	sb.append(" }, ");
	return sb.toString();
    }

    /** Вернуть данные для детей персоны */
    private String getChildrenJson() {
	return "children:[";
    }

    @Override
    public String toString() {
	return "personId=" + person.getId();
    }

    /** Создан исключительно для тестов */
    public static void resetCount() {
	count = 0;
    }
}