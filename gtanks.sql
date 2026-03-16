-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 31, 2024 at 03:06 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `gtanks`
--

-- --------------------------------------------------------

--
-- Table structure for table `black_ips`
--

CREATE TABLE `black_ips` (
  `idblack_ips` bigint(20) NOT NULL,
  `ip` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `captcha`
--

CREATE TABLE `captcha` (
  `id` bigint(20) NOT NULL,
  `code` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `captcha`
--

INSERT INTO `captcha` (`id`, `code`) VALUES
(1, 'UAA18'),
(2, 'M23G7'),
(3, '6YISA'),
(4, '7SPX'),
(5, 'ZWIQ0'),
(6, 'AV8Q'),
(7, 'SV0U'),
(8, '6BAI'),
(9, 'CU4E'),
(10, '77UT'),
(11, 'QCOXF'),
(12, '78JPB'),
(13, 'IUB4J'),
(14, 'PND9'),
(15, 'TLQL'),
(16, 'ITB0G'),
(17, 'IXO6'),
(18, 'GCMB5'),
(19, 'JHM7'),
(20, 'YRD3W'),
(21, '8GJXB'),
(22, '82GK8');

-- --------------------------------------------------------

--
-- Table structure for table `challenges_user`
--

CREATE TABLE `challenges_user` (
  `userId` bigint(20) NOT NULL,
  `battlePass` bit(1) DEFAULT NULL,
  `score` int(11) DEFAULT NULL,
  `stars` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `challenges_user`
--

INSERT INTO `challenges_user` (`userId`, `battlePass`, `score`, `stars`) VALUES
(1, b'0', 8119568, 187288),
(29, b'0', 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `container_assortment`
--

CREATE TABLE `container_assortment` (
  `id` bigint(20) NOT NULL,
  `client_id` varchar(255) DEFAULT NULL,
  `desc_en` varchar(255) DEFAULT NULL,
  `desc_ru` varchar(255) DEFAULT NULL,
  `title_en` varchar(255) DEFAULT NULL,
  `title_ru` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `daily_mission_user`
--

CREATE TABLE `daily_mission_user` (
  `userId` bigint(20) NOT NULL,
  `changePrice` int(11) DEFAULT NULL,
  `missionId1` varchar(255) DEFAULT NULL,
  `missionId2` varchar(255) DEFAULT NULL,
  `missionId3` varchar(255) DEFAULT NULL,
  `missionProgr1` int(11) DEFAULT NULL,
  `missionProgr2` int(11) DEFAULT NULL,
  `missionProgr3` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `daily_mission_user`
--

INSERT INTO `daily_mission_user` (`userId`, `changePrice`, `missionId1`, `missionId2`, `missionId3`, `missionProgr1`, `missionProgr2`, `missionProgr3`) VALUES
(1, 0, 'captureTheFlag', 'damage', 'flag_return', 0, 0, 0),
(29, 0, 'damage', 'win_cry', 'damage', 0, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `friends`
--

CREATE TABLE `friends` (
  `user_id` bigint(20) NOT NULL,
  `accepted` varchar(255) DEFAULT NULL,
  `incoming` varchar(255) DEFAULT NULL,
  `outgoing` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `garages`
--

CREATE TABLE `garages` (
  `uid` bigint(20) NOT NULL,
  `colormaps` longtext NOT NULL,
  `hulls` longtext NOT NULL,
  `inventory` longtext NOT NULL,
  `kits` longtext NOT NULL,
  `modules` longtext NOT NULL,
  `turrets` longtext NOT NULL,
  `effects` varchar(255) NOT NULL,
  `userid` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `garages`
--

INSERT INTO `garages` (`uid`, `colormaps`, `hulls`, `inventory`, `kits`, `modules`, `turrets`, `effects`, `userid`) VALUES
(1, '{\"colormaps\":[{\"id\":\"green\",\"modification\":0,\"mounted\":false},{\"id\":\"holiday\",\"modification\":0,\"mounted\":false},{\"id\":\"tiger\",\"modification\":0,\"mounted\":true}]}', '{\"hulls\":[{\"microUpgrades\":0,\"id\":\"hunter\",\"modification\":0,\"mounted\":false,\"microUpgradePrice\":100},{\"microUpgrades\":0,\"id\":\"viking\",\"modification\":3,\"mounted\":false,\"microUpgradePrice\":1500},{\"microUpgrades\":0,\"id\":\"hornet\",\"modification\":3,\"mounted\":true,\"microUpgradePrice\":1500}]}', '{\"inventory\":[{\"count\":9990,\"id\":\"health\"},{\"count\":9984,\"id\":\"armor\"},{\"count\":9995,\"id\":\"double_damage\"},{\"count\":9983,\"id\":\"n2o\"},{\"count\":9996,\"id\":\"mine\"}]}', '{\"kits\":[]}', '{\"modules\":[]}', '{\"turrets\":[{\"microUpgrades\":0,\"id\":\"smoky\",\"modification\":0,\"mounted\":false,\"microUpgradePrice\":100},{\"microUpgrades\":10,\"id\":\"ricochet\",\"modification\":3,\"mounted\":true,\"microUpgradePrice\":149655},{\"microUpgrades\":10,\"id\":\"flamethrower\",\"modification\":0,\"mounted\":false,\"microUpgradePrice\":3905}]}', '', ''),
(29, '{\"colormaps\":[{\"id\":\"holiday\",\"modification\":0,\"mounted\":false},{\"id\":\"green\",\"modification\":0,\"mounted\":true}]}', '{\"hulls\":[{\"microUpgrades\":0,\"id\":\"hunter\",\"modification\":0,\"mounted\":true,\"microUpgradePrice\":100}]}', '{\"inventory\":[]}', '{\"kits\":[]}', '{\"modules\":[{\"id\":\"standard\",\"modification\":0,\"mounted\":false}]}', '{\"turrets\":[{\"microUpgrades\":1,\"id\":\"smoky\",\"modification\":0,\"mounted\":true,\"microUpgradePrice\":908}]}', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `karma`
--

CREATE TABLE `karma` (
  `idkarma` bigint(20) NOT NULL,
  `chat_banned` bit(1) DEFAULT NULL,
  `chat_banned_before` datetime(6) DEFAULT NULL,
  `game_banned` bit(1) DEFAULT NULL,
  `game_banned_before` datetime(6) DEFAULT NULL,
  `reason_for_chat_ban` varchar(255) DEFAULT NULL,
  `reason_for_game_ban` varchar(255) DEFAULT NULL,
  `userid` varchar(255) NOT NULL,
  `banner_chat_user_id` varchar(255) DEFAULT NULL,
  `banner_game_user_id` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `karma`
--

INSERT INTO `karma` (`idkarma`, `chat_banned`, `chat_banned_before`, `game_banned`, `game_banned_before`, `reason_for_chat_ban`, `reason_for_game_ban`, `userid`, `banner_chat_user_id`, `banner_game_user_id`) VALUES
(1, b'0', NULL, b'0', NULL, NULL, NULL, 'Rengoku', 'Rengoku', NULL),
(2, b'0', NULL, b'0', NULL, NULL, NULL, 'test', NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `logs`
--

CREATE TABLE `logs` (
  `id` bigint(20) NOT NULL,
  `date` datetime(6) NOT NULL,
  `message` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `premium`
--

CREATE TABLE `premium` (
  `user_id` bigint(20) NOT NULL,
  `time` datetime(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `premium`
--

INSERT INTO `premium` (`user_id`, `time`) VALUES
(0, '2024-11-15 16:56:58.000000'),
(1, '2024-11-16 16:50:20.000000'),
(29, '2024-11-15 22:18:22.000000');

-- --------------------------------------------------------

--
-- Table structure for table `shot_effects_assortment`
--

CREATE TABLE `shot_effects_assortment` (
  `id` bigint(20) NOT NULL,
  `client_id` varchar(255) DEFAULT NULL,
  `desc_en` varchar(255) DEFAULT NULL,
  `desc_ru` varchar(255) DEFAULT NULL,
  `item_id` varchar(255) DEFAULT NULL,
  `price` int(11) DEFAULT NULL,
  `title_en` varchar(255) DEFAULT NULL,
  `title_ru` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `skins_assortment`
--

CREATE TABLE `skins_assortment` (
  `id` bigint(20) NOT NULL,
  `client_id` varchar(255) DEFAULT NULL,
  `desc_en` varchar(255) DEFAULT NULL,
  `desc_ru` varchar(255) DEFAULT NULL,
  `item_id` varchar(255) DEFAULT NULL,
  `price` int(11) DEFAULT NULL,
  `title_en` varchar(255) DEFAULT NULL,
  `title_ru` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `uid` bigint(20) NOT NULL,
  `crystalls` int(11) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `email_confirmation_code` varchar(255) DEFAULT NULL,
  `email_confirmed` bit(1) NOT NULL,
  `last_ip` varchar(255) NOT NULL,
  `last_issue_bonus` datetime(6) DEFAULT NULL,
  `next_score` int(11) NOT NULL,
  `nickname` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `place` int(11) NOT NULL,
  `rank` int(11) NOT NULL,
  `rating` int(11) NOT NULL,
  `score` int(11) NOT NULL,
  `user_type` int(11) NOT NULL,
  `deaths` int(11) NOT NULL,
  `purchasedFirstThing` bit(1) NOT NULL,
  `kd` double NOT NULL,
  `kills` int(11) NOT NULL,
  `wealth` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`uid`, `crystalls`, `email`, `email_confirmation_code`, `email_confirmed`, `last_ip`, `last_issue_bonus`, `next_score`, `nickname`, `password`, `place`, `rank`, `rating`, `score`, `user_type`, `deaths`, `purchasedFirstThing`, `kd`, `kills`, `wealth`) VALUES
(1, 48552779, '1', NULL, b'0', '/127.0.0.1:53390', NULL, 1600000, 'Rengoku', '1234', 0, 29, 1, 9127648, 1, 0, b'0', 0, 0, 0),
(28, 48920035, '1', NULL, b'0', '/127.0.0.1:59827', NULL, 1600000, 'Rengoku2', '1234', 0, 29, 1, 6048680, 3, 0, b'0', 0, 0, 0),
(29, 440, NULL, NULL, b'0', '/127.0.0.1:65139', NULL, 100, 'test', '1234', 0, 0, 1, 0, 0, 0, b'0', 0, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `user_container`
--

CREATE TABLE `user_container` (
  `container_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `count` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `user_shot_effects`
--

CREATE TABLE `user_shot_effects` (
  `user_id` bigint(20) NOT NULL,
  `shot_effect_id` bigint(20) NOT NULL,
  `mounted` bit(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `user_skins`
--

CREATE TABLE `user_skins` (
  `user_id` bigint(20) NOT NULL,
  `skin_id` bigint(20) NOT NULL,
  `mounted` bit(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `weekly_mission_user`
--

CREATE TABLE `weekly_mission_user` (
  `userId` bigint(20) NOT NULL,
  `changePrice` int(11) DEFAULT NULL,
  `missionId1` varchar(255) DEFAULT NULL,
  `missionId2` varchar(255) DEFAULT NULL,
  `missionId3` varchar(255) DEFAULT NULL,
  `missionProgr1` int(11) DEFAULT NULL,
  `missionProgr2` int(11) DEFAULT NULL,
  `missionProgr3` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `weekly_mission_user`
--

INSERT INTO `weekly_mission_user` (`userId`, `changePrice`, `missionId1`, `missionId2`, `missionId3`, `missionProgr1`, `missionProgr2`, `missionProgr3`) VALUES
(1, 0, 'captureTheFlag', 'complete_daily', 'complete_daily1', 0, 0, 0),
(29, 0, 'captureTheFlag', 'complete_daily', 'complete_daily1', 0, 0, 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `black_ips`
--
ALTER TABLE `black_ips`
  ADD PRIMARY KEY (`idblack_ips`);

--
-- Indexes for table `captcha`
--
ALTER TABLE `captcha`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `challenges_user`
--
ALTER TABLE `challenges_user`
  ADD PRIMARY KEY (`userId`);

--
-- Indexes for table `container_assortment`
--
ALTER TABLE `container_assortment`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `daily_mission_user`
--
ALTER TABLE `daily_mission_user`
  ADD PRIMARY KEY (`userId`);

--
-- Indexes for table `friends`
--
ALTER TABLE `friends`
  ADD PRIMARY KEY (`user_id`);

--
-- Indexes for table `garages`
--
ALTER TABLE `garages`
  ADD PRIMARY KEY (`uid`);

--
-- Indexes for table `karma`
--
ALTER TABLE `karma`
  ADD PRIMARY KEY (`idkarma`);

--
-- Indexes for table `logs`
--
ALTER TABLE `logs`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `premium`
--
ALTER TABLE `premium`
  ADD PRIMARY KEY (`user_id`);

--
-- Indexes for table `shot_effects_assortment`
--
ALTER TABLE `shot_effects_assortment`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `skins_assortment`
--
ALTER TABLE `skins_assortment`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`uid`);

--
-- Indexes for table `user_container`
--
ALTER TABLE `user_container`
  ADD PRIMARY KEY (`container_id`,`user_id`);

--
-- Indexes for table `user_shot_effects`
--
ALTER TABLE `user_shot_effects`
  ADD PRIMARY KEY (`user_id`,`shot_effect_id`);

--
-- Indexes for table `user_skins`
--
ALTER TABLE `user_skins`
  ADD PRIMARY KEY (`user_id`,`skin_id`);

--
-- Indexes for table `weekly_mission_user`
--
ALTER TABLE `weekly_mission_user`
  ADD PRIMARY KEY (`userId`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `black_ips`
--
ALTER TABLE `black_ips`
  MODIFY `idblack_ips` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `karma`
--
ALTER TABLE `karma`
  MODIFY `idkarma` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `logs`
--
ALTER TABLE `logs`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `uid` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
