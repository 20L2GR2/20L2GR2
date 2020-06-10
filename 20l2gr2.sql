-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Czas generowania: 06 Maj 2020, 18:42
-- Wersja serwera: 10.4.11-MariaDB
-- Wersja PHP: 7.4.5

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Baza danych: `20l2gr2`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `klienci`
--

CREATE TABLE `klienci` (
  `id_klienta` int(9) NOT NULL,
  `imie_klienta` varchar(30) COLLATE utf8mb4_polish_ci NOT NULL,
  `nazwisko_klienta` varchar(30) COLLATE utf8mb4_polish_ci NOT NULL,
  `nr_kontaktowy` int(9) NOT NULL,
  `marka_samochodu` varchar(50) COLLATE utf8mb4_polish_ci NOT NULL,
  `model_samochodu` varchar(50) COLLATE utf8mb4_polish_ci NOT NULL,
  `numer_rejestracyjny` varchar(8) COLLATE utf8mb4_polish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci;

--
-- Struktura tabeli dla tabeli `magazyn`
--

CREATE TABLE `magazyn` (
  `id_czesci` int(9) NOT NULL,
  `nazwa_czesci` varchar(100) COLLATE utf8mb4_polish_ci NOT NULL,
  `opis_czesci` text COLLATE utf8mb4_polish_ci DEFAULT NULL,
  `ilosc` int(9) NOT NULL,
  `cena` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci;

--
-- Struktura tabeli dla tabeli `pracownicy`
--

CREATE TABLE `pracownicy` (
                              `id_pracownika` int(9) NOT NULL,
                              `imie` varchar(30) COLLATE utf8mb4_polish_ci NOT NULL,
                              `nazwisko` varchar(30) COLLATE utf8mb4_polish_ci NOT NULL,
                              `stanowisko` int(1) NOT NULL,
                              `login` varchar(30) COLLATE utf8mb4_polish_ci NOT NULL,
                              `haslo` varchar(500) COLLATE utf8mb4_polish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci;

--
-- Struktura tabeli dla tabeli `zamowienia`
--

CREATE TABLE `zamowienia` (
  `id_zamowienia` int(9) NOT NULL,
  `nazwa_czesci` varchar(100) COLLATE utf8mb4_polish_ci NOT NULL,
  `kometarz` varchar(500) COLLATE utf8mb4_polish_ci NOT NULL,
  `stan_zamowienia` int(1) NOT NULL,
  `id_mechanika` int(9) NOT NULL,
  `id_czesci` int(9) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci;

--
-- Struktura tabeli dla tabeli `zlecenia`
--

CREATE TABLE `zlecenia` (
                            `id_zlecenie`             int(9) NOT NULL,
                            `id_klienta`              int(9) NOT NULL,
                            `id_mechanika`            int(9)                                  DEFAULT NULL,
                            `id_obslugaklientastart`  int(9) NOT NULL,
                            `id_obslugaklientakoniec` int(9)                                  DEFAULT NULL,
                            `stan_zlecenia`           int(1) NOT NULL,
                            `opis_usterki`            text COLLATE utf8mb4_polish_ci NOT NULL,
                            `data_rozpoczecia`        datetime                                DEFAULT current_timestamp(),
                            `data_zakonczenia`        datetime                                DEFAULT NULL,
                            `opis_naprawy`            text COLLATE utf8mb4_polish_ci          DEFAULT NULL,
                            `uzyte_czesci`            varchar(1000) COLLATE utf8mb4_polish_ci DEFAULT NULL,
                            `cena`                    double                                  DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_polish_ci;

--
-- Indeksy dla tabeli `klienci`
--
ALTER TABLE `klienci`
    ADD PRIMARY KEY (`id_klienta`);

--
-- Indeksy dla tabeli `magazyn`
--
ALTER TABLE `magazyn`
    ADD PRIMARY KEY (`id_czesci`);

--
-- Indeksy dla tabeli `pracownicy`
--
ALTER TABLE `pracownicy`
    ADD PRIMARY KEY (`id_pracownika`);

--
-- Indeksy dla tabeli `zamowienia`
--
ALTER TABLE `zamowienia`
    ADD PRIMARY KEY (`id_zamowienia`),
    ADD KEY `id_mechanika` (`id_mechanika`),
    ADD KEY `id_czesci` (`id_czesci`);

--
-- Indeksy dla tabeli `zlecenia`
--
ALTER TABLE `zlecenia`
    ADD PRIMARY KEY (`id_zlecenie`),
    ADD KEY `id_klienta` (`id_klienta`),
    ADD KEY `id_mechanika` (`id_mechanika`),
    ADD KEY `id_obslugaklientastart` (`id_obslugaklientastart`),
    ADD KEY `id_obslugaklientakoniec` (`id_obslugaklientakoniec`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT dla tabeli `klienci`
--
ALTER TABLE `klienci`
    MODIFY `id_klienta` int(9) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 21;

--
-- AUTO_INCREMENT dla tabeli `magazyn`
--
ALTER TABLE `magazyn`
    MODIFY `id_czesci` int(9) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 8;

--
-- AUTO_INCREMENT dla tabeli `pracownicy`
--
ALTER TABLE `pracownicy`
    MODIFY `id_pracownika` int(9) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 6;

--
-- AUTO_INCREMENT dla tabeli `zamowienia`
--
ALTER TABLE `zamowienia`
    MODIFY `id_zamowienia` int(9) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 3;

--
-- AUTO_INCREMENT dla tabeli `zlecenia`
--
ALTER TABLE `zlecenia`
    MODIFY `id_zlecenie` int(9) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 9;

--
-- Ograniczenia dla zrzutów tabel
--

--
-- Ograniczenia dla tabeli `zamowienia`
--
ALTER TABLE `zamowienia`
    ADD CONSTRAINT `zamowienia_ibfk_2` FOREIGN KEY (`id_mechanika`) REFERENCES `pracownicy` (`id_pracownika`),
    ADD CONSTRAINT `zamowienia_ibfk_3` FOREIGN KEY (`id_czesci`) REFERENCES `magazyn` (`id_czesci`);

--
-- Ograniczenia dla tabeli `zlecenia`
--
ALTER TABLE `zlecenia`
    ADD CONSTRAINT `zlecenia_ibfk_1` FOREIGN KEY (`id_klienta`) REFERENCES `klienci` (`id_klienta`),
    ADD CONSTRAINT `zlecenia_ibfk_2` FOREIGN KEY (`id_klienta`) REFERENCES `klienci` (`id_klienta`),
    ADD CONSTRAINT `zlecenia_ibfk_3` FOREIGN KEY (`id_mechanika`) REFERENCES `pracownicy` (`id_pracownika`),
    ADD CONSTRAINT `zlecenia_ibfk_4` FOREIGN KEY (`id_obslugaklientastart`) REFERENCES `pracownicy` (`id_pracownika`),
    ADD CONSTRAINT `zlecenia_ibfk_5` FOREIGN KEY (`id_obslugaklientakoniec`) REFERENCES `pracownicy` (`id_pracownika`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
