/*
 * Copyright (C) 2014 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.commands;

import net.wurstclient.command.CmdException;
import net.wurstclient.command.CmdSyntaxError;
import net.wurstclient.command.Command;
import net.wurstclient.events.UpdateListener;

public final class DropCmd extends Command implements UpdateListener
{
	private int slowModeTimer;
	private int slowModeSlotCounter;
	
	public DropCmd()
	{
		super("drop", "Drops all your items on the ground.", ".drop",
			"Slow mode: .drop slow",
			"If regular .drop kicks you from the server,",
			"use slow mode instead.");
	}
	
	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length > 1)
			throw new CmdSyntaxError();
		
		if(args.length < 1)
		{
			dropAllItems();
			return;
		}
		
		if(!args[0].equalsIgnoreCase("slow"))
			throw new CmdSyntaxError();
		
		slowModeTimer = 5;
		slowModeSlotCounter = 9;
		WURST.getEventManager().add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		slowModeTimer--;
		if(slowModeTimer > 0)
			return;
		
		IMC.getInteractionManager().windowClick_THROW(slowModeSlotCounter);
		slowModeSlotCounter++;
		slowModeTimer = 5;
		
		if(slowModeSlotCounter >= 45)
			WURST.getEventManager().remove(UpdateListener.class, this);
	}
	
	private void dropAllItems()
	{
		for(int i = 9; i < 45; i++)
			IMC.getInteractionManager().windowClick_THROW(i);
	}
}
