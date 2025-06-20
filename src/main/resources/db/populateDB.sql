DELETE
FROM user_role;
DELETE
FROM users;
DELETE
FROM meals;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (user_id, datetime, description, calories)
VALUES (100000, '2020-01-30 10:00:00+03', 'Завтрак', 500),
       (100000, '2020-01-30 13:00:00+03', 'Обед', 1000),
       (100000, '2020-01-30 20:00:00+03', 'Ужин', 500),
       (100000, '2020-01-31 00:00:00+03', 'Еда на граничное значение', 100),
       (100000, '2020-01-31 10:00:00+03', 'Завтрак', 1000),
       (100000, '2020-01-31 13:00:00+03', 'Обед', 500),
       (100000, '2020-01-31 20:00:00+03', 'Ужин', 410),
       (100001, '2015-06-01 14:00:00+03', 'Админ ланч', 510),
       (100001, '2015-06-01 21:00:00+03', 'Админ ужин', 1500);
