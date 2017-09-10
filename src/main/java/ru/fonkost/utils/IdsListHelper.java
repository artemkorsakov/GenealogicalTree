/** http://fonkost.ru */
package ru.fonkost.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс работы со списком идентификаторов.
 *
 * @author Артём Корсаков
 */
public final class IdsListHelper {
    /**
     * Разбиение строки на идентификаторы
     *
     * @param str
     *            the str ids
     * @return the list
     */
    public static List<Integer> parse(String str) {
	Pattern p = Pattern.compile(".*\\d.*");
	Matcher m = p.matcher(str);
	if (!m.matches()) {
	    return new ArrayList<Integer>();
	}

	String replaced = str.replaceAll("\\D", " ").replaceAll("\\s+", " ").trim();
	List<Integer> result = new ArrayList<Integer>();
	for (String num : replaced.split(" ")) {
	    result.add(Integer.parseInt(num));
	}
	return result;
    }
}