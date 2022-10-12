-- with user system/password
create user movie identified by password;
create user movietest identified by password;
create user movierole identified by password;

grant connect, resource to movie;
grant connect, resource to movietest;
grant connect, resource to movierole;


alter user movie quota unlimited on users;
alter user movietest quota unlimited on users;
alter user movierole quota unlimited on users;