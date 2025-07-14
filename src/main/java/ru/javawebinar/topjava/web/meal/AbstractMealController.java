package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkIsNew;


public abstract class AbstractMealController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final MealService service;

    public AbstractMealController(MealService service) {
        this.service = service;
    }

    public Meal get(int id) {
        log.info("get meal {}", id);
        return service.get(id, SecurityUtil.authUserId());
    }

    public void delete(int id) {
        log.info("delete meal {}", id);
        service.delete(id, SecurityUtil.authUserId());
    }

    public List<MealTo> getAll() {
        log.info("getAll");
        return MealsUtil.getTos(service.getAll(SecurityUtil.authUserId()),
                SecurityUtil.authUserCaloriesPerDay());
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        checkIsNew(meal);
        return service.create(meal, SecurityUtil.authUserId());
    }

    public void update(Meal meal, int id) {
        log.info("update {}", meal);
        assureIdConsistent(meal, id);
        service.update(meal, SecurityUtil.authUserId());
    }

    /**
     * <ol>Filter separately
     * <li>by date</li>
     * <li>by time for every date</li>
     * </ol>
     */
    public List<MealTo> getBetween(@Nullable LocalDate startDate, @Nullable LocalTime startTime,
                                   @Nullable LocalDate endDate, @Nullable LocalTime endTime) {
        log.info("getBetween dates({} - {}) time({} - {})", startDate, endDate, startTime, endTime);

        List<Meal> mealsDateFiltered = service.getBetweenInclusive(startDate, endDate, SecurityUtil.authUserId());
        return MealsUtil.getFilteredTos(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);
    }
}