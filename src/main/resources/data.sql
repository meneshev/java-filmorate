INSERT INTO PUBLIC."USER" (USER_EMAIL, USER_LOGIN, USER_NAME, USER_BIRTHDAY)
SELECT t.* FROM (
                    VALUES
                        ('user1@mail.com', 'user1_login', 'Name User1', '1995-03-30'),
                        ('user2@mail.com', 'user2_login', 'Name User2', '1996-03-30'),
                        ('user3@mail.com', 'user3_login', 'Name User3', '1997-03-30'),
                        ('user4@mail.com', 'user4_login', 'Name User4', '1998-03-30'),
                        ('user5@mail.com', 'user5_login', 'Name User5', '1999-03-30'),
                        ('user6@mail.com', 'user6_login', 'Name User6', '2000-03-30'),
                        ('user7@mail.com', 'user7_login', 'Name User7', '2001-03-30'),
                        ('user8@mail.com', 'user8_login', 'Name User8', '2002-03-30'),
                        ('user9@mail.com', 'user9_login', 'Name User9', '2003-03-30'),
                        ('user10@mail.com', 'user10_login', 'Name User10', '2004-03-30'),
                        ('user11@mail.com', 'user11_login', 'Name User11', '2005-03-30'),
                        ('user12@mail.com', 'user12_login', 'Name User12', '2006-03-30'),
                        ('user13@mail.com', 'user13_login', 'Name User13', '2007-03-30'),
                        ('user14@mail.com', 'user14_login', 'Name User14', '2007-03-30'),
                        ('user15@mail.com', 'user15_login', 'Name User15', '2007-03-30')
                    ) AS t(EMAIL, LOGIN, NAME, BIRTHDAY)
WHERE NOT EXISTS (SELECT 1 FROM PUBLIC."USER" WHERE USER_EMAIL = t.EMAIL);

INSERT INTO PUBLIC.GENRE (GENRE_NAME)
SELECT t.* FROM (
                    VALUES
                        ('Комедия'),
                        ('Драма'),
                        ('Мультфильм'),
                        ('Триллер'),
                        ('Документальный'),
                        ('Боевик')
                    ) AS t(gname)
WHERE NOT EXISTS (SELECT 1 FROM PUBLIC.GENRE WHERE GENRE_NAME = t.gname);

INSERT INTO PUBLIC.MPA (MPA_NAME)
SELECT t.* FROM (
                    VALUES
                        ('G'),
                        ('PG'),
                        ('PG-13'),
                        ('R'),
                        ('NC-17')
                    ) AS t(mpaname)
WHERE NOT EXISTS (SELECT 1 FROM PUBLIC.MPA WHERE MPA_NAME = t.mpaname);

INSERT INTO PUBLIC.FILM (FILM_NAME, FILM_DESCRIPTION, FILM_RELEASEDATE, FILM_DURATION, RATING_ID)
SELECT t.* FROM (
                    VALUES
                        ('Commando', 'A retired special agent named John Matrix led an elite unit and has left the armed forces to live in a secluded mountain home with his daughter Jenny', '1985-10-04', 90, 4),
                        ('Die Hard', 'A New York City cop, John McClane, tries to save his estranged wife and several others taken hostage by terrorists during a Christmas Eve party at the Nakatomi Plaza Skyscraper in Los Angeles, California.', '1988-07-15', 92, 4),
                        ('Toy Story', 'Toy Story is a 1995 American animated adventure comedy film produced by Pixar Animation Studios for Walt Disney Pictures.', '1995-11-22', 81, 1),
                        ('Old School', 'Old School is a 2003 American comedy film directed and co-written by Todd Phillips.', '2003-02-01', 90, 4),
                        ('The Shawshank Redemption', 'The Shawshank Redemption is a 1994 American prison drama film written and directed by Frank Darabont, based on the 1982 Stephen King novella Rita Hayworth and Shawshank Redemption.', '1994-09-23', 142, 4),
                        ('The Lord of the Rings: The Fellowship of the Ring', 'The Lord of the Rings: The Fellowship of the Ring is a 2001 epic high fantasy adventure film directed by Peter Jackson from a screenplay by Fran Walsh, Philippa Boyens, and Jackson, based on J. R. R. Tolkien''s 1954 The Fellowship of the Ring', '2001-12-19', 178, 4),
                        ('Gladiator', 'Gladiator is a 2000 epic historical drama film directed by Ridley Scott and written by David Franzoni, John Logan, and William Nicholson from a story by Franzoni', '2000-05-01', 171, 4),
                        ('All That Heaven Allows', 'All That Heaven Allows is a 1955 American melodrama film directed by Douglas Sirk, produced by Ross Hunter, and adapted by Peg Fenwick from a novel by Edna L. Lee and Harry Lee', '1955-12-25', 89, 2),
                        ('Moulin Rouge!', 'Moulin Rouge! is a 2001 jukebox musical romantic drama film directed, produced, and co-written by Baz Luhrmann.', '2000-06-01', 128, 3),
                        ('Blood Simple', 'Blood Simple is a 1984 American independent neo-noir crime film written, edited, produced and directed by Joel and Ethan Coen, and starring John Getz, Frances McDormand, Dan Hedaya and M. Emmet Walsh', '1984-10-12', 96, 4),
                        ('Ghost', 'Ghost is a 1990 American supernatural romance film directed by Jerry Zucker, written by Bruce Joel Rubin, and starring Patrick Swayze, Demi Moore, Whoopi Goldberg, and Tony Goldwyn', '1990-07-13', 127, 4),
                        ('Fight Club', 'Fight Club is a 1999 American film directed by David Fincher and starring Brad Pitt, Edward Norton, and Helena Bonham Carter', '1999-10-15', 139, 4)
                    ) AS t(NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING)
