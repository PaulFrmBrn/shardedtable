# todo markdown 

== Задание

Дан csv-файл, каждая строчка это один платеж: идентификатор отправителя; идентификатор магазина; сумма операции.
Необходимо файл загрузить в N-шардов базы данных. База данных шардированна по нескольким серверам для выполнения требований масштабируемости и отказоустойчивости.

При этом учесть:

Запись в шарды обычно медленнее чтения из файла;
От шарда может быть получена ошибка: connection timeout.

Предоставить сервис по работе с загруженными данными:
* Выдать общую сумму потраченных средств по отправителю;
* Получить TOP-3 отправителей (максимально много заплативших);
* Получить TOP-3 по магазинам (максимально много заработавших).

== Что использовал
* http://spring.io/guides/gs/accessing-data-mongodb/
* https://docs.mongodb.com/manual/tutorial/install-mongodb-on-ubuntu/
* https://www.callicoder.com/reactive-rest-apis-spring-webflux-reactive-mongo/
* https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html