package ru.javawebinar.topjava.repository.inmemory;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.repository.inmemory.InMemoryUserRepository.DEFAULT_USER_ID;

public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> usersToMealsMap = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> this.save(meal, DEFAULT_USER_ID));
    }

    private Map<Integer, Meal> getOrInitMealsMap(int userId) {
        return usersToMealsMap.computeIfAbsent(userId, id -> new ConcurrentHashMap<>());
    }

    @Override
    public Meal save(Meal meal, int userId) {
        Map<Integer, Meal> mealsMap = this.getOrInitMealsMap(userId);

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            mealsMap.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return mealsMap.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        Map<Integer, Meal> mealsMap = this.getOrInitMealsMap(userId);
        return mealsMap.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Map<Integer, Meal> mealsMap = this.getOrInitMealsMap(userId);
        return mealsMap.get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        Map<Integer, Meal> mealsMap = this.getOrInitMealsMap(userId);
        return mealsMap.values().stream()
                .sorted(Comparator.comparing(Meal::getDate).reversed())
                .collect(Collectors.toList());
    }
}

