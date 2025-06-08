<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

<h1>Meals</h1>

<table>
    <thead>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th></th>
        <th></th>
    </tr>
    </thead>
    <tbody>
    <jsp:useBean id="meals" scope="request" type="java.util.List<ru.javawebinar.topjava.model.MealTo>"/>
    <c:forEach items="${meals}" var="meal">
        <tr style="color: ${meal.excess ? 'red' : 'green'};" data-id="${meal.id}">
            <td>${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td>
                <button onclick="showDialog('Edit', this.parentElement.parentElement)">update</button>
            </td>
            <td><a href="?action=delete&meal_id=${meal.id}">[delete]</a></td>
        </tr>
    </c:forEach>
    <tr>
        <td>
            <button onclick="showDialog('Create', null)">new</button>
        </td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
    </tr>
    </tbody>
</table>

<dialog id="dialog">
    <section style="display: flex; flex-direction: column; gap: 1rem;">
        <form method="post" enctype="application/x-www-form-urlencoded" style="display: flex; flex-direction: column; align-items: start; gap: 1rem;">
            <input type="hidden" name="id">
            <h2><span id="dialog-action">_</span> meal</h2>
            <label>
                Date & Time:
                <input name="datetime" type="datetime-local">
            </label>
            <label>
                Description:
                <input name="description" type="text">
            </label>
            <label>
                Calories
                <input name="calories" type="number">
            </label>
            <button type="submit">Save</button>
        </form>
        <form method="dialog">
            <button type="submit">Cancel</button>
        </form>
    </section>
</dialog>

<script defer>
    /** @type {HTMLDialogElement} */
    const dialog = document.getElementById('dialog');
    /** @type {HTMLSpanElement} */
    const dialogAction = document.getElementById("dialog-action");

    /**
     * @param {string} id
     * @param {string} datetime
     * @param {string} description
     * @param {string} calories
     */
    function populateDialogInputs(id, datetime, description, calories) {
        dialog.querySelector(`[name="id"]`).value = id;
        dialog.querySelector(`[name="datetime"]`).value = datetime;
        dialog.querySelector(`[name="description"]`).value = description;
        dialog.querySelector(`[name="calories"]`).value = calories;
    }

    /**
     * @param {string} action
     * @param {HTMLTableRowElement} row
     */
    function showDialog(action, row) {
        if (row === null) {
            populateDialogInputs("", "", "", "");
        } else {
            console.debug("populating from", row);
            console.log(row.children[0].innerText.trim());
            populateDialogInputs(
                row.dataset.id,
                row.children[0].innerText.trim().replace(" ", "T"),
                row.children[1].innerText.trim(),
                row.children[2].innerText.trim(),
            );
        }
        dialogAction.innerText = action;
        dialog.showModal();
    }
</script>
</body>
</html>
