/** http://fonkost.ru */
package ru.fonkost.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ru.fonkost.entities.GenealogicalTree;
import ru.fonkost.entities.Person;

/** Проверка класса GenealogicalTree */
public class TestGenealogicalTree {
    private Person rurick;

    @Before
    public void Start() throws MalformedURLException {
	rurick = new Person("https://ru.wikipedia.org/wiki/Рюрик");
    }

    /**
     * Проверка создания родословного древа.<br>
     * Проверка, что родословное древо не создается с персоной равной null.<br>
     * Для корректно созданного родословного древа проверяется следующее:
     * <ul>
     * <li>Текущая персона не удалена</li>
     * <li>В родословном древе есть "непосещенные" персоны.</li>
     * <li>Родословное древо состоит только из родоночальника династии</li>
     * </ul>
     */
    @Test
    public void testCreateGenealogicalTree() {
	try {
	    new GenealogicalTree(null);
	    fail("Создалось древо с нулевым основателем династии");
	} catch (IllegalArgumentException ex) {
	    assertTrue(ex.getMessage().equals("Укажите непустого основателя династии"));
	}

	GenealogicalTree tree = new GenealogicalTree(rurick);
	assertFalse(tree.isCurrentPersonDeleted());
	assertTrue(tree.hasUnvisitingPerson());
	assertTrue(tree.getGenealogicalTree().size() == 1);
	assertTrue(tree.getGenealogicalTree().get(0).equals(rurick));
    }

    /**
     * Проверка установки в качестве текущей ту персону, которая ещё не
     * встречалась в родословном древе.
     */
    @Test
    public void testSetNotContainsPerson() throws MalformedURLException {
	GenealogicalTree tree = new GenealogicalTree(rurick);
	Person rurick2 = new Person("https://ru.wikipedia.org/wiki/Рюрик2");
	rurick2.setName("Рюрик2");
	tree.setCurrentPerson(rurick2);
	List<Person> persons = tree.getGenealogicalTree();

	assertFalse(tree.isCurrentPersonDeleted());
	assertTrue(persons.size() == 1);
	assertTrue(persons.get(0).equals(rurick2));
    }

    /**
     * Проверка установки в качестве текущей персоны саму себя.
     */
    @Test
    public void testSetCurrentPerson() throws MalformedURLException {
	GenealogicalTree tree = new GenealogicalTree(rurick);
	tree.setCurrentPerson(rurick);
	List<Person> persons = tree.getGenealogicalTree();

	assertFalse(tree.isCurrentPersonDeleted());
	assertTrue(persons.size() == 1);
	assertTrue(persons.get(0).equals(rurick));
    }

    /**
     * Проверка установки в качестве текущей ту персону, которая встречается в
     * родословном древе, но после текущей персоны.
     */
    @Test
    public void testSetUnvisitedPerson() throws MalformedURLException {
	GenealogicalTree tree = new GenealogicalTree(rurick);
	Person rurick2 = new Person("https://ru.wikipedia.org/wiki/Рюрик2");
	List<Person> children = new ArrayList<Person>();
	children.add(rurick2);
	tree.setChildren(children);
	tree.setCurrentPerson(rurick2);
	List<Person> persons = tree.getGenealogicalTree();

	assertFalse(tree.isCurrentPersonDeleted());
	assertTrue(persons.size() == 2);
	assertTrue(persons.get(0).equals(rurick2));
	assertTrue(persons.get(1).equals(rurick2));
    }

    /**
     * Проверка корректного удаления персоны, если она не встречается в списке
     * детей у "посещенных" персон.
     */
    @Test
    public void testRemovePerson() throws MalformedURLException {
	GenealogicalTree tree = new GenealogicalTree(rurick);
	List<Person> children = new ArrayList<Person>();
	Person rurick2 = new Person("https://ru.wikipedia.org/wiki/Рюрик2");
	Person rurick3 = new Person("https://ru.wikipedia.org/wiki/Рюрик3");
	Person rurick4 = new Person("https://ru.wikipedia.org/wiki/Рюрик4");
	children.add(rurick2);
	children.add(rurick3);
	children.add(rurick4);
	tree.setChildren(children);
	tree.updatingCurrentPerson();

	tree.setCurrentPerson(rurick);

	assertTrue(tree.isCurrentPersonDeleted());
	List<Person> allPersons = tree.getGenealogicalTree();
	assertTrue(allPersons.size() == 3);
	assertTrue(allPersons.get(0).equals(rurick));
	assertTrue(allPersons.get(1).equals(rurick3));
	assertTrue(allPersons.get(2).equals(rurick4));
	List<Integer> childrens = allPersons.get(0).getChildren();
	assertTrue(childrens.size() == 3);
	assertTrue(childrens.contains(rurick.getId()));
	assertFalse(childrens.contains(rurick2.getId()));
	assertTrue(childrens.contains(rurick3.getId()));
	assertTrue(childrens.contains(rurick4.getId()));
    }

