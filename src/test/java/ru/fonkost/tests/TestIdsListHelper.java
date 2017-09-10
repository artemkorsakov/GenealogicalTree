/** http://fonkost.ru */
package ru.fonkost.tests;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import ru.fonkost.utils.IdsListHelper;

/**
 * Проверка класса IdsListHelper.
 *
 * @author Артём Корсаков
 */
public class TestIdsListHelper {
    @Test
    public void testIdsListHelperAvailability() {
	new IdsListHelper();
    }

    @Test
    public void testParse() {
	assertTrue(IdsListHelper.parse("").isEmpty());
	assertTrue(IdsListHelper.parse("[]").isEmpty());
	List<Integer> result = IdsListHelper.parse("3");
	assertTrue(result.size() == 1);
	assertTrue(result.get(0) == 3);
	result = IdsListHelper.parse("3.");
	assertTrue(result.size() == 1);
	assertTrue(result.get(0) == 3);
	result = IdsListHelper.parse("3,1");
	assertTrue(result.size() == 2);
	assertTrue(result.get(0) == 3);
	assertTrue(result.get(1) == 1);
	result = IdsListHelper.parse("[1, 4]");
	assertTrue(result.size() == 2);
	assertTrue(result.get(0) == 1);
	assertTrue(result.get(1) == 4);
    }
}