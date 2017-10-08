/** http://fonkost.ru */
package ru.fonkost.entities;

import java.util.List;

public class JsonPerson {
    private static int count = 0;
    private int id;
    private Person person;
    private int descendants = -1;
    private String format = null;

    public JsonPerson(Person person) {
	this.person = person;
	id = ++count;
    }

    /** Совпадает ли заданный id с id персоны */
    public boolean isPersonId(int id) {
	return person.getId() == id;
    }

    /** Вернуть детей персоны */
    public List<Integer> getChildren() {
	return person.getChildren();
    }

    public int getDescendants() {
	return descendants;
    }

    public void setDescendants(int descendants) {
	this.descendants = descendants;
    }

    public String getFormat() {
	if (format == null) {
	    format = getPersonJson();
	}
	return format;
    }

    public void setFormat(String format) {
	this.format = format;
    }

    /** Вернуть Json персоны */
    private String getPersonJson() {
	StringBuilder sb = new StringBuilder();
	sb.append(getIdJson());
	sb.append(getNameJson());
	sb.append(getDataJson());
	sb.append(getChildrenJson());
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
	sb.append("'>wiki</a>\\\", ");
	return sb.toString();
    }

    /** Вернуть данные для детей персоны */
    private String getChildrenJson() {
	StringBuilder sb = new StringBuilder();
	sb.append("children:[");
	List<Integer> children = person.getChildren();
	if (!children.isEmpty()) {
	    int i = 0;
	    for (int id : children) {
		sb.append("indefinedChild");
		sb.append(id);
		sb.append(" ");
		i++;
		if (i < children.size()) {
		    sb.append(", ");
		}
	    }
	}
	sb.append("]}");
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
}