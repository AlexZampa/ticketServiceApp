INSERT INTO public.products (id, name, brand, description) VALUES ('A01', 'Lego Star Wars', 'LEGO', 'Space');
INSERT INTO public.products (id, name, brand, description) VALUES ('A02', 'Lego Indiana Jones', 'LEGO', 'Historical');
INSERT INTO public.products (id, name, brand, description) VALUES ('B01', 'Lego Duplo Fashion Blogger', 'LEGO Duplo', 'Fashion');
INSERT INTO public.products (id, name, brand, description) VALUES ('B02', 'Lego Dublo TikToker', 'LEGO Duplo', 'Influencer');

INSERT INTO public.profiles (email, name, surname, username, dateofbirth, hash) VALUES ('ciaone.matano@gmail.com', 'Frank', 'Matano', 'frank_m', '1989-09-14', 'd04b98f48e8f8bcc15c6ae5ac050801cd6dcfd428fb5f9e65c4e16e7807340fa');
INSERT INTO public.profiles (email, name, surname, username, dateofbirth, hash) VALUES ('paul.dano@gmail.com', 'Paul', 'Dano', 'paul_dano', '1984-06-19', 'd04b98f48e8f8bcc15c6ae5ac050801cd6dcfd428fb5f9e65c4e16e7807340fa');

INSERT INTO public.tickets (category, description, priority, expert_id, product_id, profile_id) VALUES ('Lego', 'Non funziona', 1, null, 'A01', 1);
INSERT INTO public.tickets (category, description, priority, expert_id, product_id, profile_id) VALUES ('Duplo', 'NO istruzioni', 2, null, 'B01', 1);

INSERT INTO public.ticket_history (date, status, ticket_id) VALUES ('2022-09-14', 'OPEN', 1);
INSERT INTO public.ticket_history (date, status, ticket_id) VALUES ('2022-09-15', 'PROGRESS', 1);
INSERT INTO public.ticket_history (date, status, ticket_id) VALUES ('2022-09-10', 'OPEN', 2);
