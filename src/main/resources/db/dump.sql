INSERT INTO `klienci` (`id_klienta`, `imie_klienta`, `nazwisko_klienta`, `nr_kontaktowy`, `marka_samochodu`, `model_samochodu`, `numer_rejestracyjny`) VALUES
(1, 'Adrian', 'Bieda', 123123123, 'Fiat', 'Linea', 'RBR-0000'),
(2, 'Andrzej', 'Albrycht', 321321321, 'Renault', 'Scenic', 'RKR-0000'),
(3, 'Wiktor', 'Borek', 456456456, 'Seat', 'Cordoba', 'RKR-0001'),
(4, 'Kamil', 'Cwynar', 654654654, 'Citroen', 'Saxo', 'RKR-0002');

INSERT INTO `magazyn` (`id_czesci`, `nazwa_czesci`, `opis_czesci`, `ilosc`, `cena`) VALUES
(1, 'Świeca iskrowa', 'Świeca iskrowa do silników benzynowych', 100, 29.99),
(2, 'Klocki hamulcowe VW Golf V przód', 'Klocki hamulcowe VW Golf V przód (komplet 2 szt.)', 4, 149.99),
(3, 'Olej silnikowy 5W40 5L', 'LEPKOŚĆ: 5W40\r\nACEA A3/B3, A3/B4\r\nAPI: SN/CF', 10, 89.99);

INSERT INTO `pracownicy` (`id_pracownika`, `imie`, `nazwisko`, `stanowisko`, `login`, `haslo`) VALUES
(1, 'Jan', 'Kowalski', 0, 'admin', 'admin'),
(2, 'PaweĹ‚', 'Nowak', 1, 'obslugaklienta1', 'obslugaklienta1'),
(3, 'Maciej', 'Solejuk', 2, 'mechanik1', 'mechanik1'),
(4, 'Piotr', 'Nowakowski', 3, 'magazynier1', 'magazynier1');

INSERT INTO `zamowienia` (`id_zamowienia`, `nazwa_czesci`, `kometarz`, `stan_zamowienia`, `id_mechanika`) VALUES
(1, 'Belka tylna Renault Scenic', 'Wymagany osprzęt układu hamulcowego', 0, 2),
(2, 'Reflektor przedni prawy Fiat Panda', '', 0, 2);

INSERT INTO `zlecenia` (`id_zlecenie`, `id_klienta`, `id_mechanika`, `id_obslugaklientastart`, `id_obslugaklientakoniec`, `opis_usterki`, `data_rozpoczecia`, `data_zakonczenia`, `opis_naprawy`, `uzyte_czesci`, `cena`) VALUES
(1, 1, NULL, 3, NULL, 'Wymiana klockĂłw hamulcowych przód', '2020-04-19 10:00:00', NULL, NULL, NULL, NULL);