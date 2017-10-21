/** http://fonkost.ru */
package ru.fonkost.main;

import ru.fonkost.utils.JsonHelper;

/**
 * Генерация файла в Json-формате для отображения в виде дерева. <br>
 * Примеры:
 * <a href="http://fonkost.ru/genealogicaltree_tree/romanov">Романовы</a>,
 * <a href="http://fonkost.ru/genealogicaltree_tree/rurick">Рюриковичи</a>,
 * <a href="http://fonkost.ru/genealogicaltree_tree/adam">Адам</a>,
 * <a href="http://fonkost.ru/genealogicaltree_tree/genghiskhan">Чингисхан</a>
 *
 * @author Артём Корсаков
 */
public class GenerateJsonFromBD {

    /**
     * Метод выгружает из БД родословное древо и сохраняет его в Json. Создан
     * для родословных деревьев, сгенерированных до выгрузки в Json.
     */
    public static void main(String[] args) throws Exception {
	String tableName = "20170910_rurick";
	JsonHelper.saveTree(tableName, "rurick");
	System.out.println("Complite");
    }
}