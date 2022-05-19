package de.illigalspigot.neoprotect.spigot.consolefilter;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.message.Message;

public class OldEngine1 implements Filter {

	OldEngine oldengine;

	OldEngine1(OldEngine paramOldEngine) {
		oldengine = paramOldEngine;
	}

	public Filter.Result filter(LogEvent event) {
		if (!OldEngine.access$0(oldengine).getInstance().filterConsoleMessages.isEmpty()) {
			for (String s : OldEngine.access$0(oldengine).getInstance().filterConsoleMessages) {
				if (event.getMessage().toString().contains(s)) {
					OldEngine tmp77_74 = oldengine;
					OldEngine.access$2(tmp77_74, OldEngine.access$1(tmp77_74) + 1);
					return Filter.Result.DENY;
				}
			}
		}
		return null;
	}

	public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, String arg3, Object... arg4) {
		return null;
	}

	public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, Object arg3, Throwable arg4) {
		return null;
	}

	public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, Message arg3, Throwable arg4) {
		return null;
	}

	public Filter.Result getOnMatch() {
		return null;
	}

	public Filter.Result getOnMismatch() {
		return null;
	}
}
