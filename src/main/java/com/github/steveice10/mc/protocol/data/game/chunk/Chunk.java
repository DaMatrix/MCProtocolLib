package com.github.steveice10.mc.protocol.data.game.chunk;

import com.github.steveice10.mc.protocol.util.ObjectUtil;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;

import java.util.Arrays;

public class Chunk {
    private int x;
    private int z;
    private Section sections[];
    private byte biomeData[];
    private CompoundTag tileEntities[];

    private boolean skylight;

    public Chunk(int x, int z, Section sections[], CompoundTag[] tileEntities) {
        this(x, z, sections, null, tileEntities);
    }

    public Chunk(int x, int z, Section sections[], byte biomeData[], CompoundTag[] tileEntities) {
        if(sections.length != 16) {
            throw new IllegalArgumentException("Chunk array length must be 16.");
        }

        if(biomeData != null && biomeData.length != 256) {
            throw new IllegalArgumentException("Biome data array length must be 256.");
        }

        this.skylight = false;
        boolean noSkylight = false;
        for(Section section : sections) {
            if(section != null) {
                if(section.getSkyLight() == null) {
                    noSkylight = true;
                } else {
                    this.skylight = true;
                }
            }
        }

        if(noSkylight && this.skylight) {
            throw new IllegalArgumentException("Either all chunks must have skylight values or none must have them.");
        }

        this.x = x;
        this.z = z;
        this.sections = sections;
        this.biomeData = biomeData;
        this.tileEntities = tileEntities != null ? tileEntities : new CompoundTag[0];
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }

    public Section[] getChunks() {
        return this.sections;
    }

    public boolean hasBiomeData() {
        return this.biomeData != null;
    }

    public byte[] getBiomeData() {
        return this.biomeData;
    }

    public CompoundTag[] getTileEntities() {
        return this.tileEntities;
    }

    public boolean hasSkylight() {
        return this.skylight;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof Chunk)) return false;

        Chunk that = (Chunk) o;
        return this.x == that.x &&
                this.z == that.z &&
               Arrays.equals(this.sections, that.sections) &&
               Arrays.equals(this.biomeData, that.biomeData) &&
               Arrays.equals(this.tileEntities, that.tileEntities);
    }

    @Override
    public int hashCode() {
        return ObjectUtil.hashCode(this.x, this.z, this.sections, this.biomeData, this.tileEntities);
    }

    @Override
    public String toString() {
        return ObjectUtil.toString(this);
    }
}
