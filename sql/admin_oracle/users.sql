create user movie identified by password;
create user movietest identified by password;

grant connect, resource to movie;
grant connect, resource to movietest;

-- with user system/password
alter user movie quota unlimited on users;
alter user movietest quota unlimited on users;