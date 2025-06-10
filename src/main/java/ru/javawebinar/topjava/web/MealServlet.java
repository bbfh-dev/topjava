package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.storage.MapMealStorage;
import ru.javawebinar.topjava.storage.MealStorage;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private final MealStorage storage = new MapMealStorage();
    private static final int CALORIES_PER_DAY = 2000;

    @Override
    public void init() {
        Meal[] meals = new Meal[]{
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410),
        };
        Stream.of(meals).forEach(storage::create);
        log.debug("created hard-coded meals: {}", storage.getAll());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        log.info("POST {} with data: {}", request.getRequestURI(), request.getParameterMap().keySet());

        String id = request.getParameter("id");
        String datetime = request.getParameter("datetime");
        String description = request.getParameter("description");
        String calories = request.getParameter("calories");
        if (datetime.isEmpty() || calories.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "fields must not be empty");
            return;
        }

        Meal meal = new Meal(LocalDateTime.parse(datetime), description, Integer.parseInt(calories));
        if (id.isEmpty()) {
            storage.create(meal);
        } else {
            meal.setId(Integer.parseInt(id));
            storage.update(meal);
        }

        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("GET {} with query params: {}", request.getRequestURI(), request.getQueryString());

        String action = request.getParameter("action");
        if (action == null) {
            List<MealTo> mealsTo = MealsUtil.filteredByStreams(new ArrayList<>(storage.getAll()), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY);
            request.setAttribute("meals", mealsTo);

            request.getRequestDispatcher("/meals.jsp").forward(request, response);
            return;
        }

        if (action.equals("delete")) {
            String mealIdString = request.getParameter("meal_id");
            if (mealIdString == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "missing meal_id parameter");
                return;
            }
            int mealId;
            try {
                mealId = Integer.parseInt(mealIdString);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.toString());
                return;
            }

            storage.delete(mealId);
            response.sendRedirect("meals");
            return;
        }

        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }
}
