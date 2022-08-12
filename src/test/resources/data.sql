INSERT INTO FILMS(NAME, DESCRIPTION, RELEASE_DATE, DURATION, RESTRICTION_ID)
VALUES ('Зеленая миля', 'В тюрьме для смертников появляется заключенный с божественным даром',
        '2000-04-18', 189, 3);

INSERT INTO FILMS(NAME, DESCRIPTION, RELEASE_DATE, DURATION, RESTRICTION_ID)
VALUES ('Гарри поттер и узник Азкабана', 'Беглый маг, тайны прошлого и путешествия во времени',
        '2004-06-04', 153, 1);

INSERT INTO GENRES_OF_FILM(FILM_ID, GENRE_ID) VALUES (1, 2);
INSERT INTO GENRES_OF_FILM(FILM_ID, GENRE_ID) VALUES (2, 2);

INSERT INTO USERS(EMAIL, LOGIN, NAME, BIRTHDAY) VALUES ( 'ivan@mail.ru','ivan', 'Ivan', '1997-05-29');
INSERT INTO USERS(EMAIL, LOGIN, NAME, BIRTHDAY) VALUES ( 'test@mail.ru','test', 'test', '1997-05-29');