    /**
     * Проверка корректного удаления персоны, если она встречается в списке
     * детей у "посещенных" персон, но при этом "дубликат" также встречается в
     * этом же списке. Идентификатор удаляемой персоны "не новый".
     */
    @Test
    public void testRemovePersonWhenSheIsChild() throws MalformedURLException {
	GenealogicalTree tree = new GenealogicalTree(rurick);
	List<Person> children = new ArrayList<Person>();
	Person rurick2 = new Person("https://ru.wikipedia.org/wiki/Рюрик2");
	Person rurick3 = new Person("https://ru.wikipedia.org/wiki/Рюрик3");
	Person rurick4 = new Person("https://ru.wikipedia.org/wiki/Рюрик4");
	children.add(rurick2);
	children.add(rurick3);
	children.add(rurick4);
	tree.setChildren(children);
	tree.updatingCurrentPerson();
	tree.updatingCurrentPerson();

	tree.setCurrentPerson(rurick2);

	assertTrue(tree.isCurrentPersonDeleted());
	List<Person> allPersons = tree.getGenealogicalTree();
	assertTrue(allPersons.size() == 3);
	assertTrue(allPersons.get(0).equals(rurick));
	assertTrue(allPersons.get(1).equals(rurick2));
	assertTrue(allPersons.get(2).equals(rurick4));
	List<Integer> childrens = allPersons.get(0).getChildren();
	assertTrue(childrens.size() == 2);
	assertTrue(childrens.contains(rurick2.getId()));
	assertFalse(childrens.contains(rurick3.getId()));
	assertTrue(childrens.contains(rurick4.getId()));
    }

    /**
     * Проверка корректного удаления персоны, если она встречается в списке
     * детей у "посещенных" персон. При этом идентификатор удаляемой персоны
     * "новый", который ещё не встречался в списке. Проверка, что "новый"
     * идентификатор удаляемой персоны не добавился в список детей.
     */
    @Test
    public void testRemovePersonWhenSheIsChildButAnotherId() throws MalformedURLException {
	GenealogicalTree tree = new GenealogicalTree(rurick);
	List<Person> children = new ArrayList<Person>();
	Person rurick2 = new Person("https://ru.wikipedia.org/wiki/Рюрик2");
	Person rurick3 = new Person("https://ru.wikipedia.org/wiki/Рюрик3");
	Person rurick4 = new Person("https://ru.wikipedia.org/wiki/Рюрик4");
	children.add(rurick2);
	children.add(rurick3);
	children.add(rurick4);
	tree.setChildren(children);
	tree.updatingCurrentPerson();
	tree.updatingCurrentPerson();

	Person rurick5 = new Person(rurick2.getUrl());
	tree.setCurrentPerson(rurick5);

	assertTrue(tree.isCurrentPersonDeleted());
	List<Person> allPersons = tree.getGenealogicalTree();
	assertTrue(allPersons.size() == 3);
	assertTrue(allPersons.get(0).equals(rurick));
	assertTrue(allPersons.get(1).equals(rurick2));
	assertTrue(allPersons.get(2).equals(rurick4));
	List<Integer> childrens = allPersons.get(0).getChildren();
	assertTrue(childrens.size() == 2);
	assertTrue(childrens.contains(rurick2.getId()));
	assertFalse(childrens.contains(rurick3.getId()));
	assertTrue(childrens.contains(rurick4.getId()));
	assertFalse(childrens.contains(rurick5.getId()));
    }

    /** Проверка корректной установки детей */
    @Test
    public void testSetChildren() throws MalformedURLException {
	List<Person> persons = new ArrayList<Person>();
	Person rurick2 = new Person("https://ru.wikipedia.org/wiki/Рюрик2");
	persons.add(rurick2);
	GenealogicalTree tree = new GenealogicalTree(rurick);
	tree.setChildren(persons);

	List<Person> allPersons = tree.getGenealogicalTree();
	assertTrue(allPersons.size() == 2);
	assertTrue(allPersons.get(0).equals(rurick));
	assertTrue(allPersons.get(1).equals(rurick2));
	List<Integer> childrens = allPersons.get(0).getChildren();
	assertTrue(childrens.size() == 1);
	assertTrue(childrens.contains(rurick2.getId()));
	assertTrue(allPersons.get(1).getChildren().isEmpty());
    }

