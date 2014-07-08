package com.noiz.ti.entities.tiles.multiblock;

import net.minecraftforge.common.ForgeDirection;

class Structure {
	final int[] minCoords;
	final int[] maxCoords;

	private final int block_count;
	private final int base_area;
	private final int[] center_coords;
	private final int radius;

	Structure(int[] minCoords, int[] maxCoords) {
		this.minCoords = minCoords;
		this.maxCoords = maxCoords;

		this.block_count = (maxCoords[0] - minCoords[0] + 1) * (maxCoords[1] - minCoords[1] + 1) * (maxCoords[2] - minCoords[2] + 1);
		this.base_area = (maxCoords[0] - minCoords[0] + 1) * (maxCoords[2] - minCoords[2] + 1);
		this.center_coords = new int[] { (minCoords[0] + maxCoords[0]) / 2, (minCoords[1] + maxCoords[1]) / 2, (minCoords[2] + maxCoords[2]) / 2, };
		this.radius = Math.max(Math.abs(center_coords[0] - minCoords[0]), Math.max(Math.abs(center_coords[1] - minCoords[1]), Math.abs(center_coords[2] - minCoords[2])));
	}

	boolean isValid(int[] limits) {
		if (minCoords == null || minCoords.length != 3)
			return false;
		if (maxCoords == null || maxCoords.length != 3)
			return false;

		for (int i = 0; i < 3; ++i) {
			int d = maxCoords[i] - minCoords[i] + 1;
			int max = limits == null ? TileEntityRectMultiblock.MaxScanDistance : limits[i];
			if (d < 1 || d > max)
				return false;
		}

		return true;
	}

	int[] masterCoords() {
		return minCoords;
	}

	void fillTouchedSides(TileEntityRectMultiblock member) {
		if (member.xCoord != minCoords[0])
			member.multiblockInternalDirections.add(ForgeDirection.WEST);
		if (member.xCoord != maxCoords[0])
			member.multiblockInternalDirections.add(ForgeDirection.EAST);
		if (member.zCoord != minCoords[2])
			member.multiblockInternalDirections.add(ForgeDirection.NORTH);
		if (member.zCoord != maxCoords[2])
			member.multiblockInternalDirections.add(ForgeDirection.SOUTH);
	}

	int blockCount() {
		return block_count;
	}

	int baseArea() {
		return base_area;
	}

	int[] centerCoordinates() {
		return center_coords;
	}

	int radius() {
		return radius;
	}

	int touchingHorizontalArea(Structure other) {
		int x = Math.min(maxCoords[0], other.maxCoords[0]) - Math.max(minCoords[0], other.minCoords[0]) + 1;
		int z = Math.min(maxCoords[2], other.maxCoords[2]) - Math.max(minCoords[2], other.minCoords[2]) + 1;
		return x * z;
	}
}
