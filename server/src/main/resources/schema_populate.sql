
CREATE TABLE IF NOT EXISTS public.products(
                                              id varchar(255) PRIMARY KEY,
                                              name varchar(255) NOT NULL,
                                              brand varchar(255),
                                              description varchar(255)
);

CREATE TABLE IF NOT EXISTS public.profiles(
                                              id SERIAL PRIMARY KEY,
                                              email varchar(255) NOT NULL,
                                              name varchar(255) NOT NULL,
                                              surname varchar(255) NOT NULL,
                                              username varchar(255) NOT NULL,
                                              dateofbirth DATE,
                                              role varchar(255) NOT NULL,
);

CREATE TABLE IF NOT EXISTS public.tickets(
                                             id SERIAL PRIMARY KEY,
                                             category varchar(255) NOT NULL,
                                             description varchar(255) NOT NULL,
                                             priority int NOT NULL,
                                             expert_id int,
                                             product_id varchar(255) NOT NULL,
                                             profile_id int
);

CREATE TABLE IF NOT EXISTS public.ticket_history(
                                                    id SERIAL PRIMARY KEY,
                                                    date DATE NOT NULL,
                                                    status varchar(255) NOT NULL,
                                                    ticket_id int
);

CREATE TABLE IF NOT EXISTS public.messages(
                                              id SERIAL PRIMARY KEY,
                                              datetime DATE NOT NULL,
                                              text varchar(255) NOT NULL,
                                              receiver_id int NOT NULL,
                                              sender_id int NOT NULL,
                                              ticket_id int
);

CREATE TABLE IF NOT EXISTS public.attachments(
                                              id SERIAL PRIMARY KEY,
                                              name varchar(255) NOT NULL,
                                              type varchar(255) NOT NULL,
                                              size int NOT NULL,
                                              data oid NOT NULL,
                                              message_id int
);

INSERT INTO public.products (id, name, brand, description) VALUES ('A01', 'Lego Star Wars', 'LEGO', 'Space');
INSERT INTO public.products (id, name, brand, description) VALUES ('A02', 'Lego Indiana Jones', 'LEGO', 'Historical');
INSERT INTO public.products (id, name, brand, description) VALUES ('B01', 'Lego Duplo Fashion Blogger', 'LEGO Duplo', 'Fashion');
INSERT INTO public.products (id, name, brand, description) VALUES ('B02', 'Lego Dublo TikToker', 'LEGO Duplo', 'Influencer');

INSERT INTO public.profiles (id, email, name, surname, username, dateofbirth, role) VALUES (32, 'ciaone.matano@gmail.com', 'Frank', 'Matano', 'frank_m', '1989-09-14', 'user');
INSERT INTO public.profiles (id, email, name, surname, username, dateofbirth, role) VALUES (31, 'paul.dano@gmail.com', 'Paul', 'Dano', 'paul_dano', '1984-06-19', 'manager');

INSERT INTO public.tickets (id, category, description, priority, expert_id, product_id, profile_id) VALUES (10, 'Lego', 'Non funziona', 1, null, 'A01', 31);
INSERT INTO public.tickets (id, category, description, priority, expert_id, product_id, profile_id) VALUES (11, 'Duplo', 'NO istruzioni', 2, null, 'B01', 31);

INSERT INTO public.ticket_history (id, date, status, ticket_id) VALUES (12, '2022-09-14', 'OPEN', 11);
INSERT INTO public.ticket_history (id, date, status, ticket_id) VALUES (13, '2022-09-15', 'PROGRESS', 11);
INSERT INTO public.ticket_history (id, date, status, ticket_id) VALUES (14, '2022-09-10', 'OPEN', 10);

INSERT INTO public.messages(id, datetime, text, receiver_id, sender_id, ticket_id)
        VALUES (0, '2000-07-16T19:20+01:00', 'Ciao bella', 32, 31, 10);
