package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;

public interface MealStorage {
    Collection<Meal> all();

    Meal find(int id);

    void create(Meal meal);

    void update(Meal meal);

    void delete(int id);

    void delete(Meal meal);
}
