package de.illigalspigot.neoprotect.spigot.consolefilter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import de.illigalspigot.neoprotect.spigot.NeoProtect;

public class OldEngine implements EngineInterface {
	
	   private NeoProtect csf;
	   private int msgHidden = 0;

	   public OldEngine(NeoProtect csf) {
	      this.csf = csf;
	   }

	   public int getHiddenMessagesCount() {
	      return this.msgHidden;
	   }

	   public void addHiddenMsg() {
	      ++this.msgHidden;
	   }

	   public void hideConsoleMessages() {
	      ((Logger)LogManager.getRootLogger()).addFilter(new OldEngine1(this));
	   }

	   // $FF: synthetic method
	   static NeoProtect access$0(OldEngine var0) {
	      return var0.csf;
	   }

	   // $FF: synthetic method
	   static int access$1(OldEngine var0) {
	      return var0.msgHidden;
	   }

	   // $FF: synthetic method
	   static void access$2(OldEngine var0, int var1) {
	      var0.msgHidden = var1;
	   }
	}