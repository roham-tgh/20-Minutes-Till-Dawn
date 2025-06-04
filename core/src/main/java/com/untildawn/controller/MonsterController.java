package com.untildawn.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.untildawn.Main;
import com.untildawn.model.*;

import java.util.ArrayList;
import java.util.Random;

public class MonsterController {
    private final ArrayList<Monster> monsters;
    private Texture monsterTexture;
    private float eyebatSpawnTimer = 0f;
    private float brainEaterSpawnTimer = 0f;
    private final Random random;
    private final ArrayList<HitEffect> hitEffects = new ArrayList<>();
    private float timePassed;
    private float totalGameTime;
    private final static float BRAIN_SPAWN_TIME = 3f;
    private final static float EYEBAT_SPAWN_TIME = 10f;

    public MonsterController(float totalGameTime) {
        monsters = new ArrayList<>();
        random = new Random();
        this.totalGameTime = totalGameTime;
    }

    public void update(float delta, Player player, ArrayList<Bullet> bullets) {
        eyebatSpawnTimer += delta;
        brainEaterSpawnTimer += delta;
        spawnMonster(player.position);
        for (Monster monster : monsters) {
            if (monster == null || monster.getCollisionRect() == null) continue; // Skip null entries
            if (monster instanceof Eyebat && ((Eyebat) monster).isShooting()) {
                monster.update(delta, player);
                handleBulletCollisions(monster, bullets);
                monster.draw();
                continue;
            }
            // Calculate separation force
            Vector2 separation = new Vector2();
            int neighborCount = 0;
            float separationRadius = 32f; // Adjust based on monster size

            for (Monster other : monsters) {
                if (other == null || monster == other || other.getCollisionRect() == null)
                    continue; // Skip null or self

                float distance = monster.position.dst(other.position);
                if (distance < separationRadius) {
                    Vector2 diff = new Vector2(monster.position).sub(other.position);
                    diff.nor().scl(1f / Math.max(distance, 0.1f)); // Stronger separation when closer
                    separation.add(diff);
                    neighborCount++;
                }
            }

            // Calculate direction to player
            Vector2 targetDirection = new Vector2(player.position).sub(player.getWidth() / 2, player.getHeight() / 2).sub(monster.position).nor();

            // Apply separation if there are neighbors
            if (neighborCount > 0) {
                separation.scl(1f / neighborCount);
                separation.nor();

                // Blend separation with player-following behavior
                Vector2 finalDirection = new Vector2(targetDirection).scl(0.6f).add(separation.scl(0.4f));
                finalDirection.nor();

                // Update monster position
                monster.position.add(finalDirection.scl(monster.getSpeed() * delta));
            } else {
                // No neighbors, just move towards player
                monster.position.add(targetDirection.scl(monster.getSpeed() * delta));
            }

            monster.update(delta, player);
            handleBulletCollisions(monster, bullets);
            monster.draw();
            for (int i = hitEffects.size() - 1; i >= 0; i--) {
                HitEffect effect = hitEffects.get(i);
                effect.update(delta);
                effect.draw(Main.getBatch());
                if (effect.isFinished()) {
                    hitEffects.remove(i);
                }
            }
        }

        monsters.removeIf(monster -> !monster.isActive());
    }

    private void handleBulletCollisions(Monster monster, ArrayList<Bullet> bullets) {
        for (Bullet bullet : bullets) {
            if (bullet.getCollisionRect().overlaps(monster.getCollisionRect())) {
                hitEffects.add(new HitEffect(
                    bullet.getPosition().cpy(),
                    AssetManager.getAssetManager().getBulletHitEffect()
                ));
                SoundSystem.getInstance().playSound("enemyHit");
                monster.takeDamage(bullet.getDamage());
                bullet.deactivate();
            }
        }
    }


    private void spawnMonster(Vector2 playerPosition) {
        // Pick a random corner


        if (brainEaterSpawnTimer >= BRAIN_SPAWN_TIME) {
            for (int i = 0; i <= MyTime.getInstance().getTimePassed() / 30 / 30; i++) {
                Vector2 position = getRandomPosition().add(playerPosition);
                monsters.add(new BrainEater(position.x, position.y));
            }
            brainEaterSpawnTimer = 0f;
        }
//        if (timePassed >= totalGameTime / 4  && eyebatSpawnTimer >= EYEBAT_SPAWN_TIME) {
//            for (int i = 0; i <= (4 * timePassed - totalGameTime) / 30; i++) {
//                Vector2 position = getRandomPosition().add(playerPosition);
//                monsters.add(new Eyebat(position.x, position.y));
//            }
//            eyebatSpawnTimer = 0f;
//        }
        if (eyebatSpawnTimer >= EYEBAT_SPAWN_TIME) {
            for (int i = 0; i <= MyTime.getInstance().getTimePassed() / 30; i++) {
                Vector2 position = getRandomPosition().add(playerPosition);
                monsters.add(new Eyebat(position.x, position.y));
            }
            eyebatSpawnTimer = 0f;
        }

    }

    private Vector2 getRandomPosition() {
        float margin = 32f; // Distance outside the screen
        float x = 0, y = 0;
        int edge = random.nextInt(4); // 0=left, 1=right, 2=top, 3=bottom
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        switch (edge) {
            case 0: // Left
                x = -margin;
                y = random.nextFloat() * screenHeight;
                break;
            case 1: // Right
                x = screenWidth + margin;
                y = random.nextFloat() * screenHeight;
                break;
            case 2: // Top
                x = random.nextFloat() * screenWidth;
                y = screenHeight + margin;
                break;
            case 3: // Bottom
                x = random.nextFloat() * screenWidth;
                y = -margin;
                break;
        }
        // Center the coordinates relative to (0,0) if needed
        x -= screenWidth / 2f;
        y -= screenHeight / 2f;
        return new Vector2(x, y);
    }


    public void dispose() {
        monsterTexture.dispose();
    }

    public Vector2 getNearestMonsterPosition() {
        if (monsters.isEmpty()) {
            return null; // No monsters available
        }
        float minDist = Float.MAX_VALUE;
        Vector2 nearestPos = null;
        Vector2 playerPos = Player.getPlayer().position;

        for (Monster monster : monsters) {
            if (monster == null || !monster.isActive()) continue;

            float dist = monster.position.dst2(playerPos); // Using dst2 to avoid square root calculation
            if (dist < minDist) {
                minDist = dist;
                nearestPos = monster.position;
            }
        }

        return nearestPos;
    }
}
