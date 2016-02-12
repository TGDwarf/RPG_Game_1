package GAME;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by dot on 12-02-2016.
 */
@SuppressWarnings("all")
public class Enemy {
    private String name;
    private int level, maxHp, maxAtk, maxDef, hp, atk, def;
    private boolean special;
    // script <Threshold,Text>
    private Map<Float, String> script;
    private int baseCrit, crit;
    private double baseCritD, critD;

    public Enemy(String name) {
        this.name = name;
        if (name.equals("tutorial")) {
            // level = (int) Math.random()
            maxHp = hp = 10;
            maxAtk = atk = 1;
            maxDef = def = 0;
            baseCrit = crit = 10;
            baseCritD = critD = 1.5;
            special = true;
            script = new TreeMap<Float, String>();
            script.put(1.1f, "Tutorial enemy found. Initiating battle.");
            script.put(1f, dispStats());
        }

    }

    public Enemy(int playerLevel) {
        Map<String, Integer> enemyChance = new HashMap<>();
        enemyChance.put("slime", 5);
        if (playerLevel >= 5)
            enemyChance.put("frog", 10);
        if (playerLevel >= 10)
            enemyChance.put("skeleton", 20);
        if (playerLevel >= 15)
            enemyChance.put("killer dog", 25);
        if (playerLevel >= 20)
            enemyChance.put("farthest boss", 30);
        int chance = 0;
        for (int x : enemyChance.values())
            chance += x;
        chance = rInt(chance);
        Object[] ar = enemyChance.values().toArray();
        int index = ar.length - 1;
        while (chance > 0 && index > 0) {
            chance -= (int) ar[index--];
        }
        Object[] names = enemyChance.keySet().toArray();
        String enName = (String) names[index];
        this.name = enName;
        switch (enName) {
            case "slime":
                maxHp = hp = 10;
                maxAtk = atk = 1;
                maxDef = def = 0;
                baseCrit = crit = 10;
                baseCritD = critD = 1.5;
                special = false;
                break;
            case "frog":
                maxHp = hp = 15;
                maxAtk = atk = 3;
                maxDef = def = 3;
                baseCrit = crit = 20;
                baseCritD = critD = 1.75;
                special = false;
                break;
            case "skeleton":
                maxHp = hp = 25;
                maxAtk = atk = 5;
                maxDef = def = 7;
                baseCrit = crit = 30;
                baseCritD = critD = 1.75;
                special = false;
                break;
            case "killer dog":
                maxHp = hp = 40;
                maxAtk = atk = 8;
                maxDef = def = 12;
                baseCrit = crit = 40;
                baseCritD = critD = 2;
                special = false;
                break;
            case "farthest boss":
                maxHp = hp = 100;
                maxAtk = atk = 20;
                maxDef = def = 15;
                baseCrit = crit = 50;
                baseCritD = critD = 2.5;
                special = true;
                script = new TreeMap<Float, String>();
                // put them in the opposite order you want the script to be
                // displayed
                script.put(1.1f, "*lunges at you*");
                script.put(1.2f, "NOW!");
                script.put(1.3f, "Sadly you won't be able to beat me.");
                script.put(1.4f, "So you've finally reached me. Congratulations.");
        }
    }

    public Enemy(String name, int playerLevel) {
        this.name = name;
        maxHp = hp = 10;
        maxAtk = atk = 1;
        maxDef = def = 0;
        baseCrit = crit = 10;
        baseCritD = critD = 1.5;
        special = false;
        script = new TreeMap<Float, String>();
        script.put(1f, dispStats());

    }

    public Enemy(String name, int baseHP, int baseATK, int baseDEF, int baseCRIT, double baseCRITD, boolean spec,
                 Map<Float, String> messages) {
        this.name = name;
        maxHp = hp = baseHP;
        maxAtk = atk = baseATK;
        maxDef = def = baseDEF;
        baseCrit = crit = baseCRIT;
        baseCritD = critD = baseCRITD;
        special = spec;
        script = messages;
    }

    public static int getRandPercent() {
        return (int) (Math.random() * 100) + 1;
    }

    public static int rInt(int num) {
        return (int) (Math.random() * num);
    }

    public String dispStats() {
        return "HP: " + hp + " ATK: " + atk + " DEF: " + def;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getHP() {
        return hp;
    }

    public int getAtk() {
        return atk;
    }

    public int getDef() {
        return def;
    }

    public int getCrit() {
        return crit;
    }

    public double getCritD() {
        return critD;
    }

    public int getXp() {
        int getXp = level + (atk * 3) + (def * 3) + (hp);
        return getXp > 0 ? getXp : 1;
    }

    public int changeHP(int change) {
        return hp += change;
    }

    public boolean isSpecial() {
        return special;
    }

    public void runScript() {

        Iterator<Map.Entry<Float, String>> entries = script.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Float, String> entry = entries.next();
            if (entry.getKey() >= getPercentHp()) {
                System.out.println(entry.getValue());
                entries.remove();
            }
        }
    }

    private float getPercentHp() {
        return hp / maxHp;
    }

    private void delay(long num) {
        try {
            Thread.sleep(num);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int hit(int playerLevel, int playerDef) {
        int tempCrit = crit + playerLevel / 2;
        int dmg = atk - playerDef / 2;
        if (getRandPercent() <= tempCrit) {
            System.out.println("Enemy critical hit. Damage multiplier is " + String.format("%.2f", critD));
            dmg = (int) (dmg * critD);
        }
        return dmg;
    }
}
