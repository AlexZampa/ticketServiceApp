
CREATE TABLE public.attachments (
    id integer NOT NULL,
    data oid,
    name character varying(255),
    size bigint NOT NULL,
    type character varying(255),
    message_id integer NOT NULL
);


ALTER TABLE public.attachments OWNER TO postgres;

--
-- Name: attachments_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.attachments_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.attachments_seq OWNER TO postgres;

--
-- Name: messages; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.messages (
    id integer NOT NULL,
    datetime timestamp(6) without time zone,
    text character varying(255),
    receiver_id integer NOT NULL,
    sender_id integer NOT NULL,
    ticket_id integer NOT NULL
);


ALTER TABLE public.messages OWNER TO postgres;

--
-- Name: messages_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.messages_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.messages_seq OWNER TO postgres;

--
-- Name: products; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.products (
    id character varying(255) NOT NULL,
    brand character varying(255),
    description character varying(255),
    name character varying(255)
);


ALTER TABLE public.products OWNER TO postgres;

--
-- Name: profiles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.profiles (
    id integer NOT NULL,
    dateofbirth date,
    email character varying(255),
    name character varying(255),
    surname character varying(255),
    username character varying(255)
);


ALTER TABLE public.profiles OWNER TO postgres;

--
-- Name: profiles_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.profiles_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.profiles_seq OWNER TO postgres;

--
-- Name: ticket_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ticket_history (
    id integer NOT NULL,
    date timestamp(6) without time zone,
    status character varying(255),
    ticket_id integer NOT NULL
);


ALTER TABLE public.ticket_history OWNER TO postgres;

--
-- Name: ticket_history_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.ticket_history_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.ticket_history_seq OWNER TO postgres;

--
-- Name: tickets; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tickets (
    id integer NOT NULL,
    category character varying(255),
    description character varying(255),
    priority integer NOT NULL,
    expert_id integer,
    product_id character varying(255) NOT NULL,
    profile_id integer NOT NULL
);


ALTER TABLE public.tickets OWNER TO postgres;

--
-- Name: tickets_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.tickets_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.tickets_seq OWNER TO postgres;

--
-- Data for Name: attachments; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.attachments (id, data, name, size, type, message_id) FROM stdin;
\.


--
-- Data for Name: messages; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.messages (id, datetime, text, receiver_id, sender_id, ticket_id) FROM stdin;
0	2000-07-16 19:20:00	Ciao bella	32	33	10
\.


--
-- Data for Name: products; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.products (id, brand, description, name) FROM stdin;
A01	LEGO	Space	Lego Star Wars
A02	LEGO	Historical	Lego Indiana Jones
B01	LEGO Duplo	Fashion	Lego Duplo Fashion Blogger
B02	LEGO Duplo	Influencer	Lego Dublo TikToker
\.


--
-- Data for Name: profiles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.profiles (id, dateofbirth, email, name, surname, username) FROM stdin;
32	1989-09-14	user1@mail.com	Frank	Matano	user1
33	1992-09-20	expert1@mail.com	Mary	White	expert1
34	1987-02-15	manager1@mail.con	Maccio	Capatonda	manager1
\.


--
-- Data for Name: ticket_history; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.ticket_history (id, date, status, ticket_id) FROM stdin;
12	2022-09-14 00:00:00	OPEN	11
13	2022-09-15 00:00:00	PROGRESS	11
14	2022-09-10 00:00:00	OPEN	10
1	2023-05-18 18:46:31.802923	OPEN	1
\.


--
-- Data for Name: tickets; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tickets (id, category, description, priority, expert_id, product_id, profile_id) FROM stdin;
10	Lego	Does not work	1	\N	A01	32
11	Duplo	No istruction found	2	\N	B01	32
\.


--
-- Name: attachments_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.attachments_seq', 1, false);


--
-- Name: messages_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.messages_seq', 1, false);


--
-- Name: profiles_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.profiles_seq', 1, false);


--
-- Name: ticket_history_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.ticket_history_seq', 1, true);


--
-- Name: tickets_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.tickets_seq', 1, true);


--
-- Name: attachments attachments_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.attachments
    ADD CONSTRAINT attachments_pkey PRIMARY KEY (id);


--
-- Name: messages messages_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT messages_pkey PRIMARY KEY (id);


--
-- Name: products products_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_pkey PRIMARY KEY (id);


--
-- Name: profiles profiles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.profiles
    ADD CONSTRAINT profiles_pkey PRIMARY KEY (id);


--
-- Name: ticket_history ticket_history_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ticket_history
    ADD CONSTRAINT ticket_history_pkey PRIMARY KEY (id);


--
-- Name: tickets tickets_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tickets
    ADD CONSTRAINT tickets_pkey PRIMARY KEY (id);


--
-- Name: messages fk6iv985o3ybdk63srj731en4ba; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT fk6iv985o3ybdk63srj731en4ba FOREIGN KEY (ticket_id) REFERENCES public.tickets(id);


--
-- Name: messages fk79kgt6oyju1ma9ly4qmax5933; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT fk79kgt6oyju1ma9ly4qmax5933 FOREIGN KEY (sender_id) REFERENCES public.profiles(id);


--
-- Name: tickets fk8ojtqms4badovjb5mj7w4se56; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tickets
    ADD CONSTRAINT fk8ojtqms4badovjb5mj7w4se56 FOREIGN KEY (expert_id) REFERENCES public.profiles(id);


--
-- Name: tickets fkavo2av2fyyehcvlec0vowwu1j; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tickets
    ADD CONSTRAINT fkavo2av2fyyehcvlec0vowwu1j FOREIGN KEY (product_id) REFERENCES public.products(id);


--
-- Name: attachments fkcf4ta8qdkixetfy7wnqfv3vkv; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.attachments
    ADD CONSTRAINT fkcf4ta8qdkixetfy7wnqfv3vkv FOREIGN KEY (message_id) REFERENCES public.messages(id);


--
-- Name: ticket_history fkkkhg6aquudxfcbofalx8rtc6v; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ticket_history
    ADD CONSTRAINT fkkkhg6aquudxfcbofalx8rtc6v FOREIGN KEY (ticket_id) REFERENCES public.tickets(id);


--
-- Name: messages fkljqa4ewogfngkst59di6i9stn; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT fkljqa4ewogfngkst59di6i9stn FOREIGN KEY (receiver_id) REFERENCES public.profiles(id);


--
-- Name: tickets fkqhcouoka9cxv1uuvrd6lcwy2c; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tickets
    ADD CONSTRAINT fkqhcouoka9cxv1uuvrd6lcwy2c FOREIGN KEY (profile_id) REFERENCES public.profiles(id);