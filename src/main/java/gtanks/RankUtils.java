/*
 * Decompiled with CFR 0.150.
 */
package gtanks;

import gtanks.Rank;

public class RankUtils {
    private static Rank[] ranks;
    public static int[] rankupRewards = { 0, 10, 40, 120, 230, 420, 740, 950, 1400, 2000, 2500, 3100, 3900, 4600, 5600,
            6600, 7900, 8900, 10000, 12000, 14000, 16000, 17000, 20000, 22000, 24000, 28000, 31000, 34000, 37000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000, 40000};

    public static void init() {
        ranks = new Rank[39];
        RankUtils.ranks[0] = new Rank(0, 99, "Recruit");
        RankUtils.ranks[1] = new Rank(100, 499, "Private");
        RankUtils.ranks[2] = new Rank(500, 1499, "Gefreiter");
        RankUtils.ranks[3] = new Rank(1500, 3699, "Corporal");
        RankUtils.ranks[4] = new Rank(3700, 7099, "Master Corporal");
        RankUtils.ranks[5] = new Rank(7100, 12299, "Sergeant");
        RankUtils.ranks[6] = new Rank(12300, 19999, "Staff Sergeant");
        RankUtils.ranks[7] = new Rank(20000, 28999, "Master Sergeant");
        RankUtils.ranks[8] = new Rank(29000, 40999, "First Sergeant");
        RankUtils.ranks[9] = new Rank(41000, 56999, "Sergeant-Major");
        RankUtils.ranks[10] = new Rank(57000, 75999, "Warrant Officer 1");
        RankUtils.ranks[11] = new Rank(76000, 97999,  "Warrant Officer 2");
        RankUtils.ranks[12] = new Rank(98000, 124999, "Warrant Officer 3");
        RankUtils.ranks[13] = new Rank(125000, 155999, "Warrant Officer 4");
        RankUtils.ranks[14] = new Rank(156000, 191999, "Warrant Officer 5");
        RankUtils.ranks[15] = new Rank(192000, 232999, "Third Lieutenant");
        RankUtils.ranks[16] = new Rank(233000, 279999, "Second Lieutenant");
        RankUtils.ranks[17] = new Rank(280000, 331999, "First Lieutenant");
        RankUtils.ranks[18] = new Rank(332000, 389999, "Captain");
        RankUtils.ranks[19] = new Rank(390000, 454999, "Major");
        RankUtils.ranks[20] = new Rank(455000, 526999, "Lieutenant Colonel");
        RankUtils.ranks[21] = new Rank(527000, 605999, "Colonel");
        RankUtils.ranks[22] = new Rank(606000, 691999, "Brigadier");
        RankUtils.ranks[23] = new Rank(692000, 786999,"Major General");
        RankUtils.ranks[24] = new Rank(787000, 888999,"Lieutenant General");
        RankUtils.ranks[25] = new Rank(889000, 999999, "General");
        RankUtils.ranks[26] = new Rank(1000000, 1121999, "Marshal");
        RankUtils.ranks[27] = new Rank(1122000, 1254999, "Field Marshal");
        RankUtils.ranks[28] = new Rank(1255000, 1399999, "Commander");
        RankUtils.ranks[29] = new Rank(1400000, 1599999, "Generalissimo");
        RankUtils.ranks[30] = new Rank(1600000, 1799999, "Legend");
        RankUtils.ranks[31] = new Rank(1800000, 1999999, "Legend 2");
        RankUtils.ranks[32] = new Rank(2000000, 2199999, "Legend 3");
        RankUtils.ranks[33] = new Rank(2200000, 2399999, "Legend 4");
        RankUtils.ranks[34] = new Rank(2400000, 2599999, "Legend 5");
        RankUtils.ranks[35] = new Rank(2600000, 2799999, "Legend 6");
        RankUtils.ranks[36] = new Rank(3000000, 3199999, "Legend 7");
        RankUtils.ranks[37] = new Rank(3200000, 3399999, "Legend 8");
        RankUtils.ranks[38] = new Rank(3400000, 3600000, "Legend 9");
    }

    public static int getUpdateNumber(int score) {
        int rangId;
        Rank temp = RankUtils.getRankByScore(score);
        int rang = rangId = RankUtils.getNumberRank(temp);
        int result = 0;
        try {
            result = (int) ((double) (score - RankUtils.ranks[rang - 1].max) * 1.0
                    / (double) (temp.max - RankUtils.ranks[rang - 1].max) * 10000.0);
        } catch (Exception e) {
            result = (int) ((double) (score - 0) * 1.0 / (double) (temp.max - 0) * 10000.0);
        }
        if (score > RankUtils.ranks[RankUtils.ranks.length - 1].min - 1) {
            result = 10000;
        } else if (score < 0) {
            result = 0;
        }
        return result;
    }

    public static int getNumberRank(Rank rank) {
        for (int i = 0; i < ranks.length; ++i) {
            if (ranks[i] != rank)
                continue;
            return i;
        }
        return -1;
    }

    public static Rank getRankByScore(int score) {
        Rank temp = ranks[0];
        if (score >= RankUtils.ranks[29].max) {
            temp = ranks[29];
        }
        Rank[] arrrank = ranks;
        int n = ranks.length;
        for (int i = 0; i < n; ++i) {
            Rank rank = arrrank[i];
            if (score < rank.min || score > rank.max)
                continue;
            temp = rank;
        }
        return temp;
    }

    public static Rank getRankByIndex(int index) {
        return ranks[index];
    }

    public static int stringToInt(String src) {
        try {
            int tempelate = Integer.parseInt(src);
            if (tempelate <= 0) {
                tempelate = 5000000;
            }
            return tempelate >= RankUtils.ranks[29].min ? RankUtils.ranks[29].min : tempelate;
        } catch (Exception ex) {
            return 50000000;
        }
    }
}
