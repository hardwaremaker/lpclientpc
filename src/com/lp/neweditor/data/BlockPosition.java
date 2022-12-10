package com.lp.neweditor.data;

public class BlockPosition implements Comparable<BlockPosition>{
	public static final BlockPosition INVALID = new BlockPosition(-1, -1);
	public final int x, y;

	public BlockPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public boolean isValidPosition() {
		return (x >= 0) && (y >= 0);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
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
		BlockPosition other = (BlockPosition) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public int compareTo(BlockPosition o) {
		//Sortiere nach y. Bei y gleich, dann nach x
		int compY = Integer.compare(this.y, o.y);
		if(compY == 0) {
			return Integer.compare(this.x, o.x);
		}
		return compY;
	}
	
	@Override
	public String toString() {
		return String.format("(x=%d,y=%d)", x, y);
	}
}
