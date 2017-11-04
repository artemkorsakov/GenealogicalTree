# GenealogicalTree
Программа генерации родословного древа на основе данных Wikipedia.<br><br>
На вход программе передается ссылка на страницу Wikipedia, посвященную определенной личности. Например, на страницу <a href="https://ru.wikipedia.org/wiki/Рюрик">Рюрика</a> - основателя династии Рюриковичей. Используя Selenium Webdriver, программа посещает заданную страницу, определяет детей Рюрика, затем детей детей (внуков) и дальше по поколениям.<br>
Результатом является <a href="http://fonkost.ru/genealogicaltree_tree/rurick">список потомков</a> заданной персоны, построенный на основе данных Wikipedia.<br>
Также реализована генерация родословного древа на основе данных Wikidata. Пример родословного древа <a href="http://fonkost.ru/genealogicaltree_tree/wikidata_rurick">здесь</a>.
Цикл статей по генерации родословного древа доступен <a href="http://fonkost.ru/section/Автоматизация%20управления%20браузером/Генеалогическое%20древо">здесь</a><br>
<a href="https://artemkorsakov.github.io/GenealogicalTree/">Javadoc проекта</a>

