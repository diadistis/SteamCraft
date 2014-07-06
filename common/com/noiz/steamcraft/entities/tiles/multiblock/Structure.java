package com.noiz.steamcraft.entities.tiles.multiblock;

import net.minecraftforge.common.ForgeDirection;

class Structure {
	final int[] minCoords;
	final int[] maxCoords;

	Structure(int[] minCoords, int[] maxCoords) {
		this.minCoords = minCoords;
		this.maxCoords = maxCoords;
	}

	boolean isValid(int minLength, int maxLength) {
		if (minCoords == null || minCoords.length != 3)
			return false;
		if (maxCoords == null || maxCoords.length != 3)
			return false;

		for (int i = 0; i < 3; ++i) {
			if (maxCoords[i] < minCoords[i])
				return false;

			int d = maxCoords[i] - minCoords[i];
			if (d < minLength || d > maxLength)
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
}
