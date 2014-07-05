package com.noiz.steamcraft.entities.tiles;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import TFC.TileEntities.NetworkTileEntity;

public class NetTileEntity extends NetworkTileEntity {

	@Override
	public void handleDataPacket(DataInputStream inStream) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleDataPacketServer(DataInputStream inStream)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createInitPacket(DataOutputStream outStream) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleInitPacket(DataInputStream inStream) throws IOException {
		// TODO Auto-generated method stub
		
	}

}
