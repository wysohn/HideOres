package com.hideores.cache;

public class ChunkCoord {
	private String world;
	private int x;
	private int z;
	public ChunkCoord(String world, int x, int z) {
		this.world = world;
		this.x = x;
		this.z = z;
	}
	public String getWorld() {
		return world;
	}
	public int getX() {
		return x;
	}
	public int getZ() {
		return z;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((world == null) ? 0 : world.hashCode());
		result = prime * result + x;
		result = prime * result + z;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChunkCoord other = (ChunkCoord) obj;
		if (world == null) {
			if (other.world != null)
				return false;
		} else if (!world.equals(other.world))
			return false;
		if (x != other.x)
			return false;
		if (z != other.z)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ChunkCoord [world=" + world + ", x=" + x + ", z=" + z + "]";
	}
}
