INSERT INTO `klienci` (`id_klienta`, `imie_klienta`, `nazwisko_klienta`, `nr_kontaktowy`, `marka_samochodu`,
                       `model_samochodu`, `numer_rejestracyjny`)
VALUES (1, 'Adrian', 'Bieda', 123123123, 'Fiat', 'Linea', 'RBR-0000'),
       (2, 'Andrzej', 'Albrycht', 321321321, 'Renault', 'Scenic', 'RKR-0000'),
       (3, 'Wiktor', 'Borek', 456456456, 'Seat', 'Cordoba', 'RKR-0001'),
       (4, 'Kamil', 'Cwynar', 654654654, 'Citroen', 'Saxo', 'RKR-0002');

INSERT INTO `magazyn` (`id_czesci`, `nazwa_czesci`, `opis_czesci`, `ilosc`, `cena`)
VALUES (1, 'Świeca iskrowa', 'Świeca iskrowa do silników benzynowych', 100, 29.99),
       (2, 'Klocki hamulcowe VW Golf V przód', 'Klocki hamulcowe VW Golf V przód (komplet 2 szt.)', 4, 149.99),
       (3, 'Olej silnikowy 5W40 5L', 'LEPKOŚĆ: 5W40\r\nACEA A3/B3, A3/B4\r\nAPI: SN/CF', 10, 89.99);

INSERT INTO `pracownicy` (`id_pracownika`, `imie`, `nazwisko`, `stanowisko`, `login`, `haslo`)
VALUES (1, 'Jan', 'Kowalski', 0, 'admin', '$2a$10$BuF9SIfM.hZIdceHkj7SUuRC0xFPZeCgmZrveW471ARXn6xiI.Y96'),
       (2, 'Paweł', 'Nowak', 1, 'obslugaklienta1', '$2a$10$IXoVdp5SwKZyyrMfkDDliupT9RGk0W2Pkx7Igp6i1.PgsW6xIgoA2'),
       (3, 'Maciej', 'Solejuk', 2, 'mechanik1', '$2a$10$6khGvkicw7vbbasAuY119u/SY5kzig9V8SpCzBQtc.rPwT74Nk3Wq'),
       (4, 'Piotr', 'Nowakowski', 3, 'magazynier1', '$2a$10$RSg/enzOdBj04SdJV91ese6VwewMngqzEqY1/6AcGpIvVfw4eDO0y');

INSERT INTO `zamowienia` (`id_zamowienia`, `nazwa_czesci`, `kometarz`, `stan_zamowienia`, `id_mechanika`, `id_czesci`)
VALUES (1, 'Belka tylna Renault Scenic', 'Wymagany osprzęt układu hamulcowego', 0, 3, NULL),
       (2, 'Reflektor przedni prawy Fiat Panda', '', 0, 3, NULL);

INSERT INTO `zlecenia` (`id_zlecenie`, `id_klienta`, `id_mechanika`, `id_obslugaklientastart`,
                        `id_obslugaklientakoniec`, `stan_zlecenia`, `opis_usterki`, `data_rozpoczecia`,
                        `data_zakonczenia`, `opis_naprawy`, `uzyte_czesci`, `cena`)
VALUES (1, 1, NULL, 2, NULL, 0, 'Wymiana klocków hamulcowych przód', '2020-04-19 10:00:00', NULL, NULL, NULL, NULL),
       (2, 2, NULL, 2, NULL, 0, 'Wymiana belki tylniej', '2020-04-19 12:00:00', NULL, NULL, NULL, NULL),
       (3, 3, 3, 2, NULL, 1, 'Wymiana oleju w skrzyni biegów', '2020-04-20 08:00:00', NULL, NULL, NULL, NULL),
       (4, 4, 3, 2, NULL, 1, 'Naprawa manetki kierunkowskazów', '2020-04-20 11:00:00', NULL, NULL, NULL, NULL),
       (5, 4, 3, 2, NULL, 2, 'Niedziałający klakson', '2020-04-22 15:00:00', NULL, 'Naprawiono klakson',
        'Olej silnikowy 5W40 5L - 250\nOlej silnikowy 5W40 5L - 250\nOlej silnikowy 5W40 5L - 250\n', NULL),
       (6, 3, 3, 2, NULL, 2, 'Wymiana oleju silnikowego', '2020-04-28 13:00:00', NULL, 'Wymieniono olej silnikowy', '',
        NULL),
       (7, 2, 3, 2, 2, 3, 'Wymiana wahaczy', '2020-05-02 12:00:00', '2020-05-12 10:00:00', 'Wymieniono wahacze',
        'Wahacze tył Citroen Saxo', 123),
       (8, 1, 3, 2, 2, 3, 'Naprawa radia', '2020-05-04 09:00:00', '2020-05-08 14:00:00', 'Podłączono zasilanie',
        'Olej silnikowy 5W40 5L - 250\nOlej silnikowy 5W40 5L - 250\nOlej silnikowy 5W40 5L - 250\n', 321);
