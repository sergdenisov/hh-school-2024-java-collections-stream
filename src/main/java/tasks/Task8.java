package tasks;

import common.Person;
import common.PersonService;
import common.PersonWithResumes;
import common.Resume;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/*
  Еще один вариант задачи обогащения
  На вход имеем коллекцию персон
  Сервис умеет по personId искать их резюме (у каждой персоны может быть несколько резюме)
  На выходе хотим получить объекты с персоной и ее списком резюме
 */
public class Task8 {
  private final PersonService personService;

  public Task8(PersonService personService) {
    this.personService = personService;
  }

  public Set<PersonWithResumes> enrichPersonsWithResumes(Collection<Person> persons) {
    Map<Integer, Set<Resume>> personsResumes = personService
        .findResumes(persons.stream().map(Person::id).toList())
        .stream()
        .collect(Collectors.groupingBy(Resume::personId, Collectors.toSet()));
    return persons
        .stream()
        .map(person -> new PersonWithResumes(person, personsResumes.getOrDefault(person.id(), Set.of())))
        .collect(Collectors.toSet());
  }
}
