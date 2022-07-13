package application.storage;


import application.model.Film;
import org.springframework.stereotype.Component;


@Component
public class InMemoryFilmStorage extends CommonStorage<Film>{
}
