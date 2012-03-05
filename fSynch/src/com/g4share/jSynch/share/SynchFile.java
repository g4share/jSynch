package com.g4share.jSynch.share;

import java.util.Objects;

/**
 * User: gm
 * Date: 3/5/12
 */
public class SynchFile {
    private String name;
    private long size;

    public SynchFile(String name, long size) {
        this.name = name;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;

        if (getClass() != obj.getClass()) return false;

        final SynchFile other = (SynchFile) obj;

        return this.name.equals(other.name)
                && this.size != other.size;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.name);
        hash = 37 * hash + (int) (this.size ^ (this.size >>> 32));
        return hash;
    }
}
