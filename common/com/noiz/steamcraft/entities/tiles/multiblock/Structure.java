package com.noiz.steamcraft.entities.tiles.multiblock;

import java.util.Arrays;

import net.minecraftforge.common.ForgeDirection;

class Structure {
	final int[] minCoords;
	final int[] maxCoords;

	Structure(int[] minCoords, int[] maxCoords) {
		this.minCoords = minCoords;
		this.maxCoords = maxCoords;
	}

	boolean isValid(int[] limits) {
		System.out.println(limits == null ? "--limitless--" : Arrays.toString(limits));
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
		return (maxCoords[0] - minCoords[0] + 1) * (maxCoords[1] - minCoords[1] + 1) * (maxCoords[2] - minCoords[2] + 1);
	}
}
