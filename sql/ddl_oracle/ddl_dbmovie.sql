DROP TABLE play_role;
DROP TABLE play;
DROP TABLE have_genre;
DROP TABLE movies;
DROP TABLE persons;

--
-- Table structure for table people
--

CREATE TABLE persons (
  id integer GENERATED BY DEFAULT ON NULL AS IDENTITY,
  name varchar2(50) NOT NULL,
  birthdate date NULL,
  CONSTRAINT pk_persons PRIMARY KEY (id)
);

--
-- Table structure for table `movie`
--

CREATE TABLE movies (
  id integer GENERATED BY DEFAULT ON NULL AS IDENTITY,
  title varchar(250) NOT NULL,
  year smallint NOT NULL,
  color varchar2(15) NULL,
  duration smallint NULL,
  director_id integer NULL,
  CONSTRAINT chk_movies_year CHECK(year >= 1850),
  CONSTRAINT chk_title_not_empty CHECK(length(title) > 0),
  CONSTRAINT pk_movies PRIMARY KEY (id),
  CONSTRAINT fk_m_director_id FOREIGN KEY (director_id) REFERENCES persons (id)
);

--
-- Table structure for table have_genre
--

CREATE TABLE have_genre (
  movie_id integer NOT NULL,
  genre varchar(15) NOT NULL,
  CONSTRAINT fk_hg_movie_id FOREIGN KEY (movie_id) REFERENCES movies (id)
);

--
-- Table structure for table play
--

CREATE TABLE play (
  movie_id integer NOT NULL,
  actor_id integer NOT NULL,
  CONSTRAINT pk_play PRIMARY KEY (movie_id,actor_id),
  CONSTRAINT fk_p_actor_id FOREIGN KEY (actor_id) REFERENCES persons (id),
  CONSTRAINT fk_p_movie_id FOREIGN KEY (movie_id) REFERENCES movies (id)
);

--
-- Table structure for table play
--

CREATE TABLE play_role (
  id integer GENERATED BY DEFAULT ON NULL AS IDENTITY, 
  movie_id integer NOT NULL,
  actor_id integer NOT NULL,
  role varchar(100) NULL,
  CONSTRAINT pk_play_role PRIMARY KEY (movie_id,actor_id),
  CONSTRAINT fk_pr_actor_id FOREIGN KEY (actor_id) REFERENCES persons (id),
  CONSTRAINT fk_pr_movie_id FOREIGN KEY (movie_id) REFERENCES movies (id)
);