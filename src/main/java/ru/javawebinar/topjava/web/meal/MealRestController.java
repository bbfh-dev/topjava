package ru.javawebinar.topjava.web.meal;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.util.List;

@RestController
@RequestMapping(value = MealRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MealRestController extends AbstractMealController {

    static final String REST_URL = "/rest/profile/meals";

    @GetMapping
    public List<MealTo> getAll() {
        return super.getAll();
    }

    @GetMapping("/{id}")
    public Meal get(@PathVariable int id) {
        return super.get(id);
    }
}
