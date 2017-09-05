/** http://fonkost.ru */
package ru.fonkost.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Родословное древо. <br>
 * Класс состоит из списка персон, которые подразделяются на "посещенные" и
 * "непосещенные" персоны. "Разделителем" является идентификатор текущей
 * персоны, который является идентификатором первой "непосещенной" персоны. Все
 * персоны с бОльшим индексом являются "непосещенными", с меньшим -
 * "посещенными". <br>
 * Родословное древо считается построенным, когда нет "непосещенных" персон.
 * <br>
 * <strong style="color: red;">Класс является небезопасным и может работать
 * некорректно, если его использовать вне
 * {@link ru.fonkost.main.GenerateGenealogicalTree#main(String[]) алгоритма
 * генерации родословного древа}. Возможно, более правильным решением был бы
 * перенос данного класса в качестве внутреннего приватного класса для
 * {@link ru.fonkost.main.GenerateGenealogicalTree GenerateGenealogicalTree}.
 * Пока оставлено так для удобства тестирования алгоритма. </strong>
 */
public final class GenealogicalTree {
    private List<Person> allPersons;
    private int indexCurrentUnvisitedPerson;
    private boolean isCurrentPersonDeleted;

    /**
     * Инициализация родословного древа по родоночальнику династии.
     */
    public GenealogicalTree(Person person) {
	if (person == null) {
	    throw new IllegalArgumentException("Укажите непустого основателя династии");
	}
	allPersons = new ArrayList<Person>();
	allPersons.add(person);
	indexCurrentUnvisitedPerson = 0;
	isCurrentPersonDeleted = false;
    }

    /**
     * Заменяет текущую персону на заданную. <br>
     * Если заданная персона уже встречалась раньше, то текущая персона
     * удаляется (даже если она по урлу не равна - в этом случае предполагается,
     * что с url-а текущей персоны происходит редирект на url заданной). После
     * удаления текущая персона помечается, как удаленная. <br>
     * Если заданная персона не встречается раньше, то она "замещает" текущую.
     * При этом персона не считается удаленной. <br>
     * Понятие "встречается раньше" подразумевает, что мы проверяем только
     * "посещенные" персоны. "Непосещенные" не проверяем. Теоретически возможна
     * ситуация, когда url текущей персоны редиректится на url, который может
     * встречатся "позже", среди непосещенных. Но это настолько редкая ситуация,
     * что она не стоит того, чтобы каждый раз "пробегать" по всему массиву. В
     * этом редком случае дубликат все равно удалится, когда до него
     * "дойдет очередь".
     */
    public void setCurrentPerson(Person currentPerson) {
	int indexDuplicate = allPersons.indexOf(currentPerson);
	if ((0 <= indexDuplicate) && (indexDuplicate < indexCurrentUnvisitedPerson)) {
	    removePerson(indexDuplicate);
	} else {
	    allPersons.get(indexCurrentUnvisitedPerson).copyMainData(currentPerson);
	    isCurrentPersonDeleted = false;
	}
    }

    /**
     * Удаление текущей персоны. <br>
     * Перед удалением текущей персоны нужно заменить в списке идентификаторов
     * детей всех "посещенных" персон идентификатор текущей персоны на заданный
     * идентификатор, т.к. подразумевается, что текущая персона является
     * дубликатом заданной. <br>
     * После удаления "текущая" персона помечается удаленной.
     */
    private void removePerson(int indexDuplicate) {
	int idRemovedPerson = allPersons.get(indexCurrentUnvisitedPerson).getId();
	int idDuplicate = allPersons.get(indexDuplicate).getId();
	for (int i = 0; i < indexCurrentUnvisitedPerson; i++) {
	    Person person = allPersons.get(i);
	    person.replaceChild(idRemovedPerson, idDuplicate);
	}
	allPersons.remove(indexCurrentUnvisitedPerson);
	isCurrentPersonDeleted = true;
    }

    /**
     * Добавляет детей текущей персоне. <br>
     * Если текущая персона удалена, то выдается исключение. <br>
     * Если не удалена, то пробегаемся по списку детей: <br>
     * <ul>
     * <li>Если ребенок уже встречается в родословном древе (среди "посещенных"
     * и "непосещенных" персон), то в список детей добавляется идентификатор
     * дубликата</li>
     * <li>Если ребенок не встречается в родословном древе, то в список детей
     * добавляется его идентификатор. Кроме того, сам ребенок добавляется в
     * родословное древо в конец, в список "непосещенных" персон. Т.о.
     * происходит "наполнение" списка за счет детей текущей персоны.</li>
     * </ul>
     */
    public void setChildren(List<Person> children) {
	if (isCurrentPersonDeleted) {
	    throw new IllegalArgumentException("Нельзя установить детей удаленной персоне. Текущая персона уже другая");
	}

	for (Person person : children) {
	    int index = allPersons.indexOf(person);
	    int id;
	    if (index >= 0) {
		id = allPersons.get(index).getId();
	    } else {
		allPersons.add(person);
		id = person.getId();
	    }
	    allPersons.get(indexCurrentUnvisitedPerson).setChild(id);
	}
    }

    /**
     * Обновление счетчика текущей персоны. <br>
     * Если текущая персона удалена, то на "её месте" уже находится следующая
     * "непосещенная" персона, поэтому достаточно просто "снять" признак
     * удаленной персоны с текущей. <br>
     * Если текущая персона не удалена, то считаем её "заполненной" всеми
     * данными и переходим к следующей "непосещенной" персоне.
     */
    public void updatingCurrentPerson() {
	if (isCurrentPersonDeleted) {
	    isCurrentPersonDeleted = false;
	} else {
	    indexCurrentUnvisitedPerson++;
	}
    }

    /** Признак удаленности текущей персоны */
    public boolean isCurrentPersonDeleted() {
	return isCurrentPersonDeleted;
    }

    /**
     * Проверка списка на наличие "непосещенных" персон осуществляется так: если
     * индекс текущей персоны "дошел до конца", то считаем, что "непосещенных"
     * персон не осталось.
     */
    public boolean hasUnvisitingPerson() {
	return indexCurrentUnvisitedPerson < allPersons.size();
    }

    /**
     * Возвращает url родословного древа, в роли которого выступает url текущей
     * персоны.
     */
    public String getCurrentUrl() {
	return allPersons.get(indexCurrentUnvisitedPerson).getUrl();
    }

    /** Возвращает список персон, составляющих родословное древо */
    public List<Person> getGenealogicalTree() {
	return allPersons;
    }
}