    /** Проверка невозможности установки детей удаленной персоне */
    @Test
    public void testSetChildrenDeletedPerson() throws MalformedURLException {
	List<Person> persons = new ArrayList<Person>();
	Person rurick2 = new Person("https://ru.wikipedia.org/wiki/Рюрик2");
	persons.add(rurick2);
	GenealogicalTree tree = new GenealogicalTree(rurick);
	tree.setChildren(persons);
	tree.updatingCurrentPerson();
	tree.setCurrentPerson(rurick);
	try {
	    tree.setChildren(persons);
	    fail("Удалось установить детей удаленной персоне");
	} catch (IllegalArgumentException ex) {
	    assertTrue(ex.getMessage().equals("Нельзя установить детей удаленной персоне. Текущая персона уже другая"));
	}
    }

    /** Проверка установки детей, которые уже есть в родословном древе */
    @Test
    public void testSetChildrenWhenChildContains() throws MalformedURLException {
	List<Person> persons = new ArrayList<Person>();
	persons.add(rurick);
	GenealogicalTree tree = new GenealogicalTree(rurick);
	tree.setChildren(persons);
	assertTrue(tree.getGenealogicalTree().get(0).getChildren().size() == 1);
	assertTrue(tree.getGenealogicalTree().get(0).getChildren().get(0) == rurick.getId());
    }

    /** Проверка корректного обновления текущей персоны */
    @Test
    public void testUpdatingCurrentPerson() throws MalformedURLException {
	GenealogicalTree tree = new GenealogicalTree(rurick);
	assertTrue(tree.hasUnvisitingPerson());
	tree.updatingCurrentPerson();
	assertFalse(tree.hasUnvisitingPerson());

	tree = new GenealogicalTree(rurick);
	List<Person> children = new ArrayList<Person>();
	Person rurick2 = new Person("https://ru.wikipedia.org/wiki/Рюрик2");
	Person rurick3 = new Person("https://ru.wikipedia.org/wiki/Рюрик3");
	children.add(rurick2);
	children.add(rurick3);
	tree.setChildren(children);
	tree.updatingCurrentPerson();
	tree.setCurrentPerson(rurick);
	assertTrue(tree.hasUnvisitingPerson());
	assertTrue(tree.isCurrentPersonDeleted());
	tree.updatingCurrentPerson();
	assertFalse(tree.isCurrentPersonDeleted());
	assertTrue(tree.hasUnvisitingPerson());
	tree.updatingCurrentPerson();
	assertFalse(tree.hasUnvisitingPerson());
    }

    @Test
    public void testIsCurrentPersonDeleted() throws MalformedURLException {
	GenealogicalTree tree = new GenealogicalTree(rurick);
	assertFalse(tree.isCurrentPersonDeleted());

	Person rurick2 = new Person("https://ru.wikipedia.org/wiki/Рюрик2");
	Person rurick3 = new Person("https://ru.wikipedia.org/wiki/Рюрик3");
	List<Person> persons = new ArrayList<Person>();
	persons.add(rurick2);
	persons.add(rurick3);
	tree.setChildren(persons);
	tree.updatingCurrentPerson();

	tree.setCurrentPerson(rurick);
	assertTrue(tree.isCurrentPersonDeleted());

	tree.setCurrentPerson(rurick2);
	assertFalse(tree.isCurrentPersonDeleted());
    }

    @Test
    public void testHasUnvisitingPerson() {
	GenealogicalTree tree = new GenealogicalTree(rurick);
	assertTrue(tree.hasUnvisitingPerson());
	tree.updatingCurrentPerson();
	assertFalse(tree.hasUnvisitingPerson());
    }

    @Test
    public void testGetCurrentUrl() {
	GenealogicalTree tree = new GenealogicalTree(rurick);
	assertTrue(tree.getCurrentUrl().equals(rurick.getUrl()));
    }

    @Test
    public void testGetGenealogicalTree() throws MalformedURLException {
	GenealogicalTree tree = new GenealogicalTree(rurick);
	List<Person> children = new ArrayList<Person>();
	Person rurick2 = new Person("https://ru.wikipedia.org/wiki/Рюрик2");
	children.add(rurick2);
	tree.setChildren(children);
	assertTrue(tree.getGenealogicalTree().size() == 2);
	assertTrue(tree.getGenealogicalTree().get(0).equals(rurick));
	assertTrue(tree.getGenealogicalTree().get(1).equals(rurick2));
    }
}