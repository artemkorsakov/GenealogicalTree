/** http://fonkost.ru */
package ru.fonkost.utils;

import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ru.fonkost.entities.Person;

/**
 * Класс взаимодействия с локальной базой данной.
 *
 * @author Артём Корсаков
 */
public class MySqlHelper {
    private static final String url = "jdbc:mysql://localhost:3306/genealogicaltree?serverTimezone=UTC&useUnicode=yes&characterEncoding=UTF-8";
    private static final String user = "root";
    private static final String password = "";

    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;

    /**
     * Сохранение родословного древа в заданной таблице.
     *
     * @param tableName
     *            имя таблицы
     * @param tree
     *            родословная
     * @throws MalformedURLException
     *             the malformed URL exception
     */
    public static void saveTree(String tableName, List<Person> tree) throws MalformedURLException {
	try {
	    connection = DriverManager.getConnection(url, user, password);
	    statement = connection.createStatement();

	    String table = createTable(tableName);
	    statement.executeUpdate(table);
	    System.out.println("Created table " + tableName);

	    for (Person person : tree) {
		String insert = insertPerson(tableName, person);
		statement.executeUpdate(insert);
		System.out.println("Insert person " + insert);
	    }
	    System.out.println("Success");
	} catch (SQLException sqlEx) {
	    sqlEx.printStackTrace();
	} finally {
	    try {
		connection.close();
	    } catch (SQLException se) {
	    }
	    try {
		statement.close();
	    } catch (SQLException se) {
	    }
	}
    }

    private static String createTable(String tableName) {
	StringBuilder sql = new StringBuilder();
	sql.append("CREATE TABLE " + tableName + " (");
	sql.append("id INTEGER not NULL, ");
	sql.append("name VARCHAR(255), ");
	sql.append("url VARCHAR(2048), ");
	sql.append("nameUrl VARCHAR(255), ");
	sql.append("children VARCHAR(255), ");
	sql.append("parents VARCHAR(255), ");
	sql.append("numberGeneration INTEGER, ");
	sql.append("PRIMARY KEY ( id ))");
	return sql.toString();
    }

    private static String insertPerson(String tableName, Person person) {
	StringBuilder sql = new StringBuilder();
	sql.append("INSERT INTO genealogicaltree." + tableName);
	sql.append("(id, name, url, nameUrl, children, parents, numberGeneration) \n VALUES (");
	sql.append(person.getId() + ",");
	sql.append("'" + person.getName() + "',");
	sql.append("'" + person.getUrl() + "',");
	sql.append("'" + person.getNameUrl() + "',");
	sql.append("'" + person.getChildren() + "',");
	sql.append("'" + person.getParents() + "',");
	sql.append(person.getNumberGeneration());
	sql.append(");");
	return sql.toString();
    }

    /**
     * Возвращает родословное древо из заданной таблицы. <br>
     * Метод используется только для тестирования корректности работы с БД.
     *
     * @return родословное древо из таблицы
     * @throws Exception
     */
    public static List<Person> getTreeFormNewestTable() throws Exception {
	List<Person> tree = new ArrayList<Person>();
	try {
	    connection = DriverManager.getConnection(url, user, password);
	    statement = connection.createStatement();

	    String queryTableName = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'genealogicaltree' ORDER BY CREATE_TIME DESC";
	    resultSet = statement.executeQuery(queryTableName);
	    if (!resultSet.next()) {
		throw new Exception("Не найдено ни одной таблицы");
	    }
	    String tableName = resultSet.getString("TABLE_NAME");
	    tree = getTreeFormTable(tableName);
	} catch (SQLException sqlEx) {
	    sqlEx.printStackTrace();
	} finally {
	    try {
		connection.close();
	    } catch (SQLException se) {
	    }
	    try {
		statement.close();
	    } catch (SQLException se) {
	    }
	    try {
		resultSet.close();
	    } catch (SQLException se) {
	    }
	}
	return tree;
    }

    /**
     * Возвращает родословное древо из заданной таблицы. <br>
     * Метод используется только для тестирования корректности работы с БД.
     *
     * @return родословное древо из таблицы
     * @throws Exception
     */
    public static List<Person> getTreeFormTable(String tableName) throws Exception {
	List<Person> tree = new ArrayList<Person>();
	try {
	    connection = DriverManager.getConnection(url, user, password);
	    statement = connection.createStatement();

	    String query = "select id, name, url, nameUrl, children, parents, numberGeneration from genealogicaltree."
		    + tableName;
	    resultSet = statement.executeQuery(query);

	    while (resultSet.next()) {
		String url = resultSet.getString("url");
		Person person = new Person(url);

		int id = resultSet.getInt("id");
		person.setId(id);

		String name = resultSet.getString("name");
		person.setName(name);

		String nameUrl = resultSet.getString("nameUrl");
		person.setNameUrl(nameUrl);

		int numberGeneration = resultSet.getInt("numberGeneration");
		person.setNumberGeneration(numberGeneration);

		List<Integer> children = IdsListHelper.parse(resultSet.getString("children"));
		for (int num : children) {
		    person.setChild(num);
		}

		List<Integer> parents = IdsListHelper.parse(resultSet.getString("parents"));
		for (int num : parents) {
		    person.setParent(num);
		}

		tree.add(person);
	    }
	} catch (SQLException sqlEx) {
	    sqlEx.printStackTrace();
	} finally {
	    try {
		connection.close();
	    } catch (SQLException se) {
	    }
	    try {
		statement.close();
	    } catch (SQLException se) {
	    }
	    try {
		resultSet.close();
	    } catch (SQLException se) {
	    }
	}
	return tree;
    }

    /* Обновление формата */
    public static void updateFormat(String tableName, int id, String format) throws SQLException {
	try {
	    connection = DriverManager.getConnection(url, user, password);
	    statement = connection.createStatement();
	    StringBuilder sql = new StringBuilder();
	    sql.append("UPDATE genealogicaltree." + tableName + " \n");
	    sql.append("SET `format` = '" + format.replace("'", "''") + "' ");
	    sql.append("WHERE `id` = " + id);
	    sql.append(";");
	    statement.executeUpdate(sql.toString());
	} catch (SQLException sqlEx) {
	    sqlEx.printStackTrace();
	    throw sqlEx;
	} finally {
	    try {
		connection.close();
	    } catch (SQLException se) {
	    }
	    try {
		statement.close();
	    } catch (SQLException se) {
	    }
	}
    }

    /* Получение формата */
    public static String getFormat(String tableName, int id) throws Exception {
	try {
	    connection = DriverManager.getConnection(url, user, password);
	    statement = connection.createStatement();

	    String queryTableName = "SELECT FORMAT FROM genealogicaltree." + tableName + " \n WHERE `id` = " + id + ";";
	    resultSet = statement.executeQuery(queryTableName);
	    if (!resultSet.next()) {
		throw new Exception("Формат у данной персоны не установлен");
	    }
	    String format = resultSet.getString("FORMAT");
	    return format.replace("\"", "\\\"");
	} catch (SQLException sqlEx) {
	    sqlEx.printStackTrace();
	} finally {
	    try {
		connection.close();
	    } catch (SQLException se) {
	    }
	    try {
		statement.close();
	    } catch (SQLException se) {
	    }
	}
	throw new Exception("Не удалось получить формат");
    }
}