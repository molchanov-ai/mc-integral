# Асинхронный Монте-Карло

Решение задачи поиска наименьшего (наибольшего) локального минимума (максимума)

Первая версия без блокировок со сборщиком сообщений. Используется равномерное распределение для генерации точек функции и Бернулли для определения возможности ответвиться

## TODO

- Распределения для всех этапов
- Визуализация функции
- Многомерные функции
- Определение останова деления
- Распространение энергии по мере продвижения к экстремумам
- Функции с несколькими локальными экстремумам
- Поддержка возможности запуска на разных конфигурациях: [1 поток, несколько потоков, несколько процессов]
- Замеры времени
- Список статей

## Статьи

 - Yves Atchadé, & Liwei Wang. (2021). A fast asynchronous MCMC sampler for sparse Bayesian inference. https://arxiv.org/abs/2108.06446