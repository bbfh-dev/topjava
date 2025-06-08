package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MapMealStorage implements MealStorage {
    private final AtomicInteger id = new AtomicInteger();
    private final Map<Integer, Meal> mealsMap = new ConcurrentHashMap<>();

    @Override
    public Collection<Meal> all() {
        return mealsMap.values();
    }

    @Override
    public Meal find(int id) {
        return mealsMap.get(id);
    }

    @Override
    public void create(Meal meal) {
        Integer id = this.id.incrementAndGet();
        meal.setId(id);
        mealsMap.putIfAbsent(id, meal);
    }

    @Override
    public void update(Meal meal) {
        mealsMap.computeIfPresent(meal.getId(), (id, value) -> meal);
    }

    @Override
    public void delete(int id) {
        mealsMap.remove(id);
    }

    @Override
    public void delete(Meal meal) {
        mealsMap.remove(meal.getId());
    }
}
