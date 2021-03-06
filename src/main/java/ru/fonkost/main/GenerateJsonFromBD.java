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
 * <a href="https://www.wikidata.org/wiki/Q70899">Адама (Wikidata)</a>
 * <a href="https://www.wikidata.org/wiki/Q7990">Рюрика (Wikidata)</a>
 * <a href="https://www.wikidata.org/wiki/Q181915">Романовых (Wikidata)</a>
 * <a href="https://www.wikidata.org/wiki/Q720">Чингисхана (Wikidata)</a>
 * 
 * @author Артём Корсаков
 */
public class GenerateJsonFromBD {
    /**
     * Метод выгружает из БД родословное древо и сохраняет его в Json. Создан
     * для родословных деревьев, сгенерированных до выгрузки в Json.
     */
    public static void main(String[] args) throws Exception {
	if (args == null || args.length != 2) {
	    throw new IllegalArgumentException("Must be two parameters");
	}
	JsonHelper.saveTree(args[0], args[1]);
	System.out.println("Complite");
    }
}