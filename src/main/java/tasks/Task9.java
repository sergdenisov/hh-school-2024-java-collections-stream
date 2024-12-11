package tasks;

import common.Person;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
Далее вы увидите код, который специально написан максимально плохо.
Постарайтесь без ругани привести его в надлежащий вид
P.S. Код в целом рабочий (не везде), комментарии оставлены чтобы вам проще понять чего же хотел автор
P.P.S Здесь ваши правки необходимо прокомментировать (можно в коде, можно в PR на Github)
 */
public class Task9 {
  // Кажется не нужно свойство в классе объявлять, если оно используется в одном методе только

  // Костыль, эластик всегда выдает в топе "фальшивую персону".
  // Конвертируем начиная со второй
  public List<String> getNames(List<Person> persons) {
    // Проверка на пустоту не нужна
    // Мутировать входящие параметры обычно не рекомендуют во всех языках, кажется .skip() как раз для таких кейсов
    // .toList() создает неизменяемый список, кажется более логичным так вернуть
    return persons.stream().skip(1).map(Person::firstName).toList();
  }

  // Зачем-то нужны различные имена этих же персон (без учета фальшивой разумеется)
  public Set<String> getDifferentNames(List<Person> persons) {
    // .distinct() не нужен, т.к. при создании сета все значения и так станут уникальными
    // Стрим тут не нужен, можно обойтись конструктором
    return new HashSet<>(getNames(persons));
  }

  // Тут фронтовая логика, делаем за них работу - склеиваем ФИО
  public String convertPersonToString(Person person) {
    // Во-первых, лишний раз склеивался .secondName()
    // Во-вторых, через стримы наверное это оптимальнее и более лаконично
    return Stream.of(person.secondName(), person.firstName(), person.middleName()).filter(Objects::nonNull).collect(Collectors.joining(" "));
  }

  // словарь id персоны -> ее имя
  public Map<Integer, String> getPersonNames(Collection<Person> persons) {
    // Лаконичнее через стримы
    return persons.stream().collect(Collectors.toMap(Person::id, this::convertPersonToString, (a, b) -> a));
  }

  // есть ли совпадающие в двух коллекциях персоны?
  // Через сет и .contains будет работать за O(n)
  public boolean hasSamePersons(Collection<Person> persons1, Collection<Person> persons2) {
    return persons1.stream().anyMatch(new HashSet<>(persons2)::contains);
  }

  // Посчитать число четных чисел
  public long countEven(Collection<Integer> numbers) {
    // Передавать стрим нет смысла, после терминальной операции он закрывается и все, лучше передавать коллекцию
    // Кажется нет смысла бегать по стриму, если можно просто взять его размер
    // С count++ не потокобезопасно - свыше могли parallelStream() передать
    return numbers.stream().filter(num -> num % 2 == 0).count();
  }

  // Загадка - объясните почему assert тут всегда верен
  // Пояснение в чем соль - мы перетасовали числа, обернули в HashSet, а toString() у него вернул их в сортированном порядке
  // Просто у Integer его хеш равен числу
  // При таком создании бакетов будет больше чем чисел. Всем хватит. Ну и из-за остатка от деления получится что каждое число в своем бакете
  // А вот toString() обходит по бакетам
  void listVsSet() {
    List<Integer> integers = IntStream.rangeClosed(1, 10000).boxed().collect(Collectors.toList());
    List<Integer> snapshot = new ArrayList<>(integers);
    Collections.shuffle(integers);
    Set<Integer> set = new HashSet<>(integers);
    assert snapshot.toString().equals(set.toString());
  }
}