WHERE NOT EXISTS (
    SELECT 1 FROM PUBLIC.FILM
    WHERE FILM_NAME = t.NAME
);

INSERT INTO PUBLIC.FILM_GENRES (FILM_ID, GENRE_ID)
SELECT t.* FROM (
                    VALUES
                        (1,6), (1,4), (2,6), (2,4),
                        (3,3), (4,1), (5,2), (6,6),
                        (7,5), (8,2), (9,2), (10,4),
                        (11,5), (11,2), (12,2), (12,4)
                    ) AS t(FILM_ID, GENRE_ID)
WHERE NOT EXISTS (
    SELECT 1 FROM PUBLIC.FILM_GENRES
    WHERE FILM_ID = t.FILM_ID
      AND GENRE_ID = t.GENRE_ID
);

INSERT INTO PUBLIC.FILM_LIKES (FILM_ID, USER_ID)
SELECT t.* FROM (
                    VALUES
                        (1, 3), (1, 4), (1, 5),
                        (4, 1), (4, 2), (4, 3), (4, 4),
                        (9, 15), (8, 12), (8, 11), (8, 10),
                        (8, 9), (8, 8), (8, 7), (6, 3),
                        (6, 5), (6, 6), (6, 9), (6, 7),
                        (7, 4), (7, 3), (7, 2), (7, 1),
                        (1, 2), (1, 1)
                    ) AS t(FILM_ID, USER_ID)
WHERE NOT EXISTS (
    SELECT 1 FROM PUBLIC.FILM_LIKES
    WHERE FILM_ID = t.FILM_ID
      AND USER_ID = t.USER_ID
);

INSERT INTO PUBLIC.FRIEND (ADDING_USER_ID, ADDED_USER_ID, ISACCEPTED)
SELECT t.* FROM (
                    VALUES
                        (1, 2, true),
                        (1, 3, true),
                        (1, 4, true),
                        (5, 3, true),
                        (5, 2, false),
                        (6, 1, false),
                        (4, 5, true),
                        (4, 9, true),
                        (4, 2, true),
                        (7, 6, false),
                        (7, 9, false),
                        (9, 6, true),
                        (8, 5, true),
                        (8, 4, true),
                        (8, 3, true),
                        (8, 2, false),
                        (8, 1, true),
                        (11, 3, true),
                        (11, 10, false),
                        (12, 9, false),
                        (13, 5, true),
                        (14, 3, true),
                        (14, 2, true),
                        (14, 1, true),
                        (15, 11, false),
                        (15, 12, true),
                        (15, 13, true),
                        (15, 14, true),
                        (10, 5, false),
                        (7, 3, false)
                    ) AS t(ADDING_ID, ADDED_ID, ACCEPTED)
WHERE NOT EXISTS (
    SELECT 1 FROM PUBLIC.FRIEND
    WHERE ADDING_USER_ID = t.ADDING_ID
      AND ADDED_USER_ID = t.ADDED_ID
);