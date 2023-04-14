create table if not exists products(
    id varchar(15) primary key,
    name varchar(255),
    brand varchar(255),
    description varchar(255)
);

INSERT INTO public.products (id, name, brand, description) VALUES ('A01', 'Lego Star Wars', 'LEGO', 'Space');
INSERT INTO public.products (id, name, brand, description) VALUES ('A02', 'Lego Indiana Jones', 'LEGO', 'Historical');
INSERT INTO public.products (id, name, brand, description) VALUES ('B01', 'Lego Duplo Fashion Blogger', 'LEGO Duplo', 'Fashion');
INSERT INTO public.products (id, name, brand, description) VALUES ('B02', 'Lego Dublo TikToker', 'LEGO Duplo', 'Influencer');

create table if not exists profiles(
   email varchar(255) primary key,
   name varchar(255),
   surname varchar(255),
   username varchar(255),
   dateOfBirth date,
   hash varchar(255)
);

INSERT INTO public.profiles (email, name, surname, username, dateofbirth, hash) VALUES ('ciaone.matano@gmail.com', 'Frank', 'Matano', 'frank_m', '1989-09-14', 'd04b98f48e8f8bcc15c6ae5ac050801cd6dcfd428fb5f9e65c4e16e7807340fa');
INSERT INTO public.profiles (email, name, surname, username, dateofbirth, hash) VALUES ('paul.dano@gmail.com', 'Paul', 'Dano', 'paul_dano', '1984-06-19', 'd04b98f48e8f8bcc15c6ae5ac050801cd6dcfd428fb5f9e65c4e16e7807340fa');