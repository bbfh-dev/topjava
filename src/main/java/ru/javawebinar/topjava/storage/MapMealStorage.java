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
    public Collection<Meal> getAll() {
        return mealsMap.values();
    }

    @Override
    public Meal find(int id) {
        return mealsMap.get(id);
    }

    @Override
    public Meal create(Meal meal) {
        Integer id = this.id.incrementAndGet();
        meal.setId(id);
        mealsMap.putIfAbsent(id, meal);
        return meal;
    }

    @Override
    public Meal update(Meal meal) {
        mealsMap.computeIfPresent(meal.getId(), (id, value) -> meal);
        return meal;
    }

    @Override
    public void delete(int id) {
        mealsMap.remove(id);
    }
}
