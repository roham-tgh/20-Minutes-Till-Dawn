package com.untildawn.controller;

import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Random;

public class MapController {
    public static final int CHUNK_SIZE = 16; // Size of each chunk in tiles
    public static final int TILE_SIZE = 32 ;  // Size of each tile in pixels
    private final HashMap<Long, Chunk> chunks; // Stores loaded chunks
    private final long seed;                  // Seed for consistent generation

    public MapController(long seed) {
        this.chunks = new HashMap<>();
        this.seed = seed;
    }

    public void update(Vector2 playerPosition) {
        // Convert player position to chunk coordinates
        int chunkX = Math.floorDiv((int)playerPosition.x, CHUNK_SIZE * TILE_SIZE);
        int chunkY = Math.floorDiv((int)playerPosition.y, CHUNK_SIZE * TILE_SIZE);

        // Load chunks in view distance
        for (int x = chunkX - 1; x <= chunkX + 1; x++) {
            for (int y = chunkY - 1; y <= chunkY + 1; y++) {
                long chunkKey = getChunkKey(x, y);
                if (!chunks.containsKey(chunkKey)) {
                    chunks.put(chunkKey, generateChunk(x, y));
                }
            }
        }

        // Unload distant chunks
        chunks.entrySet().removeIf(entry -> {
            Vector2 chunkPos = getChunkPosition(entry.getKey());
            return Math.abs(chunkPos.x - chunkX) > 2 || Math.abs(chunkPos.y - chunkY) > 2;
        });
    }

    private Chunk generateChunk(int chunkX, int chunkY) {
        Chunk chunk = new Chunk(CHUNK_SIZE);
        Random random = new Random(seed + getChunkKey(chunkX, chunkY));

        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int y = 0; y < CHUNK_SIZE; y++) {
                // Generate consistent tile type based on position and seed
                chunk.tiles[x][y] = random.nextInt(9); // 0-3 for different tile types
            }
        }

        return chunk;
    }

    private long getChunkKey(int x, int y) {
        return ((long)x << 32) | (y & 0xFFFFFFFFL);
    }

    private Vector2 getChunkPosition(long key) {
        return new Vector2((int)(key >> 32), (int)key);
    }

    private static class Chunk {
        final int[][] tiles;

        Chunk(int size) {
            tiles = new int[size][size];
        }
    }

    public int getTileAt(float worldX, float worldY) {
        int chunkX = Math.floorDiv((int)worldX, CHUNK_SIZE * TILE_SIZE);
        int chunkY = Math.floorDiv((int)worldY, CHUNK_SIZE * TILE_SIZE);

        int localX = Math.floorMod((int)worldX, CHUNK_SIZE * TILE_SIZE) / TILE_SIZE;
        int localY = Math.floorMod((int)worldY, CHUNK_SIZE * TILE_SIZE) / TILE_SIZE;

        Chunk chunk = chunks.get(getChunkKey(chunkX, chunkY));
        return chunk != null ? chunk.tiles[localX][localY] : -1;
    }


}
