package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;

public interface MealStorage {
    Collection<Meal> all();

    Meal find(int id);

    Meal create(Meal meal);

    Meal update(Meal meal);

    void delete(int id);
}
