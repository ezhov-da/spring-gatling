# spring-gatling

[https://gatling.io/docs/current/extensions/gradle_plugin/](https://gatling.io/docs/current/extensions/gradle_plugin/)
[https://github.com/gatling/gatling-gradle-plugin-demo](https://github.com/gatling/gatling-gradle-plugin-demo)

[https://gatling.io/docs/current/general/simulation_setup/](https://gatling.io/docs/current/general/simulation_setup/)
[https://habr.com/ru/company/tinkoff/blog/344818/](https://habr.com/ru/company/tinkoff/blog/344818/)
[https://www.usenix.org/legacy/event/nsdi06/tech/full_papers/schroeder/schroeder.pdf](https://www.usenix.org/legacy/event/nsdi06/tech/full_papers/schroeder/schroeder.pdf)
[https://gatling.io/docs/current/general/scenario/#scenario-throttling](https://gatling.io/docs/current/general/scenario/#scenario-throttling)

## inject

Определение профиля инъекции пользователей выполняется с помощью метода ```inject```.
Этот метод принимает в качестве аргумента последовательность шагов инъекции, которые будут обрабатываться **последовательно**.

### Открытые и закрытые модели рабочей нагрузки.

Когда дело доходит до модели нагрузки, системы ведут себя двумя разными способами:
- Закрытые системы, в которых вы контролируете одновременное количество пользователей
- Открытые системы, где вы контролируете скорость прихода пользователей

Убедитесь, что вы используете правильную модель нагрузки, которая соответствует нагрузке вашей реальной системы.

Закрытая система - это система, в которой количество одновременных пользователей ограничено.
На полную мощность новый пользователь может эффективно войти в систему только после выхода другого.

Типичные системы, которые ведут себя таким образом:
- колл-центр, где заняты все операторы
- сайты продажи билетов, где пользователи помещаются в очередь, когда система работает на полную мощность

Напротив, открытые системы не контролируют количество одновременных пользователей:
пользователи продолжают приходить, даже если приложения испытывают проблемы с их обслуживанием.
Большинство веб-сайтов ведут себя так.

Если вы используете закрытую модель нагрузки в своих нагрузочных тестах, в то время как ваша система на самом деле является открытой, ваш тест не работает, и вы тестируете какое-то другое воображаемое поведение. В таком случае, когда тестируемая система начинает испытывать проблемы, время отклика увеличивается, время в пути увеличивается, поэтому количество одновременных пользователей увеличивается, а инжектор замедляется, чтобы соответствовать установленному вами воображаемому пределу.

**Важно!**

Открытые и закрытые модели рабочих нагрузок антиномичны, и их нельзя смешивать в одном профиле внедрения.

#### Открытая модель

```
// Указывается длительность паузы duration перед стартом нагрузки
// Пауза на заданную продолжительность
nothingFor(duration)
```

```
// Виртуальные пользователи в количестве nbUsers будут “подниматься” сразу (по готовности)
// Вводит одновременно заданное количество пользователей
atOnceUsers(nbUsers)
```

```
// В течение времени duration будут "подниматься" виртуальные пользователи в количестве nbUsers через равные временные интервалы.
// Вводит заданное количество пользователей, равномерно распределенных во временном окне заданной продолжительности
rampUsers(nbUsers) over(duration)
```

```
// Указывается частота “поднятия” виртуальных пользователей rate (вирт. польз. в секунду) и временной интервал duration.
// В течении duration количество виртуальных пользователей будет увеличиваться на rate каждую секунду.
// Внедряет пользователей с постоянной скоростью, определенной в количестве пользователей в секунду, в течение заданного времени.
// Пользователи будут вводиться через регулярные промежутки времени
constantUsersPerSec(rate) during(duration)
```

```
// Аналогично верхней конструкции только временные интервалы между "поднятием" виртуальных пользователей будут случайными.
// Внедряет пользователей с постоянной скоростью, определенной в количестве пользователей в секунду, в течение заданного времени.
// Пользователи будут вводиться через случайные интервалы
constantUsersPerSec(rate) during(duration) randomized
```

```
// В течение времени duration виртуальные пользователи будут увеличиваться с частоты rate1 до частоты rate2.
// Вводит пользователей от начальной до целевой, определяемой в количестве пользователей в секунду, в течение заданного времени.
// Пользователи будут вводиться через регулярные промежутки времени
rampUsersPerSec(rate1) to (rate2) during(duration)
```

```
// Аналогично верхней конструкции только временные интервалы между "поднятиями" виртуальных пользователей будут случайными.
// Вводит пользователей от начальной до целевой, определяемой в количестве пользователей в секунду, в течение заданного времени.
// Пользователи будут вводиться через случайные интервалы
rampUsersPerSec(rate1) to(rate2) during(duration) randomized
```

```
// Виртуальные пользователи в количестве nbUsers будут подниматься ступенями за время duration.
// Вводит заданное количество пользователей после плавной аппроксимации пошаговой функции тяжелой стороны, растянутой до заданной продолжительности.
// Аппроксима́ция (от лат. proxima — ближайшая) или приближе́ние — научный метод, состоящий в замене одних объектов другими, в каком-то смысле близкими к исходным, но более простыми.
// Аппроксимация позволяет исследовать числовые характеристики и качественные свойства объекта, сводя задачу к изучению более простых или более удобных объектов (например, таких, характеристики которых легко вычисляются или свойства которых уже известны).
heavisideUsers(nbUsers) over(duration)
```

#### Закрытая модель

```
// Внедрить так, чтобы количество одновременных пользователей в системе было постоянным
constantConcurrentUsers(10) during (10 seconds)
```

```
// Внедрить так, чтобы количество одновременных пользователей в системе линейно увеличивалось от одного числа к другому.
rampConcurrentUsers(10) to (20) during (10 seconds)
```

**Предупреждение**

Вы должны понимать, что поведение Gatling по умолчанию - имитировать людей с браузерами, поэтому у каждого виртуального пользователя есть свои собственные соединения.
Если у вас высокий уровень создания пользователей с коротким сроком жизни, вы в конечном итоге будете открывать и закрывать множество подключений каждую секунду.
Как следствие, у вас могут закончиться ресурсы (например, временные порты, потому что ваша ОС не может их перерабатывать достаточно быстро).
Такое поведение имеет смысл, когда нагрузка, которую вы моделируете, представляет собой интернет-трафик.

Если вы на самом деле пытаетесь смоделировать небольшой парк клиентов веб-сервисов с помощью пулов соединений,
вам может потребоваться точная настройка поведения Gatling и совместное использование пула соединений между виртуальными пользователями.

**Предупреждение**

Установка меньшего количества одновременных пользователей не приведет к прерыванию работы существующих пользователей.
Для пользователей единственный способ завершить работу - завершить свой сценарий.

## Meta DSL

```
// генерируем открытый профиль внедрения рабочей нагрузки
// с уровнями 10, 15, 20, 25 и 30 приходящих пользователей в секунду
// каждый уровень длится 10 секунд
// разделены линейными рампами продолжительностью 10 секунд
setUp(
  scn.inject(
    incrementUsersPerSec(5) // Double
      .times(5)
      .eachLevelLasting(10 seconds)
      .separatedByRampsLasting(10 seconds)
      .startingFrom(10) // Double
  )
)
```

```
// генерируем закрытый профиль внедрения рабочей нагрузки
// с уровнями одновременных пользователей 10, 15, 20, 25 и 30
// каждый уровень длится 10 секунд
// разделены линейными рампами продолжительностью 10 секунд
setUp(
  scn.inject(
    incrementConcurrentUsers(5) // Int
      .times(5)
      .eachLevelLasting(10 seconds)
      .separatedByRampsLasting(10 seconds)
      .startingFrom(10) // Int
  )
)
```

```separaByRampsLasting``` и ```startFrom``` являются необязательными.
Если вы не укажете рампу, тест будет переходить с одного уровня на другой сразу после его завершения.
Если вы не укажете количество начальных пользователей, тест начнется с 0 одновременных пользователей или 0 пользователей в секунду и сразу же перейдет к следующему шагу.

## Concurrent Scenarios

Вы можете настроить несколько сценариев в одном блоке ```setUp```, чтобы они запускались одновременно и выполнялись одновременно.

```
setUp(
  scenario1.inject(injectionProfile1),
  scenario2.inject(injectionProfile2)
)
```

## throttle

Если вы хотите рассуждать в терминах запросов в секунду, а не в терминах одновременных пользователей,
рассмотрите возможность использования ```constantUsersPerSec(…)``` для установки скорости поступления пользователей и,
следовательно, запросов без необходимости регулирования, а также в большинстве случаев это будет избыточным.

Если по какой-то причине этого недостаточно, Gatling поддерживает регулирование с помощью метода ```throttle```.

Регулирование реализовано для каждого протокола с поддержкой обычных HTTP и JMS.

**Заметка**
- Вам все равно придется вводить пользователей на уровне сценария.
    Регулирование пытается обеспечить целевую пропускную способность с заданными сценариями и их профилями внедрения
    (количество пользователей и продолжительность). Это узкое место, то есть верхний предел.
    Если у вас недостаточно пользователей, вы не дойдете до дроссельной заслонки.
    Если ваша инъекция длится меньше, чем дроссельная заслонка, ваша симуляция просто остановится, когда все пользователи закончат.
    Если ваш впрыск длится дольше, чем дроссельная заслонка, симуляция остановится в конце дроссельной заслонки.
- Регулирование также можно настроить для каждого сценария.


```
// Эта симуляция достигнет 100 запросов/с с линейным изменением 10 секунд,
// затем удержит эту пропускную способность в течение 1 минуты, перейдет к 50 запросам/с и,
// наконец, удержит эту пропускную способность в течение 2 часов.
setUp(scn.inject(constantUsersPerSec(100) during (30 minutes))).throttle(
  reachRps(100) in (10 seconds),
  holdFor(1 minute),
  jumpToRps(50),
  holdFor(2 hours)
)
```

```
reachRps(target) in (duration)  // нацелить пропускную способность с увеличением в течение заданного времени
jumpToRps(3)                    // немедленно перейти к заданной целевой пропускной способности
holdFor(5 seconds)              // удерживать текущую пропускную способность в течение заданного времени
```

## Maximum duration

Наконец, с помощью maxDuration вы можете принудительно завершить выполнение на основе ограничения продолжительности,
даже если некоторые виртуальные пользователи все еще работают.

Это полезно, если вам нужно ограничить продолжительность симуляции, когда вы не можете ее предсказать.

```
setUp(scn.inject(rampUsers(1000) during (20 minutes))).maxDuration(10 minutes)
```

## Run tests

```
./gradlew application:bootRun
// все тесты
export JAVA_HOME={JAVA_PATH}; rm -rf load-tests/build/reports/; ./gradlew load-tests:gatlingRun
// конкретный тест
export JAVA_HOME={JAVA_PATH}; rm -rf load-tests/build/reports/; ./gradlew load-tests:gatlingRun-ru.ezhov.loadtests.SimpleSimulationIncrementUsersPerSec
```
