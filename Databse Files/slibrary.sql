-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : sam. 17 oct. 2020 à 23:19
-- Version du serveur :  10.4.11-MariaDB
-- Version de PHP : 7.4.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `slibrary`
--

-- --------------------------------------------------------

--
-- Structure de la table `admin`
--

CREATE TABLE `admin` (
  `id_admin` bigint(20) NOT NULL,
  `username` varchar(255) NOT NULL,
  `firstName` varchar(255) NOT NULL,
  `lastName` varchar(255) NOT NULL,
  `pass` varchar(255) NOT NULL,
  `id_adminDatabase` int(11) NOT NULL,
  `adminType` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `admin`
--

INSERT INTO `admin` (`id_admin`, `username`, `firstName`, `lastName`, `pass`, `id_adminDatabase`, `adminType`) VALUES
(153648, 'ganta', 'khalil', 'bentaiba', 'hhhh', 1, 'Principal'),
(47258, 'yuuki', 'taher', 'zoubir', 'tttt', 2, 'Trainee'),
(7498, 'meme', 'joe', 'mama', 'iii', 4, 'Trainee'),
(684684, 'just', 'haitem', 'bourahla', 'hhhh', 5, 'Principal');

-- --------------------------------------------------------

--
-- Structure de la table `book`
--

CREATE TABLE `book` (
  `id_book` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `autor` varchar(255) NOT NULL,
  `isbn` bigint(20) NOT NULL,
  `category` varchar(255) NOT NULL,
  `statu` varchar(255) NOT NULL,
  `purchaseDate` date NOT NULL,
  `orderNumber` bigint(20) NOT NULL,
  `id_bookDatabase` int(11) NOT NULL,
  `bookState` varchar(255) NOT NULL,
  `bookValue` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `book`
--

INSERT INTO `book` (`id_book`, `title`, `autor`, `isbn`, `category`, `statu`, `purchaseDate`, `orderNumber`, `id_bookDatabase`, `bookState`, `bookValue`) VALUES
(1, 'Head First Java', 'Kathy Sierra, Bert Bates', 9780596009205, 'Java', 'loaned', '2006-02-22', 596009208, 6, 'new', 1200),
(2, 'The C++ Programming Language', 'Bjarne Stroustrup', 9780321563842, 'C++', 'on the shelf', '1995-07-09', 321563842, 7, 'very good state', 850),
(3, 'Learn C the Hard Way: Practical Exercises on the Computational Subjects You Keep Avoiding (Like C)', 'Zed Shaw', 9780321884922, 'C', 'on the shelf', '2016-08-10', 321884922, 8, 'new', 900),
(4, 'Kotlin Programming: The Big Nerd Ranch Guide', 'Josh Skeen, David Greenhalgh', 9780135161630, 'Kotlin', 'on the shelf', '2019-01-08', 135161630, 9, 'new', 1500),
(5, 'Kotlin for Android Developers: Learn Kotlin the Easy Way While Developing an Android App', 'Antonio Leiva, Antonio Leiva Gordillo', 97801530075614, 'Android', 'loaned', '2016-04-24', 1530075614, 10, 'used', 750),
(6, 'Hello, Android: Introducing Google s Mobile Development Platform', 'Ed Burnette', 9781680500370, 'Android', 'loaned', '2009-02-24', 1680500370, 11, 'new', 500),
(7, 'Let Us C', 'Yashavant Kanetkar, Ashutosh Pandey', 9781934015254, 'C', 'loaned', '2000-05-08', 1934015254, 12, 'new', 1350),
(8, 'Programming Python', 'Mark Lutz', 9780596158101, 'Python', 'loaned', '1997-07-15', 596158106, 13, 'new', 1100),
(9, 'Programming Arduino: Getting Started with Sketches', 'Simon Monk', 9780071784221, 'Arduino', 'loaned', '2011-12-12', 71784225, 14, 'used', 450),
(10, 'Exploring Arduino: Tools and Techniques for Engineering Wizardry', 'Jeremy Blum', 9781118549360, 'Arduino', 'on the shelf', '2002-11-13', 1118549368, 15, 'new', 200),
(11, 'Arduino For Dummies', 'John Nussey', 9781118446379, 'Arduino', 'loaned', '2013-04-29', 1118446372, 16, 'very good state', 950),
(12, 'Programming the Raspberry Pi: Getting Started with Python', 'Simon Monk', 9780071807838, 'Raspberry Pi', 'loaned', '2012-01-09', 71807837, 17, 'new', 700),
(13, 'Raspberry Pi User Guide', 'Gareth Halfacree, Eben Upton', 9781118464465, 'Raspberry Pi', 'loaned', '2014-05-04', 11184644665, 18, 'new', 1050),
(14, 'Raspberry Pi Cookbook: Software and Hardware Problems and Solutions', 'Simon Monk', 9781491939109, 'Raspberry Pi', 'on the shelf', '2017-09-10', 1491939109, 19, 'very good state', 850),
(15, 'Raspberry Pi For Dummies', 'Mike Cook, Sean McManus', 9781118554210, 'Raspberry Pi', 'loaned', '2013-10-31', 1118554210, 20, 'new', 650),
(16, 'Raspberry Pi For Dummies', 'Mike Cook, Sean McManus', 9781118554210, 'Raspberry Pi', 'loaned', '2013-10-31', 1118554210, 28, 'new', 650),
(46, 'pjpf', 'efezf', 788797, 'Java', 'on the shelf', '2020-09-10', 9797, 29, 'new', 798),
(79, 'oif', 'fzef', 8789, 'Java', 'on the shelf', '2020-09-03', 879, 30, 'new', 786),
(68, 'Learn C the Hard Way: Practical Exercises on the Computational Subjects You Keep Avoiding (Like C)', 'Zed Shaw', 9780321884922, 'C', 'on the shelf', '2016-08-10', 321884922, 31, 'new', 900),
(887, 'Kotlin for Android Developers: Learn Kotlin the Easy Way While Developing an Android App', 'Antonio Leiva, Antonio Leiva Gordillo', 97801530075614, 'Android', 'loaned', '2016-04-24', 1530075614, 32, 'very good state', 750),
(88788, 'Kotlin for Android Developers: Learn Kotlin the Easy Way While Developing an Android App', 'Antonio Leiva, Antonio Leiva Gordillo', 97801530075614, 'Android', 'loaned', '2016-04-24', 1530075614, 43, 'new', 750),
(87, 'oufezohf', 'fzefezf', 879298789, 'jijiji', 'on the shelf', '2020-10-08', 761878, 46, 'new', 1400);

-- --------------------------------------------------------

--
-- Structure de la table `document`
--

CREATE TABLE `document` (
  `id_document` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `editor` varchar(255) NOT NULL,
  `id` bigint(20) NOT NULL,
  `category` varchar(255) NOT NULL,
  `statu` varchar(255) NOT NULL,
  `publicationDate` date NOT NULL,
  `purchaseDate` date NOT NULL,
  `orderNumber` bigint(20) NOT NULL,
  `id_documentDatabase` int(11) NOT NULL,
  `documentState` varchar(255) NOT NULL,
  `docValue` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `document`
--

INSERT INTO `document` (`id_document`, `title`, `editor`, `id`, `category`, `statu`, `publicationDate`, `purchaseDate`, `orderNumber`, `id_documentDatabase`, `documentState`, `docValue`) VALUES
(1, 'asd', 'karoum', 6525, 'algo', 'on the shelf', '2019-01-14', '2019-03-22', 952952, 1, 'new', 750),
(2, 'vfdv', 'fvdvdf', 296269, 'gngfb', 'on the shelf', '2020-03-06', '2020-03-03', 6225, 2, 'very good state', 85),
(3, 'asd', 'karoum', 6525, 'algo', 'on the shelf', '2019-01-14', '2019-03-22', 952952, 3, 'new', 150),
(4, 'lkinrge', 'greg', 58987, 'BD', 'on the shelf', '2020-04-08', '2020-04-15', 76565, 5, 'good state', 1200),
(7, 'lkinrge', 'greg', 58987, 'BD', 'on the shelf', '2020-04-08', '2020-04-15', 76565, 6, 'good state', 1200),
(6, 'asd', 'karoum', 6525, 'algo', 'loaned', '2019-01-14', '2019-03-22', 952952, 8, 'new', 150),
(11, 'asd', 'zefzef', 6525, 'ABD', 'loaned', '2019-01-14', '2019-03-22', 952952, 12, 'new', 150),
(14, 'asd', 'zefz', 6525, 'ABD', 'on the shelf', '2019-01-14', '2019-03-22', 952952, 14, 'new', 150),
(189, 'asd', 'zefz', 525, 'ABD', 'on the shelf', '2019-01-14', '2019-03-22', 952952, 15, 'new', 150),
(18, 'asd', 'zefz', 52, 'ABD', 'loaned', '2019-01-14', '2019-03-22', 952952, 16, 'new', 150),
(47, 'lkinrge', 'greg', 58987, 'BD', 'on the shelf', '2020-04-08', '2020-04-15', 76565, 18, 'good state', 1200),
(488, 'lkinrge', 'greg', 589, 'BD', 'on the shelf', '2020-04-08', '2020-04-15', 76565, 20, 'good state', 1200),
(4737, 'lkinrge', 'greg', 58978, 'BDC', 'on the shelf', '2020-04-08', '2020-04-15', 76565, 21, 'good state', 1200),
(477, 'lkinrge', 'greg', 5878, 'BDCE', 'loaned', '2020-04-08', '2020-04-15', 76565, 22, 'good state', 1200),
(4777, 'lkinrge', 'greg', 878, 'BDE', 'on the shelf', '2020-04-08', '2020-04-15', 76565, 23, 'good state', 1200),
(4775, 'lkinrge', 'greg', 58758, 'DCE', 'on the shelf', '2020-04-08', '2020-04-15', 76565, 24, 'good state', 1200),
(47754, 'lkinrge', 'greg', 587558, 'DE', 'on the shelf', '2020-04-08', '2020-04-15', 76565, 25, 'good state', 1200),
(4766, 'lkinrge', 'greg', 87665, 'DB', 'on the shelf', '2020-04-08', '2020-04-15', 76565, 28, 'good state', 1200),
(999, 'lkinrge', 'greg', 87699, 'DB', 'on the shelf', '2020-04-08', '2020-04-15', 76565, 29, 'good state', 1200),
(19, 'asd', 'karoum', 9525, 'algoo', 'loaned', '2019-01-14', '2019-03-22', 952952, 30, 'new', 150),
(4998, 'lkinrge', 'gregojn', 554858, 'yato', 'on the shelf', '2020-04-08', '2020-04-15', 76565, 31, 'good state', 1200),
(20, 'lkinrge', 'gregojn', 554858, 'yato', 'on the shelf', '2020-04-08', '2020-04-15', 76565, 32, 'good state', 1200);

-- --------------------------------------------------------

--
-- Structure de la table `latebook`
--

CREATE TABLE `latebook` (
  `id_lateBook` int(11) NOT NULL,
  `id_bookDatabase` int(11) NOT NULL,
  `id_userDatabase` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `latedoc`
--

CREATE TABLE `latedoc` (
  `id_lateDoc` int(11) NOT NULL,
  `id_docDatabase` int(11) NOT NULL,
  `id_userDatabase` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `loanedbook`
--

CREATE TABLE `loanedbook` (
  `id_loanedBook` int(11) NOT NULL,
  `id_bookDatabase` int(11) NOT NULL,
  `id_userDatabase` int(11) NOT NULL,
  `loadDate` date NOT NULL,
  `returnDate` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `loanedbook`
--

INSERT INTO `loanedbook` (`id_loanedBook`, `id_bookDatabase`, `id_userDatabase`, `loadDate`, `returnDate`) VALUES
(414, 12, 8, '2020-10-16', '2020-10-31'),
(415, 11, 9, '2020-10-16', '2020-10-17'),
(416, 10, 6, '2020-10-16', '2020-11-15'),
(417, 17, 6, '2020-10-16', '2020-11-15'),
(419, 13, 7, '2020-10-16', '2020-10-17'),
(420, 16, 1, '2020-10-16', '2020-11-15'),
(421, 28, 12, '2020-10-16', '2020-10-17'),
(422, 43, 2, '2020-10-16', '2020-11-16'),
(424, 18, 2, '2020-10-16', '2020-10-17'),
(425, 20, 2, '2020-10-16', '2020-10-17'),
(426, 32, 17, '2020-10-16', '2020-11-15'),
(427, 14, 17, '2020-10-17', '2020-11-16'),
(428, 6, 19, '2020-10-17', '2020-11-16');

-- --------------------------------------------------------

--
-- Structure de la table `loaneddoc`
--

CREATE TABLE `loaneddoc` (
  `id_loanedDoc` int(11) NOT NULL,
  `id_docDatabase` int(11) NOT NULL,
  `id_userDatabase` int(11) NOT NULL,
  `loadDate` date NOT NULL,
  `returnDate` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `loaneddoc`
--

INSERT INTO `loaneddoc` (`id_loanedDoc`, `id_docDatabase`, `id_userDatabase`, `loadDate`, `returnDate`) VALUES
(103, 12, 7, '2020-10-16', '2020-11-15'),
(105, 8, 17, '2020-10-16', '2020-11-15'),
(106, 22, 2, '2020-10-16', '2020-11-15'),
(107, 16, 19, '2020-10-16', '2020-11-15');

-- --------------------------------------------------------

--
-- Structure de la table `users`
--

CREATE TABLE `users` (
  `id_user` bigint(20) NOT NULL,
  `firstName` varchar(255) NOT NULL,
  `lastName` varchar(255) NOT NULL,
  `userType` varchar(255) NOT NULL,
  `id_userDatabase` int(11) NOT NULL,
  `copyCanLoan` int(11) NOT NULL,
  `bookLoaned` int(11) NOT NULL,
  `bookLate` int(11) NOT NULL,
  `docLoaned` int(11) NOT NULL,
  `docLate` int(11) NOT NULL,
  `ban` varchar(255) DEFAULT NULL,
  `banStart` varchar(255) DEFAULT NULL,
  `banEnd` varchar(255) DEFAULT NULL,
  `penaltyValue` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `users`
--

INSERT INTO `users` (`id_user`, `firstName`, `lastName`, `userType`, `id_userDatabase`, `copyCanLoan`, `bookLoaned`, `bookLate`, `docLoaned`, `docLate`, `ban`, `banStart`, `banEnd`, `penaltyValue`) VALUES
(330598, 'haitem', 'bourahla', 'subscriber', 1, 3, 1, 0, 0, 0, NULL, 'NULL', 'NULL', 0),
(9457861, 'khalil', 'bentaiba', 'privileged subscriber', 2, 4, 3, 0, 1, 0, NULL, 'NULL', 'NULL', 0),
(457789, 'amine', 'bouderbala', 'occasional user', 4, 1, 0, 0, 0, 0, 'YES', '2020-10-17', '2020-10-30', 0),
(788954, 'aymen', 'ajroud', 'subscriber', 6, 2, 2, 0, 0, 0, NULL, 'NULL', 'NULL', 730),
(985547, 'abd aziz', 'bouteflika', 'privileged subscriber', 7, 6, 1, 0, 1, 0, NULL, 'NULL', 'NULL', 0),
(48, 'kjdu', 'djd', 'occasional user', 8, 0, 1, 0, 0, 0, NULL, 'NULL', 'NULL', 0),
(552, 'kjddvdsu', 'djdvdv', 'occasional user', 9, 0, 1, 0, 0, 0, NULL, 'NULL', 'NULL', 200),
(7954, 'ferfr', 'irfef', 'occasional user', 10, 1, 0, 0, 0, 0, 'YES', '2020-10-16', '2020-10-31', 0),
(252989, 'za3ter', 'dabcha', 'occasional user', 12, 0, 1, 0, 0, 0, NULL, 'NULL', 'NULL', 0),
(56648, 'boulham', 'adam', 'subscriber', 17, 1, 2, 0, 1, 0, NULL, 'NULL', 'NULL', 0),
(68654, 'hamza', 'sellamna', 'privileged subscriber', 19, 6, 1, 0, 1, 0, NULL, 'NULL', 'NULL', 0);

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id_adminDatabase`,`id_admin`,`username`);

--
-- Index pour la table `book`
--
ALTER TABLE `book`
  ADD PRIMARY KEY (`id_bookDatabase`,`id_book`,`isbn`,`orderNumber`);

--
-- Index pour la table `document`
--
ALTER TABLE `document`
  ADD PRIMARY KEY (`id_documentDatabase`,`id_document`,`id`,`orderNumber`);

--
-- Index pour la table `latebook`
--
ALTER TABLE `latebook`
  ADD PRIMARY KEY (`id_lateBook`);

--
-- Index pour la table `latedoc`
--
ALTER TABLE `latedoc`
  ADD PRIMARY KEY (`id_lateDoc`);

--
-- Index pour la table `loanedbook`
--
ALTER TABLE `loanedbook`
  ADD PRIMARY KEY (`id_loanedBook`);

--
-- Index pour la table `loaneddoc`
--
ALTER TABLE `loaneddoc`
  ADD PRIMARY KEY (`id_loanedDoc`);

--
-- Index pour la table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id_userDatabase`,`id_user`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `admin`
--
ALTER TABLE `admin`
  MODIFY `id_adminDatabase` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT pour la table `book`
--
ALTER TABLE `book`
  MODIFY `id_bookDatabase` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=58;

--
-- AUTO_INCREMENT pour la table `document`
--
ALTER TABLE `document`
  MODIFY `id_documentDatabase` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;

--
-- AUTO_INCREMENT pour la table `latebook`
--
ALTER TABLE `latebook`
  MODIFY `id_lateBook` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10066;

--
-- AUTO_INCREMENT pour la table `latedoc`
--
ALTER TABLE `latedoc`
  MODIFY `id_lateDoc` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=318;

--
-- AUTO_INCREMENT pour la table `loanedbook`
--
ALTER TABLE `loanedbook`
  MODIFY `id_loanedBook` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=429;

--
-- AUTO_INCREMENT pour la table `loaneddoc`
--
ALTER TABLE `loaneddoc`
  MODIFY `id_loanedDoc` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=108;

--
-- AUTO_INCREMENT pour la table `users`
--
ALTER TABLE `users`
  MODIFY `id_userDatabase` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
